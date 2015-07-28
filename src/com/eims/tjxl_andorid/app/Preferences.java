package com.eims.tjxl_andorid.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
	
	/**
	 * shared文件名叫parenting.xml
	 */
	private static String SHARED_NAME 			= "tjxl";
	

	public static class Key{
		/**账户信息**/
		public static final String USERINFO 		= "user_info";
		public static final String USER_STORE_INFO 		= "user_store_info";
		public static final String IS_RECORD_PWD 		= "is_record_pwd";//记住密码
		public static final String USER_NAME 		= "user_name";//用户名
		public static final String USER_PWD 		= "pwd";//密码
	}
	
	/**
	 * 获取sharedpreference对象
	 * @return
	 */
	private static SharedPreferences getSharedPreferences(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
		return sharedPreferences;
	}
	
	
	/**
	 * 获取一个键值(String)
	 * @param key
	 * @return
	 */
	public static String getString(Context context,String key){
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		return sharedPrefrence.getString(key, "");
	}
	
	
	/**
	 * 获取一个键值(String)
	 * @param key
	 * @return
	 */
	public static String getString(Context context,String key, String defaultValue){
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		return sharedPrefrence.getString(key, defaultValue);
	}
	
	/**
	 * 设置一个键值(String)
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putString(Context context,String key, String value){
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		Editor editor =sharedPrefrence.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	
	/**
	 * 获取一个键值(int)
	 * @param key
	 * @return
	 */
	public static int getInt(Context context,String key){
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		return sharedPrefrence.getInt(key, 0);
	}
	
	/**
	 * 设置一个键值(int)
	 * @param key
	 * @param value
	 * @return
	 */

	public static boolean putInt(Context context,String key, int value){
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		Editor editor =sharedPrefrence.edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	
	/**
	 * 获取boolean值
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context,String key) {
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		return sharedPrefrence.getBoolean(key, false);
	}
	
	/**
	 * 获取boolean值
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context,String key, boolean defaultValue) {
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		return sharedPrefrence.getBoolean(key, defaultValue);
	}
	
	/**
	 * 设置boolean值
	 * @param key
	 * @param value
	 */
	public static boolean putBoolean(Context context,String key, boolean value){
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		Editor editor =sharedPrefrence.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}
	
	/**
	 * 移除一个key值
	 * @param key
	 * @return
	 */
	public static boolean remove(Context context,String key){
		SharedPreferences sharedPrefrence = getSharedPreferences(context);
		Editor editor =sharedPrefrence.edit();
		editor.remove(key);
		return editor.commit();
	}
	
}
