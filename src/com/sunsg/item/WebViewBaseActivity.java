package com.sunsg.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.breadtrip.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.sunsg.item.http.HttpUtilAnsync;
import com.sunsg.item.util.StorageUtils;

public class WebViewBaseActivity extends TheBaseActivity{
	private static final String URL = "WebViewBaseActivity_url";
	private WebView webView;
	private String url = "http://breadtrip.com/mobile/destination/route/2387718848/";
	private MyWebChrome mWebChrome;
	private AsyncHttpClient client;
	private Context mContext;
	private Handler mHandler;
	private String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		findView();
		setWebView();
		loadUrl();
	}
	
	private void findView(){
		webView = (WebView) findViewById(R.id.webview);
		mContext = getApplicationContext();
		client = HttpUtilAnsync.getInstance(getApplicationContext());
		mHandler = new Handler();
		path = getApplicationContext().getCacheDir().getAbsolutePath();
		Log.i("test", "pageage path = "+getApplicationContext().getCacheDir());
		File file = new File(path);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			Log.i("test", "file = "+files[i].getAbsolutePath());
		}
	}
	
	private void loadUrl(){
		
//		webView.loadUrl("file:///android_asset/index.html");
//		String name = Environment.getExternalStorageDirectory() +"/index.html";
//		try {
//			StorageUtils.copyFile(getAssets().open("index.html"), name);
//			File file = new File(name);
//			if(file.exists()) {
//				Log.i("test", "index = "+file.getAbsolutePath());
//			}else{
//				Log.i("test", "index not tttttt= ");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		webView.loadUrl("file:///sdcard"+"/index.html");
		File file  = new File(Environment.getExternalStorageDirectory(),"guang/"+StorageUtils.MD5(url));//".html"
		if(file.exists()){
			webView.loadUrl("file:///sdcard/guang/"+StorageUtils.MD5(url));//+".html"
			Log.i("test", "sdcard 存在");
		}else{
			webView.loadUrl(url);
			client.get(url, new FileAsyncHttpResponseHandler(mContext) {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers, File file) {
					if(file != null)
					StorageUtils.copyFile(file, Environment.getExternalStorageDirectory() + "/guang"+StorageUtils.MD5(url));//+".html"
					Log.i("test", "htmlllllllllllllllllllllllllllll 存在");
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						Throwable throwable, File file) {
					
				}
			});
		}
		
	}
	
	private void setWebView(){
		mWebChrome = new MyWebChrome();
        webView.setWebChromeClient(mWebChrome);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        String cacheDir = this.getApplicationContext().getDir("webCaCheDatabase", Context.MODE_PRIVATE).getPath(); 
        webSettings.setAppCacheMaxSize(1024*1024*8);
        webSettings.setAppCachePath(cacheDir);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
//        webSettings.setUserAgentString(new StringBuffer().append(webSettings.getUserAgentString()).append(" @BreadTrip/android/" + Utility.getAppVersionName(this) + "/zh").toString());
        webSettings.setBuiltInZoomControls(true);   
        setZoomControlGone(webView);
        
        
      //启用数据库  
        webSettings.setDatabaseEnabled(true);    
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 
        //启用地理定位  
        webSettings.setGeolocationEnabled(true);  
        //设置定位的数据库路径  
        webSettings.setGeolocationDatabasePath(dir);   

        //最重要的方法，一定要设置，这就是出不来的主要原因

        webSettings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                    Callback callback) {
                callback.invoke(origin, true, false);  
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });

        
        webView.setWebViewClient(new WebViewClient() {
            
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
					startActivity(intent);
				} else {
					view.loadUrl(url);
				}
                return true;
            }
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:onPageLoad()");//调用服务器就是方法
            }
            
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,final String url) {
				WebResourceResponse response = null;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					response = super.shouldInterceptRequest(view, url);
				}
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						client.get(url, new FileAsyncHttpResponseHandler(mContext) {
							
							@Override
							public void onSuccess(int statusCode, Header[] headers, File file) {
								if(file != null)
								Log.i("test", "file.getpath = "+file.getAbsolutePath());
								Log.i("test", "file.getpath = "+file.getParent());
								StorageUtils.copyFile(file, path + "/"+StorageUtils.MD5(url));
								file.delete();
							}
							
							@Override
							public void onFailure(int statusCode, Header[] headers,
									Throwable throwable, File file) {
								
							}
						});
					}
				});
				File file = new File(path,StorageUtils.MD5(url));
				if(file.exists())
					try {
						response = new WebResourceResponse("", "UTF-8", new FileInputStream(file));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				Log.i("test", "web url = "+url+"/n");
				return response;
			}
        });
        
      webView.setDownloadListener(new DownloadListener() {
			
			@Override
			public void onDownloadStart(String url, String userAgent,
			    String contentDisposition, String mimetype, long contentLength) {
				Uri uri = Uri.parse(url);  
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
	            startActivity(intent); 
			}
		});
	}
	
	
    private class MyWebChrome extends WebChromeClient {
        private CustomViewCallback mCallback;
        private View mCustomView;

        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            webView.setVisibility(View.GONE);

            // // if a view already exists then immediately terminate the new
            // one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

//            flContent.addView(view);
            mCustomView = view;
            mCallback = callback;
//            flContent.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHideCustomView() {

            if (mCustomView == null)
                return;

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
//            flContent.removeView(mCustomView);
            mCustomView = null;
//            flContent.setVisibility(View.GONE);
            mCallback.onCustomViewHidden();

            webView.setVisibility(View.VISIBLE);
        }
        
        public boolean inCustomView() {
            return (mCustomView != null);
        }
        
        public void hideCustomView() {
            onHideCustomView();
        }
    }
    
    public void setZoomControlGone(View view) {  
        Class classType;  
        Field field;  
        try {  
            classType = WebView.class;  
            field = classType.getDeclaredField("mZoomButtonsController");  
            field.setAccessible(true);  
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);  
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);  
            try {  
                field.set(view, mZoomButtonsController);  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            }  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (NoSuchFieldException e) {  
            e.printStackTrace();  
        }  
    }  
}
