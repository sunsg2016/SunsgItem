package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


abstract class SurfaceViewBase extends SurfaceView implements Callback,Runnable{
	private SurfaceHolder mHolder;
	private Canvas mCanvas;
	private Thread mThread;
	private boolean isRunning;
	
	
	public SurfaceViewBase(Context context){
		this(context, null);  
	}
	
	public SurfaceViewBase(Context context,AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		// setZOrderOnTop(true);// 设置画布 背景透明  
        // mHolder.setFormat(PixelFormat.TRANSLUCENT);  
		
		 //设置可获得焦点  
        setFocusable(true);  
        setFocusableInTouchMode(true);  
        //设置常亮  
        this.setKeepScreenOn(true);  
	}
	@Override
	public void run() {
		draw();
	}
	
	
	
	private void draw() {
		while (isRunning) {
			try {
				// 获得canvas home或back键 canvas会为空
				mCanvas = mHolder.lockCanvas();
				if (mCanvas != null) {
					// 画图
					drawOnChilde(mCanvas);
				}
			} catch (Exception e) {

			} finally {
				if (mCanvas != null)
					// 解锁画布提交画好的图像
					mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}
	
	public abstract void drawOnChilde(Canvas mCanvas);
	public abstract void sfCreate();

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//在这里面初始化 画笔什么的
		sfCreate();
		//启动线程
		isRunning = true;
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//home或back键回调用此方法
		// 通知关闭线程  
        isRunning = false;  
	}
	

}
