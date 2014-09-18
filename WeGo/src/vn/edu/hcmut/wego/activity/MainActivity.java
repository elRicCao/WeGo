package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.SectionsPagerAdapter;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.fragment.BaseFragment;
import vn.edu.hcmut.wego.fragment.FriendsFragment;
import vn.edu.hcmut.wego.fragment.GroupsFragment;
import vn.edu.hcmut.wego.fragment.NewsFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

	private SectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create fragments array comprise of 3 fragments: NEWS, FRIENDS, GROUPS
		ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
		
		NewsFragment newsFragment = new NewsFragment();
		FriendsFragment friendsFragment = new FriendsFragment();
		GroupsFragment groupsFragment = new GroupsFragment();
		
		fragments.add(newsFragment);
		fragments.add(friendsFragment);
		fragments.add(groupsFragment);

		// Create the adapter that will return a fragment for each of the three primary sections of the activity.
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);

		// When swiping between different sections, select the corresponding tab
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// Create a tab with text corresponding to the page title defined by the adapter.
		// Also specify this Activity object, which implements the TabListener interface, as the callback (listener) for when this tab is selected.
		for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(sectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
		case R.id.action_settings:
			return true;
		case R.id.action_logout:
			deleteUserPreferences();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Delete user preference when logging out
	 */
	private void deleteUserPreferences() {
		SharedPreferences preferences = getSharedPreferences(Constant.PREFS_NAME, 0);
		preferences.edit().clear().commit();
	}

}
