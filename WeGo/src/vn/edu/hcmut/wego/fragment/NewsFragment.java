package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity.ShowHideButtonBarOnTouchListener;
import vn.edu.hcmut.wego.adapter.NewsAdapter;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.storage.LocalRequest;
import vn.edu.hcmut.wego.storage.LocalRequest.NewsFeedCallBack;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class NewsFragment extends BaseFragment {

	private Context context;
	private ArrayList<News> news;
	private NewsAdapter newsAdapter;
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
		news = new ArrayList<News>();
		newsAdapter = new NewsAdapter(context, news);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate fragment layout
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);

		// Set up progress bar. This progress bar is shown only on startup when list is empty
		progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_news_progress_bar);

		// Set up button bar
		buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_news_button_bar);

		// Set up list view, touch event for list view, and adapter
		newsList = (ListView) rootView.findViewById(R.id.fragment_news_list);
		newsList.setOnTouchListener(new ShowHideButtonBarOnTouchListener(context, buttonBar));
		newsList.setAdapter(newsAdapter);

		// If news list is empty, show progress bar and create a local request to local database
		if (newsAdapter.isEmpty()) {
			progressBar.setVisibility(View.VISIBLE);
			//TODO: Working
//			LocalRequest.newsFeedRequest(context, new NewsFeedCallBack() {
//				@Override
//				public void onCompleted(ArrayList<News> result) {
//					newsAdapter.addAll(result);
//					progressBar.setVisibility(View.GONE);
//				}
//			}).executeAsync();
		}

		return rootView;
	}
}
