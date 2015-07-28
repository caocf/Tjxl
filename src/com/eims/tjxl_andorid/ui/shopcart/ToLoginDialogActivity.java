package com.eims.tjxl_andorid.ui.shopcart;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.utils.TipToast;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ToLoginDialogActivity extends Dialog {

	Context context;

	public ToLoginDialogActivity(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	private Button okBtn, continuBtn;
	private TextView mTitleTV, messageTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.goods_shopping_car_dialog_layout_zd);
		
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub

		mTitleTV = (TextView) findViewById(R.id.tip_tv);
		mTitleTV.setText("温馨提示");
		messageTV = (TextView) findViewById(R.id.message_tv);
		messageTV.setVisibility(View.GONE);
		okBtn = (Button) findViewById(R.id.ok_btn);
		okBtn.setText("去登录");
		okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(context, LoginActivity.class));
				dismiss();
			}
		});
		continuBtn = (Button) findViewById(R.id.continue_btn);
		continuBtn.setText("继续");
		continuBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

}
