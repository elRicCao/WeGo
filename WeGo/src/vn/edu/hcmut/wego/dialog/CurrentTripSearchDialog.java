package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.CurrentTripActivity.FindRouteCallback;
import vn.edu.hcmut.wego.adapter.CurrentSearchAdapter;
import vn.edu.hcmut.wego.adapter.CurrentSearchAdapter.SearchDialogCallback;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class CurrentTripSearchDialog extends DialogFragment {

	private Context context;

	private LinearLayout quickGroup;
	private LinearLayout customGroup;
	private LinearLayout resultGroup;
	private TextView searchMode;
	private EditText customSearch;
	private ListView resultList;
	private CurrentSearchAdapter adapter;
	private FindRouteCallback callback;
	private Location myLocation;

	public CurrentTripSearchDialog(Context context, FindRouteCallback _callback, Location myLocation) {
		this.context = context;
		this.callback = _callback;
		this.myLocation = myLocation;
		this.adapter = new CurrentSearchAdapter(context, new ArrayList<Place>(), new SearchDialogCallback() {

			@Override
			public void onCallback(Place place) {
				if (callback != null) {
					callback.onFindRoute(place);
				}
				dismiss();
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_search, container, false);

		quickGroup = (LinearLayout) rootView.findViewById(R.id.dialog_search_quick_search_group);
		customGroup = (LinearLayout) rootView.findViewById(R.id.dialog_search_custom_search_group);
		resultGroup = (LinearLayout) rootView.findViewById(R.id.dialog_search_result_group);
		searchMode = (TextView) rootView.findViewById(R.id.dialog_search_mode);
		resultList = (ListView) rootView.findViewById(R.id.dialog_search_result);
		customSearch = (EditText) rootView.findViewById(R.id.dialog_search_custom_search);
		ImageButton customSearchToggle = (ImageButton) rootView.findViewById(R.id.dialog_search_custom);
		ImageButton restaurantSearch = (ImageButton) rootView.findViewById(R.id.dialog_search_restaurant);
		ImageButton hotelSearch = (ImageButton) rootView.findViewById(R.id.dialog_search_hotel);
		ImageButton gasStationSearch = (ImageButton) rootView.findViewById(R.id.dialog_search_gas_station);
		ImageButton repairSearch = (ImageButton) rootView.findViewById(R.id.dialog_search_repair);

		// Set up listener for quick search button
		restaurantSearch.setOnClickListener(quickSearchListener);
		hotelSearch.setOnClickListener(quickSearchListener);
		gasStationSearch.setOnClickListener(quickSearchListener);
		repairSearch.setOnClickListener(quickSearchListener);

		// Set up toggle for custom search button
		customSearchToggle.setOnClickListener(customSearchToggleListener);

		// Set up listener for custom search field
		customSearch.setOnEditorActionListener(customSearchEditorListener);

		return rootView;
	}

	private OnClickListener customSearchToggleListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			view.setSelected(!view.isSelected());
			resultGroup.setVisibility(View.GONE);
			if (view.isSelected()) {
				customGroup.setVisibility(View.VISIBLE);
				quickGroup.setVisibility(View.GONE);
				searchMode.setText(context.getResources().getString(R.string.dialog_search_custom));
			} else {
				customGroup.setVisibility(View.GONE);
				quickGroup.setVisibility(View.VISIBLE);
				searchMode.setText(context.getResources().getString(R.string.dialog_search_quick));
			}
		}
	};

	private OnEditorActionListener customSearchEditorListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				// TODO: create server request to search on text
				resultGroup.setVisibility(View.VISIBLE);
				adapter.clear();
				addFakeData();
				adapter.notifyDataSetChanged();
				return true;
			}
			return false;
		}
	};

	private OnClickListener quickSearchListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			resultGroup.setVisibility(View.VISIBLE);
			JSONObject params = new JSONObject();
			try {

				params.put("myLatitude", myLocation.getLatitude());
				params.put("myLongitude", myLocation.getLongitude());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			switch (view.getId()) {
			case R.id.dialog_search_restaurant:
				// TODO: Create server request to search
				adapter.clear();

				try {
					params.put("typeId", 9);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServerRequest.newServerRequest(RequestType.SEARCH_PLACE_AROUND, params, new ServerRequestCallback() {

					@Override
					public void onCompleted(Object... results) {
						@SuppressWarnings("unchecked")
						ArrayList<Place> places = (ArrayList<Place>) results[0];
						for (int i = 0; i < places.size(); i++)
							adapter.add(places.get(i));
					}

				}).executeAsync();
				resultList.setAdapter(adapter);
				break;
			case R.id.dialog_search_hotel:
				// TODO: create server request to search
				adapter.clear();
				try {
					params.put("typeId", 7);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServerRequest.newServerRequest(RequestType.SEARCH_PLACE_AROUND, params, new ServerRequestCallback() {

					@Override
					public void onCompleted(Object... results) {
						@SuppressWarnings("unchecked")
						ArrayList<Place> places = (ArrayList<Place>) results[0];
						for (int i = 0; i < places.size(); i++)
							adapter.add(places.get(i));
					}

				}).executeAsync();
				resultList.setAdapter(adapter);
				break;
			case R.id.dialog_search_gas_station:
				// TODO: create server request to search
				adapter.clear();
				try {
					params.put("typeId", 11);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServerRequest.newServerRequest(RequestType.SEARCH_PLACE_AROUND, params, new ServerRequestCallback() {

					@Override
					public void onCompleted(Object... results) {
						@SuppressWarnings("unchecked")
						ArrayList<Place> places = (ArrayList<Place>) results[0];
						for (int i = 0; i < places.size(); i++)
							adapter.add(places.get(i));
					}

				}).executeAsync();
				resultList.setAdapter(adapter);
				break;
			case R.id.dialog_search_repair:
				// TODO: create server request to search
				adapter.clear();
				try {
					params.put("typeId", 12);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ServerRequest.newServerRequest(RequestType.SEARCH_PLACE_AROUND, params, new ServerRequestCallback() {

					@Override
					public void onCompleted(Object... results) {
						@SuppressWarnings("unchecked")
						ArrayList<Place> places = (ArrayList<Place>) results[0];
						for (int i = 0; i < places.size(); i++)
							adapter.add(places.get(i));
					}

				}).executeAsync();
				resultList.setAdapter(adapter);
				break;
			}
		}
	};

	public static void create(Context context, FragmentManager fragmentManager, FindRouteCallback callback, Location myLocation) {
		CurrentTripSearchDialog dialog = new CurrentTripSearchDialog(context, callback, myLocation);
		dialog.show(fragmentManager, "search_dialog");
	}

	private void addFakeData() {

		Place place1 = new Place();
		place1.setName("Bun Bo Hue");
		place1.setAverageRate(4.5);

		Place place2 = new Place();
		place2.setName("Pho");
		place2.setAverageRate(4.0);

		adapter.add(place1);
		adapter.add(place2);
	}
}
