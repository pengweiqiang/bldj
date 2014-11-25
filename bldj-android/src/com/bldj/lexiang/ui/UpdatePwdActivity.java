package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 修改密码
 * 
 * @author will
 * 
 */
public class UpdatePwdActivity extends BaseActivity {

	ActionBar mActionBar;
	private Button btn_update;
	private EditText et_old_pwd;
	private EditText et_new_pwd;
	private EditText et_new_pwd2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.update_pwd);
		
		
		super.onCreate(savedInstanceState);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("修改密码");
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
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		et_old_pwd = (EditText)findViewById(R.id.old_pwd);
		et_new_pwd = (EditText)findViewById(R.id.new_pwd);
		et_new_pwd2 = (EditText)findViewById(R.id.new_pwd2);
		btn_update = (Button)findViewById(R.id.btn_confirm);
		
	}

	@Override
	public void initListener() {
		btn_update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String old_pwd = et_old_pwd.getText().toString().trim();
				String new_pwd = et_new_pwd.getText().toString().trim();
				String new_pwd2 = et_new_pwd2.getText().toString().trim();
				if(StringUtils.isEmpty(old_pwd)){
					ToastUtils.showToast(UpdatePwdActivity.this, "请输入旧密码");
					return;
				}
				if(StringUtils.isEmpty(new_pwd)){
					ToastUtils.showToast(UpdatePwdActivity.this, "请输入新密码");
					return;
				}
				if(StringUtils.isEmpty(new_pwd2)){
					ToastUtils.showToast(UpdatePwdActivity.this, "请再次输入密码");
					return;
				}
				if(!new_pwd.equals(new_pwd2)){
					ToastUtils.showToast(UpdatePwdActivity.this, "两次密码不一致");
					return;
				}
//				ApiUserUtils.updatePwd(UpdatePwdActivity.this, username, opass, password, requestCallback);
			}
		});
	}

}
