package com.sunsg.item.breadtrip.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyBreadTripFragmentViewPageAdapter extends FragmentPagerAdapter{
	private List<Fragment> mItem;
	public MyBreadTripFragmentViewPageAdapter(FragmentManager fm) {
		super(fm);
		mItem = new ArrayList<Fragment>();
	}
	
	public MyBreadTripFragmentViewPageAdapter(FragmentManager fm,List<Fragment> items) {
		super(fm);
		mItem = items;
	}
	
	public List<Fragment> getItems(){
		if(mItem == null) mItem = new ArrayList<Fragment>();
		return mItem;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return mItem.get(arg0);
	}

	@Override
	public int getCount() {
		return mItem.size();
	}

}
