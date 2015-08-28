package com.sunsg.item.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.breadtrip.R;
import com.sunsg.item.util.Tools;

/**
 * @deacripton 横向打开和关闭 view
 * @author sunsg
 *
 */
public class HorOpenAndCloseView extends LinearLayout {
	
	public interface OnItemClickListener{
		void onitemClick(View view,int position);
	}
	public int CLODED = 1;
	public int CLODEDING = 2;
	public int OPENED = 3;
	public int OPENING = 4;
	private static final int FIRST_DEGREE = 90;// 度
	private static final int ROTATE_DEGREE_OFFSET = 10; // 度
	private int CHILED_WIDTH = 0;
	private int CHILED_HEIGHT = 0;
	private int MARGEN = 20;// px
	private Context context;
	private Scroller mScroller;
	private OnItemClickListener mOnItemClickListener;
	
	private int status = OPENED;

	public HorOpenAndCloseView(Context context) {
		this(context, null);
	}

	public HorOpenAndCloseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HorOpenAndCloseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void init(Context context) {
		setOrientation(LinearLayout.HORIZONTAL);

		this.context = context;

		MARGEN = Tools.dp2px(context, 10);

		CHILED_WIDTH = Tools.dp2px(context, 44);
		
		CHILED_HEIGHT = CHILED_WIDTH;

		mScroller = new Scroller(context, new AccelerateInterpolator());
		addChiledViews(R.drawable.im_spot_display_like);
		addChiledViews(R.drawable.im_spot_display_share);
		addChiledViews(R.drawable.im_spot_display_larger);
		addChiledViews(R.drawable.im_spot_display_more);

	}
	
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		this.mOnItemClickListener = onItemClickListener;
	}
	
	private void taggle(){
		if(status == OPENED){
			mScroller.startScroll(0, 0, -100, 0,300);
			invalidate();
		}else if(status == CLODED){
			mScroller.startScroll(0, 0, 100, 0,300);
			invalidate();
		}
	}
	
	/**
	 * 移动
	 * @param present 百分比
	 */
	private void move(float present,float x){
		for (int i = 0; i < getChildCount(); i++) {
			if(i != getChildCount() - 1){
				findViewWithTag(i).setAlpha(present);
//				findViewWithTag(i).animate().translationXBy(x).start();
				findViewWithTag(i).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			}
		}
	}


	/**
	 * 添加vaiew
	 * 
	 * @param resourceId
	 *            资源id
	 */
	private void addChiledViews(int resourceId) {
		int index = getChildCount();
		RotateImageView rotateImageView = new RotateImageView(context);
		rotateImageView.setDegree(FIRST_DEGREE + index * ROTATE_DEGREE_OFFSET);
		rotateImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		rotateImageView.setImageResource(resourceId);
		rotateImageView.setTag(index);
//		if(index >0){
//			((View)rotateImageView).setAlpha(0);
//		}
		
		
			rotateImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					if(mOnItemClickListener != null){
						int position = (int) v.getTag();
//						mOnItemClickListener.onitemClick(v, position);
						taggle();
//					}
				}
			});
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CHILED_WIDTH,CHILED_HEIGHT);
		rotateImageView.setLayoutParams(params);
		params.rightMargin = MARGEN;
		addView(rotateImageView);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mScroller.computeScrollOffset()){
			float present = mScroller.getCurrX() * 1.0f/(mScroller.getFinalX() - mScroller.getStartX());
			if(status == OPENED){
				present = 1 - present;
			}
			move(present,mScroller.getCurrX());
//			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			
//			if(status == CLODED){
//				status = OPENED;
//			}else if(status == OPENED){
//				status = CLODED;
//			}
			
			postInvalidate();
		}
	}
}

