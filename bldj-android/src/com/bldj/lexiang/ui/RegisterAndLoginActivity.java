package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bldj.lexiang.R;
/**
 * 登录注册界面
 * @author will
 *
 */
public class RegisterAndLoginActivity extends BaseActivity{

	private Button btn_login;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setActivityView("登录",R.layout.register_login);
	}

	@Override
	public void initView() {
		btn_login = (Button)findViewById(R.id.btn_login);
		
	}

	@Override
	public void initListener() {
		//登录
		btn_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		
		
	}
	
	
}
