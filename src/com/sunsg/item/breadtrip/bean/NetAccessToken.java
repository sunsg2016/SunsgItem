package com.sunsg.item.breadtrip.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NetAccessToken {
	@SerializedName("access_token") //把json串中 access_token 字段 转换为此类 的 assessToken字段
    public String accessToken;
	
    public String refreshToken;
	
    @Expose //转换成json串是不包含带有此注解的字段
	@SerializedName("expires_in")
    public long expiresIon;
    
    public String token_type;
}
