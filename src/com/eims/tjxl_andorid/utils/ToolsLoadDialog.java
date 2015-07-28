/**
 * 
 */
package com.eims.tjxl_andorid.utils;

import com.eims.tjxl_andorid.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description:  数据正在加载提示
 * @Author zd
 * @date 2015年5月18日  下午2:48:38
 *************************************************************************** 
 */
public class ToolsLoadDialog {
	
	public static Dialog mLoadDialog;

	public static void showLoadDialog(Context context, String msg) {
		if (context == null) {
			return;
		}
		if (mLoadDialog != null && mLoadDialog.isShowing()) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View login_doag = inflater.inflate(R.layout.load_doag, null);
		((TextView) login_doag.findViewById(R.id.login_doag_name)).setText(msg);
		mLoadDialog = new Dialog(context, R.style.DialogStyle);
		mLoadDialog.setCanceledOnTouchOutside(false);
		mLoadDialog.setContentView(login_doag);
		//不让dialog背景变暗
		 Window window = mLoadDialog.getWindow();  
         WindowManager.LayoutParams params = window.getAttributes();  
         params.dimAmount = 0f;  
         window.setAttributes(params);     
		
		  mLoadDialog.show();
	}

	public static void colesLoadDialog() {
		if (mLoadDialog != null && mLoadDialog.isShowing()) {
			mLoadDialog.dismiss();
			mLoadDialog = null;
		}
	}

}
