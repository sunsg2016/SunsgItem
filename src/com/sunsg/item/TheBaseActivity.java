package com.sunsg.item;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class TheBaseActivity extends Activity{
	public MainApplicaion mTheApplication;
	public Handler mHnadler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTheApplication = (MainApplicaion) getApplication();
		mHnadler = new Handler();
	}
}
