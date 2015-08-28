package com.sunsg.item.fragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.breadtrip.R;
import com.sunsg.item.util.Tools;

public class WXFragmentActivity extends FragmentActivity{
	
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	
	private ViewPager mViewPage;
	
	private ChatFragment chatFragment;

	private FoundFragment foundFragment;

	private ContactsFragment contactsFragment;
	
	private DisplayMetrics dm;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		actionBar();
		findView();
	}
	
	private void findView(){
		setContentView(R.layout.fragment_wx);
		dm = getResources().getDisplayMetrics();
		setOverflowShowingAlways();
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		
		mViewPage = (ViewPager) findViewById(R.id.pager);
		mViewPage.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		
		mPagerSlidingTabStrip.setViewPager(mViewPage);
		setTabsValue();
	}
	
	
	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		mPagerSlidingTabStrip.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#45c01a"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#45c01a"));
		// 取消点击Tab时的背景色
		mPagerSlidingTabStrip.setTabBackground(0);
	}
	
	
	//加载menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);  
		SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				Tools.toast(getApplication(), "query = "+query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				//每次输入都会调用此方法
				Tools.toast(getApplication(), "newText = "+newText);
				return true;
			}
		});
		searchItem.setOnActionExpandListener(new OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				//此item打开
//				Tools.toast(getApplicationContext(), "expand");
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				//此item隐藏
//				Tools.toast(getApplicationContext(), "collapse");
				
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
	
	//即使是在有Menu键的手机上，也能让overflow按钮显示出来了
	private void setOverflowShowingAlways() {  
	    try {  
	        ViewConfiguration config = ViewConfiguration.get(this);  
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");  
	        menuKeyField.setAccessible(true);  
	        menuKeyField.setBoolean(config, false);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	}  
	
	//menu item点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
//			Tools.toast(getApplication(), "search");
			return true;
		case R.id.action_plus:
			Tools.toast(getApplication(), "plus");
			return true;
		case R.id.action_album:
			Tools.toast(getApplication(), "album");
			return true;
		case R.id.action_collection:
			Tools.toast(getApplication(), "collection");
			return true;
		case R.id.action_card:
			Tools.toast(getApplication(), "card");
			return true;
		case R.id.action_settings:
			Tools.toast(getApplication(), "settings");
		case R.id.action_feed:
			Tools.toast(getApplication(), "feed");
			return true;
		case android.R.id.home:
			Tools.toast(getApplication(), "home");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		 if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {  
		        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {  
		            try {  
		                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);  
		                m.setAccessible(true);  
		                m.invoke(menu, true);  
		            } catch (Exception e) {  
		            }  
		        }  
		    }  
		return super.onMenuOpened(featureId, menu);
	}
	
	//隐藏actionbar
	private void actionBar(){
		ActionBar actionBar = getActionBar();
		if(actionBar != null) {
//			actionBar.hide();
			//图标左边的剪头
			actionBar.setDisplayHomeAsUpEnabled(true);  
		}
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "聊天", "发现", "通讯录" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (chatFragment == null) {
					chatFragment = new ChatFragment();
				}
				return chatFragment;
			case 1:
				if (foundFragment == null) {
					foundFragment = new FoundFragment();
				}
				return foundFragment;
			case 2:
				if (contactsFragment == null) {
					contactsFragment = new ContactsFragment();
				}
				return contactsFragment;
			default:
				return null;
			}
		}

	}
}
