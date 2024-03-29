package com.eims.tjxl_andorid.weght;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView {

//	   public boolean haveScrollbar = true;
     	private boolean haveScrollbars=false;
	    public MyGridView(Context context) {   
	        super(context);   
	    }   
	    public MyGridView(Context context, AttributeSet attrs) {   
	        super(context, attrs);   
	    }   
	    public MyGridView(Context context, AttributeSet attrs, int defStyle) {   
	        super(context, attrs, defStyle);   
	    }   
	    /**  
	     * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为false。 默认为 true  
	     *   
	     * @param haveScrollbar
	     */   
	    public void setHaveScrollbar(boolean haveScrollbar) {   
	        this.haveScrollbars = haveScrollbar;   
	    }   
	    @Override   
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
	        if (haveScrollbars == false) {   
	            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
	            super.onMeasure(widthMeasureSpec, expandSpec);   
	        } else {   
	            super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
	        }   
	    }   

}
