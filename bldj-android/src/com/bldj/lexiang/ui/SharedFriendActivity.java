package com.bldj.lexiang.ui;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.share_friend);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("分享好友");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
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
				// TODO Auto-generated method stub

			}
		});
		btn_weixin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
