package com.bldj.lexiang.api.vo;

import java.io.Serializable;
/**
 * 
 * @author will
 *
 */
public class Account  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889547444319575287L;
	
	private long userId;
	private double accountLeft;
	private String mobile;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public double getAccountLeft() {
		return accountLeft;
	}
	public void setAccountLeft(double accountLeft) {
		this.accountLeft = accountLeft;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}
