package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Scroller;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.Tools;

public class SwitchButton extends FrameLayout {

	public interface OnItemClickListener {
		public void onItemClick(int position, View view);
	}

	private static final int FIRST_DEGREE = 90;// 度
	private static final int ROTATE_DEGREE_OFFSET = 10; // 度

	private static int MARGIN = 20;// px

	private static final int ANIM_DURATION = 300;// ms

	private static int CHILED_VIEW_WIDTH = 0;

	private Context mContext;

	private Scroller mScroller;

	private OnItemClickListener mOnItemClickListener;

	// private OnClickListener mOnClickListener;

	private Status status = Status.CLOSED;

	private boolean isNeedInit = true;

	public SwitchButton(Context context) {
		this(context, null);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void addImageView(int imageResId) {
		int index = getChildCount();
		Logger.d("test", "addview index = " + index);
		RotateImageView rotateImageView = new RotateImageView(mContext);
		// rotateImageView.setDegree(FIRST_DEGREE + index *
		// ROTATE_DEGREE_OFFSET);
		rotateImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		rotateImageView.setImageResource(imageResId);
		rotateImageView.setTag(index);
		rotateImageView.setBackgroundColor(Color.RED);
		// if (index > 0) {
		// ((View) rotateImageView).setAlpha(0);
		// }
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(CHILED_VIEW_WIDTH, CHILED_VIEW_WIDTH);
		// attachViewToParent(rotateImageView, index, params);
		addView(rotateImageView, index, params);

		rotateImageView.setOnClickListener(mOnClickListener);

		// requestLayout();
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();

			if (status == Status.CLOSING || status == Status.OPENNING) {
				return;
			}

			if (position == 0) {
				toggle();
			}

			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(position, v);
			}
		}
	};

	/**
	 * 获得子view
	 * 
	 * @param position
	 * @return
	 */
	public ImageView getChildView(int position) {
		return (ImageView) findViewWithTag(position);
	}

	private void init(Context context) {
		mContext = context;

		MARGIN = Tools.dp2px(context, 10);

		CHILED_VIEW_WIDTH = Tools.dp2px(context, 44);

		mScroller = new Scroller(mContext, new AccelerateInterpolator());

		addImageView(R.drawable.im_spot_display_more);
		addImageView(R.drawable.im_spot_display_share);
		addImageView(R.drawable.im_spot_display_like);

		setBackgroundColor(Color.GREEN);
	}

	private void toggle() {
		switch (status) {
		case CLOSED:
			status = Status.OPENNING;
			mScroller.startScroll(0, 0, 100, 0, ANIM_DURATION);
			invalidate();
			isNeedInit = true;
			break;
		case OPENED:
			status = Status.CLOSING;
			mScroller.startScroll(0, 0, -100, 0, ANIM_DURATION);
			invalidate();
			isNeedInit = true;
			break;
		default:
			break;
		}
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		// if (true)
		// return;
		Logger.d("test", "computeScroll");
		if (mScroller.computeScrollOffset()) {
			if (status == Status.CLOSED || status == Status.OPENED) {
				return;
			}

			int currX = mScroller.getCurrX();
			float currRate = currX * 1.0f / (mScroller.getFinalX() - mScroller.getStartX());
			Logger.i("test", "currRate " + currRate);
			if (status == Status.CLOSING) {
				currRate = 1 - currRate;
			}

			Logger.i("test", "currRate " + currRate);
			layoutChild(currRate);

			if (currRate == 1 && status == Status.OPENNING) {
				status = Status.OPENED;
			}

			if (currRate == 0 && status == Status.CLOSING) {
				status = Status.CLOSED;
			}
			postInvalidate();
		} else {
			if (isNeedInit) {
				isNeedInit = false;

				if (status == Status.OPENED) {
					layoutChild(1);
				} else if (status == Status.CLOSED) {
					layoutChild(0);
				}

				invalidate();
			}
		}
	}

	private void layoutChild(float currRate) {
//		View vtag = getChildAt(0);
//		Logger.i("test", "gettag = "+vtag.getTag());
		View first = findViewWithTag(0);
		int initRight = first.getRight();
		Logger.i("test", "initRight = "+initRight);
		for (int i = 0; i < getChildCount(); i++) {
			View view = findViewWithTag(i);
			if (view instanceof RotateImageView) {
				RotateImageView imageView = (RotateImageView) view;

				if (i > 0) {
					imageView.setAlpha(currRate);
				}

				int degree = (int) ((FIRST_DEGREE + ROTATE_DEGREE_OFFSET * i) * (1 - currRate));
				imageView.setDegree(degree);

				int offset = (view.getWidth() + MARGIN) * i;
				int right = initRight - (int) (offset * currRate);
				view.layout(right - imageView.getWidth(), imageView.getTop(), right, imageView.getHeight());
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		Logger.d("test", "left = " + left + " right = " + right);
		if (status == Status.CLOSING) {
			mScroller.abortAnimation();
			status = Status.CLOSED;
		} else if (status == Status.OPENNING) {
			mScroller.abortAnimation();
			status = Status.OPENED;
		}

		if (status == Status.OPENED) {

			Logger.i("test", "onened");

			for (int i = getChildCount() - 1; i >= 0; i--) {
				Logger.i("test", "i      ======== =" + i);
				View imageview = findViewWithTag(i);
				int offset = (imageview.getWidth() + MARGIN) * i;
				int r = right - left - offset;
				imageview.layout(r - imageview.getWidth(), 0, r, imageview.getHeight());
				imageview.bringToFront();
				
			}

		} else if (status == Status.CLOSED) {
			Logger.i("test", "closed");
			for (int i = getChildCount() - 1; i >= 0; i--) {
				View imageview = findViewWithTag(i);
				int r = right - left;
				imageview.layout(r - imageview.getWidth(), 0, r, imageview.getHeight());
				imageview.bringToFront();
			}

		}
	}

	/**
	 * 更新状态， 如果状态不一致会有更新
	 * 
	 * @param nextStatus
	 *            OPENED和CLOSED
	 * @param isAnimate
	 */
	public void updateState(Status nextStatus, boolean isAnimate) {
		if (status == Status.OPENED || status == Status.CLOSED) {
			if (isAnimate) {
				if (status != nextStatus) {
					toggle();
				}
			} else {
				this.status = nextStatus;
				if (status == Status.OPENED) {
					layoutChild(1);
				} else if (status == Status.CLOSED) {
					layoutChild(0);
				}
			}
			invalidate();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	public enum Status {
		CLOSED, OPENED, CLOSING, OPENNING;
	}
}
