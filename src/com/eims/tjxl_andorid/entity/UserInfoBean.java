package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 账户信息
 * @author kuangyong
 *
 */
public class UserInfoBean extends BaseBean {
    private String username;            //用户名
    private String birth;               //生日
    private int check_status;           //审核状态（0待审核，1审核通过，2审核不通过，3资料未完善）
    private int sex;                 //性别 0男 1女
    private String  true_name;          //真实姓名
    private String id_card;             //身份证
    private String s_store_name;        //店铺名称
    private String s_province_id;       //省ID
    private String s_province_name;     //省名称
    private String s_city_id;           //市ID
    private String s_city_name;         //市名称
    private String s_town_id;           //县ID
    private String s_town_name;         //县名称
    private String s_address_info;      //街道
    private String phone;               //手机
    private String is_verify_phone;     //手机是否验证（0否，1是）
    private String email;               //邮箱
    private String is_verify_email;     //邮箱是否验证（0否，1是）
    private String head_img;            //头像
    private String s_business_license;  //营业执照
    private String sys_time;//系统当前时间

    public String getSys_time() {
        return sys_time;
    }

    public void setSys_time(String sys_time) {
        this.sys_time = sys_time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirth() {
        String birthFormmater="";
        if(null!=birth&&!birth.equals("")){
            if(birth.contains("-")){
                String [] data=birth.split("-");
                if(2==data.length){//只有月、日
                    birthFormmater=data[0]+"月"+data[1]+"日";
                }else if(3==data.length){//有年、月、日
                    birthFormmater=data[0]+"年"+data[1]+"月"+data[2]+"日";
                }
            }
        }
        return birthFormmater;
    }

    public String getBirthWithGign() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getCheck_status() {
        return check_status;
    }

    public void setCheck_status(int check_status) {
        this.check_status = check_status;
    }

    public String getSex() {
        return sex==0?"男":"女";
    }

    public int getSexInteger() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getS_store_name() {
        return s_store_name;
    }

    public String getAdress(){
       return s_province_name+" "+s_city_name+" "+s_town_name;
    }

    public void setS_store_name(String s_store_name) {
        this.s_store_name = s_store_name;
    }

    public String getS_province_id() {
        return s_province_id;
    }

    public void setS_province_id(String s_province_id) {
        this.s_province_id = s_province_id;
    }

    public String getS_province_name() {
        return s_province_name;
    }

    public void setS_province_name(String s_province_name) {
        this.s_province_name = s_province_name;
    }

    public String getS_city_id() {
        return s_city_id;
    }

    public void setS_city_id(String s_city_id) {
        this.s_city_id = s_city_id;
    }

    public String getS_city_name() {
        return s_city_name;
    }

    public void setS_city_name(String s_city_name) {
        this.s_city_name = s_city_name;
    }

    public String getS_town_id() {
        return s_town_id;
    }

    public void setS_town_id(String s_town_id) {
        this.s_town_id = s_town_id;
    }

    public String getS_town_name() {
        return s_town_name;
    }

    public void setS_town_name(String s_town_name) {
        this.s_town_name = s_town_name;
    }

    public String getS_address_info() {
        return s_address_info;
    }

    public void setS_address_info(String s_address_info) {
        this.s_address_info = s_address_info;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_verify_phone() {
        return is_verify_phone;
    }

    public void setIs_verify_phone(String is_verify_phone) {
        this.is_verify_phone = is_verify_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_verify_email() {
        return is_verify_email;
    }

    public void setIs_verify_email(String is_verify_email) {
        this.is_verify_email = is_verify_email;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getS_business_license() {
        return s_business_license;
    }

    public void setS_business_license(String s_business_license) {
        this.s_business_license = s_business_license;
    }
}
