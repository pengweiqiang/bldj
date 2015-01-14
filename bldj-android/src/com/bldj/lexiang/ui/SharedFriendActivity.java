package com.bldj.lexiang.ui;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 分享好友
 * 
 * @author will
 * 
 */
public class SharedFriendActivity extends BaseActivity {

	ActionBar mActionBar;
	private Button btn_tencent;
	private Button btn_sina;
	private Button btn_weixin;
	private Button btn_weixinFriend;

	
	ShareUtil shareUtil ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.share_friend);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		shareUtil = new ShareUtil(mContext);
		shareUtil.initWX();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("分享好友");
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.hideRightActionButton();
	}

	@Override
	public void initView() {
		btn_tencent = (Button) findViewById(R.id.btn_share_tencent);
		btn_sina = (Button) findViewById(R.id.btn_share_sina);
		btn_weixin = (Button) findViewById(R.id.btn_share_weixin);
		btn_weixinFriend = (Button)findViewById(R.id.btn_share_weixin_friend);
	}

	@Override
	public void initListener() {
		btn_weixinFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MyApplication.getInstance().type = 0;
				ToastUtils.showToast(mContext, "分享微信...");
				shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareAppTxt(),
						SendMessageToWX.Req.WXSceneSession,MyApplication.getInstance().getConfParams().getAboutUsUrl());
			}
		});
		btn_tencent.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String shareUrl = 
						shareUtil.shareQQ(MyApplication.getInstance().getConfParams().getShareAppTxt(), "", "");
				Intent intent = new Intent(SharedFriendActivity.this,BannerWebActivity.class);
				intent.putExtra("url", shareUrl);
				intent.putExtra("name", "腾讯微博分享");
				startActivity(intent);
			}
		});
		btn_sina.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String shareUrl = 
						shareUtil.shareSina(MyApplication.getInstance().getConfParams().getShareAppTxt(), "", "");
				Intent intent = new Intent(SharedFriendActivity.this,BannerWebActivity.class);
				intent.putExtra("url", shareUrl);
				intent.putExtra("name", "新浪分享");
				startActivity(intent);
			}
		});
		btn_weixin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MyApplication.getInstance().type = 1;
				ToastUtils.showToast(mContext, "分享微信...");
				shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareAppTxt(),
						SendMessageToWX.Req.WXSceneTimeline,MyApplication.getInstance().getConfParams().getAboutUsUrl());
//				shareUtil.sendImgToWX("健康送到家，方便你我他",
//						SendMessageToWX.Req.WXSceneTimeline);
			}
		});
	}

}
