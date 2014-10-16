package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.SectionsPagerAdapter;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.fragment.BaseFragment;
import vn.edu.hcmut.wego.fragment.FollowFragment;
import vn.edu.hcmut.wego.fragment.FriendsFragment;
import vn.edu.hcmut.wego.fragment.MapFragment;
import vn.edu.hcmut.wego.fragment.MembersFragment;
import vn.edu.hcmut.wego.fragment.MoreFragment;
import vn.edu.hcmut.wego.fragment.NewsFragment;
import vn.edu.hcmut.wego.fragment.ScheduleFragment;
import vn.edu.hcmut.wego.fragment.TripsFragment;
import vn.edu.hcmut.wego.view.SlidingTabLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class MainActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

	private SectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	private SlidingTabLayout slidingTabLayout;

	private enum Mode {
		SURFING, MOVING
	};

	private Mode currentMode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(false);

		// Spinner
		SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.mode_array, R.layout.spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(spinnerAdapter, this);

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.activity_main_pager);

		// Set up the sliding tab
		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.activity_main_sliding_tabs);

		// Create the adapter that will return a fragment for each of the three primary sections of the activity.
		// MUST set fragments before use
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set mode
		setMode(Mode.SURFING);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemId) {
		if (position == Mode.SURFING.ordinal()) {
			setMode(Mode.SURFING);
			return true;
		}
		if (position == Mode.MOVING.ordinal()) {
			setMode(Mode.MOVING);
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks
		int id = item.getItemId();
		switch (id) {

		// Action Settings
		case R.id.action_settings:
			return true;

			// Action Log out
		case R.id.action_logout:
			logOutSequence();

			// Action Search
		case R.id.action_search:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Logging out sequence: delete user preferences, database and turn back to login screen
	 */
	private void logOutSequence() {
		// Clear user preference
		SharedPreferences preferences = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		preferences.edit().clear().commit();

		// Delete database
		deleteDatabase(Constant.DATABASE_NAME);

		// Turn back to login screen
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * Set up tabs for corresponding mode
	 * 
	 * @param mode
	 */
	private void setMode(Mode mode) {
		if (currentMode != mode) {
			ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
			if (mode == Mode.SURFING) {
				fragments.add(new NewsFragment(this));
				fragments.add(new FriendsFragment(this));
				fragments.add(new FollowFragment(this));
				fragments.add(new TripsFragment(this));
				fragments.add(new MoreFragment(this));
			} else {
				fragments.add(new MapFragment(this));
				fragments.add(new MembersFragment(this));
				fragments.add(new ScheduleFragment(this));
			}
			sectionsPagerAdapter.setFragments(fragments);
			
			// If this is the initial launch, set adapter for view pager, 
			// Otherwise notify data of adapter has changed
			if (currentMode == null) {
				viewPager.setAdapter(sectionsPagerAdapter);
			} else {
				sectionsPagerAdapter.notifyDataSetChanged();
			}
			slidingTabLayout.setViewPager(viewPager);
			currentMode = mode;
		}
	}
}