package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 加盟厂家
 * @author kuangyong
 *
 */
public class FactoryInfoBean extends BaseBean {
    private String f_introduction;            //公司简介
    private String f_honor_imgs;              //荣誉图片
    private String  f_info_imgs;              //厂家图片集

    public String getF_introduction() {
        return f_introduction;
    }

    public void setF_introduction(String f_introduction) {
        this.f_introduction = f_introduction;
    }

    public String getF_honor_imgs() {
        return f_honor_imgs;
    }

    public void setF_honor_imgs(String f_honor_imgs) {
        this.f_honor_imgs = f_honor_imgs;
    }

    public String getF_info_imgs() {
        return f_info_imgs;
    }

    public void setF_info_imgs(String f_info_imgs) {
        this.f_info_imgs = f_info_imgs;
    }
}
