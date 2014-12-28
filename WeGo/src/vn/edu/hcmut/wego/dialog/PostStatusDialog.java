package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class PostStatusDialog extends DialogFragment {

	private Context context;
	private final int userId;
	private AutoCompleteTextView locationView;

	private ArrayList<Place> places;

	private CheckBox publicCheckBox;
	private TextView contentView;

	public PostStatusDialog(Context context, int userId) {
		this.context = context;
		this.userId = userId;
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
		View rootView = inflater.inflate(R.layout.dialog_status, container, false);

		locationView = (AutoCompleteTextView) rootView.findViewById(R.id.dialog_status_location);
		contentView = (TextView) rootView.findViewById(R.id.dialog_status_content);
		publicCheckBox = (CheckBox) rootView.findViewById(R.id.dialog_status_public);
		Button postButton = (Button) rootView.findViewById(R.id.dialog_status_post);

		publicCheckBox.setOnCheckedChangeListener(publicListener);
		locationView.addTextChangedListener(locationTextWatcher);
		postButton.setOnClickListener(postListener);

		return rootView;
	}

	public static class CreateStatusDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int userId;

		public CreateStatusDialogListener(Context context, FragmentManager fragmentManager, int userId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.userId = userId;
		}

		@Override
		public void onClick(View view) {
			PostStatusDialog dialog = new PostStatusDialog(context, userId);
			dialog.show(fragmentManager, "status_dialog");
		}

	}

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
						ArrayList<Place> places = (ArrayList<Place>) results[0];
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

	private OnClickListener postListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			view.setEnabled(false);
			JSONObject params = new JSONObject();
			try {
				params.put("content", contentView.getText().toString());
				params.put("placeId", places.get(0).getId());
				params.put("user", userId);
				if (publicCheckBox.isChecked())
					params.put("ispublic", 1);
				else
					params.put("ispublic", 0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ServerRequest.newServerRequest(RequestType.ACTION_POST, params, new ServerRequestCallback() {

				@Override
				public void onCompleted(Object... results) {
					PostStatusDialog.this.dismiss();
				}
			}).executeAsync();
		}
	};
}
