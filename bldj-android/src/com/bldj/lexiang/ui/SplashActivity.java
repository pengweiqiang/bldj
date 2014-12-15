package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

/**
 * 加载页
 * 
 * @author will
 * 
 */
public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash);
		super.onCreate(savedInstanceState);
		
		 // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity 
        new Handler().postDelayed(new Runnable() {
            public void run() {
            	Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            	startActivity(intent);
            	overridePendingTransition(R.anim.activity_right_in,  R.anim.activity_left_out);
            	finish();
            }
        }, 2000);
	}


	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
