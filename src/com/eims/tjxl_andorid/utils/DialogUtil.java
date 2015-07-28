package com.eims.tjxl_andorid.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.eims.tjxl_andorid.R;

import java.util.Calendar;


public class DialogUtil {



	/**
	 * 显示性别对话框
	 * 
	 * @param context
	 */
	public static void showArrayDialog(Context context,final TextView selectView, int resArray, final OnClickListener onclick) {
		final String[] array = context.getResources().getStringArray(resArray);
		new AlertDialog.Builder(context).setItems(array, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				dialog.dismiss();
				selectView.setText(array[position]);
				onclick.onClick(selectView);
			}
		}).show();
	}

	/**
	 * 显示日期（生日）对话框
	 * 
	 * @param birthdayTextView
	 * @param context
	 */
	public static void showBirthdayDialog(final TextView birthdayTextView, Context context) {
		showBirthdayDialog(birthdayTextView, null, context);
	}
	
	/**
	 * 显示日期（生日）对话框
	 * 
	 * @param birthdayTextView
	 * @param context
	 */
	public static void showBirthdayDialog(final TextView birthdayTextView, final View.OnClickListener onOkClick, Context context) {
		
		final String oldBirthday = birthdayTextView.getText().toString().trim();
		
		Calendar c = Calendar.getInstance();
		int[] date = getDateInBirthdayItem(birthdayTextView.getText().toString().trim());
		if (null != date) {
			c.set(date[0], date[1] - 1, date[2]);
		}
		Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				
				String newBirthday = year + "-" + format(monthOfYear + 1) + "-" + format(dayOfMonth);
				
				//生日做了修改才更新到服务器
				if(!oldBirthday.equals(newBirthday)){
					birthdayTextView.setText(newBirthday);
					if(null != onOkClick){
						onOkClick.onClick(birthdayTextView);
					}
				}
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

	}

	/**
	 * 日期时间显示两位数的方法
	 * 
	 * @param x
	 *            Utils.format(月+1)
	 * @return
	 */
	public final static String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
	
	/**
	 * 显示时间对话框
	 */
	public static void showTimeDialog(final TextView timeTextView, Context context) {
		final Calendar c = Calendar.getInstance();
		int[] date = getTimeItem(timeTextView.getText().toString().trim());
		if (null != date) {
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			c.set(year, month, day, date[0], date[1], date[2]);

		}
		Dialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				c.setTimeInMillis(System.currentTimeMillis());
				c.set(Calendar.HOUR_OF_DAY, hourOfDay);
				c.set(Calendar.MINUTE, minute);
				c.set(Calendar.SECOND, 0); // 设为 0
				c.set(Calendar.MILLISECOND, 0); // 设为 0
				timeTextView.setText(DateUtil.getTimeShort(c.getTime()));
			}
		}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
		dialog.show();
	}

	private static int[] getDateInBirthdayItem(String date) {
		if (TextUtils.isEmpty(date)) {
			return null;
		}

		String[] dates = date.split("-");
		int[] bir = new int[dates.length];
		bir[0] = Integer.parseInt(dates[0]);
		bir[1] = Integer.parseInt(dates[1]);
		try {
			bir[2] = Integer.parseInt(dates[2]);
		} catch (NumberFormatException nfe) {
			bir[2] = Integer.parseInt(dates[2].split(" ")[0].trim());
		}
		return bir;
	}

	private static int[] getTimeItem(String time) {
		if (TextUtils.isEmpty(time)) {
			return null;
		}

		String[] dates = time.split(":");
		int[] bir = new int[dates.length];
		bir[0] = Integer.parseInt(dates[0]);
		bir[1] = Integer.parseInt(dates[1]);
		try {
			bir[2] = Integer.parseInt(dates[2]);
		} catch (NumberFormatException nfe) {
			bir[2] = Integer.parseInt(dates[2].split(" ")[0].trim());
		} catch (IndexOutOfBoundsException nfe) {
			bir[2] = 0;
		}
		return bir;
	}


	/** 设置对话框的宽度 **/
	public static void changeDialogWidth(Dialog dialog, Context mContext) {
		if (null != dialog) {
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			int width = mContext.getResources().getDimensionPixelSize(R.dimen.px_to_dip_236);
			params.width = width;
			dialog.getWindow().setAttributes(params);
		}
	}


	/**
	 * 获取对话框的message
	 * 
	 * @param message
	 * @param name
	 * @return
	 */
	public static SpannableStringBuilder setContentDsc(Context context, String message, String name, int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder(message);
		ForegroundColorSpan orSpan = new ForegroundColorSpan(context.getResources().getColor(color));
		int index = message.indexOf(name);
		int length = name.length();
		builder.setSpan(orSpan, index, index + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}
}
