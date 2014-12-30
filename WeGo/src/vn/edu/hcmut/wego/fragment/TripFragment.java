package vn.edu.hcmut.wego.fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.CreateTripActivity;
import vn.edu.hcmut.wego.activity.CurrentTripActivity;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.activity.TripInfoActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Trip;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class TripFragment extends TabFragment {

	private static final String title = "Trip";
	private static final int iconRes = R.drawable.ic_tab_trip;
	private Context context;
	private int userId;
	private int tripId;
	private ArrayList<Trip> tripList;

	public int getTripId() {
		return tripId;
	}

	public Context getContext() {
		return context;
	}

	public TripFragment() {

	}

	public TripFragment(Context context, int userId) {
		super(title, iconRes);
		this.userId = userId;
		this.context = context;
		tripId = 1;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_trip, container, false);

		try {
			JSONObject params = new JSONObject();
			params.put(Constant.PARAM_USER_ID, userId);
			ServerRequest.newServerRequest(RequestType.FETCH_TRIP_LIST, params, new ServerRequestCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void onCompleted(Object... results) {
					tripList = (ArrayList<Trip>) results[0];

					// Current Trip
					LinearLayout currentTrip = (LinearLayout) rootView.findViewById(R.id.fragment_trip_section_current_trip);

					currentTrip.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, CurrentTripActivity.class);
							intent.putExtra("isWarning", false);
							intent.putExtra("tripId", tripList.get(0).getId());
							startActivity(intent);
						}
					});

					// Next Trip
					LinearLayout nextTripGroup = (LinearLayout) rootView.findViewById(R.id.fragment_trip_section_next_trip);
					LinearLayout nextTrip = (LinearLayout) rootView.findViewById(R.id.fragment_trip_next_list);
					for (int i = 1; i < tripList.size(); i++) {
						nextTripGroup.setVisibility(View.VISIBLE);
						View tripView = inflater.inflate(R.layout.item_next_trip, container, false);

						TextView startView = (TextView) tripView.findViewById(R.id.item_next_trip_start);
						TextView endView = (TextView) tripView.findViewById(R.id.item_next_trip_destination);
						TextView timeView = (TextView) tripView.findViewById(R.id.item_next_trip_time);
						TextView leaderView = (TextView) tripView.findViewById(R.id.item_next_trip_leader);

						startView.setText(tripList.get(i).getStartPlace().getName());
						endView.setText(tripList.get(i).getEndPlace().getName());

						DateFormat df = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
						timeView.setText(df.format(tripList.get(i).getStartDate()) + " - " + df.format(tripList.get(i).getEndDate()));

						leaderView.setText(tripList.get(i).getLeader().getName());

						RatingBar ratingBar = (RatingBar) tripView.findViewById(R.id.item_next_trip_leader_reputation);
						Utils.setUpRatingBar(context, ratingBar);

						nextTrip.addView(tripView);
						tripView.setOnClickListener(new TripInfoActivity.TripInfoListener(context, tripList.get(i).getId()));

						if (i < tripList.size() - 1) {
							View dividerView = inflater.inflate(R.layout.item_divider, container, false);
							nextTrip.addView(dividerView);
						}
					}
				}

			}).executeAsync();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		LinearLayout buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_trip_button_bar);

		ScrollView content = (ScrollView) rootView.findViewById(R.id.fragment_trip_content);
		content.setOnTouchListener(new MainActivity.BottomBarListener(context, buttonBar));

		LinearLayout buttonCreateTrip = (LinearLayout) rootView.findViewById(R.id.fragment_trip_button_create);
		buttonCreateTrip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CreateTripActivity.class);
				startActivity(intent);
			}
		});

		return rootView;
	}
}
