package com.bldj.lexiang;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.bldj.lexiang.api.vo.User;
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
	
	public static String lat = "";
	public static String lon = "";
	
	private static MyApplication myApplication = null;
	private User user = null;//全局用户
	
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;

	public static MyApplication getInstance() {
		return myApplication;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	public User getCurrentUser() {
		if(user==null){
			/*user = new User();
			user.setUsername("小新");
			user.setMobile("156262631236");*/
		}
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
//		initBaiduLocClient();

	}
	
	/**
	 * 初始化imageLoader
	 */
	public void initImageLoader(Context context){
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
				.tasksProcessingOrder(QueueProcessingType.LIFO)	
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	public DisplayImageOptions getOptions(int drawableId){
		return new DisplayImageOptions.Builder()
		.showImageOnLoading(drawableId)
		.showImageForEmptyUri(drawableId)
		.showImageOnFail(drawableId)
		.resetViewBeforeLoading(true)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	/**
	 * 初始化百度定位sdk
	 * @Title: initBaiduLocClient
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void initBaiduLocClient() {
		mLocationClient = new LocationClient(this.getApplicationContext());//声明LocationClient类
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);//注册监听函数
	}
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			double latitude = location.getLatitude();
			double longtitude = location.getLongitude();
			}
	}
	
}
