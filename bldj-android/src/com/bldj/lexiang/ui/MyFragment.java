package com.bldj.lexiang.ui;

import java.io.IOException;

import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionMetrics;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.ApiVersionUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.universalimageloader.core.ImageLoader;

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
	private TextView tv_username;// 用户名
	private ImageView image_head;// 头像

	private EditText et_nickname;

	User user;

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

		tv_username = (TextView) infoView.findViewById(R.id.tv_person_name);
		image_head = (ImageView) infoView.findViewById(R.id.img_person);

	}

	private void initData() {
		user = MyApplication.getInstance().getCurrentUser();
		if (user != null) {
			tv_username
					.setText(StringUtils.isEmpty(user.getNickname()) ? "未设置昵称"
							: user.getNickname());
			ImageLoader.getInstance().displayImage(
					user.getHeadurl(),
					image_head,
					MyApplication.getInstance().getOptions(
							R.drawable.default_image));
		} else {// 未登录
			tv_username.setText("未登录");
		}
	}

	private void initListener() {
		// 修改图片头像
		image_head.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {
					// String stream = ImageTools.imgToBase64(imgPath, null,
					// "");
					String stream = "";
					ApiUserUtils.updateHeader(mActivity, user.getUsername(),
							stream, new HttpConnectionUtil.RequestCallback() {

								@Override
								public void execute(ParseModel parseModel) {
									if (!ApiConstants.RESULT_SUCCESS
											.equals(parseModel.getStatus())) {
										ToastUtils.showToast(mActivity,
												parseModel.getMsg());
									} else {
										user.setHeadurl(/* headurl */"");
										MyApplication.getInstance().setUser(
												user);
										ImageLoader
												.getInstance()
												.displayImage(
														user.getHeadurl(),
														image_head,
														MyApplication
																.getInstance()
																.getOptions(
																		R.drawable.default_image));
									}
								}
							});
				}
			}
		});
		// 用户昵称
		tv_username.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {
					Builder dialog = new AlertDialog.Builder(mActivity);
					LayoutInflater inflater = (LayoutInflater) mActivity
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					LinearLayout layout = (LinearLayout) inflater.inflate(
							R.layout.dialogview, null);
					dialog.setView(layout);
					et_nickname = (EditText) layout.findViewById(R.id.searchC);
					et_nickname.setText(StringUtils.isEmpty(user.getNickname()) ? ""
							: user.getNickname());
					dialog.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									final String nickname = et_nickname
											.getText().toString();
									if (StringUtils.isEmpty(nickname)) {
										ToastUtils.showToast(mActivity,
												"用户昵称不能为空");
										return;
									}

									ApiUserUtils.updateNickName(
											mActivity,
											user.getUsername(),
											nickname,
											new HttpConnectionUtil.RequestCallback() {

												@Override
												public void execute(
														ParseModel parseModel) {
													if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
															.getStatus())) {
														ToastUtils
																.showToast(
																		mActivity,
																		parseModel
																				.getMsg());
													} else {
														user.setNickname(nickname);
														MyApplication
																.getInstance()
																.setUser(user);
														tv_username
																.setText(nickname);
													}
												}
											});
								}
							});

					dialog.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}

							});
					dialog.show();
				}
			}
		});
		ll_myinfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {
					
				}
				/*
				 * Intent intent = new Intent(mActivity,
				 * RegisterAndLoginActivity.class); startActivity(intent);
				 */
			}
		});
		// 退出登录
		btn_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MyApplication.getInstance().setUser(null);
				SharePreferenceManager.saveBatchSharedPreference(mActivity, Constant.FILE_NAME, "user", "");
				initData();
			}
		});
		// 我的收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 if (checkIsLogin()) {
					 Intent intent = new Intent(mActivity,
								MyCollectFragmentActivity.class);
						startActivity(intent);
				 }
				
			}
		});
		// 优惠卷
		btn_coupons.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				 if (checkIsLogin()) {
					 Intent intent = new Intent(mActivity,
								CouponsFragmentActivity.class);
						startActivity(intent);
				 }
				
			}
		});
		// 修改密码
		ll_updatePwd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {
					Intent intent = new Intent(mActivity,
							UpdatePwdActivity.class);
					startActivity(intent);
				}
			}
		});
		// 我的地址
		btn_address.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {
					Intent intent = new Intent(mActivity,
							AddressesActivity.class);
					startActivity(intent);
				}

			}
		});
		// 我的订单
		btn_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkIsLogin()) {
					Intent intent = new Intent(mActivity,MyOrdersActivity.class);
					startActivity(intent);
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
				ToastUtils.showToast(mActivity, "正在检查...");
				ApiVersionUtils.checkVersion(mActivity, new HttpConnectionUtil.RequestCallback() {
					
					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS
								.equals(parseModel.getStatus())) {
							ToastUtils.showToast(mActivity,
									parseModel.getMsg());
						}else{
							ToastUtils.showToast(mActivity,
									parseModel.getMsg());
						}
					}
				});
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

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

}
