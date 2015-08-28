package com.sunsg.item;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.breadtrip.R;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.StatusesAPI;

public class SinaShareActivity extends Activity implements IWeiboHandler.Response{
	/** 新浪授权 */
	private AuthInfo sinaWeibo;
	private SsoHandler sinaSsoHandler;
	/** 新浪微博api */
	private IWeiboShareAPI mWeiboShareAPI;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sina_share);
		sinaWeibo = new AuthInfo(this, "151032027", "http://api.breadtrip.com/accounts/oauth2_sina/login/callback/", null);
		sinaSsoHandler = new SsoHandler(this, sinaWeibo);
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, "151032027");
		mWeiboShareAPI.registerApp();
		sinaSsoHandler.authorize(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				
			}
			
			@Override
			public void onComplete(Bundle bundle) {
				String token = bundle.getString("access_token");
	            String expiresIn = bundle.getString("expires_in");
	            Oauth2AccessToken tokens = new Oauth2AccessToken();
	            tokens.setToken(token);
//	            tokens.setExpiresIn(expiresIn);
	            StatusesAPI mStatusesAPI = new StatusesAPI(getApplicationContext(), "151032027", tokens);
	           Bitmap bit  = BitmapFactory.decodeResource(getResources(), R.drawable.im_breadtrip_share_default);
	           String shareurl = "http://breadev.com/btrip/trip_id/2386607060/spots/2387719094/";

	           mStatusesAPI.upload("你好"+shareurl, bit, "0.0", "0.0",new RequestListener() {
					
					@Override
					public void onWeiboException(WeiboException arg0) {
						Logger.e("test", "分享失败 "+ arg0.toString());
					}
					
					@Override
					public void onComplete(String arg0) {
						Logger.e("test", "分享成功 "+ arg0);
					}
				});
	            
	            Logger.e("test", "sina auther token = "+token + "expirein = "+expiresIn);
			}
			
			@Override
			public void onCancel() {
				
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(sinaSsoHandler != null){
			sinaSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		Logger.e("test", "errcode "+baseResp.errCode);
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, "Share to Weibo Success！", Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, "Share is Canceled！", Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(this, "Share to Weibo Failure！" + "Error Message: " + baseResp.errMsg, Toast.LENGTH_LONG).show();
			break;
		}
		
		
		finish();
	}
}
