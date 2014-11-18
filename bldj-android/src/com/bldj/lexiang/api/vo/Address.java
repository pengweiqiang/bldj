package com.bldj.lexiang.api.vo;

import java.io.Serializable;
/**
 * 用户地址vo
 * @author will
 *
 */
public class Address implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7120840589691200591L;
	private int id;
	private String curLocation;//当前位置
	private String detailAddress;//详细地址
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCurLocation() {
		return curLocation;
	}
	public void setCurLocation(String curLocation) {
		this.curLocation = curLocation;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	
	
	
}
