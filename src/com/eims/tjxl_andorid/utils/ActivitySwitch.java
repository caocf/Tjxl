package com.eims.tjxl_andorid.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.AppManager;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: ActivitySwitch
 * @Description: Activity界面切换工具类
 * @Author zd
 * @date 2015年4月14日 上午11:20:10
 *************************************************************************** 
 */
public class ActivitySwitch {

	protected void openActivity(Class<?> pClass) {
		// openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	public static void openActivity(Class<?> pClass, Bundle pBundle,
			Activity conActivity) {
		Intent intent = new Intent(conActivity, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		conActivity.startActivity(intent);
		conActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 *
	 * @param pClass
	 * @param pBundle
	 */
	public static void openActivityForResult(Class<?> pClass, Bundle pBundle,
			Activity conActivity,int requestCode) {
		Intent intent = new Intent(conActivity, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		conActivity.startActivityForResult(intent,requestCode);
		conActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}

	/**
	 * 通过Action启动Activity
	 * 
	 * @param pAction
	 */
	// protected void openActivity(String pAction) {
	// openActivity(pAction, null);
	// }

	/**
	 * 通过Action启动Activity，并且含有Bundle数据
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle,
			Activity conActivity) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		conActivity.startActivity(intent);
		conActivity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}
	
	public static void finishActivity(Activity context){
		AppManager.getAppManager().finishActivity((Activity) context);
		((Activity) context).overridePendingTransition(
				R.anim.push_right_in, R.anim.push_right_out);
	}

}
