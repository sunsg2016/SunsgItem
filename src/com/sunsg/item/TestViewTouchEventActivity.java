package com.sunsg.item;

import com.breadtrip.R;
import com.sunsg.item.view.MyLinearLayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TestViewTouchEventActivity extends FragmentActivity{
	private Button btnTest1;
	private Button btnTest2;
	private MyLinearLayout llTest2;
	private ImageView mImageView;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_test_view_touch_event);
		initView();
		bindEvnt();
		initData();
	}
	
	private void initView(){
		btnTest1 = (Button) findViewById(R.id.btn_test1);
		btnTest2 = (Button) findViewById(R.id.btn_test2);
		mImageView = (ImageView) findViewById(R.id.im_test1);
		llTest2 = (MyLinearLayout) findViewById(R.id.ll_test2);
	}
	
	private void bindEvnt(){
		//按钮点击
		btnTest1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  Log.d("test", "button1 onClick execute");  
			}
		});
		
		//按钮touch点击
		btnTest1.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 Log.d("test", "onTouch execute, action " + event.getAction());  
				return false;
			}
		});
		
		//button2 点击
		btnTest2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Log.d("test", "button2 onClick execute");  
			}
		});
		
		//图片点击
//		mImageView.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Log.d("test", "image onclick execute "); 
//			}
//		});
		
		//图片touch点击
		mImageView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d("test", "image onTouch execute, action " + event.getAction());  
				return false;
			}
		});
		
		
		
		//父布局点击
		llTest2.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d("test", "myLayout on touch");  
				return true;
			}
		});
		
		
	}
	
	private void initData(){
		
	}
}
