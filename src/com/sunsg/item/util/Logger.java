/**
 * 
 */
package com.sunsg.item.util;

import android.util.Log;

/**日志管理，所有的Log应该从这里输出
 * @ClassName: Logger
 * @author 
 * @date 
 *
 */
public class Logger {

    public final static boolean IS_OPEN = true;
    private final static String TAG = "SunsgItem";
    
    public static void e(String log){
        if(IS_OPEN) {
            Log.i(TAG, log);
        }
    }
    
    public static void e(String tag, String log) {
        if(IS_OPEN) {
            Log.i(tag, log);
        }
    }
    
    public static void i(String log){
        if(IS_OPEN) {
            Log.i(TAG, log);
        }
    }
    
    public static void i(String tag, String log) {
        if(IS_OPEN) {
            Log.i(tag, log);
        }
    }
    
    public static void d(String log){
        if(IS_OPEN) {
            Log.d(TAG, log);
        }
    }
    
    public static void d(String tag, String log) {
        if(IS_OPEN) {
            Log.d(tag, log);
        }
    }
}
