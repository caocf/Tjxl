package com.eims.tjxl_andorid.ui.product;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.FactoryImgGridAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.IUserInfo;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyGridView;

import org.apache.http.Header;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 * 专区类型 Created by kuangyong on 2015/6/24.
 */
public class JionFactoryActivity extends BaseActivity implements View.OnClickListener {
    public static final String UID = "uid";
    private String uid;//用户id
    private static final int QYJS=1;//企业介绍
    private static final int GSXX=2;//工商信息
    private static final int RYTP=3;//荣誉图片
    private static final int LXWM=4;//联系我们

    private HeadView head;
    //	选项卡
    private TextView btn_qyjs;//企业介绍
    private TextView btn_gsxx;//工商信息
    private TextView btn_rytp;//荣誉图片
    private TextView btn_lxwm;//联系我们

    //	展示内容
    private TextView tv_qyjs;//企业介绍

    private LinearLayout layout_gsxx;//工商信息
    private TextView tv_company;//公司名称
    private TextView tv_adress;//注册地址
    private TextView tv_license_code;//工商注册号
    private TextView tv_create_time;//厂家成立时间
    private TextView tv_representative;//法定代表人

    private MyGridView gv_imgs;//荣誉图片

    private LinearLayout layout_contact;//联系我们
    private TextView tv_company2;//公司名称
    private TextView tv_contact_person;//联系人
    private TextView tv_tel;//座机号码
    private TextView tv_phone;//手机号码
    private ImageView ivbigright;//图片3
    private ImageView ivright2;//图片2
    private ImageView ivright1;//图片1
    private ImageView ivleft2;//图片5
    private ImageView ivleft1;//图片4
    private ImageView ivbigleft;//图片6

    private IUserInfo iUserInfo;//加盟厂家

    private FactoryImgGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_factory_layout);
        uid = getIntent().getStringExtra(UID);
        findViews();
        setlistener();
        initheadview();
        getData();
    }

    private void setlistener() {
        btn_qyjs.setOnClickListener(this);
        btn_gsxx.setOnClickListener(this);
        btn_rytp.setOnClickListener(this);
        btn_lxwm.setOnClickListener(this);
    }


    private void findViews() {
        this.head = (HeadView) findViewById(R.id.head);
        ivbigright = (ImageView) findViewById(R.id.iv_big_right);
        ivright2 = (ImageView) findViewById(R.id.iv_right2);
        ivright1 = (ImageView) findViewById(R.id.iv_right1);
        ivleft2 = (ImageView) findViewById(R.id.iv_left2);
        ivleft1 = (ImageView) findViewById(R.id.iv_left1);
        ivbigleft = (ImageView) findViewById(R.id.iv_big_left);
        this.layout_contact = (LinearLayout) findViewById(R.id.layout_contact);
        this.tv_phone = (TextView) findViewById(R.id.tv_phone);
        this.tv_tel = (TextView) findViewById(R.id.tv_tel);
        this.tv_contact_person = (TextView) findViewById(R.id.tv_contact_person);
        this.tv_company2 = (TextView) findViewById(R.id.tv_company2);
        this.gv_imgs = (MyGridView) findViewById(R.id.gv_imgs);
        this.layout_gsxx = (LinearLayout) findViewById(R.id.layout_gsxx);
        this.tv_representative = (TextView) findViewById(R.id.tv_representative);
        this.tv_create_time = (TextView) findViewById(R.id.tv_create_time);
        this.tv_license_code = (TextView) findViewById(R.id.tv_license_code);
        this.tv_adress = (TextView) findViewById(R.id.tv_adress);
        this.tv_company = (TextView) findViewById(R.id.tv_company);
        this.tv_qyjs = (TextView) findViewById(R.id.tv_qyjs);
        this.btn_lxwm = (TextView) findViewById(R.id.btn_lxwm);
        this.btn_rytp = (TextView) findViewById(R.id.btn_rytp);
        this.btn_gsxx = (TextView) findViewById(R.id.btn_gsxx);
        this.btn_qyjs = (TextView) findViewById(R.id.btn_qyjs);
        this.head = (HeadView) findViewById(R.id.head);

    }

    private void initheadview() {
        head.setText("加盟厂家");
        head.setGobackVisible();
        head.setRightResource();
    }

    /**
     * 初始化UI
     */
    private void init() {
        changeBtnBg(btn_qyjs);
        showWitchOne(QYJS);

        tv_qyjs.setText(null != iUserInfo.getData() ? iUserInfo.getData().getF_introduction() : "");

        if(null!=iUserInfo.getUserMap()){
            tv_company.setText("公司名称："+iUserInfo.getUserMap().getF_company_name());
            tv_company2.setText("公司名称："+iUserInfo.getUserMap().getF_company_name());
            tv_adress.setText("注册地址："+iUserInfo.getUserMap().getF_address_info());
            tv_license_code.setText("工商注册号："+iUserInfo.getUserMap().getF_registration_mark());
            tv_create_time.setText("成立时间："+iUserInfo.getUserMap().getF_create_time());
            tv_representative.setText("法定代表人："+iUserInfo.getUserMap().getF_legal_representative());
        }

        adapter=new FactoryImgGridAdapter(mContext,getImgs(null != iUserInfo.getData() ? iUserInfo.getData().getF_honor_imgs() : ""));
        gv_imgs.setAdapter(adapter);

        if(null!=iUserInfo.getOurMap()){
            tv_contact_person.setText("联系人："+iUserInfo.getOurMap().getF_link_man());
            tv_tel.setText("座机号码："+iUserInfo.getOurMap().getF_link_moblie());
            tv_phone.setText("手机号码："+iUserInfo.getOurMap().getF_link_phone());
        }
        ArrayList<String> imgs=getImgs(null != iUserInfo.getData() ? iUserInfo.getData().getF_honor_imgs() : "");
        ArrayList<ImageView> imageViews=new ArrayList<ImageView>();
        imageViews.add(ivright1);
        imageViews.add(ivright2);
        imageViews.add(ivbigright);
        imageViews.add(ivleft1);
        imageViews.add(ivleft2);
        imageViews.add(ivbigleft);
        for (int i=0;i<(imgs.size()>6?6:imgs.size());i++){
            ImageManager.Load(imgs.get(i),imageViews.get(i));
        }

    }

    private ArrayList<String> getImgs(String img){
        ArrayList<String> imgs=new ArrayList<String>();
        String [] arrayImg=img.split(",");
        if(0!=arrayImg.length){
            for (int i=0;i<arrayImg.length;i++){
                imgs.add(arrayImg[i]);
            }
        }
        return imgs;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qyjs://企业介绍
                changeBtnBg(btn_qyjs);
                showWitchOne(QYJS);
                break;
            case R.id.btn_gsxx://工商信息
                changeBtnBg(btn_gsxx);
                showWitchOne(GSXX);
                break;
            case R.id.btn_rytp://荣誉图片
                changeBtnBg(btn_rytp);
                showWitchOne(RYTP);
                break;
            case R.id.btn_lxwm://联系我们
                changeBtnBg(btn_lxwm);
                showWitchOne(LXWM);
                break;
        }
    }

    /**
     * 显示哪个布局
     * @param witchOne
     */
    private void showWitchOne(int witchOne) {
        tv_qyjs.setVisibility(View.GONE);
        layout_gsxx.setVisibility(View.GONE);
        gv_imgs.setVisibility(View.GONE);
        layout_contact.setVisibility(View.GONE);
        switch (witchOne){
            case QYJS:
                tv_qyjs.setVisibility(View.VISIBLE);
               break;
            case GSXX:
                layout_gsxx.setVisibility(View.VISIBLE);
               break;
            case RYTP:
                gv_imgs.setVisibility(View.VISIBLE);
               break;
            case LXWM:
                layout_contact.setVisibility(View.VISIBLE);
               break;
        }
    }

    private void changeBtnBg(TextView showTextView) {
        btn_qyjs.setBackgroundResource(R.color.gary_bg_btn);
        btn_gsxx.setBackgroundResource(R.color.gary_bg_btn);
        btn_rytp.setBackgroundResource(R.color.gary_bg_btn);
        btn_lxwm.setBackgroundResource(R.color.gary_bg_btn);
        showTextView.setBackgroundResource(R.drawable.bg_white_red);
    }

    /**
     * 请求数据
     */
    private void getData() {
        CustomResponseHandler hanndHandler = new CustomResponseHandler(mContext, true, "正在加载...") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                iUserInfo = IUserInfo.explainJson(content, mContext);
                if (null != iUserInfo) {
                    init();
                }
            }
        };
        RequestParams params = new RequestParams();
        params.put("sellerId", uid);
        HttpClient.iUserInfo(hanndHandler, params);
    }

}
