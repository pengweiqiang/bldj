package com.bldj.lexiang.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.ApiVersionUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.PhotoUtil;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.RoundImageView;
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
	private RoundImageView image_head;// 头像

	private EditText et_nickname;

	User user;

	String path;

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
		image_head = (RoundImageView) infoView.findViewById(R.id.img_person);

	}

	private void initData() {
		user = MyApplication.getInstance().getCurrentUser();
		if (user != null) {
			btn_logout.setText(getResources().getString(R.string.logout));
			tv_username
					.setText(StringUtils.isEmpty(user.getNickname()) ? "未设置昵称"
							: user.getNickname());
			ImageLoader.getInstance().displayImage(
					ReqUrls.Connection_Type_Common+ReqUrls.DEFAULT_REQ_HOST_IP+user.getHeadurl(),
					image_head,
					MyApplication.getInstance().getOptions(
							R.drawable.default_head_image));
		} else {// 未登录
			tv_username.setText("未登录");
			btn_logout.setText(getResources().getString(R.string.login));
			image_head.setImageDrawable(getResources().getDrawable(R.drawable.default_head_image));
		}
	}

	private void initListener() {
		// 修改图片头像
		image_head.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (checkIsLogin()) {
					showAvatarPop(view);
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
			public void onClick(View view) {
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
				if (checkIsLogin()) {
					MyApplication.getInstance().setUser(null);
					SharePreferenceManager.saveBatchSharedPreference(mActivity,
							Constant.FILE_NAME, "user", "");
					initData();
				}
				
			}
		});
		// 我的收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				if (checkIsLogin()) {
					Intent intent = new Intent(mActivity,
							MyCollectFragmentActivity.class);
					startActivity(intent);
//				}

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
					Intent intent = new Intent(mActivity,
							MyOrdersActivity.class);
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
				ApiVersionUtils.checkVersion(mActivity,
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {
									ToastUtils.showToast(mActivity,
											parseModel.getMsg());
								} else {
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
		actionBar.setTitle("我的");
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

	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;

	public String filePath = "";

	private void showAvatarPop(View parent) {
		View view = LayoutInflater.from(mActivity).inflate(
				R.layout.pop_showavator, null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				File dir = new File(Constant.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// 获取相片的保存路径
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, 0);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.white));
				// layout_choose.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 1);
			}
		});

		avatorPop = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		// avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}

	/**
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	boolean isFromCamera = false;// 区分拍照旋转
	int degree = 0;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:// 拍照修改头像
			if (resultCode == Activity.RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "拍照后的角度：" + degree);
				startImageAction(Uri.fromFile(file), 200, 200, 2, true);
			}
			break;
		case 1:// 本地修改头像
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == Activity.RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200, 2, true);
			} else {
				// ShowToast("照片获取失败");
			}

			break;
		case 2:// 裁剪头像返回
				// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				// Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropAvator(data);
			}
			// 初始化文件路径
			filePath = "";
			// 上传头像
			updateHeadImage();
			break;
		default:
			break;

		}
	}

	/**
	 * 保存裁剪的头像
	 * 
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				image_head.setImageBitmap(bitmap);
				// 保存图片
				String filename = new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()) + ".jpg";
				path = Constant.MyAvatarDir + filename;
				PhotoUtil.saveBitmap(Constant.MyAvatarDir, filename, bitmap,
						false);
				// 上传头像
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}

	/**
	 * 上传头像图片
	 */
	private void updateHeadImage() {

		// String stream = ImageTools.imgToBase64(path, null,"");
		// File file = new File(path);
		ApiUserUtils.updateHeader2(mActivity, user.getUsername(), path,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mActivity, parseModel.getMsg());
						} else {
							ToastUtils.showToast(mActivity, parseModel.getMsg());
							user.setHeadurl(ReqUrls.Connection_Type_Common+ReqUrls.DEFAULT_REQ_HOST_IP+parseModel.getData().getAsString());
							MyApplication.getInstance().setUser(user);
							ImageLoader.getInstance().displayImage(
									user.getHeadurl(),
									image_head,
									MyApplication.getInstance().getOptions(
											R.drawable.default_head_image));
						}
					}
				});
	}

	// ApiUserUtils.updateHeader(mActivity,user.getUsername(),
	// stream,new File(path), new HttpConnectionUtil.RequestCallback() {
	//
	// @Override
	// public void execute(ParseModel parseModel) {
	// if (!ApiConstants.RESULT_SUCCESS
	// .equals(parseModel.getStatus())) {
	// ToastUtils.showToast(mActivity,
	// parseModel.getMsg());
	// } else {
	// // user.setHeadurl(headurl);
	// MyApplication.getInstance().setUser(
	// user);
	// ImageLoader
	// .getInstance()
	// .displayImage(
	// user.getHeadurl(),
	// image_head,
	// MyApplication
	// .getInstance()
	// .getOptions(
	// R.drawable.default_image));
	// }
	// }
	// });
	// }

}
