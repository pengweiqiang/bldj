package com.bldj.lexiang.api.vo;

import java.io.Serializable;
import java.util.List;
/**
 * 分类vo
 * @author will
 *
 */
public class Category  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889547444319575287L;
	
	private int categoryId;
	private String name;//广告名称
	private String picurl;//广告图地址
	private List<Product> products;
	
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
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	
	
}
