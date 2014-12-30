package vn.edu.hcmut.wego.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupInfoActivity extends ActionBarActivity {

	private ProgressBar loadingView;
	private RelativeLayout contentView;
	private ImageView imageView;
	private TextView nameView;
	private LinearLayout memberGroup;
	private TextView memberNumView;
	private LinearLayout adminGroup;
	private TextView adminNameView;
	private TextView annoucementView;
	private LinearLayout requestList;
	private LinearLayout messageList;
	private ActionBar actionBar;

	private Group group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
		actionBar = getSupportActionBar();
		actionBar.setTitle("WeGo");

		getViews();

		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.group_info, menu);
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

	private void getViews() {

		loadingView = (ProgressBar) findViewById(R.id.activity_group_info_loading);
		contentView = (RelativeLayout) findViewById(R.id.activity_group_info_content);
		imageView = (ImageView) findViewById(R.id.activity_group_info_image);
		nameView = (TextView) findViewById(R.id.activity_group_info_name);
		memberGroup = (LinearLayout) findViewById(R.id.activity_group_info_member);
		memberNumView = (TextView) findViewById(R.id.activity_group_info_member_num);
		adminGroup = (LinearLayout) findViewById(R.id.activity_group_info_admin);
		adminNameView = (TextView) findViewById(R.id.activity_group_info_admin_name);
		annoucementView = (TextView) findViewById(R.id.activity_group_info_announcement);
		requestList = (LinearLayout) findViewById(R.id.activity_group_info_request);
		messageList = (LinearLayout) findViewById(R.id.activity_group_info_message);
	}

	private void loadData() {
		Intent intent = getIntent();
		int group_id = intent.getExtras().getInt("group_id");
		JSONObject params = new JSONObject();
		try {
			params.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_GROUP_INFO, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				// respond friend request
				Group group = (Group) results[0];
				nameView.setText(group.getName());
				memberNumView.setText(String.valueOf(group.getCount()));
				adminNameView.setText(group.getAdmin().getName());
				annoucementView.setText(group.getAnnouncement().getContent());
			}

		}).executeAsync();
	}

	private void setLoadingStatus(boolean status) {
		if (status) {
			loadingView.setVisibility(View.VISIBLE);
			contentView.setVisibility(View.GONE);
		} else {
			loadingView.setVisibility(View.GONE);
			contentView.setVisibility(View.VISIBLE);
		}
	}

	private void populateViews() {

	}

	public static class GroupInfoListener implements OnClickListener {

		private Context context;
		private int groupId;

		public GroupInfoListener(Context context, int groupId) {
			this.context = context;
			this.groupId = groupId;
		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, GroupInfoActivity.class);
			intent.putExtra(Constant.INTENT_GROUP_ID, groupId);
			context.startActivity(intent);
		}

	}
}
