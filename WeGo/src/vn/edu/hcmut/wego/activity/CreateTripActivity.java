package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.R.id;
import vn.edu.hcmut.wego.R.layout;
import vn.edu.hcmut.wego.R.menu;
import vn.edu.hcmut.wego.adapter.TabPagerAdapter;
import vn.edu.hcmut.wego.fragment.MoreFragment;
import vn.edu.hcmut.wego.fragment.NewsFragment;
import vn.edu.hcmut.wego.fragment.PickPlaceFragment;
import vn.edu.hcmut.wego.fragment.SocialFragment;
import vn.edu.hcmut.wego.fragment.TabFragment;
import vn.edu.hcmut.wego.fragment.TripFragment;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class CreateTripActivity extends ActionBarActivity {

	private ViewPager viewPager;
	private TabPagerAdapter pagerAdapter;
	private ArrayList<TabFragment> fragments;
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Configuration config = getResources().getConfiguration();

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		PickPlaceFragment pickPlaceFragment = new PickPlaceFragment(this);
		fragmentTransaction.replace(android.R.id.content, pickPlaceFragment);
		fragmentTransaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.current_trip, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
