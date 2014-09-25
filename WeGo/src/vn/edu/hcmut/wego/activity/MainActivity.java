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

	private final int MODE_SURFING_POSITION = 0;
	private final int MODE_MOVING_POSITION = 1;

	private int currentMode = MODE_SURFING_POSITION;

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

		// Create fragments array comprise of 4 fragments: NEWS, FRIENDS, FOLLOW, TRIPS and MORE
		ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
		fragments.add(new NewsFragment(this));
		fragments.add(new FriendsFragment(this));
		fragments.add(new FollowFragment(this));
		fragments.add(new TripsFragment(this));
		fragments.add(new MoreFragment(this));

		// Create the adapter that will return a fragment for each of the three primary sections of the activity.
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.activity_main_pager);
		viewPager.setAdapter(sectionsPagerAdapter);

		// Set up the sliding tab
		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.activity_main_sliding_tabs);
		slidingTabLayout.setViewPager(viewPager);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemId) {
		switch (position) {
		case MODE_SURFING_POSITION: {
			// If current mode is not surfing, delete all fragments 
			// and set new fragments comprising of NEWS, FRIENDS, FOLLOW, TRIPS and MORE
			if (currentMode != MODE_SURFING_POSITION) {
				ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
				fragments.add(new NewsFragment(this));
				fragments.add(new FriendsFragment(this));
				fragments.add(new FollowFragment(this));
				fragments.add(new TripsFragment(this));
				fragments.add(new MoreFragment(this));
				sectionsPagerAdapter.setNewFragments(fragments);
				sectionsPagerAdapter.notifyDataSetChanged();
				slidingTabLayout.setViewPager(viewPager);
				currentMode = MODE_SURFING_POSITION;
			}
			return true;
		}

		case MODE_MOVING_POSITION: {
			// If current mode is not moving, delete all fragments 
			// and set new fragments comprising of MAP, MEMBERS and SCHEDULE
			if (currentMode != MODE_MOVING_POSITION) {
				ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
				fragments.add(new MapFragment(this));
				fragments.add(new MembersFragment(this));
				fragments.add(new ScheduleFragment(this));
				sectionsPagerAdapter.setNewFragments(fragments);
				sectionsPagerAdapter.notifyDataSetChanged();
				slidingTabLayout.setViewPager(viewPager);
				currentMode = MODE_MOVING_POSITION;
			}
			return true;
		}

		default:
			return false;
		}
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
	 * Delete user preference when logging out
	 */
	private void logOutSequence() {
		// Clear user preference
		SharedPreferences preferences = getSharedPreferences(Constant.PREFS_NAME, 0);
		preferences.edit().clear().commit();

		// Turn back to login screen
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}