package com.sunsg.item.breadtrip.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breadtrip.R;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sunsg.item.breadtrip.bean.TuiJian;
import com.sunsg.item.breadtrip.bean.TuiJian2;
import com.sunsg.item.http.HttpUtilAnsync;
import com.sunsg.item.util.AbJsonUtil;
import com.sunsg.item.util.Logger;

public class BaseFragment extends Fragment{
	private String title;
	private AsyncHttpClient client;
	private boolean isLoadData;
	private boolean loadDataSuccess;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = HttpUtilAnsync.getInstance(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tuijian, container, false);
		((TextView)view.findViewById(R.id.text_fragment1)).setText(title);
		if(isLoadData){
			loadData();
		}
		return view;
	}
	
	public void loadData(){
		 if(client != null){
			 String url = "http://api.breadtrip.com/featured/";
			 client.get(url, new AsyncHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					if(200 == statusCode){
						
						String result = new String(responseBody);
						Logger.e("test", "result =="+result);
						TuiJian tujian = (TuiJian) AbJsonUtil.fromJson(result,TuiJian.class);
						
						Logger.e("test", "tuijian =="+AbJsonUtil.toJson(tujian));
						try {
							JSONObject obj = new JSONObject(result);
							JSONArray array = obj.optJSONArray("elements");
							String re = array.optJSONObject(0).toString();
							Logger.e("test", "elements re = "+re);
							List<TuiJian2> tujian2 = (List<TuiJian2>) AbJsonUtil.fromJson(re, new TypeToken<ArrayList<TuiJian2>>(){});
							Logger.e("test","tuijian2 = "+AbJsonUtil.toJson(tujian2));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					
				}
			});
		 }
	}
	
	public static BaseFragment getInstance(String title,boolean isLoadData){
		BaseFragment fr = new BaseFragment();
		fr.title = title;
		fr.isLoadData = isLoadData;
		return fr;
	}
}
