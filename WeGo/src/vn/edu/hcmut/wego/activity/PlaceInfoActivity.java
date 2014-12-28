package vn.edu.hcmut.wego.activity;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PlaceInfoActivity extends ActionBarActivity {
	private ActionBar actionBar;
	private TextView placeName;
	private TextView placeWishList;
	private TextView placeRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_info);
		actionBar = getSupportActionBar();

		placeName = (TextView) findViewById(R.id.activity_place_info_name);
		placeWishList = (TextView) findViewById(R.id.activity_place_info_wishlist);
		placeRate = (TextView) findViewById(R.id.activity_place_info_rating);

		populateInfo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.place_info, menu);
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

	private void populateInfo() {
		try {
			int placeId = getIntent().getExtras().getInt("placeId");
			JSONObject params = new JSONObject().put("placeId", placeId);
			ServerRequest.newServerRequest(RequestType.FETCH_PLACE_INFO, params, new ServerRequestCallback() {

				@Override
				public void onCompleted(Object... results) {
					Place place = (Place) results[0];

					actionBar.setTitle(place.getName());
					placeName.setText(place.getName());
					placeWishList.setText(String.valueOf(place.getNumOfWishList()));
					placeRate.setText(String.valueOf(place.getAverageRate()));
				}
			}).executeAsync();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static class PlaceInfoListener implements OnClickListener {

		private Context context;
		private int placeId;

		public PlaceInfoListener(Context context, int placeId) {
			this.context = context;
			this.placeId = placeId;
		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, PlaceInfoActivity.class);
			intent.putExtra("placeId", placeId);
			context.startActivity(intent);
		}

	}
}
