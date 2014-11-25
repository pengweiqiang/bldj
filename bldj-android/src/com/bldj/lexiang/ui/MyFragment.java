package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

/**
 * 我的
 * 
 * @author will
 * 
 */
public class MyFragment extends BaseFragment {

	private View infoView;
	private ActionBar mActionBar;
	private Button btn_logout;
	private Button btn_collect;
	private LinearLayout ll_myinfo;
	private LinearLayout ll_updatePwd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.my_fragment, container, false);
		
		
		initView();
		
		initListener();
		return infoView;
	}
	
	private void initView(){
		
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		btn_logout = (Button) infoView.findViewById(R.id.btn_logout);
		ll_myinfo = (LinearLayout)infoView.findViewById(R.id.myinfo);
		ll_updatePwd = (LinearLayout)infoView.findViewById(R.id.update_pwd);
		btn_collect = (Button)infoView.findViewById(R.id.btn_collect);
	}
	
	private void initListener(){
		ll_myinfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity,RegisterAndLoginActivity.class);
				startActivity(intent);
			}
		});
		//退出登录
		btn_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}
		});
		//我的收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity,MyCollectActivity.class);
				startActivity(intent);
			}
		});
		//修改密码
		ll_updatePwd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity,UpdatePwdActivity.class);
				startActivity(intent);
			}
		});
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我");
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}

}
