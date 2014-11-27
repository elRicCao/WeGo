package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.CurrentTripActivity;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.activity.TripInfoActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
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

	public TripFragment(Context context) {
		super(title, iconRes);
		this.context = context;
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
		for (int i = 0; i < 2; i++) {
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

		return rootView;
	}

}
