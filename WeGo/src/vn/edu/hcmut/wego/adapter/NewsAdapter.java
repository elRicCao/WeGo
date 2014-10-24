package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper.SelectType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	private Context context;
	private ArrayList<News> news;
	private DatabaseOpenHelper database;
	
	public NewsAdapter(Context context, ArrayList<News> news) {
		super(context, 0, news);
		this.context = context;
		this.news = news;
		database = new DatabaseOpenHelper(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_news, parent, false);
		}
		
		News newsItem = news.get(position);
		User user = database.selectUser(newsItem.getOwnerId(), SelectType.BRIEF);
		
		TextView ownerNameView = (TextView) convertView.findViewById(R.id.item_news_user_name);
		TextView timeView = (TextView) convertView.findViewById(R.id.item_news_time);
		TextView contentView = (TextView) convertView.findViewById(R.id.item_news_content);
		TextView likesView = (TextView) convertView.findViewById(R.id.item_news_num_of_likes);
		TextView commentsView = (TextView) convertView.findViewById(R.id.item_news_num_of_comments);
		
		ownerNameView.setText(user.getName());
		timeView.setText(newsItem.getTime().toString());
		contentView.setText(newsItem.getContent());
		likesView.setText(String.valueOf(newsItem.getNumOfLikes()) + " likes");
		commentsView.setText(String.valueOf(newsItem.getNumOfComments()) + " comments");
		
		return convertView;
	}
}
