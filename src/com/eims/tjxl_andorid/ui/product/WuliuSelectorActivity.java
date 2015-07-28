/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
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
import com.eims.tjxl_andorid.ui.product.pop.AreasPop;
import com.eims.tjxl_andorid.ui.product.pop.CityPop;
import com.eims.tjxl_andorid.ui.product.pop.PopAdapter;
import com.eims.tjxl_andorid.ui.product.pop.ProvincePop;
import com.eims.tjxl_andorid.ui.product.pop.ProvincePop.ClickItemListener;
import com.eims.tjxl_andorid.utils.DWSqliteUtils;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description:  物流选择
 * @Author zd
 * @date 2015年6月25日  下午7:57:40
 *************************************************************************** 
 */
public class WuliuSelectorActivity  extends BaseActivity{
	
	private static final String TAG = "WuliuSelectorActivity";
	private HeadView headView;
	 private TextView province,city,country;
	 private SimpleCursorAdapter adapterProvince = null;
	 private SimpleCursorAdapter adapterCity = null;
	 private SimpleCursorAdapter adapterArea = null;
	 
	 public   SQLiteDatabase mDatabase;
	 private String provinceId,cityId,areaId;
	 private Button btn_ok;
	private Intent intent;
	private TextView shoeAdress;
	//省份集合
	private List<Adress> adressProvince=new ArrayList<CityAdressBean.Adress>();
	private List<City> adressCity=new ArrayList<CityAdressBean.City>();
	private List<Areas> adressAreas=new ArrayList<CityAdressBean.Areas>();
	private ProvincePop mProvincePop;
	private CityPop cityPop;;
	private AreasPop areasPop;
	private String uid;
	private String cid;
	private TextView feiyong;
	private WuLiuP data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_wuliu_layout);
		findviews();
		setActionBar();
		intent = this.getIntent();
		if(intent!=null){
			String province_city = intent.getStringExtra("province_city");
			uid = intent.getStringExtra("uid");
	        shoeAdress.setText(province_city);
		}
	
		initdata();
		province.setVisibility(View.VISIBLE);
		city.setVisibility(View.GONE);
		country.setVisibility(View.GONE);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			        if("请选择".equals(province.getText().toString())||"请选择".equals(city.getText().toString())||"请选择".equals(country.getText().toString())){
			        	TipToast.makeText(getBaseContext(), "请选择物流地址", 0).show();
			        	return;
			        }
			        Bundle bundle=new Bundle();
			        bundle.putString("pname", province.getText().toString());
			        bundle.putString("cname", city.getText().toString());
			        bundle.putString("countryname", country.getText().toString());
			        if(data!=null){
			            bundle.putString("feiyong", data.data.first_price);
			        }
			        intent.putExtras(bundle);
			        setResult(0, intent);
			        WuliuSelectorActivity.this.finish();
			}
		});
	}
	
	private void  findviews(){
		headView=(HeadView) findViewById(R.id.head);
		province=(TextView) findViewById(R.id.choose_prvionce);
		city=(TextView) findViewById(R.id.city);
		country=(TextView) findViewById(R.id.city_qu);
		shoeAdress=(TextView) findViewById(R.id.fahuo_adress);
		btn_ok=(Button) findViewById(R.id.btn_ok);
		feiyong=(TextView) findViewById(R.id.feiyong);
	}
	
	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("物流说明");
		headView.setGobackVisible();
		headView.setRightGone();
	}
	

	
	private void  initdata(){
		
		SharedPreferences mPreferences = getSharedPreferences("adress", Context.MODE_PRIVATE);
		if(!mPreferences.contains("adress")){
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
		
		 province.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mProvincePop=new ProvincePop(WuliuSelectorActivity.this,1,adressProvince);
				mProvincePop.showDialog();
				mProvincePop.setQuitDialogListener(new ClickItemListener() {
						@Override
						public void onConfirmClick(Adress bean,
								PopAdapter adapter) {
							// TODO Auto-generated method stub
							   province.setText(bean.pn);
							    //应该获取当前省份下的所有城市
							    adressCity.clear();
							    for(int j=0;j<bean.cities.size();j++){
						    	    City city=bean.cities.get(j);
						    	    adressCity.add(city);  	    
						       }
							    province.setVisibility(View.VISIBLE);
								city.setVisibility(View.VISIBLE);
								country.setVisibility(View.GONE);
							    city.performClick();
						}
					});
			}
		});
		
		 city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cityPop=new CityPop(WuliuSelectorActivity.this, 2, adressCity);
				cityPop.showDialog();
				cityPop.setQuitDialogListener(new CityPop.ClickItemListener() {
					@Override
					public void onConfirmClick(City bean) {
						// TODO Auto-generated method stub
						city.setText(bean.cn);
						cid=bean.cId;
						adressAreas.clear();
						  for(int j=0;j<bean.areas.size();j++){
					    	    Areas areas=bean.areas.get(j);
					    	    adressAreas.add(areas); 
					       }
						    province.setVisibility(View.VISIBLE);
							city.setVisibility(View.VISIBLE);
							country.setVisibility(View.VISIBLE);
						  country.performClick();
					}
				});
			}
		});
		 
		 country.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				areasPop=new AreasPop(WuliuSelectorActivity.this, 3, adressAreas);
				areasPop.showDialog();
				areasPop.setQuitDialogListener(new AreasPop.ClickItemListener() {
					@Override
					public void onConfirmClick(Areas bean) {
						// TODO Auto-generated method stub
						country.setText(bean.an);
						loadYunFei();
					}
				});
			}
		});
	
		 
		
	}
		
//		
//		if( AppContext.isFirstCopy){
//			 copyData();//拷贝assets目录下的数据库到databases目录下
//		}
//		
//		DWSqliteUtils instance = DWSqliteUtils.getInstance(WuliuSelectorActivity.this);
//		mDatabase = instance.mDatabase;
//		 
//		cursorProvince =  mDatabase.rawQuery("SELECT _id,parentId,areaName,level from area_table where level = ?",
//				new String[]{"1"});
//		 
//		adapterProvince = new SimpleCursorAdapter(this,
//	              R.layout.spinner_item, 
//	              cursorProvince, 
//	              new String[]{"areaName" }, 
//	              new int[]{R.id.text_name}); 
//		
//		province.setAdapter(adapterProvince);
//		province.setOnItemSelectedListener(new ItemProvinceListenerImpl());
//		province.setSelection((cursorProvince.getCount() - 1));
//		LogUtil.d(TAG, "province:"+(cursorProvince.getCount()-1));
//	}
//	
//
//	   private class ItemProvinceListenerImpl implements OnItemSelectedListener{
//		@Override
//		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			
//			cursorProvince.moveToPosition(arg2);
//			int nameColumnIndex = cursorProvince.getColumnIndex("_id");
//			int id = cursorProvince.getInt(nameColumnIndex);
//			provinceId = id + "";
//			
//			cursorCity =  mDatabase.rawQuery("SELECT _id,parentId,areaName,level from area_table where parentId = ?",
//					new String[]{id+""});
//			 
//			adapterCity = new SimpleCursorAdapter(WuliuSelectorActivity.this,
//		              R.layout.spinner_item, 
//		              cursorCity, 
//		              new String[]{"areaName" }, 
//		              new int[]{R.id.text_name}); 
//			
//			city.setAdapter(adapterCity);
//			city.setOnItemSelectedListener(new ItemCityListenerImpl());
//			
//			LogUtil.d(TAG, "province:"+(cursorCity.getCount()-1));
//		}
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {
//			
//		}
//	         
//	 }
//	   
//
//	   private class ItemCityListenerImpl implements OnItemSelectedListener{
//		@Override
//		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			cursorCity.moveToPosition(arg2);
//			
//			int nameColumnIndex = cursorCity.getColumnIndex("_id");
//			int id = cursorCity.getInt(nameColumnIndex);
//			cityId = id + "";
//		 
//			cursorArea =  mDatabase.rawQuery("SELECT _id,parentId,areaName,level from area_table where parentId = ?",
//					new String[]{id+""});
//			 
//			adapterArea = new SimpleCursorAdapter(WuliuSelectorActivity.this,
//		              R.layout.spinner_item, 
//		              cursorArea, 
//		              new String[]{"areaName" }, 
//		              new int[]{R.id.text_name}); 
//			
//			country.setAdapter(adapterArea);
//			country.setOnItemSelectedListener(new ItemAreaListenerImpl());
//		}
//		@Override
//		public void onNothingSelected(AdapterView<?> arg0) {
//			
//		}
//	         
//	 }
//	   
//	   private class ItemAreaListenerImpl implements OnItemSelectedListener{
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				cursorArea.moveToPosition(arg2);
//				
//				int nameColumnIndex = cursorArea.getColumnIndex("_id");
//				int id = cursorArea.getInt(nameColumnIndex);
//				 
//				areaId = id + "";
//				 
//			}
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				
//			}
//		         
//		 }
//	   
//		@Override
//		protected void onDestroy() {
//			super.onDestroy();
//	        cursorProvince.close();
//	        cursorCity.close();
//	   	    cursorArea.close();
//	        mDatabase.close();
//	        
//	        //
//	       
//	      
//		}
//	   
//	
//	public   void copyData(){
//		
//	     InputStream in = null;
//	     FileOutputStream out = null;
//	     String dpath = String.format(databasepath, this.getApplicationInfo().packageName);
//	     
//	     String path = dpath + "/duowei_db.db";
//	     Log.e("path", path);
//	     File file = new File(dpath);
//	     File fileData = new File(path);
//	     if (!fileData.exists()) {
//	    	 
//	        try
//	        {
//	          if(!file.exists()){
//	        	  file.mkdir();
//	          }
//	        
//	          in = this.getAssets().open("duowei_db.db"); 
//	          out = new FileOutputStream(fileData);
//	          int length = -1;
//	          byte[] buf = new byte[1024];
//	          while ((length = in.read(buf)) != -1)
//	          {
//	             out.write(buf, 0, length);
//	          }
//	          out.flush();
//	        }
//	        catch (Exception e)
//	        {
//	          e.printStackTrace();
//	        }
//	        finally{
//	        	AppContext.isFirstCopy = false;
//	          if (in != null)
//	          {
//	             try {
//	               in.close();
//	             } catch (IOException e1) {
//	               e1.printStackTrace();
//	             }
//	          }
//	          if (out != null)
//	          {
//	             try {
//	               out.close();
//	             } catch (IOException e1) {
//	               e1.printStackTrace();
//	             }
//	          }
//	        }
//	     }
	 //  }
	
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
	
	private void  loadYunFei(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(this, false, ""){
		
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if(statusCode==200){
	                LogUtil.d(TAG, content);
					data = GsonUtils.json2bean(content, WuLiuP.class);
					feiyong.setText("运费:￥"+data.data.first_price);
				}
			}
		};
		RequestParams params=new RequestParams();
		params.put("uid",uid );
		params.put("cid",cid );
		HttpClient.QueryYunFei(mHandler, params);
	};
	
	
	public  class WuLiuP{
		public Data data;
		public String type;
	}
	
	public class Data{
		public String first_price;
		public String first_weight;
		public String logistics_id;
		public String pre_weight;
	}
	
}
