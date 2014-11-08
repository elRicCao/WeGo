package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

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
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.view.SlidingTabLayout;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;

public class MainActivity extends ActionBarActivity {

	private SectionsPagerAdapter sectionsPagerAdapter;
	private ViewPager viewPager;
	private SlidingTabLayout slidingTabLayout;
	private Session session;
	private UiLifecycleHelper uiHelper;
	private ActionBar actionBar;

	private enum ViewMode {
		SURFING, MOVING
	};

	private ViewMode currentMode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// UIHelper for controling Facebook session's state
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		// Set up the action bar.
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.activity_main_pager);

		// Set up the sliding tab
		slidingTabLayout = (SlidingTabLayout) findViewById(R.id.activity_main_sliding_tabs);
		slidingTabLayout.setOnPageChangeListener(new PageChangeListener());

		// Create the adapter that will return a fragment for each of the three primary sections of the activity.
		// MUST set fragments before use
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set mode
		setViewingMode(ViewMode.SURFING);

		session = Session.getActiveSession();
		
		//TODO: Debug entry
		debug();
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
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
			onLogOut();

			// Action Search
		case R.id.action_search:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Logging out sequence: delete user preferences, database and turn back to login screen
	 */
	private void onLogOut() {
		// Clear user preference
		SharedPreferences preferences = getSharedPreferences(Constant.PREFS_NAME, MODE_PRIVATE);
		preferences.edit().clear().commit();

		// Delete database
		deleteDatabase(Constant.DATABASE_NAME);

		// Clear Facebook data
		if (session != null) {
			if (!session.isClosed())
				session.closeAndClearTokenInformation();
		} else {
			session = new Session(this);
			Session.setActiveSession(session);
			session.closeAndClearTokenInformation();
		}

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
	private void setViewingMode(ViewMode mode) {
		if (currentMode != mode) {
			ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
			if (mode == ViewMode.SURFING) {
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
			actionBar.setTitle(viewPager.getAdapter().getPageTitle(0));
			slidingTabLayout.setViewPager(viewPager);
			currentMode = mode;
		}
	}

	/**
	 * On Touch Listener used for show and hide bottom button bar in child fragments
	 */
	public static class ShowHideButtonBarOnTouchListener implements OnTouchListener {

		private Context context;
		private LinearLayout buttonBar;
		private float baseY = 0.0f;

		public ShowHideButtonBarOnTouchListener(Context context, LinearLayout buttonBar) {
			this.buttonBar = buttonBar;
			this.context = context;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			int action = MotionEventCompat.getActionMasked(event);
			int minimumDistance = context.getResources().getDisplayMetrics().heightPixels * 10 / 100;
			view.performClick();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				baseY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				// Swipe Up
				if (event.getY() - baseY > minimumDistance && buttonBar.getVisibility() == View.GONE) {
					Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_from_bottom);
					buttonBar.startAnimation(animation);
					buttonBar.setVisibility(View.VISIBLE);

				}
				// Swipe Down
				else if (baseY - event.getY() > minimumDistance && buttonBar.getVisibility() == View.VISIBLE) {
					Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_down_to_bottom);
					buttonBar.startAnimation(animation);
					buttonBar.setVisibility(View.GONE);
				}
				break;
			}
			return false;
		}
	}

	/**
	 * Page change listener used for setting activity title corresponding with current fragment
	 */
	private class PageChangeListener extends SimpleOnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			actionBar.setTitle(viewPager.getAdapter().getPageTitle(position));
		}
	}
	
	private void debug() {
		Log.i("Debug", "Debug Server Service");
		try {
			JSONObject params = new JSONObject();
			params.put("id", 1);
			ServerRequest.newServerRequest(RequestType.FETCH_NEWS, params, null).executeAsync();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}