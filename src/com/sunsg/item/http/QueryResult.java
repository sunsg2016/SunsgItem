package com.sunsg.item.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QueryResult {
	private String resultString = "";
	private JSONObject resultJSONObject = null;
	private JSONArray resultJSONArray = null;
	private String queryAction = "";
	public QueryResult(String result){
		resultString = result;
		decodeResult(result);
	}
	
	private void decodeResult(String result){
		if(result != null && result.length() >0) {
			try {
				resultJSONObject = new JSONObject(result);
			} catch (JSONException oe) {
				resultJSONObject = null;
				try {
					resultJSONArray = new JSONArray(result);
				} catch (JSONException ae) {
					resultJSONArray = null;
				}
			}
		}
	}
	
	public void setQueryAction(String queryAction){
		this.queryAction = queryAction;
	}
	
	public String getQueryAction(){
		return queryAction;
	}
	
	public JSONObject getJSONobject(){
		return resultJSONObject;
	}
	
	public JSONArray getJSONArray(){
		return resultJSONArray;
	}
	
	public String getResultString(){
		return resultString;
	}
	
	public String getApicode(){
		String code = "";
		if(resultJSONObject != null){
			 code = resultJSONObject.optString("apicode");
			if(code == null || code.length() <= 0)
				code = resultJSONObject.optString("Apicode");
			if(code == null || code.length() <= 0)
				code = resultJSONObject.optString("apiCode");
			if(code == null || code.length() <= 0)
				code = resultJSONObject.optString("ApiCode");
		}
		return code;
	}
	

	/**
	 * 如果返回结构符合JSON格式，可以通过此方法获得对应key的Object
	 * 
	 * @param key 格式： a:b:c ...
	 * @return
	 */
	public Object getObjectWithJsonKey(String key) {
		if(key == null || key.trim().length() <= 0) {
			return resultJSONObject != null ? resultJSONObject : (resultJSONArray != null ? resultJSONArray : null);
		} else {
			String[] keys = key.split(":");
			Object result = null;
			JSONObject obj = resultJSONObject;
			JSONArray arr = resultJSONArray;
			for(String k : keys) {
				String str = "";
				if(obj != null) {
					str = obj.optString(k);
				} else {
					if(arr != null) {
						try {
							int i = Integer.parseInt(k);
							str = arr.optString(i);
						} catch(NumberFormatException e) {
							try {
								throw new Exception("无效的参数");
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
					} else {
						try {
							throw new Exception("无效的JSON格式串");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
				result = str;
				try {
					obj = new JSONObject(str);
					result = obj;
				} catch (JSONException e) {
					try {
						arr = new JSONArray(str);
						result = arr;
					} catch (JSONException e1) {
					}
				}
			}
			return result;
		}
	}
}
