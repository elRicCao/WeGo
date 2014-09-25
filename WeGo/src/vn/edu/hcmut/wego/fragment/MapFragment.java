package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends BaseFragment {

	public MapFragment(Context context) {
		setTitle(context.getString(R.string.title_fragment_map));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		return rootView;
	}
	
}
