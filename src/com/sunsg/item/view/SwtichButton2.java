package com.sunsg.item.view;

import com.breadtrip.R;
import com.sunsg.item.util.Tools;
import com.sunsg.item.view.SwitchButton.Status;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

public class SwtichButton2 extends FrameLayout{
	private static final int FIRST_DEGREE = 90;// 度
	private static final int ROTATE_DEGREE_OFFSET = 10; // 度
	private Context mContext;
	
	private Scroller mScroller;
	
	private int CHIKED_VIEW_WIDTH = 0;
	
	private int MARGEN = 20;//px
	
	private Status status = Status.CLODED;
	
	private boolean isNeedInit = true;
	
	public SwtichButton2(Context context) {
		this(context,null);
	}
	public SwtichButton2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		this.mContext = context;
		mScroller = new Scroller(mContext, new AccelerateInterpolator());
		
		CHIKED_VIEW_WIDTH = Tools.dp2px(mContext, 44);
		
		MARGEN = Tools.dp2px(mContext, 10);
		
		addImageView(R.drawable.im_spot_display_more);
		addImageView(R.drawable.im_spot_display_share);
		addImageView(R.drawable.im_spot_display_like);
		
	}
	
	private void addImageView(int resourceId){
		int index = getChildCount();
		RotateImageView imageView = new RotateImageView(mContext);
		imageView.setDegree(FIRST_DEGREE  + index * ROTATE_DEGREE_OFFSET);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageView.setImageResource(resourceId);
		imageView.setTag(index);
		imageView.setOnClickListener(onClickListener);
		if(index >0){
			((View)imageView).setAlpha(0);
		}
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(CHIKED_VIEW_WIDTH,CHIKED_VIEW_WIDTH);
		addView(imageView, index, params);
	}
	
	private void taggle(){
		if(status == Status.CLODED){
			status = Status.OPENING;
			mScroller.startScroll(0, 0, 100, 0,300);
			invalidate();
		}else if(status == Status.OPENED){
			status = Status.CLODEING;
			mScroller.startScroll(0, 0, -100, 0,300);
			invalidate();
		}
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mScroller.computeScrollOffset()){
			if(status == Status.CLODED || status == Status.OPENED){
				return;
			}
			
			int crurX = mScroller.getCurrX();
			float present = (crurX * 1.0f)/(mScroller.getFinalX() - mScroller.getStartX());
			if(status == Status.CLODEING){
				present = 1 - present;
			}
			layoutChiled(present);
			
			if(present == 0 && status == Status.CLODEING){
				status = Status.CLODED;
			}
			
			if(present == 1 && status == Status.OPENING){
				status = Status.OPENED;
			}
			invalidate();
			
		}else{
			if (isNeedInit) {
				isNeedInit = false;

				if (status == Status.OPENED) {
					layoutChiled(1);
				} else if (status == Status.CLODED) {
					layoutChiled(0);
				}

				invalidate();
			}
		}
	}
	
	private void layoutChiled(float present){
		int count = getChildCount();
		if(count > 0){
			View firstView = findViewWithTag(0);
			int initRight = firstView.getRight();
			int right = 0;
			View chiledView = null;
			RotateImageView imageView = null;
			for (int i = 0; i < count; i++) {
				chiledView = findViewWithTag(i);
				if(chiledView instanceof RotateImageView){
					imageView = (RotateImageView) chiledView;
					if(i >0){
						chiledView.setAlpha(present);
					}
					int degress = (int) ((FIRST_DEGREE +ROTATE_DEGREE_OFFSET *i)*(1-present));
					imageView.setDegree(degress);
					
					right = initRight - (int)((imageView.getWidth() + MARGEN) *i *present) ;
					imageView.layout(right - imageView.getWidth(), imageView.getTop(), right ,imageView.getHeight());
					
				}
			}
		}
	}
	
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(status == Status.CLODEING){
			mScroller.abortAnimation();
			status = Status.CLODED;
		}else if(status == Status.OPENING){
			mScroller.abortAnimation();
			status = Status.OPENED;
		}
		
		if(status == Status.OPENED){
			int count = getChildCount();
			View view = null;
			int r = 0;
			for (int i = count - 1; i >=0; i--) {
				view = findViewWithTag(i);
				r = right - left - (view.getWidth() + MARGEN) *i;
				view.layout(r - view.getWidth(), 0, r, view.getHeight());
				view.bringToFront();
			}
		}else if(status == Status.CLODED){
			int count = getChildCount();
			View view = null;
			int r = 0;
			for (int i = count - 1; i >=0; i--) {
				view = findViewWithTag(i);
				r = right - left;
				view.layout(r - view.getWidth(), 0, r, view.getHeight());
				view.bringToFront();
			}
		}
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int position = (int) v.getTag();
			if(position == 0){
				taggle();
			}
		}
	};
	
	
	public enum Status{
		CLODED,CLODEING,OPENED,OPENING
	}

}
