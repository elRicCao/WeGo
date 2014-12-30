package vn.edu.hcmut.wego.activity;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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

public class PlaceInfoActivity extends ActionBarActivity {
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_info);
		actionBar = getSupportActionBar();
		Intent intent = getIntent();
		actionBar.setTitle(intent.getStringExtra("placeName"));

		JSONObject params = new JSONObject();

		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_place_info_progress_bar);
		final ScrollView contentView = (ScrollView) findViewById(R.id.activity_place_info_content);
		LinearLayout bottomBar = (LinearLayout) findViewById(R.id.activity_place_info_bottom_bar);

		contentView.setOnTouchListener(new MainActivity.BottomBarListener(this, bottomBar));

		progressBar.setVisibility(View.VISIBLE);
		contentView.setVisibility(View.GONE);

		try {
			params.put("placeId", intent.getExtras().getInt("placeId"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_PLACE_INFO, params, new ServerRequestCallback() {
			@Override
			public void onCompleted(Object... results) {
				Place place = (Place) results[0];
				actionBar.setTitle(place.getName());
				TextView placeName = (TextView) findViewById(R.id.activity_place_info_name);
				placeName.setText(place.getName());
				TextView placeWishList = (TextView) findViewById(R.id.activity_place_info_wishlist);
				placeWishList.setText(String.valueOf(place.getNumOfWishList()));
				TextView placeRate = (TextView) findViewById(R.id.activity_place_info_rating);
				placeRate.setText(String.valueOf(place.getAverageRate()));
				RatingBar ratingBar = (RatingBar) findViewById(R.id.activity_place_info_rating_bar);
				Utils.setUpRatingBar(PlaceInfoActivity.this, ratingBar);
				ratingBar.setRating((float) place.getAverageRate());
				
				TextView rateTime = (TextView) findViewById(R.id.activity_place_info_rating_times);
				rateTime.setText(String.valueOf(place.getNumOfReviews()));

				progressBar.setVisibility(View.GONE);
				contentView.setVisibility(View.VISIBLE);
			}
		}).executeAsync();

		LinearLayout reviewList = (LinearLayout) findViewById(R.id.activity_place_info_reputation_list);

		View review1 = LayoutInflater.from(this).inflate(R.layout.item_review, reviewList, false);
		RatingBar rateReview = (RatingBar) review1.findViewById(R.id.item_review_rating_bar);
		Utils.setUpRatingBar(this, rateReview);
		rateReview.setRating(4.5f);

		reviewList.addView(review1);

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

	public static class PlaceInfoListener implements OnClickListener {

		private Context context;
		private int placeId;
		private String placeName;

		public PlaceInfoListener(Context context, int placeId, String placeName) {
			this.context = context;
			this.placeId = placeId;
			this.placeName = placeName;
		}

		@Override
		public void onClick(View view) {
			Intent intent = new Intent(context, PlaceInfoActivity.class);
			intent.putExtra("placeId", placeId);
			intent.putExtra("placeName", placeName);
			context.startActivity(intent);
		}

	}
}
