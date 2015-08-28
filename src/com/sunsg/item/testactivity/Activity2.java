package com.sunsg.item.testactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.breadtrip.R;

public class Activity2 extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		((TextView)findViewById(R.id.title)).setText("Activity2");
		findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity2.this,Activity3.class);
				startActivity(intent);
			};
		});
		Log.i("test","onCreate "+this.getClass().getName());
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.i("test","onNewIntent  "+this.getClass().getName());
		 if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {  
			 finish();  
		 }  
	}
	
}
