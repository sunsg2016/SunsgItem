package com.sunsg.item.view;

import com.sunsg.item.util.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class RotateImageView extends ImageView {

	private int mCurrentDegree = 0; // [0, 359]

	public RotateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RotateImageView(Context context) {
		super(context);
	}

	public void setDegree(int degree) {
		// make sure in the range of [0, 359]
		degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
		if (degree == mCurrentDegree)
			return;
		mCurrentDegree = degree;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null)
			return;

		Rect bounds = drawable.getBounds();
		int w = bounds.right - bounds.left;
		int h = bounds.bottom - bounds.top;

		if (w == 0 || h == 0)
			return; // nothing to draw

		int left = getPaddingLeft();
		int top = getPaddingTop();
		int right = getPaddingRight();
		int bottom = getPaddingBottom();
		int width = getWidth() - left - right;
		int height = getHeight() - top - bottom;

		int saveCount = canvas.getSaveCount();
		canvas.translate(left + width / 2, top + height / 2);
		canvas.rotate(-mCurrentDegree);
		canvas.translate(-w / 2, -h / 2);
		drawable.draw(canvas);
		canvas.restoreToCount(saveCount);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Logger.i("test", "imageview ontoucheventllllllllllllllll");
		return super.onTouchEvent(event);
	}

}
