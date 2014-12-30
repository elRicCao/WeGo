package vn.edu.hcmut.wego.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.Trip;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TripInfoActivity extends ActionBarActivity {

	private ActionBar actionBar;
	private TextView startName;
	private TextView endName;
	private ImageView startView;
	private ImageView endView;
	private TextView numOfMember;
	private TextView timeView;
	private TextView announceView;
	private TextView adminName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip_info);
		actionBar = getActionBar();
		startName = (TextView) findViewById(R.id.activity_trip_info_start_name);
		endName = (TextView) findViewById(R.id.activity_trip_info_end_name);
		numOfMember = (TextView) findViewById(R.id.activity_trip_info_member);
		timeView = (TextView) findViewById(R.id.activity_trip_info_Time);
		adminName = (TextView) findViewById(R.id.activity_trip_info_admin_name);
		announceView = (TextView) findViewById(R.id.activity_trip_info_announcement);

		populateInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip_info, menu);
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

	private void populateInfo() {
		try {
			int tripId = getIntent().getExtras().getInt("tripId");
			JSONObject params = new JSONObject().put("tripId", tripId);
			ServerRequest.newServerRequest(RequestType.FETCH_TRIP_INFO, params, new ServerRequestCallback() {

				@Override
				public void onCompleted(Object... results) {
					Trip trip = (Trip) results[0];
					startName.setText(trip.getStartPlace().getName());
					endName.setText(trip.getMinorDestination().get(trip.getMinorDestination().size() - 1).getName());
					numOfMember.setText(String.valueOf(trip.getMembers().size()));
					adminName.setText(trip.getLeader().getName());
					announceView.setText(trip.getAnnouncement());
					DateFormat df = new SimpleDateFormat("d/M", Locale.ENGLISH);
					timeView.setText(df.format(trip.getStartDate()) + " - " + df.format(trip.getEndDate()));
				}
			}).executeAsync();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static class TripInfoListener implements OnClickListener {

		private Context context;
		private int tripId;

		public TripInfoListener(Context context, int tripId) {
			this.context = context;
			this.tripId = tripId;
		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, TripInfoActivity.class);
			intent.putExtra("tripId", tripId);
			context.startActivity(intent);
		}

	}
}
