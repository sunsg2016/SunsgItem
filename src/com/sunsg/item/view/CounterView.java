package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class CounterView extends View implements View.OnClickListener {

	/** 背景画笔 */
	private Paint mPaintBg;
	/** 文字的画笔 */
	private Paint mPaintText;
	/** 用于计算文字的宽和高 */
	private Rect mBounds;

	/** 背景 */
	private Rect mRectBg;
	/** 计数用 */
	private int mCount = 0;
	
	private String value;

	/** 用于显示文字的宽和高 */
	private Point mPoint;
	
	private int mTextWidth;
	private int mTextHeight;

	public CounterView(Context context) {
		this(context, null);
	}

	public CounterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CounterView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mRectBg = new Rect(0, 0, MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
		FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt(); 
		//计算文字的高
		int baseline = (MeasureSpec.getSize(heightMeasureSpec) - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
		mPoint.x = (MeasureSpec.getSize(widthMeasureSpec) - mTextWidth) / 2;
		mPoint.y = MeasureSpec.getSize(heightMeasureSpec)/2 + mTextHeight/2;
//		mPoint.y = baseline;

	}

	private void init(Context context) {
		mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintBg.setColor(Color.BLUE);
//		mPaintBg.setStyle(Style.STROKE);

		mPoint = new Point();

		mPaintText = new Paint();
		mPaintText.setAntiAlias(true);
		mPaintText.setColor(Color.YELLOW);
		mPaintText.setTextSize(100);
		mPaintText.setTextAlign(Align.CENTER);
		mBounds = new Rect();

		value = String.valueOf(mCount);
		mPaintText.getTextBounds(value, 0, value.length(), mBounds);
		mTextWidth = mBounds.width();
		mTextHeight = mBounds.height();

		setOnClickListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(mRectBg,mPaintBg);
		canvas.drawText(value, mPoint.x, mPoint.y, mPaintText);

	}

	@Override
	public void onClick(View v) {
		mCount++;
		value = String.valueOf(mCount);
		invalidate();
	}

}
