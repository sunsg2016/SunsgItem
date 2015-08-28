package com.sunsg.item;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.breadtrip.R;

public class WhichAppInstalledActivity extends Activity{
	private LinearLayout mRootLL;
	private List<AppInfer> mList;
	private MyHandler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		notInstalledAction();
		setContentView(R.layout.activity_which_app_installed);
		mRootLL = (LinearLayout) findViewById(R.id.content_LL);
		mList = new ArrayList<WhichAppInstalledActivity.AppInfer>();
		handler = new MyHandler();
		getOthersApp();
	}
	
	
	
	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				findViewById(R.id.loading).setVisibility(View.GONE);
				LinearLayout.LayoutParams params = null;
				AppInfer appinfo = null;
				for (int i = 0; i < mList.size(); i++) {
					appinfo = mList.get(i);
					LinearLayout ll = new LinearLayout(WhichAppInstalledActivity.this);
					ll.setTag(appinfo);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 5, 10, 5);
					ImageView icon = new ImageView(WhichAppInstalledActivity.this);
					icon.setLayoutParams(params);
					icon.setImageDrawable(appinfo.appIcon);
					
					ll.addView(icon);
					
					LinearLayout llRight = new LinearLayout(WhichAppInstalledActivity.this);
					llRight.setOrientation(LinearLayout.VERTICAL);
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 0, 10, 5);
					ll.addView(llRight);
					
					TextView appPageageName = new TextView(WhichAppInstalledActivity.this);
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 5, 10, 0);
					appPageageName.setLayoutParams(params);
					appPageageName.setText(appinfo.appPackegName);
					appPageageName.setTextSize(18);
					appPageageName.setTextColor(0xff000000);
					llRight.addView(appPageageName);
					
					TextView appName = new TextView(WhichAppInstalledActivity.this);
					params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 5, 10, 5);
					appName.setLayoutParams(params);
					appName.setText(appinfo.appName);
					appName.setTextSize(18);
					appName.setTextColor(0xff000000);
					llRight.addView(appName);
					
					params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
					ll.setLayoutParams(params);
					mRootLL.addView(ll);
					
					TextView tvline = new TextView(WhichAppInstalledActivity.this);
					params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,1);
					tvline.setBackgroundColor(0xff000000);
					mRootLL.addView(tvline);
					
					ll.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							AppInfer appinfo = (AppInfer) v.getTag();
							 PackageManager packageManager = WhichAppInstalledActivity.this.getPackageManager(); 
							 Intent intent=new Intent(Intent.ACTION_MAIN, null); 
							 intent.addCategory(Intent.CATEGORY_LAUNCHER); 
							 intent =packageManager.getLaunchIntentForPackage(appinfo.appPackegName); 
							 try {
								 startActivity(intent); 
							} catch (Exception e) {
								Toast.makeText(getApplication(), "此应用么有界面", Toast.LENGTH_LONG).show();
							}
							 
							 
//							 Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);  
//						        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER); 
						}
					});
				}
			}
		}
	}
	
	
	private void getOthersApp(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				isAvilible(WhichAppInstalledActivity.this,"");
			}
		}).start();
		
		
	}
	
//	判断手机已安装某程序的方法： 
	private boolean isAvilible(Context context, String packageName){ 
		final PackageManager packageManager = context.getPackageManager();//获取packagemanager 
	    List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息 
	    List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名 
	    //pinfo信息
	       if(pinfo != null){ 
	            for(int i = 0; i < pinfo.size(); i++){ 
	                  String pn = pinfo.get(i).packageName; 
	                  AppInfer appinfo = new AppInfer();
	                  appinfo.appIcon = packageManager.getApplicationIcon(pinfo.get(i).applicationInfo);
	                  appinfo.appPackegName = pinfo.get(i).packageName;
	                  appinfo.appName = packageManager.getApplicationLabel(pinfo.get(i).applicationInfo).toString();
	                  mList.add(appinfo);
//	                  Log.i("test", "app name = "+packageManager.getApplicationLabel(pinfo.get(i).applicationInfo).toString());
	                  pName.add(pn); 
	              } 
	          } 
	       handler.sendEmptyMessage(1);
	       return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE 
	}
//   没有安装去下载
	private void notInstalledAction(){
		if(isAvilible(this, "com.tencent.mm")){ 
            Intent i = new Intent(); 
            ComponentName cn = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"); 
            i.setComponent(cn); 
            startActivityForResult(i, RESULT_OK);    
        } 
       //未安装，跳转至market下载该程序 
        else { 
            Uri uri = Uri.parse("market://details?id=com.tencent.mm");//id为包名 
            Intent it = new Intent(Intent.ACTION_VIEW, uri); 
            startActivity(it); 
        }
	}
	
	class AppInfer{
		public String appPackegName;
		public String appName;
		public Drawable appIcon;
		public AppInfer(){
			
		}
	}


}
