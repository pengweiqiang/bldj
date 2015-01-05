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
	
	private String starRule;//星级判断   x<10 一颗星   10<=x<30 两颗星 左闭右开以此类推  eg:"10;30;50;100"

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
	
	public String getStarRule() {
		return starRule;
	}

	public void setStarRule(String starRule) {
		this.starRule = starRule;
	}
	
	/**
	 * 默认常量配置，防止网络连接失败
	 * @return
	 */
	public static ConfParams getDefaultConfParams(){
		
		ConfParams c = new ConfParams();
		c.setAboutUsUrl("http://www.maidu360.com/home/d");
		c.setServiceNum("4006200606");
		c.setShareAppTxt("对着镜子给自己鞠个躬，对自己说：姑奶奶，您辛苦了。过去的一年，心没少受伤，苦也没少受，必须要叫脉度推拿师傅来给解解乏！2015年一定要对自己好点！咋高兴咋任性咋来。下载地址:http://t.cn/RZcPd90");
		c.setShareProTxt("公司请了脉度推拿师付上门给做推拿调理，效果真心不错。好东西大家来分享,下载地址:http://t.cn/RZcPd90");
		c.setShareSellerTxt("网上搜索找百度，上门推拿找脉度。度娘度哥度姐真给力！下载地址:http://t.cn/RZcPd90");
		c.setRecruitTxt("http://dwz.cn/zGzfl");
		return c;
	}
	
}
