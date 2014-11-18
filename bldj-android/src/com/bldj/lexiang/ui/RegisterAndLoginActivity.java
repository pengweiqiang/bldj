package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DialogUtil;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.NetUtil;
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
		setActivityView("登录",R.layout.register_login);
		
		super.onCreate(savedInstanceState);
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
				ApiUserUtils.login(RegisterAndLoginActivity.this.getApplicationContext(), "", "", new RequestCallback() {
					
					public void execute(ParseModel parseModel) {
						if(ApiConstants.RESULT_SUCCESS.equals(parseModel.getStatus())){//登录成功
							User user = JsonUtils.fromJson(parseModel.getData().toString(), User.class);
							user.getUsername();
							
							application.setUser(user);//保存user全局
							
						}else if(NetUtil.NET_ERR == Integer.valueOf(parseModel.getStatus())){//网络异常
							DialogUtil.showToast(RegisterAndLoginActivity.this, parseModel.getMsg());
						}
					}
				});
			}
		});
		
		
		
	}
	
	
}
