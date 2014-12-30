package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

public class SuggestPlaceFragment extends Fragment {
	private Context context;
	private ArrayList<LatLng> markerPoints;
	private ArrayList<Integer> idPlaces;
	private ArrayList<String> namePlaces;
	private LinearLayout suggestPlaceList;
	private ArrayList<Place> places;
	private ArrayList<Integer> idSuggestPlace;
	private int chosen = 1;

	private LinearLayout btnNextStep;
	private Spinner minorDestinationList;

	public SuggestPlaceFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_suggest_place, container, false);
		suggestPlaceList = (LinearLayout) view.findViewById(R.id.fragment_suggest_place_list);
		btnNextStep = (LinearLayout) view.findViewById(R.id.button_next_step3);
		minorDestinationList = (Spinner) view.findViewById(R.id.minor_destination_list);

		markerPoints = new ArrayList<LatLng>();
		idPlaces = new ArrayList<Integer>();
		namePlaces = new ArrayList<String>();

		Bundle bundle = getArguments();
		markerPoints = bundle.getParcelableArrayList("markerPoints");
		idPlaces = bundle.getIntegerArrayList("idPlaces");
		namePlaces = bundle.getStringArrayList("namePlaces");
		idSuggestPlace = new ArrayList<Integer>();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);

		for (int i = 1; i < namePlaces.size(); i++)
			adapter.add(namePlaces.get(i));
		
		populate(inflater, suggestPlaceList);
		
		
		btnNextStep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				ReviewTripFragment reviewTripFragment = new ReviewTripFragment(context);

				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("markerPoints", markerPoints);
				bundle.putIntegerArrayList("idSuggestPlace", idSuggestPlace);
				bundle.putIntegerArrayList("idPlaces", idPlaces);
				bundle.putStringArrayList("namePlaces", namePlaces);
				reviewTripFragment.setArguments(bundle);
				fragmentTransaction.replace(android.R.id.content, reviewTripFragment);
				fragmentTransaction.commit();
			}
		});
		return view;
	}	
	
	private void populate(final LayoutInflater inflater, final ViewGroup container) {
		View view1 = inflater.inflate(R.layout.item_pick_place, container, false);
		TextView nameView = (TextView) view1.findViewById(R.id.item_pick_place_info_name);
		RatingBar ratingBar1 = (RatingBar) view1.findViewById(R.id.item_pick_place_rate);
		Utils.setUpRatingBar(context, ratingBar1);
		nameView.setText("Po Nagar");
		
		View view2 = inflater.inflate(R.layout.item_pick_place, container, false);
		TextView nameView2 = (TextView) view2.findViewById(R.id.item_pick_place_info_name);
		RatingBar ratingBar2 = (RatingBar) view2.findViewById(R.id.item_pick_place_rate);
		Utils.setUpRatingBar(context, ratingBar2);
		nameView2.setText("Long Son Pagoda");
		
		View view3 = inflater.inflate(R.layout.item_pick_place, container, false);
		TextView nameView3 = (TextView) view3.findViewById(R.id.item_pick_place_info_name);
		RatingBar ratingBar3 = (RatingBar) view3.findViewById(R.id.item_pick_place_rate);
		Utils.setUpRatingBar(context, ratingBar3);
		nameView3.setText("Yang Bay Waterfall");
		
		container.addView(view1);
		container.addView(view2);
		container.addView(view3);
	}
}
