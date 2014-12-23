package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SuggestPlaceFragment extends Fragment {
	private Context context;
	private ArrayList<LatLng> markerPoints;
	private ArrayList<Integer> idPlaces;
	private ArrayList<String> namePlaces;
	private LinearLayout suggestPlaceList;
	private ArrayList<Place> places;
	private ArrayList<Place> chosenPlace;
	private ArrayList<Integer> idList;

	private LinearLayout btnNextStep;

	public SuggestPlaceFragment(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_suggest_place, container, false);
		suggestPlaceList = (LinearLayout) view.findViewById(R.id.fragment_suggest_place_list);
		btnNextStep = (LinearLayout) view.findViewById(R.id.button_next_step3);
		markerPoints = new ArrayList<LatLng>();
		idPlaces = new ArrayList<Integer>();
		namePlaces = new ArrayList<String>();
		Bundle bundle = getArguments();
		markerPoints = bundle.getParcelableArrayList("markerPoints");
		idPlaces = bundle.getIntegerArrayList("idPlaces");
		namePlaces = bundle.getStringArrayList("namePlaces");
		idList = new ArrayList<Integer>();
		btnNextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				FragmentManager fragmentManager = getFragmentManager();		
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				ReviewTripFragment reviewTripFragment = new ReviewTripFragment(context);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("markerPoints", markerPoints);
				bundle.putIntegerArrayList("idPlaces", idPlaces);
				bundle.putStringArrayList("namePlaces", namePlaces);
				reviewTripFragment.setArguments(bundle);				
				fragmentTransaction.replace(android.R.id.content, reviewTripFragment);
				fragmentTransaction.commit();
			}
		});
		JSONObject params = new JSONObject();

		try {
			params.put("idPlaces0", idPlaces.get(0));
			params.put("idPlaces1", idPlaces.get(1));
			if (idPlaces.size() > 1)
				params.put("idPlaces2", idPlaces.get(2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ServerRequest.newServerRequest(RequestType.SUGGEST, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				places = (ArrayList<Place>) results[0];
				chosenPlace = new ArrayList<Place>();
				for (int i = 0; i < places.size(); i++) {
					View suggestPlace = inflater.inflate(R.layout.item_suggest_place, container, false);
					final TextView placeName = (TextView) suggestPlace.findViewById(R.id.item_suggest_place_name);
					final TextView placeAddress = (TextView) suggestPlace.findViewById(R.id.item_suggest_place_address);
					placeName.setText(places.get(i).getName());
					placeAddress.setText(places.get(i).getAddress());
					suggestPlaceList.addView(suggestPlace);
					boolean exist = false;
					int j;
					for (j = 0; j < idList.size(); j++) {
						if (i == idList.get(j)) {
							exist = true;
							break;
						}
					}
					if (exist)
						idList.remove(j);
					else
						idList.add(i);
					suggestPlace.setOnClickListener(new PlaceListener(i));
				}
			}
		}).executeAsync();
		return view;

	}

	private class PlaceListener implements OnClickListener {
		private boolean isSelected = false;
		private int i;
		public PlaceListener(int j) {
			i = j;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.setSelected(!isSelected);
			isSelected = !isSelected;
			if (isSelected) {
				int j;
				boolean exist = false;
				for(j = 0; j <idList.size();j++)
				{
					if(i == idList.get(j))
					{
						exist = true;
						break;
					}
				}
				if(!exist)idList.add(i);
			} else {
				int j;
				boolean exist = false;
				for(j = 0; j <idList.size();j++)
				{
					if(i == idList.get(j))
					{
						exist = true;
						break;
					}
				}
				if(exist)idList.remove(j);
			}
		}

	}

}
