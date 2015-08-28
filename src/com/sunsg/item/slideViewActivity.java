package com.sunsg.item;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.Tools;
import com.sunsg.item.view.HotelDrawerScrollView;

public class slideViewActivity extends Activity implements OnTouchListener,OnClickListener{
	private HotelDrawerScrollView rootScroll;//自定义scrollview
	private RelativeLayout leftContent;//scrollView 里的左边部分
	private RelativeLayout mainContent;//scrollView 里的主要部分
	private Button showMainContent;
	private float xDown;
	private float xUp;
	private float yDown;
	private float yUp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideview);
		rootScroll = (HotelDrawerScrollView) findViewById(R.id.root_slide_SV);
		leftContent = (RelativeLayout) findViewById(R.id.left_content);
		mainContent = (RelativeLayout) findViewById(R.id.main_content);
		showMainContent = (Button) findViewById(R.id.show_main_content);
		
		
		
		updateMainViewLayout();
		rootScroll.setNoResponseWidth(leftContent.getLayoutParams().width);
		rootScroll.setOnTouchListener(this);
		rootScroll.post(new Runnable() {
			
			@Override
			public void run() {
				rootScroll.scrollTo(leftContent.getLayoutParams().width, 0);
//				rootScroll.scrollTo(0, 0);
				showMainContent.setVisibility(View.GONE);
			}
		});
		
		showMainContent.setOnClickListener(this);
		
		findViewById(R.id.btn_test_touch_event).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Logger.e("test", "setOnTouchListener ACTION_DOWN");
					break;
				case MotionEvent.ACTION_MOVE:
					Logger.e("test", "setOnTouchListener ACTION_MOVE");
					break;
				case MotionEvent.ACTION_UP:
					Logger.e("test", "setOnTouchListener ACTION_UP");
					break;

				default:
					break;
				}
				return false;
			}
		});
	}
	
	/*
	 * 调整主要布局
	 * 
	 */
	private void updateMainViewLayout() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int leftWidth = dm.widthPixels - Tools.dp2px(this, 60);
		int mainWidth = dm.widthPixels;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(leftWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		leftContent.setLayoutParams(lp);
		lp = new LinearLayout.LayoutParams(mainWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		mainContent.setLayoutParams(lp);
	}
	
	
	//OnTouchListener
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId() == R.id.root_slide_SV) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
					xDown = xUp = event.getX();
					yDown = yUp = event.getY();
			}else if(event.getAction() == MotionEvent.ACTION_MOVE){
					xDown = xUp;
					xUp = event.getX();
					yDown = yUp;
					yUp = event.getY();
			}
			else if(event.getAction() == MotionEvent.ACTION_CANCEL ||event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_UP) {
					xUp = event.getX();
					yUp = event.getY();
					boolean leftAndRight = Math.abs(xUp - xDown) - Math.abs(yUp - yDown) > 0;
					if(leftAndRight && xUp - xDown > 0){
						rootScroll.post(new Runnable() {public void run() {
							rootScroll.smoothScrollTo(0, 0);
							showMainContent.setVisibility(View.VISIBLE);
						}});
					} else {
						rootScroll.post(new Runnable() {public void run() {
							rootScroll.smoothScrollTo(leftContent.getLayoutParams().width, 0);
							showMainContent.setVisibility(View.GONE);
						}});
					}
			}
		}
		return false;
	}
	
	//点击事件 OnClickListener
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.show_main_content) {
			rootScroll.post(new Runnable() {public void run() {
				rootScroll.smoothScrollTo(leftContent.getLayoutParams().width, 0);
				showMainContent.setVisibility(View.GONE);
			}});
		}
		
	}
}
