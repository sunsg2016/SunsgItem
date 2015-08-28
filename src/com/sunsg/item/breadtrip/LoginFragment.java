package com.sunsg.item.breadtrip;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;

import com.breadtrip.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.sunsg.item.breadtrip.bean.NetAccessToken;
import com.sunsg.item.breadtrip.bean.util.UtilBean;
import com.sunsg.item.http.HttpUtilAnsync;
import com.sunsg.item.util.AbJsonUtil;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.SPUtils;
import com.sunsg.item.util.Tools;

public class LoginFragment extends FragmentActivity{
	private EditText mEtUsername;
	private EditText mEtPasswrod;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.bread_login_fragment_activity);
		mEtUsername = (EditText) findViewById(R.id.ET_username);
		mEtPasswrod = (EditText) findViewById(R.id.ET_password);
		getAccessToken();
	}
	
	public void go(View view){
		Intent intent = new Intent(this,BreadTripFragmentActivity.class);
		startActivity(intent);
	}
	
	public void login(View view){
//		getSplashScreen();
		String umsername = mEtUsername.getText().toString();
		String password = mEtPasswrod.getText().toString();
		if(Tools.isEmpty(umsername)){
			Tools.toast(getApplicationContext(), "用户名不能为空");
			return;
		}
		if(Tools.isEmpty(password)){
			Tools.toast(getApplicationContext(), "密码不能为空");
			return;
		}
		AsyncHttpClient client = HttpUtilAnsync.getInstance(getApplicationContext());
		final String url = "http://api.breadtrip.com/accounts/login/";
		RequestParams parsms = new RequestParams();
		parsms.put("username", umsername);
		parsms.put("password", password);
		client.post(url, parsms,new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if(200 == statusCode){
					syncCookies(url);
//					mEtPasswrod.setText("");
					Logger.e("test3","result = "+ new String(responseBody));
					UtilBean.setUser(getApplicationContext(), new String(responseBody));
					
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Logger.e("test3","result = "+ new String(error.toString()));
				Logger.e("test3","responseBody = "+ new String(responseBody));
				
			}
		});
	}
	
	private void getSplashScreen(){
		final String url= "http://api.breadtrip.com/splash_screen/?size=1280x720";
		AsyncHttpClient client = HttpUtilAnsync.getInstance(getApplicationContext());
		client.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if(200 == statusCode){
					syncCookies(url);
					Logger.e("test4","result = "+ new String(responseBody));
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Logger.e("test4","result = "+ new String(error.toString()));
				Logger.e("test4","responseBody = "+ new String(responseBody));
			}
		});
	}
	
	//获得accesstoken
	private void getAccessToken(){
		final String url = "http://api.breadtrip.com/android/bdchannel/get_access_token/";
		AsyncHttpClient client = HttpUtilAnsync.getInstance(getApplicationContext());
		client.get(url, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if(statusCode == 200){
					syncCookies(url);
					Logger.e("test4","result = "+ new String(responseBody));
					
					SPUtils.setFileName(SPUtils.Bread_Trip);
//					if(Tools.isEmpty((String)(SPUtils.get(getApplicationContext(), "accessToken", new String())))){
						SPUtils.put(getApplicationContext(), "accessToken", new String(responseBody));
//					}
					String json = (String) SPUtils.get(getApplicationContext(), "accessToken", new String());
					NetAccessToken token = (NetAccessToken) AbJsonUtil.fromJson(json, NetAccessToken.class);
					if(token != null){
						Logger.e("test4","json = "+json+" token = "+ token.accessToken +" expiresIn = "+token.expiresIon +" refreshToken = "+token.refreshToken);
						Logger.e("test4","obj to string = "+AbJsonUtil.toJson(token));
					}
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Logger.e("test4","result = "+ new String(error.toString()));
				Logger.e("test4","responseBody = "+ new String(responseBody));
			}
		});
	}
	
	/**
     * 同步cookiers
     * author sunsg 
     * time 2014.11.25
     * @param context
     */
    public  void syncCookies(String url) {
    	Logger.e("test4","urlllllllllllllll =  "+url);
        CookieSyncManager.createInstance(getApplicationContext());
        CookieManager cookieManager = CookieManager.getInstance();
        CookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
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
