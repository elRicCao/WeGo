package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PickPlaceFragment extends Fragment {
	private ArrayAdapter<String> adapter;
	private ArrayList<Place> places;
	private String[] placeName;
	private AutoCompleteTextView departure,destination;
	private Context context;
	private ImageButton addMinor;
	private LinearLayout nextStep;
	private LinearLayout minorDestinationList;
	private ArrayList<LatLng> markerPoints;
	private ArrayList<Integer> idPlaces;
	private ArrayList<String> namePlaces;
	
	public PickPlaceFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pick_place, container, false);
		addMinor = (ImageButton) view.findViewById(R.id.button_add_minor_destination);
		departure = (AutoCompleteTextView) view.findViewById(R.id.departure_text);
		destination = (AutoCompleteTextView) view.findViewById(R.id.destination_text);
		minorDestinationList = (LinearLayout) view.findViewById(R.id.fragment_minor_destination_list);
		nextStep = (LinearLayout) view.findViewById(R.id.button_next_step1);
		markerPoints = new ArrayList<LatLng>();
		idPlaces = new ArrayList<Integer>();
		namePlaces = new ArrayList<String>();
		JSONObject params = new JSONObject();
		ServerRequest.newServerRequest(RequestType.SELECT, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				// Hide progress bar
				places = (ArrayList<Place>) results[0];
				placeName = new String[places.size()];
				for (int i = 0; i < places.size(); i++) {
					placeName[i] = places.get(i).getName();
				}
				adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, placeName);
				departure.setAdapter(adapter);
				destination.setAdapter(adapter);
			}
		}).executeAsync();
		
		addMinor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View destinationView = inflater.inflate(R.layout.item_minor_destination, container, false);
				EditText minorDestinationText = (EditText) destinationView.findViewById(R.id.minor_destination_text);
				minorDestinationText.setText(destination.getText());
				destination.setText("");
				minorDestinationList.addView(destinationView);
			}
		});
		
		departure.setOnItemClickListener(new PlaceListener());
		destination.setOnItemClickListener(new PlaceListener());
		nextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				FragmentManager fragmentManager = getFragmentManager();
				
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				MapFragment mapFragment = new MapFragment(context);
				Bundle bundle = new Bundle();
				
				bundle.putParcelableArrayList("markerPoints", markerPoints);
				bundle.putIntegerArrayList("idPlaces", idPlaces);
				bundle.putStringArrayList("namePlaces", namePlaces);
				mapFragment.setArguments(bundle);				
				fragmentTransaction.replace(android.R.id.content, mapFragment);
				fragmentTransaction.commit();
				
			}
		});
		return view;
		
		

	}
	private class PlaceListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			int i;
			for (i = 0; i < placeName.length; i++) {
				if (placeName[i].equals(arg0.getItemAtPosition(arg2))) {
					idPlaces.add(places.get(i).getId());
					namePlaces.add(places.get(i).getName());
					break;
				}
			}
			markerPoints.add(new LatLng(places.get(i).getLatitude(), places.get(i).getLongtitude()));
			
		}
		
	}
}
