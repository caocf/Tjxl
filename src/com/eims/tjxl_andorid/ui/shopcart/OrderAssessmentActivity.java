/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.alipay.sdk.util.JsonUtils;
import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 订单商品评价
 * @Author zd
 * @date 2015年7月2日  上午10:10:24
 *************************************************************************** 
 */
public class OrderAssessmentActivity  extends BaseActivity{
	
	protected static final String TAG = "OrderAssessmentActivity";
	private HeadView head;
	private Button btn_commint;
	private TextView orderNo,orderAddtime,orderPayTime,orderLogisticTime,orderOkTime,userRemarks;
	private TextView yunfei,payTotalmoney;
	private MyListView mListView;
	private String orderId;
	private List<AssessmentBean> dataList;
	private AssessmentAdapter mAdapter;
	private OrderMap map;
	private RatingBar msxfBar,fwtdBar,fhsdBar;
	private RatingBarChangeListenerImpl  barChangeListenerImpl;
	private String msScore,fwScore,fhScore;
	private int position = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_assessment_layout);
		findviews();
		initActionBar();
		dataList=new ArrayList<OrderAssessmentActivity.AssessmentBean>();
		if(getIntent().getExtras()!=null){
			Bundle bundle = getIntent().getExtras();
			orderId = bundle.getString("order_id");
			position = bundle.getInt("position", -1);
			Log.d("zhiheng", "comment position = "+position);
		}
		if(mAdapter==null){
			mAdapter=new AssessmentAdapter();
		}
		mListView.setAdapter(mAdapter);
		loaddata();
	}
	
	private void  findviews(){
		head=(HeadView) findViewById(R.id.head);
		orderNo= (TextView) findViewById(R.id.order_no);
		orderAddtime= (TextView) findViewById(R.id.order_addtime);
		orderPayTime= (TextView) findViewById(R.id.order_paymoney_time);
		orderLogisticTime= (TextView) findViewById(R.id.order_fahuo_time);
		orderOkTime= (TextView) findViewById(R.id.order_ok_time);
		userRemarks= (TextView) findViewById(R.id.order_user_remarks);
		btn_commint=(Button) findViewById(R.id.commint_assessment);
		yunfei=(TextView) findViewById(R.id.yunfei);
		payTotalmoney=(TextView) findViewById(R.id.total_om);
		mListView=(MyListView) findViewById(R.id.order_assesment_listview);
		msxfBar=(RatingBar) findViewById(R.id.msxf_ratingbar);
		fwtdBar=(RatingBar) findViewById(R.id.fwtd_ratingbar);
		fhsdBar=(RatingBar) findViewById(R.id.fhsd_ratingbar);
	}
	   private void initActionBar() { 
			head.setText("订单评价");
			head.setRightGone();	
		}
	   private void  showUI(){
		   orderNo.setText(map.order_code);
		   orderAddtime.setText(map.addtime);
		   orderPayTime.setText(map.order_status_time2);
		   orderLogisticTime.setText(map.order_status_time4 );
		   orderOkTime.setText(map.order_status_time5 );
		   userRemarks.setText(map.order_remark);
		   payTotalmoney.setText("总计：￥"+map.total_price+"元");
		   yunfei.setText("应付款(含运费￥"+map.logistics_fare+"元 ");
	//	   barChangeListenerImpl=new RatingBarChangeListenerImpl();
		   msxfBar.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl(1));
		   fwtdBar.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl(2));
		   fhsdBar.setOnRatingBarChangeListener(new RatingBarChangeListenerImpl(3));
		   final StringBuilder ids=new StringBuilder();
		   final StringBuilder assessments=new StringBuilder();
		   final StringBuilder score=new StringBuilder();
		   btn_commint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for(AssessmentBean temp : dataList){
					LogUtil.d(TAG, temp.assessmentContext+"------"+temp.isAssessment);
					if(StringUtils.isEmpty(temp.assessmentContext)){
						TipToast.makeText(mContext, "请输入评论信息", 0).show();
						return;
					}
						ids.append(temp.commodity_id+"@");
						score.append(temp.isAssessment+"@");
						assessments.append(temp.assessmentContext+"@");
						commintAssessment(ids.toString(),assessments.toString(),score.toString());
				}
			}
		});
	   }
	   
	   private void  loaddata(){
		   
			CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在加载中..."){
				@Override
	    		public void onSuccess(int statusCode, String content) {
	    			// TODO Auto-generated method stub
	    			super.onSuccess(statusCode, content);
	    			LogUtil.d(TAG, content);
	    			try {
						JSONObject obj=new JSONObject(content);
						String data = obj.getString("data");
						String orderMap = obj.getString("orderMap");
						map = GsonUtils.json2bean(orderMap, OrderMap.class);
						dataList.clear();
						showUI();
						Type type = new TypeToken<ArrayList<AssessmentBean>>() {}.getType();
						dataList= new Gson().fromJson(data, type);
						mAdapter.notifyDataSetChanged();
					} catch (Exception e) {
					e.printStackTrace();
					}
	    		}
	    	};
	    	RequestParams params=new RequestParams();
	    	params.put("uid", user.id);
	    	params.put("oid", orderId);
	    	HttpClient.iFindReviewById(handler, params);
		   
	   }
	   /**提交评论
	 * @param scores 
	 * @param comments 
	 * @param ids **/
	   private void commintAssessment(String ids, String comments, String scores){
		   
			CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在加载中..."){
				@Override
	    		public void onSuccess(int statusCode, String content) {
	    			// TODO Auto-generated method stub
	    			super.onSuccess(statusCode, content);
	    			LogUtil.d(TAG, content);
	    			try {
		                TipToast.makeText(mContext, "订单评价成功", 0).show();
		                mAdapter.notifyDataSetChanged();
		                Intent intent = new Intent(OrderListActivity.ACTION_REFRESH_LIST);
		                intent.putExtra("position", position);
		                sendBroadcast(intent);
		                ActivitySwitch.finishActivity(OrderAssessmentActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    	};
	    	RequestParams params=new RequestParams();
	    	params.put("uid", user.id);
	    	params.put("id", orderId);
	    	params.put("commodity_ids", ids);
	    	params.put("scores", scores);
	    	params.put("content", comments);
	    	params.put("score1", msScore);
	    	params.put("score2", fwScore);	    	
	    	params.put("score3", fhScore);
	    	HttpClient.iAddReview(handler, params);
	   }
	   
	   
	   class AssessmentAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return dataList==null ? 0 :dataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return dataList.get(arg0);
		}


		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		@Override
		public View getView(int arg0, View convertview, ViewGroup arg2) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if(convertview==null){
				holder=new ViewHolder();
				convertview=View.inflate(mContext, R.layout.order_assessment_item_layout, null);
				holder.good_icon=(ImageView) convertview.findViewById(R.id.assessment_good_image);
				holder.name=(TextView) convertview.findViewById(R.id.assessment_good_name);
				holder.radioGroup=(RadioGroup) convertview.findViewById(R.id.rg_assessment);
				holder.rb_haop=(RadioButton) convertview.findViewById(R.id.rb_haop);
				holder.rb_chap=(RadioButton) convertview.findViewById(R.id.rb_chap);
				holder.rb_zhongp=(RadioButton) convertview.findViewById(R.id.rb_zhongp);
				holder.content=(EditText) convertview.findViewById(R.id.content);
				convertview.setTag(holder);
			}else{
				holder=(ViewHolder) convertview.getTag();
			}
			if(dataList.get(arg0)!=null){
				final AssessmentBean bean = dataList.get(arg0);
				ImageManager.Load(URLs.IMAGE_URL+bean.commodity_img, holder.good_icon);
				holder.name.setText(bean.commodity_name);
				
				//holder.rb_haop.setChecked(true);
				holder.content.addTextChangedListener(new TextWatcher() {			
					@Override
					public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
						bean.assessmentContext=holder.content.getText().toString();
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int arg1, int arg2,
							int arg3) {	
					}
					@Override
					public void afterTextChanged(Editable arg0) {		
					}
				});
				holder.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {		
					@Override
					public void onCheckedChanged(RadioGroup arg0, int checkId) {
						if(checkId==R.id.rb_haop){
							bean.isAssessment="5";
						}else if(checkId==R.id.rb_zhongp){
							bean.isAssessment="3";
						}else if(checkId==R.id.rb_chap){
							bean.isAssessment="1";
						}
					}
				});
			}
		
			return convertview;
		}
		   
	   }
	   
	   class ViewHolder{
		   ImageView  good_icon;
		   TextView name;
		   RadioGroup radioGroup;
		   RadioButton rb_haop,rb_chap,rb_zhongp;
		   EditText content;
		 
	   }
	   
	   class AssessmentBean{
		   
		   public String commodity_code;
		   public String commodity_id;
		   public String commodity_img;
		   public String commodity_name;
		   public String is_review;
		   public String isAssessment;//评价等级  差评 1，中评3，好评5
		   public String assessmentContext;//评价内容
	   }
	   
	   class OrderMap{
		   public String  order_status_time5;// ：确认收货时间
		   public String addtime;// ：下单时间
		   public String order_status_time2; //：付款时间
		   public String order_status_time4;// ：发货时间
		   public String  order_remark;// ：买家留言
		   public String  total_price;// ：应付金额
		   public String  order_code;// ：订单编号
		   public String logistics_fare;//运费
	   }
	   
	   //注意onRatingChanged方法中的最后一个参数boolean fromUser:
	   //若是由用户触摸手势或方向键轨迹球移动触发RatingBar的等级改变,返回true
	   //若是由编程触发RatingBar的等级改变,返回false
	   private class RatingBarChangeListenerImpl implements OnRatingBarChangeListener{
		   private int type;//1描述相符 2服务态度 3发货速度
		   public RatingBarChangeListenerImpl(int type){
			 this.type=type;  
		   }
	     @Override
	     public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
	    	 LogUtil.d(TAG,"现在的等级为 rating="+rating+",是否是用户触发 fromUser="+fromUser);
	       if(1==type){
	    	   msScore=rating+"";
	        }else if(2==type){
	        	fwScore=rating+"";
	        }else if(3==type){
	        	fhScore=rating+"";
	        }
	     
	     }
	     
	   }

}
