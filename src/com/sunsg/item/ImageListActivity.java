package com.sunsg.item;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.breadtrip.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.sunsg.item.bean.Trip;
import com.sunsg.item.bean.Trip.Days;
import com.sunsg.item.bean.Trip.WayPoints;
import com.sunsg.item.http.HttpUtilAnsync;
import com.sunsg.item.http.HttpUtilAnsync.ResponseListener;
import com.sunsg.item.util.AbJsonUtil;
import com.sunsg.item.util.BitmapDownloadTask;
import com.sunsg.item.util.BitmapDownloadTask.OnLoadImageFinish;
import com.sunsg.item.util.BitmapUtils;
import com.sunsg.item.util.ImageStroe;
import com.sunsg.item.util.ImageStroe.LoadBitmapCallBack;
import com.sunsg.item.util.ImageUtility;
import com.sunsg.item.util.Logger;
import com.sunsg.item.util.Tools;

public class ImageListActivity extends Activity {
	private ListView mListView;
	private MyImageAdapter mMyImageAdapter;
	private int mFirstVisibleItem;
	private int mVisibleItemCount;
	private boolean isFirstEnter;
	private Handler mHandler;
	
	private Set<BitmapDownloadTask> taskCollection;
	private Display mDisplay;
	private List<WayPoints> items;
	private ImageStroe mImageStore;
	private HttpUtilAnsync mHttpUtilAnsync;
	private ImageView imageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_listview);
		mDisplay = getWindowManager().getDefaultDisplay();
		isFirstEnter = true;
		mHandler = new Handler();
		taskCollection = new HashSet<BitmapDownloadTask>();
		imageView = (ImageView) findViewById(R.id.iamge1);
		mListView = (ListView) findViewById(R.id.ImageListView_);
		mMyImageAdapter = new MyImageAdapter((MainApplicaion)getApplication());
		mListView.setAdapter(mMyImageAdapter);
		Display dis = getWindowManager().getDefaultDisplay();
		mMyImageAdapter.setWandH(dis.getWidth(), dis.getHeight());
//		mListView.setOnScrollListener(this);
		mImageStore = new ImageStroe(getApplicationContext());
		mImageStore.setMaxImageCoount(2);
		getTrip();
//		mMyImageAdapter.setImtes(items);
		mMyImageAdapter.setItems();
		Picasso.with(getApplicationContext()).load("http://breadtripimages.qiniudn.com/photo_2014_09_14_b0400700dc115b8b655714f320c9f514.jpg?imageView/2/w/1384/h/1384/q/85").resize(300, 300).into(imageView);
//		mHttpUtilAnsync = HttpUtilAnsync.getInstances(getApplicationContext());
		 if (SDK_INT >= GINGERBREAD) {
		        try {
		          Class.forName("com.squareup.okhttp.OkHttpClient");
		          Logger.e("test", "okhttpclient");
		        } catch (ClassNotFoundException ignored) {
		        	 Logger.e("test", "not okhttpclient");
		        }
		    }
		 
		 
		

//		mHttpUtilAnsync.getData(new Trip(), "http://api.breadtrip.com/trips/2386620849/waypoints/", listener);
	}
	
	
	private ResponseListener<Trip> listener = new ResponseListener<Trip>() {

		@Override
		public void onSuccess(Trip t, String url) {
			if(t != null){
				Logger.e("test", "trip = "+t);
			}
		}

		@Override
		public void onFailed(int errorCode, String errorMessage) {
			Logger.e("test", "errorCode = "+errorCode);
			
		}
	};
	
	//构建数据
	private void getTrip(){
		String json = Tools.getFromAssets(this, "trip.txt");
		Trip trip = (Trip) AbJsonUtil.fromJson(json, Trip.class);
		items = new ArrayList<Trip.WayPoints>();
		List<Days> days = trip.days;
		Days day =null;
		for (int i = 0; i < days.size(); i++) {
			day = days.get(i);
			items.addAll(day.waypoints);
		}
		
		Logger.e("test", "trip = "+items.toString());
	}
	
	@Override
	public void finish() {
		super.finish();
		BitmapUtils.getInstance().clear();
		mImageStore.clearData();
	}


//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		// 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
//		if (scrollState == SCROLL_STATE_IDLE) {
//				loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
//		} else {
//			cancelAllTasks();
//		}
//	}


//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//		mFirstVisibleItem = firstVisibleItem;
//		mVisibleItemCount = visibleItemCount;
//		// 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
//		// 因此在这里为首次进入程序开启下载任务。
//		if (isFirstEnter && visibleItemCount > 0) {
//			loadBitmaps(firstVisibleItem, visibleItemCount);
//			isFirstEnter = false;
//		}
//	}
	
	private LoadBitmapCallBack listeners = new LoadBitmapCallBack() {
		
		@Override
		public void onFinish(final Bitmap bitmap, final int requestCode) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					ImageView ivs = (ImageView)mListView.findViewWithTag(requestCode);
					if(ivs != null && bitmap != null){
						ivs.setImageBitmap(bitmap);
					}
				}
			});
		}
		
		@Override
		public void onChangeProgress(int progress, int requestCode) {
			
		}
	};
	
	
	private void loadBitmaps(int firstVisibleItem,int visibleItemCount){
		try {
			for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
				String url = "";
				final Bitmap bit = BitmapUtils.getInstance().getBitmapFromMemoryCache(url);
				if(bit == null){
					
//					BitmapWorkerTask task = new BitmapWorkerTask();
					BitmapDownloadTask task = new BitmapDownloadTask((MainApplicaion)getApplication());
					task.setOnLoadImageFinish(new OnLoadImageFinish() {
						
						@Override
						public void onLoadImageFinish(final Bitmap bitmap, final String url) {
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									//根据tag获得view  加上这个下载完图片之后就可以个view设置图片 体验上很好
									ImageView im = (ImageView) mListView.findViewWithTag(url);
									if(im != null){
										im.getLayoutParams().height = (int)(((double)im.getHeight())/((double)im.getWidth()) * mDisplay.getWidth());
										im.setImageBitmap(bitmap);
									}
								}
							});
							
						}
					});
					taskCollection.add(task);
					task.execute(url);
				}else{
					ImageView im = (ImageView) mListView.findViewWithTag(url);
					if(im != null){
						im.getLayoutParams().height = (int)(((double)im.getHeight())/((double)im.getWidth()) * mDisplay.getWidth());
						im.setImageBitmap(bit);
					}
				}
				
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 取消所有正在下载或等待下载的任务。
	 */
	public void cancelAllTasks() {
		if (taskCollection != null) {
			for (BitmapDownloadTask task : taskCollection) {
				task.cancel(false);
			}
		}
	}
	
	 class MyImageAdapter extends BaseAdapter{
			private MainApplicaion mApp;
			private String[] mItems;
//			private List<WayPoints> wayItmes;
			private int w = 1;
			private int h = 1;
			private int bitH;
			public MyImageAdapter(MainApplicaion app){
				mApp = app;
				mItems = Images.localImagePath;
				bitH = Tools.dp2px(app, 400);
//				wayItmes = new ArrayList<Trip.WayPoints>();
			}
			
			public void setWandH(int w,int h){
				this.w = w;
				this.h = h;
			}
			
			
//			public void setImtes(List<WayPoints> items){
//				this.wayItmes = items;
//				notifyDataSetChanged();
//			}
			
			public void setItems(){
				notifyDataSetChanged();
			}
			
//			public List<WayPoints> getItems(){
//				return wayItmes;
//			}
			@Override
			public int getCount() {
				return mItems.length;
//				return wayItmes.size();
			}

//			@Override
//			public WayPoints getItem(int position) {
//				return wayItmes.get(position);
//			}
			
			@Override
			public String getItem(int position) {
				return mItems[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final ImageItemView views;
				if(convertView == null){
					views = new ImageItemView();
					convertView = View.inflate(mApp, R.layout.activity_image_listview_item, null);
					views.im = (ImageView) convertView.findViewById(R.id.im_IV);
					views.tv = (TextView) convertView.findViewById(R.id.content_TV);
					convertView.setTag(views);
				}else{
					views = (ImageItemView) convertView.getTag();
				}
				
				
				//waypoint 
//				WayPoints wayPoint = wayItmes.get(position);
//				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(wayPoint.photo_info.w,wayPoint.photo_info.h);
//				views.im.setLayoutParams(params);
//				
//				
//				String url = wayPoint.photo_w640;
////				Picasso.with(getApplicationContext()).load(url).centerCrop().resize(wayPoint.photo_info.w, wayPoint.photo_info.h).into(views.im);
//				views.im.setTag(position);
//				if(mImageStore.checkExistRom(url)){
//					Bitmap bit  = mImageStore.getBitmap(url);
//					if(bit != null){
//						views.im.setImageBitmap(bit);
//					}
//				}else{
//					views.im.setImageResource(R.drawable.featured_photo);
//					mImageStore.downBitmap(url, position, listeners);
//				}
				//waypoint 
				
				
				//String
				BitmapFactory.Options opt = new Options();
				opt.inJustDecodeBounds = true;
				String imagePath = mItems[position];
				BitmapFactory.decodeFile(imagePath, opt);
				int showH = w *(opt.outHeight)/opt.outWidth;
				views.im.getLayoutParams().width = w;
				views.im.getLayoutParams().height = showH;
//				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w,showH);
//				views.im.setLayoutParams(params);
				String path = mItems[position];
				views.im.setTag(position);
				if(mImageStore.checkExistRom(path)){
					Bitmap bit = mImageStore.getBitmap(path);
					if(bit != null){
						views.im.setImageBitmap(bit);
					}
				}else{
					views.im.setImageResource(R.drawable.featured_photo);
					mImageStore.loadBitmap(path,w,showH,position, listeners);
				}
				
				//String
				
				//picasso
//				int showH = *bitH/w;
				
//				BitmapFactory.Options opt = new Options();
//				opt.inJustDecodeBounds = true;
//				String imagePath = mItems[position];
//				BitmapFactory.decodeFile(imagePath, opt);
//				int showH = 1;
//				if(opt.outWidth != 0 && opt.outHeight != 0){
//					int degree = ImageUtility.getPhotoOrientation(imagePath);
//					if(degree == 90 || degree == 270){
//						//横向
//						showH = w *(opt.outWidth)/opt.outHeight;
//						Picasso.with(getApplicationContext()).load(new File(mItems[position])).resize(showH,w).centerCrop().into(views.im);
//						//picasso
//					}else{
//						showH = w *(opt.outHeight)/opt.outWidth;
//						Picasso.with(getApplicationContext()).load(new File(mItems[position])).resize(w, showH).centerCrop().into(views.im);
//						//picasso
//					}
//					
//				}
//				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w,showH);
//				views.im.setLayoutParams(params);
				//picasso
				
				
//				String url = mItems[position];
//				views.im.setTag(url);
//				setImage(url,views.im);
				return convertView;
			}
			
			public void setImage(String url,ImageView imageView){
				
				Bitmap bit = BitmapUtils.getInstance().getBitmapFromMemoryCache(url);
				if(bit != null){
					imageView.getLayoutParams().height = (int)(((double)imageView.getHeight())/((double)imageView.getWidth()) * mDisplay.getWidth());
					imageView.setImageBitmap(bit);
				}else{
					imageView.getLayoutParams().height = mDisplay.getWidth();
					imageView.setImageResource(R.drawable.imagelistview_imagebg);
				}
			}
			
			public class ImageItemView{
				public ImageView im;
				public TextView tv;
			}
		}
		


	 public static class Images{
		 //大一点的图
		 public final static String[]imageLargeUrls = new String[]{
//			 "http://img.yiqiwan.kuxun.cn/Uploads/8/3/8/493891/post/400x400_53baa237aa34b.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/8/3/8/493891/post/400x400_53c684d294fc2.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/8/3/8/493891/post/400x400_53c68a040d965.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be47b0a93e2.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be4ec90c6ab.jpg",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be4fcc9f392.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be4fd233937.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be4fd73c5e1.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be54bf27927.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be54c5c4811.jpg",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be54cb4b902.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be58c77feac.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be58ce0ab2b.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be58d4739f8.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be5c5dd2c04.jpg",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be6169a70ac.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be6170388ae.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be61756a343.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be61bc0716c.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be61c1207ad.jpg",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be61c6490aa.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53be622848578.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53bf445cadfa5.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53bf4abfee8c8.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53bf4f732d355.jpg",
//			 
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63c028bc93.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63c0c86443.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63c256e188.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63c69828c7.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63c8715c5c.jpg",
			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dba725eec9.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dba836bb01.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dfbc274140.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dfc0d587fd.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_539d87f0ac0ed.jpg",
	//
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbb3ab3983.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbb8a77ce5.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbbd263785.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbbee88b41.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbc0f242f5.JPG",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbce1d920e.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbcfc4ef90.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbd31907a4.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbd53b566f.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbd78974c2.JPG",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbd9654d15.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbddf93d3a.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dbe024cb4c.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc6344898e.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc67f0f339.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc69fdef7c.JPG",
//			
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc6c3a1abd.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc6dd6bb58.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc6f6448c6.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc71d30f47.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc73bef7e2.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc7a31879f.JPG",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc7c504f49.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc7e62a713.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc7fe6a266.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc81df0741.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc83a5a5f1.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc85f008d4.JPG",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc911987b8.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc92c33b61.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc9456ad83.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc9650ff25.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dc98678c30.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dcac4aef91.JPG",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dcae80643e.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dcb06d3ab1.JPG",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/2/9/f/372523/post/400x400_538dcba0f2242.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63b7346fc4.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63ba0a2071.jpg",
//			 
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63bcd69730.jpg",
//			 "http://img.yiqiwan.kuxun.cn/Uploads/4/5/b/732939/post/400x400_53c63bef1a40f.jpg",

			 
			 
			 
			 
			 "http://breadtripimages.qiniudn.com/photo_2014_09_12_c2c260e2820c63e745a9dfbf2000b13b.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_3ee98f19232165aec8fd8b15b7a77564.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_a3834d7b7e755c0df0400f25d7e3ac9a.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_1c4c3fc50bcf8f70ad3a63c91312db9b.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_12_ed7ee39cf4678e8a3c7f12af47134955.jpg?imageView/2/w/1384/h/1384/q/85",
			 
			 "http://breadtripimages.qiniudn.com/photo_2014_09_12_90699ebfad1e353b5ebd0174cd3c65cf.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_12_52d9c5cd29eda837af40c1ed9718cbaf.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_bc16a980711e3245393be7ec925ff607.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_12_dac88195906ad8870faa78d3f704be35.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_12_3f539236e0c48ff5f14a02b6cb98807b.jpg?imageView/2/w/1384/h/1384/q/85",
			 
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_87342e3d0b99d725ee4bfaef8b9ec613.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_719d72ca2b6b6b2afc211c338f5a2219.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_30c4eba40ca18c621f79fc567f9b69d7.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_701cadea88e02b262005c8f2dd7c18ab.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_0a7f62194dd8c2a6667512246f1abe30.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_26_789482c196f32cf14abd00c23584a32f.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_f1910c75bf00aa4a20d6ce07a878803e.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_d247c5c45f2a2dc5af78b4f08b6a9428.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_983a1d151c62b61c8c1b7d9fb13bd07a.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_3fba8f713b67735e68890f6f3a2090fc.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_3c578ded68bbd93c4b0121bccead28f6.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_df43226a7b02ca65c11fecc1a44380ba.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_4244c51b4f00de7ab750d0c212c27ec9.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_8b44c4ec78d4257a7a3c1d3165bcc80c.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_c0281626d28facbc8e9ebc946f638da3.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_17_65830d0cc9c8d52e92817ef92dd90b1c.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_b97d842ff87917f4e8cde2fa52f1fcf0.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_24d88c1f33d85d7cc7ed7db5a5f9e6b3.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_c2db3da27a25801cb3ef93cd998d7001.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_24851cf07860140201dea2344dd8166e.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_58f519b6e87199dac8d0304be144a2ce.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_4afcf579186ee0583e858f9d00a444e0.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_5e6507989adcc498b8d27946f0417e26.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_1b039849399721f5f5fe31fde54d6828.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_b441883b7d52c25e26fed513b479542e.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_e2486c5a55d8d7fe5e2ad45817896c82.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_3262470d654d31d56b1e0d65cedf3410.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_53fbf95e0d1ac727ee136438b009051d.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_9bed6399c5fac65ca20200f20a28a26a.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_bf1df44974e86c1889d87e7cbec75ced.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_fe148bb25b34e704204e4222bbc127e6.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_f03a1020c9dbfad33520d674757f9fb1.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_13_17a35a99311eb605a588cd9840638903.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_0daabc7a6996e25d8d97039df20695d0.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_a8a9aa421506f6e68abc264792a83e65.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_f171c460c9afc5cd62e8b641b616bee3.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_90fe798bb8cb99c048e74317bc0f10af.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_e0065cdf4f9a0b3450efc0bbf3b0e0a0.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_b4728f3e049b3c9805b60fe61e6aa6cd.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_b0400700dc115b8b655714f320c9f514.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_866dbdd05436f48773b4b1c31ee7a18c.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_ac304b3713c692d9d1b8a5a8caf764a3.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_8cbe454b9c15eb53503360c98094659b.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_d3bca40f5b04d7e3cb3b4afabc6ca260.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_17_16fe28eec722848e429dff5fb623ca1d.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_74b146536ff2377440420a6db8cc7232.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_3b460c71b8718b6474b8a2dc9e4a29bc.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_2862f30fd7a0691f01aa9794943e899f.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_bf5592d0e782681e04f32ab52fe768d8.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_abd7e0081148e9c79d97288a0b120c64.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_d524cf66df03a11a83bcd3f95425ceac.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_3ae672dadaab1df4af14718b6d22659a.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_14_aaffacb15e776afa31da2ff4881b22cc.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_1a1082c3c5f67883fdc8c094150bf460.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_128e1a9b458ec6d941c4eaebc8cf45fa.jpg?imageView/2/w/1384/h/1384/q/85",

			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_bb4e4bab1368b5018202ad8b9e3ed64a.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_c825990b2cfffbb661cc9054942f8f00.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_6cfac3ef22d241d14c771747b4d92d87.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_ade751de5199b731d4375f55869a72cc.jpg?imageView/2/w/1384/h/1384/q/85",
			 "http://breadtripimages.qiniudn.com/photo_2014_09_15_ed472344a94da92ab781f323f58b182b.jpg?imageView/2/w/1384/h/1384/q/85",

			 
		 };
	 
		 public final static String[] imageThumbUrls = new String[] {
				"https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s160-c/A%252520Photographer.jpg",
				"https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s160-c/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
				"https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s160-c/Another%252520Rockaway%252520Sunset.jpg",
				"https://lh3.googleusercontent.com/--L0Km39l5J8/URquXHGcdNI/AAAAAAAAAbs/3ZrSJNrSomQ/s160-c/Antelope%252520Butte.jpg",
				"https://lh6.googleusercontent.com/-8HO-4vIFnlw/URquZnsFgtI/AAAAAAAAAbs/WT8jViTF7vw/s160-c/Antelope%252520Hallway.jpg",
				"https://lh4.googleusercontent.com/-WIuWgVcU3Qw/URqubRVcj4I/AAAAAAAAAbs/YvbwgGjwdIQ/s160-c/Antelope%252520Walls.jpg",
				"https://lh6.googleusercontent.com/-UBmLbPELvoQ/URqucCdv0kI/AAAAAAAAAbs/IdNhr2VQoQs/s160-c/Apre%2525CC%252580s%252520la%252520Pluie.jpg",
				"https://lh3.googleusercontent.com/-s-AFpvgSeew/URquc6dF-JI/AAAAAAAAAbs/Mt3xNGRUd68/s160-c/Backlit%252520Cloud.jpg",
				"https://lh5.googleusercontent.com/-bvmif9a9YOQ/URquea3heHI/AAAAAAAAAbs/rcr6wyeQtAo/s160-c/Bee%252520and%252520Flower.jpg",
				"https://lh5.googleusercontent.com/-n7mdm7I7FGs/URqueT_BT-I/AAAAAAAAAbs/9MYmXlmpSAo/s160-c/Bonzai%252520Rock%252520Sunset.jpg",
				"https://lh6.googleusercontent.com/-4CN4X4t0M1k/URqufPozWzI/AAAAAAAAAbs/8wK41lg1KPs/s160-c/Caterpillar.jpg",
				"https://lh3.googleusercontent.com/-rrFnVC8xQEg/URqufdrLBaI/AAAAAAAAAbs/s69WYy_fl1E/s160-c/Chess.jpg",
				"https://lh5.googleusercontent.com/-WVpRptWH8Yw/URqugh-QmDI/AAAAAAAAAbs/E-MgBgtlUWU/s160-c/Chihuly.jpg",
				"https://lh5.googleusercontent.com/-0BDXkYmckbo/URquhKFW84I/AAAAAAAAAbs/ogQtHCTk2JQ/s160-c/Closed%252520Door.jpg",
				"https://lh3.googleusercontent.com/-PyggXXZRykM/URquh-kVvoI/AAAAAAAAAbs/hFtDwhtrHHQ/s160-c/Colorado%252520River%252520Sunset.jpg",
//				"https://lh3.googleusercontent.com/-ZAs4dNZtALc/URquikvOCWI/AAAAAAAAAbs/DXz4h3dll1Y/s160-c/Colors%252520of%252520Autumn.jpg",
//				"https://lh4.googleusercontent.com/-GztnWEIiMz8/URqukVCU7bI/AAAAAAAAAbs/jo2Hjv6MZ6M/s160-c/Countryside.jpg",
//				"https://lh4.googleusercontent.com/-bEg9EZ9QoiM/URquklz3FGI/AAAAAAAAAbs/UUuv8Ac2BaE/s160-c/Death%252520Valley%252520-%252520Dunes.jpg",
//				"https://lh6.googleusercontent.com/-ijQJ8W68tEE/URqulGkvFEI/AAAAAAAAAbs/zPXvIwi_rFw/s160-c/Delicate%252520Arch.jpg",
//				"https://lh5.googleusercontent.com/-Oh8mMy2ieng/URqullDwehI/AAAAAAAAAbs/TbdeEfsaIZY/s160-c/Despair.jpg",
//				"https://lh5.googleusercontent.com/-gl0y4UiAOlk/URqumC_KjBI/AAAAAAAAAbs/PM1eT7dn4oo/s160-c/Eagle%252520Fall%252520Sunrise.jpg",
//				"https://lh3.googleusercontent.com/-hYYHd2_vXPQ/URqumtJa9eI/AAAAAAAAAbs/wAalXVkbSh0/s160-c/Electric%252520Storm.jpg",
//				"https://lh5.googleusercontent.com/-PyY_yiyjPTo/URqunUOhHFI/AAAAAAAAAbs/azZoULNuJXc/s160-c/False%252520Kiva.jpg",
//				"https://lh6.googleusercontent.com/-PYvLVdvXywk/URqunwd8hfI/AAAAAAAAAbs/qiMwgkFvf6I/s160-c/Fitzgerald%252520Streaks.jpg",
//				"https://lh4.googleusercontent.com/-KIR_UobIIqY/URquoCZ9SlI/AAAAAAAAAbs/Y4d4q8sXu4c/s160-c/Foggy%252520Sunset.jpg",
//				"https://lh6.googleusercontent.com/-9lzOk_OWZH0/URquoo4xYoI/AAAAAAAAAbs/AwgzHtNVCwU/s160-c/Frantic.jpg",
//				"https://lh3.googleusercontent.com/-0X3JNaKaz48/URqupH78wpI/AAAAAAAAAbs/lHXxu_zbH8s/s160-c/Golden%252520Gate%252520Afternoon.jpg",
//				"https://lh6.googleusercontent.com/-95sb5ag7ABc/URqupl95RDI/AAAAAAAAAbs/g73R20iVTRA/s160-c/Golden%252520Gate%252520Fog.jpg",
//				"https://lh3.googleusercontent.com/-JB9v6rtgHhk/URqup21F-zI/AAAAAAAAAbs/64Fb8qMZWXk/s160-c/Golden%252520Grass.jpg",
//				"https://lh4.googleusercontent.com/-EIBGfnuLtII/URquqVHwaRI/AAAAAAAAAbs/FA4McV2u8VE/s160-c/Grand%252520Teton.jpg",
//				"https://lh4.googleusercontent.com/-WoMxZvmN9nY/URquq1v2AoI/AAAAAAAAAbs/grj5uMhL6NA/s160-c/Grass%252520Closeup.jpg",
//				"https://lh3.googleusercontent.com/-6hZiEHXx64Q/URqurxvNdqI/AAAAAAAAAbs/kWMXM3o5OVI/s160-c/Green%252520Grass.jpg",
//				"https://lh5.googleusercontent.com/-6LVb9OXtQ60/URquteBFuKI/AAAAAAAAAbs/4F4kRgecwFs/s160-c/Hanging%252520Leaf.jpg",
//				"https://lh4.googleusercontent.com/-zAvf__52ONk/URqutT_IuxI/AAAAAAAAAbs/D_bcuc0thoU/s160-c/Highway%2525201.jpg",
//				"https://lh6.googleusercontent.com/-H4SrUg615rA/URquuL27fXI/AAAAAAAAAbs/4aEqJfiMsOU/s160-c/Horseshoe%252520Bend%252520Sunset.jpg",
//				"https://lh4.googleusercontent.com/-JhFi4fb_Pqw/URquuX-QXbI/AAAAAAAAAbs/IXpYUxuweYM/s160-c/Horseshoe%252520Bend.jpg",
//				"https://lh5.googleusercontent.com/-UGgssvFRJ7g/URquueyJzGI/AAAAAAAAAbs/yYIBlLT0toM/s160-c/Into%252520the%252520Blue.jpg",
//				"https://lh3.googleusercontent.com/-CH7KoupI7uI/URquu0FF__I/AAAAAAAAAbs/R7GDmI7v_G0/s160-c/Jelly%252520Fish%2525202.jpg",
//				"https://lh4.googleusercontent.com/-pwuuw6yhg8U/URquvPxR3FI/AAAAAAAAAbs/VNGk6f-tsGE/s160-c/Jelly%252520Fish%2525203.jpg",
//				"https://lh5.googleusercontent.com/-GoUQVw1fnFw/URquv6xbC0I/AAAAAAAAAbs/zEUVTQQ43Zc/s160-c/Kauai.jpg",
//				"https://lh6.googleusercontent.com/-8QdYYQEpYjw/URquwvdh88I/AAAAAAAAAbs/cktDy-ysfHo/s160-c/Kyoto%252520Sunset.jpg",
//				"https://lh4.googleusercontent.com/-vPeekyDjOE0/URquwzJ28qI/AAAAAAAAAbs/qxcyXULsZrg/s160-c/Lake%252520Tahoe%252520Colors.jpg",
//				"https://lh4.googleusercontent.com/-xBPxWpD4yxU/URquxWHk8AI/AAAAAAAAAbs/ARDPeDYPiMY/s160-c/Lava%252520from%252520the%252520Sky.jpg",
//				"https://lh3.googleusercontent.com/-897VXrJB6RE/URquxxxd-5I/AAAAAAAAAbs/j-Cz4T4YvIw/s160-c/Leica%25252050mm%252520Summilux.jpg",
//				"https://lh5.googleusercontent.com/-qSJ4D4iXzGo/URquyDWiJ1I/AAAAAAAAAbs/k2pBXeWehOA/s160-c/Leica%25252050mm%252520Summilux.jpg",
//				"https://lh6.googleusercontent.com/-dwlPg83vzLg/URquylTVuFI/AAAAAAAAAbs/G6SyQ8b4YsI/s160-c/Leica%252520M8%252520%252528Front%252529.jpg",
//				"https://lh3.googleusercontent.com/-R3_EYAyJvfk/URquzQBv8eI/AAAAAAAAAbs/b9xhpUM3pEI/s160-c/Light%252520to%252520Sand.jpg",
//				"https://lh3.googleusercontent.com/-fHY5h67QPi0/URqu0Cp4J1I/AAAAAAAAAbs/0lG6m94Z6vM/s160-c/Little%252520Bit%252520of%252520Paradise.jpg",
//				"https://lh5.googleusercontent.com/-TzF_LwrCnRM/URqu0RddPOI/AAAAAAAAAbs/gaj2dLiuX0s/s160-c/Lone%252520Pine%252520Sunset.jpg",
//				"https://lh3.googleusercontent.com/-4HdpJ4_DXU4/URqu046dJ9I/AAAAAAAAAbs/eBOodtk2_uk/s160-c/Lonely%252520Rock.jpg",
//				"https://lh6.googleusercontent.com/-erbF--z-W4s/URqu1ajSLkI/AAAAAAAAAbs/xjDCDO1INzM/s160-c/Longue%252520Vue.jpg",
//				"https://lh6.googleusercontent.com/-0CXJRdJaqvc/URqu1opNZNI/AAAAAAAAAbs/PFB2oPUU7Lk/s160-c/Look%252520Me%252520in%252520the%252520Eye.jpg",
//				"https://lh3.googleusercontent.com/-D_5lNxnDN6g/URqu2Tk7HVI/AAAAAAAAAbs/p0ddca9W__Y/s160-c/Lost%252520in%252520a%252520Field.jpg",
//				"https://lh6.googleusercontent.com/-flsqwMrIk2Q/URqu24PcmjI/AAAAAAAAAbs/5ocIH85XofM/s160-c/Marshall%252520Beach%252520Sunset.jpg",
//				"https://lh4.googleusercontent.com/-Y4lgryEVTmU/URqu28kG3gI/AAAAAAAAAbs/OjXpekqtbJ4/s160-c/Mono%252520Lake%252520Blue.jpg",
//				"https://lh4.googleusercontent.com/-AaHAJPmcGYA/URqu3PIldHI/AAAAAAAAAbs/lcTqk1SIcRs/s160-c/Monument%252520Valley%252520Overlook.jpg",
//				"https://lh4.googleusercontent.com/-vKxfdQ83dQA/URqu31Yq_BI/AAAAAAAAAbs/OUoGk_2AyfM/s160-c/Moving%252520Rock.jpg",
//				"https://lh5.googleusercontent.com/-CG62QiPpWXg/URqu4ia4vRI/AAAAAAAAAbs/0YOdqLAlcAc/s160-c/Napali%252520Coast.jpg",
//				"https://lh6.googleusercontent.com/-wdGrP5PMmJQ/URqu5PZvn7I/AAAAAAAAAbs/m0abEcdPXe4/s160-c/One%252520Wheel.jpg",
//				"https://lh6.googleusercontent.com/-6WS5DoCGuOA/URqu5qx1UgI/AAAAAAAAAbs/giMw2ixPvrY/s160-c/Open%252520Sky.jpg",
//				"https://lh6.googleusercontent.com/-u8EHKj8G8GQ/URqu55sM6yI/AAAAAAAAAbs/lIXX_GlTdmI/s160-c/Orange%252520Sunset.jpg",
//				"https://lh6.googleusercontent.com/-74Z5qj4bTDE/URqu6LSrJrI/AAAAAAAAAbs/XzmVkw90szQ/s160-c/Orchid.jpg",
//				"https://lh6.googleusercontent.com/-lEQE4h6TePE/URqu6t_lSkI/AAAAAAAAAbs/zvGYKOea_qY/s160-c/Over%252520there.jpg",
//				"https://lh5.googleusercontent.com/-cauH-53JH2M/URqu66v_USI/AAAAAAAAAbs/EucwwqclfKQ/s160-c/Plumes.jpg",
//				"https://lh3.googleusercontent.com/-eDLT2jHDoy4/URqu7axzkAI/AAAAAAAAAbs/iVZE-xJ7lZs/s160-c/Rainbokeh.jpg",
//				"https://lh5.googleusercontent.com/-j1NLqEFIyco/URqu8L1CGcI/AAAAAAAAAbs/aqZkgX66zlI/s160-c/Rainbow.jpg",
//				"https://lh5.googleusercontent.com/-DRnqmK0t4VU/URqu8XYN9yI/AAAAAAAAAbs/LgvF_592WLU/s160-c/Rice%252520Fields.jpg",
//				"https://lh3.googleusercontent.com/-hwh1v3EOGcQ/URqu8qOaKwI/AAAAAAAAAbs/IljRJRnbJGw/s160-c/Rockaway%252520Fire%252520Sky.jpg",
//				"https://lh5.googleusercontent.com/-wjV6FQk7tlk/URqu9jCQ8sI/AAAAAAAAAbs/RyYUpdo-c9o/s160-c/Rockaway%252520Flow.jpg",
//				"https://lh6.googleusercontent.com/-6cAXNfo7D20/URqu-BdzgPI/AAAAAAAAAbs/OmsYllzJqwo/s160-c/Rockaway%252520Sunset%252520Sky.jpg",
//				"https://lh3.googleusercontent.com/-sl8fpGPS-RE/URqu_BOkfgI/AAAAAAAAAbs/Dg2Fv-JxOeg/s160-c/Russian%252520Ridge%252520Sunset.jpg",
//				"https://lh6.googleusercontent.com/-gVtY36mMBIg/URqu_q91lkI/AAAAAAAAAbs/3CiFMBcy5MA/s160-c/Rust%252520Knot.jpg",
//				"https://lh6.googleusercontent.com/-GHeImuHqJBE/URqu_FKfVLI/AAAAAAAAAbs/axuEJeqam7Q/s160-c/Sailing%252520Stones.jpg",
//				"https://lh3.googleusercontent.com/-hBbYZjTOwGc/URqu_ycpIrI/AAAAAAAAAbs/nAdJUXnGJYE/s160-c/Seahorse.jpg",
//				"https://lh3.googleusercontent.com/-Iwi6-i6IexY/URqvAYZHsVI/AAAAAAAAAbs/5ETWl4qXsFE/s160-c/Shinjuku%252520Street.jpg",
//				"https://lh6.googleusercontent.com/-amhnySTM_MY/URqvAlb5KoI/AAAAAAAAAbs/pFCFgzlKsn0/s160-c/Sierra%252520Heavens.jpg",
//				"https://lh5.googleusercontent.com/-dJgjepFrYSo/URqvBVJZrAI/AAAAAAAAAbs/v-F5QWpYO6s/s160-c/Sierra%252520Sunset.jpg",
//				"https://lh4.googleusercontent.com/-Z4zGiC5nWdc/URqvBdEwivI/AAAAAAAAAbs/ZRZR1VJ84QA/s160-c/Sin%252520Lights.jpg",
//				"https://lh4.googleusercontent.com/-_0cYiWW8ccY/URqvBz3iM4I/AAAAAAAAAbs/9N_Wq8MhLTY/s160-c/Starry%252520Lake.jpg",
//				"https://lh3.googleusercontent.com/-A9LMoRyuQUA/URqvCYx_JoI/AAAAAAAAAbs/s7sde1Bz9cI/s160-c/Starry%252520Night.jpg",
//				"https://lh3.googleusercontent.com/-KtLJ3k858eY/URqvC_2h_bI/AAAAAAAAAbs/zzEBImwDA_g/s160-c/Stream.jpg",
//				"https://lh5.googleusercontent.com/-dFB7Lad6RcA/URqvDUftwWI/AAAAAAAAAbs/BrhoUtXTN7o/s160-c/Strip%252520Sunset.jpg",
//				"https://lh5.googleusercontent.com/-at6apgFiN20/URqvDyffUZI/AAAAAAAAAbs/clABCx171bE/s160-c/Sunset%252520Hills.jpg",
//				"https://lh4.googleusercontent.com/-7-EHhtQthII/URqvEYTk4vI/AAAAAAAAAbs/QSJZoB3YjVg/s160-c/Tenaya%252520Lake%2525202.jpg",
//				"https://lh6.googleusercontent.com/-8MrjV_a-Pok/URqvFC5repI/AAAAAAAAAbs/9inKTg9fbCE/s160-c/Tenaya%252520Lake.jpg",
//				"https://lh5.googleusercontent.com/-B1HW-z4zwao/URqvFWYRwUI/AAAAAAAAAbs/8Peli53Bs8I/s160-c/The%252520Cave%252520BW.jpg",
//				"https://lh3.googleusercontent.com/-PO4E-xZKAnQ/URqvGRqjYkI/AAAAAAAAAbs/42nyADFsXag/s160-c/The%252520Fisherman.jpg",
//				"https://lh4.googleusercontent.com/-iLyZlzfdy7s/URqvG0YScdI/AAAAAAAAAbs/1J9eDKmkXtk/s160-c/The%252520Night%252520is%252520Coming.jpg",
//				"https://lh6.googleusercontent.com/-G-k7YkkUco0/URqvHhah6fI/AAAAAAAAAbs/_taQQG7t0vo/s160-c/The%252520Road.jpg",
//				"https://lh6.googleusercontent.com/-h-ALJt7kSus/URqvIThqYfI/AAAAAAAAAbs/ejiv35olWS8/s160-c/Tokyo%252520Heights.jpg",
//				"https://lh5.googleusercontent.com/-Hy9k-TbS7xg/URqvIjQMOxI/AAAAAAAAAbs/RSpmmOATSkg/s160-c/Tokyo%252520Highway.jpg",
//				"https://lh6.googleusercontent.com/-83oOvMb4OZs/URqvJL0T7lI/AAAAAAAAAbs/c5TECZ6RONM/s160-c/Tokyo%252520Smog.jpg",
//				"https://lh3.googleusercontent.com/-FB-jfgREEfI/URqvJI3EXAI/AAAAAAAAAbs/XfyweiRF4v8/s160-c/Tufa%252520at%252520Night.jpg",
//				"https://lh4.googleusercontent.com/-vngKD5Z1U8w/URqvJUCEgPI/AAAAAAAAAbs/ulxCMVcU6EU/s160-c/Valley%252520Sunset.jpg",
//				"https://lh6.googleusercontent.com/-DOz5I2E2oMQ/URqvKMND1kI/AAAAAAAAAbs/Iqf0IsInleo/s160-c/Windmill%252520Sunrise.jpg",
//				"https://lh5.googleusercontent.com/-biyiyWcJ9MU/URqvKculiAI/AAAAAAAAAbs/jyPsCplJOpE/s160-c/Windmill.jpg",
//				"https://lh4.googleusercontent.com/-PDT167_xRdA/URqvK36mLcI/AAAAAAAAAbs/oi2ik9QseMI/s160-c/Windmills.jpg",
//				"https://lh5.googleusercontent.com/-kI_QdYx7VlU/URqvLXCB6gI/AAAAAAAAAbs/N31vlZ6u89o/s160-c/Yet%252520Another%252520Rockaway%252520Sunset.jpg",
//				"https://lh4.googleusercontent.com/-e9NHZ5k5MSs/URqvMIBZjtI/AAAAAAAAAbs/1fV810rDNfQ/s160-c/Yosemite%252520Tree.jpg", 
				};
		 
		 public final static String[] localImagePath={
			 "/storage/emulated/0/DCIM/Camera/20150604_130336.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150604_130343.jpg",
			 "/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-06-02-18-59-31.png",
			 "/storage/emulated/0/DCIM/Camera/20150529_184844.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150529_185027.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150529_185041.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150529_185058.jpg",
			 "/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-05-29-12-28-53.png",
			 " /storage/emulated/0/Pictures/Screenshots/Screenshot_2015-05-24-22-49-23.png",
			 "/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-05-24-22-58-38.png", 
			 "/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-05-24-22-59-15.png",
			 "/storage/emulated/0/Pictures/Screenshots/Screenshot_2015-05-12-21-47-47.png",
			 "/storage/emulated/0/DCIM/面包旅行/1432721052656.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150503_170408.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150503_170434.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150503_171007.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150503_171032.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150425_082759.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150425_082818.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150425_082831.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_132450.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_132537.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_132556.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_132601.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_132620.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_133852.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_134003.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_134030.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_134541.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_134557.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_134619.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_134656.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_134707.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_134742.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_134905.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_134934.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_134950.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135015.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135039.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135056.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135107.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135115.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135123.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135135.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135254.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135301.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135307.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135321.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135332.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135400.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135413.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_135427.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135434.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135446.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_135449.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_141606.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_141639.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150411_141650.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150411_141717.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150410_081010.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150410_081018.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170255.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150406_170300.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170302.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170335.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150406_170343.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170345.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150406_170411.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170412.jpg", 
			 "/storage/emulated/0/DCIM/Camera/20150406_170852.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170853.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170855.jpg",
			 "/storage/emulated/0/DCIM/Camera/20150406_170858.jpg"

		 };
	}
	
}
	

	
	
	
