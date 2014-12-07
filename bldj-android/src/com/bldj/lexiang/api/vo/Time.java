package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class Time implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7682617984069393102L;
	
	private int index;
	private String time;//时间点
	private int status; //0可预约 1被预约
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	
	
}
