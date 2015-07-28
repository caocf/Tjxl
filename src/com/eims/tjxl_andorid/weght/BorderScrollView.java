package com.eims.tjxl_andorid.weght;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * BorderScrollView
 * <ul>
 * <li>onTop and onBottom response ScrollView</li>
 * <li>you can {@link #setOnBorderListener(OnBorderListener)} to set your top
 * and bottom response</li>
 * </ul>
 * 
 * @author trinea@trinea.cn 2013-5-21
 */
public class BorderScrollView extends ScrollView {

	private OnBorderListener onBorderListener;
	private View contentView;
	private int MAXScrollY;

	public BorderScrollView(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	public BorderScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	public BorderScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		doOnBorderListener();
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

	/**
	 * OnBorderListener, Called when scroll to top or bottom
	 * 
	 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a>
	 *         2013-5-22
	 */
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

	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	// Return false if we're scrolling in the x direction
	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}
}