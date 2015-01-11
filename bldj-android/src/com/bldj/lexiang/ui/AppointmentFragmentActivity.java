package com.bldj.lexiang.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;

/**
 * 我要预约
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class AppointmentFragmentActivity extends BaseFragmentActivity implements OnGetSuggestionResultListener{

	CustomViewPager mViewPager;
	private ActionBar mActionBar;
	MainFragmentAdapter adapter;

	// TabPageIndicator tabPageIndicator;

	private RadioGroup rg_title;
	private RadioButton rb_my, rb_other;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	
	private SuggestionSearch mSuggestionSearch = null;
	String city ;
	ArrayList<String> locationList;
	AppointmentOtherFragment appointOtherFragment;
	AppointmentMyFragment appointMyFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment);

		// initTabLine();
		initView();

		initListener();
		
		
		if (!StringUtils.isEmpty((String)SharePreferenceManager.getSharePreferenceValue(this, Constant.FILE_NAME, "city", ""))) {
			city = (String)SharePreferenceManager.getSharePreferenceValue(this, Constant.FILE_NAME, "city", "");
		}
		
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);// 需要包含地址信息
		mLocClient.setLocOption(option);
		mLocClient.start();
	}
	
	
	
	@Override
	protected void onResume() {
		/**
		 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
		 */
		locationList = new ArrayList<String>();
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		
		mSuggestionSearch
				.requestSuggestion((new SuggestionSearchOption())
						.keyword(MyApplication.getInstance().street).city(city));
		
		super.onResume();
	}



	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我要预约");
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.setRightTextActionButton("", R.drawable.common_address, true,
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(
								AppointmentFragmentActivity.this,
								AddressesActivity.class);
						intent.putExtra("type", 1);
						startActivityForResult(intent, 123);
					}
				});
	}

	private void initView() {

		// tabPageIndicator =
		// (TabPageIndicator)findViewById(R.id.tabPageIndicator);
		mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
		// mViewPager.setScanScroll(false);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		rb_my = (RadioButton) findViewById(R.id.radio_my);
		rb_other = (RadioButton) findViewById(R.id.radio_other);
		rg_title = (RadioGroup) findViewById(R.id.rg_title);

		appointOtherFragment = new AppointmentOtherFragment();
		appointMyFragment = new AppointmentMyFragment();
		
		adapter = new MainFragmentAdapter(
				getSupportFragmentManager());
		// 绑定适配器
		mViewPager.setAdapter(adapter);
		// tabPageIndicator.setViewPager(mViewPager);
	}

	private void initListener() {
		rg_title.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_my:
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.radio_other:
					mViewPager.setCurrentItem(1, false);
					break;
				}
			}
		});

		// 设置滑动监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mViewPager.setCurrentItem(position, false);
				switch (position) {
				case 0:
					rb_my.setChecked(true);
					break;
				case 1:
					rb_other.setChecked(true);
					break;

				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.i("tuzi", arg0 + "," + arg1 + "," + arg2);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public class MainFragmentAdapter extends FragmentPagerAdapter {

		private String[] titles = new String[] { "给自己预约", "给他人预约" };

		public MainFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		public MainFragmentAdapter(FragmentManager fm, Context context) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return appointMyFragment;
			case 1:
				return appointOtherFragment;
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
			if (MyApplication.lat == location.getLatitude() && MyApplication.lon == location.getLongitude()) {
				mLocClient.stop();
			}else{
				MyApplication.getInstance().addressStr = location.getAddrStr();
				MyApplication.lat = location.getLatitude();
				MyApplication.lon = location.getLongitude();
				MyApplication.getInstance().street = location.getStreet();
				appointMyFragment.setLocation(location.getAddrStr());
				
				if(mSuggestionSearch!=null){
					mSuggestionSearch
					.requestSuggestion((new SuggestionSearchOption())
						.keyword(MyApplication.getInstance().street).city(city));
				}
			}
			
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 20) {
			String address = data.getStringExtra("address");
			if(!StringUtils.isEmpty(address)){
				SharePreferenceManager.saveBatchSharedPreference(AppointmentFragmentActivity.this, Constant.FILE_NAME, "address",address);
			}
		}
	}

	
	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
//			Toast.makeText(AddressActivity.this, "没有更多了~", Toast.LENGTH_SHORT)
//					.show();
			locationList.clear();
//			locationList.add(btn_location.getText().toString());
		}else{
			locationList.clear();
			for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
	//			Log.e("TAG", info.key);
				if (info != null) {
	//				mAdapter.add(info);
					if(!locationList.contains(info.key)){
						locationList.add(info.key);
					}
				}
	//			mAdapter.notifyDataSetChanged();// 默认聚焦最后一行
	//			lvAddress.setSelection(mAdapter.getCount());
			}
		}
		appointMyFragment.setSearchList(locationList);
//		appointOtherFragment.setSearchList(locationList);
		
	}

}
