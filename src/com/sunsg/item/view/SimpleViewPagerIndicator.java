package com.sunsg.item.view;

/**
 * 
 * 根据viewpage滑动而设计
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleViewPagerIndicator extends LinearLayout {

	private static final int COLOR_TEXT_NORMAL = 0xFF000000;
	private static final int COLOR_INDICATOR_COLOR = Color.GREEN;

	private String[] mTitles;
	private int mTabCount;
	private int mIndicatorColor = COLOR_INDICATOR_COLOR;
	private float mTranslationX;
	private Paint mPaint = new Paint();
	private int mTabWidth;
	private View.OnClickListener onItemClickListener;

	public SimpleViewPagerIndicator(Context context) {
		this(context, null);
	}

	public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setColor(mIndicatorColor);
		mPaint.setStrokeWidth(9.0F);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTabWidth = w / mTabCount;
	}

	public void setTitles(String[] titles) {
		mTitles = titles;
		mTabCount = titles.length;
		generateTitleView();

	}

	public void setIndicatorColor(int indicatorColor) {
		this.mIndicatorColor = indicatorColor;
	}
	
	public void setOnItemClickListener(View.OnClickListener onItemClickListener){
		this.onItemClickListener = onItemClickListener;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		canvas.save();
		canvas.translate(mTranslationX, getHeight());
		canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
		canvas.restore();
	}

	public void scroll(int position, float offset) {
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */
		Log.i("test", "getwidth = " + getWidth() + "getheight = " + getHeight());
		mTranslationX = getWidth() / mTabCount * (position + offset);
		invalidate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	private void generateTitleView() {
		if (getChildCount() > 0)
			this.removeAllViews();
		int count = mTitles.length;

		setWeightSum(count);
		for (int i = 0; i < count; i++) {
			TextView tv = new TextView(getContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
			lp.weight = 1;
			tv.setGravity(Gravity.CENTER);
			tv.setTextColor(COLOR_TEXT_NORMAL);
			tv.setText(mTitles[i]);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			tv.setLayoutParams(lp);
			if(this.onItemClickListener != null){
				tv.setTag(i);
				tv.setOnClickListener(onItemClickListener);
			}
			
			addView(tv);
		}
	}

}
