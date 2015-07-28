package com.eims.tjxl_andorid.utils;

import java.util.Set;
import java.util.TreeSet;

import com.eims.tjxl_andorid.ui.setting.PictQualitySettingFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("NewApi")
public class SharedPreferencesUtils {

	public static String SP_NAME = "tjxlInfo";
	private static SharedPreferences sp;

	public static final String KEY_GLOBAL_SEARCH_HISTORY = "global_search_history";
	public static final String KEY_FACTORY_SEARCH_HISTORY = "factory_search_history";

	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);

		}
		return sp.getBoolean(key, defValue);
	}

	public static void saveString(Context context, String key, String value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);

		}
		return sp.getString(key, defValue);
	}

	public static void saveInt(Context context, String key, int value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(Context context, String key, int defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getInt(key, defValue);
	}
	
	public static void saveGlobalSearchHistory(Context context,Set<String> historySet){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		sp.edit().putStringSet(KEY_GLOBAL_SEARCH_HISTORY, historySet).commit();
	}
	
	public static Set<String> getGlobalSearchHistory(Context context){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getStringSet(KEY_GLOBAL_SEARCH_HISTORY, new TreeSet<String>());
	}
	
	public static void saveFactorylSearchHistory(Context context,Set<String> factorySet){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		sp.edit().putStringSet(KEY_FACTORY_SEARCH_HISTORY, factorySet).commit();
	}
	
	public static Set<String> getFactorylSearchHistory(Context context){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getStringSet(KEY_FACTORY_SEARCH_HISTORY, new TreeSet<String>());
	}

	/**
	 * 存储图片质量设置
	 */
	public static void setPicQualityMode(Context context, int modeIndex){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		sp.edit().putInt(PictQualitySettingFragment.KEY_PIC_MODE, modeIndex).commit();
	}
	/**
	 * 获取图片质量设置
	 */
	public static int getPicQualityMode(Context context){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getInt(PictQualitySettingFragment.KEY_PIC_MODE, 0);
	}
}
