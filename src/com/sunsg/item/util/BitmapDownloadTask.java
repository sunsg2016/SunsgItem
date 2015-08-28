package com.sunsg.item.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.sunsg.item.MainApplicaion;
/**
 * 异步下载图片的任务
 */
public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap>{
	private String imageUrl;
	private MainApplicaion mApp;
	private int bitmapWidth;
	private int bitmapheitht;
	private OnLoadImageFinish oLoadImageFinish;
	public interface OnLoadImageFinish{
		void onLoadImageFinish(Bitmap bitmap,String url);
	}
	public void setOnLoadImageFinish(OnLoadImageFinish loadImageFinish){
		this.oLoadImageFinish = loadImageFinish;
	}
	public BitmapDownloadTask(){
		
	}
	public BitmapDownloadTask(MainApplicaion app){
		init(app,0,0);
	}
	
	public BitmapDownloadTask(MainApplicaion app,int width,int height){
		init(app,width,height);
	}
	
	private void init(MainApplicaion app,int width,int height){
		mApp = app;
		bitmapWidth = width;
		bitmapheitht = height;
//		execute();
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		imageUrl = params[0];
		Bitmap bitmap = null;
		if(!TextUtils.isEmpty(imageUrl)){
			// 在后台开始下载图片
			bitmap = downloadBitmap(params[0]);
			if(bitmap != null){
				// 图片下载完成后缓存到LrcCache中
				BitmapUtils.getInstance().addBitmapToMemoryCache(imageUrl, bitmap);
			}
		}
		return bitmap;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if(oLoadImageFinish != null) oLoadImageFinish.onLoadImageFinish(result, imageUrl);
	}
	
	/**
	 * 建立HTTP请求，并获取Bitmap对象。
	 * 
	 * @param imageUrl
	 *            图片的URL地址
	 * @return 解析后的Bitmap对象
	 */
	private Bitmap downloadBitmap(String imageUrl) {
		Bitmap bitmap = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(imageUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5 * 1000);
			con.setReadTimeout(10 * 1000);
			con.setUseCaches(false);
//			con.setDoInput(true);
//			con.setDoOutput(true);
			
//			File file = new File(mApp.getImagePath(),StorageUtils.MD5(imageUrl));
//			String path = file.getAbsolutePath();
//			Log.i("test", "path = "+path);
//			StorageUtils.write(con.getInputStream(), path);
//			
//			bitmap = Tools.decodeBitmapFormPath(path,bitmapWidth,bitmapheitht);
			
			
			InputStream is = con.getInputStream();
			if(is != null) {
                long streamLength = con.getContentLength();
                byte[] bytes = new byte[1024];
                ByteArrayBuffer byteBuffer = new ByteArrayBuffer(1024);
                int len = -1;
                int downloadSize = 0;
                int progress = 0;
               
                while((len = is.read(bytes)) != -1) {
                    byteBuffer.append(bytes, 0, len);
                    downloadSize += len;
                }
                   is.close();
                   
                   Options opt = new Options();
//       	        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
       	        opt.inPurgeable = true;  
       	        opt.inInputShareable = true;  
       			bitmap = BitmapFactory.decodeByteArray(byteBuffer.buffer(), 0, byteBuffer.buffer().length, opt);
                }
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		catch (OutOfMemoryError e) {
//			Log.i("test", "内存泄露");
//		} 
		finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return bitmap;
	}
	
	

}
