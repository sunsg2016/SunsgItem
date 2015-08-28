package com.sunsg.item.breadtrip.bean.util;

import android.content.Context;

import com.sunsg.item.breadtrip.bean.NetAccessToken;
import com.sunsg.item.breadtrip.bean.User;
import com.sunsg.item.util.AbJsonUtil;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.SPUtils;

public class UtilBean {

	public static void setAccessToken(Context context,String json){
		
	}
	
	public static NetAccessToken getAccessToken(Context context){
		NetAccessToken accessToken = null;
		return accessToken;
	}
	
	public static void setUser(Context context,String json){
		SPUtils.setFileName(SPUtils.Bread_Trip);
		SPUtils.put(context, "user", json);
		Logger.e("test", "user to json = "+json);
	}
	
	public static User getUser(Context context){
		User user = null;
		SPUtils.setFileName(SPUtils.Bread_Trip);
		String json = (String) SPUtils.get(context, "user", new String());
		Logger.e("test", "json to user = "+json);
		user = (User) AbJsonUtil.fromJson(json, User.class);
		if(user != null && user.access_token != null){
			user.cookier = user.access_token.token_type +" "+user.access_token.accessToken;
			user.isUsing = true;
		}
		return user;
	}
}
