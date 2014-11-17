package com.bldj.lexiang.api.handler;

import org.apache.http.HttpStatus;

import android.os.Message;

import com.bldj.lexiang.constant.enums.UserActionType;

/**
 * user相关的handler
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class UserHandler extends BaseHandler {

	private UserActionType uat;
	
	public UserHandler(UserActionType uat){
		this.uat=uat;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
			case HttpStatus.SC_OK:
				//String result = msg.obj.toString();
				if(uat.getUserActionType()==UserActionType.LOGIN.getUserActionType()){ //登陆
					
				}
				break;
			default:
				break;
		}
	}
}
