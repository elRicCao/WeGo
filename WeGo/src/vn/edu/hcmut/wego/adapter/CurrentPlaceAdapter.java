package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.dialog.CurrentTripPlaceDialog.PlaceDialogCallback;
import vn.edu.hcmut.wego.entity.Place;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CurrentPlaceAdapter extends ArrayAdapter<Place> {

	private Context context;
	private ArrayList<Place> places;
	private PlaceDialogCallback callback;

	public CurrentPlaceAdapter(Context context, ArrayList<Place> places, PlaceDialogCallback callback) {
		super(context, 0, places);
		this.context = context;
		this.places = places;
		this.callback = callback;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_current_place, parent, false);

			ViewHolder holder = new ViewHolder();

			holder.nameView = (TextView) convertView.findViewById(R.id.item_current_place_name);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_current_place_image);
			holder.routeButton = (ImageButton) convertView.findViewById(R.id.item_current_place_route);
			
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		final Place place = places.get(position);

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Picasso.with(context).load(R.drawable.ho_chi_minh).into(holder.imageView);
		holder.nameView.setText(place.getName());
		
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
		ImageButton routeButton;
	}
	
	
}
