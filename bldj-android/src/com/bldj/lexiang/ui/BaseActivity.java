package com.bldj.lexiang.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.commons.AppManager;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends Activity implements OnClickListener {

	protected static final String TAG = "BaseActivity";
	MyApplication application ;
	// protected static Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// mContext = this;
		AppManager.getAppManager().addActivity(this);
		
		initView();

		initListener();
		application = MyApplication.getInstance();

	}

	/**
	 * 左返回按钮点击事件
	 */
	protected void onLeftBackClick() {
		AppManager.getAppManager().finishActivity(this);
	}

	// TODO 设置标题
	public void setActivityView(CharSequence title, int layoutResID) {
		setContentView(layoutResID);
		TextView tv_title = (TextView) findViewById(R.id.title_textview);
		if (null != tv_title) {
			tv_title.setText(title);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		dismissWaitingDialog();
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();
	}

	protected void startActivity(Class<?> aTargetClass, Bundle aBundle) {
		Intent i = new Intent(this, aTargetClass);
		if (aBundle != null) {
			i.putExtras(aBundle);
		}
		startActivity(i);
	}

	protected void dismissWaitingDialog() {
	}

	public void showToast(String aMessage) {
		Toast.makeText(this, aMessage, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int resId) {
		Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideInputMethod() {
		View view = getCurrentFocus();
		if (view != null) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(view.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	/**
	 * 初始化控件
	 */
	public abstract void initView();

	/**
	 * 初始化控件的事件
	 */
	public abstract void initListener();

}
