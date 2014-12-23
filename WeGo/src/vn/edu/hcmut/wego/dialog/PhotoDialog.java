package vn.edu.hcmut.wego.dialog;

import vn.edu.hcmut.wego.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

public class PhotoDialog extends DialogFragment {

	private Context context;
	private Bitmap bitmap;
	
	private EditText locationField;
	private EditText descField;
	private ImageView photoView;
	private CheckBox publicCheck;
	private Button postButton;

	public PhotoDialog(Context context, Bitmap bitmap) {
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
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_photo, container, false);

		// Get controls of views
		locationField = (EditText) rootView.findViewById(R.id.dialog_photo_location);
		descField = (EditText) rootView.findViewById(R.id.dialog_photo_description);
		photoView = (ImageView) rootView.findViewById(R.id.dialog_photo_image);
		publicCheck = (CheckBox) rootView.findViewById(R.id.dialog_photo_public);
		postButton = (Button) rootView.findViewById(R.id.dialog_photo_post);
		
		// Show photo
		Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);
		photoView.setImageBitmap(result);
		
		// Set up location search
		
		// Set up post button
		postButton.setOnClickListener(PostListener);
		
		return rootView;
	}
	
	private OnClickListener PostListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			//TODO: Create server request to post image
			String desc = descField.getText().toString().trim();
			boolean isPublic = publicCheck.isChecked();
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
		PhotoDialog dialog = new PhotoDialog(context, bitmap);
		dialog.show(fragmentManager, "photo_dialog");
	}
}
