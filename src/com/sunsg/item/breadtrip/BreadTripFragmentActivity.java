package com.sunsg.item.breadtrip;

import java.util.List;

import com.breadtrip.R;
import com.sunsg.item.breadtrip.adapter.MyBreadTripFragmentViewPageAdapter;
import com.sunsg.item.breadtrip.fragment.BaseFragment;
import com.sunsg.item.util.Logger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Button;

public class BreadTripFragmentActivity extends FragmentActivity{
	private ViewPager mViewPage;
	private Button mTuiJian,mDestination,mTravel,mSale,mPersional;
	private MyBreadTripFragmentViewPageAdapter mMyBreadTripFragmentViewPageAdapter;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_activity_breadtrip);
		getIntentData();
		findView();
		bindEvent();
	}
	
	private void getIntentData(){
		
	}
	
	private void findView(){
		mViewPage = (ViewPager) findViewById(R.id.ViewPager);
		mMyBreadTripFragmentViewPageAdapter = new MyBreadTripFragmentViewPageAdapter(getSupportFragmentManager());
		List<Fragment> item = mMyBreadTripFragmentViewPageAdapter.getItems();
		item.add(BaseFragment.getInstance("推荐",true));
		item.add(BaseFragment.getInstance("目的地",false));
		item.add(BaseFragment.getInstance("旅程",false));
		item.add(BaseFragment.getInstance("特价",false));
		item.add(BaseFragment.getInstance("我的",false));
		mViewPage.setAdapter(mMyBreadTripFragmentViewPageAdapter);
//		mViewPage.setCurrentItem(1);
	}
	
	private void bindEvent(){
		mViewPage.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Logger.e("test", "onpageselected");
				((BaseFragment)mMyBreadTripFragmentViewPageAdapter.getItem(arg0)).loadData();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
}
