package com.eims.tjxl_andorid.dialog;

import com.eims.tjxl_andorid.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DeleteCollectionDialog extends Dialog{

	private static final String TAG = "DeleteCollectionDialog";
	private TextView tv_msg;
	private Button btn_ok,btn_cancel;
	public DeleteCollectionDialog(Context context) {
		super(context,R.style.dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_delete_collection);
		setView();
	}
	
	private void setView(){
		btn_ok = (Button) findViewById(R.id.button_ok);
		btn_cancel = (Button) findViewById(R.id.button_cancel);
		tv_msg = (TextView) findViewById(R.id.tv_dialog_msg);
	}
	
	public void setOnclickListener(android.view.View.OnClickListener listener){
		btn_ok.setOnClickListener(listener);
		btn_cancel.setOnClickListener(listener); 
	}

	public void setMsg(String msg){
		tv_msg.setText(msg);
	}
}
