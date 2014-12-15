package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.LoadingDialog;

/**
 * 登录
 * 
 * @author will
 * 
 */
public class LoginFragment extends BaseFragment {

	private EditText et_phone;
	private EditText et_password;
	private TextView tv_forgetPwd;
	private Button btn_login;
	private View infoView;
	LoadingDialog loading ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.login, container, false);
		
		initView();
		
		initListener();
		
		
		return infoView;
	}
	/**
	 * 初始化控件
	 */
	private void initView(){
		
		et_phone = (EditText)infoView.findViewById(R.id.phone);
		et_password = (EditText)infoView.findViewById(R.id.password);
		tv_forgetPwd = (TextView)infoView.findViewById(R.id.forget_pwd);
		btn_login = (Button)infoView.findViewById(R.id.login);
		
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		btn_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String phone = et_phone.getText().toString();
				String password = et_password.getText().toString();
				if(StringUtils.isEmpty(phone)){
					et_phone.requestFocus();
					ToastUtils.showToast(mActivity, "用户名不能为空");
					return;
				}
				if(StringUtils.isEmpty(password)){
					et_password.requestFocus();
					ToastUtils.showToast(mActivity, "密码不能为空");
					return;
				}
				loading = new LoadingDialog(mActivity, "登录中...");
				loading.show();
				 ApiUserUtils.login(mActivity, phone, password, new RequestCallback() {
					
					@Override
					public void execute(ParseModel parseModel) {
						loading.cancel();
						if(ApiConstants.RESULT_SUCCESS.equals(parseModel.getStatus())){//登录成功
							User user = JsonUtils.fromJson(parseModel.getData().toString(), User.class);
							MyApplication.getInstance().setUser(user);
							SharePreferenceManager.saveBatchSharedPreference(mActivity, Constant.FILE_NAME, "user", JsonUtils.toJson(user));
							mActivity.finish();
						}else{
							ToastUtils.showToast(mActivity, parseModel.getMsg());
						}
					}
				});				
			}
		});
		
		tv_forgetPwd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity,ForgetPwdActivity.class);
				startActivity(intent);
			}
		});
		
	}

}
