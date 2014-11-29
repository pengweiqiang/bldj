package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

/**
 * 我要认证
 * 
 * @author will
 * 
 */
public class AuthentActivity extends BaseActivity {

	ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.authent);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我要认证");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionBar.setRightTextActionButton("同意", new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AuthentActivity.this,ApplyAuthentActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void initView() {
	
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
