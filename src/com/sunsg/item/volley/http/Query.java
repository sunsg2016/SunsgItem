package com.sunsg.item.volley.http;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import android.content.Context;

import com.android.volley.Request.Method;

public class Query {
	public static final String DEBUG_URL = "http://api.m.kuxun.cn/passengeroperation/android/4/baidu_chunhua_market%7C5.1.0%7C4.4.2/";
	public static final String RELEASE_URL = "http://apitest.dev.m.kuxun.cn/getHotelWithCLK/android/4/baidu_chunhua_market%7C3.0.0%7C4.2.2/";
	public static final String rootUrl = DEBUG_URL;
	
	public static  final int GET = Method.GET;
	public static  final int POST = Method.POST; 
	private int method;
	private Context context; 
	private String action;
	private String url;
	
	private Type type;
	
	public Query(Context context,String action){
		this.context = context;
		this.action = action;
		url = "";
	}
	
	public Query(Context context){
		this.context = context;
		url = "";
	}
	
	public void setType(Type type){
		this.type = type;
	}
	
	public Type getType(){
		return type;
	}
	
	public void buildUrl(Map<String, String> getParams){
		StringBuffer buffer = new StringBuffer(url);
		if(getParams != null){
			Iterator<String> ite = getParams.keySet().iterator();
			String key = ite.next();
			String value;
			try {
				value = URLEncoder.encode(getParams.get(key), HTTP.UTF_8);
				//添加第一个参数
				if(ite.hasNext()) buffer.append("?").append(key).append("=").append(value);
				//添加第一个以后的参数
				while (ite.hasNext()) {
					key = ite.next();
					value = URLEncoder.encode(getParams.get(key), HTTP.UTF_8);
					buffer.append("&").append(key).append("=").append(value);
				}
				this.url = buffer.toString();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getUrl(){
		return url = rootUrl + url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public void setMethod(int method){
		this.method = method;
	}
	
	public int getMethod(){
		return this.method;
	}
	
	public void setAction(String action){
		this.action = action;
	}
	
	public String getAction(){
		return action;
	}
	
}
