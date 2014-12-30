package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.CommentAdapter;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.server.ServerRequest;
import vn.edu.hcmut.wego.server.ServerRequest.RequestType;
import vn.edu.hcmut.wego.server.ServerRequest.ServerRequestCallback;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class CommentDialog extends DialogFragment {

	private Context context;
	private CommentAdapter adapter;
	private News newsItem;
	private TextView numView;

	public CommentDialog(Context context, News newsItem) {
		this.context = context;
		this.newsItem = newsItem;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		adapter = new CommentAdapter(context, new ArrayList<Message>());
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("Debug", String.valueOf(newsItem.getId()));
		View rootView = inflater.inflate(R.layout.dialog_comment, container, false);
		
		numView = (TextView) rootView.findViewById(R.id.dialog_comment_num_comment);

		ListView commentList = (ListView) rootView.findViewById(R.id.dialog_comment_list);
		commentList.setAdapter(adapter);

		final EditText commentField = (EditText) rootView.findViewById(R.id.dialog_comment_input);

		ImageButton commentButton = (ImageButton) rootView.findViewById(R.id.dialog_comment_send);
		commentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String comment = commentField.getText().toString().trim();
				if (!comment.isEmpty()) {
					// TODO: create server request and push comment
//					User currentUser = new User();
//					currentUser.setId(Utils.getUserId(context));
//					currentUser.setName(Utils.getUserName(context));
//					currentUser.setImage("");
//					
//					Message pushComment = new Message();
//					pushComment.setContent(comment);
//					pushComment.setSender(currentUser);
//					pushComment.setTime(new Date());

					commentField.setText(null);
					
					JSONObject params = new JSONObject();
					try {
						params.put("user", Utils.getUserId(context));
						params.put("post", newsItem.getId());
						params.put("owner", newsItem.getOwner().getId());
						params.put("content", comment);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					ServerRequest.newServerRequest(RequestType.ACTION_COMMENT_POST, params, new ServerRequestCallback() {
						
						@Override
						public void onCompleted(Object... results) {
							loadData();
						}
					}).executeAsync();
				}
			}
		});

		loadData();

		return rootView;
	}

	private void loadData() {
		switch (newsItem.getType()) {
		case LIKE_POST:
		case COMMENT_POST:
		case POST:
			JSONObject params = new JSONObject();
			try {
				params.put("post", newsItem.getId());
				Log.i("Debug", params.toString());
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ServerRequest.newServerRequest(RequestType.FETCH_COMMENT_POST, params, new ServerRequestCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void onCompleted(Object... results) {
					ArrayList<Message> message = (ArrayList<Message>) results[0];
					adapter.clear();
					for (int i = 0; i < message.size(); i++) {
						adapter.add(message.get(i));
					}
					numView.setText(String.valueOf(message.size()) + " comments");
				}
			}).executeAsync();
			break;

		case COMMENT_PHOTO:
		case LIKE_PHOTO:
		case PHOTO:
			break;
		case REVIEW:
		case LIKE_REVIEW:
			break;
		default:
			break;
		}
	}
}
