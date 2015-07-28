package com.eims.tjxl_andorid.ui.product;

import java.util.List;

import loopj.android.http.RequestParams;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.entity.GoodDetail;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.shopcart.ToLoginDialogActivity;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:我的 购买记录
 * @Author zd
 *************************************************************************** 
 */
public class RecommendedFragment extends android.support.v4.app.Fragment {

	public static RecommendedFragment meFragment;

   

    private TextView tv1,tv2;
    private ToLoginDialogActivity loginDialog;
    private User user;
	public static RecommendedFragment getInstance() {
		if (meFragment == null) {
			meFragment = new RecommendedFragment();
		}
		return meFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		loginDialog=new ToLoginDialogActivity(getActivity(), R.style.load_dialog);
		return inflater.inflate(R.layout.mebuy_good_layout, null);
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	   findview();
	   inituI();
	}
	
	private void findview(){
	
		 tv1=(TextView) getActivity().findViewById(R.id.total_numer);
		 tv2=(TextView) getActivity().findViewById(R.id.total_money);
	    
	}
	
	private  void inituI(){
		if(AppContext.isLogin){
			user=AppContext.getLocalUserInfo(getActivity());
			loadtData();
		}else{
			loginDialog.show();
		}
	}

	private void  loadtData(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), false, ""){
			@Override
		    public void onSuccess(int statusCode, String content) {
		    	// TODO Auto-generated method stub
		    	super.onSuccess(statusCode, content);
		    	LogUtil.d(TAG, content);
		    	MeBuyGoodBean bean = GsonUtils.json2bean(content, MeBuyGoodBean.class);
		    	if(StringUtils.isEmpty(bean.data.total_qty)){
		    		bean.data.total_qty="0";
		    	}
		    	if(StringUtils.isEmpty(bean.data.thm_qty)){
		    		bean.data.thm_qty="0";
		    	}
		       	if(StringUtils.isEmpty(bean.data.total_money)){
		    		bean.data.total_money="0.00";
		    	}
		    	tv1.setText("累计购买："+bean.data.total_qty+"件，近3个月成交"+bean.data.thm_qty+"件");
		    	tv2.setText("累计购买金额：￥"+bean.data.total_money+"元");
		    }	
		};
		RequestParams params=new RequestParams();
		params.put("id", ProductDeatil.goodId);
		params.put("uid",user.id);
		
		HttpClient.iQueryMyBuyedList(mHandler, params);
	}
	
	public  class  MeBuyGoodBean{
		public MeBuyInfo data;	
		public String type;
		public String msg;
		public class MeBuyInfo {
			public String total_money;
			public String total_qty;
			public String thm_qty;
			
		}
		
	}
	
	

}
