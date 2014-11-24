package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class Ad  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889547444319575287L;
	
	private String name;//广告名称
	private String picurl;//广告图地址
	private String leavetime;//停留时间
	private String actionUrl;//点击跳转地址
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getLeavetime() {
		return leavetime;
	}
	public void setLeavetime(String leavetime) {
		this.leavetime = leavetime;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	
	
}
