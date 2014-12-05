package com.bldj.lexiang.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bldj.lexiang.R;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, ApiConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		ToastUtils.showToast(this, "分享"+resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
			builder.show();
		}
		
		 switch (resp.errCode) {  
	        case BaseResp.ErrCode.ERR_OK:  
	        	ToastUtils.showToast(this, "分享成功");
	            //分享成功  
	            break;  
	        case BaseResp.ErrCode.ERR_USER_CANCEL:  
	        	ToastUtils.showToast(this, "分享取消");
	            //分享取消  
	            break;  
	        case BaseResp.ErrCode.ERR_AUTH_DENIED:  
	            //分享拒绝  
	        	ToastUtils.showToast(this, "分享拒绝");
	            break;  
	        }  
	}
	
	
}