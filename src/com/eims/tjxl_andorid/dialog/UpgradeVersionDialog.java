package com.eims.tjxl_andorid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.eims.tjxl_andorid.R;

public class UpgradeVersionDialog extends Dialog{

	public UpgradeVersionDialog(Context context, int theme) {
		super(context,theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.dialog_upgrade_version);
	}

}
