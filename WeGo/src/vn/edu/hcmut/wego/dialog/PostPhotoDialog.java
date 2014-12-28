package vn.edu.hcmut.wego.dialog;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.UploadImage;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

public class PostPhotoDialog extends DialogFragment {

	private Context context;
	private Bitmap bitmap;

	private ArrayList<Place> places;

	private AutoCompleteTextView locationView;
	private EditText descriptionView;
	private ImageView photoView;
	private CheckBox publicCheckBox;
	private Button postButton;
	private Bitmap result;

	public PostPhotoDialog(Context context, Bitmap bitmap) {
		this.context = context;
		this.bitmap = bitmap;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_photo, container, false);

		// Get controls of views
		locationView = (AutoCompleteTextView) rootView.findViewById(R.id.dialog_photo_location);
		descriptionView = (EditText) rootView.findViewById(R.id.dialog_photo_description);
		photoView = (ImageView) rootView.findViewById(R.id.dialog_photo_image);
		publicCheckBox = (CheckBox) rootView.findViewById(R.id.dialog_photo_public);
		postButton = (Button) rootView.findViewById(R.id.dialog_photo_post);

		// Show photo
		result = getResizedBitmap(bitmap, 320);
		photoView.setImageBitmap(result);

		// Set up listener
		locationView.addTextChangedListener(locationTextWatcher);
		publicCheckBox.setOnCheckedChangeListener(publicListener);
		postButton.setOnClickListener(PostListener);

		return rootView;
	}

	private OnClickListener PostListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			String imageName = getImageName();
			UploadImage.DoUploadImage(result, imageName);

			JSONObject params = new JSONObject();
			try {
				params.put("user", Utils.getUserId(context));
				params.put("place", places.get(0).getId());
				params.put("description", descriptionView.getText().toString().trim());
				params.put("name", imageName.trim());
				params.put("ispublic", (publicCheckBox.isChecked()) ? 1 : 0);
				Log.i("Debug link", params.toString());
				ServerRequest.newServerRequest(RequestType.ACTION_PHOTO, params, new ServerRequestCallback() {
					@Override
					public void onCompleted(Object... results) {
						PostPhotoDialog.this.dismiss();
					}
				}).executeAsync();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public static class PhotoDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private Bitmap bitmap;

		public PhotoDialogListener(Context context, FragmentManager fragmentManager, Bitmap bitmap) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.bitmap = bitmap;
		}

		@Override
		public void onClick(View view) {
			CreatePhotoDialog(context, fragmentManager, bitmap);
		}

	}

	public static void CreatePhotoDialog(Context context, FragmentManager fragmentManager, Bitmap bitmap) {
		PostPhotoDialog dialog = new PostPhotoDialog(context, bitmap);
		dialog.show(fragmentManager, "photo_dialog");
	}

	private TextWatcher locationTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			int index = 0;
			if (s.length() != 0) {
				JSONObject params = new JSONObject();
				try {
					params.put("index", index);
					params.put("input", s.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				ServerRequest.newServerRequest(RequestType.SEARCH_PLACE, params, new ServerRequestCallback() {

					@SuppressWarnings("unchecked")
					@Override
					public void onCompleted(Object... results) {
						places = (ArrayList<Place>) results[0];
						String[] placeName = new String[places.size()];

						for (int i = 0; i < places.size(); i++) {
							placeName[i] = places.get(i).getName();
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, placeName);

						locationView.setAdapter(adapter);
					}
				}).executeAsync();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	private OnCheckedChangeListener publicListener = new OnCheckedChangeListener() {

		private ColorStateList backup;

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				backup = publicCheckBox.getTextColors();
				publicCheckBox.setTextColor(context.getResources().getColor(R.color.green));
				publicCheckBox.setTypeface(Typeface.DEFAULT_BOLD);
			} else {
				publicCheckBox.setTextColor(backup);
				publicCheckBox.setTypeface(Typeface.DEFAULT);
			}
		}
	};

	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float ratio = (float) width / (float) height;
		if (ratio > 0) {
			width = maxSize;
			height = (int) (width / ratio);
		} else {
			height = maxSize;
			width = (int) (height * ratio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

	private String getImageName() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}
