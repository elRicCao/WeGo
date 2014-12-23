package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.activity.PlaceInfoActivity;
import vn.edu.hcmut.wego.dialog.WishlistDialog;
import vn.edu.hcmut.wego.entity.Place;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MoreFragment extends TabFragment {

	private static final int titleRes = R.string.title_fragment_more;
	private static final int iconRes = R.drawable.ic_tab_more;
	private Context context;
	private int userId;
	private ArrayList<Place> topPlaces;

	public MoreFragment(Context context, int userId) {
		super(context.getResources().getString(titleRes), iconRes);
		this.context = context;
		this.userId = userId;
		this.topPlaces = new ArrayList<Place>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_more, container, false);

		LinearLayout topPlaces = (LinearLayout) rootView.findViewById(R.id.fragment_more_top_place_list);

		LinearLayout topPhotos = (LinearLayout) rootView.findViewById(R.id.fragment_more_top_photo_list);
		
		LinearLayout buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_more_button_bar);
		
		ScrollView content = (ScrollView) rootView.findViewById(R.id.fragment_more_content);
		
		content.setOnTouchListener(new MainActivity.BottomBarListener(context, buttonBar));

		populateTopPlaces(inflater, topPlaces);
		populateTopPhotos(inflater, topPhotos);
		
		LinearLayout wishlistButton = (LinearLayout) rootView.findViewById(R.id.fragment_more_button_wishlist);
		wishlistButton.setOnClickListener(new WishlistDialog.WishlistDialogListener(context, getFragmentManager(), userId));

		return rootView;
	}

	private void populateTopPlaces(LayoutInflater inflater, ViewGroup container) {
		// int size = 2;
		// for (int i = 0; i < size; i++) {
		// View placeView = inflater.inflate(R.layout.item_place, container, false);
		// container.addView(placeView);
		//
		// placeView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// Intent intent = new Intent(context, PlaceInfoActivity.class);
		// startActivity(intent);
		// }
		// });
		//
		// if (i < size - 1) {
		// View dividerView = inflater.inflate(R.layout.item_divider, container, false);
		// container.addView(dividerView);
		// }
		// }

		View placeView = inflater.inflate(R.layout.item_place, container, false);
		container.addView(placeView);

		View dividerView = inflater.inflate(R.layout.item_divider, container, false);
		container.addView(dividerView);

		placeView = inflater.inflate(R.layout.item_place_fake_1, container, false);
		container.addView(placeView);

		dividerView = inflater.inflate(R.layout.item_divider, container, false);
		container.addView(dividerView);

		placeView = inflater.inflate(R.layout.item_place_fake_2, container, false);
		container.addView(placeView);
	}

	private void populateTopPhotos(LayoutInflater inflater, ViewGroup container) {
		View placeView = inflater.inflate(R.layout.item_photo, container, false);
		container.addView(placeView);

		View dividerView = inflater.inflate(R.layout.item_divider, container, false);
		container.addView(dividerView);

		placeView = inflater.inflate(R.layout.item_photo_fake_1, container, false);
		container.addView(placeView);

		dividerView = inflater.inflate(R.layout.item_divider, container, false);
		container.addView(dividerView);

//		placeView = inflater.inflate(R.layout.item_photo_fake_2, container, false);
//		container.addView(placeView);
	}
}
