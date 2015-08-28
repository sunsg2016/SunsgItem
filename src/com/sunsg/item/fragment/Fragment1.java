package com.sunsg.item.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breadtrip.R;

public class Fragment1 extends BaseFragment{
	private TextView tv;
	private Activity activity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("test", "onCreateView1");  
		View view = inflater.inflate(R.layout.fragment1, container,false);
		tv = (TextView) view.findViewById(R.id.text_fragment1);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,WXFragmentActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}
	
	@Override  
    public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        Log.i("test", "onAttach1");  
    }  
	
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        activity = getActivity();
        Log.i("test", "onCreate1");  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        Log.i("test", "onActivityCreated1");  
    }  
  
    @Override  
    public void onStart() {  
        super.onStart();  
        Log.i("test", "onStart1");  
    }  
  
    @Override  
    public void onResume() {  
        super.onResume();  
        Log.i("test", "onResume1");  
    }  
  
    @Override  
    public void onPause() {  
        super.onPause();  
        Log.i("test", "onPause1");  
    }  
  
    @Override  
    public void onStop() {  
        super.onStop();  
        Log.i("test", "onStop1");  
    }  
  
    @Override  
    public void onDestroyView() {  
        super.onDestroyView();  
        Log.i("test", "onDestroyView1");  
    }  
  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        Log.i("test", "onDestroy1");  
    }  
  
    @Override  
    public void onDetach() {  
        super.onDetach();  
        Log.i("test", "onDetach1");  
    } 
}
