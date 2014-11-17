package com.bldj.lexiang.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class LocalBitmapCache {
	
	private Map<String, SoftReference<Bitmap>> mCache; // simple cache with
	private static LocalBitmapCache mFsBitmapCache;
	
	private LocalBitmapCache() {
		mCache = new HashMap<String, SoftReference<Bitmap>>();
	}
	
	public static LocalBitmapCache getInstence() {
		if (mFsBitmapCache == null) {
			mFsBitmapCache = new LocalBitmapCache();
		}
		return mFsBitmapCache;
	}
	
	public void finish() {
		synchronized (this) {
			mCache.clear();
		}
	}
	
	public Bitmap load(String aName) {
		SoftReference<Bitmap> refToBitmap;
		Bitmap bitmap = null;
		
		synchronized (this) {
			refToBitmap = mCache.get(aName);
		}
		
		if (refToBitmap != null) {
			bitmap = refToBitmap.get();
		}
		
		return bitmap;
	}
	
	public void store(String aName, Bitmap aBitmap) {
		synchronized (this) {
			mCache.put(aName, new SoftReference<Bitmap>(aBitmap));
		}
	}
	
}
