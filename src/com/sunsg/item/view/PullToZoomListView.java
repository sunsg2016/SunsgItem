package com.sunsg.item.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.sunsg.item.util.BitmapUtils;
import com.sunsg.item.util.Logger;

public class PullToZoomListView extends ListView implements AbsListView.OnScrollListener {
	private static final int INVALID_VALUE = -1;
	private static final String TAG = "PullToZoomListView";
	private static final Interpolator sInterpolator = new Interpolator() {
		public float getInterpolation(float paramAnonymousFloat) {
			float f = paramAnonymousFloat - 1.0F;
			return 1.0F + f * (f * (f * (f * f)));
		}
	};
	int mActivePointerId = -1;
	private FrameLayout mHeaderContainer;
	private int mHeaderHeight;
	private ImageView mIvCover;
	float mLastMotionY = -1.0F;
	float mLastScale = -1.0F;
	float mMaxScale = -1.0F;
	private AbsListView.OnScrollListener mOnScrollListener;
	private ScalingRunnalable mScalingRunnalable;
	private int mScreenHeight;
	private ImageView mShadow;
	
	private boolean isScrolling = false;
	
	private final PointF mTouchPoint = new PointF();
	private int mTouchSlop;
	public boolean mCancelClick = true;
	public PullToZoomListView(Context paramContext) {
		super(paramContext);
		init(paramContext);
	}

	public PullToZoomListView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}

	public PullToZoomListView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init(paramContext);
	}
	

	private void endScraling() {
		if (this.mHeaderContainer.getBottom() >= this.mHeaderHeight)
			Log.i("test", "endScraling");
		this.mScalingRunnalable.startAnimation(200L);
	}
	
	public boolean getScrollState(){
		return isScrolling;
	}
	private void init(Context paramContext) {
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		((Activity) paramContext).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		this.mScreenHeight = localDisplayMetrics.heightPixels;
		this.mHeaderContainer = new FrameLayout(paramContext);
		this.mIvCover = new ImageView(paramContext);
//		mIvCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
		int i = localDisplayMetrics.widthPixels;
		setHeaderViewSize(i, (int) (9.0F * (i / 16.0F)));
		this.mShadow = new ImageView(paramContext);
		FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -2);
//		localLayoutParams.gravity = 80;
		this.mShadow.setLayoutParams(localLayoutParams);
		this.mHeaderContainer.addView(this.mIvCover);
		this.mHeaderContainer.addView(this.mShadow);
		
		addHeaderView(this.mHeaderContainer);
		this.mScalingRunnalable = new ScalingRunnalable();
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		Log.i("test", "touchslop ================ "+mTouchSlop);
		super.setOnScrollListener(this);
		
	}
	
	public void setHeadViewContent(View view){
		if(view != null){
			FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			localLayoutParams.gravity = Gravity.BOTTOM;
			view.setLayoutParams(localLayoutParams);
			this.mHeaderContainer.addView(view);
		}
	}

	private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
		int i = (paramMotionEvent.getAction()) >> 8;
		if (paramMotionEvent.getPointerId(i) == this.mActivePointerId)
			if (i != 0) {
				int j = 1;
				this.mLastMotionY = paramMotionEvent.getY(0);
				this.mActivePointerId = paramMotionEvent.getPointerId(0);
				return;
			}
	}

	private void reset() {
		this.mActivePointerId = -1;
		this.mLastMotionY = -1.0F;
		this.mMaxScale = -1.0F;
		this.mLastScale = -1.0F;
	}

	public ImageView getIvCoverView() {
		return this.mIvCover;
	}
	
	public void setIvCoverResourceBitmap(int resourceId){
		mIvCover.setImageResource(resourceId);
		mIvCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}
	
	public void setIvCoverBitmap(Bitmap bitmap){
		mIvCover.setImageBitmap(BitmapUtils.getBlurBitmap(getContext(), bitmap));
		mIvCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}
	
	public void setIvCoverView(ImageView imCover){
		if(imCover != null) this.mIvCover = imCover;
	}
	
	private View titleView;
	float scrollHeight = 0;
	private int minHeight = 0;
	private int maxHeight = 0;

	public void setTitleView(View titleView){
		this.titleView = titleView;
	}
	
	private void initHeight() {
		maxHeight = mIvCover.getHeight();
		minHeight = maxHeight - titleView.getHeight();
	}
	
	// maxHeight=600,minHeight=450
	// 图的高度是600，图减去标题栏的高度是450
	private void changeAlpha() {
		if(titleView != null){
			initHeight();
			// 假设图片高度为500dp,标题栏高度为100dp,则滑动高度为500-100到500，开始透明度渐变效果
			// 滑动高度大于500，一直是不透明状态，滑动高度小于400，一直是透明状态
			// 限制高度不能小于0
			if (scrollHeight < minHeight) {
				titleView.getBackground().setAlpha(0);
			} else if (scrollHeight < maxHeight) {
				titleView.getBackground().setAlpha((int) (255 * (scrollHeight - minHeight) / (maxHeight - minHeight)));
			} else {
				titleView.getBackground().setAlpha(255);
			}
		}
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			
//			return false;
//		case MotionEvent.ACTION_MOVE:
//			if(mCancelClick){
//				return false;
//			}else{
//				return false;
//			}
//
//		case MotionEvent.ACTION_UP:
//			
//			break;
//
//
//		default:
//			break;
//		}
//		return false;
////		return super.onInterceptTouchEvent(ev);
//	}
	
	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
		super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
		if (this.mHeaderHeight == 0)
			this.mHeaderHeight = this.mHeaderContainer.getHeight();
	}
	
	
	@Override
	public void onScroll(AbsListView paramAbsListView, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		isScrolling = true;
		float f = this.mHeaderHeight - this.mHeaderContainer.getBottom();
		Log.i("test", "f|" + f  +"firstVisibleItem = "+firstVisibleItem);
		if ((f > 0.0F) && (f < this.mHeaderHeight)) {
			Log.i("test", "1");
			int i = (int) (0.65D * f);
//			this.mIvCover.scrollTo(0, -i );
			isScrolling = false;
		} else if (this.mIvCover.getScrollY() != 0) {
			Log.i("test", "2");
			this.mIvCover.scrollTo(0, 0);
			isScrolling = false;
		}else if(f == 0f){
			isScrolling = false;
		}
		
		
		
		// 高度等于每项高度×滑过的项个数-当前项的top(为负值)
					View firstView = paramAbsListView.getChildAt(0);
					if (firstView != null) {
						int top = 0, height = 0;
						top = firstView.getTop();
						height = firstView.getHeight();
						Log.w("test", "top  == "+top +"height = "+height);
						if (firstVisibleItem == 0) {// HeadView可见
							scrollHeight = firstVisibleItem * height - top;
						} else {
							scrollHeight = mIvCover.getHeight() + (firstVisibleItem - 1) * height - top;
						}
						changeAlpha();
					}
		
		
		
		if (this.mOnScrollListener != null) {
			this.mOnScrollListener.onScroll(paramAbsListView, firstVisibleItem,visibleItemCount, totalItemCount);
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (this.mOnScrollListener != null)
			this.mOnScrollListener.onScrollStateChanged(view,scrollState);
		
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 高度等于每项高度×滑过的项个数-当前项的top(为负值)

			View firstView = view.getChildAt(0);
			if (firstView != null) {
				int top = 0, height = 0;
				int firstVisibleItem = view.getFirstVisiblePosition();
				top = firstView.getTop();
				height = firstView.getHeight();
				if (firstVisibleItem == 0) {// HeadView可见
					scrollHeight = firstVisibleItem * height - top;
				} else {
					scrollHeight = mIvCover.getHeight() + (firstVisibleItem - 1) * height - top;
				}
				changeAlpha();
			}
		}
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
//		if (!this.mZoomable) {  
//            return super.onInterceptTouchEvent(paramMotionEvent);  
//        }  
        switch (paramMotionEvent.getAction() & MotionEvent.ACTION_MASK) {  
        case MotionEvent.ACTION_DOWN:  
  
            this.mActivePointerId = paramMotionEvent.getPointerId(0);  
            this.mMaxScale = (this.mScreenHeight / this.mHeaderHeight);  
            break;  
  
        case MotionEvent.ACTION_UP:  
            reset();  
            break;  
  
        case MotionEvent.ACTION_POINTER_DOWN:  
            this.mActivePointerId = paramMotionEvent  
                    .getPointerId(paramMotionEvent.getActionIndex());  
            break;  
  
        case MotionEvent.ACTION_POINTER_UP:  
            onSecondaryPointerUp(paramMotionEvent);  
            break;  
        }  
		return super.onInterceptTouchEvent(paramMotionEvent);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		
		final float x = paramMotionEvent.getX();
		final float y = paramMotionEvent.getY();
		Log.i("test", "" + (0xFF & paramMotionEvent.getAction()));
		Log.i("test", "onTouchEvent");
		switch (paramMotionEvent.getAction()) {
		case 4:
		case MotionEvent.ACTION_DOWN:
			if (!this.mScalingRunnalable.mIsFinished) {
				this.mScalingRunnalable.abortAnimation();
			}
			this.mLastMotionY = paramMotionEvent.getY();
			this.mActivePointerId = paramMotionEvent.getPointerId(0);
			this.mMaxScale = (this.mScreenHeight / this.mHeaderHeight);
			this.mLastScale = (this.mHeaderContainer.getBottom() / this.mHeaderHeight);
			mTouchPoint.x = x;
			mTouchPoint.y = y;
			mCancelClick = true;
			Logger.e("test3","dispatchTouchEvent MotionEvent.ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			Logger.e("test3","dispatchTouchEvent MotionEvent.ACTION_MOVE");
			Log.i("test", "paramMotionEvent.getY() == "+paramMotionEvent.getY() + "mTouchPoint.y = "+mTouchPoint.y +" Math.abs(paramMotionEvent.getY() - mTouchPoint.y) = "+Math.abs(paramMotionEvent.getY() - mTouchPoint.y) +" mTouchSlop = "+mTouchSlop);
			if (Math.abs(paramMotionEvent.getY() - mTouchPoint.y) > mTouchSlop) {
				mCancelClick = false;
			}else{
				mCancelClick = true;
			}
			Log.i("test","mCancelClick   = "+mCancelClick);
			
			Log.i("test", "mActivePointerId  list mmmmmmmmmmmmmmmmmmmmmmmove" + mActivePointerId);
			int j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
			if (j == -1) {
				Log.e("PullToZoomListView", "Invalid pointerId="
						+ this.mActivePointerId + " in onTouchEvent");
			} else {
				if (this.mLastMotionY == -1.0F)
					this.mLastMotionY = paramMotionEvent.getY(j);
				if (this.mHeaderContainer.getBottom() >= this.mHeaderHeight) {
					ViewGroup.LayoutParams localLayoutParams = this.mHeaderContainer
							.getLayoutParams();
					float f = ((paramMotionEvent.getY(j) - this.mLastMotionY + this.mHeaderContainer
							.getBottom()) / this.mHeaderHeight - this.mLastScale)
							/ 2.0F + this.mLastScale;
					if ((this.mLastScale <= 1.0D) && (f < this.mLastScale)) {
						localLayoutParams.height = this.mHeaderHeight;
						this.mHeaderContainer
								.setLayoutParams(localLayoutParams);
						return super.onTouchEvent(paramMotionEvent);
					}
					this.mLastScale = Math.min(Math.max(f, 1.0F),this.mMaxScale);
					localLayoutParams.height = ((int) (this.mHeaderHeight * this.mLastScale));
					if (localLayoutParams.height < this.mScreenHeight)
						this.mHeaderContainer
								.setLayoutParams(localLayoutParams);
					this.mLastMotionY = paramMotionEvent.getY(j);
					return false;
				}
				this.mLastMotionY = paramMotionEvent.getY(j);
			}
			break;
		case MotionEvent.ACTION_UP:
			Logger.e("test3","dispatchTouchEvent MotionEvent.ACTION_UP");
			reset();
			endScraling();
			break;
		case MotionEvent.ACTION_CANCEL:
			int i = paramMotionEvent.getActionIndex();
			this.mLastMotionY = paramMotionEvent.getY(i);
			this.mActivePointerId = paramMotionEvent.getPointerId(i);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			onSecondaryPointerUp(paramMotionEvent);
			this.mLastMotionY = paramMotionEvent.getY(paramMotionEvent
					.findPointerIndex(this.mActivePointerId));
			break;
		}
		return super.onTouchEvent(paramMotionEvent);
	}

	public void setHeaderViewSize(int paramInt1, int paramInt2) {
		Object localObject = this.mHeaderContainer.getLayoutParams();
		if (localObject == null)
			localObject = new AbsListView.LayoutParams(paramInt1, paramInt2);
		((ViewGroup.LayoutParams) localObject).width = paramInt1;
		((ViewGroup.LayoutParams) localObject).height = paramInt2;
		this.mHeaderContainer.setLayoutParams((ViewGroup.LayoutParams) localObject);
		this.mHeaderHeight = paramInt2;
	}

	public void setOnScrollListener(
			AbsListView.OnScrollListener paramOnScrollListener) {
		this.mOnScrollListener = paramOnScrollListener;
	}

	public void setShadow(int paramInt) {
		this.mShadow.setBackgroundResource(paramInt);
	}

	class ScalingRunnalable implements Runnable {
		long mDuration;
		boolean mIsFinished = true;
		float mScale;
		long mStartTime;

		ScalingRunnalable() {
		}

		public void abortAnimation() {
			this.mIsFinished = true;
		}

		public boolean isFinished() {
			return this.mIsFinished;
		}

		public void run() {
			float f2;
			ViewGroup.LayoutParams localLayoutParams;
			if ((!this.mIsFinished) && (this.mScale > 1.0D)) {
				float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) this.mStartTime)
						/ (float) this.mDuration;
				f2 = this.mScale - (this.mScale - 1.0F) * PullToZoomListView.sInterpolator.getInterpolation(f1);
				localLayoutParams = PullToZoomListView.this.mHeaderContainer.getLayoutParams();
				if (f2 > 1.0F) {
					Log.i("test", "f2>1.0");
					localLayoutParams.height = PullToZoomListView.this.mHeaderHeight;
					
					localLayoutParams.height = ((int) (f2 * PullToZoomListView.this.mHeaderHeight));
					
					PullToZoomListView.this.mHeaderContainer.setLayoutParams(localLayoutParams);
					PullToZoomListView.this.post(this);
					return;
				}
				this.mIsFinished = true;
			}
		}

		public void startAnimation(long paramLong) {
			this.mStartTime = SystemClock.currentThreadTimeMillis();
			this.mDuration = paramLong;
			this.mScale = ((float) (PullToZoomListView.this.mHeaderContainer.getBottom()) / PullToZoomListView.this.mHeaderHeight); 
			this.mIsFinished = false;
			PullToZoomListView.this.post(this);
		}
	}
}
