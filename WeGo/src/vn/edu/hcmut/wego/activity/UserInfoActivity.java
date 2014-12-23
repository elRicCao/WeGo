package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class UserInfoActivity extends ActionBarActivity {

	private TextView userName;
	private TextView numOfFriend;
	private TextView numOfFollow;
	private TextView numOfTrip;
	private TextView reputation;
	private TextView userLocation;
	private TextView userPhone;
	private TextView userEmail;
	private TextView userActivities;
	private TableLayout layoutContact;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		actionBar = getSupportActionBar();
		actionBar.setTitle("Elric");

		RatingBar ratingBar = (RatingBar) findViewById(R.id.activity_user_info_rating_bar);
		LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
		layerDrawable.getDrawable(2).setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);

		userName = (TextView) findViewById(R.id.activity_user_info_name);
		numOfFriend = (TextView) findViewById(R.id.activity_user_info_num_of_friend);
		numOfFollow = (TextView) findViewById(R.id.activity_user_info_num_of_follow);
		numOfTrip = (TextView) findViewById(R.id.activity_user_info_num_of_trip);
		reputation = (TextView) findViewById(R.id.activity_user_info_rating);
		userLocation = (TextView) findViewById(R.id.activity_user_info_location);
		userPhone = (TextView) findViewById(R.id.activity_user_info_phone);
		userEmail = (TextView) findViewById(R.id.activity_user_info_email);
		userActivities = (TextView) findViewById(R.id.activity_user_info_recent_activities);
		layoutContact = (TableLayout) findViewById(R.id.TableLayout1);
		Intent intent = getIntent();
		int user_id = intent.getExtras().getInt("user_id");
		int current_user_id = intent.getExtras().getInt("current_user_id");

		// JSONObject params = new JSONObject();
		// try {
		// params.put("user_id", user_id);
		// params.put("current_user_id", current_user_id);
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// ServerRequest.newServerRequest(RequestType.FETCH_USER_INFO, params, new ServerRequestCallback() {
		//
		// @Override
		// public void onCompleted(Object... results) {
		// // respond friend request
		// ArrayList<Object> obj = (ArrayList<Object>) results[0];
		// User user = (User) obj.get(0);
		// userName.setText(user.getName());
		// numOfFriend.setText(String.valueOf(user.getNumOfFriend()));
		// numOfFollow.setText(String.valueOf(user.getNumOfFollow()));
		// numOfTrip.setText(String.valueOf(user.getNumOfTrip()));
		// reputation.setText(String.valueOf(user.getAverageVote()));
		// userLocation.setText(user.getLocation().getName());
		// userPhone.setText(user.getPhone());
		// userEmail.setText(user.getEmail());
		// if(user.getRecentActivities().size() > 0)userActivities.setText(user.getRecentActivities().get(0));
		// for(int i = 1; i < user.getRecentActivities().size(); i++)
		// {
		// userActivities.setText(userActivities.getText().toString() + "\n" + user.getRecentActivities().get(i));
		// }
		// if(Integer.parseInt(obj.get(1).toString()) == 0)layoutContact.setVisibility(View.GONE);
		// else layoutContact.setVisibility(View.VISIBLE);
		//
		// }
		// }).executeAsync();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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

		public UserInfoListener(Context context, int userId, int currentUserId) {
			this.context = context;
			this.userId = userId;
			this.currentUserId = currentUserId;
		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, UserInfoActivity.class);
			intent.putExtra(Constant.INTENT_USER_ID, userId);
			intent.putExtra("currentUser", currentUserId);

			context.startActivity(intent);
		}

	}
}
