package com.eims.tjxl_andorid.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.AppContext;

public class UIHelper {

	private static Toast mToast;
	public static Dialog mLoadDialog;
	private static int count;


	public static void showLoadDialog(Context context) {
		showLoadDialog(context, null);
	}

	public static void showLoadDialog(Context context, String msg) {
		if (context == null) {
			return;
		}
		if (context.isRestricted())
			if (mLoadDialog != null && mLoadDialog.isShowing())
				return;
		count++;
		if (count > 1) {
			count = 0;
			return;
		}
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View login_dialog = inflater.inflate(R.layout.load_doag, null);
		mLoadDialog = new Dialog(context, R.style.load_dialog);
		mLoadDialog.setCanceledOnTouchOutside(false);
		if (!TextUtils.isEmpty(msg)) {
			TextView messageTV = (TextView) login_dialog
					.findViewById(R.id.login_doag_name);
			messageTV.setText(msg);
		}
		mLoadDialog.setContentView(login_dialog);
		try {
			mLoadDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void cloesLoadDialog() {
		try {

			if (mLoadDialog != null && mLoadDialog.isShowing()) {
				mLoadDialog.dismiss();
				count = 0;
				mLoadDialog = null;
			}
		} catch (Exception e) {
		}
	}
	
	public static void showToast(int resId) {
		if (mToast == null)
			mToast = Toast.makeText(AppContext.getInstance(), AppContext
					.getInstance().getResources().getString(resId),
					Toast.LENGTH_LONG);
		mToast.setText(resId);
		mToast.show();
	}

	public static void showToast(String msg) {
		if (mToast == null)
			mToast = Toast.makeText(AppContext.getInstance(), msg,
					Toast.LENGTH_LONG);
		mToast.setText(msg);
		mToast.show();
	}

}
