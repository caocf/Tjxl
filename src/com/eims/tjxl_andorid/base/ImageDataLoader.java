package com.eims.tjxl_andorid.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.entity.BannerBean;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.exhibition.ExhibitionActivity;
import com.eims.tjxl_andorid.ui.exhibition.ExhibitionDetailActivity;
import com.eims.tjxl_andorid.ui.home.area.SimpleAreaActivity;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 * banner图加载类
 * Created by kuangyong on 15/7/15.
 */
public class ImageDataLoader {

    public static final String FIRST_PAGE="首页";
    public static final String DISCOUNT_PAGE="特价专区";
    public static final String HOTAREA_PAGE="热销商品";
    public static final String STARFACTORY_PAGE="明星厂家";
    public static final String BRAND_PAGE="品牌馆";
    public static final String SHOE_PAGE="鞋样推荐";
    public static final String EXHIBITION_PAGE="实体展厅";
    public static final String EXHIBITION_DETAIL_PAGE="实体展厅详情";

    private Context context;

    public ImageDataLoader(Context context,String type){
        this.context=context;
        loadBannerList(type);
    }

    /***
     * 	//	位置（1:首页,2:特价专区,3:热销商品,4:明星厂家,5:品牌馆,6:鞋样推荐,7:实体展厅,8:实体展厅详情 ）
     */
    public  void loadBannerList(String type){

        CustomResponseHandler mHandler=new CustomResponseHandler(context, false, ""){
            @Override
            public void onSuccess(int statusCode, String content) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, content);
                if(LogUtil.isDebug){
                    LogUtil.i(TAG, "banner图--->"+content);
                }
                try {
                    JSONObject obj=new JSONObject(content);
                    String data = obj.getString("data");
                    if(data.length()>4){
                        BannerBean bannerBean = GsonUtils.json2bean(content, BannerBean.class);
                        ArrayList<BannerBean.BannerItem> imagePath = new ArrayList<BannerBean.BannerItem>();
                        for(BannerBean.BannerItem item : bannerBean.data){
                            imagePath.add(item);
                        }
                        if(null!=listener){
                            listener.onComplete(imagePath);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        RequestParams params=new RequestParams();
        params.put("location", type);
        HttpClient.iQueryBannerList(mHandler, params);
    }

    /**
     * bannar图跳转
     * @param context
     * @param item
     */
    public static void gotoWitchOne(Activity context,BannerBean.BannerItem item){
        if(null!=item){
            if("1".equals(item.skip_type)){                                 //跳转类型 1栏目
                Bundle bundle=new Bundle();
                if(FIRST_PAGE.equals(item.url)){                                 //首页
                    AppManager.getAppManager().finishToHome();
                    MainActivity.radioGroup.getChildAt(0).performClick();
                    MainActivity.mainPager.setCurrentItem(0);
                }else if(SHOE_PAGE.equals(item.url)){                           //鞋样推荐
                    AppManager.getAppManager().finishToHome();
                    MainActivity.radioGroup.getChildAt(1).performClick();
                    MainActivity.mainPager.setCurrentItem(1);
                }else if(DISCOUNT_PAGE.equals(item.url)){                       //特价专区
                    bundle.putInt(SimpleAreaActivity.AREA_TYPE, 1);
                    ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle,context);
                }else if(HOTAREA_PAGE.equals(item.url)){                        //热销商品
                    bundle.putInt(SimpleAreaActivity.AREA_TYPE, 2);
                    ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, context);
                }else if(STARFACTORY_PAGE.equals(item.url)){                    //明星厂家
                    bundle.putInt(SimpleAreaActivity.AREA_TYPE, 3);
                    ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, context);
                }else if(BRAND_PAGE.equals(item.url)){                          //品牌馆
                    bundle.putInt(SimpleAreaActivity.AREA_TYPE, 4);
                    ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, context);
                }else if(EXHIBITION_PAGE.equals(item.url)){                     //实体展厅
                    ActivitySwitch.openActivity(ExhibitionActivity.class, null, context);
                }
            }else if("2".equals(item.skip_type)){                           //跳转类型 2 商品）
                Bundle bundle=new Bundle();
                bundle.putString(ProductDeatil.INTENT_KEY, item.ids);
                ActivitySwitch.openActivity(ProductDeatil.class, bundle,
                        context);
            }else if("3".equals(item.skip_type)){                           //实体展厅详情
                Bundle bundle=new Bundle();
                bundle.putString(ExhibitionDetailActivity.EXHIBITION_ID,item.ids);
                ActivitySwitch.openActivity(ExhibitionDetailActivity.class, bundle,context);
            }
        }
    }

    public interface OnLoaderCompleteListener{
        void onComplete(ArrayList<BannerBean.BannerItem> imagePath);
    }

    public void setOnLoaderCompleteListener(OnLoaderCompleteListener listener){
        this.listener=listener;
    }

    private OnLoaderCompleteListener listener;
}
