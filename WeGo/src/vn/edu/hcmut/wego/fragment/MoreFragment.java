package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity;
import vn.edu.hcmut.wego.activity.PlaceInfoActivity;
import vn.edu.hcmut.wego.dialog.WishlistDialog;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MoreFragment extends TabFragment {

	private static final int titleRes = R.string.title_fragment_more;
	private static final int iconRes = R.drawable.ic_tab_more;
	private Context context;
	private int userId;

	private LinearLayout topPhotoGroup;
	private LinearLayout topPlaceGroup;

	public MoreFragment(Context context, int userId) {
		super(context.getResources().getString(titleRes), iconRes);
		this.context = context;
		this.userId = userId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_more, container, false);

		LinearLayout topPlaceList = (LinearLayout) rootView.findViewById(R.id.fragment_more_top_place_list);
		LinearLayout topPhotoList = (LinearLayout) rootView.findViewById(R.id.fragment_more_top_photo_list);
		LinearLayout buttonBar = (LinearLayout) rootView.findViewById(R.id.fragment_more_button_bar);
		ScrollView content = (ScrollView) rootView.findViewById(R.id.fragment_more_content);
		LinearLayout wishlistButton = (LinearLayout) rootView.findViewById(R.id.fragment_more_button_wishlist);
		topPlaceGroup = (LinearLayout) rootView.findViewById(R.id.fragment_more_section_top_place);
		topPhotoGroup = (LinearLayout) rootView.findViewById(R.id.fragment_more_section_top_photo);

		topPhotoGroup.setVisibility(View.GONE);
		topPlaceGroup.setVisibility(View.GONE);

		content.setOnTouchListener(new MainActivity.BottomBarListener(context, buttonBar));

		populateTopPlaces(inflater, topPlaceList);
		populateTopPhotos(inflater, topPhotoList);

		wishlistButton.setOnClickListener(new WishlistDialog.WishlistDialogListener(context, getFragmentManager(), userId));

		return rootView;
	}

	private void populateTopPlaces(final LayoutInflater inflater, final ViewGroup container) {
		JSONObject params = new JSONObject();
		ServerRequest.newServerRequest(RequestType.FETCH_TOP_PLACE, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				@SuppressWarnings("unchecked")
				ArrayList<Place> topPlaces = (ArrayList<Place>) results[0];

				if (topPlaces.size() > 0) {
					topPlaceGroup.setVisibility(View.VISIBLE);
				}

				for (int i = 0; i < topPlaces.size(); i++) {
					View placeView = inflater.inflate(R.layout.item_place, container, false);

					TextView placeName = (TextView) placeView.findViewById(R.id.item_place_info_name);
					TextView placeRate = (TextView) placeView.findViewById(R.id.item_place_rate);
					TextView placeWishList = (TextView) placeView.findViewById(R.id.item_place_wishlist);
					ImageView placeImage = (ImageView) placeView.findViewById(R.id.item_place_image);

					placeName.setText(topPlaces.get(i).getName());
					placeRate.setText(String.valueOf(topPlaces.get(i).getAverageRate()));
					placeWishList.setText(String.valueOf(topPlaces.get(i).getNumOfWishList()));

					Picasso.with(context).load(topPlaces.get(i).getAvatar()).into(placeImage);

					container.addView(placeView);

					placeView.setOnClickListener(new PlaceInfoActivity.PlaceInfoListener(context, topPlaces.get(i).getId(), topPlaces.get(i).getName()));

					if (i < topPlaces.size() - 1) {
						View dividerView = inflater.inflate(R.layout.item_divider, container, false);
						container.addView(dividerView);
					}
				}
			}
		}).executeAsync();
	}

	private void populateTopPhotos(final LayoutInflater inflater, final ViewGroup container) {
		JSONObject params = new JSONObject();
		ServerRequest.newServerRequest(RequestType.FETCH_TOP_PHOTO, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				@SuppressWarnings("unchecked")
				ArrayList<News> topPhotos = (ArrayList<News>) results[0];

				if (topPhotos.size() > 0) {
					topPhotoGroup.setVisibility(View.VISIBLE);
				}

				for (int i = 0; i < topPhotos.size(); i++) {
					View placeView = inflater.inflate(R.layout.item_photo, container, false);

					TextView userView = (TextView) placeView.findViewById(R.id.item_photo_user_name);
					TextView descriptionView = (TextView) placeView.findViewById(R.id.item_photo_description);
					TextView likeView = (TextView) placeView.findViewById(R.id.item_photo_like);
					TextView commentView = (TextView) placeView.findViewById(R.id.item_photo_comment);

					ImageView userImage = (ImageView) placeView.findViewById(R.id.item_photo_user_image);
					ImageView placeImage = (ImageView) placeView.findViewById(R.id.item_photo_image);

					userView.setText(topPhotos.get(i).getOwner().getName());
					descriptionView.setText(topPhotos.get(i).getContent());
					likeView.setText(String.valueOf(topPhotos.get(i).getNumOfLikes()));
					commentView.setText(String.valueOf(topPhotos.get(i).getNumOfComments()));

					final int width = (int) (getResources().getDisplayMetrics().widthPixels - 48 * getResources().getDisplayMetrics().density);

					Transformation transformation = new Transformation() {

						@Override
						public Bitmap transform(Bitmap source) {
							double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
							int targetHeight = (int) (width * aspectRatio);

							Bitmap result = Bitmap.createScaledBitmap(source, width, targetHeight, false);
							if (result != source) {
								source.recycle();
							}
							return result;
						}

						@Override
						public String key() {
							return "transformation" + " desiredWidth";
						}
					};

					User owner = topPhotos.get(i).getOwner();

					if (owner.getImage() == null || owner.getImage().isEmpty()) {
						Picasso.with(context).load(R.drawable.default_user).into(userImage);
					} else {
						Picasso.with(context).load(topPhotos.get(i).getOwner().getImage()).into(userImage);
					}
					
					Picasso.with(context).load(topPhotos.get(i).getPhoto()).transform(transformation).into(placeImage);

					container.addView(placeView);

					if (i < topPhotos.size() - 1) {
						View dividerView = inflater.inflate(R.layout.item_divider, container, false);
						container.addView(dividerView);
					}
				}
			}
		}).executeAsync();
	}
}
