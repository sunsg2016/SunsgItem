/**
 * 
 */
package com.breadtrip.view;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.breadtrip.R;
import com.breadtrip.bean.User;
import com.breadtrip.datacenter.ImageStorage;
import com.breadtrip.datacenter.ImageStorage.LoadImageCallback;
import com.breadtrip.datacenter.UserCenter;
import com.breadtrip.net.Constant;
import com.breadtrip.net.HttpTask.EventListener;
import com.breadtrip.net.NetShareManager;
import com.breadtrip.utility.Logger;
import com.breadtrip.utility.Utility;
import com.breadtrip.view.ChosePhotoActivity.PhotoInfo;
import com.breadtrip.view.base.BaseActivity;
import com.breadtrip.view.controller.SNSBind;
import com.breadtrip.view.controller.ShareTrack.WebShareData;
import com.breadtrip.view.customview.ProgressDialog;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tendcloud.tenddata.TCAgent;

/**
 * 分享内容到微博
 * @ClassName: ShareMicroblogActivity
 * @author WangJun
 * @date 2012-9-19 下午05:34:51
 */
public class ShareMicroblogActivity extends BaseActivity implements IWeiboHandler.Response{
    public final static String SHARE_TRIP_URL = "http://breadtrip.com/trips/%s/";
    public final static String SHARE_TRACK_URL = "http://breadtrip.com/trips/%s/#wp&s";
    public final static String SHARE_PASSPORT_AND_ACHIECE ="http://breadtrip.com/u/%s/";
    public final static String SHARE_DESTINATION ="http://destination/place/%s/%s/";
    
    public final static String KEY_SHARE_TITLE = "title";
    public final static String KEY_TRIP_ID = "tripId";
    public final static String KEY_TRACK_ID = "trackId";
    public final static String KEY_DESTINATION_ID = "destinationId";
    public final static String KEY_DESTINATION_TYPE = "destinationType";
    public final static String KEY_DATA = "data";
    public final static String KEY_TYPE = "type";
    public final static String KEY_TEXT = "text";
    public final static String KEY_PHOTO = "photo";
    public final static String KEY_CODE = "code";

    public final static int TYPE_TRIP = 0;
    public final static int TYPE_ALL_PASSPORT = 1;
    public final static int TYPE_MILEAGES = 2;
    public final static int TYPE_TRAVEL_ACHIEVEMENTS = 3;
    public final static int TYPE_TRACK = 4;
    public final static int TYPE_SHARE_APP = 5;
    public final static int TYPE_END_TRIP = 6;
    public final static int TYPE_A_PASSPORT = 7;
    public final static int TYPE_SHARE_WEB_DATA = 8;
    public final static int TYPE_SHARE_DESTINATION = 9;
    
    private final int code_no_network = -1;
    private final int code_chose_photo = 2;
    private final int code_share_trip = 3;
    private final int code_down_image = 5;
    private final int code_get_access_token = 6;
    
    private final int FALSE = 0;
    private final int TRUE = 1;
    private final String interval = ",";
    
    private ImageButton btnBack;
    private ImageButton btnOK;
    private TextView tvTitle;
    private EditText etContent;

    private ImageView ivPhoto;
    private RelativeLayout rlActionBar;
    private ProgressDialog progressDialog;
    private ImageView ivTitleLogo;

    private UserCenter userCenter;
    private NetShareManager netShareManager;
    private ImageStorage imageStorage;
    
    private ArrayList<PhotoInfo> data;
    private ArrayList<PhotoInfo> shareData;
    private int type;
    private long tripId;
    private long trackId;
    private String title;
    private String text;
    private String photo;
    private String code;//分享国家的code
    private String destinationId;
    private String destinationType;
    private String userId;
    
    private Activity _this;
    private AlertDialog shareTripAlertDialog;
    private SNSBind snsBind;
    private WebShareData mWebShareData;
    
    //新浪微博
    private IWeiboShareAPI mWeiboShareAPI = null; 
    private boolean isSelectBitmap;
    private String mSinaAccessToken;
    
    
    /**
     * @author WangJun
     * @since 2012-9-19
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_microblog_activity);
        analysisIntent();
        findView();
        bindEvent();
    }
    
    /**
     * @author WangJun
     * @since 2012-9-20
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    /**
     * @author WangJun
     * @since 2012-9-20
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		snsBind.onActivityResult(requestCode, resultCode, data);
        if(requestCode == code_chose_photo) {
            if(resultCode == RESULT_OK) {
                shareData = data.getParcelableArrayListExtra(ChosePhotoActivity.KEY_RESULT);
                if(shareData.size() > 0) {
                    String url = shareData.get(0).url;
                    if(imageStorage.checkIsExist(url)) {
                        ivPhoto.setImageBitmap(imageStorage.getBitmap(url));
                        isSelectBitmap = true;
                    } else {
                        ivPhoto.setImageResource(R.drawable.edit_waypoint_photo);
                        if(!imageStorage.checkDownloading(url)) {
                            imageStorage.downImage(url, callback, code_down_image);
                        }
                    }
                } else 
                	ivPhoto.setImageResource(R.drawable.edit_waypoint_photo);
            }
        }
        
    }
    
    /**
     * 解析Intent
     * @author WangJun
     * @since 2012-9-20
     */
    private void analysisIntent() {
        Intent intent = getIntent();
        type = intent.getIntExtra(KEY_TYPE, TYPE_TRIP);
        if(type == TYPE_SHARE_WEB_DATA) {
            mWebShareData = intent.getParcelableExtra(KEY_DATA);
            text = mWebShareData.shareText;
            photo = mWebShareData.sharePhoto;
        } else {
            title = intent.getStringExtra(KEY_SHARE_TITLE);
            data = intent.getParcelableArrayListExtra(KEY_DATA);
            tripId = intent.getLongExtra(KEY_TRIP_ID, -1);
            trackId = intent.getLongExtra(KEY_TRACK_ID, -1);
            text = intent.getStringExtra(KEY_TEXT);
            photo = intent.getStringExtra(KEY_PHOTO);
            code = intent.getStringExtra(KEY_CODE);
            destinationId = intent.getStringExtra(KEY_DESTINATION_ID);
            destinationType = intent.getStringExtra(KEY_DESTINATION_TYPE);
        }
    }
    
    /**
     * 初始化控件
     * @author WangJun
     * @since 2012-9-20
     */
    private void findView() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnOK = (ImageButton) findViewById(R.id.btnOK);
        btnOK.setVisibility(View.VISIBLE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        snsBind = new SNSBind(this, true);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        rlActionBar = (RelativeLayout) findViewById(R.id.rl_actionbar);
        ivTitleLogo = (ImageView) findViewById(R.id.ivTitleLogo);
        if(title != null) {
            if(type == TYPE_TRIP) {
                tvTitle.setText(getString(R.string.tv_share, title));
                String [] trip_share_content = getResources().getStringArray(R.array.trip_share_content);
            	int index = (int)(Math.random() * 5) ;
                etContent.setText(String.format(trip_share_content[index], title));
            } else {
                tvTitle.setText(title);
            }
        } else {
        	ivTitleLogo.setVisibility(View.VISIBLE);
        }
        rlActionBar.setPadding(0, 0, 0, 0);
        if(text != null) {
            etContent.setText(text);
        }
        etContent.setSelection(etContent.length());
		if (type != TYPE_TRIP
				&& !(type == TYPE_TRACK && photo != null && !photo.isEmpty())
				&& !(type == TYPE_SHARE_DESTINATION && photo != null && !photo
						.isEmpty())) {
			ivPhoto.setVisibility(View.GONE);
		}
        
        shareData = new ArrayList<PhotoInfo>();
        imageStorage = new ImageStorage(this);
        progressDialog = new ProgressDialog(this);
        netShareManager = new NetShareManager(this);
        userCenter = UserCenter.getUserCenter(this);
        userId = String.valueOf(userCenter.getCurrentUserNetId());
        User user = userCenter.getCurrentUser();
		snsBind.setSinaBind(user.isBindSina);
		snsBind.setTencentBind(user.isBindTencent);
		snsBind.setQzoneBind(user.isBindQzone);
		
		mWeiboShareAPI = mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.SINA_APP_KEY); 
		mWeiboShareAPI.registerApp();  
		
		_this = this;
        if(photo != null && !photo.isEmpty()) {
            imageStorage.downImage(photo, callback, code_down_image);
        }
        getAccessToken();
    }
    
    /**
     * 绑定事件
     * @author WangJun
     * @since 2012-9-20
     */
    private void bindEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                _this.finish();
            }
        });
        
        btnOK.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String text = etContent.getText().toString();
                String shareUrl = "";
                if(snsBind.sina_bind_trip || snsBind.tencent_bind_trip ){//|| snsBind.qzone_bind_trip
	                if(text == null || text.isEmpty()) {
	                    Utility.showToast(_this, R.string.toast_share_text_can_not_null);
	                    return;
	                }
	                StringBuffer tracksId = new StringBuffer();
	                if(shareData != null) {
	                    int count = shareData.size();
	                    for(int i = 0; i < count; i++) {
	                        PhotoInfo photoInfo = shareData.get(i);
							if (i != 0) {
	                            tracksId.append(interval);
	                        }
	                        tracksId.append(photoInfo.tag);
	                    }
	                }
	                String shareTo = "";
	                if(snsBind.sina_bind_trip) {
	                    shareTo += Constant.SNS_SINA;
	                }
	                if(snsBind.tencent_bind_trip) {
	                    if(!shareTo.isEmpty()) {
	                        shareTo += ",";
	                    }
	                    shareTo += Constant.SNS_TENCENT;
	                }
	                if(snsBind.qzone_bind_trip) {
	                    if(!shareTo.isEmpty()) {
	                        shareTo += ",";
	                    }
	                    shareTo += Constant.SNS_QZONE;
	                }
	                Logger.e("debug", "shareto = " + shareTo);
                    switch (type) {
                    case TYPE_TRIP:
                        if ((shareData != null && shareData.size() > 0) || (!snsBind.sina_bind_trip && !snsBind.tencent_bind_trip)) {
                            progressDialog.show();
//                            netShareManager.shareTrip(text, tripId, tracksId.toString(), shareTo, code_share_trip, listener);
                            shareUrl = String.format(SHARE_TRIP_URL, tripId);
                        } else {
                            showTripShareDialog(shareTo, text, tracksId.toString());
                            return;
                        }
                        TCAgent.onEvent(_this, getString(R.string.talking_data_share), getString(R.string.talking_data_share_trip));
                        break;
                    case TYPE_ALL_PASSPORT:
                        progressDialog.show();
//                        netShareManager.sharePassports(text, shareTo, code_share_trip, listener);
                        shareUrl = String.format(SHARE_PASSPORT_AND_ACHIECE, userId);
                        break;
                    case TYPE_MILEAGES:
                        progressDialog.show();
//                        netShareManager.shareTripMileage(text, shareTo, code_share_trip, listener);
                        shareUrl = String.format(SHARE_PASSPORT_AND_ACHIECE, userId);
                        TCAgent.onEvent(_this, getString(R.string.talking_data_share), getString(R.string.talking_data_share_map));
                        break;
                    case TYPE_TRAVEL_ACHIEVEMENTS:
                        progressDialog.show();
//                        netShareManager.shareTravelAchievement(text, shareTo, code_share_trip, listener);
                        shareUrl = String.format(SHARE_PASSPORT_AND_ACHIECE, userId);
                        TCAgent.onEvent(_this, getString(R.string.talking_data_share), getString(R.string.talking_data_share_achievements));
                        break;
                    case TYPE_TRACK:
                        progressDialog.show();
//                        netShareManager.shareTrack(text, trackId, shareTo, code_share_trip, listener);
                        shareUrl = String.format(SHARE_TRACK_URL, tripId,trackId);
                        TCAgent.onEvent(_this, getString(R.string.talking_data_share), getString(R.string.talking_data_share_track));
                        break;
                    case TYPE_SHARE_APP:
                        progressDialog.show();
//                        netShareManager.shareAPP(getString(R.string.app_download_title), text + getString(R.string.app_download_url), shareTo, code_share_trip, listener);
                        text = getString(R.string.app_download_title) + text;
                        shareUrl = getString(R.string.app_download_url);
                        TCAgent.onEvent(_this, getString(R.string.talking_data_share), getString(R.string.talking_data_share_app));
                        break;
                    case TYPE_END_TRIP:
                        progressDialog.show();
//                        String tripUrl = getString(R.string.share_trip_url, tripId);
//                        netShareManager.shareTripMileageInEnd(text + ": " + tripUrl, tripId, shareTo, code_share_trip, listener);
                        shareUrl = String.format(SHARE_TRIP_URL, tripId);
                        TCAgent.onEvent(_this, getString(R.string.talking_data_share), getString(R.string.talking_data_share_end_trip));
                        break;
                    case TYPE_A_PASSPORT:
                        progressDialog.show();
//                        netShareManager.sharePassport(text, code, shareTo, code_share_trip, listener);
                        shareUrl = String.format(SHARE_PASSPORT_AND_ACHIECE, userId);
                        TCAgent.onEvent(_this, getString(R.string.talking_data_share), getString(R.string.talking_data_share_add_passport));
                        break;
                    case TYPE_SHARE_WEB_DATA:
                        progressDialog.show();
                        netShareManager.shareAPP(text, photo, mWebShareData.qqTitle, mWebShareData.qqSummary, mWebShareData.qqImage, shareTo, code_share_trip, listener);
                      //TODO
                        break;
                    case TYPE_SHARE_DESTINATION:
                    	progressDialog.show();
//                    	netShareManager.shareDestination(text, destinationType, destinationId, shareTo, code_share_trip, listener);
                    	shareUrl = String.format(SHARE_DESTINATION, destinationType,destinationId);
                    	break;
                    default:
                        break;
                    }
                    sendSianMessage(text,shareUrl);
                    
	            } else {
	            	Utility.showToast(_this, R.string.toast_bind_sns_faild);	
	            }
            }
        });
        
      
        ivPhoto.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(type == TYPE_TRACK || type == TYPE_SHARE_DESTINATION) {
                    Utility.showPhoto(_this, photo);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(_this, ChosePhotoActivity.class);
                    intent.putParcelableArrayListExtra(ChosePhotoActivity.KEY_DATA, data);
                    intent.putParcelableArrayListExtra(ChosePhotoActivity.KEY_SELECTION, shareData);
                    startActivityForResult(intent, code_chose_photo);
                }
            }
        });
    }
    
    /**
     * 从服务器获取accesstoken
     */
    private void getAccessToken(){
    	netShareManager.getAccessToken(code_get_access_token, listener);
    }
    
    /**
     * 解析accesstoken
     * 构建StatusesAPI 新浪微博api
     */
    private void parseAccessToken(String json){
    	try {
			JSONObject obj = new JSONObject(json);
			if(obj != null){
				JSONObject objSian = obj.optJSONObject("sina");
				if(objSian != null){
					mSinaAccessToken = objSian.optString("access_token");
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /** 
     * 第三方应用发送请求消息到微博，唤起微博分享界面。 
     */  
    private void sendSianMessage(String text,String url) {  
    	 if (LoginActivity.isSinaSSOExisted(_this)) { 
    		 /*微博数据的message对象*/  
    	        WeiboMultiMessage multmess = new WeiboMultiMessage();  
    	        TextObject textobj = new TextObject();  
    	        textobj.text = text;  
    	        multmess.textObject = textobj;  
    	        multmess.imageObject = getImageObj();  
    	        if(!TextUtils.isEmpty(url)) multmess.mediaObject = getWebpageObj(url);
    	        /*微博发送的Request请求*/  
    	        SendMultiMessageToWeiboRequest multRequest = new SendMultiMessageToWeiboRequest();  
    	        multRequest.multiMessage = multmess;  
    	        // 用transaction唯一标识一个请求  
    	        multRequest.transaction = String.valueOf(System.currentTimeMillis());  
    	  
    	        // 3. 发送请求消息到微博，唤起微博分享界面  
    	        mWeiboShareAPI.sendRequest(multRequest);  
    	        progressDialog.cancel();
         }else{  
        	 /* 没有新浪微博客户端*/
         }
    }  
    
    /**
     * 创建多媒体（网页）消息对象。
     * 
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(String url) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = String.valueOf(System.currentTimeMillis());
        mediaObject.title = "";
        mediaObject.description = "";
        // 设置 Bitmap 类型的图片到视频对象里
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.im_breadtrip_share_default);
        mediaObject.setThumbImage(bitmapDrawable.getBitmap());
        mediaObject.actionUrl = url;
        mediaObject.defaultText = "";
        return mediaObject;
    }
    
	/**
	 * 创建图片消息对象。
	 * 
	 * @return 图片消息对象。
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		BitmapDrawable bitmapDrawable = null;
		if(isSelectBitmap && ivPhoto != null && ivPhoto.getVisibility() == View.VISIBLE && ivPhoto.getDrawable() != null){
			bitmapDrawable = (BitmapDrawable) ivPhoto.getDrawable();
		}else{
			// 如果二维码未生成，直接点击分享到微博按钮，则默认分享本app的logo
			bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.im_breadtrip_share_default);
		}
		imageObject.setImageObject(bitmapDrawable.getBitmap());
		return imageObject;
	}
    
    
    
    private void showTripShareDialog(final String shareTo, final String text, final String trackId){
		if (shareTripAlertDialog == null) {
    		shareTripAlertDialog = new BreadTripAlertDialog(_this);
        	shareTripAlertDialog.setMessage(getString(R.string.dialog_share_trip));
        	shareTripAlertDialog.setTitle(R.string.tv_prompt);
        	shareTripAlertDialog.setIcon(0);
        	shareTripAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
					getString(R.string.dialog_btn_need_not),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (shareTripAlertDialog.isShowing()) {
								shareTripAlertDialog.dismiss();
//		                        netShareManager.shareTrip(text, tripId, trackId, shareTo, code_share_trip, listener);
		                        String shareUrl = String.format(SHARE_TRACK_URL, tripId,trackId);
		                        sendSianMessage(text, shareUrl);
							}
						}
					});
        	shareTripAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					getString(R.string.dialog_btn_now_try),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (shareTripAlertDialog.isShowing()) {
								shareTripAlertDialog.dismiss();
							}
						}
					});
		}
		if (!shareTripAlertDialog.isShowing()) {
			shareTripAlertDialog.show();
		}
    }
    
    
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.arg1 == code_no_network) {
                Utility.showToast(_this, R.string.toast_no_network);
            } else if(msg.arg1 == code_down_image) {
                ivPhoto.setImageBitmap((Bitmap) msg.obj);
                isSelectBitmap = true;
            } else if(msg.arg1 == code_get_access_token){
            	
            }
            else {
                progressDialog.cancel();
                if(msg.arg2 == TRUE) {
                    Utility.showToast(_this, R.string.toast_share_success);
                    setResult(RESULT_OK);
                    finish();
                } else {
                	if(msg.obj != null) {
                        Utility.showToast(_this, msg.obj.toString());
                    }
                }
            }
            
        };
    };
    
    private LoadImageCallback callback = new LoadImageCallback() {
        
        @Override
        public void done(Bitmap bitmap, int requestCode) {
            if(bitmap != null) {
                Message msg = new Message();
                msg.arg1 = requestCode;
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onChangeProgress(int progress, int requestCode) {
            
        }
    };
    
    private EventListener listener = new EventListener() {
        
        @Override
        public void onStart(int requestCode) {
            
        }
        
        @Override
        public void onReturnValues(String values, int requestCode, int returnCode) {
            Logger.e("onReturnValues = " + values);
            Message msg = new Message();
            if(returnCode == Constant.HTTP_RETURN_CODE_NO_NETWORK) {
                msg.arg1 = code_no_network;
                handler.sendMessage(msg);
                msg = new Message();
            }
            msg.arg1 = requestCode;
            if(requestCode == code_share_trip) {
                if(returnCode == Constant.HTTP_RETURN_CODE_OK) {
                    msg.arg2 = TRUE;
                } else {
                    msg.arg2 = FALSE;
                    msg.obj = Utility.getErrorString(values);
                }
            }else if(requestCode == code_get_access_token){
            	 if(returnCode == Constant.HTTP_RETURN_CODE_OK) {
                     msg.arg2 = TRUE;
                     parseAccessToken(values);
                 } else {
                     msg.arg2 = FALSE;
                     msg.obj = Utility.getErrorString(values);
                 }
            }
            handler.sendMessage(msg);
        }
        
        @Override
        public void onReturnBytes(byte[] bytes, int requestCode, int returnCode) {
            
        }
    };
    
   
    /**
     * @see {@link Activity#onNewIntent}
     */	
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

	@Override
	public void onResponse(BaseResponse baseResp) {
		Logger.i("test", "errcode "+baseResp.errCode);
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
	}
}
