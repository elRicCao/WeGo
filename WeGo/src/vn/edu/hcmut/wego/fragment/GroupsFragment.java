package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroupsFragment extends BaseFragment {
	
	public GroupsFragment() {
		setTitle("GROUPS");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
		return rootView;
	}
}
