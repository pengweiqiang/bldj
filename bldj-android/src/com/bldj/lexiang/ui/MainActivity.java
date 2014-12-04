package com.bldj.lexiang.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.commons.AppManager;
import com.bldj.lexiang.utils.ToastUtils;

public class MainActivity extends FragmentActivity implements
		OnPageChangeListener {
	// 精品推荐
	protected HomeFragment mHomeFragment;
	// 分类
	protected HotProductFragment mProductFragment;
	// 我的
	protected MyFragment mMyFragment;
	// 商城
	protected MallFragment mMallFragment;

	protected FragmentPagerAdapter mAdapter;
	protected RadioGroup mTabIndicators;
	protected ViewPager mViewPager;

	protected FragmentManager mFragmentManager;
	int[] tabIds = { R.id.home, R.id.hot, R.id.my, R.id.mall };

	// 定位获取当前用户的地理位置
	private LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_main);
		mFragmentManager = getSupportFragmentManager();
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mTabIndicators = (RadioGroup) findViewById(R.id.tabIndicators);

		mAdapter = new FragmentPagerAdapter(mFragmentManager) {

			@Override
			public int getCount() {
				return tabIds.length;
			}

			@Override
			public Fragment getItem(int position) {
				if (position == 0) {
					return mHomeFragment = new HomeFragment();
				} else if (position == 1) {
					return mProductFragment = new HotProductFragment();
				} else if (position == 2) {
					return mMyFragment = new MyFragment();
				} else if (position == 3) {
					return mMallFragment = new MallFragment();
				}
				return null;
			}
		};
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);

		mViewPager.setOffscreenPageLimit(4);
		// 实现模块切换
		mTabIndicators
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						for (int i = 0; i < tabIds.length; i++) {
							if (checkedId == tabIds[i]) {
								mViewPager.setCurrentItem(i, false);
								// if (tabIds[i] == R.id.info) {
								//
								// }
								break;
							}
						}
					}
				});
		setCurrentTabId(R.id.home);
		//开启定位
		initLocClient();
	}

	/*
	 * 切換至指定 tabId 的模块
	 */
	public void setCurrentTabId(int tabId) {
		((RadioButton) mTabIndicators.findViewById(tabId)).setChecked(true);
	}

	public int getCurrentTabId() {
		return mTabIndicators.getCheckedRadioButtonId();
	}

	private boolean isExit = false;

	// 退出操作
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isExit == false) {
				isExit = true;
				handler.sendEmptyMessageDelayed(0, 3000);
				ToastUtils.showToast(this, "再按一次进行退出");
				return true;
			} else {
				AppManager.getAppManager().finishAllActivity();
				return false;
			}
		}
		return true;
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int i) {
		setCurrentTabId(tabIds[i]);
	}

	/**
	 * 开启定位，更新当前用户的经纬度坐标
	 * 
	 * @Title: initLocClient
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initLocClient() {
		mLocationClient = MyApplication.getInstance().mLocationClient;
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式:高精度模式
		option.setCoorType("bd09ll"); // 设置坐标类型:百度经纬度
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为1000ms:低于1000为手动定位一次，大于或等于1000则为定时定位
		option.setIsNeedAddress(true);// 不需要包含地址信息
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

}
