package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends BaseFragment {

	public NewsFragment() {
		setTitle("NEWS");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);
		return rootView;
	}
}
