package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.fragment.WeGoFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WeGoPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<WeGoFragment> fragments;

	public WeGoPagerAdapter(FragmentManager fragmentManager, ArrayList<WeGoFragment> fragments) {
		super(fragmentManager);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return fragments.get(position).getTitle();
	}
	
	public int getPageIconRes(int position){
		return fragments.get(position).getIconRes();
	}

}