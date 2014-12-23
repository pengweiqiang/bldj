package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 申请认证
 * 
 * @author will
 * 
 */
public class ApplyAuthentActivity extends BaseActivity {

	ActionBar mActionBar;
	
	private EditText et_real_name;
	private EditText et_email;
	private EditText et_phone;
	private Button btn_auth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.apply_authent);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我要认证");
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
		et_real_name = (EditText)findViewById(R.id.real_name);
		et_email = (EditText)findViewById(R.id.common_email);
		et_phone = (EditText)findViewById(R.id.phone);
		btn_auth = (Button)findViewById(R.id.confirm);
	}

	@Override
	public void initListener() {
		btn_auth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String realName = et_real_name.getText().toString().trim();
				if(StringUtils.isEmpty(realName)){
					ToastUtils.showToast(ApplyAuthentActivity.this, "请输入您的真实姓名");
					return ;
				}
				Long phone = Long.parseLong(et_phone.getText().toString().trim());
				String emial = et_email.getText().toString().trim();
				ApiUserUtils.unifor(ApplyAuthentActivity.this, phone, "", 1, realName, emial, 
						"", "", "", 0, 0, new HttpConnectionUtil.RequestCallback(){

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
										.getStatus())) {
									ToastUtils.showToast(ApplyAuthentActivity.this, parseModel.getMsg());
								}else{
									ToastUtils.showToast(ApplyAuthentActivity.this, "申请认证已通知到后台，静候佳音");
								}
							}
					
					
				});
			}
		});
	}

}
