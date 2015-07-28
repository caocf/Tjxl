package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 已注册店铺列表
 * Created by kuangyong on 2015/6/28.
 */
public class RegistedListPage implements Serializable {
    private ArrayList<RegistedListBean> msg;//个人资料
    public ArrayList<RegistedListBean> getData() {
        return msg;
    }
    public int type;//Gson
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setData(ArrayList<RegistedListBean> msg) {
        this.msg = msg;
    }
    public static RegistedListPage explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            RegistedListPage bean=gson.fromJson(json,RegistedListPage.class);
            if(null!=bean){
                if (bean.getType() <= 0) {
                    String msg=new JSONObject(json).optString("msg");
                    Toast.makeText(context, msg,
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
