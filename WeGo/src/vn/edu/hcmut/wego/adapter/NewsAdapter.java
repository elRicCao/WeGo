package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsAdapter extends ArrayAdapter<News> {

	private Context context;
	private ArrayList<News> news;

	private CommentListener listener;
	private ViewHolder holder;

	// private DatabaseOpenHelper database;
	private News newsItem;

	public NewsAdapter(Context context, ArrayList<News> news) {
		super(context, 0, news);
		this.context = context;
		this.news = news;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_news, parent, false);

			holder = new ViewHolder();
			holder.actionGroup = (LinearLayout) convertView.findViewById(R.id.item_news_action_group);
			holder.actorView = (TextView) convertView.findViewById(R.id.item_news_actor);
			holder.otherActorView = (TextView) convertView.findViewById(R.id.item_news_actor_others);
			holder.connectorView = (TextView) convertView.findViewById(R.id.item_news_actor_connector);
			holder.actionView = (TextView) convertView.findViewById(R.id.item_news_actor_action);

			holder.ownerNameView = (TextView) convertView.findViewById(R.id.item_news_user_name);
			holder.ownerActionView = (TextView) convertView.findViewById(R.id.item_news_user_action);
			holder.ownerImageView = (ImageView) convertView.findViewById(R.id.item_news_user_image);

			holder.timeView = (TextView) convertView.findViewById(R.id.item_news_time);
			holder.contentView = (TextView) convertView.findViewById(R.id.item_news_content);
			holder.likeView = (TextView) convertView.findViewById(R.id.item_news_like);
			holder.commentView = (TextView) convertView.findViewById(R.id.item_news_comment);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_news_photo);

			holder.reviewGroup = (LinearLayout) convertView.findViewById(R.id.item_news_review_group);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.item_news_rate);
			Utils.setUpRatingBar(context, holder.ratingBar);
			holder.placeName = (TextView) convertView.findViewById(R.id.item_news_place);
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		newsItem = news.get(position);
		User owner = newsItem.getOwner();

		// Get view holder from view tag
		holder = (ViewHolder) convertView.getTag();

		// Set up action group, showing the action of other users causing the news
		if (newsItem.getType() == NewsType.POST || newsItem.getType() == NewsType.PHOTO || newsItem.getType() == NewsType.REVIEW) {
			holder.actionGroup.setVisibility(View.GONE);
		} else {
			holder.actorView.setText(newsItem.getActors().get(0).getName());
			holder.actionView.setText(Utils.getActionText(context, newsItem.getType()));
		}

		if (newsItem.getType() == NewsType.REVIEW) {
			holder.reviewGroup.setVisibility(View.VISIBLE);
			holder.ratingBar.setRating(newsItem.getRate());
			holder.placeName.setText(newsItem.getPlace().getName());
		} else {
			holder.reviewGroup.setVisibility(View.GONE);
		}

		// Set up owner's name. If this view is clicked, jump to user info screen
		holder.ownerNameView.setText(owner.getName());
		holder.ownerNameView.setOnClickListener(new UserInfoActivity.UserInfoListener(context, owner.getId(), Utils.getUserId(context), owner.getName()));

		if (owner.getImage() == null || owner.getImage().isEmpty()) {
			Picasso.with(context).load(R.drawable.default_user).into(holder.ownerImageView);
		} else {
			Picasso.with(context).load(owner.getImage()).into(holder.ownerImageView);
		}

		// Set up owner action
		holder.ownerActionView.setText(Utils.getOwnerActionText(context, newsItem.getType()));

		// Set up time view
		holder.timeView.setText(Utils.formatDate(newsItem.getTime()));

		// Set up content view
		holder.contentView.setText(newsItem.getContent());

		// Set up like view
		if (newsItem.isLiked())
			holder.likeView.setSelected(true);

		holder.likeView.setOnClickListener(likeListener);
		holder.likeView.setText(String.valueOf(newsItem.getNumOfLikes()));

		holder.commentView.setOnClickListener(commentClickListener);
		holder.commentView.setText(String.valueOf(newsItem.getNumOfComments()));

		if (newsItem.getType() == NewsType.REVIEW) {
			holder.commentView.setVisibility(View.GONE);
		} else {
			holder.commentView.setVisibility(View.VISIBLE);
		}

		if (newsItem.getType() == NewsType.PHOTO) {
			holder.imageView.setVisibility(View.VISIBLE);
			Picasso.with(context).load(newsItem.getPhoto()).into(holder.imageView);
		}

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
		ImageView ownerImageView;

		// Time
		TextView timeView;

		// Content
		TextView contentView;

		// Like and comment
		TextView likeView;
		TextView commentView;

		ImageView imageView;

		LinearLayout reviewGroup;
		RatingBar ratingBar;
		TextView placeName;
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
			switch (newsItem.getType()) {
			case LIKE_POST:
			case COMMENT_POST:
			case POST:
				JSONObject params = new JSONObject();
				try {
					params.put("post", newsItem.getId());
					params.put("user", Utils.getUserId(context));
					params.put("owner", newsItem.getOwner().getId());
					params.put("like", newsItem.isLiked());

				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.i("Debug news adapter", params.toString());
				ServerRequest.newServerRequest(RequestType.ACTION_LIKE_POST, params, new ServerRequestCallback() {
					@SuppressWarnings("unchecked")
					@Override
					public void onCompleted(Object... results) {
						int numLikes = (Integer) results[0];
						holder.likeView.setText(String.valueOf(numLikes));
						newsItem.setLiked(!newsItem.isLiked());
						holder.likeView.setSelected(!holder.likeView.isSelected());
					}
				}).executeAsync();
				break;

			case COMMENT_PHOTO:
			case LIKE_PHOTO:
			case PHOTO:
				holder.likeView.setSelected(true);
				holder.likeView.setText(String.valueOf(newsItem.getNumOfLikes() + 1));
				break;
			case REVIEW:
			case LIKE_REVIEW:
				holder.likeView.setSelected(true);
				holder.likeView.setText(String.valueOf(newsItem.getNumOfLikes() + 1));
				break;
			default:
				break;
			}
		}
	};

	public void setCommentListener(CommentListener listener) {
		this.listener = listener;
	}

	public interface CommentListener {
		public void onComment(News newsItem);
	}
}
