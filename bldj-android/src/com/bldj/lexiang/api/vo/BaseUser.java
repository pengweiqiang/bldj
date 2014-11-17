package com.bldj.lexiang.api.vo;
/**
 * user相关的基类
 * 
 * @author handong
 * @email dong.han1@renren-inc.com
 */
public abstract class BaseUser {

	public String accessToken;
	public String uniqueKey;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	
}
