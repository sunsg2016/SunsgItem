package com.sunsg.item;

import com.breadtrip.R;
import com.sunsg.item.fragment.TabFragment;
import com.sunsg.item.view.CustomView;
import com.sunsg.item.view.SimpleViewPagerIndicator;
import com.sunsg.item.view.StickyNavLayout;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumeActivity extends FragmentActivity {
	private CustomView mView;
	private String[] mTitles = new String[] { "简介", "评价", "相关" };
	private SimpleViewPagerIndicator mIndicator;
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private TabFragment[] mFragments = new TabFragment[mTitles.length];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				consumeView();
//		setContentView(R.layout.activity_consume);
//		findview();
//		initData();
//		bind();
		
		
//		setContentView(R.layout.activity_consume_move_linearlayout);
//		initLinearLayout();
	}
	
	private void initLinearLayout(){
		
	}

	private void findview() {
		mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
		mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
	}

	private void initData() {
		mIndicator.setOnItemClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int tag = (int) v.getTag();
				mViewPager.setCurrentItem(tag);
			}
		});
		mIndicator.setTitles(mTitles);

		for (int i = 0; i < mTitles.length; i++) {
			mFragments[i] = (TabFragment) TabFragment.newInstance(mTitles[i]);
		}

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mTitles.length;
			}

			@Override
			public Fragment getItem(int position) {
				return mFragments[position];
			}

		};

		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
	}

	private void bind() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				mIndicator.scroll(position, positionOffset);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void consumeView() {
		mView = new CustomView(this);
		Button btn = new Button(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1140 * 2);
		btn.setBackgroundColor(Color.BLUE);
		btn.setLayoutParams(params);
		mView.addView(btn);

		Button btn2 = new Button(this);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1140 * 2);
		btn2.setBackgroundColor(Color.GREEN);
		btn2.setLayoutParams(params);
		mView.addView(btn2);
		setContentView(mView);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "toas.btn", Toast.LENGTH_SHORT).show();
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "toas.btn2", Toast.LENGTH_SHORT).show();

			}
		});
	}

}
