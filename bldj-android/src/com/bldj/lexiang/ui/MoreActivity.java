package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

/**
 * 更多
 * 
 * @author will
 * 
 */
public class MoreActivity extends BaseActivity {

	ActionBar mActionBar;

	private Button btn_share, btn_about_us, btn_feed_back, btn_authent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.more);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("更多");
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
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_feed_back = (Button) findViewById(R.id.btn_feed_back);
		btn_about_us = (Button) findViewById(R.id.btn_about_us);
		btn_authent = (Button) findViewById(R.id.btn_authent);
	}

	@Override
	public void initListener() {
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MoreActivity.this,
						SharedFriendActivity.class);
				startActivity(intent);
			}
		});

		btn_feed_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MoreActivity.this,
						FeedBackActivity.class);
				startActivity(intent);
			}
		});
		btn_about_us.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MoreActivity.this,
						AboutActivity.class);
				startActivity(intent);
			}
		});
		btn_authent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MoreActivity.this,
						AuthentActivity.class);
				startActivity(intent);
			}
		});

	}

}
