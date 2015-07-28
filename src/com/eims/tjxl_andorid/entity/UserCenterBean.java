package com.eims.tjxl_andorid.entity;

import android.content.Context;

import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by kuangyong on 2015/6/27.
 */
public class UserCenterBean extends BaseBean{

    private IQueryUserCenterInfoBean data;//账户中心数据

    public IQueryUserCenterInfoBean getData() {
        return data;
    }

    public void setData(IQueryUserCenterInfoBean data) {
        this.data = data;
    }

    public static UserCenterBean explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            UserCenterBean bean=gson.fromJson(json,UserCenterBean.class);
            Preferences.putString(context,Preferences.Key.USERINFO, json);//把会员中心信息保存到本地
            return bean;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
