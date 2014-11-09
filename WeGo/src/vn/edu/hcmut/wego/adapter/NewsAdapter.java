package vn.edu.hcmut.wego.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	private Context context;
	private ArrayList<News> news;
	private CommentButtonListener callback;

	// private DatabaseOpenHelper database;

	public NewsAdapter(Context context, ArrayList<News> news) {
		super(context, 0, news);
		this.context = context;
		this.news = news;
		// database = new DatabaseOpenHelper(context);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_news, parent, false);
		}

		// Get news item corresponding by postion
		final News newsItem = news.get(position);
		User user = newsItem.getOwner();

		// Set up action indicator bar
		RelativeLayout actionGroup = (RelativeLayout) convertView.findViewById(R.id.item_news_action_group);
		if (newsItem.getType() == NewsType.POST || newsItem.getType() == NewsType.PHOTO || newsItem.getType() == NewsType.REVIEW)
			actionGroup.setVisibility(View.GONE);
		else {
			TextView actorView = (TextView) convertView.findViewById(R.id.item_news_actor);
			actorView.setText(newsItem.getActors().get(0).getName());

			if (newsItem.getActors().size() > 1) {
				convertView.findViewById(R.id.item_news_actor_connector).setVisibility(View.VISIBLE);

				TextView otherActorView = (TextView) convertView.findViewById(R.id.item_news_actor_others);
				if (newsItem.getActors().size() == 2)
					otherActorView.setText(newsItem.getActors().get(1).getName());
				else
					otherActorView.setText(String.valueOf(newsItem.getActors().size() - 1) + context.getResources().getString(R.string.item_news_actor_others));
			}

			TextView actionView = (TextView) convertView.findViewById(R.id.item_news_actor_action);
			actionView.setText(getActionText(newsItem.getType()));
		}

		// Set up owner name view
		TextView ownerNameView = (TextView) convertView.findViewById(R.id.item_news_user_name);
		ownerNameView.setText(user.getName());

		// Set up owner action
		TextView ownerActionView = (TextView) convertView.findViewById(R.id.item_news_user_action);
		ownerActionView.setText(getOwnerActionText(newsItem.getType()));

		// Set up time view
		TextView timeView = (TextView) convertView.findViewById(R.id.item_news_time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d 'at' hh:mma", Locale.getDefault());
		timeView.setText(dateFormat.format(newsItem.getTime()).replace("AM", "am").replace("PM", "pm"));

		// Set up content view
		TextView contentView = (TextView) convertView.findViewById(R.id.item_news_content);
		contentView.setText(newsItem.getContent());

		// Set up like indicator view
		TextView likesView = (TextView) convertView.findViewById(R.id.item_news_num_of_likes);
		likesView.setText(String.valueOf(newsItem.getNumOfLikes()) + context.getResources().getString(R.string.item_news_like));
		if (newsItem.getNumOfLikes() == 0)
			likesView.setVisibility(View.GONE);

		// Set up comment indicator view
		TextView commentsView = (TextView) convertView.findViewById(R.id.item_news_num_of_comments);
		commentsView.setText(String.valueOf(newsItem.getNumOfComments()) + context.getResources().getString(R.string.item_news_comment));
		if (newsItem.getNumOfComments() == 0)
			commentsView.setVisibility(View.GONE);

		// Set up like button
		TextView likeButton = (TextView) convertView.findViewById(R.id.item_news_button_like);
		likeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: create server request to push like event to server, receive number of like update, set text to Unlike
			}
		});

		// Set up comment button
		TextView commentButton = (TextView) convertView.findViewById(R.id.item_news_button_comment);
		commentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null)
					callback.onClick(newsItem);
			}
		});
		return convertView;
	}

	public void setCommentButtonListener(CommentButtonListener callback) {
		this.callback = callback;
	}

	public interface CommentButtonListener {
		public void onClick(News newsItem);
	}

	/**
	 * Get action text of actor from resources
	 * 
	 * @param type
	 * @return action text
	 */
	private String getActionText(NewsType type) {
		String actionText = new String();
		switch (type) {
		case COMMENT_POST:
			actionText = context.getResources().getString(R.string.item_news_actor_action_comment_post);
			break;
		case LIKE_POST:
			actionText = context.getResources().getString(R.string.item_news_actor_action_like_post);
			break;
		case COMMENT_PHOTO:
			actionText = context.getResources().getString(R.string.item_news_actor_action_comment_photo);
			break;
		case LIKE_PHOTO:
			actionText = context.getResources().getString(R.string.item_news_actor_action_like_photo);
			break;
		case LIKE_REVIEW:
			actionText = context.getResources().getString(R.string.item_news_actor_action_like_review);
			break;
		default:
			break;
		}
		return actionText;
	}

	/**
	 * Get action text of owner from resources
	 * 
	 * @param type
	 * @return action text
	 */
	private String getOwnerActionText(NewsType type) {
		String actionText = new String();
		switch (type) {
		case POST:
		case COMMENT_POST:
		case LIKE_POST:
			actionText = context.getResources().getString(R.string.item_news_user_action_write_status);
			break;

		case PHOTO:
		case COMMENT_PHOTO:
		case LIKE_PHOTO:
			actionText = context.getResources().getString(R.string.item_news_user_action_post_photo);
			break;

		case REVIEW:
		case LIKE_REVIEW:
			actionText = context.getResources().getString(R.string.item_news_user_action_write_review);
			break;
		default:
			break;
		}
		return actionText;
	}
}
