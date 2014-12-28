package vn.edu.hcmut.wego.dialog;

import java.util.Date;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Group;
import vn.edu.hcmut.wego.entity.Message;
import vn.edu.hcmut.wego.entity.User;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WishlistDialog extends DialogFragment {

	private Context context;
	private final int userId;

	public WishlistDialog(Context context, int userId) {
		this.context = context;
		this.userId = userId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		addFakeData();
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_wishlist, container, false);

		LinearLayout list = (LinearLayout) rootView.findViewById(R.id.dialog_wishlist_list);

		populateWishlist(inflater, list);

		return rootView;
	}

	public static class WishlistDialogListener implements OnClickListener {

		private Context context;
		private FragmentManager fragmentManager;
		private int userId;

		public WishlistDialogListener(Context context, FragmentManager fragmentManager, int userId) {
			this.context = context;
			this.fragmentManager = fragmentManager;
			this.userId = userId;
		}

		@Override
		public void onClick(View view) {
			WishlistDialog dialog = new WishlistDialog(context, userId);
			dialog.show(fragmentManager, "wishlist_dialog");
		}

	}

	private void populateWishlist(LayoutInflater inflater, ViewGroup container) {
		View placeView1 = inflater.inflate(R.layout.item_place, container, false);
		container.addView(placeView1);

		View dividerView1 = inflater.inflate(R.layout.item_divider, container, false);
		container.addView(dividerView1);

//		View placeView2 = inflater.inflate(R.layout.item_place_fake_1, container, false);
//		container.addView(placeView2);

		View dividerView2 = inflater.inflate(R.layout.item_divider, container, false);
		container.addView(dividerView2);

//		View placeView3 = inflater.inflate(R.layout.item_place_fake_2, container, false);
//		container.addView(placeView3);
		
		Log.i("Debug wishlist", String.valueOf(container.getChildCount()));
	}

	private void addFakeData() {
		Group groupA = new Group();
		groupA.setName("Saigon Traveler");

		User userA = new User();
		userA.setName("elRic");

		Message messageA = new Message();
		messageA.setSender(userA);
		messageA.setContent("Hey, anyone want to have a trip to Da Lat?");
		messageA.setTime(new Date());

		groupA.setAnnouncement(messageA);

		Group groupB = new Group();
		groupB.setName("iTravel");

	}
}
