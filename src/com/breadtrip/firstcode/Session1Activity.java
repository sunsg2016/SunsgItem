package com.breadtrip.firstcode;

import com.breadtrip.R;
import com.sunsg.item.util.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * @title sesson1
 * @destription Session1
 * @author sunsg
 * @date 2015.7.18 8:40
 */
public class Session1Activity extends Activity implements View.OnClickListener {
	private Button mBtnNormal;
	private Button mBtnDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session1);
		mBtnNormal = (Button) findViewById(R.id.btn_normal);
		mBtnDialog = (Button) findViewById(R.id.btn_dialog);
		mBtnNormal.setOnClickListener(this);
		mBtnDialog.setOnClickListener(this);
		this.getClass().getSimpleName();
		Logger.d("firstcode", this.getClass().getSimpleName() +" onCreate()");
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
		Logger.d("firstcode", this.getClass().getSimpleName() +" onRestart()");
		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.session1, menu);
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch (id) {
		case R.id.btn_normal:
			intent.setClass(this, Session1_NormalActivity.class);
			break;
		case R.id.btn_dialog:
			intent.setClass(this, Session1_DialogActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
}
