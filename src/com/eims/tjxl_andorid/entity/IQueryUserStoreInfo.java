package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 店铺个人中心
 * Created by kuangyong on 2015/6/28.
 */
public class IQueryUserStoreInfo extends BaseBean{
    private UserInfoBean data;//个人资料
    public UserInfoBean getData() {
        return data;
    }

    public void setData(UserInfoBean data) {
        this.data = data;
    }
    public static IQueryUserStoreInfo explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            IQueryUserStoreInfo bean=gson.fromJson(json,IQueryUserStoreInfo.class);
            if(null!=bean){
                if (bean.getType() <= 0) {
                    Toast.makeText(context, bean.getMsg(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }else{
                    Preferences.putString(context, Preferences.Key.USER_STORE_INFO, json);//把会员信息保存到本地
                }
            }
            return bean;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
