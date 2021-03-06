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

public class Fragment2 extends BaseFragment{
	//为Fragment加载布局时调用。
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		Log.i("test", "onCreateView2");
		return inflater.inflate(R.layout.fragment2, container,false);
	}
	
	
	//Fragment和Activity建立关联的时候调用。
	@Override  
    public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        Log.i("test", "onAttach2");  
       
    }  
	
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        Log.i("test", "onCreate2");  
    }  
  
	//当Activity中的onCreate方法执行完后调用。
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        Log.i("test", "onActivityCreated2");  
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
        Log.i("test", "onStart2");  
    }  
  
    @Override  
    public void onResume() {  
        super.onResume();  
        Log.i("test", "onResume2");  
    }  
  
    @Override  
    public void onPause() {  
        super.onPause();  
        Log.i("test", "onPause2");  
    }  
  
    @Override  
    public void onStop() {  
        super.onStop();  
        Log.i("test", "onStop2");  
    }  
  
    
    //Fragment中的布局被移除时调用。
    @Override  
    public void onDestroyView() {  
        super.onDestroyView();  
        Log.i("test", "onDestroyView2");  
    }  
    
    //Fragment和Activity解除关联的时候调用。
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        Log.i("test", "onDestroy2");  
    }  
  
    @Override  
    public void onDetach() {  
        super.onDetach();  
        Log.i("test", "onDetach2");  
    }  
}
