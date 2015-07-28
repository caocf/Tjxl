package com.eims.tjxl_andorid.base;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;

public class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;//Gson
	public String msg;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static BaseBean explainJson(String json,Context context){
		try {
			Gson gson=new Gson();
			BaseBean bean=gson.fromJson(json,BaseBean.class);
			if(null!=bean){
				if (bean.getType() <= 0) {
					Toast.makeText(context, bean.getMsg(),
							Toast.LENGTH_LONG).show();
					return null;
				}
			}
			return bean;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
