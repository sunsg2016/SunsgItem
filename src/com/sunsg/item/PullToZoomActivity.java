package com.sunsg.item;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.Tools;
import com.sunsg.item.view.PullToZoomListView;
import com.sunsg.item.view.TipsStarLinearLayout;

public class PullToZoomActivity extends Activity{
	private PullToZoomListView mPullToZoomListView;
	private RelativeLayout mContent;
	private ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pull_to_zoom);
		listview = (ListView) findViewById(R.id.listview);
		mPullToZoomListView = (PullToZoomListView) findViewById(R.id.pull_to_zoom_list);
		mContent = (RelativeLayout) findViewById(R.id.rl_content);
		if(mContent.getParent() != null){
			((RelativeLayout)mContent.getParent()).removeView(mContent);
		}
		//title设置
		mPullToZoomListView.setTitleView(null);
		//封面图设置
		mPullToZoomListView.setIvCoverResourceBitmap(R.drawable.userinfo_defalut_cover);
		//content
		mPullToZoomListView.setHeadViewContent(mContent);
		RelativeLayout rl = new RelativeLayout(this);
		rl.setBackgroundColor(0xffffffff);
		
		RelativeLayout content = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		content.setLayoutParams(params);
		rl.addView(content);
		
		Button btn = new Button(this);
		params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Tools.dp2px(this, 600));
		params.setMargins(0, 100, 0, 0);
		btn.setLayoutParams(params);
//		btn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "btn", Toast.LENGTH_SHORT).show();
//				
//			}
//		});
		btn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Logger.e("test2", "MotionEvent.ACTION_DOWN  ");
					break;
				case MotionEvent.ACTION_MOVE:
					Logger.e("test2", "MotionEvent.ACTION_MOVE  ");
					break;
				case MotionEvent.ACTION_UP:
					Logger.e("test2", "MotionEvent.ACTION_UP  ");
//					Toast.makeText(getApplicationContext(), "btn", Toast.LENGTH_SHORT).show();
					break;
				
				}
				
				return false;
			}
		});
		rl.addView(btn);
		final TipsStarLinearLayout stars = new TipsStarLinearLayout(this);
		stars.setItem(5);
		params =new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		stars.setLayoutParams(params);
		
		content.addView(stars);
		
//		stars.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				int action = event.getAction();
//				switch (action) {
//				case MotionEvent.ACTION_DOWN:
//					stars.getCount(event.getX());
//					break;
//				case MotionEvent.ACTION_MOVE:
//					stars.getCount(event.getX());
//					Log.i("test", "moveeeeeeeeeeeeeeeeeeeeeeeee = "+event.getX());
//					break;
//				case MotionEvent.ACTION_OUTSIDE:
//				case MotionEvent.ACTION_CANCEL:
//				case MotionEvent.ACTION_UP:
//					
//					break;
//					
//				}
//				return false;
//			}
//		});
//		
		
//		mPullToZoomListView.setHeadViewContent(rl);
		mPullToZoomListView.setAdapter(new MyAdapter());
		mPullToZoomListView.addFooterView(rl);
		listview.setVisibility(View.GONE);
//		listview.addHeaderView(rl);
//		listview.setAdapter(new MyAdapter());
		
		findViewById(R.id.left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mPullToZoomListView.mCancelClick)
				Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
			}
		});
		
		findViewById(R.id.right).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mPullToZoomListView.mCancelClick)
				Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
			}
		});
		
		RelativeLayout rls = (RelativeLayout) findViewById(R.id.rl_stars);
		final TipsStarLinearLayout stars2 = new TipsStarLinearLayout(this);
		stars2.setItem(5);
		params =new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		stars2.setLayoutParams(params);
		rls.addView(stars2);
		
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
