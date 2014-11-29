package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 重置密码
 * 
 * @author will
 * 
 */
public class ForgetPwdActivity extends BaseActivity {

	ActionBar mActionBar;
	private Button btn_update;
	private EditText et_phone;
	private EditText et_code;
	private EditText et_new_pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.forget_pwd);
		
		
		super.onCreate(savedInstanceState);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("密码重置");
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
		et_phone = (EditText)findViewById(R.id.phone);
		et_new_pwd = (EditText)findViewById(R.id.new_pwd);
		et_code = (EditText)findViewById(R.id.code);
		btn_update = (Button)findViewById(R.id.btn_confirm);
		
	}

	@Override
	public void initListener() {
		btn_update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String phone = et_phone.getText().toString().trim();
				String password = et_new_pwd.getText().toString().trim();
				String code = et_code.getText().toString().trim();
				if(StringUtils.isEmpty(phone)){
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入手机号码");
					return;
				}
				if(StringUtils.isEmpty(code)){
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入验证码");
					return;
				}
				if(StringUtils.isEmpty(password)){
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入您的新密码");
					return;
				}
				ApiUserUtils.forgetPwd(ForgetPwdActivity.this, phone, password, new HttpConnectionUtil.RequestCallback(){

					@Override
					public void execute(ParseModel parseModel) {
						if(ApiConstants.RESULT_SUCCESS.equals(parseModel.getStatus())){//修改成功
							ToastUtils.showToast(ForgetPwdActivity.this, "密码修改成功");
							Intent intent = new Intent(ForgetPwdActivity.this,RegisterAndLoginActivity.class);
							startActivity(intent);
							finish();
						}else{
							ToastUtils.showToast(ForgetPwdActivity.this, parseModel.getMsg());
						}
					}
					
					
				});
			}
		});
	}

}
