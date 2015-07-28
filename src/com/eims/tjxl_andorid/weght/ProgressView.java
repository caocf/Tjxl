package com.eims.tjxl_andorid.weght;

import com.eims.tjxl_andorid.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProgressView extends Dialog {

	private Context mContext;
	private View contentView;
	private ImageView mLoadingImg;
	private LinearLayout mLinearLayout;
	private TextView mTipView;
	private View mMutixView;

	public ProgressView(Context context) {
		super(context);
		this.mContext = context;
		findViewbyIds();
	}

	public ProgressView(Context context, int attrs) {
		super(context, attrs);
		this.mContext = context;
		findViewbyIds();
	}

	private void findViewbyIds() {
		// LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT);
		contentView = LayoutInflater.from(mContext).inflate(
				R.layout.progress_layout, null);
		mLoadingImg = (ImageView) contentView.findViewById(R.id.loading_img);
		mLinearLayout = (LinearLayout) contentView
				.findViewById(R.id.tiplinearlayout);
		mTipView = (TextView) contentView.findViewById(R.id.error_tv);
		setCanceledOnTouchOutside(false);
		setContentView(contentView);
	}

	public void setMutixView(View mutixview) {
		this.mMutixView = mutixview;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Loading();
		super.show();
	}

	public void Loading() {
		startVisable();
		mLoadingImg.setVisibility(View.VISIBLE);
		mLinearLayout.setVisibility(View.GONE);
		mLoadingImg.setVisibility(View.VISIBLE);
		AnimationDrawable imgAnim = (AnimationDrawable) mLoadingImg
				.getBackground();
		imgAnim.setOneShot(false);
		if (imgAnim.isRunning()) {
			imgAnim.stop();
		}
		imgAnim.start();
	}

	public void RetryAndTips(View.OnClickListener listener, String tipMsg) {
		ProgressView.this.startVisable();
		mLoadingImg.setVisibility(View.GONE);
		mLinearLayout.setVisibility(View.VISIBLE);
		mTipView.setText(tipMsg);
	}

	public void startVisable() {
		contentView.setVisibility(View.VISIBLE);
		if (mMutixView != null) {
			mMutixView.setVisibility(View.GONE);
		}
	}

	public void finishVisable() {
		contentView.setVisibility(View.GONE);
		if (mMutixView != null) {
			mMutixView.setVisibility(View.VISIBLE);
		}
	}

}
