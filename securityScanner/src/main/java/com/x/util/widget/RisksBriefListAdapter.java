package com.x.util.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.x.potentialrisk.BasicRiskResult;
import com.x.securityscanner.R;
import com.x.util.Constants;

public class RisksBriefListAdapter extends BaseAdapter {
	
	class ViewHolder {
		TextView num;
		TextView desc;
		TextView desc_d;
	}

	private Context mContext;
	private ArrayList<BasicRiskResult> mContents;

	public RisksBriefListAdapter(Context context, ArrayList<BasicRiskResult> basicRiskResults) {
		mContext = context;
		mContents = basicRiskResults;
	}

	@Override
	public int getCount() {
		return mContents.size();
	}

	@Override
	public Object getItem(int position) {
		return mContents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_risk_brief, parent, false);
			vh.num = (TextView) convertView.findViewById(R.id.num);
			vh.desc =  (TextView) convertView.findViewById(R.id.desc);
			vh.desc_d =  (TextView) convertView.findViewById(R.id.desc_d);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		BasicRiskResult rResult = mContents.get(position); 
		vh.num.setText(Integer.toString(rResult.num));
		
		switch (rResult.type) {
		case Constants.RISK_TYPE_DEBUGGABLE:
			vh.desc.setText(R.string.risk_debuggable);
			vh.desc_d.setText(R.string.risk_debuggable_d);
			break;
		case Constants.RISK_TYPE_EXPORTED_ACTIVITY:
			vh.desc.setText(R.string.risk_activity);
			vh.desc_d.setText(R.string.risk_activity_d);
			break;
		case Constants.RISK_TYPE_EXPORTED_SERVICE:
			vh.desc.setText(R.string.risk_service);
			vh.desc_d.setText(R.string.risk_service_d);
			break;
		case Constants.RISK_TYPE_EXPORTED_PROVIDER:
			vh.desc.setText(R.string.risk_provider);
			vh.desc_d.setText(R.string.risk_provider_d);
			break;
		case Constants.RISK_TYPE_EXPORTED_RECEIVER:
			vh.desc.setText(R.string.risk_broadcastreceiver);
			vh.desc_d.setText(R.string.risk_broadcastreceiver_d);
			break;
		case Constants.RISK_TYPE_DANGERPERMISSION:
			vh.desc.setText(R.string.risk_dangerpermission);
			vh.desc_d.setText(R.string.risk_dangerpermission_d);
			break;
		default:
			break;
		}
		
		return convertView;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
