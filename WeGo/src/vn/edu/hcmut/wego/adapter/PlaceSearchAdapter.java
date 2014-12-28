package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.UserInfoActivity;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.News.NewsType;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.entity.User;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceSearchAdapter extends ArrayAdapter<Place> {

	private Context context;
	private ArrayList<Place> places;
	private Place place;

	public PlaceSearchAdapter(Context context, ArrayList<Place> places) {
		super(context, 0, places);
		this.context = context;
		this.places = places;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_search_place, parent, false);

			ViewHolder holder = new ViewHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.item_search_place_name);
			
			convertView.setTag(holder);
		}

		// Get news item corresponding by postion
		place = places.get(position);

		// Get view holder from view tag
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.nameView.setText(place.getName());
		return convertView;
	}

	/**
	 * Hold view of news item
	 */
	private static class ViewHolder {
		ImageView imageView;
		TextView nameView;
		
	}

}
