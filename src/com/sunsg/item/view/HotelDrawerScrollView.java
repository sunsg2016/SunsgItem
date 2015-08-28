/**
 * 
 */
package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * @author 尚传亮
 * 
 * 
 */
public class HotelDrawerScrollView extends HorizontalScrollView {
	
	private OnDrawerListener onDrawerListener;
	public static interface OnDrawerListener {
		void onDrawer(int scrollx, int scrolly);
	}
	public void setOnDrawerListener(OnDrawerListener listener) { onDrawerListener = listener; }
	
	/**
	 * 设置Touch无响应宽度
	 * 
	 * @param width
	 */
	public void setNoResponseWidth(int width) { noResponseWidth = width; }
	private int noResponseWidth = 0;

	/**
	 * @param context
	 */
	public HotelDrawerScrollView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public HotelDrawerScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public HotelDrawerScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_DOWN) {
			if((int) ev.getX() < noResponseWidth && getScrollX() <= 0) {
				return false;
			}
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(onDrawerListener != null) onDrawerListener.onDrawer(getScrollX(), getScrollY());
	}
}
