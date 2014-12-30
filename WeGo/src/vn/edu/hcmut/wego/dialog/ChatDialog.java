package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.ChatAdapter;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ChatDialog extends DialogFragment {

	private Context context;
	private ChatAdapter adapter;

	private ListView chatList;
	private EditText inputView;

	private User contact;
	private User currentUser;

	public ChatDialog(Context context, int userId, int generalContactId) {
		this.context = context;

		currentUser = new User();
		currentUser.setId(userId);

		contact = new User();
		contact.setId(generalContactId);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);

		adapter = new ChatAdapter(context, new ArrayList<Message>(), currentUser.getId());
		// addFakeData();
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_chat, container, false);

		chatList = (ListView) rootView.findViewById(R.id.dialog_chat_list);
		inputView = (EditText) rootView.findViewById(R.id.dialog_chat_input);
		ImageButton sendButton = (ImageButton) rootView.findViewById(R.id.dialog_chat_send);

		// Set up chat list
		chatList.setAdapter(adapter);

		// Set up action send in editor
		inputView.setOnEditorActionListener(sendActionEditorListener);

		// Set up click event of send button
		sendButton.setOnClickListener(sendButtonClickListener);

		loadData();

		return rootView;
	}

	private OnEditorActionListener sendActionEditorListener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEND) {
				onSend();
				return true;
			}
			return false;
		}
	};

	private OnClickListener sendButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			onSend();
		}
	};

	private void onSend() {
		String message = inputView.getText().toString().trim();
		if (!message.isEmpty()) {
			JSONObject params = new JSONObject();
			try {
				params.put("sender", currentUser.getId());
				params.put("receiver", contact.getId());
				params.put("content", inputView.getText().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ServerRequest.newServerRequest(RequestType.ACTION_SEND_MESSAGE, params, new ServerRequestCallback() {

				@Override
				public void onCompleted(Object... results) {
					if (Integer.parseInt(results[0].toString()) == 1) {
						loadData();
					}

				}
			}).executeAsync();

		}
	}
	
	private void loadData() {
		JSONObject params = new JSONObject();
		try {
			params.put("user_id", currentUser.getId());
			params.put("friend_id", contact.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ServerRequest.newServerRequest(RequestType.FETCH_FRIEND_MESSAGE, params, new ServerRequestCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onCompleted(Object... results) {
				ArrayList<Message> messages = (ArrayList<Message>) results[0];
				adapter.clear();
				for (Message message : messages) {
					adapter.add(message);
				}
			}
		}).executeAsync();
	}

	public static class ChatDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int userId;
		private int generalContactId;

		public ChatDialogListener(Context context, FragmentManager fragmentManager, int userId, int generalContactId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.userId = userId;
			this.generalContactId = generalContactId;
		}

		@Override
		public void onClick(View view) {
			ChatDialog chatDialog = new ChatDialog(context, userId, generalContactId);
			chatDialog.show(fragmentManager, "chat_dialog");
		}

	}
}
