package vn.edu.hcmut.wego.adapter;

import java.util.ArrayList;

import vn.edu.hcmut.wego.fragment.BaseFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<BaseFragment> fragments;
	private FragmentManager fragmentManager;
	
	public SectionsPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
		this.fragmentManager = fragmentManager;
	}

	public SectionsPagerAdapter(FragmentManager fragmentManager, ArrayList<BaseFragment> fragments) {
		this(fragmentManager);
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
	
	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}
	
	public void setFragments(ArrayList<BaseFragment> fragments) {
		if (this.fragments != null) {
			for (int i = 0; i < this.fragments.size(); i++) {
				fragmentManager.beginTransaction().remove(this.fragments.get(i)).commit();
			}
		}
		this.fragments = fragments;
	}
}