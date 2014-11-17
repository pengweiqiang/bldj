package com.bldj.lexiang;

import java.io.File;
import com.bldj.lexiang.utils.CommonHelper;
import com.bldj.lexiang.utils.FileUtils;
import com.bldj.lexiang.utils.NetUtil;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.bldj.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.bldj.universalimageloader.core.ImageLoader;
import com.bldj.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

public class StorePlatform {
	/**
	 * 存放缓冲路径
	 */
	private static String BASE_PATH = "/sitech/store_sdk/download";
	

    /**
     * @param activity 当前页面activity
     * @param enterId  企业id(必填,不能为空)
     * @param userId  用户id(必填,不能为空)
     * @param userName 用户名(必填,不能为空)
     */
	public static void launchAppStore(Activity activity,String enterId,String userId,String userName){
        if(null == activity){
            Log.e("sitech Store SDK","activity不能为空");
            return;
        }
        if(null == enterId || "".equals(enterId.trim())){
            ToastUtils.showToast(activity,"userID不能为空");
            return;
        }
        if(null == userId || "".equals(userId.trim())){
            ToastUtils.showToast(activity,"userID不能为空");
            return;
        }
        if(null == userName || "".equals(userName.trim())){
            ToastUtils.showToast(activity,"userName不能为空");
            return;
        }
        GlobalConfig.ENTER_ID = enterId;
        GlobalConfig.USER_ID = userId;
        GlobalConfig.USER_NAME = userName;
		initCachePath(activity);
		NetUtil.getNetworkState(activity);
		//activity.startActivity(new Intent(activity,MainActivity.class));
	}
	
	private static void initCachePath(Activity activity) {
		if (CommonHelper.isSDCardMounted()) { // 插入SD卡
			CommonHelper.setSdCardRemoved(false);
			String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
			//ToastUtils.showToast(activity, sdcard);
			// 给目录赋值权限
			CommonHelper.chmod("777", sdcard);
			GlobalConfig.CACHE_DOWNLOAD_HOME = GlobalConfig.CACHE_IMG_PATH = sdcard + BASE_PATH;
			GlobalConfig.CACHE_IMG_PATH = sdcard + BASE_PATH + "/images/";
			GlobalConfig.CACHE_DATA_PATH = sdcard + BASE_PATH + "/data/";
			GlobalConfig.CACHE_ERROR_LOG_PATH = sdcard + BASE_PATH + "/errorlog/";
			GlobalConfig.CACHE_DOWNLOAD_APK_PATH = sdcard + BASE_PATH + "/apk/";
			GlobalConfig.CACHE_DOWNLOAD_TEMP_PATH = sdcard + BASE_PATH + "/temp/";
			GlobalConfig.CACHE_UPDATE_PATH = sdcard + BASE_PATH + "/Update/";
			
			// 启动目录监听
			File apkpath = new File(GlobalConfig.CACHE_DOWNLOAD_APK_PATH);
			if (!apkpath.exists()) {
				apkpath.mkdirs();
			}
			
		} else {
			CommonHelper.setSdCardRemoved(true);
			// ToastUtils.showToast(this, R.string.ds_nosdcard);
		}
		initImageDownloader(activity);
        initShoppingCartNum(activity);
	}

	/**
	 * 初始化图片下载器
	 */
	private static void initImageDownloader(Activity activity) {
		File imageCacheFile = FileUtils.getImgCacheDir(activity);
		if(!ImageLoader.getInstance().isInited()){
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity)
			// .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
					.threadPoolSize(3) // default
					// .threadPriority(Thread.NORM_PRIORITY - 1) // default
					// .tasksProcessingOrder(QueueProcessingType.FIFO) // default
					.denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache())
					// .memoryCacheSize(2 * 1024 * 1024)
					.memoryCacheSizePercentage(10) // default 13
					.discCache(new UnlimitedDiscCache(imageCacheFile)) // default
					.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
					
					// .writeDebugLogs()
					.build();
			ImageLoader.getInstance().init(config);
		}
		
	}

    private static void initShoppingCartNum(Activity activity){
    }
}
