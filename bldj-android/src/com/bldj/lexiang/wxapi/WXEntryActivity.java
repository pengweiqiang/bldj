package com.bldj.lexiang.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.ui.BaseActivity;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{

	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shared);
		  // 通过WXAPIFactory工厂，获取IWXAPI的实例
    	api = WXAPIFactory.createWXAPI(this, Constant.appID, false);
    	
    	
    	api.handleIntent(getIntent(), this);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	// 微信发送请求到第三方应用时，会回调到该方法
		@Override
		public void onReq(BaseReq req) {
			ToastUtils.showToast(mContext, "wx:::"+req.getType());
			switch (req.getType()) {
			
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				goToGetMsg();		
				
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				goToShowMsg((ShowMessageFromWX.Req) req);
				break;
			default:
				break;
			}
		}

		// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
		@Override
		public void onResp(BaseResp resp) {
			String result = "";
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				if(MyApplication.getInstance().type==1){
					shared_addCode();
				}
				result = "分享成功";
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "分享取消";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "分享拒绝";
				break;
			default:
				result = "未知";
				break;
			}
			MyApplication.getInstance().type = -1;
			if(!result.equals("分享取消")){
				ToastUtils.showToast(mContext, result);
			}
			finish();
			
		}
		
		private void goToGetMsg() {
//			Intent intent = new Intent(this, GetFromWXActivity.class);
//			intent.putExtras(getIntent());
//			startActivity(intent);
//			finish();
		}
		
		private void goToShowMsg(ShowMessageFromWX.Req showReq) {
			WXMediaMessage wxMsg = showReq.message;		
			WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;
			
			StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
			msg.append("description: ");
			msg.append(wxMsg.description);
			msg.append("\n");
			msg.append("extInfo: ");
			msg.append(obj.extInfo);
			msg.append("\n");
			msg.append("filePath: ");
			msg.append(obj.filePath);
			
//			Intent intent = new Intent(this, ShowFromWXActivity.class);
//			intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//			intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//			intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//			startActivity(intent);
//			finish();
		}
		
		/**
		 * 分享成功之后获取电子卷
		 */
		private void shared_addCode() {
			User user  = MyApplication.getInstance().getCurrentUser();
			long userId = 0;
			if(user!=null){
				userId = user.getUserId();
			}
			ApiBuyUtils.couponsManage(this, userId, 2,
					"", 0,0,0,0, new HttpConnectionUtil.RequestCallback() {

						@Override
						public void execute(ParseModel parseModel) {
							if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
									.getStatus())) {
								 ToastUtils.showToast(WXEntryActivity.this,parseModel.getMsg());
							} else {
//								ToastUtils.showToast(WXEntryActivity.this,parseModel.getMsg());
							}
						}
					});
		}

}
