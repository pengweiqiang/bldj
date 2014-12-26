package com.bldj.lexiang.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.PatternUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.LoadingDialog;

/**
 * 重置密码
 * 
 * @author will
 * 
 */
public class ForgetPwdActivity extends BaseActivity {

	ActionBar mActionBar;
	private Button btn_update;
	private Button btn_getCode;
	private EditText et_phone;
	private EditText et_code;
	private EditText et_new_pwd;
	LoadingDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.forget_pwd);

		super.onCreate(savedInstanceState);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("密码重置");
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
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		et_phone = (EditText) findViewById(R.id.phone);
		et_new_pwd = (EditText) findViewById(R.id.new_pwd);
		et_code = (EditText) findViewById(R.id.code);
		btn_update = (Button) findViewById(R.id.btn_confirm);
		btn_getCode = (Button) findViewById(R.id.get_code);

	}

	@Override
	public void initListener() {
		btn_update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				final String phone = et_phone.getText().toString().trim();
				String password = et_new_pwd.getText().toString().trim();
				String code = et_code.getText().toString().trim();
				if (StringUtils.isEmpty(phone)) {
					et_phone.requestFocus();
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入手机号码");
					return;
				}
				if (StringUtils.isEmpty(code)) {
					et_code.requestFocus();
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入验证码");
					return;
				}
				if (StringUtils.isEmpty(password)) {
					et_new_pwd.requestFocus();
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入您的新密码");
					return;
				}
				String tagCode = (String)et_code.getTag();
				if(StringUtils.isEmpty(tagCode)){
					ToastUtils.showToast(ForgetPwdActivity.this, "请先获取验证码");
					return;
				}
				if(!PatternUtils.checkPhoneNum(phone)){
					et_phone.requestFocus();
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入正确的手机号");
					return;
				}
				if(!code.equals(tagCode)){
					et_code.requestFocus();
					ToastUtils.showToast(ForgetPwdActivity.this, "验证码错误");
					return;
				}
				loading = new LoadingDialog(mContext);
				loading.show();
				ApiUserUtils.forgetPwd(ForgetPwdActivity.this, phone, password,
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								loading.cancel();
								if (ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {// 修改成功
									ToastUtils.showToast(
											ForgetPwdActivity.this, "密码修改成功");
									Intent intent = new Intent();
									intent.putExtra("name", phone);
									setResult(21, intent);
									finish();
								} else {
									ToastUtils.showToast(
											ForgetPwdActivity.this,
											parseModel.getMsg());
								}
							}

						});
			}
		});

		// 获取验证码
		btn_getCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String mobile = et_phone.getText().toString().trim();
				if (StringUtils.isEmpty(mobile)) {
					et_phone.requestFocus();
					ToastUtils
							.showToast(ForgetPwdActivity.this, "请输入手机号码获取验证码");
					return;
				}
				if(!PatternUtils.checkPhoneNum(mobile)){
					et_phone.requestFocus();
					ToastUtils.showToast(ForgetPwdActivity.this, "请输入正确的手机号");
					return;
				}
				regainCode();
				if (btn_getCode.isEnabled()) {
					btn_getCode.setEnabled(false);
					ApiUserUtils.getCode(ForgetPwdActivity.this, mobile,
							new HttpConnectionUtil.RequestCallback() {

								@Override
								public void execute(ParseModel parseModel) {
									handler.sendEmptyMessage(0);
									if (ApiConstants.RESULT_SUCCESS
											.equals(parseModel.getStatus())) {// 发送成功
										String code = parseModel.getData()
												.getAsString();
										if (!StringUtils.isEmpty(code)) {
											et_code.setTag(code);
										}

									} else {
										ToastUtils.showToast(
												ForgetPwdActivity.this,
												parseModel.getMsg());
									}
								}
							});

				}
			}
		});

	}

	private Timer timer;// 计时器
	private int time = 120;// 倒计时120秒

	private void regainCode() {
		time = 120;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(time--);
			}
		}, 0, 1000);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				btn_getCode.setEnabled(true);
				btn_getCode.setText("获取验证码");
				timer.cancel();
			} else {
				btn_getCode.setText(msg.what + "秒重发");
			}
		};
	};

}
