package com.sunsg.item.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

public class CustomView extends LinearLayout {
	private OverScroller mScroller;
	private int mTouchSlop;
//	private float x,y;
	private float mLastY;
	
	public CustomView(Context context) {
		super(context);
		init(context);
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		
	}
	
	private void init(Context context){
		mScroller = new OverScroller(getContext());
		setOrientation(LinearLayout.VERTICAL);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
	}

	//调用此方法滚动到目标位置
	public void smoothScrollTo(int fx, int fy) {
		int dx = fx - mScroller.getFinalX();
		int dy = fy - mScroller.getFinalY();
		smoothScrollBy(dx, dy);
	}

	//调用此方法设置滚动的相对偏移
	public void smoothScrollBy(int dx, int dy) {
		//设置mScroller的滚动偏移量
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);
		invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		float y1 = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastY = y1;
			 break;
		case MotionEvent.ACTION_MOVE:
			float dy = y1 - mLastY;
			if(Math.abs(dy) > mTouchSlop){
				return true;
			}
			break;

		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("test", "ontouch");
		int action = event.getAction();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("test", " y s = " + y + "event.getY() = "+event.getY());
//			scrollBy(0, (int)(mLastY - y));
			smoothScrollBy(0,(int)(mLastY - y));
			mLastY = y;
			Log.i("test", " y d = " + y);
			break;
		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}
//		return true;
		return super.onTouchEvent(event);
	}

	@Override
	public void computeScroll() {
		//先判断mScroller滚动是否完成
		if (mScroller.computeScrollOffset()) {
			//这里调用View的scrollTo()完成实际的滚动
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			//必须调用该方法，否则不一定能看到滚动效果
			postInvalidate();
		}
		super.computeScroll();
	}

}
