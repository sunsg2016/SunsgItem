package com.sunsg.item.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.breadtrip.R;

public class TestFragmentActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_text_activity);
		Fragment1 fragment1 = new Fragment1();
//		getFragmentManager().beginTransaction().add(fragment, tag);
//		getFragmentManager().beginTransaction().replace(R.id.layout1,fragment1).commit();
		Fragment2 fragment2 = new Fragment2();
//		getFragmentManager().beginTransaction().replace(R.id.layout2, fragment2).commit();
	}
}
