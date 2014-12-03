package com.bldj.lexiang.api.vo;

import java.io.Serializable;
/**
 * 美容师vo
 * @author will
 *
 */
public class Seller implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7120840589691200591L;
	
	private long id;
	private String username;
	private String nickname;
	private String area;
	private String recommend;
	private String mobile;
	private String address;
	private String avgPrice;
	private String userGrade;
	private String headurl;
	private int workyear;
	private int dealnumSum;
	private double distance;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(String avgPrice) {
		this.avgPrice = avgPrice;
	}
	public String getUserGrade() {
		return userGrade;
	}
	public void setUserGrade(String userGrade) {
		this.userGrade = userGrade;
	}
	public String getHeadurl() {
		return headurl;
	}
	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}
	public int getWorkyear() {
		return workyear;
	}
	public void setWorkyear(int workyear) {
		this.workyear = workyear;
	}
	public int getDealnumSum() {
		return dealnumSum;
	}
	public void setDealnumSum(int dealnumSum) {
		this.dealnumSum = dealnumSum;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	
	
}
