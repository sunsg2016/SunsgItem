package com.sunsg.item.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.util.Log;

public class BitmapUtils {
	private static BitmapUtils ME;
	private  LruCache<String, Bitmap> mMemoryCache;
	private int mMaxMemory;
	private int mCacheSize;
	
	public static BitmapUtils  getInstance(){
		if(ME == null){
			ME = new BitmapUtils();
		}
		return ME;
	}
	
	private BitmapUtils(){
		init();
	}
	
	//初始化
	public void init(){
		mMaxMemory = (int) (Runtime.getRuntime().maxMemory());  // 获取应用程序最大可用内存
		mCacheSize = mMaxMemory / 8;// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize){
			@Override
			protected int sizeOf(String key, Bitmap bit) {
				return bit.getRowBytes() * bit.getHeight();
			}
		};
	}
	
	
	//清楚缓存
	public void clear(){
		synchronized (this) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					mMemoryCache.evictAll();
				}
			}).start();
		}
	}
	
	
	/**
	 * 根据key从sdCard加载图片
	 * 
	 * 
	 * @param path
	 *            所获图片的绝对地址
	 * @param width
	 *             设置所获图片的宽
	 * @param  height 
	 *              设置所获图片的高
	 */
	public Bitmap loadImageFromFile(String path,int width,int height){
		return Tools.decodeBitmapFormPath(path, width, height);
	}
	
	/**
	 * 根据key从sdCard加载图片
	 * 
	 * 
	 * @param path
	 *            所获图片的绝对地址
	 *            
	 */
	public Bitmap loadImageFromFile(String path){
		return Tools.decodeBitmapFormPath(path, 0, 0);
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
	
	/**
	 * 获得str的MD5串
	 * 
	 * @param str
	 * @return
	 */
	public final static String MD5(String str) {
		String encodingStr = "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char chars[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				chars[k++] = hexDigits[byte0 >>> 4 & 0xf];
				chars[k++] = hexDigits[byte0 & 0xf];
			}
			encodingStr = new String(chars);
		} catch (Exception e) {
			return encodingStr;
		}
		return encodingStr;
	}
	
	//模糊处理
	public static Bitmap getBlurBitmap(Context context,Bitmap bitmap){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		bitmap = Blur.fastblur(context, bitmap, 12);
		return bitmap;
	}
	
	//获取圆形图
	public static Bitmap getRoundeBitmap(Bitmap bitmap){
		 int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
        } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
        }
         
        Bitmap output = Bitmap.createBitmap(width,
                        height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
         
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);
         
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
	}
	
	
	private void openSelectAndCrop() {
//		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		Intent intent = new Intent("android.intent.action.PICK");
		intent.setType("image/*");
		intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
//		intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
		intent.setDataAndType(Uri.fromFile(new File("")), "image/*");
		intent.putExtra("output", Uri.fromFile(new File("")));
		intent.putExtra("outputFormat", "JPEG");// 返回格式
//		startActivityForResult(intent, 1000000);
		
//		 Intent intent = new Intent("android.intent.action.PICK");
//         intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//         intent.putExtra("output", getAvatarImagePath());
//         intent.putExtra("crop", "true");
//         intent.putExtra("aspectX", 1);// 裁剪框比例
//         intent.putExtra("aspectY", 1);
//         intent.putExtra("outputX", DefaultConfig.AVATAR_WIDTH);// 输出图片大小
//         intent.putExtra("outputY", DefaultConfig.AVATAR_HEIGTH);
//         startActivityForResult(intent, code_crop);
	}
	
	public static Bitmap getBitmapFromeInputStream(InputStream is,Options opt){
		Bitmap bitmap = null;
		if(opt == null){
			opt = new Options();
//	        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;
		}
		if(is != null) {
			byte[] bytes = new byte[1024];
			ByteArrayBuffer byteBuffer = new ByteArrayBuffer(1024);
			int len = -1;
			try {
				while ((len = is.read(bytes)) != -1) {
					byteBuffer.append(bytes, 0, len);
				}
				is.close();
				bitmap = BitmapFactory.decodeByteArray(byteBuffer.buffer(), 0,byteBuffer.buffer().length, opt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	
	
}
