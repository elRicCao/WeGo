package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Offer;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.fragment.FriendsFragment.EntryItem;
import vn.edu.hcmut.wego.fragment.FriendsFragment.Item;
import vn.edu.hcmut.wego.fragment.FriendsFragment.SectionItem;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper.SelectType;
import vn.edu.hcmut.wego.utility.Commons;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FriendRequestAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private DatabaseOpenHelper database;
	private FriendRequestAdapterEventListener eventListener;

	public FriendRequestAdapter(Context context, ArrayList<Item> requests) {
		super(context, 0, requests);
		this.context = context;
		this.items = requests;

		database = new DatabaseOpenHelper(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Item item = items.get(position);
		int userId = Commons.getUserId(context);

		if (item != null) {

			// Get inflater
			LayoutInflater inflater = LayoutInflater.from(context);

			// Section Item
			if (item.isSection()) {

				final SectionItem sectionItem = (SectionItem) item;
				
				// Inflate layout for section
				convertView = inflater.inflate(R.layout.item_friend_request_section, parent, false);

				// Set values for layout components
				TextView titleView = (TextView) convertView.findViewById(R.id.item_friend_request_section_title);
				TextView indicatorView = (TextView) convertView.findViewById(R.id.item_friend_request_section_indicator);
				titleView.setText(sectionItem.getTitle());
				indicatorView.setText(String.valueOf(sectionItem.getItems().size()));
			}

			// Entry Item
			else {

				// Set up remove animation
				final Animation removeAnimation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
				removeAnimation.setDuration(750);
				removeAnimation.setAnimationListener(new RemoveAnimationListener(position));

				// Get offer object from entry item
				final EntryItem entryItem = (EntryItem) item;
				final Offer offer = entryItem.getOffer();

				// Sent entries
				if (offer.getSenderId() == userId) {

					// Inflate layout for sent entry
					final View sentEntryView = inflater.inflate(R.layout.item_friend_request_entry_sent, parent, false);

					// Find receiver info from database
					User receiver = database.selectUser(offer.getReceiverId(), SelectType.BRIEF);

					// Set up components
					TextView nameView = (TextView) sentEntryView.findViewById(R.id.item_friend_request_entry_sent_name);
					TextView locationView = (TextView) sentEntryView.findViewById(R.id.item_friend_request_entry_sent_location);
					TextView deleteButton = (TextView) sentEntryView.findViewById(R.id.item_friend_request_entry_sent_button_delete);

					nameView.setText(receiver.getName());
					locationView.setText("Ho Chi Minh City");
					deleteButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							sentEntryView.startAnimation(removeAnimation);
							database.deleteOffer(offer);
							entryItem.getSectionItem().getItems().remove(entryItem);
						}
					});
					return sentEntryView;
				}
				// Received entries
				else {

					// Inflate layout for received entry
					final View receivedEntryView = inflater.inflate(R.layout.item_friend_request_entry_received, parent, false);

					// Find sender info from database
					final User sender = database.selectUser(offer.getSenderId(), SelectType.BRIEF);

					// Set up components
					TextView nameView = (TextView) receivedEntryView.findViewById(R.id.item_friend_request_entry_received_name);
					TextView locationView = (TextView) receivedEntryView.findViewById(R.id.item_friend_request_entry_received_location);
					TextView acceptButton = (TextView) receivedEntryView.findViewById(R.id.item_friend_request_entry_received_button_accept);
					TextView declineButton = (TextView) receivedEntryView.findViewById(R.id.item_friend_request_entry_received_button_decline);

					locationView.setText("Ho Chi Minh City");
					nameView.setText(sender.getName());
					acceptButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							receivedEntryView.startAnimation(removeAnimation);
							database.deleteOffer(offer);
							database.makeFriend(sender);
							if (eventListener != null) {
								eventListener.onAddFriend();
							}
							entryItem.getSectionItem().getItems().remove(entryItem);
						}
					});

					declineButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							receivedEntryView.startAnimation(removeAnimation);
							database.deleteOffer(offer);
							entryItem.getSectionItem().getItems().remove(entryItem);
						}
					});
					return receivedEntryView;
				}
			}
		}
		return convertView;
	}

	public FriendRequestAdapterEventListener getEventListener() {
		return eventListener;
	}

	public void setEventListener(FriendRequestAdapterEventListener eventListener) {
		this.eventListener = eventListener;
	}

	/**
	 * Remove animation listener for handle remove item in data
	 * 
	 * @author elRic
	 *
	 */
	private class RemoveAnimationListener implements AnimationListener {

		private int position;

		public RemoveAnimationListener(int position) {
			this.position = position;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			FriendRequestAdapter.this.remove(items.get(position));
			FriendRequestAdapter.this.notifyDataSetChanged();
		}
	}
	
	public interface FriendRequestAdapterEventListener {
		public void onAddFriend();
	}
}
