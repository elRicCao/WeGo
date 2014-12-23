package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	private Context context;
	private ArrayList<News> news;

	private CommentListener listener;

	// private DatabaseOpenHelper database;
	private News newsItem;

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

			ViewHolder holder = new ViewHolder();
			holder.actionGroup = (LinearLayout) convertView.findViewById(R.id.item_news_action_group);
			holder.actorView = (TextView) convertView.findViewById(R.id.item_news_actor);
			holder.otherActorView = (TextView) convertView.findViewById(R.id.item_news_actor_others);
			holder.connectorView = (TextView) convertView.findViewById(R.id.item_news_actor_connector);
			holder.actionView = (TextView) convertView.findViewById(R.id.item_news_actor_action);
			holder.ownerNameView = (TextView) convertView.findViewById(R.id.item_news_user_name);
			holder.ownerActionView = (TextView) convertView.findViewById(R.id.item_news_user_action);
			holder.timeView = (TextView) convertView.findViewById(R.id.item_news_time);
			holder.contentView = (TextView) convertView.findViewById(R.id.item_news_content);
			holder.likeView = (TextView) convertView.findViewById(R.id.item_news_like);
			holder.commentView = (TextView) convertView.findViewById(R.id.item_news_comment);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_news_photo);
			holder.likeText = (TextView) convertView.findViewById(R.id.item_news_like);
			holder.commentText = (TextView) convertView.findViewById(R.id.item_news_comment);
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		newsItem = news.get(position);
		User owner = newsItem.getOwner();

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();

		// Set up action group, showing the action of other users causing the news
		if (newsItem.getType() == NewsType.POST || newsItem.getType() == NewsType.PHOTO || newsItem.getType() == NewsType.REVIEW) {
			holder.actionGroup.setVisibility(View.GONE);
		} else {
			holder.actorView.setText(newsItem.getActors().get(0).getName());
			holder.actionView.setText(Utils.getActionText(context, newsItem.getType()));

			int numOfActors = newsItem.getActors().size();
//			if (numOfActors > 1) {
//				holder.connectorView.setVisibility(View.VISIBLE);
//				if (numOfActors == 2) {
//					holder.otherActorView.setText(newsItem.getActors().get(1).getName());
//				} else {
//					holder.otherActorView.setText(String.valueOf(numOfActors - 1) + Utils.getString(context, R.string.item_news_actor_others));
//				}
//			}
		}
		if(position == 1)
		{
			holder.imageView.setVisibility(View.VISIBLE);
			holder.likeText.setText(String.valueOf(8));
			holder.commentText.setText(String.valueOf(12));
		}
		if(position == 2)
		{
		//	holder.imageView.setVisibility(View.VISIBLE);
			holder.likeText.setText(String.valueOf(13));
			holder.commentText.setText(String.valueOf(7));
		}
		// Set up owner's name. If this view is clicked, jump to user info screen
		holder.ownerNameView.setText(owner.getName());
		holder.ownerNameView.setOnClickListener(new UserInfoActivity.UserInfoListener(context, owner.getId(),1));

		// Set up owner action
		holder.ownerActionView.setText(Utils.getOwnerActionText(context, newsItem.getType()));

		// Set up time view
		holder.timeView.setText(Utils.formatDate(newsItem.getTime()));

		// Set up content view
		holder.contentView.setText(newsItem.getContent());

		// Set up like view
		holder.likeView.setOnClickListener(likeListener);
		
		holder.commentView.setOnClickListener(commentClickListener);

		return convertView;
	}

	/**
	 * Hold view of news item
	 */
	private static class ViewHolder {

		// Action group
		LinearLayout actionGroup;
		TextView actorView;
		TextView connectorView;
		TextView otherActorView;
		TextView actionView;

		// Owner info
		TextView ownerNameView;
		TextView ownerActionView;

		// Time
		TextView timeView;

		// Content
		TextView contentView;

		// Like and comment
		TextView likeView;
		TextView commentView;
		
		ImageView imageView;
		TextView likeText;
		TextView commentText;
		
		
	}

	private OnClickListener commentClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (listener != null && newsItem != null) {
				listener.onComment(newsItem);
			}
		}
	};
	private OnClickListener likeListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			view.setSelected(!view.isSelected());
		}
	};

	public void setCommentListener(CommentListener listener) {
		this.listener = listener;
	}

	public interface CommentListener {
		public void onComment(News newsItem);
	}
}
