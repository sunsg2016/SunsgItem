package com.breadtrip.firstcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;

public class Session1_NormalActivity extends Activity {
	private Button mBtnSingle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session1_normal);
		mBtnSingle = (Button) findViewById(R.id.btn_sing_top);
		mBtnSingle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Session1_NormalActivity.this,Session1_DialogActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onStart() {
		Logger.d("firstcode", this.getClass().getSimpleName() +" onStart()");
		super.onStart();
	}
	
	
	@Override
	protected void onResume() {
		Logger.d("firstcode", this.getClass().getSimpleName() +" onResume()");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Logger.d("firstcode", this.getClass().getSimpleName() +" onPause()");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Logger.d("firstcode", this.getClass().getSimpleName() +" onStop()");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Logger.d("firstcode", this.getClass().getSimpleName() +" onDestroy()");
		super.onDestroy();
	}
	
	@Override
	protected void onRestart() {
		Logger.d("firstcode", this.getClass().getSimpleName() +" onStart()");
		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.session1__normal, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
