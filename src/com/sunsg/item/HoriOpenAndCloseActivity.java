package com.sunsg.item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;

public class HoriOpenAndCloseActivity extends Activity{
	private MyCallBack mCallBackTest;
	private Button mBtn;
	private Handler mHander = new Handler();
	private TextView mTvTest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hori_open_and_close);
		mBtn = (Button) findViewById(R.id.btn_test);
		mTvTest = (TextView) findViewById(R.id.tv_test);
		mTvTest.setText("");
		
		mCallBackTest = new MyCallBack();
		mBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newThread();
			}
		});
		
		mHander.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Logger.i("test", "mTvTest.getheight = "+mTvTest.getHeight());
			}
		},3000);
		
	}
	
	
	private void newThread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
//				mBtn.setText("call back");
//				mBtn.setVisibility(View.VISIBLE);
				Message message = Message.obtain();
				mCallBackTest.sendMessgae(message);
				mCallBackTest.postRun(new Runnable() {
					
					@Override
					public void run() {
//						mBtn.setText("call back");
					}
				});
				
			}
		}).start();
	}
	public interface MyCallBackTest{
		void callback(Message message);
	}
	
	  class MyCallBack {
		 private List<Message> mquene;
		private boolean quite = true;
		private Runnable running;
		private BlockingQueue<Message> mArray = new ArrayBlockingQueue<Message>(3);
		public  MyCallBack() {
			mquene = new ArrayList<Message>();
			doWork();
		}
		
		private void sendMessgae(Message text){
			mquene.add(text);
			try {
				mArray.put(text);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		public void doWork(){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (quite) {
						Logger.i("test", "quite");
						try {
							Message message = mArray.take();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(running != null){
							running.run();
						}
					}
				}
			}).start();
			
//				if(mArray.size() >0){
//					Message message = mquene.get(0);
//					if(running != null){
//						running.run();
//					}
//				}
//			}
		}
		
		public void quite(){
			this.quite = false;
		}

		
		public void callback(Message message){
			
		}
		
		public void postRun(Runnable run){
			running = run;
		}
		
	}
}
