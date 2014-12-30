package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.GroupSearchAdapter;
import vn.edu.hcmut.wego.adapter.PlaceSearchAdapter;
import vn.edu.hcmut.wego.adapter.TripSearchAdapter;
import vn.edu.hcmut.wego.adapter.UserSearchAdapter;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.Trip;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends ActionBarActivity {

	private enum Search {
		USER, GROUP, PLACE, TRIP
	};

	private String[] placeDBName;
	private ArrayList<Place> placeDB;
	private ArrayList<Place> places;
	private ArrayList<Group> groups;
	private ArrayList<Trip> trips;
	private ArrayList<User> users;
	private PlaceSearchAdapter adapterPlace;
	private GroupSearchAdapter adapterGroup;
	private UserSearchAdapter adapterUser;
	private TripSearchAdapter adapterTrip;
	private ArrayAdapter<String> adapterPlaceDB;

	private ListView resultList;

	private EditText searchField;

	private LinearLayout searchTripGroup;
	private AutoCompleteTextView departureField;
	private AutoCompleteTextView destinationField;

	private View lastSelected;
	private Search type;

	private boolean empty;
	private int index;
	private int start_id = 0;
	private int end_id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		ImageButton userButton = (ImageButton) findViewById(R.id.activity_search_user);
		ImageButton groupButton = (ImageButton) findViewById(R.id.activity_search_group);
		ImageButton tripButton = (ImageButton) findViewById(R.id.activity_search_trip);
		ImageButton placeButton = (ImageButton) findViewById(R.id.activity_search_place);

		searchField = (EditText) findViewById(R.id.activity_search_field);

		searchTripGroup = (LinearLayout) findViewById(R.id.activity_search_trip_group);
		departureField = (AutoCompleteTextView) findViewById(R.id.activity_search_departure);
		destinationField = (AutoCompleteTextView) findViewById(R.id.activity_search_destination);

		resultList = (ListView) findViewById(R.id.activity_search_list);

		setType(Search.USER, userButton);

		userButton.setOnClickListener(chooseListener);
		groupButton.setOnClickListener(chooseListener);
		tripButton.setOnClickListener(chooseListener);
		placeButton.setOnClickListener(chooseListener);

		searchField.setOnEditorActionListener(searchListener);
		destinationField.setOnEditorActionListener(searchListener);
		resultList.setOnScrollListener(resultListener);
	}

	private OnEditorActionListener searchListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				index = 0;
				empty = false;
				JSONObject params = new JSONObject();

				switch (type) {
				case USER:
					String userName = searchField.getText().toString().trim();
					if (!userName.isEmpty()) {
						// TODO: search user base on userName
						try {
							params.put("input", userName);
							params.put("index", index);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_USER, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								users = (ArrayList<User>) results[0];

								adapterUser = new UserSearchAdapter(getBaseContext(), users);
								resultList.setAdapter(adapterUser);
							}
						}).executeAsync();
					}

					break;

				case GROUP:
					String groupName = searchField.getText().toString().trim();
					if (!groupName.isEmpty()) {
						// TODO: search user base on groupName
						try {
							params.put("input", groupName);
							params.put("index", index);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_GROUP, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								groups = (ArrayList<Group>) results[0];

								adapterGroup = new GroupSearchAdapter(getBaseContext(), groups);
								resultList.setAdapter(adapterGroup);
							}
						}).executeAsync();
					}
					break;

				case PLACE:
					String placeName = searchField.getText().toString().trim();
					if (!placeName.isEmpty()) {
						// TODO: search user base on userName
						try {
							params.put("input", placeName);
							params.put("index", index);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_PLACE, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								places = (ArrayList<Place>) results[0];

								adapterPlace = new PlaceSearchAdapter(getBaseContext(), places);
								resultList.setAdapter(adapterPlace);
							}
						}).executeAsync();
					}
					break;
				case TRIP:
					// TODO: search trip base on departure and destination
					// TODO: check 2 field not null first
					if (start_id > 0 && end_id > 0) {
						try {
							params.put("start_id", start_id);
							params.put("end_id", end_id);
							params.put("index", index);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_TRIP, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {
								trips = (ArrayList<Trip>) results[0];
								adapterTrip = new TripSearchAdapter(getBaseContext(), trips);
								resultList.setAdapter(adapterTrip);

							}
						}).executeAsync();
					}

					break;
				}
				return true;
			}
			return false;
		}
	};

	private OnClickListener chooseListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.activity_search_user:
				setType(Search.USER, view);
				if (adapterGroup != null)
					adapterGroup.clear();
				if (adapterPlace != null)
					adapterPlace.clear();
				if (adapterTrip != null)
					adapterTrip.clear();
				break;

			case R.id.activity_search_group:
				setType(Search.GROUP, view);
				if (adapterPlace != null)
					adapterPlace.clear();
				if (adapterTrip != null)
					adapterTrip.clear();
				if (adapterUser != null)
					adapterUser.clear();
				break;

			case R.id.activity_search_place:
				setType(Search.PLACE, view);
				if (adapterGroup != null)
					adapterGroup.clear();
				if (adapterTrip != null)
					adapterTrip.clear();
				if (adapterUser != null)
					adapterUser.clear();
				break;

			case R.id.activity_search_trip:
				setType(Search.TRIP, view);
				if (adapterGroup != null)
					adapterGroup.clear();
				if (adapterPlace != null)
					adapterPlace.clear();
				if (adapterUser != null)
					adapterUser.clear();
				ServerRequest.newServerRequest(RequestType.SELECT, new JSONObject(), new ServerRequestCallback() {

					@Override
					public void onCompleted(Object... results) {
						// Hide progress bar
						placeDB = (ArrayList<Place>) results[0];
						placeDBName = new String[placeDB.size()];
						for (int i = 0; i < placeDB.size(); i++) {
							placeDBName[i] = placeDB.get(i).getName();
						}
						adapterPlaceDB = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, placeDBName);
						departureField.setAdapter(adapterPlaceDB);
						destinationField.setAdapter(adapterPlaceDB);

						departureField.setOnItemClickListener(new PlaceListener(true));
						destinationField.setOnItemClickListener(new PlaceListener(false));
					}
				}).executeAsync();
				break;
			}
		}
	};

	private OnScrollListener resultListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && !empty) {
				index += 15;
				JSONObject params = new JSONObject();
				switch (type) {
				case USER:
					String userName = searchField.getText().toString().trim();
					if (!userName.isEmpty()) {
						// TODO: search user base on userName
						try {
							params.put("input", userName);
							params.put("index", index);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_USER, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								users = (ArrayList<User>) results[0];
								if (users.isEmpty())
									empty = true;
								for (int i = 0; i < users.size(); i++)
									adapterUser.add(users.get(i));
							}
						}).executeAsync();
					}

					break;

				case GROUP:
					String groupName = searchField.getText().toString().trim();
					if (!groupName.isEmpty()) {
						// TODO: search user base on groupName
						try {
							params.put("input", groupName);
							params.put("index", index);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_GROUP, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								groups = (ArrayList<Group>) results[0];
								if (groups.isEmpty())
									empty = true;

								for (int i = 0; i < groups.size(); i++)
									adapterGroup.add(groups.get(i));
							}
						}).executeAsync();
					}
					break;

				case PLACE:
					String placeName = searchField.getText().toString().trim();
					if (!placeName.isEmpty()) {
						// TODO: search user base on userName
						try {
							params.put("input", placeName);
							params.put("index", index);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_PLACE, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								places = (ArrayList<Place>) results[0];
								if (places.isEmpty())
									empty = true;
								for (int i = 0; i < places.size(); i++)
									adapterPlace.add(places.get(i));
							}
						}).executeAsync();
					}
					break;
				case TRIP:
					// TODO: search trip base on departure and destination
					// TODO: check 2 field not null first
					if (start_id > 0 && end_id > 0) {
						try {
							params.put("start_id", start_id);
							params.put("index", index);
							params.put("end_id", end_id);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ServerRequest.newServerRequest(RequestType.SEARCH_TRIP, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {
								trips = (ArrayList<Trip>) results[0];
								if (trips.isEmpty())
									empty = true;
								for (int i = 0; i < trips.size(); i++)
									adapterTrip.add(trips.get(i));
								;

							}
						}).executeAsync();
					}
					break;
				}

			}
		}
	};

	private void setType(Search type, View view) {
		if (lastSelected != null) {
			lastSelected.setSelected(false);
		}
		view.setSelected(true);
		lastSelected = view;
		this.type = type;
		if (type == Search.USER || type == Search.GROUP || type == Search.PLACE) {
			searchField.setText("");
			searchField.setVisibility(View.VISIBLE);
			searchTripGroup.setVisibility(View.GONE);
		} else {
			departureField.setText("");
			destinationField.setText("");
			searchField.setVisibility(View.GONE);
			searchTripGroup.setVisibility(View.VISIBLE);
		}
	}

	private class PlaceListener implements OnItemClickListener {

		private boolean isDeparture;

		public PlaceListener(boolean isDeparture) {
			// TODO Auto-generated constructor stub
			this.isDeparture = isDeparture;
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			int i;
			for (i = 0; i < placeDBName.length; i++) {
				if (placeDBName[i].equals(arg0.getItemAtPosition(arg2))) {
					if (isDeparture)
						start_id = placeDB.get(i).getId();
					else
						end_id = placeDB.get(i).getId();
					break;
				}
			}

		}

	};
}
