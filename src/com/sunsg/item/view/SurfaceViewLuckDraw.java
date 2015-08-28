package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.breadtrip.R;

public class SurfaceViewLuckDraw extends SurfaceViewBase{
	private String[] mStrs = new String[] { "单反相机", "IPAD", "恭喜发财", "IPHONE",  
            "妹子一只", "恭喜发财" }; 
    private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,  
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01 }; 
    
    /** 
     * 与文字对应的图片 
     */  
    private int[] mImgs = new int[] { R.drawable.danfan, R.drawable.ipad,  
            R.drawable.f040, R.drawable.iphone, R.drawable.meizi,  
            R.drawable.f040 };  
  
    /** 
     * 与文字对应图片的bitmap数组 
     */  
    private Bitmap[] mImgsBitmap;  
    /** 
     * 盘块的个数 
     */  
    private int mItemCount = 6;  
    
    /** 
     * 绘制盘块的范围 
     */  
    private RectF mRange = new RectF();  
    /** 
     * 圆的直径 
     */  
    private int mRadius;  
    /** 
     * 绘制盘快的画笔 
     */  
    private Paint mArcPaint;  
  
    /** 
     * 绘制文字的画笔 
     */  
    private Paint mTextPaint;  
  
    
    /** 
     * 滚动的速度 
     */  
    private double mSpeed;  
    private volatile float mStartAngle = 0;  
    /** 
     * 是否点击了停止 
     */  
    private boolean isShouldEnd;  
  
    /** 
     * 控件的中心位置 
     */  
    private int mCenter;  
    /** 
     * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准 
     */  
    private int mPadding;  
  
    /** 
     * 背景图的bitmap 
     */  
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),  
            R.drawable.bg2);  
    /** 
     * 文字的大小 
     */  
    private float mTextSize = TypedValue.applyDimension(  
            TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());  

	public SurfaceViewLuckDraw(Context context) {
		super(context,null);
	}
	
	public SurfaceViewLuckDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		 int width = Math.min(getMeasuredWidth(), getMeasuredHeight());  
	     // 获取圆形的直径  
	     mRadius = width - getPaddingLeft() - getPaddingRight();  
	     // padding值  
	     mPadding = getPaddingLeft();  
	     // 中心点  
	     mCenter = width / 2;  
	     setMeasuredDimension(width, width);  
	}
	
	@Override
	public void sfCreate() {
		 // 初始化绘制圆弧的画笔  
        mArcPaint = new Paint();  
        mArcPaint.setAntiAlias(true);  
        mArcPaint.setDither(true);  
        // 初始化绘制文字的画笔  
        mTextPaint = new Paint();  
        mTextPaint.setColor(0xFFffffff);  
        mTextPaint.setTextSize(mTextSize);  
        // 圆弧的绘制范围  
        mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius  
                + getPaddingLeft(), mRadius + getPaddingLeft());  
  
        // 初始化图片  
        mImgsBitmap = new Bitmap[mItemCount];  
        for (int i = 0; i < mItemCount; i++)  
        {  
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),  
                    mImgs[i]);  
        }  
		
	}
	

	@Override
	public void drawOnChilde(Canvas mCanvas) {
		long start = System.currentTimeMillis();
		// 绘制背景图  
        drawBg(mCanvas);  

        /** 
         * 绘制每个块块，每个块块上的文本，每个块块上的图片 
         */  
        float tmpAngle = mStartAngle;  
        float sweepAngle = (float) (360 / mItemCount);  
        for (int i = 0; i < mItemCount; i++)  
        {  
            // 绘制快快  
            mArcPaint.setColor(mColors[i]);  
            mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true,  
                    mArcPaint);  
            // 绘制文本  
            drawText(mCanvas,tmpAngle, sweepAngle, mStrs[i]);  
            // 绘制Icon  
            drawIcon(mCanvas,tmpAngle, i);  

            tmpAngle += sweepAngle; 
//            break;
        }  

        // 如果mSpeed不等于0，则相当于在滚动  
        mStartAngle += mSpeed;  

        // 点击停止时，设置mSpeed为递减，为0值转盘停止  
        if (isShouldEnd)  
        {  
            mSpeed -= 1;  
        }  
        if (mSpeed <= 0)  
        {  
            mSpeed = 0;  
            isShouldEnd = false;  
        }  
        // 根据当前旋转的mStartAngle计算当前滚动到的区域  
        calInExactArea(mStartAngle);  
        
        long end = System.currentTimeMillis();
		try {
			if (end - start < 50) {
				Thread.sleep(50 - (end - start));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * 根据当前旋转的mStartAngle计算当前滚动到的区域 绘制背景，不重要，完全为了美观 
	 */  
	private void drawBg(Canvas mCanvas)  
	{  
	    mCanvas.drawColor(0xFFFFFFFF);  
	    mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2,  
	            mPadding / 2, getMeasuredWidth() - mPadding / 2,  
	            getMeasuredWidth() - mPadding / 2), null);  
	} 
	
    private void drawText(Canvas mCanvas,float startAngle, float sweepAngle, String string)  
    {  
        Path path = new Path();  
        path.addArc(mRange, startAngle, sweepAngle);  
        float textWidth = mTextPaint.measureText(string);  
        // 利用水平偏移让文字居中  
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);// 水平偏移  
        float vOffset = mRadius / 2 / 6;// 垂直偏移  
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);  
    }  
    
    private void drawIcon(Canvas mCanvas,float startAngle, int i)  
    {  
        // 设置图片的宽度为直径的1/8  
        int imgWidth = mRadius / 8;  
  
        float angle = (float) ((30 + startAngle) * (Math.PI / 180));  
  
        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));  
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));  
  
        // 确定绘制图片的位置  
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth  
                / 2, y + imgWidth / 2);  
  
        mCanvas.drawBitmap(mImgsBitmap[i], null, rect, null);  
  
    }  
    
	/**
	 * 根据当前旋转的mStartAngle计算当前滚动到的区域
	 * 
	 * @param startAngle
	 */
	public void calInExactArea(float startAngle)
	{
		// 让指针从水平向右开始计算
		float rotate = startAngle + 90;
		rotate %= 360.0;
		for (int i = 0; i < mItemCount; i++)
		{
			// 每个的中奖范围
			float from = 360 - (i + 1) * (360 / mItemCount);
			float to = from + 360 - (i) * (360 / mItemCount);

			if ((rotate > from) && (rotate < to))
			{
				Log.d("TAG", mStrs[i]);
				return;
			}
		}
	}
	
	
	
	
	
	/**
	 * 点击开始旋转
	 * 
	 * @param luckyIndex
	 */
	public void luckyStart(int luckyIndex)
	{
		// 每项角度大小
		float angle = (float) (360 / mItemCount);
		// 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
		float from = 270 - (luckyIndex + 1) * angle;
		float to = from + angle;
		// 停下来时旋转的距离
		float targetFrom = 4 * 360 + from;
		/**
		 * <pre>
		 *  (v1 + 0) * (v1+1) / 2 = target ;
		 *  v1*v1 + v1 - 2target = 0 ;
		 *  v1=-1+(1*1 + 8 *1 * target)/2;
		 * </pre>
		 */
		float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
		float targetTo = 4 * 360 + to;
		float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

		mSpeed = (float) (v1 + Math.random() * (v2 - v1));
		isShouldEnd = false;
	}

	public void luckyEnd()
	{
		mStartAngle = 0;
		isShouldEnd = true;
	}

	public boolean isStart()
	{
		return mSpeed != 0;
	}

	public boolean isShouldEnd()
	{
		return isShouldEnd;
	}

}
