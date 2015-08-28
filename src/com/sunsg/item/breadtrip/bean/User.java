package com.sunsg.item.breadtrip.bean;

import com.google.gson.annotations.SerializedName;

public class User {
	public long id;
	public String name;
	public boolean isUsing;
	public String cookier;
	
	@SerializedName("id")
	public long netId;
	
	@SerializedName("name")
	public String userName;
	
	@SerializedName("avatar_m")
	public String avatarNorm;
	
	@SerializedName("avatar_s")
	public String avatarSmall;
	
	@SerializedName("avatar_l")
	public String avatarLar;
	
	@SerializedName("avatar_l")
	public SnsSync sns_sync;
	
	public NetAccessToken access_token;
	public static class SnsSync{
		@SerializedName("sina")
	    public boolean isBindSina;
		
		@SerializedName("tencent")
	    public boolean isBindTencent;
		
		@SerializedName("qq")
	    public boolean isBindQzone;
	}
}
