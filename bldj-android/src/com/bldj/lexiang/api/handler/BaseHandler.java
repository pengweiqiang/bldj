package com.bldj.lexiang.api.handler;

import org.apache.http.HttpStatus;

import com.bldj.lexiang.utils.NetUtil;

import android.os.Handler;
import android.os.Message;

/**
 * handler基类
 * @author zhuyb
 * @email zhuyongb0@live.com
 */
public class BaseHandler extends Handler {

	private String result;
	
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
			case NetUtil.NET_QUERY_SUCC:
			case HttpStatus.SC_OK:
				result = msg.obj.toString();
				break;
			default:
				break;
		}
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
