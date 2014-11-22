package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.TabPagerAdapter;
import vn.edu.hcmut.wego.fragment.MoreFragment;
import vn.edu.hcmut.wego.fragment.NewsFragment;
import vn.edu.hcmut.wego.fragment.SocialFragment;
import vn.edu.hcmut.wego.fragment.TripFragment;
import vn.edu.hcmut.wego.fragment.TabFragment;
import vn.edu.hcmut.wego.view.SlidingTabLayout;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {

	private ViewPager viewPager;
	private TabPagerAdapter pagerAdapter;
	private ArrayList<TabFragment> fragments;
	private SlidingTabLayout slidingTab;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		fragments = new ArrayList<TabFragment>();
		fragments.add(new TripFragment(this));
		fragments.add(new NewsFragment(this));
		fragments.add(new SocialFragment(this));
		fragments.add(new MoreFragment(this));

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

	public static class TileOnTouchListerner implements OnTouchListener {
		private float fromX = 1, toX = 0.99f;
		private float fromY = 1, toY = 0.98f;
		private float pivotX, pivotY;

		@Override
		public boolean onTouch(View view, MotionEvent event) {

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				pivotX = event.getX() / view.getWidth();
				pivotY = event.getY() / view.getHeight();
				onScaleDown(view);
				break;

			case MotionEvent.ACTION_UP:
				onScaleUp(view);
				view.performClick();
				break;

			case MotionEvent.ACTION_MOVE:
				onScaleUp(view);
				break;
			}
			return true;
		}

		protected void onScaleUp(View view) {
			ScaleAnimation scaleAnimation = new ScaleAnimation(toX, fromX, toY, fromY, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
			scaleAnimation.setFillEnabled(true);
			scaleAnimation.setFillAfter(true);
			scaleAnimation.setDuration(150);
			view.startAnimation(scaleAnimation);
		}

		protected void onScaleDown(View view) {
			ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
			scaleAnimation.setFillEnabled(true);
			scaleAnimation.setFillAfter(true);
			scaleAnimation.setDuration(150);
			view.startAnimation(scaleAnimation);
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
}
