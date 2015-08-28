package com.sunsg.item.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.sunsg.item.util.Logger;

public class TextTouchEventButton extends Button{

	public TextTouchEventButton(Context context) {
		super(context);
	}
	
	

	public TextTouchEventButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	

	public TextTouchEventButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Logger.e("test", "onTouchEvent ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			Logger.e("test", "onTouchEvent ACTION_MOVE");
			break;
		case MotionEvent.ACTION_UP:
			Logger.e("test", "onTouchEvent ACTION_UP");
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Logger.e("test", "dispatchTouchEvent ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			Logger.e("test", "dispatchTouchEvent ACTION_MOVE");
			break;
		case MotionEvent.ACTION_UP:
			Logger.e("test", "dispatchTouchEvent ACTION_UP");
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}

}
