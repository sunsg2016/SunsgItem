package com.sunsg.item.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Properties;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.breadtrip.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.Toast;

public class Tools {
	
	
	/**
	 * 判断字符串是否为空 </br> 判断条件是: null || "" || "null"
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null || "".equals(str.replaceAll(" ", "")) || "null".equals(str.toLowerCase()));
	}
	
	/**
	 * 用scheme 启动activity
	 * @param scheme 格式 "sunsg://guang" ; "sunsg" 代表 scheme  "guang" 代表 host 
	 */
	
	public static void startActivityWithScheme(Context context,String scheme){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
		context.startActivity(intent);
	}
	
	/**
	 * 根据手机的分辨率dp单位转为px像素
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public  static void toast(Context context,String toast){
		Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
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
	
	//计算insamplesize的值
	public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		// 都是0 不进行压缩
		if(reqWidth != 0 && reqHeight != 0){
			if (height > reqHeight || width > reqWidth) {
				// 计算出实际宽高和目标宽高的比率
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
				// 一定都会大于等于目标的宽和高。
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
		}
		return inSampleSize;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	    // 调用上面定义的方法计算inSampleSize值
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static Bitmap decodeBitmapFormPath(String path,int width,int height){
		
	    final BitmapFactory.Options options = new BitmapFactory.Options();// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	    
	    options.inJustDecodeBounds = true;//等与true时拿到的bitmap ＝ null options可以拿到图片的宽和高
	    
	    BitmapFactory.decodeFile(path, options);
	    
	    options.inSampleSize = calculateInSampleSize(options, width, height); // 调用上面定义的方法计算inSampleSize值
	    
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(path, options);
	}
	
	
    /**
     * 设置DefaultHttpClient的UserAgent
     * @author WangJun
     * @since 2013-3-8
     * @throws 无
     * @param dhc
     * @param context
     */
    public static void setUserAgent(DefaultHttpClient dhc, Context context) {
        Field[] fields = Build.class.getDeclaredFields();
        Properties mDeviceCrashInfo = new Properties();
        for (Field field : fields) {
            try {
                // setAccessible(boolean flag)
                // 将此对象的 accessible 标志设置为指示的布尔值。
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
        }
        dhc.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "BreadTrip/android/" + 
                getAppVersionName(context) + "/"+
                Locale.getDefault().getLanguage() +" (android OS" +
                android.os.Build.VERSION.RELEASE + ") " + mDeviceCrashInfo.getProperty("DEVICE","UNKOWN") + " " + "Map/AutoNavi/v1.4.2" + " (" + Build.MODEL +"," + Build.BRAND + ")");
    }
    
    /**
     * UserAgent content
     * @param context
     * @return
     */
    public static String buildUserAgentString(Context context){
    	 Field[] fields = Build.class.getDeclaredFields();
         Properties mDeviceCrashInfo = new Properties();
         for (Field field : fields) {
             try {
                 // setAccessible(boolean flag)
                 // 将此对象的 accessible 标志设置为指示的布尔值。
                 // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常
                 field.setAccessible(true);
                 mDeviceCrashInfo.put(field.getName(), field.get(null));
             } catch (Exception e) {
                 // TODO Auto-generated catch block
             }
         }
         StringBuffer str = new StringBuffer();
         str.append("BreadTrip/android/").append(getAppVersionName(context))
         .append("/")
         .append(Locale.getDefault().getLanguage())
         .append(" (android OS")
         .append(android.os.Build.VERSION.RELEASE)
         .append(") ")
         .append(mDeviceCrashInfo.getProperty("DEVICE","UNKOWN"))
         .append(" Map/AutoNavi/v1.4.2 (")
         .append(Build.MODEL)
         .append(",")
         .append(Build.BRAND)
         .append(")");
         return str.toString();
    }
    
    /**   
     * 返回当前程序版本名   
     */    
    public final static String getAppVersionName(Context context) {     
        String versionName = "";     
        try {     
            // ---get the package info---     
            PackageManager pm = context.getPackageManager();     
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);     
            versionName = pi.versionName;     
            if (versionName == null || versionName.length() <= 0) {     
                return "";     
            }     
        } catch (Exception e) {     
            
        }     
        return versionName;     
    }
    
    public final static SpannableString getImageSpanString(Context context,Drawable drawable){
//    	Drawable drawable = context.getResources().getDrawable(R.drawable.im_poi_details_douhao);
//		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		SpannableString spannable = new SpannableString("[des]");  
		ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
		spannable.setSpan(span, 0, "[des]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
    	return spannable;
    }
    
	/**
	 * 从assets 文件夹中获取文件并读取数据 
	 * @param context
	 * @param fileName
	 * @return
	 */
	  
    public static String getFromAssets(Context context,String fileName){  
        String result = "";  
            try {  
                InputStream in = context.getResources().getAssets().open(fileName);  
                //获取文件的字节数  
                int lenght = in.available();  
                //创建byte数组  
                byte[]  buffer = new byte[lenght];  
                //将文件中的数据读到byte数组中  
                in.read(buffer);  
                result = new String(buffer);
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return result;  
    }  
    
//    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//    Display display = wm.getDefaultDisplay();
//    DisplayMetrics dm = new DisplayMetrics();
//    display.getMetrics(dm);
}
