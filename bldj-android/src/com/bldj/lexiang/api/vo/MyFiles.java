package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class MyFiles  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889547444319575287L;
	
	private long id;
	private String dealdate;
	private long userId;
	private String operator;
	private String content;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDealdate() {
		return dealdate;
	}
	public void setDealdate(String dealdate) {
		this.dealdate = dealdate;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
