package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.dialog.CurrentTripMemberDialog;
import vn.edu.hcmut.wego.dialog.CurrentTripPlaceDialog;
import vn.edu.hcmut.wego.dialog.CurrentTripSearchDialog;
import vn.edu.hcmut.wego.dialog.CurrentTripWarningDialog;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.Trip;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.DrawPath;
import vn.edu.hcmut.wego.utility.DrawPath.DrawCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CurrentTripActivity extends ActionBarActivity {

	private GoogleMap map;

	private int userId;
	private int tripId;
	private Trip trip;

	private DrawPath currentPath;

	private boolean isFindRoute = false;
	private boolean isDoneLoading = false;
	private boolean isWarning = false;
	private boolean isWarnRoute = false;

	private Marker memberMarker = null;
	private Marker warningMarker = null;
	

	private BroadcastReceiver locationReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String message = intent.getStringExtra("message");

			String[] contents = message.split("\\|");
			int senderId = Integer.parseInt(contents[1]);
			String senderName = "";
			double latitude = Double.parseDouble(contents[2].split(",")[0].trim());
			double longitude = Double.parseDouble(contents[2].split(",")[1].trim());

			for (int i = 0; i < trip.getMembers().size(); i++) {
				if (trip.getMembers().get(i).getId() == senderId) {
					senderName = trip.getMembers().get(i).getName();
					break;
				}
			}
			if (memberMarker != null) {
				memberMarker.remove();
			}
			MarkerOptions markerOption = new MarkerOptions().position(new LatLng(latitude, longitude)).title(senderName);
			memberMarker = map.addMarker(markerOption);
			LatLng myLatLng = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
			LatLng warnLatLng= new LatLng(latitude, longitude);
			if (myLatLng.latitude <= warnLatLng.latitude && myLatLng.longitude <= warnLatLng.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(myLatLng, warnLatLng), 1));
			else if (myLatLng.latitude <= warnLatLng.latitude && myLatLng.longitude >= warnLatLng.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(myLatLng.latitude, warnLatLng.longitude), new LatLng(warnLatLng.latitude, myLatLng.longitude)), 1));
			else if (myLatLng.latitude >= warnLatLng.latitude && myLatLng.longitude <= warnLatLng.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(warnLatLng.latitude, myLatLng.longitude), new LatLng(myLatLng.latitude, warnLatLng.longitude)), 1));
			else
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(warnLatLng.latitude, warnLatLng.longitude), new LatLng(myLatLng.latitude, myLatLng.longitude)), 1));
		//	map.animateCamera(CameraUpdateFactory.zoomOut());
			Log.i("Debug", String.valueOf(latitude) + "," + String.valueOf(longitude));

			// TODO: Make this location of this user on map, move camera to this location, show name.
		}
	};

	private IntentFilter filter = new IntentFilter(Constant.BROADCAST_LOCATION_ACTION);;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_trip);
		filter.setPriority(1);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		userId = Utils.getUserId(this);

		Intent intent = getIntent();
		tripId = intent.getExtras().getInt("tripId");
		isWarning = intent.getBooleanExtra("isWarning", false);

		// Get views
		ImageButton memberButton = (ImageButton) findViewById(R.id.activity_current_trip_member);
		ImageButton placesButton = (ImageButton) findViewById(R.id.activity_current_trip_places);
		ImageButton warningButton = (ImageButton) findViewById(R.id.activity_current_trip_warning);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.activity_current_trip_map);

		// Set up button actions
		memberButton.setOnClickListener(actionListener);
		placesButton.setOnClickListener(actionListener);
		warningButton.setOnClickListener(actionListener);

		// Set up map
		map = mapFragment.getMap();
		map.setMyLocationEnabled(true);

		LoadDestination();

	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(locationReceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(locationReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.current_trip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.activity_current_trip_search:
			// Log.d("anaasd",String.valueOf(map.getMyLocation().getLatitude()));
			if (isDoneLoading)
				CurrentTripSearchDialog.create(this, getSupportFragmentManager(), findRoute, map.getMyLocation());
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (isFindRoute || isWarnRoute) {
			if(isFindRoute)isFindRoute = !isFindRoute;
			if(isWarnRoute)isWarnRoute = !isWarnRoute;
			
			map.clear();
			map.addPolyline(currentPath.getPolyOption());
			for (int i = 0; i < currentPath.getMarkerOptionList().size(); i++) {
				map.addMarker(currentPath.getMarkerOptionList().get(i));
			}
			LatLng origin = currentPath.getMarkerPoints().get(0);
			LatLng dest = currentPath.getMarkerPoints().get(currentPath.getMarkerPoints().size() - 1);
			if (origin.latitude <= dest.latitude && origin.longitude <= dest.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(origin, dest), 0));
			else if (origin.latitude <= dest.latitude && origin.longitude >= dest.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(origin.latitude, dest.longitude), new LatLng(dest.latitude, origin.longitude)), 0));
			else if (origin.latitude >= dest.latitude && origin.longitude <= dest.longitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(dest.latitude, origin.longitude), new LatLng(origin.latitude, dest.longitude)), 0));
			else
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(dest.latitude, dest.longitude), new LatLng(origin.latitude, origin.longitude)), 0));
			map.animateCamera(CameraUpdateFactory.zoomOut());
		} else
			super.onBackPressed();
	}

	private OnClickListener actionListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (isDoneLoading) {
				switch (view.getId()) {
				case R.id.activity_current_trip_member:
					CurrentTripMemberDialog.create(CurrentTripActivity.this, getSupportFragmentManager(), findLocation, trip.getMembers());
					break;
				case R.id.activity_current_trip_places:
					CurrentTripPlaceDialog.create(CurrentTripActivity.this, getSupportFragmentManager(), findRoute, trip.getPlaces());
					break;
				case R.id.activity_current_trip_warning:
					CurrentTripWarningDialog.create(CurrentTripActivity.this, getSupportFragmentManager(), tripId, trip.getMembers());
					break;
				default:
					break;
				}
			}
		}
	};

	private FindLocationCallback findLocation = new FindLocationCallback() {

		@Override
		public void onFindLocation(User user) {
			try {
				JSONObject params = new JSONObject();

				String message = Constant.GET_USER_LOCATION + "|" + String.valueOf(userId);
				String condition = " where user_id=" + String.valueOf(userId);

				params.put("message", message);
				params.put("condition", condition);

				Log.i("Debug send GCM", params.toString());

				ServerRequest.newServerRequest(RequestType.PUSH_TO_SELECTED_USER, params, null).executeAsync();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private FindRouteCallback findRoute = new FindRouteCallback() {

		@Override
		public void onFindRoute(Place place) {
			// TODO Auto-generated method stub
			ArrayList<Place> places = new ArrayList<Place>();
			Location myLocation = map.getMyLocation();

			Place currentLocation = new Place();
			currentLocation.setName("Mine");
			currentLocation.setLatitude(myLocation.getLatitude());
			currentLocation.setLongitude(myLocation.getLongitude());

			places.add(currentLocation);
			places.add(place);

			Log.i("Debug find route", place.getName() + String.valueOf(place.getLatitude()) + String.valueOf(place.getLongitude()));

			map.clear();

			DrawPath drawPath = new DrawPath(map, getBaseContext(), places);
			drawPath.startDraw(null);
			isFindRoute = true;
		}

	};

	public interface FindLocationCallback {
		public void onFindLocation(User user);
	}

	public interface FindRouteCallback {
		public void onFindRoute(Place place);
	}

	public void LoadDestination() {
		try {
			JSONObject params = new JSONObject();
			params.put("tripId", tripId);
			ServerRequest.newServerRequest(RequestType.FETCH_TRIP_INFO, params, new ServerRequestCallback() {

				@Override
				public void onCompleted(Object... results) {
					trip = (Trip) results[0];

					ArrayList<Place> places = new ArrayList<Place>();
					places.add(trip.getStartPlace());

					for (int i = 0; i < trip.getMinorDestination().size(); i++) {
						places.add(trip.getMinorDestination().get(i));
					}

					currentPath = new DrawPath(map, getBaseContext(), places);
					currentPath.startDraw(new DrawCallback() {
						
						@Override
						public void onDoneDrawing() {
								isDoneLoading = true;
							if (isWarning) {
								Intent intent = CurrentTripActivity.this.getIntent();
								String message = intent.getStringExtra("message");
								// Format header|tripId|userId|userName|lat,long
								String[] contents = message.split("\\|");

								double latitude = Double.parseDouble(contents[4].split(",")[0].trim());
								double longitude = Double.parseDouble(contents[4].split(",")[1].trim());

								int userId = Integer.parseInt(contents[2]);
								String userName = "";
								for (int i = 0; i < trip.getMembers().size(); i++) {
									if (trip.getMembers().get(i).getId() == userId) {
										userName = trip.getMembers().get(i).getName();
										break;
									}
								}
								ArrayList<Place> places = new ArrayList<Place>();
								Place currentPlace = new Place();
								currentPlace.setName("Mine");
								currentPlace.setLatitude(map.getMyLocation().getLatitude());
								currentPlace.setLongitude(map.getMyLocation().getLongitude());
								
								Place warningPlace = new Place();
								warningPlace.setName(userName);
								warningPlace.setLatitude(latitude);
								warningPlace.setLongitude(longitude);
								map.clear();
								places.add(currentPlace);
								places.add(warningPlace);
								DrawPath drawPath = new DrawPath(map, getBaseContext(), places);
								drawPath.startDraw(null);
								isWarnRoute = true; 	
//								if (warningMarker != null) {
//									warningMarker.remove();
//								}
//								MarkerOptions markerOption = new MarkerOptions().position(new LatLng(latitude, longitude)).title(userName);
//								warningMarker = map.addMarker(markerOption);
//								LatLng myLatLng = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
//								LatLng warnLatLng= new LatLng(latitude, longitude);
//								if (myLatLng.latitude <= warnLatLng.latitude && myLatLng.longitude <= warnLatLng.longitude)
//									map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(myLatLng, warnLatLng), 1));
//								else if (myLatLng.latitude <= warnLatLng.latitude && myLatLng.longitude >= warnLatLng.longitude)
//									map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(myLatLng.latitude, warnLatLng.longitude), new LatLng(warnLatLng.latitude, myLatLng.longitude)), 1));
//								else if (myLatLng.latitude >= warnLatLng.latitude && myLatLng.longitude <= warnLatLng.longitude)
//									map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(warnLatLng.latitude, myLatLng.longitude), new LatLng(myLatLng.latitude, warnLatLng.longitude)), 1));
//								else
//									map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(warnLatLng.latitude, warnLatLng.longitude), new LatLng(myLatLng.latitude, myLatLng.longitude)), 1));
							//	map.animateCamera(CameraUpdateFactory.zoomOut());
								// TODO: make this user location on map, move camera
							}
						}
					});
					
				}

			}).executeAsync();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
