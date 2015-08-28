package com.sunsg.item.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.breadtrip.R;

public class TipsStarLinearLayout extends LinearLayout{
	private LinearLayout.LayoutParams params = null;
	private int width;
	public int height;
	public int margen;
	public TipsStarLinearLayout(Context context) {
		super(context);
		init(context);
	}

	public TipsStarLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	@SuppressLint("NewApi")
	public TipsStarLinearLayout(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		init(context);
   }
	
	private void init(Context context){
		setOrientation(LinearLayout.HORIZONTAL);
	}
	
	public void setItem(double count){
		setItem(count,R.drawable.im_stars_select,R.drawable.im_stars_mid,R.drawable.im_stars_normal,0,0);
	}
	
	public void setItem(double count,int margen,int size){
		setItem(count,R.drawable.im_stars_select,R.drawable.im_stars_mid,R.drawable.im_stars_normal,margen,size);
	}
	
	public void setItem(double count,int selectResourceId,int midResoutceId,int normalResoutceId){
		setItem(count,selectResourceId,midResoutceId,normalResoutceId);
	}

	public void setItem(double count,int selectResourceId,int midResoutceId,int normalResoutceId,int margen,int size){
		ImageView iv = null;
		int zheng = (int)((count * 10 ) / 10);
		int yu = (int)((count * 10 ) % 10);
		for (int i = 0; i < 5; i++) {
			iv = new ImageView(getContext());
			if(i < zheng){
				iv.setImageResource(selectResourceId);
			}
			else if(i == zheng){
				if(yu ==0){
					iv.setImageResource(normalResoutceId);
				}else{
					iv.setImageResource(midResoutceId);
				}
			}
			else{
				iv.setImageResource(normalResoutceId);
			}
			if(margen == 0) margen = 10;
			iv.setPadding(0, 0, margen, 0);
			margen = margen;
			if(size != 0){
				params = new LinearLayout.LayoutParams(size,size);
				iv.setLayoutParams(params);
			}
			
			addView(iv);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		Log.i("test", "tipsstarts width = "+width +" height = "+height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private float downX;
	private float downY;
	private int count;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			getCount(downX);
			return true;
		case MotionEvent.ACTION_MOVE:
			getCount(event.getX());
			Log.i("test2", "moveeeeeeeeeeeeeeeeeeeeeeeee = "+event.getX());
			postInvalidate();
			return true;
		case MotionEvent.ACTION_OUTSIDE:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			
			break;
			
		}
		return super.onTouchEvent(event);
	}
	
	public double getCount(float x){
		double count = 0;
		float touchx = x - 4*margen;
		count = touchx/((width - 4*margen)/5);
		
		Log.i("test2", "tips  x == z"+x+"count ====== "+count);
		removeAllViews();
		setItem(count);
		return count;
	}
}
