package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ViewDragHelperLayout extends LinearLayout {
	/** 简单 移动 */
	private static final int TYPE_NORMAL = 0;
	/** 自动回原点 移动 */
	private static final int TYPE_AUTO_BACK = 1;
	/** 触摸左边界 移动 */
	private static final int TYPE_EDGE_TRACE_LEFT = 2;
	private ViewDragHelper mDragger;

	/** 就是演示简单的移动 */
	// private View mDragView;
	/** 演示除了移动后，松手自动返回到原本的位置。（注意你拖动的越快，返回的越快） */
	// private View mAutoBackView;
	/** 边界移动时对View进行捕获。 */
	// private View mEdgeTrackerView;

	private Point mAutoBackOriginPos = new Point();

	public ViewDragHelperLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
			/** 返回 true 可以移动 */
			@Override
			public boolean tryCaptureView(View child, int pointerId) {
				// mEdgeTrackerView禁止直接移动
//				Log.d("test", "pointerId = " + pointerId);
				// view 的 tag
				int type = (int) child.getTag();
				if(type == TYPE_AUTO_BACK){
					//自动回原点的 初始 x，y值
					mAutoBackOriginPos.x = child.getLeft();
					mAutoBackOriginPos.y = child.getTop();
				}
				return type != TYPE_EDGE_TRACE_LEFT;
			}

			/** left 横向 即将移动的位置 */
			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				//左右边界的限制
				final int leftBound = getPaddingLeft();
				final int rightBound = getWidth() - child.getWidth() - leftBound;
				final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
//				Log.d("test", "dx = " + dx);
				return newLeft;
			}

			/** top 纵向即将移动的位置 */
			@Override
			public int clampViewPositionVertical(View child, int top, int dy) {
				return top;
			}

			/** 手指释放的时候回调 */
			@Override
			public void onViewReleased(View releasedChild, float xvel, float yvel) {
				// super.onViewReleased(releasedChild, xvel, yvel);
				// mAutoBackView手指释放时可以自动回去
				int type = (int) releasedChild.getTag();
				if (type == TYPE_AUTO_BACK) {
					mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
					invalidate();
				}
			}

			/** 在边界拖动时回调 */
			@Override
			public void onEdgeDragStarted(int edgeFlags, int pointerId) {
				// super.onEdgeDragStarted(edgeFlags, pointerId);
				mDragger.captureChildView(findViewWithTag(TYPE_EDGE_TRACE_LEFT), pointerId);
			}
		});
		/** 左边 可以滑动 */
		mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return mDragger.shouldInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragger.processTouchEvent(event);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// mAutoBackOriginPos.x = mAutoBackView.getLeft();
		// mAutoBackOriginPos.y = mAutoBackView.getTop();
	}

	@Override
	public void computeScroll() {
		// super.computeScroll();
		if (mDragger.continueSettling(true)) {
			invalidate();
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// mDragView = getChildAt(0);
		// mAutoBackView = getChildAt(1);
		// mEdgeTrackerView = getChildAt(2);
	}
}
