package com.sunsg.item.testactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.breadtrip.R;

public class Activity3 extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		((TextView)findViewById(R.id.title)).setText("Activity3");
		findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity3.this,Activity4.class);
				startActivity(intent);
			};
		});
	}
}
