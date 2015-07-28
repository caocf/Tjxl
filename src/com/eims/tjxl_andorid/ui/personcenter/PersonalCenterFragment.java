package com.eims.tjxl_andorid.ui.personcenter;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.db.dao.ProductDao;
import com.eims.tjxl_andorid.db.dbmodel.Product;
import com.eims.tjxl_andorid.entity.IQueryUserCenterInfoBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.entity.UserCenterBean;
import com.eims.tjxl_andorid.ui.setting.SettingActivity;
import com.eims.tjxl_andorid.ui.shopcart.OrderListActivity;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.ui.user.MessageListActivity;
import com.eims.tjxl_andorid.ui.user.MyCollectionsActivity;
import com.eims.tjxl_andorid.ui.user.RegisterActivity;
import com.eims.tjxl_andorid.ui.user.UserInfoActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.CircularImage;
import com.google.gson.Gson;

import loopj.android.http.RequestParams;

/**
 * *************************************************************************
 *
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月14日 下午3:10:29
 * **************************************************************************
 */
public class PersonalCenterFragment extends BaseLazyFragment implements OnClickListener {

    public static PersonalCenterFragment personalCenterFragment;
    private View view;
    private LinearLayout ll_login_btn;
    private TextView mUsername;//用户名
    private TextView btnLogin;
    private com.eims.tjxl_andorid.weght.CircularImage ciuser;//用户头像
    private android.widget.ImageView btnsetting;//设置按钮
    private android.widget.TextView tvdsk;//待付款文本
    private android.widget.LinearLayout btndsk;//待付款按钮
    private android.widget.TextView tvdfh;//待发货文本
    private android.widget.LinearLayout btndfh;//待发货按钮
    private android.widget.TextView tvdsh;//待收货文本
    private android.widget.LinearLayout btndsh;//待收货按钮
    private android.widget.TextView tvdpj;//待评价文本
    private android.widget.LinearLayout btndpj;//待评价按钮
    private android.widget.TextView tvshz;//售后中文本
    private android.widget.LinearLayout btnshz;//售后中按钮
    private android.widget.TextView tvordercenter;//订单中心文本
    private android.widget.LinearLayout btnordercenter;//订单中心按钮
    private android.widget.TextView tvcollectionpro;//收藏商品文本
    private android.widget.LinearLayout btncollectionpro;//收藏商品按钮
    private android.widget.TextView tvcollectionfac;//收藏厂家文本
    private android.widget.LinearLayout btncollectionfac;//收藏厂家按钮
    private android.widget.TextView tvmessagecenter;//系统消息文本
    private android.widget.LinearLayout btnmessagecenter;//系统消息按钮
    private android.widget.TextView tvuserinfo;//账户信息文本
    private android.widget.LinearLayout btnuserinfo;//账户信息按钮
    private boolean isFlag = false;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    initdata();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public static PersonalCenterFragment getInstance() {
        if (personalCenterFragment == null) {
            personalCenterFragment = new PersonalCenterFragment();
        }
        return personalCenterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me_center_layout, null);
        //System.out.println("onCreateView.....");
        findviews();
        lazyload();
        initView(explainLocalUserInfo());
        setListener();
        return view;
    }

    private void setListener() {
        btnuserinfo.setOnClickListener(this);
        this.btnuserinfo.setOnClickListener(this);
        this.btnmessagecenter.setOnClickListener(this);
        this.btncollectionfac.setOnClickListener(this);
        this.btncollectionpro.setOnClickListener(this);
        this.btnordercenter.setOnClickListener(this);
        this.btnshz.setOnClickListener(this);
        this.btndpj.setOnClickListener(this);
        this.btndsh.setOnClickListener(this);
        this.btndfh.setOnClickListener(this);
        this.btndsk.setOnClickListener(this);
        this.btnsetting.setOnClickListener(this);
        this.ciuser.setOnClickListener(this);
    }


    private void findviews() {
        ll_login_btn = (LinearLayout) view.findViewById(R.id.login_btn);
        this.btnuserinfo = (LinearLayout) view.findViewById(R.id.btn_user_info);
        this.tvuserinfo = (TextView) view.findViewById(R.id.tv_user_info);
        this.btnmessagecenter = (LinearLayout) view.findViewById(R.id.btn_message_center);
        this.tvmessagecenter = (TextView) view.findViewById(R.id.tv_message_center);
        this.btncollectionfac = (LinearLayout) view.findViewById(R.id.btn_collection_fac);
        this.tvcollectionfac = (TextView) view.findViewById(R.id.tv_collection_fac);
        this.btncollectionpro = (LinearLayout) view.findViewById(R.id.btn_collection_pro);
        this.tvcollectionpro = (TextView) view.findViewById(R.id.tv_collection_pro);
        this.btnordercenter = (LinearLayout) view.findViewById(R.id.btn_order_center);
        this.tvordercenter = (TextView) view.findViewById(R.id.tv_order_center);
        this.btnshz = (LinearLayout) view.findViewById(R.id.btn_shz);
        this.tvshz = (TextView) view.findViewById(R.id.tv_shz);
        this.btndpj = (LinearLayout) view.findViewById(R.id.btn_dpj);
        this.tvdpj = (TextView) view.findViewById(R.id.tv_dpj);
        this.btndsh = (LinearLayout) view.findViewById(R.id.btn_dsh);
        this.tvdsh = (TextView) view.findViewById(R.id.tv_dsh);
        this.btndfh = (LinearLayout) view.findViewById(R.id.btn_dfh);
        this.tvdfh = (TextView) view.findViewById(R.id.tv_dfh);
        this.btndsk = (LinearLayout) view.findViewById(R.id.btn_dsk);
        this.tvdsk = (TextView) view.findViewById(R.id.tv_dsk);
        this.btnsetting = (ImageView) view.findViewById(R.id.btn_setting);
        this.ciuser = (CircularImage) view.findViewById(R.id.ci_user);
        mUsername = (TextView) view.findViewById(R.id.uname);
        btnLogin = (TextView) view.findViewById(R.id.login_text);
        ll_login_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (isFlag) {
                    AppContext.saveUserNull(getActivity());
                    AppContext.isLogin = false;
                    AppContext.userInfo = null;
                    Preferences.remove(getActivity(),Preferences.Key.USER_STORE_INFO);
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                } else {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }
            }
        });
        // 控件初始化完成
        isPrepared = true;
    }

    @Override
    protected void lazyload() {
        System.out.println("lazyload.....");
        handler.sendEmptyMessage(0);
        if (!isPrepared || !isVisible) {
            return;
        }

    }

    private void initdata() {
        if (AppContext.isLogin) {
            User userInfo = AppContext.getLocalUserInfo(getActivity());
            if (userInfo != null) {
                mUsername.setText(userInfo.username);
                btnLogin.setText("退出登录");
                isFlag = true;
            }
        } else {
            mUsername.setText("未登录");
            btnLogin.setText("登录");
            isFlag = false;
            ciuser.setImageResource(R.drawable.icon_head);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppContext.isLogin) {
            User userInfo = AppContext.getLocalUserInfo(getActivity());
            if (userInfo != null) {
                mUsername.setText(userInfo.username);
                btnLogin.setText("退出登录");
                isFlag = true;
                getIQueryUserCenterInfo(userInfo.id);
                this.ciuser.setOnClickListener(this);
            }
        }else{
            mUsername.setText("未登录");
            btnLogin.setText("登录");
            isFlag = false;
            int count=0;
            tvdsk.setText(count+"");
            tvdfh.setText(count+"");
            tvdsh.setText(count+"");
            tvdpj.setText(count+"");
            tvshz.setText(count+"");
            tvcollectionpro.setText(count+"件商品");
            tvcollectionfac.setText(count + "个厂家");
            ciuser.setImageResource(R.drawable.icon_head);
        }
    }

    /**
     * 获取账户中心信息
     * @param userId
     */
    private void getIQueryUserCenterInfo(String userId) {
        CustomResponseHandler mHandler = new CustomResponseHandler(
                getActivity()) {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (LogUtil.isDebug)
                    Log.i(TAG, "用户中心信息：" + content);
                UserCenterBean bean = UserCenterBean.explainJson(content,getActivity());
                initView(bean);
            }
        };
        RequestParams params = new RequestParams();
        params.put("uid", userId);
        HttpClient.iQueryUserCenterInfo(mHandler, params);
    }

    //刷新界面
    private void initView(UserCenterBean bean) {
        if(bean!=null&&null!=bean.getData()){
            IQueryUserCenterInfoBean centerInfo=bean.getData();
            tvdsk.setText(centerInfo.getPay_count()+"");
            tvdfh.setText(centerInfo.getDai_count()+"");
            tvdsh.setText(centerInfo.getShou_count()+"");
            tvdpj.setText(centerInfo.getPing_count()+"");
            tvshz.setText(centerInfo.getRep_count()+"");
            tvcollectionpro.setText(centerInfo.getCommodity_count()+"件商品");
            tvcollectionfac.setText(centerInfo.getStore_count()+"个厂家");
            ImageManager.Load(centerInfo.getHead_img(),ciuser);
        }else{
            mUsername.setText("未登录");
            btnLogin.setText("登录");
            isFlag = false;
            int count=0;
            tvdsk.setText(count+"");
            tvdfh.setText(count+"");
            tvdsh.setText(count+"");
            tvdpj.setText(count+"");
            tvshz.setText(count+"");
            tvcollectionpro.setText(count+"件商品");
            tvcollectionfac.setText(count + "个厂家");
            ciuser.setImageResource(R.drawable.icon_head);
        }
    }

    /**
     * 解析本地缓存的会员中心信息
     */
    private UserCenterBean explainLocalUserInfo(){
        UserCenterBean bean=null;
        String json= Preferences.getString(getActivity(),Preferences.Key.USERINFO);
        if(null!=json){
            bean=new Gson().fromJson(json,UserCenterBean.class);
        }
        return bean;
    }

    @Override
    public void onClick(View v) {
    	Bundle bundle = null;
        switch (v.getId()) {
            case R.id.btn_user_info://账户信息
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                    bundle=new Bundle();
                    ActivitySwitch.openActivity(UserInfoActivity.class,bundle,getActivity());
                }
                break;
            case R.id.btn_message_center://系统消息
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                    ActivitySwitch.openActivity(MessageListActivity.class, null, this.getActivity());
                }
                break;
            case R.id.btn_collection_fac://收藏厂家
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                    bundle = new Bundle();
                    bundle.putInt("index", 2);
                    ActivitySwitch.openActivity(MyCollectionsActivity.class, bundle, this.getActivity());
                }
                break;
            case R.id.btn_collection_pro://收藏商品
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                    bundle = new Bundle();
                    bundle.putInt("index", 1);
                    ActivitySwitch.openActivity(MyCollectionsActivity.class, bundle, this.getActivity());
                }
                break;
            case R.id.btn_order_center://订单中心
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                	  bundle=new Bundle();
                      bundle.putString("from", "-1");
                    ActivitySwitch.openActivity(OrderListActivity.class, bundle, this.getActivity());
                }
                break;
            case R.id.btn_shz://售后中
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                	  bundle=new Bundle();
                      bundle.putString("from", "5");
                  	  ActivitySwitch.openActivity(OrderListActivity.class, bundle, getActivity()); 
                }
                break;
            case R.id.btn_dpj://待评价
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                 	  bundle=new Bundle();
                      bundle.putString("from", "4");
                  	  ActivitySwitch.openActivity(OrderListActivity.class, bundle, getActivity());   
                }
                break;
            case R.id.btn_dsh://待收货
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                 	  bundle=new Bundle();
                      bundle.putString("from", "3");
                  	  ActivitySwitch.openActivity(OrderListActivity.class, bundle, getActivity());   
                }
                break;
            case R.id.btn_dfh://待发货
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                	  bundle=new Bundle();
                      bundle.putString("from", "2");
                  	  ActivitySwitch.openActivity(OrderListActivity.class, bundle, getActivity());    
                }
                break;
            case R.id.btn_dsk://待付款
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                    bundle=new Bundle();
                    bundle.putString("from", "1");
                    bundle.putString("tag", "1");
                	ActivitySwitch.openActivity(OrderListActivity.class, bundle, getActivity());    	
                }
                break;
            case R.id.btn_setting://系统设置
                ActivitySwitch.openActivity(SettingActivity.class, null, this.getActivity());
                break;
            case R.id.ci_user://用户头像
                if (!AppContext.isLogin) {
                    ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
                }else{
                    bundle=new Bundle();
                    ActivitySwitch.openActivity(UserInfoActivity.class, bundle, getActivity());
                }
                break;
        }
    }
}
