package com.bldj.lexiang.api.vo;

import java.io.Serializable;
/**
 * 美容师的评价
 * @author will
 *
 */
public class Evals implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7120840589691200591L;
	
	private int goodEval;//好评
	private int midEval;//中评
	private int badEval; //差评
	
	
	public int getGoodEval() {
		return goodEval;
	}
	public void setGoodEval(int goodEval) {
		this.goodEval = goodEval;
	}
	public int getMidEval() {
		return midEval;
	}
	public void setMidEval(int midEval) {
		this.midEval = midEval;
	}
	public int getBadEval() {
		return badEval;
	}
	public void setBadEval(int badEval) {
		this.badEval = badEval;
	}
	
	
	
}
