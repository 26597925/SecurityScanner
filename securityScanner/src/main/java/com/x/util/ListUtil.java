package com.x.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ListUtil {
	
	public static class MyListView extends ListView {  
		  
	    public MyListView(Context context) {   
	        super(context);  
	    }  
	  
	    public MyListView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	  
	    public MyListView(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle);  
	    }  
	  
	    @Override  
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	                MeasureSpec.AT_MOST);  
	        super.onMeasure(widthMeasureSpec, expandSpec);  
	    }  
	}  
	
	public static ListView CreateListView(Context context) {
		MyListView listView = new MyListView(context);
		listView.setDivider(null);
		listView.setVerticalScrollBarEnabled(false);
		listView.setLayoutParams(new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		return listView;
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView , BaseAdapter adapter) {       
	    if (adapter == null) {    
	        return;    
	    }    
	  
	    int totalHeight = 0;    
	    for (int i = 0; i < adapter.getCount(); i++) {    
	        View listItem = adapter.getView(i, null, listView);    
	        listItem.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);    
	        totalHeight += listItem.getMeasuredHeight();    
	    }    
	  
	    ViewGroup.LayoutParams params = listView.getLayoutParams();    
	    params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1)) ;    
	    ((MarginLayoutParams)params).setMargins(0, 15, 15, 15);
	    listView.setLayoutParams(params);    
	}   
}
