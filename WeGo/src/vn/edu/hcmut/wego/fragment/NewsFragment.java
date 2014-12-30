package vn.edu.hcmut.wego.fragment;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.MainActivity;
//import vn.edu.hcmut.wego.activity.MainActivity.ShowHideButtonBarOnTouchListener;
import vn.edu.hcmut.wego.adapter.NewsAdapter;
import vn.edu.hcmut.wego.adapter.NewsAdapter.CommentListener;
import vn.edu.hcmut.wego.dialog.CommentDialog;
import vn.edu.hcmut.wego.dialog.PostPhotoDialog;
import vn.edu.hcmut.wego.dialog.PostStatusDialog;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewsFragment extends TabFragment {

	private static final int titleRes = R.string.title_fragment_news;
	private static final int iconRes = R.drawable.ic_tab_news;

	private int userId;
	private Context context;
	private NewsAdapter newsAdapter;
	private ProgressBar progressBar;
	private LinearLayout bottomBar;
	private LinearLayout statusButton;
	private LinearLayout photoButton;
	private ListView newsList;

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;

	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

	private Uri fileUri; // file url to store image/video

	public NewsFragment(Context context, int userId) {
		super(context.getResources().getString(titleRes), iconRes);
		this.userId = userId;
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		newsAdapter = new NewsAdapter(context, new ArrayList<News>());
		newsAdapter.setCommentListener(commentListener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate fragment layout
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);

		// Get control of all views
		progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_news_progress_bar);
		bottomBar = (LinearLayout) rootView.findViewById(R.id.fragment_news_bottom_bar);
		newsList = (ListView) rootView.findViewById(R.id.fragment_news_list);
		statusButton = (LinearLayout) rootView.findViewById(R.id.fragment_news_button_status);
		photoButton = (LinearLayout) rootView.findViewById(R.id.fragment_news_button_photo);

		// Set up list view, touch event for list view, and adapter
		newsList.setOnTouchListener(new MainActivity.BottomBarListener(context, bottomBar));
		newsList.setAdapter(newsAdapter);

		// Set up action for status button
		statusButton.setOnClickListener(new PostStatusDialog.CreateStatusDialogListener(context, getFragmentManager(), userId));

		// Set up action for photo button
		photoButton.setOnClickListener(photoButtonListener);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		JSONObject params = new JSONObject();
		try {
			params.put("user", userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_NEWS_FEED, params, new ServerRequestCallback() {

			@Override
			public void onCompleted(Object... results) {
				@SuppressWarnings("unchecked")
				ArrayList<News> news = (ArrayList<News>) results[0];
				Log.i("Debug", String.valueOf(news.size()));
				newsAdapter.clear();
				newsAdapter.notifyDataSetChanged();
				newsAdapter.addAll(news);
				
				addFakeData();
			}
		}).executeAsync();
	}

	private CommentListener commentListener = new CommentListener() {
		@Override
		public void onComment(News newsItem) {
			FragmentManager fragmentManager = NewsFragment.this.getFragmentManager();
			CommentDialog commentDialog = new CommentDialog(context, newsItem);
			commentDialog.show(fragmentManager, "comment_dialog");
		}
	};

	private OnClickListener photoButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!Utils.isDeviceSupportCamera(context)) {
				Toast.makeText(context, "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
			} else {
				onCaptureImage();
			}
		}
	};

	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void onCaptureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// Start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/*
	 * Here we store the file url as it will be null after returning from camera app
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save file url in bundle as it will be null on scren orientation changes
		outState.putParcelable("file_uri", fileUri);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		// Get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// If the result is capturing image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == -1) {
				// Successfully captured the image and show photo dialog
				previewCapturedImage();
			} else if (resultCode == 0) {
				// User cancelled Image capture
				Toast.makeText(context, "User cancelled image capture", Toast.LENGTH_SHORT).show();
			} else {
				// Failed to capture image
				Toast.makeText(context, "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/*
	 * Display image from a path in Photo Dialog
	 */
	private void previewCapturedImage() {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4 / 3;
			options.inJustDecodeBounds = false;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

			PostPhotoDialog.CreatePhotoDialog(context, getFragmentManager(), bitmap);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/*
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	private void addFakeData() {
		User user1 = new User();
		user1.setName("Như Ý");
		
		Place place1 = new Place();
		place1.setName("Ho Chi Minh City");
		
		News news1 = new News();
		news1.setType(NewsType.REVIEW);
		news1.setPlace(place1);
		news1.setRate(4);
		
		String date = "2014-12-30 8:17:00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		
		try {
			news1.setTime(dateFormat.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		news1.setContent("Amazing City!");
		news1.setNumOfLikes(10);
		
		news1.setOwner(user1);
		
		newsAdapter.add(news1);
	}
}
