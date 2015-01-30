package com.bldj.lexiang.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.PatternUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.LoadingDialog;

/**
 * 注册
 * 
 * @author will
 * 
 */
public class RegisterFragment extends BaseFragment {

	View infoView;

	private EditText et_phone;
	private EditText et_code;
	private EditText et_password;
	private Button btn_code;
	private Button btn_register;
	LoadingDialog loading;

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
	private void initView() {

		et_phone = (EditText) infoView.findViewById(R.id.phone);
		et_password = (EditText) infoView.findViewById(R.id.password);
		et_code = (EditText) infoView.findViewById(R.id.code);

		btn_code = (Button) infoView.findViewById(R.id.get_code);
		btn_register = (Button) infoView.findViewById(R.id.register);

	}

	/**
	 * 事件初始化
	 */
	private void initListener() {
		btn_register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String phone = et_phone.getText().toString();
				String password = et_password.getText().toString();
				String code = et_code.getText().toString();
				if (StringUtils.isEmpty(phone)) {
					et_phone.requestFocus();
					ToastUtils.showToast(mActivity, "用户名不能为空");
					return;
				}
				if (StringUtils.isEmpty(code)) {
					et_code.requestFocus();
					ToastUtils.showToast(mActivity, "验证码不能为空");
					return;
				}
				if (StringUtils.isEmpty(password)) {
					et_password.requestFocus();
					ToastUtils.showToast(mActivity, "密码不能为空");
					return;
				}
				String tagCode = (String)et_code.getTag();
				if(StringUtils.isEmpty(tagCode)){
					ToastUtils.showToast(mActivity, "请先获取验证码");
					return;
				}
				if(!PatternUtils.checkPhoneNum(phone)){
					et_phone.requestFocus();
					ToastUtils.showToast(mActivity, "请输入正确的手机号");
					return;
				}
				if(!code.equals(tagCode)){
					et_code.requestFocus();
					ToastUtils.showToast(mActivity, "验证码错误");
					return;
				}

				loading = new LoadingDialog(mActivity, "注册中...");
				loading.show();
				ApiUserUtils.register(mActivity, phone, password,
						MyApplication.lon, MyApplication.lat,
						new RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								loading.dismiss();
								if (ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {// 注册成功
									User user = JsonUtils.fromJson(parseModel
											.getData().toString(), User.class);
									MyApplication.getInstance().setUser(user);

									et_phone.setText("");
									et_password.setText("");
									et_code.setText("");
									//提示注册成功送优惠卷
									ToastUtils.showToast(mActivity,MyApplication.getInstance().getConfParams().getTxtRexSucc());
									
									((RegisterAndLoginActivity) mActivity).mViewPager
											.setCurrentItem(0, false);

								} else {
									ToastUtils.showToast(mActivity,
											parseModel.getMsg());
								}
							}
						});
			}
		});
		// 获取验证码
		btn_code.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String mobile = et_phone.getText().toString().trim();
				if (StringUtils.isEmpty(mobile)) {
					et_phone.requestFocus();
					ToastUtils.showToast(mActivity, "请输入手机号码获取验证码");
					return;
				}
				if(!PatternUtils.checkPhoneNum(mobile)){
					et_phone.requestFocus();
					ToastUtils.showToast(mActivity, "请输入正确的手机号");
					return;
				}
				regainCode();
				if(btn_code.isEnabled()){
					btn_code.setEnabled(false);
					ApiUserUtils.getCode(mActivity, mobile,
							new HttpConnectionUtil.RequestCallback() {
	
								@Override
								public void execute(ParseModel parseModel) {
//									handler.sendEmptyMessage(0);
									if (ApiConstants.RESULT_SUCCESS
											.equals(parseModel.getStatus())) {// 发送成功
										String code = parseModel.getData()
												.getAsString();
										if (!StringUtils.isEmpty(code)) {
											et_code.setTag(code);
										}
	
									} else {
										ToastUtils.showToast(mActivity,
												parseModel.getMsg());
									}
								}
							});
					
				}
			}
		});

	}

	private Timer timer;// 计时器
	private int time = 120;//倒计时120秒

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
				btn_code.setEnabled(true);
				btn_code.setText("获取验证码");
				timer.cancel();
			} else {
				btn_code.setText(msg.what + "秒重发");
			}
		};
	};

}
