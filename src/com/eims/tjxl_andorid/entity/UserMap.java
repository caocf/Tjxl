package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 工商信息集合
 * @author kuangyong
 *
 */
public class UserMap extends BaseBean {
    private String f_company_name;              //公司名称
    private String f_address_info;              //地址
    private String  f_registration_mark;        //工商注册号
    private String  f_create_time;              //厂家成立时间
    private String  f_legal_representative;     //法定代表人

    public String getF_company_name() {
        return f_company_name;
    }

    public void setF_company_name(String f_company_name) {
        this.f_company_name = f_company_name;
    }

    public String getF_address_info() {
        return f_address_info;
    }

    public void setF_address_info(String f_address_info) {
        this.f_address_info = f_address_info;
    }

    public String getF_registration_mark() {
        return f_registration_mark;
    }

    public void setF_registration_mark(String f_registration_mark) {
        this.f_registration_mark = f_registration_mark;
    }

    public String getF_create_time() {
        return f_create_time;
    }

    public void setF_create_time(String f_create_time) {
        this.f_create_time = f_create_time;
    }

    public String getF_legal_representative() {
        return f_legal_representative;
    }

    public void setF_legal_representative(String f_legal_representative) {
        this.f_legal_representative = f_legal_representative;
    }
}
