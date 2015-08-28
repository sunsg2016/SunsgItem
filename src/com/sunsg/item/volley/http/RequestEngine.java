package com.sunsg.item.volley.http;

/**
 * 
 * @author sunsg
 * 
 * @param 
 */
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sunsg.item.bean.QueryResult;
import com.sunsg.item.util.Debug;

public class RequestEngine {
	protected RequestQueue mQueue;
	protected Context context;
	public static Map<String, StringRequest> map = new HashMap<String, StringRequest>();

	public RequestEngine(Context context) {
		this(context, null);
	}

	public RequestEngine(Context context, RequestQueue queue) {
		this.context = context;
		if (queue == null)
			mQueue = Volley.newRequestQueue(context);
		else
			mQueue = queue;
	}

	// 取消本次查询
	public static void cancel(String action) {
		if (map.containsKey(action)) {
			map.get(action).cancel();
			map.remove(action);
			if (Debug.tag)
				Log.i("test", "[" + action + " ] 已经取消");
		}
	}

	public <T> RequestHandle addTask(final Query query, final CallBack callbcak) {

		if (map.containsKey(query.getAction())) {
			// 正在查询
			if (Debug.tag)
				Log.i("test", "action = [ " + query.getAction() + " ]" + "正在查询");
		} else {
			StringRequest request = new StringRequest(Method.POST,
					query.getUrl(), new Listener<String>() {

						@Override
						public void onResponse(String result) {
							if (Debug.tag)
								Log.i("test", "[ " + query.getAction()
										+ " ] rsult = [ " + result + " ]");
							Gson gson = new Gson();
							QueryResult re = (QueryResult) gson.fromJson(
									result, query.getType());
							re.setAction(query.getAction());
							callbcak.onSuccess(re);
							map.remove(query.getAction());
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							callbcak.onError("error");
							map.remove(query.getAction());
						}

					}) {
				@Override
				protected Map<String, String> getParams()
						throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					params.put(
							"content",
							"{\"cardtype\":0,\"birthday\":\"1988-08-22\",\"sid\":\"\",\"sex\":0,\"packagetype\":0,\"cardnum\":\"341281198808227756\",\"type\":0,\"syncflag\":\"s\",\"cid\":\"1b51990e-39a2-491b-a567-a9c4a5adbc3b\",\"insuranceamount\":0,\"sign\":\"\",\"insurancecount\":1,\"name\":\"孙曙光\",\"owner\":\"\",\"pricestr\":\"\",\"insuranceid\":\"\",\"ticketid\":\"\",\"insurancename\":\"\"}");
					Log.i("test", "post params");
					return params;
				}

				// @Override
				// public byte[] getBody() throws AuthFailureError {
				// //params == null ? super.getBody() :
				// return
				// "{\"cardtype\":0,\"birthday\":\"1988-08-22\",\"sid\":\"\",\"sex\":0,\"packagetype\":0,\"cardnum\":\"341281198808227756\",\"type\":0,\"syncflag\":\"s\",\"cid\":\"1b51990e-39a2-491b-a567-a9c4a5adbc3b\",\"insuranceamount\":0,\"sign\":\"\",\"insurancecount\":1,\"name\":\"孙曙光\",\"owner\":\"\",\"pricestr\":\"\",\"insuranceid\":\"\",\"ticketid\":\"\",\"insurancename\":\"\"}".getBytes();
				// }

				// @Override
				// public Map<String, String> getHeaders() throws
				// AuthFailureError {
				// Map<String, String> headers = new HashMap<String, String>();
				// headers.put("Charset", "UTF-8");
				// headers.put("Content-Type", "application/x-javascript");
				// return headers;
				// }
			};

			if (Debug.tag)
				Log.i("test", "[ " + query.getAction() + " ] url = [ "
						+ request.getUrl() + " ]");
			// if(Debug.tag) Log.i("test", "action = [ "+query.getAction()
			// +"]");
			mQueue.add(request);
			// RequestHandle hadler = new RequestHandle(request);
			map.put(query.getAction(), request);
		}

		return null;
	}

	// http://api.m.kuxun.cn/passengeroperation/android/4/baidu_chunhua_market%7C5.1.0%7C4.4.2/
	// content ==
	// {"cardtype":0,"birthday":"1988-08-22","sid":"","sex":0,"packagetype":0,"cardnum":"341281198808227756","type":0,"syncflag":"s","cid":"1b51990e-39a2-491b-a567-a9c4a5adbc3b","insuranceamount":0,"sign":"","insurancecount":1,"name":"孙曙光","owner":"","pricestr":"","insuranceid":"","ticketid":"","insurancename":""}

	public interface CallBack {
		void onSuccess(QueryResult result);

		void onError(String error);
	}

	// 加载图片
	public class BitmapCache implements ImageCache {

		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
			// int maxSize = 10 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}

}
