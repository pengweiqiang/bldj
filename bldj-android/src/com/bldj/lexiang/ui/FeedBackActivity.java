package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.LoadingDialog;

/**
 * 反馈意见
 * 
 * @author will
 * 
 */
public class FeedBackActivity extends BaseActivity {

	ActionBar mActionBar;
	private EditText et_suggestion;
	private Button btn_submit;
	private TextView tv_str_length;
	private LoadingDialog loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.feed_back);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("反馈意见");
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
		et_suggestion = (EditText)findViewById(R.id.suggestion);
		btn_submit = (Button)findViewById(R.id.btn_submit);
		tv_str_length = (TextView)findViewById(R.id.tv_str_length);
	}

	@Override
	public void initListener() {
		et_suggestion.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String content = et_suggestion.getText().toString();  
				tv_str_length.setText(content.length() + "/"  
		                + 500);  
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		
		
		btn_submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String suggestion = et_suggestion.getText().toString().trim();
				if(StringUtils.isEmpty(suggestion)){
					et_suggestion.requestFocus();
					ToastUtils.showToast(FeedBackActivity.this, "请输入您的宝贵意见");
					return ;
				}
				if(suggestion.length()>500){
					ToastUtils.showToast(FeedBackActivity.this, "字数不能超过500字");
					return ;
				}
				User user = MyApplication.getInstance().getCurrentUser();
				long userId = 0;
				String nickname = "",username = "";
				if(user != null){
					userId = Long.parseLong(user.getUserId()+"");
					nickname= user.getNickname();
					username= user.getUsername();
				}
				loading = new LoadingDialog(mContext);
				loading.show();
				ApiUserUtils.unifor(FeedBackActivity.this, userId, suggestion, 0, nickname, username, 
						"", "", "", 0, 0, new HttpConnectionUtil.RequestCallback(){

							@Override
							public void execute(ParseModel parseModel) {
								loading.cancel();
								if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
										.getStatus())) {
									ToastUtils.showToast(FeedBackActivity.this, parseModel.getMsg());
								}else{
									ToastUtils.showToast(FeedBackActivity.this, "感谢您提出宝贵的意见");
								}
							}
					
					
				});
			}
		});
	}

}
