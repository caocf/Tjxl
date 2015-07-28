package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * 系统消息列表
 * Created by kuangyong on 2015/6/30.
 */
public class IQueryLetterPageBean extends BaseBean{

    private ArrayList<MessageBean> data;//消息列表

    public ArrayList<MessageBean> getData() {
        return data;
    }

    public void setData(ArrayList<MessageBean> data) {
        this.data = data;
    }

    public static IQueryLetterPageBean explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            IQueryLetterPageBean bean=gson.fromJson(json,IQueryLetterPageBean.class);
            if(null!=bean){
                if (bean.getType() != 1) {
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
