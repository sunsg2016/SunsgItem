package com.sunsg.item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.litepal.LitePalApplication;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.sample.Constants;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.util.LruCache;
import android.util.Log;

public class MainApplicaion extends LitePalApplication{
	private static String dbName = "kxhotel.db";
	private String rootPath;
	private String path = "sunsg11";
	private String dbPath;
	private String dbCityPath;
	private String imagePath;
	private String tempPath;
	
	
	
	
	//图片缓存
	private  LruCache<String, Bitmap> mMemoryCache;
	private int mMaxMemory;
	private int mCacheSize;
	
	@Override
	public void onCreate() {
		androidUniversalImageLoader();
		super.onCreate();
		checkRootFilePath();
		checkDbFileFromAssert();
		initCache();
		initImageLoader(getApplicationContext());
	}
	
	//初始化图片缓存
	public void initCache(){
		mMaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);  // 获取应用程序最大可用内存
		mCacheSize = mMaxMemory / 8;// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bit) {
//				return bit.getRowBytes() * bit.getHeight();
				return bit.getByteCount();
			}
		};
			Log.i("test", "maxxxcache  mCacheSize ＝ "+ mCacheSize);
	}
	
	
	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
			Log.i("test", "size1 = "+mMemoryCache.size());
		}
		Log.i("test", "size = "+mMemoryCache.size());
	}
	
	
	
	
	
	

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}
	
	
	//文件目录
	public void checkRootFilePath(){
		File file = null;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			 file = new File(Environment.getExternalStorageDirectory(),path);
			if(!file.exists()){
				file.mkdirs();
			}
			rootPath = file.getAbsolutePath();
			Log.i("test", "rootPath = "+rootPath);
		}else{
			file = new File("/data/data"+getPackageName()+"/files/sunsg11");
			if(!file.exists()){
				file.mkdirs();
			}
			rootPath = file.getAbsolutePath();
		}
		
		//数据库的地址
		file = new File(rootPath,"db");
		if(!file.exists()) {
			file.mkdirs();
		}
		dbPath = file.getAbsolutePath();
		
		//图片的地址
		file = new File(rootPath,"imagePath");
		if(!file.exists()){
			file.mkdirs();
		}
		imagePath = file.getAbsolutePath();
		
		//临时文件地址
		file = new File(rootPath,"tempPath");
		if(!file.exists()){
			file.mkdirs();
		}
		tempPath = file.getAbsolutePath();
		
	}
	
	public String getRootPath(){
		return rootPath;
	}
	
	public String getDbPath(){
		return dbPath;
	}
	
	public String getDbCityPath(){
		return dbCityPath;
	}
	
	public String getImagePath(){
		return imagePath;
	}
	
	public String getTempPath(){
		return tempPath;
	}
	
	//检查assert数据文件
	public void checkDbFileFromAssert(){
		File file = new File(dbPath,dbName);
		FileOutputStream fos = null;
		try {
			if(!file.exists() && file.createNewFile()){
				fos = new FileOutputStream(file);
				InputStream is = getAssets().open(dbName);
				byte[] buffer = new byte[10240];
				while(is.read(buffer) >0) fos.write(buffer);
				fos.flush();
				fos.close();
				is.close();
				dbCityPath = file.getAbsolutePath();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/**
	 * Android-Universal-Image-Loader
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	private void androidUniversalImageLoader(){
		if (Constants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	} 
}
