package com.sunsg.item.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.breadtrip.R;

public class Fragment4 extends BaseFragment{
	//为Fragment加载布局时调用。
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		Log.i("test", "onCreateView4");
		return inflater.inflate(R.layout.fragment4, container,false);
	}
	
	
	//Fragment和Activity建立关联的时候调用。
	@Override  
    public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        Log.i("test", "onAttach4");  
       
    }  
	
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        Log.i("test", "onCreate4");  
    }  
  
	//当Activity中的onCreate方法执行完后调用。
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        Log.i("test", "onActivityCreated4");  
        Button btn = (Button) getActivity().findViewById(R.id.btn_fragment);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String text =  ((TextView)getActivity().findViewById(R.id.text_fragment1)).getText().toString();
				Toast.makeText(getActivity(), "text  = "+text, Toast.LENGTH_SHORT).show();
			}
		});
    }  
  
    @Override  
    public void onStart() {  
        super.onStart();  
        Log.i("test", "onStart4");  
    }  
  
    @Override  
    public void onResume() {  
        super.onResume();  
        Log.i("test", "onResume4");  
    }  
  
    @Override  
    public void onPause() {  
        super.onPause();  
        Log.i("test", "onPause4");  
    }  
  
    @Override  
    public void onStop() {  
        super.onStop();  
        Log.i("test", "onStop4");  
    }  
  
    
    //Fragment中的布局被移除时调用。
    @Override  
    public void onDestroyView() {  
        super.onDestroyView();  
        Log.i("test", "onDestroyView4");  
    }  
    
    //Fragment和Activity解除关联的时候调用。
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        Log.i("test", "onDestroy4");  
    }  
  
    @Override  
    public void onDetach() {  
        super.onDetach();  
        Log.i("test", "onDetach4");  
    }  
}
