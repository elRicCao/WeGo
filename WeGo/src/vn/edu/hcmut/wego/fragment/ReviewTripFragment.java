package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewTripFragment extends Fragment {
	private Context context;
	private ArrayList<LatLng> markerPoints;
	private ArrayList<Integer> idPlaces;
	private ArrayList<String> namePlaces;
	private ArrayList<Integer> idSuggestPlace;
	private TextView startDate;
	private TextView endDate;
	private TextView tripCost;
	private CheckBox isPublic;
	private LinearLayout createTrip;
	public ReviewTripFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_review_plan_trip, container, false);
		
		markerPoints = new ArrayList<LatLng>();
		idPlaces = new ArrayList<Integer>();
		namePlaces = new ArrayList<String>();
		idSuggestPlace = new ArrayList<Integer>();
		Bundle bundle = getArguments();
		markerPoints = bundle.getParcelableArrayList("markerPoints");
		idPlaces = bundle.getIntegerArrayList("idPlaces");
		namePlaces = bundle.getStringArrayList("namePlaces");
		idSuggestPlace = bundle.getIntegerArrayList("idSuggestPlace");
		
		startDate = (TextView) view.findViewById(R.id.create_trip_start_date);
		endDate = (TextView) view.findViewById(R.id.create_trip_end_date);
		tripCost = (TextView) view.findViewById(R.id.create_trip_cost);
		isPublic = (CheckBox) view.findViewById(R.id.create_trip_public);
		createTrip = (LinearLayout) view.findViewById(R.id.button_next_step3);
		
		createTrip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JSONObject params = new JSONObject();

				try {
					params.put("startId", idPlaces.get(0));
					params.put("leaderId", 1);
					params.put("start_date", startDate.getText().toString());
					params.put("end_date", endDate.getText().toString());
					params.put("cost", tripCost.getText().toString().substring(0, tripCost.getText().length()-1));
					
					JSONArray array = new JSONArray();
					for(int i = 0; i < idSuggestPlace.size();i++)
						array.put(idSuggestPlace.get(i));
					params.put("idSuggestPlace", array);
					JSONArray arrayDest = new JSONArray();
					for(int i = 1; i < idPlaces.size(); i++)
						arrayDest.put(idPlaces.get(i));
					params.put("idMinorDestination", arrayDest);
					if(isPublic.isChecked())
						params.put("isPublic", 1);
					else
						params.put("isPublic", 0);
				
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ServerRequest.newServerRequest(RequestType.ACTION_CREATE_TRIP, params, new ServerRequestCallback() {

					@Override
					public void onCompleted(Object... results) {
						getActivity().finish();
					}
				}).executeAsync();
			}
		});
		
		return view;

	}

}
