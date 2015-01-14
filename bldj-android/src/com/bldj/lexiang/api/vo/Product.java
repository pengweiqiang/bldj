package com.bldj.lexiang.api.vo;

import java.io.Serializable;
/**
 * 商品vo
 * @author will
 *
 */
public class Product implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7120840589691200591L;
	
	private long id;
	private String name;
	private String picurl;
	private double curPrice;
	private double marketPrice;
	private String oneword;
	private long timeConsume;
	private int sellerNum;
	private String proDetailUrl;
	private String suitsCrowd;
	private long sellerId;
	private String extPrice;//二人套餐@2@236||三人套餐@3#333
	
	
	
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
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public double getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(double curPrice) {
		this.curPrice = curPrice;
	}
	public double getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getOneword() {
		return oneword;
	}
	public void setOneword(String oneword) {
		this.oneword = oneword;
	}
	public long getTimeConsume() {
		return timeConsume;
	}
	public void setTimeConsume(long timeConsume) {
		this.timeConsume = timeConsume;
	}
	public int getSellerNum() {
		return sellerNum;
	}
	public void setSellerNum(int sellerNum) {
		this.sellerNum = sellerNum;
	}
	public String getProDetailUrl() {
		return proDetailUrl;
	}
	public void setProDetailUrl(String proDetailUrl) {
		this.proDetailUrl = proDetailUrl;
	}
	public String getSuitsCrowd() {
		return suitsCrowd;
	}
	public void setSuitsCrowd(String suitsCrowd) {
		this.suitsCrowd = suitsCrowd;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public String getExtPrice() {
		return extPrice;
	}
	public void setExtPrice(String extPrice) {
		this.extPrice = extPrice;
	}
	
	
	
	
}
