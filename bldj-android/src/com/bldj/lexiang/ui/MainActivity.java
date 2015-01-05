package com.bldj.lexiang.ui;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.ApiVersionUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.AppManager;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.BadgeView;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseFragmentActivity implements
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
	int[] tabIds = { R.id.home, R.id.hot, R.id.mall,R.id.my };

	// 定位获取当前用户的地理位置
	private LocationClient mLocationClient;
	
	private Button btn_mall;//显示订单按钮右上角具体的数字
	private BadgeView myBadgeView;
	//总数统计 start
	public static int count = 0;
	private int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		setContentView(R.layout.activity_main);
		mFragmentManager = getSupportFragmentManager();
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mTabIndicators = (RadioGroup) findViewById(R.id.tabIndicators);
		btn_mall = (Button)findViewById(R.id.btn_mall);
		myBadgeView = remind(0);
		
//		IntentFilter countFilter = new IntentFilter(Constant.ACTION_MESSAGE_COUNT);
//		registerReceiver(mCountMsgReceiver, countFilter);

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
					return mMallFragment = new MallFragment();
				} else if (position == 3) {
					return mMyFragment = new MyFragment();
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
						User user = MyApplication.getInstance().getCurrentUser();
						for (int i = 0; i < tabIds.length; i++) {
							if (checkedId == tabIds[i]) {
								index = i;
								if(user ==null && i>=2){
									Intent intent = new Intent(MainActivity.this,RegisterAndLoginActivity.class);
									startActivity(intent);
									return;
								}
								mViewPager.setCurrentItem(i, false);
								break;
							}
						}
					}
				});
		setCurrentTabId(R.id.home);
		//开启定位
		initLocClient();
		UmengUpdateAgent.setUpdateCheckConfig(false);
		UmengUpdateAgent.update(this);
		
		//umeng push 
		PushAgent mPushAgent = PushAgent.getInstance(MainActivity.this);
		mPushAgent.enable();
//		checVersion();
//		String device_token = UmengRegistrar.getRegistrationId(this);
	}
	
	@Override
	protected void onStop() {
		if(mViewPager.getCurrentItem() != index){
			mViewPager.setCurrentItem(index, false);
		}
		super.onStop();
		
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
	
	private BadgeView remind(int count) { //BadgeView的具体使用
		BadgeView badge1 = new BadgeView(this, btn_mall);// 创建一个BadgeView对象，view为你需要显示提醒的控件
		if(count>99){//当消息数量大于99，显示99+
			badge1.setText("99+");
		}else{
			badge1.setText(String.valueOf(count)); // 需要显示的提醒类容
		}
		badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
		badge1.setTextColor(Color.WHITE); // 文本颜色
		badge1.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
		badge1.setTextSize(12); // 文本大小
		badge1.setBadgeMargin(8, 6); // 水平和竖直方向的间距
//		badge1.setBadgeMargin(1); //各边间隔
		// badge1.toggle(); //显示效果，如果已经显示，则影藏，如果影藏，则显示
		if(count>0){
			badge1.show();// 只有显示
			// badge1.hide();//影藏显示
		}
		return badge1;
	}
	
	/**
	 * 显示订单数量红点
	 */
	/*private BroadcastReceiver mCountMsgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_MESSAGE_COUNT.equals(intent.getAction())) {
				
			}
		}
	};*/
	
	public void showCount(){
//		count = intent.getIntExtra("countOrders", count);
		
		if(count>0 && !myBadgeView.isShown()){
			myBadgeView.show();
		}else if(count<=0 && myBadgeView.isShown()){
			myBadgeView.hide();
		}
		myBadgeView.setText(String.valueOf(count));
	}
	@Override
	protected void onResume() {
		User user = MyApplication.getInstance().getCurrentUser();
		if(user!=null){
			//查询未支付订单数量
			ApiBuyUtils.getOrders(this, user.getUserId(),0,ApiConstants.MAX_STATUS,0,
					new HttpConnectionUtil.RequestCallback() {
	
						@Override
						public void execute(ParseModel parseModel) {
							
							if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
									.getStatus())) {
								
							} else {
								ArrayList<Order> ordersList = JsonUtils.fromJson(
										parseModel.getData().toString(),
										new TypeToken<ArrayList<Order>>() {
										});
								count = ordersList.size();
								showCount();
								Intent intent = new Intent(Constant.ACTION_MESSAGE_COUNT);
								intent.putExtra("orders", ordersList);
								sendBroadcast(intent);
							}
	
						}
					});
		}
		super.onResume();
	}
	
	/**
	 * 检查更新
	 */
	private void checVersion(){
		ApiVersionUtils.checkVersion(MainActivity.this,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS
								.equals(parseModel.getStatus())) {
						} else {
							ToastUtils.showToast(MainActivity.this,
									parseModel.getMsg());
						}
					}
				});
	}
	
}
