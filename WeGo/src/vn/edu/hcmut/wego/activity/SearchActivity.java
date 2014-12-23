package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.GroupSearchAdapter;
import vn.edu.hcmut.wego.adapter.PlaceSearchAdapter;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class SearchActivity extends ActionBarActivity {
	private ArrayList<Place> places;
	private ArrayList<Group> groups;
	private ArrayList<Trip> trips;
	private ArrayList<User> users;
	private ListView lv;
	private PlaceSearchAdapter adapterPlace;
	private GroupSearchAdapter adapterGroup;
	private UserSearchAdapter adapterUser;
	private PlaceSearchAdapter adapterTrip;
	private EditText tx;
	private int index;
	private int chosen;
	private Spinner spinner;
	private boolean empty = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		spinner = (Spinner) findViewById(R.id.spinner1);
		String[] myString = new String[] { "Group", "User", "Trip", "Place" };
		ArrayAdapter<String> adap = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, myString);
		spinner.setAdapter(adap);
		tx = (EditText) findViewById(R.id.autoCompleteTextView1);
		lv = (ListView) findViewById(R.id.listView1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				chosen = arg2;
				tx.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		tx.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				index = 0;
				if (s.length() != 0) {
					empty = false;
					JSONObject params = new JSONObject();
					try {
						params.put("index", index);
						params.put("input", s.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					switch (chosen) {
					case 0:

						ServerRequest.newServerRequest(RequestType.SEARCH_GROUP, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								groups = (ArrayList<Group>) results[0];

								adapterGroup = new GroupSearchAdapter(getBaseContext(), groups);
								lv.setAdapter(adapterGroup);
							}
						}).executeAsync();
						break;
					case 1:
						ServerRequest.newServerRequest(RequestType.SEARCH_USER, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								users = (ArrayList<User>) results[0];

								adapterUser = new UserSearchAdapter(getBaseContext(), users);
								lv.setAdapter(adapterUser);
							}
						}).executeAsync();
						break;
					case 2:

						break;
					case 3:
						ServerRequest.newServerRequest(RequestType.SEARCH_PLACE, params, new ServerRequestCallback() {

							@Override
							public void onCompleted(Object... results) {

								// Hide progress bar
								places = (ArrayList<Place>) results[0];

								adapterPlace = new PlaceSearchAdapter(getBaseContext(), places);
								lv.setAdapter(adapterPlace);
							}
						}).executeAsync();
						break;
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0 && !empty) {
					index += 7;
					JSONObject params = new JSONObject();
					try {
						params.put("index", index);
						params.put("input", tx.getText().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					switch (chosen) {
					case 0:
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
						break;
					case 1:
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
						break;
					case 2:

						break;
					case 3:
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
						break;
					}

				}
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:

			break;
		case R.id.action_search:
			onSearchRequested();
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
