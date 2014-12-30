package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.dialog.ChatDialog;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserInfoActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		final ActionBar actionBar = getSupportActionBar();

		Intent intent = getIntent();
		final int user_id = intent.getExtras().getInt("user_id");
		final int current_user_id = intent.getExtras().getInt("current_user_id");
		final String userName = intent.getStringExtra("userName");
		
		actionBar.setTitle(userName);

		// Info group
		final TextView nameView = (TextView) findViewById(R.id.activity_user_info_name);
		final TextView descriptionView = (TextView) findViewById(R.id.activity_user_info_description);
		final TextView friendView = (TextView) findViewById(R.id.activity_user_info_num_of_friend);
		final TextView followView = (TextView) findViewById(R.id.activity_user_info_num_of_follow);
		final TextView tripView = (TextView) findViewById(R.id.activity_user_info_num_of_trip);

		// Reputation group
		final TextView reputationView = (TextView) findViewById(R.id.activity_user_info_rating);
		final RatingBar ratingBar = (RatingBar) findViewById(R.id.activity_user_info_rating_bar);
		Utils.setUpRatingBar(this, ratingBar);
		final TextView rateTime = (TextView) findViewById(R.id.activity_user_info_rating_times);

		// Contact group
		final LinearLayout contactGroup = (LinearLayout) findViewById(R.id.activity_user_info_contact_group);
		final TextView locationView = (TextView) findViewById(R.id.activity_user_info_location);
		final TextView phoneView = (TextView) findViewById(R.id.activity_user_info_phone);
		final TextView emailView = (TextView) findViewById(R.id.activity_user_info_email);

		// Send message
		final LinearLayout messageButton = (LinearLayout) findViewById(R.id.activity_user_info_message);
		
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.activity_user_info_bottom_bar);
		final ScrollView contentView = (ScrollView) findViewById(R.id.activity_user_info_content);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_user_info_progress);
		
		contentView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		
		contentView.setOnTouchListener(new MainActivity.BottomBarListener(this, bottomBar));

		// Load from server
		JSONObject params = new JSONObject();
		try {
			params.put("user_id", user_id);
			params.put("current_user_id", current_user_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_USER_INFO, params, new ServerRequestCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCompleted(Object... results) {
				ArrayList<Object> obj = (ArrayList<Object>) results[0];
				User user = (User) obj.get(0);

				nameView.setText(user.getName());
				actionBar.setTitle(user.getName());
				friendView.setText(String.valueOf(user.getNumOfFriend()));
				followView.setText(String.valueOf(user.getNumOfFollow()));
				tripView.setText(String.valueOf(user.getNumOfTrip()));

				reputationView.setText(String.valueOf(user.getAverageVote()));
				ratingBar.setRating((float) user.getAverageVote());
				rateTime.setText("(" + String.valueOf(user.getNumOfVote()) + ")");

				if (Integer.parseInt(obj.get(1).toString()) == 0) {
					contactGroup.setVisibility(View.GONE);
				} else {
					contactGroup.setVisibility(View.VISIBLE);
					locationView.setText(user.getLocation().getName());
					phoneView.setText(user.getPhone());
					emailView.setText(user.getEmail());
				}
				
				progressBar.setVisibility(View.GONE);
				contentView.setVisibility(View.VISIBLE);

				messageButton.setOnClickListener(new ChatDialog.ChatDialogListener(UserInfoActivity.this, UserInfoActivity.this.getSupportFragmentManager(), current_user_id, user_id));
			}
		}).executeAsync();

		LinearLayout reviewList = (LinearLayout) findViewById(R.id.activity_user_info_reputation_list);
		
		View review1 = LayoutInflater.from(this).inflate(R.layout.item_review_user, reviewList, false);
		RatingBar rateReview = (RatingBar) review1.findViewById(R.id.item_review_user_rating_bar);
		Utils.setUpRatingBar(this, rateReview);
		rateReview.setRating(4.5f);
		
		reviewList.addView(review1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class UserInfoListener implements OnClickListener {

		private Context context;
		private int userId;
		private int currentUserId;
		private String userName;

		public UserInfoListener(Context context, int userId, int currentUserId, String userName) {
			this.context = context;
			this.userId = userId;
			this.currentUserId = currentUserId;
			this.userName = userName;
		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, UserInfoActivity.class);
			intent.putExtra(Constant.INTENT_USER_ID, userId);
			intent.putExtra("userName", userName);
			intent.putExtra("currentUser", currentUserId);

			context.startActivity(intent);
		}

	}
}
