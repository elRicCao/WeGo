package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Date;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.ChatAdapter;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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

	public enum ChatDialogType {
		FRIEND_MESSAGE, GROUP_MESSAGE, TRIP_MESSAGE
	}

	private Context context;
	private ChatAdapter adapter;

	private ListView chatList;
	private EditText inputView;

	private User contact;
	private User currentUser;

	private Handler handler;

	private Runnable update = new Runnable() {
		@Override
		public void run() {
			// TODO: Create Server Request to pull message from server
		}
	};

	public ChatDialog(Context context, ChatDialogType type, int userId, int generalContactId) {
		this.context = context;
		
		// TODO: Debug
		currentUser = new User();
		currentUser.setId(1);

		contact = new User();
		contact.setId(2);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);

		adapter = new ChatAdapter(context, new ArrayList<Message>(), currentUser.getId());
		handler = new Handler();
		addFakeData();
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		update.run();
	}

	@Override
	public void onStop() {
		super.onStop();
		handler.removeCallbacks(update);
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
			// TODO: Create server request to push message to server

		}
	}
	
	public static class ChatDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private ChatDialogType type;
		private int userId;
		private int generalContactId;
		
		public ChatDialogListener(Context context, FragmentManager fragmentManager, ChatDialogType type, int userId, int generalContactId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.type = type;
			this.userId = userId;
			this.generalContactId = generalContactId;
		}
		
		@Override
		public void onClick(View view) {
			ChatDialog chatDialog = new ChatDialog(context, type, userId, generalContactId);
			chatDialog.show(fragmentManager, "chat_dialog");
		}
		
	}

	private void addFakeData() {
		Message message = new Message();

		message.setSender(currentUser);
		message.setContent("Hey, do you want to join my trip to Vinh Long? It's from 27/11 to 30/11 and we will stay at my friend's house");
		message.setTime(new Date());

		Message message2 = new Message();

		message2.setSender(contact);
		message2.setContent("Nice. How about cost and vehicle? I think we should ride our motorbike because it will be cheaper and more convenient");
		message2.setTime(new Date());

		adapter.add(message);
		adapter.add(message2);
		
	}
}
