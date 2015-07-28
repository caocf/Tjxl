/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.LogisticsBean;
import com.eims.tjxl_andorid.entity.LogisticsBean.LogisticItemBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.imageupload.TipsToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年7月1日  下午2:16:09
 *************************************************************************** 
 */
public class OrderWuliuQueryActivity  extends  BaseActivity{
	public static final String TAG1 = "OrderWuliuQueryActivity";
    private HeadView headview;
    private TextView mExpressName;//快递名称
    private TextView mExpressNo;//快递单号
    private TextView mLogisticsStatus;//快递状态
    private ListView mListView;
    private String logistics_no;//物流单号
    private String logistics_en;//快递公司英文名
	private LogisticsBean bean;
	private List<LogisticItemBean> itemBeans=new ArrayList<LogisticsBean.LogisticItemBean>();
	private LogisticsAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_wuliuquery_layout);
		findviews();
		initActionBar();
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			logistics_no=bundle.getString("logistics_no");
			logistics_en=bundle.getString("logistics_en");
		}
		if(mAdapter==null){
			mAdapter=new LogisticsAdapter();
		}
		mListView.setAdapter(mAdapter);
		
		loadLogisticsData();
		
	}
	
	
    private void initActionBar() { 
		headview.setText("物流详情");
		headview.setRightResource();
	}
    private void  updataUI(){
    	mExpressName.setText(bean.name);
    	mExpressNo.setText(bean.order);
    	String status = bean.status;
    	  //订单跟踪状态：status  
//        0：查询出错（即errCode!=0），
//		1：暂无记录，
//		2：在途中，
//		3：派送中，
//		4：已签收，
//		5：拒收，
//		6：疑难件
//		7：退回
    	if("0".equals(status)){
    		mLogisticsStatus.setText("查询出错");
    	}else if("1".equals(status)){
    		mLogisticsStatus.setText("暂无记录");
    	}else if("2".equals(status)){
    		mLogisticsStatus.setText("在途中");
    	}else if("3".equals(status)){
    		mLogisticsStatus.setText("派送中");
		}else if("4".equals(status)){
			mLogisticsStatus.setText("已签收");
		}else if("5".equals(status)){
			mLogisticsStatus.setText("拒收");
		}else if("6".equals(status)){
			mLogisticsStatus.setText("疑难件");
		}else if("7".equals(status)){
			mLogisticsStatus.setText("退回");
		}
    }
	
	private  void  findviews(){
		headview=(HeadView) findViewById(R.id.head);
		mExpressName=(TextView) findViewById(R.id.wuliu_company);
		mExpressNo=(TextView) findViewById(R.id.expass_no);
		mLogisticsStatus=(TextView) findViewById(R.id.logistis_status);
		mListView=(ListView) findViewById(R.id.wuliuinfo_listview);
	}
	private  void  loadLogisticsData(){
	  	if(!available){
    		TipsToast.makeText(mContext, "亲，你的网络有点问题哦", 0).show();
    		return;
    	}
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在加载..."){
		

			@Override
    		public void onSuccess(int statusCode, String content) {
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, content);
    			LogUtil.d(TAG1, content);
    			 bean = GsonUtils.json2bean(content, LogisticsBean.class);
    			 updataUI();
    			 itemBeans.clear();
    			 itemBeans.addAll(bean.data);
    			 Collections.reverse(itemBeans);
    			 mAdapter.notifyDataSetChanged();
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("logistics_no", logistics_no);
    	params.put("logistics_en",logistics_en);
    	HttpClient.Order_Logistis(handler, params);
	}
	
	class LogisticsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemBeans==null ? 0 : itemBeans.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return itemBeans.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		
		@Override
		public View getView(int arg0, View convertview, ViewGroup arg2) {
	        ViewHolder holder;
	        if(convertview==null){
	        	holder=new ViewHolder();
	        	convertview=View.inflate(mContext, R.layout.order_logistics_list_item, null);
	        	holder.icon=(ImageView) convertview.findViewById(R.id.image);
	        	holder.text=(TextView) convertview.findViewById(R.id.text);
	        	holder.time=(TextView) convertview.findViewById(R.id.time);
	        	convertview.setTag(holder);
	        }else{
	        	holder=(ViewHolder) convertview.getTag();
	        }
	        LogisticItemBean logisticItemBean = itemBeans.get(arg0);
	        if(arg0==0){
	        	  holder.icon.setImageResource(R.drawable.timeline_green);
	        	  holder.text.setTextColor(getResources().getColor(R.color.logistic_text_color));
	        	  holder.time.setTextColor(getResources().getColor(R.color.logistic_text_color));
	        }else{
	        	  holder.icon.setImageResource(R.drawable.gary_yuan);
	        	  holder.text.setTextColor(getResources().getColor(R.color.gary_text_color));
	        	  holder.time.setTextColor(getResources().getColor(R.color.gary_text_color));
	        }
	        holder.text.setText(logisticItemBean.content);
	        holder.time.setText(logisticItemBean.time);
			return convertview;
		}
		
	}
	
	class ViewHolder{
		private ImageView icon;
		private TextView text;
		private TextView time;
	}
	
}
