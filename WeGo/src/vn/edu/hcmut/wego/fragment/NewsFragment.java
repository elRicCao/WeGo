package vn.edu.hcmut.wego.fragment;

import java.util.ArrayList;

import vn.edu.hcmut.wego.R;
import vn.edu.hcmut.wego.entity.News;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends BaseFragment {

	private Context context;
	private ArrayList<News> news;
	
	public NewsFragment(Context context) {
		this.context = context;
		setTitle(this.context.getString(R.string.title_fragment_news));
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO: Load news from database
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);
		return rootView;
	}
}
