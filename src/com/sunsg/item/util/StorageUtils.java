
package com.sunsg.item.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class StorageUtils {

	public static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    public static final String FILE_ROOT = SDCARD_ROOT + "kuxun/apks";
    public static final long LOW_STORAGE_THRESHOLD = 1024 * 1024 * 10;

    public static boolean isSdCardWrittenable() {

        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
    /**
     * 
     * @return
     */
    public static long getAvailableStorage() {
        String storageDirectory = null;
        storageDirectory = Environment.getExternalStorageDirectory().toString();
        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }

    public static boolean checkAvailableStorage() {
        if (getAvailableStorage() < LOW_STORAGE_THRESHOLD) {
            return false;
        }
        return true;
    }

    public static boolean isSDCardPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 建立目录
     * @throws IOException
     */
    public static void mkdir() throws IOException {
        File file = new File(FILE_ROOT);
        if (!file.exists() || !file.isDirectory())
            file.mkdirs();
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 转换数据
     * @param size
     * @return
     */
    public static String size(long size) {
        if (size / (1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024);
            DecimalFormat df = new DecimalFormat("#.##");
            return "" + df.format(tmpSize) + "MB";
        } else if (size / 1024 > 0) {
            return "" + (size / (1024)) + "KB";
        } else
            return "" + size + "B";
    }
    /**
     * 安装apk文件
     * @param context
     * @param url
     * @throws MalformedURLException 
     */
    public static void installAPK(Context context, final String url) throws MalformedURLException {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(FILE_ROOT, new File(new URL(url).getFile()).getName())), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    /**
     * 删除文件
     * @param path
     * @return
     */
    public static boolean delete(File path) {
        boolean result = true;
        if (path.exists()) {
            if (path.isDirectory()) {
                for (File child : path.listFiles()) {
                    result &= delete(child);
                }
                result &= path.delete(); // Delete empty directory.
            }
            if (path.isFile()) {
                result &= path.delete();
            }
            if (!result) {
                Log.e(null, "Delete failed;");
            }
            return result;
        } else {
            Log.e(null, "File does not exist.");
            return false;
        }
    }
    
    public final static String MD5(String str) {
		String encodingStr = "";
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
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
			encodingStr = new String(chars).toUpperCase();
		} catch (Exception e) {
			return encodingStr;
		}
		return encodingStr;
	}
    
    /**
     * copy文件
     * @param str
     * @return
     */
    
    public static void copyFile(File fromFile,String toFilePath){
    	File toFile = new File(toFilePath);
        try {
        	if(!toFile.exists()){
        		FileInputStream fis = new FileInputStream(fromFile);
        		copyFile(fis, toFilePath);
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
     * copy文件
     * @param str
     * @return
     */
    public static void copyFile(InputStream inputStream ,String toFilePath){
    		File toFile = new File(toFilePath);
        	try {
        		if(!toFile.exists()){
        			FileOutputStream fos = new FileOutputStream(toFile);
        			byte[] buffer = new byte[1024];
        			int length = 0;
        			while((length = inputStream.read(buffer))!=-1){
        				fos.write(buffer,0,length);
        			}
        			fos.flush();
        			fos.close();
        			inputStream.close();
        		}
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    }
    /**
     * 写文件
     * @param inputStream
     * @param toFilePath
     */
    public static void write(InputStream inputStream ,String toFilePath){
    	copyFile(inputStream,toFilePath);
    }
    
    
}
