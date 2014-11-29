package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
 * 反馈意见
 * 
 * @author will
 * 
 */
public class FeedBackActivity extends BaseActivity {

	ActionBar mActionBar;
	private EditText et_suggestion;
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
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionBar.setRightTextActionButton("发送", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String suggestion = et_suggestion.getText().toString().trim();
				if(StringUtils.isEmpty(suggestion)){
					ToastUtils.showToast(FeedBackActivity.this, "请输入您的宝贵意见");
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
				ApiUserUtils.unifor(FeedBackActivity.this, userId, suggestion, 0, nickname, username, 
						"", "", "", 0, 0, new HttpConnectionUtil.RequestCallback(){

							@Override
							public void execute(ParseModel parseModel) {
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

	@Override
	public void initView() {
		et_suggestion = (EditText)findViewById(R.id.suggestion);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
