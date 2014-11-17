package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.FollowAdapter;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper;
import vn.edu.hcmut.wego.storage.LocalRequest;
import vn.edu.hcmut.wego.storage.LocalRequest.NewsFeedCallBack;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class FollowFragment extends WeGoFragment {

	private Context context;
	private ArrayList<News> news;
	private FollowAdapter followAdapter;
	private ListView newsList;
	private LinearLayout buttonBar;
	private ProgressBar progressBar;

	public FollowFragment(Context context) {
		this.context = context;
		setTitle(context.getString(R.string.title_fragment_follow));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		news = new ArrayList<News>();
		followAdapter = new FollowAdapter(context, news);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Inflate fragment layout
		View rootView = inflater.inflate(R.layout.fragment_follow, container, false);

		// Set up progress bar
		progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_follow_progress_bar);

		// Set up button bar
		buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_follow_button_bar);

		// Set up list view
		newsList = (ListView) rootView.findViewById(R.id.fragment_follow_list);
		newsList.setAdapter(followAdapter);
//		newsList.setOnTouchListener(new ShowHideButtonBarOnTouchListener(context, buttonBar));

		// If news list is empty, show progress bar
		if (followAdapter.isEmpty()) {
			progressBar.setVisibility(View.VISIBLE);
			//TODO: Working
//			LocalRequest.newsFeedRequest(context, new NewsFeedCallBack() {
//				
//				@Override
//				public void onCompleted(ArrayList<News> result) {
//					progressBar.setVisibility(View.GONE);
//					followAdapter.addAll(result);
//				}
//			}).executeAsync();
		}

		return rootView;
	}
}
