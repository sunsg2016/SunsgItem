package com.sunsg.item.util;

/**
 * 
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;

import com.squareup.picasso.Request;
import com.sunsg.item.MainApplicaion;

public class ImageStroe {
	/** 最大缓存图片的数量 */
	private int max_images_count = 8;
	/** 最多创建几个线程 */
	private int max_thread_count = 3;
	/** 图片缓存路径 */
	private String cachePath;
	private List<Image> images;
	/** 正在下载的图片集合 */
	private List<String> downingImages;
	/** 线程池 */
	private ExecutorService threadPools = null;
	/** MainApplication */
	private MainApplicaion applicatioon;

	public ImageStroe(Context context) {
		applicatioon = (MainApplicaion) context.getApplicationContext();
		cachePath = applicatioon.getImagePath();// 默认的图片缓存地址
		images = new ArrayList<ImageStroe.Image>();
		downingImages = new ArrayList<String>();
		threadPools = Executors.newFixedThreadPool(max_thread_count);
	}

	/**
	 * 添加bitmap到内存
	 * 
	 * @param url
	 * @param bitmap
	 */
	private synchronized void addBitmap(String url, Bitmap bitmap) {
		if (!checkExistRom(url)) {
			int count = images.size();
			if (count >= max_images_count) {
				// 删除据现在事件最长的
				Image image = null;
				image = images.get(0);
				int minPoision = 0;
				long minTime = image.getLastUsedTime();
				for (int i = 1; i < count; i++) {
					image = images.get(i);
					if (image.getLastUsedTime() < minTime) {
						minTime = image.getLastUsedTime();
						minPoision = i;
					}
				}
				// 清楚缓存
				// image = images.get(minPoision);
				// if(image.bitmap != null && !image.bitmap.isRecycled()){
				// image.bitmap.recycle();
				// image.bitmap = null;
				// }
				images.remove(minPoision);
			}
			Image image = new Image();
			image.url = url;
			image.bitmap = bitmap;
			image.lastUsedTime = System.currentTimeMillis();
			images.add(image);
		}
	}

	/**
	 * 根据图片url获得图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		int count = images.size();
		Image image = null;
		for (int i = 0; i < count; i++) {
			image = images.get(i);
			if (image.equals(url)) {
				// 把此图片最后一次的使用事件改为当前时间
				image.lastUsedTime = System.currentTimeMillis();
				bitmap = image.bitmap;
				break;
			}
		}
		if (bitmap != null && bitmap.isRecycled()) {
			bitmap = null;
		}
		Logger.e("test", "image 从内存中加载 key url  = " + url);
		return bitmap;

	}

	/**
	 * 清楚数据
	 */
	public synchronized void clearData() {
		int count = images.size();
		Image image = null;
		for (int i = 0; i < count; i++) {
			image = images.get(i);
			if (image.bitmap != null && !image.bitmap.isRecycled()) {
				image.bitmap.recycle();
				image.bitmap = null;
			}
		}
		images.clear();
		downingImages.clear();
		// 加快垃圾回收器回收
		System.gc();

	}

	/**
	 * 设置缓存图片的最大数量
	 */
	public void setMaxImageCoount(int imageCount) {
		this.max_images_count = imageCount;
	}

	/**
	 * 设置最多创建线程的个数 默认 5个
	 * 
	 * @param threadCount
	 */
	public void setMaxImageThreadCount(int threadCount) {
		this.max_thread_count = threadCount;
		threadPools = Executors.newFixedThreadPool(threadCount);
	}

	/**
	 * 设置图片缓存路径
	 */
	public void setCachePath(String cachePath) {
		this.cachePath = cachePath;
	}

	/**
	 * 检查是否已经在内存中
	 * 
	 * @param url
	 */
	public synchronized boolean checkExistRom(String url) {
		boolean isExist = false;
		int count = images.size();
		Image image = null;
		for (int i = 0; i < count; i++) {
			image = images.get(i);
			if (image.url.equals(url)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	/**
	 * 检查是否有本地缓存
	 * 
	 * @param url
	 *            图片的下载地址 非md5过的
	 * @return
	 */
	public boolean checkExistCachePath(String url) {
		boolean isExist = false;
		File file = new File(cachePath, MD5(url));
		if (file.exists()) {
			isExist = true;
		}
		return isExist;
	}

	/**
	 * 是否正在下载此图片
	 * 
	 * @param url
	 * @return
	 */
	public boolean isDowning(String url) {
		return downingImages.contains(url);
	}

	/**
	 * 从本地缓存加载图片
	 * 
	 * @param cachePath
	 */
	@SuppressWarnings("finally")
	private Bitmap loadBitmapFromCachePath(String cachePath) {
		Bitmap bitmap = null;
		try {
			FileInputStream inputStream = new FileInputStream(cachePath);
			ByteArrayBuffer byteBuffer = new ByteArrayBuffer(1024);
			int count = -1;
			byte[] by = new byte[1024];
			try {
				count = inputStream.read(by);
				while (count > 0) {
					byteBuffer.append(by, 0, count);
					count = inputStream.read(by);
				}
				inputStream.close();
				bitmap = getBitmap(byteBuffer.buffer());

			} catch (IOException e) {
				Logger.e("test", "download image file io error!!!");
				e.printStackTrace();
			}
			byteBuffer = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			return bitmap;
		}

	}

	/**
	 * 通过bytes返回Bitmap
	 * 
	 * @param bytes
	 * @return
	 */
	public Bitmap getBitmap(byte[] bytes) {
		Options opt = new Options();
		// opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
		return bitmap;
	}
	
	public Bitmap getBitmap(byte[] bytes,int reWidth,int reHeitht){
		Options opt = new Options();
		// opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
		calculateInSampleSize(reWidth,reHeitht,opt.outWidth,opt.outHeight,opt);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
	}
	
	private void calculateInSampleSize(int reqWidth, int reqHeight, int width, int height, BitmapFactory.Options options) {
		    int sampleSize = 1;
		    if (height > reqHeight || width > reqWidth) {
		      final int heightRatio;
		      final int widthRatio;
		      if (reqHeight == 0) {
		        sampleSize = (int) Math.floor((float) width / (float) reqWidth);
		      } else if (reqWidth == 0) {
		        sampleSize = (int) Math.floor((float) height / (float) reqHeight);
		      } else {
		        heightRatio = (int) Math.floor((float) height / (float) reqHeight);
		        widthRatio = (int) Math.floor((float) width / (float) reqWidth);
		        sampleSize = Math.min(heightRatio, widthRatio);
		      }
		    }
		    options.inSampleSize = sampleSize;
		    options.inJustDecodeBounds = false;
		  }

	/**
	 * 下载图片的入口 从内存，从本地缓存和从网络获得图片 统一走这个方法
	 * 
	 * @param url
	 * @param requestCode
	 * @param listener
	 */
	public void downBitmap(final String url, final int requestCode, final LoadBitmapCallBack listener) {
		threadPools.execute(new Runnable() {

			@Override
			public void run() {
				Uri uri = Uri.parse(url);
				Logger.e("test", "uri.getpath = " + uri.toString());

				String path = cachePath + "/" + MD5(url);
				Bitmap bitmap = null;
				// 检查是否有本地缓存
				File file = new File(path, url);
				if (file.exists() && file.length() > 0) {
					bitmap = loadBitmapFromCachePath(path);
					Logger.e("test", "image 加载从本地缓存 path＝" + file.getAbsolutePath());
				} else {
					// 没有正在下载 去下载
					if (!isDowning(url)) {
						Logger.e("test", "image 加载从本地缓存 url＝" + url);
						FileOutputStream fos = null;
						InputStream is = null;
						try {
							downingImages.add(url);
							URL urlLoad = new URL(url);
							HttpURLConnection conn = (HttpURLConnection) urlLoad.openConnection();
							is = new BufferedInputStream(conn.getInputStream());
							is.mark(is.available());
							is.reset();
							bitmap = BitmapFactory.decodeStream(is);
							// 缓存到本地
							StorageUtils.write(is, path);
							conn.disconnect();
						} catch (Exception e) {
							e.printStackTrace();
							// TODO 在这个要不要加 下面一行
							// listener.onFinish(bitmap, requestCode);
						} finally {
							try {
								if (is != null)
									is.close();
							} catch (IOException e) {
							}

							try {
								if (fos != null)
									fos.close();
							} catch (IOException e) {
							}

							downingImages.remove(url);
						}
					}
				}

				if (bitmap != null) {
					// 添加图片到内存
					addBitmap(url, bitmap);
				}
				listener.onFinish(bitmap, requestCode);
			}
		});
	}

	/**
	 * 加载本地路径图片
	 * 
	 * @param filePath
	 * @param requestCode
	 * @param listener
	 */
	public void loadBitmap(final String filePath, int reWidth,int reHeight,final int requestCode, final LoadBitmapCallBack listener) {
		if (TextUtils.isEmpty(filePath)) {
			throw new IllegalArgumentException("文件路径不能为空");
		}
		threadPools.execute(new Runnable() {

			@Override
			public void run() {
//				Uri uri = Uri.parse(filePath);
				// uri.getPath();
				// if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
				if (!isDowning(filePath)) {
					downingImages.add(filePath);
					Logger.e("test", "loading image" + filePath);
					Bitmap bitmap = loadBitmapFromCachePath(filePath);
					
					// matrix.postRotate(ImageUtility.getPhotoOrientation(filePath));

					if (bitmap != null) {
						Matrix matrix = new Matrix();
						int exifOrientation = ImageUtility.getFileExifRotation(filePath);
						if (exifOrientation != 0) {
							matrix.preRotate(ImageUtility.getExifRotation(exifOrientation));
							int exifTranslation = ImageUtility.getExifTranslation(exifOrientation);
							if (exifTranslation != 1) {
								matrix.postScale(exifTranslation, 1);
							}
							Bitmap newResult = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
							if (newResult != bitmap) {
								bitmap.recycle();
								bitmap = newResult;
							}
						}
						
						// 添加图片到内存
						addBitmap(filePath, bitmap);
						listener.onFinish(bitmap, requestCode);

					}

					downingImages.remove(filePath);

				} else {
					Logger.e("test", "正在加载 。。。 path = " + filePath);
				}
				// }
			}
		});
	}

	
	
	/** Image bitmap 载体 */
	class Image {
		String url;
		long lastUsedTime;
		Bitmap bitmap;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public long getLastUsedTime() {
			return lastUsedTime;
		}

		public void setLastUsedTime(long lastUsedTime) {
			this.lastUsedTime = lastUsedTime;
		}

		public Bitmap getBitmap() {
			return bitmap;
		}

		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
	}

	/** 下载图片的回调接口 */
	public interface LoadBitmapCallBack {
		/**
		 * 
		 * @param bitmap
		 *            下载的bitmap
		 * @param requestCode
		 *            下载图片请求code
		 */
		void onFinish(Bitmap bitmap, int requestCode);

		/**
		 * 
		 * @param progress
		 *            下载的百分比
		 * @param requestCode
		 *            下载图片请求code
		 */
		void onChangeProgress(int progress, int requestCode);
	}

	/**
	 * MD5 加密
	 * 
	 * @param str
	 * @return
	 */
	public final static String MD5(String str) {
		String encodingStr = "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
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
}
