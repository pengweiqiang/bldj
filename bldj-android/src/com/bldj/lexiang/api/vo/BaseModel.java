package com.bldj.lexiang.api.vo;

import java.io.Serializable;

import com.bldj.gson.JsonElement;

public class BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8998410069756234518L;
	
	
	private String result;
	
	private JsonElement data;
	
	private String msg;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public JsonElement getData() {
		return data;
	}

	public void setData(JsonElement data) {
		this.data = data;
	}
}
