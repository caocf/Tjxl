package com.eims.tjxl_andorid.dialog;

import com.eims.tjxl_andorid.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

public class TextMessageDialog extends Dialog {

	private TextView message;

	public TextMessageDialog(Context context) {
		super(context, R.style.load_dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_text_message);
		message = (TextView) findViewById(R.id.message);
	}


	public void setMessage(String msg) {
		message.setText(msg);
	}

	public void setMessage(int msg) {
		message.setText(msg);
	}
}
