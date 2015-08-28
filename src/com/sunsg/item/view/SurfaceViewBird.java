package com.sunsg.item.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.breadtrip.R;
import com.sunsg.item.bird.Bird;
import com.sunsg.item.bird.Floor;
import com.sunsg.item.bird.Pipe;
import com.sunsg.item.util.Tools;

public class SurfaceViewBird extends SurfaceViewBase{
	 /** 
     * 当前View的尺寸 
     */  
    private int mWidth;  
    private int mHeight;  
    private RectF mGamePanelRect = new RectF();  
    
    
    
    /** 
     * 背景 
     */  
    private Bitmap mBg; 
    
    /** 
     * *********鸟相关********************** 
     */  
    private Bird mBird;  
    private Bitmap mBirdBitmap;  
  
    /** 
     * 地板 
     */  
    private Paint mPaint; 
    private Floor mFloor;  
    private Bitmap mFloorBg;  
    private int mSpeed;  
    
    
    
    
    /** 
     * *********管道相关********************** 
     */  
    /** 
     * 管道 
     */  
    private Bitmap mPipeTop;  
    private Bitmap mPipeBottom;  
    private RectF mPipeRect;  
    private int mPipeWidth;  
    /** 
     * 管道的宽度 60dp 
     */  
    private static final int PIPE_WIDTH = 60;  
  
    private List<Pipe> mPipes = new ArrayList<Pipe>();  
    
    
    
    
    
    /** 
     * 分数 
     */  
    private final int[] mNums = new int[] { R.drawable.bird_num0, R.drawable.bird_num1,  
            R.drawable.bird_num2, R.drawable.bird_num3, R.drawable.bird_num4, R.drawable.bird_num5,  
            R.drawable.bird_num6, R.drawable.bird_num7, R.drawable.bird_num8, R.drawable.bird_num9 };  
    private Bitmap[] mNumBitmap;  
    private int mGrade = 100;  
    /** 
     * 单个数字的高度的1/15 
     */  
    private static final float RADIO_SINGLE_NUM_HEIGHT = 1 / 15f;  
    /** 
     * 单个数字的宽度 
     */  
    private int mSingleGradeWidth;  
    /** 
     * 单个数字的高度 
     */  
    private int mSingleGradeHeight;  
    /** 
     * 单个数字的范围 
     */  
    private RectF mSingleNumRectF;  
    
    
	public SurfaceViewBird(Context context) {
		super(context);
		init(context);
	}

	public SurfaceViewBird(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		mBg = loadImageByResId(R.drawable.bird_bg1);
		mBirdBitmap = loadImageByResId(R.drawable.bird);
		//  地板
		mFloorBg = loadImageByResId(R.drawable.bird_floor_bg2); 
		
		/**
		 * 地板
		 */
		mPaint = new Paint();  
        mPaint.setAntiAlias(true);  
        mPaint.setDither(true);  
		// 初始化速度 
        mSpeed = Tools.dp2px(getContext(), 2); 
        
        
        
        
        
        //管道
        mPipeWidth = Tools.dp2px(getContext(), PIPE_WIDTH);
        mPipeTop = loadImageByResId(R.drawable.bird_pipe_top);  
        mPipeBottom = loadImageByResId(R.drawable.bird_pipe_down); 
        
        //分数
        mNumBitmap = new Bitmap[mNums.length];  
        for (int i = 0; i < mNumBitmap.length; i++)  
        {  
            mNumBitmap[i] = loadImageByResId(mNums[i]);  
        }  
	}

	@Override
	public void drawOnChilde(Canvas canvas) {
		drawBg(canvas);
		drawBird(canvas);
		drawPipes(canvas);
		drawFloor(canvas);
		drawGrades(canvas);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;  
        mHeight = h;  
        mGamePanelRect.set(0, 0, w, h); 
        
        // 初始化mBird  
        mBird = new Bird(getContext(), mWidth, mHeight, mBirdBitmap);
        
        // 初始化地板  
        mFloor = new Floor(mWidth, mHeight, mFloorBg); 
        
        // 初始化管道范围  
        mPipeRect = new RectF(0, 0, mPipeWidth, mHeight);  
        Pipe pipe = new Pipe(getContext(), w, h, mPipeTop, mPipeBottom);  
        mPipes.add(pipe);  
        
		// 初始化分数
		mSingleGradeHeight = (int) (h * RADIO_SINGLE_NUM_HEIGHT);
		mSingleGradeWidth = (int) (mSingleGradeHeight * 1.0f
				/ mNumBitmap[0].getHeight() * mNumBitmap[0].getWidth());
		mSingleNumRectF = new RectF(0, 0, mSingleGradeWidth, mSingleGradeHeight);  
	}

	@Override
	public void sfCreate() {
		
	}
	
	private Bitmap loadImageByResId(int resid){
		return BitmapFactory.decodeResource(getResources(), resid);
	}
	
	/**
	 * 绘制背景
	 * @param canvas
	 */
	private void drawBg(Canvas canvas){
		canvas.drawBitmap(mBg, null, mGamePanelRect, null);
	}
	
	/**
	 * 绘制鸟
	 */
	private void drawBird(Canvas canvas){
		mBird.draw(canvas);
	}
	
	/** 
     * 绘制管道 
     */  
    private void drawPipes(Canvas canvas)  
    {  
        for (Pipe pipe : mPipes)  
        {  
            pipe.setX(pipe.getX() - mSpeed);  
            pipe.draw(canvas, mPipeRect);  
        }  
    }  
	
    /**
     * 绘制地板
     * @param canvas
     */
	private void drawFloor(Canvas canvas) {
		mFloor.draw(canvas, mPaint);
		// 更新我们地板绘制的x坐标  
        mFloor.setX(mFloor.getX() - mSpeed);  
	}
	
	/**
	 * 绘制分数
	 */
	private void drawGrades(Canvas canvas) {
		String grade = mGrade + "";
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.translate(mWidth / 2 - grade.length() * mSingleGradeWidth / 2,
				1f / 8 * mHeight);
		// draw single num one by one
		for (int i = 0; i < grade.length(); i++) {
			String numStr = grade.substring(i, i + 1);
			int num = Integer.valueOf(numStr);
			canvas.drawBitmap(mNumBitmap[num], null, mSingleNumRectF, null);
			canvas.translate(mSingleGradeWidth, 0);
		}
		canvas.restore();

	}
	
	
	

}
