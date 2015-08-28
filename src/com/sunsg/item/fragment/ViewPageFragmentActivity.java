package com.sunsg.item.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.breadtrip.R;

public class ViewPageFragmentActivity extends FragmentActivity{
	private ViewPager mViewPage;
	private FragmentsPageAdapter mFragmentsPageAdapter;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_viewpage);
		mViewPage = (ViewPager) findViewById(R.id.ViewPager);
		mFragmentsPageAdapter = new FragmentsPageAdapter(getSupportFragmentManager());
		mFragmentsPageAdapter.addFragment(new Fragment1());
		mFragmentsPageAdapter.addFragment(new Fragment2());
		mFragmentsPageAdapter.addFragment(new Fragment3());
		mFragmentsPageAdapter.addFragment(new Fragment4());
		mViewPage.setAdapter(mFragmentsPageAdapter);
		mViewPage.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		mViewPage.setCurrentItem(1);
	}
	
	
	private class FragmentsPageAdapter extends FragmentPagerAdapter{
		private List<Fragment> framgents = null;
		public FragmentsPageAdapter(FragmentManager fm) {
			super(fm);
			framgents = new ArrayList<Fragment>();
		}

		@Override
		public Fragment getItem(int pos) {
			return framgents.get(pos);
		}

		@Override
		public int getCount() {
			return framgents.size();
		}
		
		public void addFragment(Fragment fragment){
			framgents.add(fragment);
		}
		
	}
}
