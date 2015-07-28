package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 验证
 * Created by kuangyong on 2015/6/30.
 */
public class IsExistUserNameBean extends BaseBean{

    private ValidateWayBean data;//消息

    public ValidateWayBean getData() {
        return data;
    }

    public void setData(ValidateWayBean data) {
        this.data = data;
    }

    public static IsExistUserNameBean explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            IsExistUserNameBean bean=gson.fromJson(json,IsExistUserNameBean.class);
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
