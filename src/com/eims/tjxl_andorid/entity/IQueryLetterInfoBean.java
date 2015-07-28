package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 系统消息
 * Created by kuangyong on 2015/6/30.
 */
public class IQueryLetterInfoBean extends BaseBean{

    private MessageDetailBean data;//消息

    public MessageDetailBean getData() {
        return data;
    }

    public void setData(MessageDetailBean data) {
        this.data = data;
    }

    public static IQueryLetterInfoBean explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            IQueryLetterInfoBean bean=gson.fromJson(json,IQueryLetterInfoBean.class);
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
