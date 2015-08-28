package com.sunsg.item.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.sunsg.item.util.HttpUtil;
import com.sunsg.item.util.Tools;

public class HttpRequest {
	private static String ROOT_URL = "http://api.m.kuxun.cn";
	private static final  int TIMEOUT = 30000;
	private static HttpParams httpParames = null;
	private static HttpRequest ME = null;
	private static OnQueryListener onQueryListener; 
	public HttpRequest(){
		
	}
	
	private static final int HttpAsyncTask_Count= 20;
	public static List<HttpAsyncTask> mHttpAsyncTaskList;
	public static List<HttpAsyncTask> mHttpAsyncTaskCanceledlist;
	public static List<Query> querys;
	public static void init(){
		if(ME == null){
			ME = new HttpRequest();
			httpParames = new BasicHttpParams();
			// 超时时间
			HttpConnectionParams.setConnectionTimeout(httpParames, TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParames, TIMEOUT);
			mHttpAsyncTaskList = new ArrayList<HttpRequest.HttpAsyncTask>();
			mHttpAsyncTaskCanceledlist = new ArrayList<HttpRequest.HttpAsyncTask>();
			querys = new ArrayList<HttpRequest.Query>();
		}
	}
	
	
	/**
	 * 一般ͨget
	 * @param url
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String httpGet(String url) throws IllegalStateException, IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient(httpParames);
		HttpResponse httpResponse = client.execute(httpGet);
		
		return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
	}
	
	/**
	 * gzip get
	 * @param url
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String httpGetForGzip(String url) throws IllegalStateException, IOException {
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Accept-Encoding", "gzip");
		HttpClient client = new DefaultHttpClient(httpParames);
		HttpResponse httpResponse = client.execute(httpGet);
		return HttpUtil.httpEntityToString(httpResponse.getEntity());
	}
	
	/**
	 * http post 请求
	 * @param map 
	 * @param url
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String httpPost(List<NameValuePair> nameValuePairs,String url) throws IllegalStateException, IOException{
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
		HttpClient client = new DefaultHttpClient(httpParames);
		HttpResponse httpResponse = client.execute(post);
		return HttpUtil.httpEntityToString(httpResponse.getEntity());
	}
	
	//设置query监听器
	public static void setOnQueryListener(OnQueryListener onQueryListeners){
		onQueryListener = onQueryListeners;
	}
	

	//添加query并启动查询task
	public static void startQuery(Query query){
		init();
		if(query != null){
			removeSameQueryTask(query.getQueryAction());
			querys.add(query);
			checkQuerys();
		}
	}
	
	//检查query是否可以立即执行
	private static void checkQuerys(){
		Query query = getFirstQuery();
		if(query != null){
			//查询task队列和已经取消的了的查询task队列之和 小于 规定的 HttpAsyncTask_Count 则查询task执行否则放在 查询task队列的第一的位置
			if(mHttpAsyncTaskList.size() + mHttpAsyncTaskCanceledlist.size() < HttpAsyncTask_Count){
				mHttpAsyncTaskList.add(new HttpAsyncTask(query));
				
			}else{
				if(!query.isCancel)intertQueryAtFirst(query);
			}
		}
		//debug
		Log.i("test", "总的querytask ＝ "+ (mHttpAsyncTaskList.size() + mHttpAsyncTaskCanceledlist.size()) +" 查询队列 ＝ "+mHttpAsyncTaskList.size()+" 取消的查询队列 ＝ "+mHttpAsyncTaskCanceledlist.size());
	
	}
	
	//获得队列中第一个query 并且删除第一个query
	private static Query getFirstQuery(){
		if(querys.size() >0) {
			Query q = querys.get(0);
			querys.remove(0);
			return q;
		}
		else return null;
	}
	
	//把query插入到队列的第一个位置
	private static void intertQueryAtFirst(Query query){
		if(query != null){
			int index = -1;
			for(Query q:querys){
				if(q.getQueryAction() == query.getQueryAction()){
					index = 1;
				}
			}
			if(index != 1)querys.add(0, query);
		}
	}
	
	//把和 queryAction相同的task移到 mHttpAsyncTaskCanceledlist的队列
	private static void removeSameQueryTask(String queryAction){
		if(!Tools.isEmpty(queryAction)){
			//debug
			Log.i("test", "mHttpAsyncTaskList.size() = "+mHttpAsyncTaskList.size());
			for(HttpAsyncTask task : mHttpAsyncTaskList){
				if(queryAction.equals(task.getQueryAction())){
					 task.cancelQuery();
					 mHttpAsyncTaskList.remove(task);
					 mHttpAsyncTaskCanceledlist.add(task);
					 break;
				}
			}
		}
	}
	
	//查询task执行完之后取消
	public static void removeHttpQueryTask(HttpAsyncTask httpQueryTask){
		if(httpQueryTask != null){
			mHttpAsyncTaskList.remove(httpQueryTask);
			mHttpAsyncTaskCanceledlist.remove(httpQueryTask);
		}
	}
	
	
	//get 请求
	public static String getWork(Query query)throws Exception{
		String result = "";
		result = httpGetForGzip(query.getUrl());
		return result;
		
	}
	
	//post 请求
	public static String postWork(Query query) throws Exception{
		String result = "";
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Iterator<String> it = query.getMapNameValuePair().keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			String value = query.getMapNameValuePair().get(name);
			nameValuePairs.add(new BasicNameValuePair(name, value));
		}
		
		result = httpPost(nameValuePairs, query.getUrl());
		return result;
	}
	
	
	/**
	 *    HttpAsynctTask  mHttpAsynctTask = new HttpAsynctTask();
	 *                                 mHttpAsynctTask.execute("url"); "url" doInBackground 参数
	 * @author sunsg
	 *
	 */
	public static class HttpAsyncTask extends AsyncTask<String,Integer,String>{
		private boolean isRun;
		private Query query;
		public HttpAsyncTask(Query query){
			this.query = query;
			execute();
		}
		
		public boolean isRun(){
			return isRun;
		}
		
		public String getQueryAction(){
			if(query != null) return query.getQueryAction();
			return "";
		}
		
		public void cancelQuery(){
			if(query != null){
				query.setCancle(true);
//				cancel(true);
			}
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isRun = true;
			if(onQueryListener != null) onQueryListener.onQueryStart(getQueryAction());
		}
		
		@Override
		protected String doInBackground(String... params) {
			String queryResult = "";
			if(query != null && !query.getCancel()){
				try {
					//post 查询
					if(query.isPost){
						
						Log.i("test", "[****************查询  方式 post action ===================== "+query.getQueryAction() +"] url = "+query.getUrl());
						Iterator<String> it = query.getMapNameValuePair().keySet().iterator();
						while (it.hasNext()) {
							String name = it.next();
							String value = query.getMapNameValuePair().get(name);
							Log.i("test", "post name = "+name +" value = "+value);
						}
						queryResult = HttpRequest.postWork(query);
					}
					//get 查询
					else{
						Log.i("test", "[****************查询 方式 get action ===================== "+query.getQueryAction() +"] url = "+query.getUrl());
						queryResult = HttpRequest.getWork(query);
						
					}
				} catch (Exception e) {
					//查询错误
					HttpRequest.removeSameQueryTask(query.getQueryAction());
				}
				
			}
			//第一个参数
			return queryResult;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//第三个参数
			isRun = false;
			//返回结果
			if(!query.getCancel()){
				QueryResult queryResult = new  QueryResult(result);
				queryResult.setQueryAction(getQueryAction());
				if(onQueryListener != null) onQueryListener.onQueryComplete(queryResult);
				//debug
				Log.i("test", "[****************返回结果 action ===================== "+query.getQueryAction()+"] result ＝ "+result);
			}else {
				//任务已经取消 返回空字符串
				QueryResult queryResult = new  QueryResult("");
				queryResult.setQueryAction(getQueryAction());
				if(onQueryListener != null) onQueryListener.onQueryComplete(queryResult);
				//debug
				Log.i("test", "[****************返回结果 action ===================== "+query.getQueryAction()+"] result ＝ "+" 此查询由于网络或者手动取消查询 已经取消 不做处理");
			}
			
			HttpRequest.removeHttpQueryTask(this);
			HttpRequest.checkQuerys();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			//第二个参数
			super.onProgressUpdate(values);
		}
		
	}
	
	public static class Query{
		private String url;
		private String queryActon;
		private boolean isPost;
		private boolean isCancel;
		private Map<String,String> mapParams;
		private Map<String,String> mapNameValuePairs;
		
		public Query(){
			url = "";
			isPost = false;
			isCancel = false;
			mapNameValuePairs = new HashMap();
			mapParams = new HashMap();
		}
		
		public Query(String method,String queryAction,Map<String,String> mapParams,boolean isPost,Map<String, String> mapNameValuePair){
			this.url = HttpRequest.ROOT_URL +"/"+method;
			this.mapParams = mapParams;
			buildCompleteUrl();
			this.queryActon = queryAction;
			this.isPost = isPost;
			this.mapNameValuePairs = mapNameValuePair;
		}
		
		//构造url
		private void buildCompleteUrl(){
			if(mapParams != null){
				buildCompleteUrl(mapParams);
			}
		}
		public void buildCompleteUrl(Map<String,String> mapParams){
			StringBuffer buffer = new StringBuffer(url);
			if(mapParams != null){
				Iterator<String> ite = mapParams.keySet().iterator();
				String key = ite.next();
				String value;
				try {
					value = URLEncoder.encode(mapParams.get(key), HTTP.UTF_8);
					//添加第一个参数
					if(ite.hasNext()) buffer.append("?").append(key).append("=").append(value);
					//添加第一个以后的参数
					while (ite.hasNext()) {
						key = ite.next();
						value = URLEncoder.encode(mapParams.get(key), HTTP.UTF_8);
						buffer.append("&").append(key).append("=").append(value);
					}
					this.url = buffer.toString();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public void setUrl(String url){
			this.url = url;
		}
		public String getUrl(){
			return url;
		}
		
		public void setQueryAction(String queryAction){
			this.queryActon = queryAction;
		}
		public String getQueryAction(){
			return queryActon;
		}
		
		public void setPost(boolean isPost){
			this.isPost = isPost;
		}
		
		public boolean getIsPost(){
			return isPost;
		}
		
		public boolean getCancel(){
			return isCancel;
		}
		
		public void setCancle(boolean isCancle){
			this.isCancel = isCancle;
		}
		
		public void setMapNameValuePair(Map<String, String> map){
			this.mapNameValuePairs = map;
		}
		
		public Map<String,String> getMapNameValuePair(){
			return mapNameValuePairs;
		}
	}
	
	public interface OnQueryListener{
		public void onQueryStart(String queryAction);
		public void onQueryComplete(QueryResult queryResult);
	}
}
