package vn.edu.hcmut.wego.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.CreateTripActivity;
import vn.edu.hcmut.wego.activity.CurrentTripActivity;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.activity.TripInfoActivity;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;

public class TripFragment extends TabFragment {

	private static final String title = "Trip";
	private static final int iconRes = R.drawable.ic_tab_trip;
	private Context context;
	private int userId;
	private int tripId;

	public int getTripId(){
		return tripId;
	}
	public Context getContext(){
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_trip, container, false);

		
		LinearLayout currentTrip = (LinearLayout) rootView.findViewById(R.id.fragment_trip_section_current_trip);
		currentTrip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CurrentTripActivity.class);
				startActivity(intent);
			}
		});

		LinearLayout nextTrip = (LinearLayout) rootView.findViewById(R.id.fragment_trip_next_list);
		for (int i = 0; i < 1; i++) {
			View tripView = inflater.inflate(R.layout.item_next_trip, container, false);
			nextTrip.addView(tripView);

			RatingBar ratingBar = (RatingBar) tripView.findViewById(R.id.item_next_trip_leader_reputation);
			LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
			layerDrawable.getDrawable(2).setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);

			tripView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, TripInfoActivity.class);
					startActivity(intent);
				}
			});

			if (i < 1) {
				View dividerView = inflater.inflate(R.layout.item_divider, container, false);
				nextTrip.addView(dividerView);
			}
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
		
		JSONObject params = new JSONObject();
		try {
			params.put(Constant.PARAM_USER_ID, userId);
			// for (int i = 0; i < 1; i++) {
			// View requestView = inflater.inflate(R.layout.item_friend_request, container, false);
			// container.addView(requestView);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_TRIP_LIST, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				
			}

		}).executeAsync();
		return rootView;
	}
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    inflater.inflate(R.menu.main, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_search:
	        // Not implemented here
	        return false;
	    case R.id.action_settings:
	        // Do Fragment menu item stuff here
	        return true;
	    default:
	        break;
	    }

	    return false;
	}
	
}
