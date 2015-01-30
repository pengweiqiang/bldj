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
	
	private String mobileRule;//手机号校验正则表达式
	
	private String preferential;//优惠特区的支付界面的顶层文案
	
	private String enterZonePic;//企业专区的banner
	
	private String txtRexSucc;//注册成功的提示

	private String txtShareSucc;//分享朋友圈的提示
	
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
	
	public String getMobileRule() {
		return mobileRule;
	}

	public void setMobileRule(String mobileRule) {
		this.mobileRule = mobileRule;
	}

	public String getPreferential() {
		return preferential;
	}

	public void setPreferential(String preferential) {
		this.preferential = preferential;
	}
	

	public String getEnterZonePic() {
		return enterZonePic;
	}

	public void setEnterZonePic(String enterZonePic) {
		this.enterZonePic = enterZonePic;
	}
	
	public String getTxtRexSucc() {
		return txtRexSucc;
	}

	public void setTxtRexSucc(String txtRexSucc) {
		this.txtRexSucc = txtRexSucc;
	}

	public String getTxtShareSucc() {
		return txtShareSucc;
	}

	public void setTxtShareSucc(String txtShareSucc) {
		this.txtShareSucc = txtShareSucc;
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
		c.setMobileRule("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		c.setPreferential("购买套卡后平台会返回给你序列号且享受优惠折扣，后续凭借序列号消费即可!");
		c.setTxtRexSucc("首次注册成功赠送100优惠券，请去我的优惠券查看使用");
		c.setTxtShareSucc("分享成功赠送50元代金券,请去我的优惠券查看使用");
		return c;
	}
	/**
	 * 获取星级数
	 * @return
	 */
	public static int getStarRuleCount(int currentCount,String starRule){
		int level = 1;
		try{
			String []starRules = starRule.split(";");//10;30;50;100   1颗星  [0，10）   二颗星 [10,30)  三颗星[30,50)
			if(starRules.length>0){
				if(currentCount < Integer.valueOf(starRules[0])){//少于第一数
					level = 1;
					return level;
				}
				if(currentCount > Integer.valueOf(starRules[starRules.length-1])){//大于最后一个数
					level = 5;
					return level;
				}
			}
			for (int i = 0; i < starRules.length; i++) {
				int star = Integer.valueOf(starRules[i]);
				int end = Integer.valueOf(starRules[i+1]);
				if(currentCount >= star && currentCount < end){
					level = i+2;
					break;
				}
				
				
			}
		}catch(Exception e){
			
		}
		return level;
	}
	
}
