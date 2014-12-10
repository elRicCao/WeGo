package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.PlaceInfoActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MoreFragment extends TabFragment {

	private static final String title = "More";
	private static final int iconRes = R.drawable.ic_tab_more;
	private Context context;
	private int userId;

	public MoreFragment(Context context, int userId) {
		super(title, iconRes);
		this.context = context;
		this.userId = userId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_more, container, false);

		LinearLayout topPlaces = (LinearLayout) rootView.findViewById(R.id.fragment_more_top_place_list);
		
		populateTopPlaces(inflater, topPlaces);
		
		return rootView;
	}
	
	private void populateTopPlaces(LayoutInflater inflater, ViewGroup container) {
		int size = 2;
		for (int i = 0; i < size; i++) {
			View placeView = inflater.inflate(R.layout.item_place, container, false);
			container.addView(placeView);
			
			placeView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, PlaceInfoActivity.class);
					startActivity(intent);
				}
			});
			
			if (i < size - 1) {
				View dividerView = inflater.inflate(R.layout.item_divider, container, false);
				container.addView(dividerView);
			}
		}
	}
}
