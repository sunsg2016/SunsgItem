package com.sunsg.item.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import com.breadtrip.R;
import com.sunsg.item.util.Tools;

public class TuoYuanMei extends View{
	private  BitmapShader bitmapShader = null;  
    private Bitmap bitmap = null;  
    private Paint paint = null;  
    private ShapeDrawable shapeDrawable = null;  
    private int bitmapWidth  = 0;  
    private int bitmapHeight = 0; 
    private int width;
    private int heitht;
	public TuoYuanMei(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		//得到图像  
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mei1);    
        bitmapWidth = bitmap.getWidth();  
        bitmapHeight = bitmap.getHeight();  
        //构造渲染器BitmapShader  
        bitmapShader = new BitmapShader(bitmap,TileMode.MIRROR,TileMode.REPEAT); 
        
        width = Tools.dp2px(context, 400);
        heitht = width * bitmapHeight /bitmapWidth;
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//将图片裁剪为椭圆形    
        //构建ShapeDrawable对象并定义形状为椭圆    
        shapeDrawable = new ShapeDrawable(new OvalShape());  
        //得到画笔并设置渲染器  
        shapeDrawable.getPaint().setShader(bitmapShader);  
        //设置显示区域  
        canvas.save();
        canvas.translate(-100, 0);
        shapeDrawable.setBounds(-100,0,width -400,heitht -10); 
        //绘制shapeDrawable  
        shapeDrawable.draw(canvas);  
        canvas.restore();
	}
	
}
