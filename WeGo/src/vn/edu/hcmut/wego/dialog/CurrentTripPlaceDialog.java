package vn.edu.hcmut.wego.dialog;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.activity.CurrentTripActivity.FindRouteCallback;
import vn.edu.hcmut.wego.adapter.CurrentPlaceAdapter;
import vn.edu.hcmut.wego.entity.Place;
import vn.edu.hcmut.wego.utility.Utils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class CurrentTripPlaceDialog extends DialogFragment {

	private Context context;
	private CurrentPlaceAdapter adapter;
	private int userId;
	private FindRouteCallback callback;

	public CurrentTripPlaceDialog(Context context, FindRouteCallback _callback) {
		this.context = context;
		this.userId = Utils.getUserId(context);
		this.callback = _callback;
		this.adapter = new CurrentPlaceAdapter(context, new ArrayList<Place>(), new PlaceDialogCallback() {
			@Override
			public void onCallback(Place place) {
				if (callback != null) {
					callback.onFindRoute(place);
				}
				dismiss();
			}
		});
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
		getDialog().getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE | LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_places, container, false);

		TextView memberView = (TextView) rootView.findViewById(R.id.dialog_places_num);
		ListView listView = (ListView) rootView.findViewById(R.id.dialog_places_list);

		memberView.setText(String.valueOf(adapter.getCount()) + " places");
		listView.setAdapter(adapter);

		return rootView;
	}
	
	public static void create(Context context, FragmentManager fragmentManager, FindRouteCallback callback) {
		CurrentTripPlaceDialog currentPlacesDialog = new CurrentTripPlaceDialog(context, callback);
		currentPlacesDialog.show(fragmentManager, "current_place_dialog");
	}

	private void addFakeData() {
		Place place = new Place();
		place.setName("Opera House");

		Place place2 = new Place();
		place2.setName("Museum");

		adapter.add(place);
		adapter.add(place2);
	}

	public interface PlaceDialogCallback {
		public void onCallback(Place place);
	}
}
