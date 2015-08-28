package com.sunsg.item.http;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.sunsg.item.bean.Trip;
import com.sunsg.item.util.AbJsonUtil;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.Tools;

public class HttpUtilAnsync {
	private static AsyncHttpClient client;
	private static Context contexts;
	private static HttpUtilAnsync mHttpUtilAnsync;
	
	
	public static AsyncHttpClient getInstance(Context context){
		contexts = context;
		if(client == null){
			client = new AsyncHttpClient();
			client.setUserAgent(Tools.buildUserAgentString(context));
			client.setCookieStore(new PersistentCookieStore(context));
		}
		return client;
	}
	
	public static HttpUtilAnsync getInstances(Context context){
		contexts = context;
		if(client == null){
			client = new AsyncHttpClient();
			client.setUserAgent(Tools.buildUserAgentString(context));
			client.setCookieStore(new PersistentCookieStore(context));
		}
		if(mHttpUtilAnsync == null){
			mHttpUtilAnsync = new HttpUtilAnsync();
		}
		return mHttpUtilAnsync;
	}
	
	public  CookieStore getCookieStore(Context context){
		if(client == null)getInstance(context);
		return ((DefaultHttpClient)client.getHttpClient()).getCookieStore();
	}
	
	public   <T> void getData(final Object obj,final String url,final ResponseListener<T> listener ){
		client.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if(200 == statusCode){
					syncCookies(url);
					
					String json = new String(responseBody);
					Logger.e("test","url = "+url+" result = "+ json);
					@SuppressWarnings("unchecked")
					Trip t = (Trip) AbJsonUtil.fromJson(json, Trip.class);
					Logger.e("test", "t = "+t);
//					listener.onSuccess(t, url);
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				listener.onFailed(statusCode, new String(responseBody));
//				Logger.e("test4","result = "+ new String(error.toString()));
//				Logger.e("test4","responseBody = "+ new String(responseBody));
			}
		});
	}
	
	public interface ResponseListener<T>{
		void onSuccess(T t,String url);
		void onFailed(int errorCode,String errorMessage);
	}
	
	/**
     * 同步cookiers
     * author sunsg 
     * time 2014.11.25
     * @param context
     */
    public   void syncCookies(String url) {
    	Logger.e("test4","urlllllllllllllll =  "+url);
        CookieSyncManager.createInstance(contexts);
        CookieManager cookieManager = CookieManager.getInstance();
        CookieStore cookieStore = new PersistentCookieStore(contexts);
//        CookieStore cookieStore = HttpUtilAnsync.getCookieStore(getApplicationContext());
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = cookieStore.getCookies();
        for(int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
          	String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
            Logger.e("test4", "syncCookies = " + cookieString);
//            cookieManager.setCookie("breadtrip.com", cookieString);
        }
//        CookieSyncManager.getInstance().sync();
    }
	
}
