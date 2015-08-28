package com.sunsg.item.volley.http;

import com.android.volley.Request;

/**
 * 
 * @author sunsg
 * 
 * @param 
 */
public class RequestHandle {
	public static final int PREDO = 0;
	public static final int DOING = 1;
	public static final int DONE = 2;
	public static final int ERROR = 3;
	public static final int APICODE_ERROR = 4;
	public static final int CANCEL = 5;
	
	private int state = PREDO;
	private String completeURL;
	private String action;
	private Request request;

	public RequestHandle() {};

	public RequestHandle(Request request) {
		setRequest(request);
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	
	public void setAction(String action){
		this.action = action;
	}
	
	public String getAction(){
		return action;
	}

	public String getCompleteURL() {
		return completeURL;
	}

	public void setCompleteURL(String completeURL) {
		this.completeURL = completeURL;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RequestHandle))
			return false;
		if (!((RequestHandle) o).getCompleteURL().equals(getCompleteURL()))
			return false;
		else
			return true;
	}
	
	
	/**
	 * 取消
	 */
	public void cancel(){
		state = CANCEL;
		request.cancel();
	}
	
	/**
	 * 是否正在运行
	 * @return
	 */
	public boolean isDoing(){
		return getState() == DOING;
	}
}
