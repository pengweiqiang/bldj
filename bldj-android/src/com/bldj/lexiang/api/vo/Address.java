package com.bldj.lexiang.api.vo;

import java.io.Serializable;

import com.bldj.lexiang.utils.StringUtils;
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
	private String contactor;//联系人
	
	
	
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
	public String getContactor() {
		return StringUtils.isEmpty(contactor)?"":contactor;
	}
	public void setContactor(String contactor) {
		this.contactor = contactor;
	}
	
	
	
}
