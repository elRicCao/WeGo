package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import vn.edu.hcmut.wego.R;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewTripFragment extends Fragment {
	private Context context;
	private ArrayList<LatLng> markerPoints;
	private ArrayList<Integer> idPlaces;
	private ArrayList<String> namePlaces;
	public ReviewTripFragment(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_review_plan_trip, container, false);
//		markerPoints = new ArrayList<LatLng>();
//		idPlaces = new ArrayList<Integer>();
//		namePlaces = new ArrayList<String>();
//		Bundle bundle = getArguments();
//		markerPoints = bundle.getParcelableArrayList("markerPoints");
//		idPlaces = bundle.getIntegerArrayList("idPlaces");
//		namePlaces = bundle.getStringArrayList("namePlaces");
		
		return view;

	}

}
