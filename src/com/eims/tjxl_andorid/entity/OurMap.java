package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 联系我们
 * @author kuangyong
 *
 */
public class OurMap extends BaseBean {
    private String f_link_man;              //联系人
    private String f_link_moblie;           //联系电话
    private String  f_link_phone;           //联系手机

    public String getF_link_man() {
        return f_link_man;
    }

    public void setF_link_man(String f_link_man) {
        this.f_link_man = f_link_man;
    }

    public String getF_link_moblie() {
        return f_link_moblie;
    }

    public void setF_link_moblie(String f_link_moblie) {
        this.f_link_moblie = f_link_moblie;
    }

    public String getF_link_phone() {
        return f_link_phone;
    }

    public void setF_link_phone(String f_link_phone) {
        this.f_link_phone = f_link_phone;
    }
}
