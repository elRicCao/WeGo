package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;
import java.util.Date;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity;
//import vn.edu.hcmut.wego.activity.MainActivity.ShowHideButtonBarOnTouchListener;
import vn.edu.hcmut.wego.adapter.NewsAdapter;
import vn.edu.hcmut.wego.adapter.NewsAdapter.CommentListener;
import vn.edu.hcmut.wego.dialog.CommentDialog;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class NewsFragment extends TabFragment {

	private static final String title = "News";
	private static final int iconRes = R.drawable.ic_tab_news;

	private int userId;
	private Context context;
	private NewsAdapter newsAdapter;
	private ProgressBar progressBar;
	private LinearLayout bottomBar;
	private ListView newsList;

	public NewsFragment(Context context, int userId) {
		super(title, iconRes);
		this.userId = userId;
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		newsAdapter = new NewsAdapter(context, new ArrayList<News>());
		newsAdapter.setCommentListener(commentListener);

		// TODO: Debug entry
		addFakeData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// Inflate fragment layout
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);

		// Get control of all views
		progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_news_progress_bar);
		bottomBar = (LinearLayout) rootView.findViewById(R.id.fragment_news_bottom_bar);
		newsList = (ListView) rootView.findViewById(R.id.fragment_news_list);

		// Set up list view, touch event for list view, and adapter
		newsList.setOnTouchListener(new MainActivity.BottomBarListener(context, bottomBar));
		newsList.setAdapter(newsAdapter);

		// If news list is empty, show progress bar and create a server request to fetch data from server
		// if (newsAdapter.isEmpty()) {
		// progressBar.setVisibility(View.VISIBLE);
		// TODO: Working
		// LocalRequest.newsFeedRequest(context, new NewsFeedCallBack() {
		// @Override
		// public void onCompleted(ArrayList<News> result) {
		// newsAdapter.addAll(result);
		// progressBar.setVisibility(View.GONE);
		// }
		// }).executeAsync();
		// }

		return rootView;
	}

	private CommentListener commentListener = new CommentListener() {
		@Override
		public void onComment(News newsItem) {
			FragmentManager fragmentManager = NewsFragment.this.getFragmentManager();
			CommentDialog commentDialog = new CommentDialog(context);
			commentDialog.show(fragmentManager, "comment_dialog");
		}
	};

	private void addFakeData() {
		User user1 = new User();
		user1.setName("elRic");

		User user2 = new User();
		user2.setName("Mai Huu Nhan");

		User user3 = new User();
		user3.setName("Phan Tran Viet");

		User user4 = new User();
		user4.setName("SuperBo");

		// TODO: Sample post
		News news = new News();
		news.setOwner(user1);
		news.setType(NewsType.POST);
		news.setTime(new Date());
		news.setContent("Sample Post!");
		news.setNumOfLikes(0);
		news.setNumOfComments(0);
		newsAdapter.add(news);

		// TODO: Sample post with comment
		news = new News();
		news.setOwner(user1);

		ArrayList<User> actors = new ArrayList<User>();
		actors.add(user2);
		actors.add(user3);
		actors.add(user4);
		news.setActors(actors);

		news.setType(NewsType.COMMENT_POST);
		news.setTime(new Date());
		news.setContent("Sample Post with Comment!");
		news.setNumOfLikes(0);
		news.setNumOfComments(actors.size());

		newsAdapter.add(news);

		// TODO: Sample Post with like
		news = new News();
		news.setOwner(user2);

		actors = new ArrayList<User>();
		actors.add(user1);
		news.setActors(actors);

		news.setType(NewsType.LIKE_POST);
		news.setTime(new Date());
		news.setContent("Sampe Post with like!");
		news.setNumOfLikes(actors.size());
		news.setNumOfComments(0);

		newsAdapter.add(news);
		newsAdapter.add(news);
		newsAdapter.add(news);
		newsAdapter.add(news);
	}
}
