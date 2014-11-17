package vn.edu.hcmut.wego.fragment;

import android.support.v4.app.Fragment;

public abstract class WeGoFragment extends Fragment {
	private String title;
	private int iconRes;
	
	public WeGoFragment() {
		
	}
	
	public WeGoFragment(String title, int iconRes) {
		this.title = title;
		this.iconRes = iconRes;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}

}
