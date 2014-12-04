package com.bldj.lexiang;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.bldj.universalimageloader.cache.memory.MemoryCacheAware;
import com.bldj.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.bldj.universalimageloader.core.DisplayImageOptions;
import com.bldj.universalimageloader.core.ImageLoader;
import com.bldj.universalimageloader.core.ImageLoaderConfiguration;
import com.bldj.universalimageloader.core.assist.ImageScaleType;
import com.bldj.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

	public static String TAG;

	public static double lat;
	public static double lon;
	public static String city = "";

	private static MyApplication myApplication = null;
	private User user = null;// 全局用户

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;

	public static MyApplication getInstance() {
		return myApplication;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getCurrentUser() {
		return user;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		TAG = this.getClass().getSimpleName();
		// 由于Application类本身已经单例，所以直接按以下处理即可。
		myApplication = this;
		initImageLoader(this);
		String userJson = (String) SharePreferenceManager
				.getSharePreferenceValue(this, "currentuser", "user", "");
		if (!StringUtils.isEmpty(userJson)) {
			user = JsonUtils.fromJson(userJson, User.class);
		}
		initBaiduLocClient();

	}

	/**
	 * 初始化imageLoader
	 */
	public void initImageLoader(Context context) {
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

		MemoryCacheAware<String, Bitmap> memoryCache;

		memoryCache = new LRULimitedMemoryCache(memoryCacheSize);

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(memoryCache).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public DisplayImageOptions getOptions(int drawableId) {
		return new DisplayImageOptions.Builder().showImageOnLoading(drawableId)
				.showImageForEmptyUri(drawableId).showImageOnFail(drawableId)
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 初始化百度定位sdk
	 * 
	 * @Title: initBaiduLocClient
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initBaiduLocClient() {
		// 初始化地图Sdk
		SDKInitializer.initialize(this);

		mLocationClient = new LocationClient(this.getApplicationContext());// 声明LocationClient类
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);// 注册监听函数
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			// double latitude = location.getLatitude();
			// double longtitude = location.getLongitude();
			String province = location.getProvince();
			city = location.getCity();
			String district = location.getDistrict();
			String addressStr = location.getAddrStr();
			System.out.println("province:" + province + "  city:" + city
					+ "  district:" + district + "  addressStr:" + addressStr);
			if (lat == location.getLatitude() && lon == location.getLongitude()) {
				// 若两次请求获取到的地理位置坐标是相同的，则不再定位
				if (!StringUtils.isEmpty(city)) {
					city = city.replace("市", "");
				}
				SharePreferenceManager.saveBatchSharedPreference(myApplication,
						Constant.FILE_NAME, "city", city);
				Intent intent = new Intent();
				intent.setAction(Constant.LOCATION);
				// 要发送的内容
				intent.putExtra("city", city);
				// 发送 一个无序广播
				myApplication.sendBroadcast(intent);
				mLocationClient.stop();
				return;
			}
			lat = location.getLatitude();
			lon = location.getLongitude();
		}
	}

}
