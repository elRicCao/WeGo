package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.adapter.NewsAdapter;
import vn.edu.hcmut.wego.entity.News;
import vn.edu.hcmut.wego.entity.Trip;
import vn.edu.hcmut.wego.storage.DatabaseOpenHelper;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TripsFragment extends BaseFragment {
	
	private Context context;
	private ArrayList<Trip> trips;
	private NewsAdapter newsAdapter;
	private DatabaseOpenHelper database;

	public TripsFragment(Context context) {
		this.context = context;
		setTitle(context.getString(R.string.title_fragment_trips));
		database = new DatabaseOpenHelper(context);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_trips, container, false);
		return rootView;
	}
	
}
