package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TripFragment extends WeGoFragment {
	
	private static final String title = "Trip";
	private static final int iconRes = R.drawable.ic_trip;

	public TripFragment(Context context) {
		super(title, iconRes);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_trip, container, false);

		LinearLayout currentTrip = (LinearLayout) rootView.findViewById(R.id.fragment_trip_section_current_trip);
		
		currentTrip.setOnTouchListener(new MainActivity.TileOnTouchListerner());
		
		return rootView;
	}
	
}
