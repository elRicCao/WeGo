package vn.edu.hcmut.wego.fragment;

import vn.edu.hcmut.wego.R;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoreFragment extends TabFragment {

	private static final String title = "More";
	private static final int iconRes = R.drawable.ic_tab_more;
	private Context context;
	private int userId;

	public MoreFragment(Context context, int userId) {
		super(title, iconRes);
		this.context = context;
		this.userId = userId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_more, container, false);

		return rootView;
	}
}
