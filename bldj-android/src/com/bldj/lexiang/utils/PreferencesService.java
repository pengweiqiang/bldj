package com.bldj.lexiang.utils;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 数据存取类
 */
public class PreferencesService {
	private static SharedPreferences mPreferences;
	private static Context mContext;
	private static String mShareName = "share_neusoft";
	private static int mMode = Context.MODE_PRIVATE;

	public static int getMode() {
		return mMode;
	}

	public static void setMode(int mMode) {
		PreferencesService.mMode = mMode;
	}

	public static String getPreferenceName() {
		return mShareName;
	}

	public static void setPreferenceName(String mShareName) {
		PreferencesService.mShareName = mShareName;
	}

	private PreferencesService() {
	}

	private static PreferencesService mPreferencesService;

	public static PreferencesService getInstance(Context context) {
		if (mPreferencesService == null) {
			mContext = context;
			mPreferencesService = new PreferencesService();
			mPreferences = mContext.getSharedPreferences(getPreferenceName(), getMode());
		}
		return mPreferencesService;
	}

	public Editor getEditor() {
		return mPreferences.edit();
	}

	/**
	 * 批量保存
	 */
	public void saveInfo(Map<String, String> info) {
		Editor editor = mPreferences.edit();
		Set<String> keys = info.keySet();
		for (String str : keys) {
			editor.putString(str, info.get(str));
		}
		editor.commit();
	}

	/**
	 * 保存long类型数据
	 */
	public void setLong(String name, long value) {
		Editor editor = mPreferences.edit();
		editor.putLong(name, value);
		editor.commit();
	}

	/**
	 * 保存long类型数据
	 */
	public long getLong(String name) {
		return mPreferences.getLong(name, -1);
	}

	/**
	 * 保存long类型数据
	 */
	public void setInt(String name, int value) {
		Editor editor = mPreferences.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	/**
	 * 保存boolean类型数据
	 */
	public void setBoolean(String name, boolean value) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	/**
	 * 保存long类型数据
	 */
	public long getInt(String name) {
		return mPreferences.getInt(name, -1);
	}

	/**
	 * 读取状态
	 */
	public boolean getBoolean(String key, boolean def) {
		return mPreferences.getBoolean(key, def);
	}

	/**
	 * 读取boolean状态
	 */
	public void putBoolean(String key, boolean val) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, val);
		editor.commit();
	}

	/**
	 * 保存String类型
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return mPreferences.getString(key, "");
	}

	public String getString(String key, String defValue) {
		return mPreferences.getString(key, defValue);
	}

	/**
	 * 保存string类型数据
	 */
	public void setString(String key, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void clear() {
		Editor editor = mPreferences.edit();
		editor.clear();
		editor.commit();
	}

}
