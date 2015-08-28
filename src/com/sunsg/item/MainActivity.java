package com.sunsg.item;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.breadtrip.R;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.sample.WaypointsActivity;
import com.nostra13.universalimageloader.sample.activity.HomeActivity;
import com.sunsg.item.bean.HotelDeatilsBean;
import com.sunsg.item.breadtrip.LoginFragment;
import com.sunsg.item.fragment.FragmentDemo;
import com.sunsg.item.fragment.ViewPageFragmentActivity;
import com.sunsg.item.http.HttpRequest;
import com.sunsg.item.http.HttpRequest.OnQueryListener;
import com.sunsg.item.http.HttpRequest.Query;
import com.sunsg.item.http.QueryResult;
import com.sunsg.item.testactivity.Activity1;
import com.sunsg.item.threadinterrupt.TestThreadInterrupt;
import com.sunsg.item.util.GsonTest;
import com.sunsg.item.volley.http.RequestEngine;
import com.sunsg.loopj.LoopjFragmentActivity;

public class MainActivity extends Activity implements OnQueryListener {
	private int index = 0;
	String action;
	TestThreadInterrupt thread;

	// private ConnectivityManager connectivityManager;
	// private NetworkInfo info;
	// private NetCheckBroadCast mNetCheckBroadCast;
	//
	// private class NetCheckBroadCast extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String action = intent.getAction();
	// if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	// Logger.i("test", "网络状态已经改变");
	// connectivityManager = (ConnectivityManager)
	// getSystemService(Context.CONNECTIVITY_SERVICE);
	// info = connectivityManager.getActiveNetworkInfo();
	// if (info != null && info.isAvailable()) {
	// String name = info.getTypeName();
	// Logger.i("test", "当前网络名称" + name);
	// } else {
	// }
	// }
	// }
	//
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// httpRequest();
		// httpPostRequest();
		new GsonTest();
		action = "test_volley_http_get_1";
		// getJsonPost();
		Log.i("test", "hide");
		((MainApplicaion) getApplication()).checkRootFilePath();
		// TestThreadInterrupt 测试
		thread = new TestThreadInterrupt();
		// thread.start();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i("test", "hide");
		Toast.makeText(getApplicationContext(), "hide", Toast.LENGTH_SHORT).show();
		super.onConfigurationChanged(newConfig);
	}

	// 本机应用
	public void installed_apk(View v) {
		Log.i("test", "getMac = " + getMac());
		Intent intent = new Intent(this, WhichAppInstalledActivity.class);
		startActivity(intent);
		thread.quite();
	}

	// scrollview slide
	public void slide(View v) {
		Intent intent = new Intent(this, slideViewActivity.class);
		startActivity(intent);
		RequestEngine.cancel(action);
		// action = "test_volley_http_get_2";
		// volleyHttpGet(null);
		thread.interrupt();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.i("test", "hide");
		Toast.makeText(getApplicationContext(), "hide", Toast.LENGTH_SHORT).show();
		return super.dispatchKeyEvent(event);
	}

	// volley http get
	public void volleyHttpGet(View v) {
		RequestEngine engine = new RequestEngine(MainActivity.this);
		com.sunsg.item.volley.http.Query query = new com.sunsg.item.volley.http.Query(this);
		query.setAction("test_post");
		query.setMethod(com.sunsg.item.volley.http.Query.POST);
		// Map<String, String> map = new HashMap<String, String>();
		//
		// map.put("deviceid", "1b8e2bd5-d1ed-449f-99c2-3d16d62fccb2");
		// map.put("city", "%E5%8C%97%E4%BA%AC");
		// map.put("checkintime", "2014-07-08");
		// map.put("price", "");
		// map.put("checkouttime", "2014-07-09");
		// map.put("appname", "KuxunTravelBaodian");
		// map.put("length", "15");
		// map.put("offset", "0");
		query.setType(new TypeToken<HotelDeatilsBean>() {
		}.getType());
		// query.buildUrl(map);
		// query.setUrl("http://apitest.dev.m.kuxun.cn/getHotelWithCLK/android/4/baidu_chunhua_market%7C3.0.0%7C4.2.2/?zoom=&deviceid=1b8e2bd5-d1ed-449f-99c2-3d16d62fccb2&city=%E5%8C%97%E4%BA%AC&checkintime=2014-07-08&price=&checkouttime=2014-07-09&style=&order=&appname=KuxunTravelBaodian&service=&length=15&brand=&offset=0&key=");
		engine.addTask(query, callback);
	}

	// 自定义listview
	public void MyistView(View view) {
		Intent intent = new Intent(this, MyListActivity.class);
		startActivity(intent);
	}

	// 选择城市
	public void SelectCity(View v) {
		Intent intent = new Intent(this, SelectCityActivity.class);
		startActivity(intent);

	}

	// 图片listview
	public void ImageListView(View v) {
		Intent intent = new Intent(this, ImageListActivity.class);
		startActivity(intent);
	}

	// fragment
	public void fragment(View v) {
		Intent intent = new Intent(this, FragmentDemo.class);// TestFragmentActivity
																// ViewPageFragmentActivity
		startActivity(intent);
	}

	// testactivity
	public void testactivity(View v) {
		Intent intent = new Intent(this, Activity1.class);
		startActivity(intent);
	}

	// pulltooomactivity
	public void pulltooomactivity(View v) {
		Intent intent = new Intent(this, PullToZoomActivity.class);
		startActivity(intent);
	}

	// 百度定位
	public void baidulocation(View v) {
		Intent intent = new Intent(this, BaiduLocation.class);
		startActivity(intent);
	}

	// RandomAccessFile
	public void randomaccessfile(View v) {
		Intent intent = new Intent(this, RandomAccessFileActivity.class);
		startActivity(intent);
	}

	// loopj
	public void loopj(View v) {
		Intent intent = new Intent(this, LoopjFragmentActivity.class);
		startActivity(intent);
	}

	// loopj sample
	public void loopjSample(View v) {
		Intent intent = new Intent(this, WaypointsActivity.class);
		startActivity(intent);
	}

	// universalImageLoader sample
	public void universalImageLoader(View v) {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	// universalImageLoader sample
	public void loginFragment(View v) {
		Intent intent = new Intent(this, LoginFragment.class);
		startActivity(intent);
	}

	// personal image sample
	public void personalImage(View v) {
		Intent intent = new Intent(this, com.sunsg.item.persionalimage.MainActivity.class);
		startActivity(intent);
	}

	// 抽奖
	public void luckDrawActivity(View v) {
		Intent intent = new Intent(this, LuckDrawActivity.class);
		startActivity(intent);
	}

	// webview
	public void webview(View v) {
		Intent intent = new Intent(this, WebViewBaseActivity.class);
		startActivity(intent);
	}

	// test move
	public void consumeActivity(View v) {
		Intent intent = new Intent(this, ConsumeActivity.class);
		startActivity(intent);
	}

	// testTouchEvent
	public void testTouchEvent(View v) {
		Intent intent = new Intent(this, TestViewTouchEventActivity.class);
		startActivity(intent);
	}

	// LitePalActivity
	public void litePalActivity(View v) {
		Intent intent = new Intent(this, LitePalActivity.class);
		startActivity(intent);
	}

	// TestThreadActivity
	public void testThreadActivity(View v) {
		Intent intent = new Intent(this, TestThreadActivity.class);
		startActivity(intent);
	}

	// TestThreadActivity
	public void baseJavaTestActivity(View v) {
		Intent intent = new Intent(this, BaseJavaTestActivity.class);
		startActivity(intent);
	}

	// 新浪微博分享sdk
	public void sinaShare(View v) {
		Intent intent = new Intent(this, SinaShareActivity.class);
		startActivity(intent);
	}

	// drag
	public void ViewDragHelper(View v) {
		Intent intent = new Intent(this, ViewDragActivity.class);
		startActivity(intent);
	}

	//横向打开的view
	public void HorOpenAndCloseView(View v) {
		Intent intent = new Intent(this, HoriOpenAndCloseActivity.class);
		startActivity(intent);
	}

	private RequestEngine.CallBack callback = new RequestEngine.CallBack() {

		@Override
		public void onSuccess(com.sunsg.item.bean.QueryResult result) {
			// Log.i("test", "rsult .getaction = "+result.getAction());
			// Gson gson = new Gson();
			// Log.i("test",
			// "hotels   = "+gson.toJson((HotelDeatilsBean)result,new
			// TypeToken<HotelDeatilsBean>(){}.getType()));
			//
		}

		@Override
		public void onError(String error) {

		}

	};

	private void getJsonPost() {
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		String url = "http://127.0.0.1:8099/api/api.ashx";
		StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// 处理返回的JSON数据
				Log.d("test", "Response" + response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				// POST 参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("action", "product_list");
				params.put("ver", "1");
				params.put("pageindex", "1");
				params.put("pageCount", "5");
				return params;
			}
		};
		requestQueue.add(postRequest);
	}

	// http 请求
	private void httpRequest() {
		// get请求
		findViewById(R.id.Btn_http_request).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "点击", Toast.LENGTH_SHORT).show();
				Query query = null;
				if (index == 0) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("appname", "KuxunHotel");
					params.put("deviceId", "58231515-6557-4f0e-a825-99fc0ab66b91");
					params.put("zoom", "");
					params.put("city", "%E5%8C%97%E4%BA%AC");
					params.put("checkintime", "2014-04-10");
					params.put("price", "");

					params.put("checkouttime", "2014-04-11");
					params.put("style", "");
					params.put("order", "");
					params.put("service", "");

					params.put("offset", "0");
					params.put("key", "");
					query = new Query("getHotelWithCLK/android/4/Kuxun_Market%7C5.0.0%7C4.2.2/", "HELLO", params, false, null);
				} else if (index == 1) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("appname", "KuxunPlane");
					params.put("deviceId", "74585df6-10bf-4ab7-8420-a2e70c5be3f2");
					params.put("authToken", "0c7a2e309e100be8c4c6b5a1d029e8c2");
					params.put("depart", "%E5%8C%97%E4%BA%AC");

					params.put("arrive", "%E4%B8%8A%E6%B5%B7");
					params.put("date", "1397059200");
					query = new Query("getFlightWithQushi/android/3/360_Market%7C4.5.3%7C4.2.2/", "QUSHI", params, false, null);

				} else if (index == 2) {
					// http://api.dev.m.kuxun.cn/getappimages/android/3/360_Market%7C4.5.3%7C4.2.2/?appname=KuxunPlane&deviceId=74585df6-10bf-4ab7-8420-a2e70c5be3f2&authToken=7ee2c2ce71a81a0fa605d21ccac21ecc&type=bootchart
					Map<String, String> params = new HashMap<String, String>();
					params.put("appname", "KuxunPlane");
					params.put("deviceId", "74585df6-10bf-4ab7-8420-a2e70c5be3f2");
					params.put("authToken", "7ee2c2ce71a81a0fa605d21ccac21ecc");
					params.put("type", "bootchart");

					query = new Query("getappimages/android/3/360_Market%7C4.5.3%7C4.2.2/", "YINDAOTU", params, false, null);

				} else if (index == 3) {
					// http://api.dev.m.kuxun.cn/getdateholidays/android/2/360_Market%7C4.5.3%7C4.2.2/?appname=KuxunPlane&deviceId=74585df6-10bf-4ab7-8420-a2e70c5be3f2&authToken=efbf08832323acf12d9d7110408ffe2e
					Map<String, String> params = new HashMap<String, String>();
					params.put("appname", "KuxunPlane");
					params.put("deviceId", "74585df6-10bf-4ab7-8420-a2e70c5be3f2");
					params.put("authToken", "efbf08832323acf12d9d7110408ffe2e");
					query = new Query("getdateholidays/android/2/360_Market%7C4.5.3%7C4.2.2/", "HOLIDAY", params, false, null);

				} else if (index == 4) {
					// http://api.dev.m.kuxun.cn/getappimages/android/3/360_Market%7C4.5.3%7C4.2.2/?appname=KuxunPlane&deviceId=74585df6-10bf-4ab7-8420-a2e70c5be3f2&authToken=7ee2c2ce71a81a0fa605d21ccac21ecc&type=bootchart
					Map<String, String> params = new HashMap<String, String>();
					params.put("appname", "KuxunPlane");
					params.put("deviceId", "74585df6-10bf-4ab7-8420-a2e70c5be3f2");
					params.put("authToken", "7ee2c2ce71a81a0fa605d21ccac21ecc");
					params.put("type", "bootchart");

					query = new Query("getappimages/android/3/360_Market%7C4.5.3%7C4.2.2/", "IMAGE", params, false, null);

				}
				HttpRequest.setOnQueryListener(MainActivity.this);
				HttpRequest.startQuery(query);
				index++;
				if (index == 4)
					index = 0;

			}
		});

		// put请求
		findViewById(R.id.Btn_http_request).setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				Toast.makeText(MainActivity.this, "长按", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	private void httpPostRequest() {
		findViewById(R.id.Btn_http_post).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// http://api.dev.m.kuxun.cn/usercenterlogin/android/4/Kuxun_Market%7C5.0.0%7C4.2.2/?deviceid=8c9841ef-de0f-4740-91a0-77979acfd8e5&appname=KuxunPlane
				// content =
				// {"uname":"15210110302","pass":"123456","deviceid":"8c9841ef-de0f-4740-91a0-77979acfd8e5"}
				Map<String, String> params = new HashMap<String, String>();
				params.put("deviceid", "8c9841ef-de0f-4740-91a0-77979acfd8e5");
				params.put("appname", "KuxunPlane");

				Map<String, String> nameValue = new HashMap<String, String>();
				nameValue.put("content",
						"{\"uname\":\"15210110302\",\"pass\":\"123456\",\"deviceid\":\"8c9841ef-de0f-4740-91a0-77979acfd8e5\"}");
				Query query = new Query("usercenterlogin/android/4/Kuxun_Market%7C5.0.0%7C4.2.2/", "LHOGINlogin", params, true, nameValue);
				HttpRequest.setOnQueryListener(MainActivity.this);
				HttpRequest.startQuery(query);
			}
		});
	}

	@Override
	public void onQueryStart(String queryAction) {

	}

	/**
	 * 1 cpu号：
	 * 
	 * 文件在： /proc/cpuinfo
	 * 
	 * 通过Adb shell 查看：
	 * 
	 * adb shell cat /proc/cpuinfo
	 * 
	 * 2 mac 地址
	 * 
	 * 文件路径 /sys/class/net/wlan0/address
	 * 
	 * adb shell cat /sys/class/net/wlan0/address xx:xx:xx:xx:xx:aa
	 * 
	 * 这样可以获取两者的序列号，
	 * 
	 * @return
	 */
	private String getMac() {
		String macSerial = "";
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("/sys/class/net/wlan0/address");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

	@Override
	public void onQueryComplete(QueryResult queryResult) {
		if (queryResult != null) {
			// debug
			Log.i("test", "queryAction ==================++++++++++++++++++++" + queryResult.getQueryAction());
		}
	}

}
