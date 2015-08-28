package com.sunsg.loopj;

import java.io.File;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.breadtrip.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.sunsg.item.util.Logger;

public class LoopjFragmentActivity extends FragmentActivity{
	AsyncHttpClient client;
	ImageView im;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_loopj);
		// 创建异步请求的客户端对象  
		client = new AsyncHttpClient(); 
		im = (ImageView) findViewById(R.id.im_bitmap);
		
	}
	
	//AsyncHttpClient post
	public void post(View view){
		loginByAsyncHttpClientPost();
	}
	//AsyncHttpClient get
	public void get(View view){
		asyncHttpClientGet();
	}
	
	//AsynHttpClient getBitmap
	public void getBitmap(View view){
//		asyncHttpClientGetBitmap();
		asyncHttpClientGetBitmapToSdcard();
	}
	
	/**
     * 同步cookiers
     * author sunsg 
     * time 2014.11.25
     * @param context
     */
    public  void syncCookies() {
//        CookieSyncManager.createInstance(getApplicationContext());
//        CookieManager cookieManager = CookieManager.getInstance();
//        CookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
//        cookieManager.setAcceptCookie(true);
//        List<Cookie> cookies = cookieStore.getCookies();
//        for(int i = 0; i < cookies.size(); i++) {
//            Cookie cookie = cookies.get(i);
//          	String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
//            Logger.e("debug", "syncCookies = " + cookieString);
//            cookieManager.setCookie("breadtrip.com", cookieString);
//        }
//        CookieSyncManager.getInstance().sync();
    }
    
    /**
     * 采用AsyncHttpClient的Get方式进行实现   byte[]
     */
    
    private void asyncHttpClientGet(){
    	 String urlString = "http://api.breadtrip.com/featured/";
         client.get(urlString, new AsyncHttpResponseHandler() {
            
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == 200) { 
					for (int i = 0; i < headers.length; i++) {
						Logger.e("test", " header "+i +" = "+headers[i]);
					}
					Logger.e("test", "responsebody = "+new String(responseBody));
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Logger.e("test", "get faulure"+error.toString());
			};
         });
    }
    
	
	/** 
     * 采用AsyncHttpClient的Post方式进行实现 
     */  
    public void loginByAsyncHttpClientPost() {  
        String url = "http://api.breadtrip.com/accounts/login/"; // 定义请求的地址  
        // 创建请求参数的封装的对象  
        RequestParams params = new RequestParams();  
        params.put("username", "Sunsg"); // 设置请求的参数名和参数值  
        params.put("password", "123456");// 设置请求的参数名和参数  
        client.setUserAgent("BreadTrip/android/4.2.0/zh (android OS4.4.2) htc_m8tl Map/AutoNavi/v1.4.2 (HTC M8t,htc)");
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        //添加自己的cookiers
        BasicClientCookie newCookie = new BasicClientCookie("domain", ".breadtrip.com");//name = value
//        newCookie.setVersion(1);
//        newCookie.setDomain(".breadtrip.com");
//        newCookie.setPath("/");
        myCookieStore.addCookie(newCookie);
        // 执行post方法  
        client.post(url, params, new AsyncHttpResponseHandler() {  
//           
           /** 
           * 成功处理的方法 
           * statusCode:响应的状态码; headers:相应的头信息 比如 响应的时间，响应的服务器 ; 
           * responseBody:响应内容的字节 
           */  
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				if (statusCode == 200) { 
					for (int i = 0; i < headers.length; i++) {
						Logger.e("test", " header "+i +" = "+headers[i]);
					}
					
					Logger.e("test", "responsebody = "+new String(responseBody));
					syncCookies();
				}
			}
		   /** 
           * 失败处理的方法 
           * error：响应失败的错误信息封装到这个异常对象中 
           */  
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				 error.printStackTrace();// 把错误信息打印出轨迹来  
				 Logger.e("test","error = "+error.toString());
			}  
        });  
    }  
    
    /**
     * 
     * 采用AsyncHttpClient的get方式获取图片
     */
    private void asyncHttpClientGetBitmap(){
    	String url = "http://f.hiphotos.baidu.com/album/w%3D2048/sign=38c43ff7902397ddd6799f046dbab3b7/9c16fdfaaf51f3dee973bf7495eef01f3b2979d8.jpg";
    	client.get(url, new BinaryHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0,binaryData.length, new Options());
				im.setImageBitmap(bitmap);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] binaryData,
					Throwable error) {
				
			}
		});
    }
    
    /**
     * 
     * 采用AsyncHttpClient的get方式获取图片
     */
    private void asyncHttpClientGetBitmapToSdcard(){
    	String url = "http://f.hiphotos.baidu.com/album/w%3D2048/sign=38c43ff7902397ddd6799f046dbab3b7/9c16fdfaaf51f3dee973bf7495eef01f3b2979d8.jpg";
    	client.get(url, new FileAsyncHttpResponseHandler(getApplicationContext()) {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, File file) {
				Logger.e("test", "file path = "+file.getAbsolutePath());
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				im.setImageBitmap(bitmap);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, File file) {
				
			}
		});
    }
    
}
