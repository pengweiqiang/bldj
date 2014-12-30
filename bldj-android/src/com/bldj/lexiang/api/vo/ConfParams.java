package com.bldj.lexiang.api.vo;

import java.io.Serializable;

public class ConfParams  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7889547444319575287L;
	
	private String aboutUsUrl;//关于我们的地址
	
	private String shareAppTxt;//分享app文案
	
	private String shareProTxt;//分享产品文案
	
	private String shareSellerTxt;//分享推拿师文案
	
	private String recruitTxt;//推拿师招聘文案
	
	private String serviceNum;//客服电话
	
	private String serviceEmail;//客服邮件

	public String getAboutUsUrl() {
		return aboutUsUrl;
	}

	public void setAboutUsUrl(String aboutUsUrl) {
		this.aboutUsUrl = aboutUsUrl;
	}

	public String getShareAppTxt() {
		return shareAppTxt;
	}

	public void setShareAppTxt(String shareAppTxt) {
		this.shareAppTxt = shareAppTxt;
	}

	public String getShareProTxt() {
		return shareProTxt;
	}

	public void setShareProTxt(String shareProTxt) {
		this.shareProTxt = shareProTxt;
	}

	public String getShareSellerTxt() {
		return shareSellerTxt;
	}

	public void setShareSellerTxt(String shareSellerTxt) {
		this.shareSellerTxt = shareSellerTxt;
	}

	public String getRecruitTxt() {
		return recruitTxt;
	}

	public void setRecruitTxt(String recruitTxt) {
		this.recruitTxt = recruitTxt;
	}

	public String getServiceNum() {
		return serviceNum;
	}

	public void setServiceNum(String serviceNum) {
		this.serviceNum = serviceNum;
	}

	public String getServiceEmail() {
		return serviceEmail;
	}

	public void setServiceEmail(String serviceEmail) {
		this.serviceEmail = serviceEmail;
	}
	
	
}
