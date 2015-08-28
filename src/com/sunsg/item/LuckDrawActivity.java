package com.sunsg.item;

import com.breadtrip.R;
import com.sunsg.item.view.BitmapShaderView;
import com.sunsg.item.view.OverDrawView;
import com.sunsg.item.view.SurfaceViewLuckDraw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class LuckDrawActivity extends Activity{
	private SurfaceViewLuckDraw mSurfaceViewLuckDraw;
	private ImageButton mStartBtn;
	private Button mLuckDraw;
	private Button mBird;
	private RelativeLayout mRlLuckDraw;
	private RelativeLayout mRlBird;
	
	private BitmapShaderView mBitmapShaderView;
	private RelativeLayout mRlOther;
	
	private RelativeLayout mRlOverDraw;
	private OverDrawView mOverDrawView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luck_draw);
		mSurfaceViewLuckDraw = (SurfaceViewLuckDraw) findViewById(R.id.luckdraw);
		mStartBtn = (ImageButton) findViewById(R.id.Im_start);
		
		mLuckDraw = (Button) findViewById(R.id.btn_luck_draw);
		mBird = (Button) findViewById(R.id.btn_bird);
		
		mRlLuckDraw = (RelativeLayout) findViewById(R.id.rl_luckDraw);
		mRlBird = (RelativeLayout) findViewById(R.id.rl_bird);
		
		mBitmapShaderView = (BitmapShaderView) findViewById(R.id.flooer);
		mRlOther =  (RelativeLayout) findViewById(R.id.rl_other);
		
		mRlOverDraw = (RelativeLayout) findViewById(R.id.rl_overDraw);
		mOverDrawView = (OverDrawView) findViewById(R.id.overDrawView);
	}
	
	public void bird(View view){
		mRlBird.setVisibility(View.VISIBLE);
		mRlLuckDraw.setVisibility(View.GONE);
		mSurfaceViewLuckDraw.setVisibility(View.GONE);
		mRlOther.setVisibility(View.GONE);
	}
	
	public void luckDraw(View view){
		mRlBird.setVisibility(View.GONE);
		mRlLuckDraw.setVisibility(View.VISIBLE);
		mSurfaceViewLuckDraw.setVisibility(View.VISIBLE);
		mRlOther.setVisibility(View.GONE);
		mRlOverDraw.setVisibility(View.GONE);
	}
	public void flooer(View view){
		mRlBird.setVisibility(View.GONE);
		mRlLuckDraw.setVisibility(View.GONE);
		mSurfaceViewLuckDraw.setVisibility(View.GONE);
		mRlOverDraw.setVisibility(View.GONE);
		mRlOther.setVisibility(View.VISIBLE);
	}
	
	public void overDraw(View view){
		mRlOverDraw.setVisibility(View.VISIBLE);
		
		mRlBird.setVisibility(View.GONE);
		mRlLuckDraw.setVisibility(View.GONE);
		mSurfaceViewLuckDraw.setVisibility(View.GONE);
		mRlOther.setVisibility(View.GONE);
		
	}
	
	
	
	
	
	public void start(View view){
		{
			if (!mSurfaceViewLuckDraw.isStart())
			{
				mStartBtn.setImageResource(R.drawable.stop);
				mSurfaceViewLuckDraw.luckyStart(1);
			} else
			{
				if (!mSurfaceViewLuckDraw.isShouldEnd())

				{
					mStartBtn.setImageResource(R.drawable.start);
					mSurfaceViewLuckDraw.luckyEnd();
				}
			}
		}
	}
}
