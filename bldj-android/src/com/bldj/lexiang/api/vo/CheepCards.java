package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class CheepCards  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889547444319575287L;
	
	private long id;
	private String name;
	private String colorVal;
	private String items;//1000元即可享受原价188/次的项目10次;可享受头部、肩部等全套服务项目;可享受免费深度中医理疗按摩5次
	private double price;
	private String createtime;
	private String callbackUrl;
	private String picUrl;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColorVal() {
		return colorVal;
	}
	public void setColorVal(String colorVal) {
		this.colorVal = colorVal;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	
	
}
