package com.sunsg.item.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;

import com.breadtrip.R;

public class BitmapShaderView extends SurfaceViewBase{
	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private Paint mPaint;
	private int width;
	private int height;
	private int mSpeed;
	private int x;
	public BitmapShaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird_floor_bg2);
		mBitmapShader = new BitmapShader(mBitmap, TileMode.REPEAT, TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);  
        mPaint.setDither(true);
        mPaint.setShader(mBitmapShader);
        mSpeed = 4;
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
	}
	

	@Override
	public void drawOnChilde(Canvas mCanvas) {
		
		mCanvas.save(Canvas.MATRIX_SAVE_FLAG);
		mCanvas.translate(x, 0);
		mCanvas.drawRect(x, 0, width - x , height, mPaint);
		mCanvas.restore();
//		Log.i("test", "follllllll  x = "+x);
		x = x - mSpeed;
		if(-x > width){
			x = x%width;
		}
	}

	@Override
	public void sfCreate() {
		
	}
}
