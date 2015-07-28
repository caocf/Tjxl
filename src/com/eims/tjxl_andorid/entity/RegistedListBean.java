package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 已注册鞋店
 * Created by kuangyong on 2015/6/28.
 */
public class RegistedListBean extends BaseBean{

    private String s_store_name;//店铺名称
    private String s_adv_img;//店铺图片
    private String s_address_info;//店铺地址

    public String getS_store_name() {
        return s_store_name;
    }

    public void setS_store_name(String s_store_name) {
        this.s_store_name = s_store_name;
    }

    public String getS_adv_img() {
        return s_adv_img;
    }

    public void setS_adv_img(String s_adv_img) {
        this.s_adv_img = s_adv_img;
    }

    public String getS_address_info() {
        return s_address_info;
    }

    public void setS_address_info(String s_address_info) {
        this.s_address_info = s_address_info;
    }
}
