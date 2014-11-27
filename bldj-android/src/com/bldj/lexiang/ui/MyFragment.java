package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bldj.lexiang.MyApplication;
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
	private Button btn_logout;// 退出
	private Button btn_collect;// 收藏
	private Button btn_coupons;// 优惠卷
	private Button btn_address;// 我的地址
	private Button btn_order;// 我的订单
	private Button btn_myFiles;// 我的档案
	private Button btn_checkVersion;// 检测新版本
	private LinearLayout ll_myinfo;// 我的个人信息
	private LinearLayout ll_updatePwd;// 修改密码

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.my_fragment, container, false);

		initView();

		initListener();
		return infoView;
	}

	private void initView() {

		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		ll_myinfo = (LinearLayout) infoView.findViewById(R.id.myinfo);
		btn_logout = (Button) infoView.findViewById(R.id.btn_logout);
		ll_updatePwd = (LinearLayout) infoView.findViewById(R.id.update_pwd);
		btn_collect = (Button) infoView.findViewById(R.id.btn_collect);
		btn_coupons = (Button) infoView.findViewById(R.id.btn_coupons);
		btn_address = (Button) infoView.findViewById(R.id.btn_address);
		btn_order = (Button) infoView.findViewById(R.id.btn_order);
		btn_myFiles = (Button) infoView.findViewById(R.id.btn_myFiles);
		btn_checkVersion = (Button) infoView
				.findViewById(R.id.btn_check_version);
	}

	private void initListener() {
		ll_myinfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {

				}
				/*Intent intent = new Intent(mActivity,
						RegisterAndLoginActivity.class);
				startActivity(intent);*/
			}
		});
		// 退出登录
		btn_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		// 我的收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				if (checkIsLogin()) {
//
//				}
				Intent intent = new Intent(mActivity, MyCollectFragmentActivity.class);
				startActivity(intent);
			}
		});
		// 优惠卷
		btn_coupons.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (checkIsLogin()) {
//
//				}
				Intent intent = new Intent(mActivity,
						CouponsFragmentActivity.class);
				startActivity(intent);
			}
		});
		// 修改密码
		ll_updatePwd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity, UpdatePwdActivity.class);
				startActivity(intent);
			}
		});
		// 我的地址
		btn_address.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				if (checkIsLogin()) {
//
//				}
				Intent intent = new Intent(mActivity, AddressesActivity.class);
				startActivity(intent);
			}
		});
		// 我的订单
		btn_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {

				}
			}
		});
		// 我的档案
		btn_myFiles.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (checkIsLogin()) {

				}

			}
		});
		// 检查新版
		btn_checkVersion.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我");
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}

	/**
	 * 判断是否登录
	 * 
	 * @return
	 */
	private boolean checkIsLogin() {
		if (MyApplication.getInstance().getCurrentUser() == null) {
			Intent intent = new Intent(mActivity,
					RegisterAndLoginActivity.class);
			startActivity(intent);
			return false;
		}
		return true;
	}

}
