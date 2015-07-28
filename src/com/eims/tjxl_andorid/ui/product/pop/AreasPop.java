/**
 * 
 */
package com.eims.tjxl_andorid.ui.product.pop;

import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.CityAdressBean.Adress;
import com.eims.tjxl_andorid.entity.CityAdressBean.Areas;
import com.eims.tjxl_andorid.entity.CityAdressBean.City;
import com.eims.tjxl_andorid.ui.product.pop.ProvincePop.ClickItemListener;

import android.app.Activity;
import android.app.Dialog;
import android.location.Address;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月26日  下午2:20:15
 *************************************************************************** 
 */
public class AreasPop {
	
	private Activity content;
	private int type;
	private View view;
	private ListView listview;
	private AreasAdapter mAdapter;
	private List<Areas> provinceList;
	private Dialog mDialog;
	private ClickItemListener mClickItemListener;
	private  Button title;
	private ImageView back;
	
	public AreasPop(Activity content,int type,List<Areas> provinceList){
		  this.content=content;
		  this.type=type;
		  this.provinceList=provinceList;
		  initListView();
		  
	}
	
	private void initListView(){
		view = content.getLayoutInflater().inflate(R.layout.selector_province_layout, null);
		listview=(ListView) view.findViewById(R.id.listview);
		title=(Button) view.findViewById(R.id.ok_btn);
		title.setText("请选择区");
		back=(ImageView) view.findViewById(R.id.executop_pop_left_iv);
		back.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				closeDialog();
			}
		});
		if(mAdapter==null){
			mAdapter=new AreasAdapter(provinceList, content);
		}
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long arg3) {
				Areas bean = (Areas) parent.getAdapter().getItem(postion);
				mClickItemListener.onConfirmClick(bean);
				closeDialog();
			}
		});
	}
	
	public void setQuitDialogListener(ClickItemListener quitDialogListener) {
		mClickItemListener = quitDialogListener;
	}
	
	
	public interface  ClickItemListener{
		public void onConfirmClick(Areas bean);
	}  
	
	public void  showDialog(){
		if (mDialog == null) {
			mDialog = new Dialog(content, R.style.load_dialog);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setContentView(view);
		}
		mDialog.show();
	}
	
	/**
	 * 关闭弹出框
	 */
	public void closeDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}


}
