package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.breadtrip.R;

public class OverDrawView extends View{
	private Bitmap[] mCards = new Bitmap[3];
	private int[] mImgId = new int[]{R.drawable.featured_photo, R.drawable.featured_photo, R.drawable.featured_photo};
	
	private Paint[] paints = new Paint[3];
	private int[] paintsId = new int[]{Color.RED,Color.GREEN,Color.BLUE};
	
	public OverDrawView(Context context) {
		super(context);
		init(context);
	}

	public OverDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OverDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	
	private void init(Context context){
		Bitmap bit = null;
		Paint paint = null;
		for (int i = 0; i < mCards.length; i++) {
			bit = BitmapFactory.decodeResource(getResources(), R.drawable.featured_photo);
			mCards[i] = bit;
			
			paint = new Paint();
			paint.setColor(paintsId[i]);
			paints[i] = paint;
			
		}
		setBackgroundColor(0xffd5d5d5);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		canvas.save();
//        canvas.translate(0, 120);
//        for (int i = 0; i < mCards.length; i++) {
//        	canvas.translate(120, 0);
//        	canvas.save();
//        	if(i < mCards.length - 1 ){
//        		canvas.clipRect(0, 0, 120, mCards[i].getHeight());
//        	}
//            canvas.drawBitmap(mCards[i], 0, 0, null);
//            canvas.restore();
////            canvas.drawRect(0, 0, mCards[i].getWidth(), mCards[i].getHeight(), paints[i]);
//		}
//        canvas.restore();
		for (int i = 0; i < mCards.length; i++) {
			canvas.save();
			if(i < mCards.length -1){
				canvas.clipRect(120*i, 0, 120, mCards[i].getHeight());
			}
//			canvas.drawBitmap(mCards[i], 120*i, 0, null);
			canvas.drawRect(120*i, 0, mCards[i].getWidth(), mCards[i].getHeight(), paints[i]);
			canvas.restore();
//			canvas.drawRect(120*i, 0, mCards[i].getWidth(), mCards[i].getHeight(), paints[i]);
		}

	}

}
