package com.eims.tjxl_andorid.weght.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class NestScrollView extends ScrollView {

	public NestScrollView(Context context) {
		super(context);
	}

	public NestScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NestScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private float xDistance, yDistance;
	private float xLast, yLast;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;
			if (xDistance > yDistance) {
				return false;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		doOnBorderListener();
	}


	/**
	 * OnBorderListener, Called when scroll to top or bottom
	 *
	 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a>
	 *         2013-5-22
	 */

	private OnBorderListener onBorderListener;
	private View contentView;
	private int MAXScrollY;

	public static interface OnBorderListener {

		/**
		 * Called when scroll to bottom
		 */
		public void onBottom();

		/**
		 * Called when scroll to top
		 */
		public void onTop();

		/**
		 * 既不在顶 也不再底
		 */
		public void onother();
	}

	public void setOnBorderListener(final OnBorderListener onBorderListener) {
		this.onBorderListener = onBorderListener;
		if (onBorderListener == null) {
			return;
		}

		if (contentView == null) {
			contentView = getChildAt(0);
		}
	}

	private void doOnBorderListener() {
		if (contentView != null
				&& contentView.getMeasuredHeight() <= getScrollY()
				+ getHeight()) {
			MAXScrollY = getScrollY();
			if (onBorderListener != null) {
				onBorderListener.onBottom();
			}
		} else if (getScrollY() == 0) {
			if (onBorderListener != null) {
				onBorderListener.onTop();
			}
		} else if (getScrollY() < MAXScrollY - 30) {
			if (onBorderListener != null) {
				onBorderListener.onother();
			}
		}
	}


}
