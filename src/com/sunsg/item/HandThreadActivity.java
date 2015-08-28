package com.sunsg.item;

import com.breadtrip.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.Html;
import android.widget.TextView;

public class HandThreadActivity extends Activity {

	private TextView mTvCounter;

	private HandlerThread mCheckMsgThread;
	private Handler mCheckMsgHandler;
	private boolean isUpdateInfo;

	private static final int MSG_UPDATE_INFO = 10;

	// 与UI线程管理的handler
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTvCounter = (TextView) findViewById(R.id.counter);
		initBackThread();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 开始查询
		isUpdateInfo = true;
		mCheckMsgHandler.sendEmptyMessage(MSG_UPDATE_INFO);
	}

	private void initBackThread() {
		mCheckMsgThread = new HandlerThread("check-message-coming");
		mCheckMsgThread.start();
		mCheckMsgHandler = new Handler(mCheckMsgThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				checkForUpdate();
				if (isUpdateInfo) {
					mCheckMsgHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
				}
			}
		};

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 停止查询
		isUpdateInfo = false;
		mCheckMsgHandler.removeMessages(MSG_UPDATE_INFO);

	}

	/**
	 * 模拟从服务器解析数据
	 */
	private void checkForUpdate() {
		try {
			// 模拟耗时
			Thread.sleep(1000);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					String result = "实时更新中，当前大盘指数：<font color='red'>%d</font>";
					result = String.format(result, (int) (Math.random() * 3000 + 1000));
					mTvCounter.setText(Html.fromHtml(result));
				}
			});

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 释放资源
		mCheckMsgThread.quit();
	}
}
