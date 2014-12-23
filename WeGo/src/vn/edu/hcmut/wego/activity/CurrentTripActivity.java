package vn.edu.hcmut.wego.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.dialog.MemberDialog;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.DirectionsJSONParser;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CurrentTripActivity extends ActionBarActivity {

	private AutoCompleteTextView searchPlace;
	private static ArrayAdapter<String> adapter;
	private static ArrayList<Place> places;
	private static String[] placeName;
	GoogleMap map;
	LatLng origin, dest;
	LatLng position;
	Marker marker;
	ArrayList<LatLng> markerRestaurants;
	ArrayList<LatLng> markerGasStations;
	ArrayList<LatLng> markerUsers;
	ArrayList<Marker> restaurants;
	private static ArrayList<LatLng> markerPoints;

	private ImageButton memberButton;
	private ImageButton warningButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_trip);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Getting reference to SupportMapFragment of the activity_main
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

		// Getting reference to Button

		// Getting Map for the SupportMapFragment
		map = fm.getMap();
		map.setMyLocationEnabled(true);
		markerPoints = new ArrayList<LatLng>();
		searchPlace = (AutoCompleteTextView) findViewById(R.id.search_place_current);
		memberButton = (ImageButton) findViewById(R.id.activity_current_trip_member);
		warningButton = (ImageButton) findViewById(R.id.activity_current_trip_warning);
		markerPoints.add(new LatLng(10.768451, 106.6943626));
		markerPoints.add(new LatLng(12.2595968, 109.2405693));
		// itemGasStation.setOnClickListener(new OnClickListener() {
		//
		// private boolean isSelected = false;
		//
		// @Override
		// public void onClick(View v) {
		// v.setSelected(!isSelected);
		// isSelected = !isSelected;
		// if (isSelected) {
		// markerRestaurants = new ArrayList<LatLng>();
		// restaurants = new ArrayList<Marker>();
		// // get Restaurant from database;
		// markerRestaurants.add(new LatLng(10.7713378, 106.6580898));
		// markerRestaurants.add(new LatLng(10.771597, 106.659689));
		// markerRestaurants.add(new LatLng(10.7727417, 106.6594387));
		// markerRestaurants.add(new LatLng(10.7721331, 106.657411));
		// markerRestaurants.add(new LatLng(10.7720448, 106.6590632));
		//
		// MarkerOptions markerOptions = new MarkerOptions();
		// for (int i = 0; i < markerRestaurants.size(); i++) {
		// BitmapFactory.Options options = new BitmapFactory.Options();
		//
		// // downsizing image as it throws OutOfMemory Exception for larger
		// // images
		// options.inSampleSize = 1;
		// Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_gas_station_active, options);
		// restaurants.add(map.addMarker(markerOptions
		// .position(markerRestaurants.get(i))
		// .title("Restaurant")
		// .snippet("description")
		// .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, true)))));
		// }
		// } else
		// // Remove the meat
		// for (int i = 0; i < markerRestaurants.size(); i++) {
		// restaurants.get(i).remove();
		// }
		// }
		// });

		BitmapFactory.Options options = new BitmapFactory.Options();

		// downsizing image as it throws OutOfMemory Exception for larger
		// images
		options.inSampleSize = 1;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ho_chi_minh, options);
		marker = map.addMarker(new MarkerOptions().position(markerPoints.get(0)).title("Ho Chi Minh City").icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));

		options = new BitmapFactory.Options();

		// downsizing image as it throws OutOfMemory Exception for larger
		// images
		options.inSampleSize = 1;
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nha_trang, options);
		marker = map.addMarker(new MarkerOptions().position(markerPoints.get(1)).title("Nha Trang").icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));

		if (markerPoints.size() >= 2) {
			origin = markerPoints.get(0);
			dest = markerPoints.get(markerPoints.size() - 1);
			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(origin, dest);

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}
		JSONObject params = new JSONObject();
		ServerRequest.newServerRequest(RequestType.SELECT, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				// Hide progress bar
				places = (ArrayList<Place>) results[0];
				placeName = new String[places.size()];
				for (int i = 0; i < places.size(); i++) {
					placeName[i] = places.get(i).getName();
				}
				adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, placeName);
				searchPlace.setAdapter(adapter);

			}
		}).executeAsync();

		searchPlace.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				int i;
				for (i = 0; i < placeName.length; i++) {
					if (placeName[i].equals(arg0.getItemAtPosition(arg2))) {
						break;
					}
				}
				position = new LatLng(places.get(i).getLatitude(), places.get(i).getLongtitude());
				BitmapFactory.Options options = new BitmapFactory.Options();

				// downsizing image as it throws OutOfMemory Exception for larger
				// images
				options.inSampleSize = 1;
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ho_chi_minh, options);
				marker = map.addMarker(new MarkerOptions().position(position).title(places.get(i).getName()).icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			}

		});
		memberButton.setOnClickListener(new MemberDialog.MemberDialogListener(this, getSupportFragmentManager(), 1));

	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, HashMap<String, List<List<HashMap<String, String>>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected HashMap<String, List<List<HashMap<String, String>>>> doInBackground(String... jsonData) {

			JSONObject jObject;
			HashMap<String, List<List<HashMap<String, String>>>> result = new HashMap<String, List<List<HashMap<String, String>>>>();

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				result = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(HashMap<String, List<List<HashMap<String, String>>>> result) {

			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get("routes").get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);
			}

			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions.width(10).color(getResources().getColor(R.color.green)).geodesic(true));
			if (origin.latitude < dest.latitude)
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(origin, dest), 0));
			else
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(dest, origin), 0));
			map.animateCamera(CameraUpdateFactory.zoomOut());
			map.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(final Marker marker) {
					final Handler handler = new Handler();

					final long startTime = SystemClock.uptimeMillis();
					final long duration = 2000;

					Projection proj = map.getProjection();
					final LatLng markerLatLng = marker.getPosition();
					Point startPoint = proj.toScreenLocation(markerLatLng);
					startPoint.offset(0, -100);
					final LatLng startLatLng = proj.fromScreenLocation(startPoint);

					final Interpolator interpolator = new BounceInterpolator();

					handler.post(new Runnable() {
						@Override
						public void run() {
							long elapsed = SystemClock.uptimeMillis() - startTime;
							float t = interpolator.getInterpolation((float) elapsed / duration);
							double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
							double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
							marker.setPosition(new LatLng(lat, lng));

							if (t < 1.0) {
								// Post again 16ms later.
								handler.postDelayed(this, 16);
							}
						}
					});
					return false;
				}

			});

		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Waypoints
		String waypoints = "";
		for (int i = 1; i < markerPoints.size() - 1; i++) {
			LatLng point = (LatLng) markerPoints.get(i);
			if (i == 1)
				waypoints = "waypoints=";
			waypoints += point.latitude + "," + point.longitude + "|";
		}

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
		return url;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.current_trip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
