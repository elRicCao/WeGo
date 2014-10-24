package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.NewsAdapter;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class NewsFragment extends BaseFragment {

	private Context context;
	private ArrayList<News> news;
	private NewsAdapter newsAdapter;
	private DatabaseOpenHelper database;
	private ProgressBar progressBar;
	private LinearLayout buttonBar;
	private ListView newsList;

	public NewsFragment(Context context) {
		this.context = context;
		setTitle(this.context.getString(R.string.title_fragment_news));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new DatabaseOpenHelper(context);
		news = new ArrayList<News>();
		newsAdapter = new NewsAdapter(context, news);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate fragment layout
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);

		// Set up progress bar
		progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_news_progress_bar);

		// Set up button bar
		buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_news_button_bar);

		// Set up list view
		newsList = (ListView) rootView.findViewById(R.id.fragment_news_list);
		newsList.setOnTouchListener(new ShowHideButtonBarOnTouchListener());
		newsList.setAdapter(newsAdapter);

		// If news list is empty, show progress bar
		if (newsAdapter.isEmpty()) {
			(new LoadDatabaseTask()).execute();
		}

		return rootView;
	}

	private class LoadDatabaseTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			news = database.selectAllNews();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (news.isEmpty()) {
				(new LoadServerTask()).execute();
			} else {
				progressBar.setVisibility(View.GONE);
				newsAdapter.addAll(news);
			}
		}

	}

	private class LoadServerTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (news.isEmpty()) {

			} else {
				progressBar.setVisibility(View.GONE);
				newsAdapter.addAll(news);
			}
		}

	}

	private class ShowHideButtonBarOnTouchListener implements OnTouchListener {

		private float baseY = 0.0f;

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
