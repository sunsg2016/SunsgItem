package com.sunsg.item.threadinterrupt;

import android.util.Log;

public class TestThreadInterrupt extends Thread{
	private boolean mQuite;
	@Override
	public void run() {
		while (true) {
			try {
				sleep(10);
				Log.i("test", "running");
			} catch (InterruptedException e1) {
				if(mQuite){
					Log.i("test", "end");
					return;
				}else{
					continue;
				}
			}
		}
	}
	
	public void quite(){
		this.mQuite = true;
		interrupt();
	}
}
