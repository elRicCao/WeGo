package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CurrentSearchAdapter extends ArrayAdapter<Place> {

	private Context context;
	private ArrayList<Place> places;
	private SearchDialogCallback callback;

	public CurrentSearchAdapter(Context context, ArrayList<Place> places, SearchDialogCallback callback) {
		super(context, 0, places);
		this.context = context;
		this.places = places;
		this.callback = callback;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_quick_search_place, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.item_search_place_name);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_search_place_image);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.item_search_place_rate);
			holder.routeButton = (ImageButton) convertView.findViewById(R.id.item_search_place_route);
			
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		final Place place = places.get(position);

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		// Set up name
		holder.nameView.setText(place.getName());
		
		// Set up image
		Picasso.with(context).load(R.drawable.ho_chi_minh).into(holder.imageView);
		
		// Set up rating
		Utils.setUpRatingBar(context, holder.ratingBar);
		holder.ratingBar.setRating(4.5f);
		
		// Set up find route button
		holder.routeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (callback != null) {
					callback.onCallback(place);
				}
			}
		});
		
		return convertView;
	}

	/**
	 * Hold view of news item
	 */
	private static class ViewHolder {
		ImageView imageView;
		TextView nameView;
		RatingBar ratingBar;
		ImageButton routeButton;
	}

	public interface SearchDialogCallback {
		public void onCallback(Place place);
	}
}
