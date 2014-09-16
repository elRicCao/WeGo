package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendsFragment extends BaseFragment {

	public FriendsFragment() {
		setTitle("FRIENDS");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
		return rootView;
	}
	
}
