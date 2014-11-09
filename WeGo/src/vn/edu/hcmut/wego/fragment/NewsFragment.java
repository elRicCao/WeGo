package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;
import java.util.Date;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity.ShowHideButtonBarOnTouchListener;
import vn.edu.hcmut.wego.adapter.NewsAdapter;
import vn.edu.hcmut.wego.adapter.NewsAdapter.CommentButtonListener;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.User;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
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

		// TODO: Debug entry
		debug();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

		// Inflate fragment layout
		final View rootView = inflater.inflate(R.layout.fragment_news, container, false);

		// Set up progress bar. This progress bar is shown only on startup when list is empty
		progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_news_progress_bar);

		// Set up button bar
		buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_news_button_bar);

		// Set up list view, touch event for list view, and adapter
		newsList = (ListView) rootView.findViewById(R.id.fragment_news_list);
		newsList.setOnTouchListener(new ShowHideButtonBarOnTouchListener(context, buttonBar));
		newsList.setAdapter(newsAdapter);

		newsAdapter.setCommentButtonListener(new CommentButtonListener() {
			@Override
			public void onClick(News newsItem) {
				FragmentManager fragmentManager = NewsFragment.this.getFragmentManager();
				CommentDialog commentDialog = new CommentDialog();
				commentDialog.show(fragmentManager, "comment_dialog");
			}
		});

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

	private class CommentDialog extends DialogFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.dialog_comment, container, false);
			
			return rootView;
		}
	}

	private void debug() {
		User user1 = new User();
		user1.setName("elRic");
		
		User user2 = new User();
		user2.setName("Mai Huu Nhan");
		
		User user3 = new User();
		user3.setName("Phan Tran Viet");

		User user4 = new User();
		user4.setName("SuperBo");
		
		//TODO: Sample post
		News news = new News();
		news.setOwner(user1);
		news.setType(NewsType.POST);
		news.setTime(new Date());
		news.setContent("Sample Post!");
		news.setNumOfLikes(0);
		news.setNumOfComments(0);
		newsAdapter.add(news);
		
		//TODO: Sample post with comment
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
		
		//TODO: Sample Post with like
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
	}
}
