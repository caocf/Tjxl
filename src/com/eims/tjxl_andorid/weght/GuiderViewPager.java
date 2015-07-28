package com.eims.tjxl_andorid.weght;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GuiderViewPager extends ViewPager{
	
	public static boolean isCanScrollLeft = false;

	public GuiderViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return super.onTouchEvent(arg0);
	}
}
