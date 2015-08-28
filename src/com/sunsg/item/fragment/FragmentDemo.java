package com.sunsg.item.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.breadtrip.R;

public class FragmentDemo extends FragmentActivity{
	
	private Button mBtn1;
	private Button mBtn2;
	private Button mBtn3;
	private Button mBtn4;
	
	private Fragment1 mfg1;
	private Fragment2 mfg2;
	private Fragment3 mfg3;
	private Fragment4 mfg4;
	
	 private FragmentManager fragmentManager;  
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_activity_demo);
		fragmentManager =  getSupportFragmentManager();//getFragmentManager();  
		mBtn1 = (Button) findViewById(R.id.btn1);
		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn3 = (Button) findViewById(R.id.btn3);
		mBtn4 = (Button) findViewById(R.id.btn4);
		
		mBtn1.setOnClickListener(onClickListener);
		mBtn2.setOnClickListener(onClickListener);
		mBtn3.setOnClickListener(onClickListener);
		mBtn4.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn1:
				selectionIndex(0);
				break;
			case R.id.btn2:
				selectionIndex(1);
				break;
			case R.id.btn3:
				selectionIndex(2);
				break;
			case R.id.btn4:
				selectionIndex(3);
				break;
			}
		}
	};
	
	/**
	 * 0 第一个 1第二个 2第三个 3第四个
	 * 
	 */
	private void selectionIndex(int index){
		// 开启一个Fragment事务  
        FragmentTransaction transaction = fragmentManager.beginTransaction();  
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况  
        hideFragments(transaction);  
        
        switch (index) {
		case 0:
			if(mfg1 == null){
				mfg1 = new Fragment1();
				transaction.add(R.id.content, mfg1);
			}else{
				transaction.show(mfg1);
			}
			break;

		case 1:
			if(mfg2 == null){
				mfg2 = new Fragment2();
				transaction.add(R.id.content, mfg2);
			}else{
				transaction.show(mfg2);
			}
			break;

			
		case 2:
			if(mfg3 == null){
				mfg3 = new Fragment3();
				transaction.add(R.id.content, mfg3);
			}else{
				transaction.show(mfg3);
			}
			break;

		case 3:
			if(mfg4 == null){
				mfg4 = new Fragment4();
				transaction.add(R.id.content, mfg4);
			}else{
				transaction.show(mfg4);
			}
			break;
		}
        
        transaction.commit(); 
	}
	
	private void hideFragments(FragmentTransaction transaction){
		if(mfg1 != null){
			transaction.hide(mfg1);
		}
		
		if(mfg2 != null){
			transaction.hide(mfg2);
		}
		
		if(mfg3 != null){
			transaction.hide(mfg3);
		}
		
		if(mfg4 != null){
			transaction.hide(mfg4);
		}
	}
}
