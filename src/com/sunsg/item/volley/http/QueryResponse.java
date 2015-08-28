package com.sunsg.item.volley.http;

/**
 * 
 * @author sunsg
 * 
 * @param 
 */
public class QueryResponse<T> {
	private String apiCode;
	private T data;
	private String msg;
	private String action;

	public void setApicode(String apicode) {
		this.apiCode = apicode;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}
	
	public void setMsg(String msg){
		this.msg = msg;
	}
	
	public String getMsg(){
		return msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public void setAction(String action){
		this.action = action;
	}
	
	public String getAction(){
		return action;
	}

}
