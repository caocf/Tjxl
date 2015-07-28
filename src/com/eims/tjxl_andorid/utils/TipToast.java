package com.eims.tjxl_andorid.utils;
import com.eims.tjxl_andorid.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class TipToast extends Toast {

	public TipToast(Context context) {
		super(context);
	}

	public static TipToast makeText(Context context, CharSequence text,
			int duration) {
		TipToast result = new TipToast(context);

		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.tips_toast_view, null);
		TextView tv = (TextView) v.findViewById(R.id.tips_msg);
		tv.setText(text);

		result.setView(v);
		// setGravity方法用于设置位置，此处为垂直居中
		result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		result.setDuration(duration);

		return result;
	}

	public static TipToast makeText(Context context, int resId, int duration)
			throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId),
				duration);
	}

	public void setIcon(int iconResId) {
		if (getView() == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		ImageView iv = (ImageView) getView().findViewById(R.id.tips_icon);
		if (iv == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		iv.setImageResource(iconResId);
	}

	@Override
	public void setText(CharSequence s) {
		if (getView() == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		TextView tv = (TextView) getView().findViewById(R.id.tips_msg);
		if (tv == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		tv.setText(s);
	}
}
