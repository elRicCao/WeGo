package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.TabPagerAdapter;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.dialog.TripInviteDialog;
import vn.edu.hcmut.wego.fragment.MoreFragment;
import vn.edu.hcmut.wego.fragment.NewsFragment;
import vn.edu.hcmut.wego.fragment.SocialFragment;
import vn.edu.hcmut.wego.fragment.TabFragment;
import vn.edu.hcmut.wego.fragment.TripFragment;
import vn.edu.hcmut.wego.view.SlidingTabLayout;
import android.content.Context;
import android.content.Intent;
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

public class MainActivity extends ActionBarActivity {

	private ViewPager viewPager;
	private TabPagerAdapter pagerAdapter;
	private ArrayList<TabFragment> fragments;
	private SlidingTabLayout slidingTab;
	private ActionBar actionBar;
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		userId = getIntent().getExtras().getInt(Constant.INTENT_USER_ID);

		fragments = new ArrayList<TabFragment>();
		fragments.add(new TripFragment(this, userId));
		fragments.add(new NewsFragment(this, userId));
		fragments.add(new SocialFragment(this, userId));
		fragments.add(new MoreFragment(this, userId));

		viewPager = (ViewPager) findViewById(R.id.activity_main_pager);
		pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(pagerAdapter);

		slidingTab = (SlidingTabLayout) findViewById(R.id.activity_main_sliding_tabs);
		slidingTab.setViewPager(viewPager);
		slidingTab.setOnPageChangeListener(new PageChangeListener());

		actionBar = getSupportActionBar();
		actionBar.setTitle(pagerAdapter.getPageTitle(0));
	}

	private class PageChangeListener extends SimpleOnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			actionBar.setTitle(pagerAdapter.getPageTitle(position));
		}
	}

	public static class BottomBarListener implements OnTouchListener {

		private Context context;
		private LinearLayout buttonBar;
		private float baseY = 0.0f;

		public BottomBarListener(Context context, LinearLayout buttonBar) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			break;
		case R.id.action_search:
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			break;

		case R.id.action_invite_user:
			TripFragment fragment = (TripFragment) fragments.get(0);
			Context context = fragment.getContext();
			int tripId = fragment.getTripId();
			TripInviteDialog dialog = new TripInviteDialog(context, tripId);
			dialog.show(fragment.getFragmentManager(), "trip_invite_fragment");
		}
		return super.onOptionsItemSelected(item);
	}
}
