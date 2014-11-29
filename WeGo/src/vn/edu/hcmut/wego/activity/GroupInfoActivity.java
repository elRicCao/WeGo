package vn.edu.hcmut.wego.activity;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.constant.Constant;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class GroupInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_info, menu);
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
