package com.sunsg.item.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Scroller;

import com.sunsg.item.util.Logger;

/**
 * @description 可以移动的 linearlayout
 * @author sunsg
 * @date 2015.8.6
 */
public class CanMoveLinearLayout extends LinearLayout {
	/** 使view平滑滚动辅助类 */
	private OverScroller mScroller;
	/** 速度追踪 */
	private VelocityTracker mVelocityTracker;
	/** 区别用户点击还是 拖拽 dy > mTouchSlop 拖拽 */
	private int mTouchSlop;

	/** 最大速度 每秒钟移动 */
	private int mMaximumVelocity;

	/** 最小速度 每秒钟移动 */
	private int mMinimumVelocity;

	/** up时 Y坐标 */
	private float mLastY;
	/** 是否时拖拽 */
	private boolean mDragging;

	private Scroller mScroll;

	public CanMoveLinearLayout(Context context) {
		this(context, null);
	}

	public CanMoveLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CanMoveLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void init(Context context) {
		mScroller = new OverScroller(context);

		mVelocityTracker = VelocityTracker.obtain();
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();// 8
																			// 系统值
																			// 大于表示移动
																			// 小于表示点击
		mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();// 系统值
																							// 8000
		mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();// 系统值
																							// 50
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Logger.i("test", "onTouchEvent");
		mVelocityTracker.addMovement(event);
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Logger.i("test", "MotionEvent.ACTION_DOWN");
			if (!mScroller.isFinished())
				mScroller.abortAnimation();
			mVelocityTracker.clear();
			mVelocityTracker.addMovement(event);
			mLastY = event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
			Logger.i("test", "MotionEvent.ACTION_MOVE");
			float dy = y - mLastY;
			// 判断是否是滑动
			if (!mDragging && Math.abs(dy) > mTouchSlop) {
				mDragging = true;
			}

			if (mDragging) {
				// yidong
				Logger.i("test", "move mDragging");
				scrollBy(0, (int) -dy);
				mLastY = y;
			}
			break;

		case MotionEvent.ACTION_UP:
			mDragging = false;
			mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
			// 获得滑动的速度 向上滑动 小于 0 向下滑动 大于 0
			int velocityY = (int) mVelocityTracker.getYVelocity();
			Logger.i("test", "velocityY = "+velocityY);
			if (Math.abs(velocityY) > mMinimumVelocity) {
				fling(-velocityY);
			}
			mVelocityTracker.clear();
			break;

		case MotionEvent.ACTION_CANCEL:
			mDragging = false;
			if (!mScroller.isFinished())
				mScroller.abortAnimation();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}


	public void fling(int velocityY) {
		Logger.d("test", "fling " + velocityY);
		mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, (int)Math.abs((-mLastY - getScrollY())));
		invalidate();// 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
	}

	@Override
	public void computeScroll() {
		// true 表示滚动没有完成 false表示滚动完成
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			invalidate();
		}
		super.computeScroll();
	}

}
