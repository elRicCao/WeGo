package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserMessageActivity;
import vn.edu.hcmut.wego.activity.UserPageActivity;
import vn.edu.hcmut.wego.adapter.FriendListAdapter;
import vn.edu.hcmut.wego.adapter.FriendRequestAdapter;
import vn.edu.hcmut.wego.adapter.FriendRequestAdapter.FriendRequestAdapterEventListener;
import vn.edu.hcmut.wego.entity.Offer;
import vn.edu.hcmut.wego.entity.Offer.Type;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper;
import vn.edu.hcmut.wego.utility.Utility;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsFragment extends BaseFragment {

	private ArrayList<User> friends;
	private ArrayList<Item> items;
	private ArrayList<Offer> sent;
	private ArrayList<Offer> received;
	private FriendListAdapter friendAdapter;
	private FriendRequestAdapter requestAdapter;
	private Context context;
	private DatabaseOpenHelper database;
	private boolean isViewingRequest = false;
	private ListView listView;
	private int userId;

	public FriendsFragment(Context context) {
		// Get context
		this.context = context;

		// Set title
		setTitle(this.context.getString(R.string.title_fragment_friends));

		// Get database
		database = new DatabaseOpenHelper(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get user id saved in shared preferences
		userId = Utility.getUserId(context);

		// Open database to load friends info from database and create friend adapter
		friends = database.selectAllFriends();
		friendAdapter = new FriendListAdapter(context, friends);

		// Open database to load friends info from database and create request adapter
//		sent = database.selectAllOffers(userId, Type.FRIEND_REQUEST, true);
//		received = database.selectAllOffers(userId, Type.FRIEND_REQUEST, false);
		//TODO: Working
		sent = new ArrayList<Offer>();
		received = new ArrayList<Offer>();

		// Set up requests array comprising of 2 sections: Friend Request Sent and Friend Request Received
		items = new ArrayList<Item>();

		// Add sent requests to requests array
		if (!sent.isEmpty()) {
			SectionItem sectionSentRequest = new SectionItem("Friend Request Sent");
			items.add(sectionSentRequest);
			for (Offer offer : sent) {
				EntryItem item = new EntryItem(offer, sectionSentRequest);
				items.add(item);
				sectionSentRequest.getItems().add(item);
			}
		}

		// Add received requests to requests array
		if (!received.isEmpty()) {
			SectionItem sectionReceivedRequest = new SectionItem("Friend Request Received");
			items.add(sectionReceivedRequest);
			for (Offer offer : received) {
				EntryItem item = new EntryItem(offer, sectionReceivedRequest);
				items.add(item);
				sectionReceivedRequest.getItems().add(item);
			}
		}

		// Create request adapter
		requestAdapter = new FriendRequestAdapter(context, items);
		requestAdapter.setEventListener(new FriendRequestAdapterEventListener() {
			
			@Override
			public void onAddFriend() {
				friends = database.selectAllFriends();
				friendAdapter = new FriendListAdapter(context, friends);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

		// Set up list view
		listView = (ListView) rootView.findViewById(R.id.fragment_friend_listview);
		listView.setAdapter(friendAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (isViewingRequest) {
					if (!items.get(position).isSection()) {
						Intent intent = new Intent(context, UserPageActivity.class);
						context.startActivity(intent);
					}
				}
				else {
					Intent intent = new Intent(context, UserMessageActivity.class);
					context.startActivity(intent);
				}
			}
		});

		// Set up message button
		Button messageButton = (Button) rootView.findViewById(R.id.fragment_friends_button_message);
		messageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Set up request indicator
		final TextView requestIndicator = (TextView) rootView.findViewById(R.id.fragment_friends_request_indicator);
		if (!received.isEmpty()) {
			requestIndicator.setVisibility(View.VISIBLE);
			requestIndicator.setText(Integer.toString(received.size()));
		}

		//
		final TextView switchButtonText = (TextView) rootView.findViewById(R.id.fragment_friends_button_switch_text);
		// Set up friend request/ friend list button button
		LinearLayout switchButton = (LinearLayout) rootView.findViewById(R.id.fragment_friends_button_swtich);
		switchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isViewingRequest) {
					// Update control variable
					isViewingRequest = false;

					// Set adapter for list view
					listView.setAdapter(friendAdapter);

					// Update request indicator
					received = database.selectAllOffers(userId, Type.FRIEND_REQUEST, false);

					if (received.isEmpty()) {
						requestIndicator.setVisibility(View.GONE);
					} else {
						requestIndicator.setVisibility(View.VISIBLE);
						requestIndicator.setText(String.valueOf(received.size()));
					}

					// Update text of switch button
					switchButtonText.setText("Friend Request");
				} else {
					// Update control variable
					isViewingRequest = true;

					// Set adapter for list view
					listView.setAdapter(requestAdapter);

					// Hide request indicator
					requestIndicator.setVisibility(View.GONE);

					// Update text of switch button
					switchButtonText.setText("Friend List");
				}
			}
		});

		return rootView;
	}

	public interface Item {
		public boolean isSection();
	}

	public class SectionItem implements Item {

		private String title;
		private ArrayList<EntryItem> items;

		public SectionItem(String title) {
			this.setTitle(title);
			items = new ArrayList<EntryItem>();
		}

		@Override
		public boolean isSection() {
			return true;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public ArrayList<EntryItem> getItems() {
			return items;
		}

		public void setItems(ArrayList<EntryItem> items) {
			this.items = items;
		}
	}

	public class EntryItem implements Item {

		private Offer offer;
		private SectionItem sectionItem;

		public EntryItem(Offer offer, SectionItem sectionItem) {
			this.offer = offer;
			this.sectionItem = sectionItem;
		}

		@Override
		public boolean isSection() {
			return false;
		}

		public Offer getOffer() {
			return offer;
		}

		public void setOffer(Offer offer) {
			this.offer = offer;
		}

		public SectionItem getSectionItem() {
			return sectionItem;
		}

		public void setSectionItem(SectionItem sectionItem) {
			this.sectionItem = sectionItem;
		}
	}

}
