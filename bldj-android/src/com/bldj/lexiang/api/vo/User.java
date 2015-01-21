package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long userId;//用户userId,全局唯一
	private String username;//用户登录名，全局唯一
	private String mobile;//用户手机号
	private String nickname;//用户昵称
	private String headurl;//头像
	private double lon;//经度
	private double lat;//纬度
	private double accountLeft;//余额
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getAccountLeft() {
		return accountLeft;
	}
	public void setAccountLeft(double accountLeft) {
		this.accountLeft = accountLeft;
	}
	
	
	
}
