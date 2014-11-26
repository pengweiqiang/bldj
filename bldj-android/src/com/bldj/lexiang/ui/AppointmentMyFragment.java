package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
import com.bldj.lexiang.view.ActionBar;

/**
 * 为自己预约
 * 
 * @author will
 * 
 */
public class AppointmentMyFragment extends BaseFragment {
	
	View infoView;

	private EditText et_phone;
	private EditText et_code;
	private EditText et_password;
	private Button btn_code;
	private Button btn_register;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.register, container, false);
		
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
		et_code = (EditText)infoView.findViewById(R.id.code);
		
		btn_code = (Button)infoView.findViewById(R.id.get_code);
		btn_register = (Button)infoView.findViewById(R.id.register);
		
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		btn_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String phone = et_phone.getText().toString();
				String password = et_password.getText().toString();
				String code = et_code.getText().toString();
				if(StringUtils.isEmpty(phone)){
					ToastUtils.showToast(mActivity, "用户名不能为空");
					return;
				}
				if(StringUtils.isEmpty(code)){
					ToastUtils.showToast(mActivity, "验证码不能为空");
					return;
				}
				if(StringUtils.isEmpty(password)){
					ToastUtils.showToast(mActivity, "密码不能为空");
					return;
				}
				
				
				 ApiUserUtils.register(mActivity, phone, password, MyApplication.lon, MyApplication.lat,new RequestCallback() {
					
					@Override
					public void execute(ParseModel parseModel) {
						if(ApiConstants.RESULT_SUCCESS.equals(parseModel.getStatus())){//注册成功
//							User user = JsonUtils.fromJson(parseModel.getData().toString(), User.class);
//							MyApplication.getInstance().setUser(user);
							
							
							et_phone.setText("");
							et_password.setText("");
							et_code.setText("");
							
						}else{
							ToastUtils.showToast(mActivity, parseModel.getMsg());
							((RegisterAndLoginActivity)mActivity).setCurrentTitle(0);
						}
					}
				});				
			}
		});
		//获取验证码
		btn_code.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
	}
	
}
