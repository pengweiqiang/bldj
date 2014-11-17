package com.bldj.lexiang.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bldj.lexiang.R;
import com.bldj.lexiang.GlobalConfig;

public class DialogUtil {
	private static DialogUtil dialogUtil = new DialogUtil();
	
	public DialogUtil getInstance() {
		if (dialogUtil == null) {
			dialogUtil = new DialogUtil();
		}
		return dialogUtil;
	}
	
	/**
	 * 通用AlertDialog
	 * 
	 * @param ctx
	 * @param head
	 * @param tip
	 * @param positiveId
	 * @param negativeId
	 * @param callback
	 */
	public static void createAlertDialog(Context ctx, int head, int tip, int positiveId, int negativeId,
			final AlertDialogOperate callback) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		if (-1 != head) {
			builder.setTitle(head);
		}
		builder.setMessage(tip);
		if (positiveId != -1) {
			builder.setPositiveButton(positiveId, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (null != callback) {
						callback.operate();
					}
					dialog.dismiss();
				}
			});
		}
		if (negativeId != -1) {
			builder.setNegativeButton(negativeId, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
		builder.create().show();
	}
	
	/**
	 * 2G/3G状态下下载提示Dialog
	 * 
	 * @param ctx
	 * @param callback
	 */
	public static void createNoWiFiAlertDialog(Context ctx, final AlertDialogOperate callback) {
		if (NetUtil.getNetworkState(ctx) == GlobalConfig.NETWORK_STATE_IDLE) {
			showToast(ctx, R.string.NETWORK_ERROR);
			return;
		}
		if (GlobalConfig.SETTING_WIFI && GlobalConfig.CURRENT_NETWORK_STATE_TYPE != GlobalConfig.NETWORK_STATE_WIFI) {
			showRenRenDialog(ctx, ctx.getString(R.string.prompt),
					ctx.getString(R.string.not_wifi), null, ctx.getString(R.string.go_on_download),
					ctx.getString(R.string.dlg_cancel), false, callback);
		} else {
			callback.operate();
		}
	}
	
	
	/**
	 * 显示RenRen统一风格对话框
	 * 
	 * @param context 上下文
	 * @param titleID 标题文字id，如空填-1
	 * @param tipID 提示内容文字id
	 * @param extraview 提示内容下可增加一个额外的view
	 * @param positiveId 确认功能按钮的文字id，无此按钮填-1
	 * @param negativeId 取消功能按钮的文字id，无此按钮填-1
	 * @param cancelable 对话框是否可通过物理返回键关闭
	 * @param callback 点击确认/取消按钮后的回调,operate()和cancel()分别对应确认和取消功能
	 */
//	public static Dialog showRenRenDialog(Context context, int titleID, int tipID, View extraview, int positiveId,
//			int negativeId, boolean cancelable, final AlertDialogOperate callback) {
//		
//		final Dialog dialog = new Dialog(context, android.R.style.Theme_Dialog);
//		dialog.setContentView(buildView(context));
//		Window dialogWindow = dialog.getWindow();
//		WindowManager.LayoutParams params = dialogWindow.getAttributes();
//		params.width = (int) (DeviceInfo.getScreenWidth() * 0.9);
//		dialogWindow.setGravity(Gravity.CENTER | Gravity.CENTER);
//		dialogWindow.setAttributes(params);
//		
//		TextView title = (TextView) dialogWindow.findViewById(R.id.renrendialog_title);
//		LinearLayout titlelayout = (LinearLayout) dialogWindow.findViewById(R.id.renrendialog_titlelayout);
//		TextView tip = (TextView) dialogWindow.findViewById(R.id.renrendialog_tip);
//		LinearLayout extralayout = (LinearLayout) dialogWindow.findViewById(R.id.renrendialog_extralayout);
//		TextView renrendialog_ok = (TextView) dialogWindow.findViewById(R.id.renrendialog_ok);
//		TextView renrendialog_cancel = (TextView) dialogWindow.findViewById(R.id.renrendialog_cancel);
//		
//		/** 对话框标题文字 */
//		if (titleID != -1) {
//			title.setText(titleID);
//		} else {
//			titlelayout.setVisibility(View.GONE);
//		}
//		
//		/** 对话框提示文字 */
//		if (tipID != -1) {
//			tip.setText(tipID);
//		} else {
//			tip.setVisibility(View.GONE);
//		}
//		
//		/** 对话框额外view,在tip文字下面 */
//		if (extraview != null) {
//			extralayout.setVisibility(View.VISIBLE);
//			extralayout.addView(extraview);
//		}
//		
//		/** 各按钮的状况 */
//		if (positiveId != -1) {
//			renrendialog_ok.setText(positiveId);
//			renrendialog_ok.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					if (callback != null) {
//						callback.operate();
//					}
//					dialog.dismiss();
//				}
//			});
//		} else {
//			renrendialog_ok.setVisibility(View.GONE);
//		}
//		if (negativeId != -1) {
//			renrendialog_cancel.setText(negativeId);
//			renrendialog_cancel.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					if (callback != null) {
//						callback.cancel();
//					}
//					dialog.dismiss();
//				}
//			});
//		} else {
//			renrendialog_cancel.setVisibility(View.GONE);
//		}
//		
//		dialog.setCancelable(cancelable);
//		if (cancelable) {
//			dialog.setOnCancelListener(new OnCancelListener() {
//				
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					if (callback != null) {
//						callback.cancel();
//					}
//				}
//			});
//		}
//		dialog.show();
//		return dialog;
//		return null;
//	}
	
	/**
	 * 显示RenRen统一风格对话框
	 * 
	 * @param context 上下文
	 * @param titleID 标题文字id，如空填-1
	 * @param tipID 提示内容文字String，如空填null
	 * @param extraview 提示内容下可增加一个额外的view
	 * @param positive 确认功能按钮的文字id，无此按钮填-1
	 * @param negativeId 取消功能按钮的文字id，无此按钮填-1
	 * @param cancelable 对话框是否可通过物理返回键关闭
	 * @param callback 点击确认/取消按钮后的回调,operate()和cancel()分别对应确认和取消功能
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("ResourceAsColor")
    public static void showRenRenDialog(Context context, String titleID, String tipID, View extraview, String positive,
			String negativeId, boolean cancelable, final AlertDialogOperate callback) {
        int dp15 = CommonHelper.getDp2px(context, 15);
        int dp72 = CommonHelper.getDp2px(context, 72);
        int dp30 = CommonHelper.getDp2px(context, 30);
        int dp20 = CommonHelper.getDp2px(context, 20);

		final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

		LinearLayout rootView  = new LinearLayout(context);
		FrameLayout.LayoutParams rootViewParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		rootView.setLayoutParams(rootViewParams);
		rootView.setOrientation(LinearLayout.VERTICAL);
		rootView.setBackgroundDrawable(CommonHelper.getAssertNinePatchDrawable(context, com.bldj.lexiang.utils.R.drawable.bg_share_to_other));
		
		LinearLayout titlelayout = new LinearLayout(context);
		LinearLayout.LayoutParams titlelayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		rootView.setLayoutParams(titlelayoutParams);
		rootView.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout r31 = new LinearLayout(context);
		LinearLayout.LayoutParams r31Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		r31Params.bottomMargin = dp15;
		r31Params.topMargin = dp15;
		r31.setLayoutParams(r31Params);
		r31.setOrientation(LinearLayout.VERTICAL);
		
		TextView title = new TextView(context);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		title.setLayoutParams(titleParams);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.parseColor("#155AA1"));
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
		
		r31.addView(title);
		
		LinearLayout r32 = new LinearLayout(context);
		LinearLayout.LayoutParams r32Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				1);
		r32.setLayoutParams(r32Params);
		r32.setBackgroundColor(Color.parseColor("#155AA1"));
		r32.setOrientation(LinearLayout.VERTICAL);
		
		titlelayout.addView(r31);
		titlelayout.addView(r32);

		TextView tip = new TextView(context);
		LinearLayout.LayoutParams tipParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tipParams.leftMargin = dp30;
		tipParams.rightMargin = dp30;
		tipParams.topMargin = dp20;
		tipParams.bottomMargin = dp20;
		tip.setLayoutParams(tipParams);
		tip.setTextColor(com.bldj.lexiang.utils.R.color.text_dark_gray);
		tip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		
		LinearLayout extralayout = new LinearLayout(context);
		LinearLayout.LayoutParams extralayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		extralayoutParams.leftMargin = dp30;
		extralayoutParams.rightMargin = dp30;
		extralayoutParams.topMargin = dp20;
		extralayoutParams.bottomMargin = dp20;
		extralayout.setLayoutParams(extralayoutParams);
		extralayout.setOrientation(LinearLayout.VERTICAL);	
		extralayout.setVisibility(View.GONE);

		LinearLayout r22 = new LinearLayout(context);
		LinearLayout.LayoutParams r22Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		r22.setLayoutParams(r22Params);
		r22.setOrientation(LinearLayout.HORIZONTAL);
		r22.setPadding(0, 0, 0, dp15);
		
		TextView renrendialog_ok = new TextView(context);
		LinearLayout.LayoutParams okParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
                dp72);
		okParams.leftMargin = dp15;
		okParams.weight = 1;		
		renrendialog_ok.setLayoutParams(okParams);
		renrendialog_ok.setGravity(Gravity.CENTER);
		renrendialog_ok.setText(positive);
		renrendialog_ok.setClickable(true);
		renrendialog_ok.setBackgroundDrawable(CommonHelper.getNinePatchStateDrawable(context, com.bldj.lexiang.utils.R.drawable.personal_center_change_account_normal, com.bldj.lexiang.utils.R.drawable.personal_center_change_account_pressed));
		renrendialog_ok.setTextColor(Color.parseColor("#FF626262"));
		renrendialog_ok.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	
		LinearLayout mid = new LinearLayout(context);
		LinearLayout.LayoutParams midParams = new LinearLayout.LayoutParams(
				dp15,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mid.setLayoutParams(midParams);
		mid.setOrientation(LinearLayout.HORIZONTAL);
        
		TextView renrendialog_cancel = new TextView(context);
		LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
                dp72);
		cancelParams.rightMargin = dp15;
		cancelParams.weight = 1;		
		renrendialog_cancel.setLayoutParams(cancelParams);
		renrendialog_cancel.setGravity(Gravity.CENTER);
		renrendialog_cancel.setText(positive);
		renrendialog_cancel.setClickable(true);
		renrendialog_cancel.setBackgroundDrawable(CommonHelper.getNinePatchStateDrawable(context, com.bldj.lexiang.utils.R.drawable.personal_center_change_account_normal, com.bldj.lexiang.utils.R.drawable.personal_center_change_account_pressed));
		renrendialog_cancel.setTextColor(Color.parseColor("#FF626262"));
		renrendialog_cancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

		r22.addView(renrendialog_ok);
		r22.addView(mid);
		r22.addView(renrendialog_cancel);
  
		rootView.addView(titlelayout);
		rootView.addView(tip);
		rootView.addView(extralayout);
		rootView.addView(r22);
     
     
		dialog.setContentView(rootView);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
        DeviceInfo.setContext(context);
		params.width = (int) (DeviceInfo.getScreenWidth() * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		dialogWindow.setGravity(Gravity.CENTER );
		dialogWindow.setAttributes(params);
		
		
		/** 对话框标题文字 */
		if (titleID != null) {
			title.setText(titleID);
		} else {
			titlelayout.setVisibility(View.GONE);
		}
		
		/** 对话框提示文字 */
		if (tipID != null) {
			tip.setText(tipID);
		} else {
			tip.setVisibility(View.GONE);
		}
		
		/** 对话框额外view,在tip文字下面 */
		if (extraview != null) {
			extralayout.setVisibility(View.VISIBLE);
			extralayout.addView(extraview);
		}
		
		/** 各按钮的状况 */
		if (positive != null) {
			renrendialog_ok.setText(positive);
			renrendialog_ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (callback != null) {
						callback.operate();
					}
					dialog.dismiss();
				}
			});
		} else {
			renrendialog_ok.setVisibility(View.GONE);
		}
		if (negativeId != null) {
			renrendialog_cancel.setText(negativeId);
			renrendialog_cancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (callback != null) {
						callback.cancel();
					}
					dialog.dismiss();
				}
			});
		} else {
			renrendialog_cancel.setVisibility(View.GONE);
		}
		
		dialog.setCancelable(cancelable);
		if (cancelable) {
			dialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					if (callback != null) {
						callback.cancel();
					}
				}
			});
		}
		dialog.show();
		
	}
	
	/**
	 * 简单的提示框。只有标题、提示信息、确定按钮
	 * 
	 * @param context
	 * @param titleID 标题
	 * @param tipID 提示信息
	 */
	public static void showRenRenTipDialog(Context context, int titleID, int tipID) {
		//showRenRenDialog(context, titleID, tipID, null, R.string.dlg_ok, -1, true, null);
	}
	
	
	
	public static void showToast(Context context, String text) {
		Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
		t.show();
	}
	
	public static void showToast(Context context, int text) {
		Toast t = Toast.makeText(context, text, Toast.LENGTH_LONG);
		t.show();
	}
	
	public static void showToast(Context context, String text, int duration) {
		Toast t = Toast.makeText(context, text, duration);
		t.show();
	}
	
	private static ProgressDialog mWaitingDialog;
	
	public static void showWaitingDialog(Activity aActivity, int aResId) {
		showWaitingDialog(aActivity, aActivity.getString(aResId));
	}
	
	/**
	 * loading框
	 */
	public static void showWaitingDialog(Activity aActivity, String aMessage) {
		if (aActivity.isFinishing()) {
			return;
		}
		if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
			mWaitingDialog.cancel();
		}
		mWaitingDialog = new ProgressDialog(aActivity);
		mWaitingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_MENU == keyCode || KeyEvent.KEYCODE_SEARCH == keyCode) {
					return true;
				}
				return false;
			}
		});
		
		mWaitingDialog.setTitle(null);
		mWaitingDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				dismissWaitingDialog();
			}
		});
		
		mWaitingDialog.setCanceledOnTouchOutside(false);
		mWaitingDialog.setMessage(aMessage);
		mWaitingDialog.show();
	}
	
	/**
	 * 取消loading框
	 */
	public static void dismissWaitingDialog() {
		try {
			if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
				mWaitingDialog.dismiss();
				mWaitingDialog = null;
			}
		} catch (IllegalArgumentException e) {
		}
	}
	
	/**
	 * 获得提示对话框
	 * 
	 * @param context
	 * @param title 标题
	 * @param message 内容
	 * @param clickListener 确定点击回调
	 * @return
	 */
	public static AlertDialog getDialog(Context context, int title, int message,
			DialogInterface.OnClickListener clickListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(R.string.dlg_ok, clickListener);
		
		builder.setNegativeButton(R.string.dlg_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		return builder.create();
	}
	
}
