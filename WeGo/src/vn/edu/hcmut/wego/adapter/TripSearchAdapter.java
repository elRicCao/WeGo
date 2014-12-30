package vn.edu.hcmut.wego.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Trip;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class TripSearchAdapter extends ArrayAdapter<Trip> {

	private Context context;
	private ArrayList<Trip> trips;

	// private CommentListener listener;

	// private DatabaseOpenHelper database;
	private Trip tripItem;

	public TripSearchAdapter(Context context, ArrayList<Trip> trips) {
		super(context, 0, trips);
		this.context = context;
		this.trips = trips;
		// database = new DatabaseOpenHelper(context);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_next_trip, parent, false);

			ViewHolder holder = new ViewHolder();

			holder.tripStart = (TextView) convertView.findViewById(R.id.item_next_trip_start);
			holder.tripEnd = (TextView) convertView.findViewById(R.id.item_next_trip_destination);
			holder.tripCost = (TextView) convertView.findViewById(R.id.item_next_trip_cost);
			holder.tripTime = (TextView) convertView.findViewById(R.id.item_next_trip_time);
			holder.tripLeader = (TextView) convertView.findViewById(R.id.item_next_trip_leader);
			holder.leaderRate = (RatingBar) convertView.findViewById(R.id.item_next_trip_leader_reputation);
			Utils.setUpRatingBar(context, holder.leaderRate);
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		tripItem = trips.get(position);

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.tripStart.setText(tripItem.getStartPlace().getName());
		holder.tripEnd.setText(tripItem.getEndPlace().getName());
		holder.tripLeader.setText(tripItem.getLeader().getName());
		holder.tripCost.setText(String.valueOf(tripItem.getCost()) + "d");
		DateFormat df = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
		holder.tripTime.setText(df.format(tripItem.getStartDate()) + " - " + df.format(tripItem.getEndDate()));

		holder.leaderRate.setRating((float) tripItem.getLeader().getAverageVote());
		return convertView;
	}

	/**
	 * Hold view of news item
	 */
	private static class ViewHolder {

		TextView tripStart;
		TextView tripEnd;
		TextView tripCost;
		TextView tripTime;
		TextView tripLeader;
		RatingBar leaderRate;
	}

}
