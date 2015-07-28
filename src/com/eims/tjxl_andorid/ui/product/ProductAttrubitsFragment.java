/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.entity.GoodDetail;
import com.eims.tjxl_andorid.entity.GoodDetail.GoodPropert;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月17日 上午10:57:49
 *************************************************************************** 
 */
public class ProductAttrubitsFragment extends android.support.v4.app.Fragment {
	//品牌名称
	private  TextView ppName;
	//品牌编码
	private  TextView ppCode;
	
	

	public static ProductAttrubitsFragment productAttrubitsFragment;
	
    private LinearLayout ll_key_values;


	public static ProductAttrubitsFragment getInstance() {
		if (productAttrubitsFragment == null) {
			productAttrubitsFragment = new ProductAttrubitsFragment();
		}
		return productAttrubitsFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		return inflater.inflate(R.layout.product_attrubits, null);
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		findviews();
		setData();
	}

	private  void  findviews(){
		ppName=(TextView) getActivity().findViewById(R.id.ppname);
		ppCode=(TextView) getActivity().findViewById(R.id.ppcode);
		ll_key_values=(LinearLayout) getActivity().findViewById(R.id.key_value);
	}
	
	private void  setData(){
        if(ProductDeatil.detail!=null){
        	ppName.setText(ProductDeatil.detail.pdMap.brand_name);
        	ppCode.setText(ProductDeatil.detail.pdMap.commodity_code);
        	   ll_key_values.removeAllViews();
               for(int i=0;i<ProductDeatil.detail.propertyList.size();i++){
            	   GoodPropert goodPropert = ProductDeatil.detail.propertyList.get(i);
            	   View  item=View.inflate(getActivity(), R.layout.attriubts_layout_item, null);
            	   TextView key=(TextView) item.findViewById(R.id.key);
            	   TextView value=(TextView) item.findViewById(R.id.value);
            	   key.setText(goodPropert.property_name);
            	   value.setText(goodPropert.property_value);
            	   ll_key_values.addView(item);
        }
     
        	
        }
	
	}
	
    

}
