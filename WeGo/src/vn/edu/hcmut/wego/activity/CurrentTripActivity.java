package vn.edu.hcmut.wego.activity;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.dialog.CurrentTripMemberDialog;
import vn.edu.hcmut.wego.dialog.CurrentTripPlaceDialog;
import vn.edu.hcmut.wego.dialog.CurrentTripSearchDialog;
import vn.edu.hcmut.wego.dialog.CurrentTripWarningDialog;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.utility.Utils;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class CurrentTripActivity extends ActionBarActivity {

	private GoogleMap map;

	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_trip);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		userId = Utils.getUserId(this);

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

		// TODO: load destinaion and draw
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
			CurrentTripSearchDialog.create(this, getSupportFragmentManager(), findRoute);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnClickListener actionListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.activity_current_trip_member:
				CurrentTripMemberDialog.create(CurrentTripActivity.this, getSupportFragmentManager(), findLocation);
				break;
			case R.id.activity_current_trip_places:
				CurrentTripPlaceDialog.create(CurrentTripActivity.this, getSupportFragmentManager(), findRoute);
				break;
			case R.id.activity_current_trip_warning:
				CurrentTripWarningDialog.create(CurrentTripActivity.this, getSupportFragmentManager());
				break;
			default:
				break;
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

		}
	};

	public interface FindLocationCallback {
		public void onFindLocation(User user);
	}

	public interface FindRouteCallback {
		public void onFindRoute(Place place);
	}
}
