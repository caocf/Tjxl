/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.CityAdressBean;
import com.eims.tjxl_andorid.entity.CityAdressBean.Adress;
import com.eims.tjxl_andorid.entity.CityAdressBean.Areas;
import com.eims.tjxl_andorid.entity.CityAdressBean.City;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.entity.UserAdressBean.AdressBean;
import com.eims.tjxl_andorid.ui.product.WuliuSelectorActivity;
import com.eims.tjxl_andorid.ui.product.pop.AreasPop;
import com.eims.tjxl_andorid.ui.product.pop.CityPop;
import com.eims.tjxl_andorid.ui.product.pop.PopAdapter;
import com.eims.tjxl_andorid.ui.product.pop.ProvincePop;
import com.eims.tjxl_andorid.ui.product.pop.ProvincePop.ClickItemListener;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月28日  下午5:27:04
 *************************************************************************** 
 */
public class AddNewAdressActivity extends BaseActivity{
	private static final String TAG = null;
	private HeadView headView;
	private EditText mUsername,mUserPhone,mEmailNum,mUserDetailAdress;
	private TextView mPrivonce,mCity,mCityQu;
	//省份集合
	private List<Adress> adressProvince=new ArrayList<CityAdressBean.Adress>();
	private List<City> adressCity=new ArrayList<CityAdressBean.City>();
	private List<Areas> adressAreas=new ArrayList<CityAdressBean.Areas>();
	private ProvincePop mProvincePop;
	private CityPop cityPop;;
	private AreasPop areasPop;
	private String cid,pid,aid;
	private Button btn_addAdress,btn_cancel;
	private User user;
	private String uname;
	private String uphone;
	private String uemail;
	private String province;
	private String city;
	private String cityQu;
	private String detailAdress;
	private AdressBean tempBean;
	private boolean isflag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_newadress_layout);
		findviews();
		initview();
		setActionBar();
        initdata();
        setLisenter();
	}
	
	private void  findviews(){
		headView=(HeadView) findViewById(R.id.headview);
		mUsername= (EditText) findViewById(R.id.uname);
		mUserPhone= (EditText) findViewById(R.id.uphone);
		mEmailNum= (EditText) findViewById(R.id.uemail);
		mUserDetailAdress= (EditText) findViewById(R.id.adress_detail);
		mPrivonce=(TextView) findViewById(R.id.province);
		mCity=(TextView) findViewById(R.id.city);
		mCityQu=(TextView) findViewById(R.id.city_qu);
		btn_addAdress=(Button) findViewById(R.id.btn_add_adress);
		btn_cancel=(Button) findViewById(R.id.btn_cancel);
	}
	
	private void setActionBar() {
		// TODO Auto-generated method stub
		if(isflag){
			headView.setText("修改收货地址");
		}else{
			headView.setText("新增收货地址");
		}
		
		headView.setGobackVisible();
		headView.setRightGone();
	}
	
	private void  setLisenter(){
	
			btn_addAdress.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(checkInfo()){
					addAdress();
					}
				}
			});
			
			btn_cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AddNewAdressActivity.this.finish();
				}
			});
			
		}
	
	
	private  boolean checkInfo(){
		uname = mUsername.getText().toString();
		uphone = mUserPhone.getText().toString();
		uemail = mEmailNum.getText().toString();
		province = mPrivonce.getText().toString();
		city = mCity.getText().toString();
		cityQu = mCityQu.getText().toString();
		detailAdress = mUserDetailAdress.getText().toString();
		if(TextUtils.isEmpty(uname)){
			TipToast.makeText(mContext, "请填写收货人", 0).show();
			return false;
		}else if(TextUtils.isEmpty(uphone)){
			TipToast.makeText(mContext, "请填写收货人手机号码", 0).show();
			return false;
		}else if(!StringUtils.isMobileNO(uphone)){
			TipToast.makeText(mContext, "手机号码格式不正确", 0).show();
			return false;
		}else if(!StringUtils.checkPost(uemail)){
			TipToast.makeText(mContext, "邮编格式不正确", 0).show();
			return false;
		}
		else if("请选择".equals(province)||"请选择".equals(city)||"请选择".equals(cityQu)){
			TipToast.makeText(mContext, "请选择省市区", 0).show();
			return false;
		}else if(TextUtils.isEmpty(detailAdress)){
			TipToast.makeText(mContext, "请填写详细地址", 0).show();
			return false;
		}
		return true;
	}
	
	private void  initview(){
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			tempBean = (AdressBean) bundle.getSerializable("adressBean");
			isflag=true;
			mUsername.setText(tempBean.consignee_name);
			mUserPhone.setText(tempBean.consignee_phone);
			mEmailNum.setText(tempBean.postal_code);
			mUserDetailAdress.setText(tempBean.address);
		}else{
			isflag=false;
		}
	}
	

	private void  initdata(){
		user = AppContext.getLocalUserInfo(mContext);
		SharedPreferences mPreferences = getSharedPreferences("adress", Context.MODE_PRIVATE);
		if(!mPreferences.contains("adress")){
		//	loadAdress();
			AppContext.getInstance().getAreaJson();
		}
		String adressXml = mPreferences.getString("adress", "");
		CityAdressBean adressBean = GsonUtils.json2bean(adressXml, CityAdressBean.class);
		for(int i=0;i<adressBean.data.size();i++){
			String pn = adressBean.data.get(i).pn;
			LogUtil.d(TAG,pn);
		    Adress adress = adressBean.data.get(i);
		    adressProvince.add(adress); 
		}
		
	      if(isflag){
          	 for(int p=0;p<adressProvince.size();p++){
          		 if(tempBean.province_id.equals(adressProvince.get(p).pId)){
          			mPrivonce.setText(adressProvince.get(p).pn);
          			 pid=adressProvince.get(p).pId;
          			Adress temp = adressProvince.get(p);
          			 for(City city : temp.cities){
          				 if(tempBean.city_id.equals(city.cId)){
          					 cid=city.cId;
          					 mCity.setText(city.cn);
          					 //break;
          					 for(Areas areas : city.areas){
          						 if(tempBean.area_id.equals(areas.aId)){
          							 aid=areas.aId;
              						 mCityQu.setText(areas.an);
              						 break;
          						 }
          					 }
          				 }
          			 }
          			 break;
          		 }
          	 }
           }
		
		mPrivonce.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mProvincePop=new ProvincePop(AddNewAdressActivity.this,1,adressProvince);
				mProvincePop.showDialog();
				mProvincePop.setQuitDialogListener(new ClickItemListener() {
						@Override
						public void onConfirmClick(Adress bean,
								PopAdapter adapter) {
							// TODO Auto-generated method stub
							   mPrivonce.setText(bean.pn);
							   pid=bean.pId;
							    //应该获取当前省份下的所有城市
							    adressCity.clear();
							    for(int j=0;j<bean.cities.size();j++){
						    	    City city=bean.cities.get(j);
						    	    adressCity.add(city);  	    
						       }
							    mCity.performClick();
						}
					});
			}
		});
		
//	     if(isflag){
//          	 for(int p=0;p<adressCity.size();p++){
//          		 if(tempBean.city_id.equals(adressCity.get(p).cId)){
//          			mCity.setText(adressCity.get(p).cn);
//          			 cid=adressCity.get(p).cId;
//          		 }
//          	 }
//           }
		
		mCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cityPop=new CityPop(AddNewAdressActivity.this, 2, adressCity);
				cityPop.showDialog();
				cityPop.setQuitDialogListener(new CityPop.ClickItemListener() {
					@Override
					public void onConfirmClick(City bean) {
						// TODO Auto-generated method stub
						mCity.setText(bean.cn);
						cid=bean.cId;
						adressAreas.clear();
						  for(int j=0;j<bean.areas.size();j++){
					    	    Areas areas=bean.areas.get(j);
					    	    adressAreas.add(areas); 
					       }
						  mCityQu.performClick();
					}
				});
			}
		});
		 
		
//	     if(isflag){
//          	 for(int p=0;p<adressAreas.size();p++){
//          		 if(tempBean.area_id.equals(adressAreas.get(p).aId)){
//          			mCityQu.setText(adressAreas.get(p).an);
//          			 aid=adressAreas.get(p).aId;
//          		 }
//          	 }
//           }
//		
		mCityQu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				areasPop=new AreasPop(AddNewAdressActivity.this, 3, adressAreas);
				areasPop.showDialog();
				areasPop.setQuitDialogListener(new AreasPop.ClickItemListener() {
					@Override
					public void onConfirmClick(Areas bean) {
						// TODO Auto-generated method stub
						mCityQu.setText(bean.an);
						aid=bean.aId;
					}
				});
			}
		});
	
		 
		
	}
	
	private void  loadAdress(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(this, false, ""){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if(statusCode==200){
					SharedPreferences mPreferences = getSharedPreferences(
							"adress", Context.MODE_PRIVATE);
					Editor mEditor = mPreferences.edit();
					mEditor.putString("adress", content);
					mEditor.commit();
					
				}
			}
		};
		RequestParams params=new RequestParams();
		HttpClient.QueryPrvionceCity(mHandler, params);
	}
	
	private void  addAdress(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(this, true, "正在加载..."){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if(statusCode==200){
					LogUtil.d(TAG, content);
					AddNewAdressActivity.this.finish();
					AdressListActivity.instance.loaddata();
				}
			}
		};
		RequestParams params=new RequestParams();
		if(isflag){
			params.put("id",tempBean.id);
		}
		params.put("uid",user.id);
		params.put("province_id",pid);
		params.put("city_id",cid);
		params.put("area_id",aid);
		params.put("address",detailAdress);
		params.put("address_show",province+city+cityQu);
		params.put("postal_code",uemail);
		params.put("consignee_name",uname);
		params.put("consignee_phone",uphone);
		params.put("is_default","");
		if(isflag){
			HttpClient.Modfiy_Adress(mHandler, params);
		}else{
			HttpClient.Add_Adress(mHandler, params);
		}
		
	}

}
