package com.bldj.lexiang.ui;

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
	private Button btn_sina;
	private Button btn_weixin;

	
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
		btn_sina = (Button) findViewById(R.id.btn_share_sina);
		btn_weixin = (Button) findViewById(R.id.btn_share_weixin);
	}

	@Override
	public void initListener() {
		btn_sina.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String shareUrl = 
						ShareUtil.shareSina("健康送到家，方便你我他", "http://www.baidu.com", 
								"http://img2.imgtn.bdimg.com/it/u=626942633,892821771&fm=21&gp=0.jpg");
				Intent intent = new Intent(SharedFriendActivity.this,BannerWebActivity.class);
				intent.putExtra("url", shareUrl);
				intent.putExtra("name", "新浪分享");
				startActivity(intent);
			}
		});
		btn_weixin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ToastUtils.showToast(mContext, "分享微信...");
				shareUtil.sendWebPageToWX("健康送到家，方便你我他",
						SendMessageToWX.Req.WXSceneTimeline);
			}
		});
	}

}
