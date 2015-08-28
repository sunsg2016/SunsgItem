package com.sunsg.item.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout{

	public MyLinearLayout(Context context) {
		this(context,null);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private void init(Context context){
		
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		return super.onInterceptTouchEvent(ev);
		return true;
	}
	
	

}
