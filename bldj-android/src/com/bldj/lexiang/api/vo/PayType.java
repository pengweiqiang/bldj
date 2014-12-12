package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class PayType  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889547444319575287L;
	
	private long id;
	private String name;
	private String description;
	private int code;
	private String iconPic;
	private String payId;
	private String payKey;
	private String payNum;
	private String rsaPublicKey;
	private String rsaPrivateKey;
	
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getIconPic() {
		return iconPic;
	}
	public void setIconPic(String iconPic) {
		this.iconPic = iconPic;
	}
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getPayKey() {
		return payKey;
	}
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	public String getPayNum() {
		return payNum;
	}
	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}
	public String getRsaPublicKey() {
		return rsaPublicKey;
	}
	public void setRsaPublicKey(String rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}
	public String getRsaPrivateKey() {
		return rsaPrivateKey;
	}
	public void setRsaPrivateKey(String rsaPrivateKey) {
		this.rsaPrivateKey = rsaPrivateKey;
	}
	
	
	
}
