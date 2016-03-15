package com.x.util.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.potentialrisk.AttackSurfaceCollecter;
import com.x.potentialrisk.BasicRiskResult;
import com.x.potentialrisk.PotentialRiskAppInfo;
import com.x.securityscanner.R;
import com.x.util.Constants;

public class RisksDetailListAdapter extends BaseExpandableListAdapter {

	class ViewHolder {
		TextView desc_d;
	}

	private Context mContext;
	private ArrayList<BasicRiskResult> mGroups;

	public RisksDetailListAdapter(Context context, ArrayList<BasicRiskResult> results) {
		mContext = context;
		mGroups = results;
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_title, parent, false);
			vh.desc_d = (TextView) convertView.findViewById(R.id.desc_d);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		BasicRiskResult rResult = mGroups.get(groupPosition); 

		switch (rResult.type) {
		case Constants.RISK_TYPE_DEBUGGABLE:
			vh.desc_d.setText(mContext.getString(R.string.risk_debuggable_title));
			break;
		case Constants.RISK_TYPE_EXPORTED_ACTIVITY:
			vh.desc_d.setText(mContext.getString(R.string.risk_activity_title));
			break;
		case Constants.RISK_TYPE_EXPORTED_SERVICE:
			vh.desc_d.setText(mContext.getString(R.string.risk_service_title));
			break;
		case Constants.RISK_TYPE_EXPORTED_PROVIDER:
			vh.desc_d.setText(mContext.getString(R.string.risk_provider_title));
			break;
		case Constants.RISK_TYPE_EXPORTED_RECEIVER:
			vh.desc_d.setText(mContext.getString(R.string.risk_broadcastreceiver_title));
			break;
		case Constants.RISK_TYPE_DANGERPERMISSION:
			vh.desc_d.setText(mContext.getString(R.string.risk_dangerpermission_title));
			break;
		default:
			break;
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_risk_detail, parent, false);
			vh.desc_d =  (TextView) convertView.findViewById(R.id.desc_d);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		BasicRiskResult rResult = mGroups.get(groupPosition); 

		switch (rResult.type) {
		case Constants.RISK_TYPE_DEBUGGABLE:
			StringBuffer sb1 = new StringBuffer();
			sb1.append(mContext.getString(R.string.list_apps)).append("\n\n");
			Iterator<String> iter = AttackSurfaceCollecter.debuggableApps.iterator(); 
			while(iter.hasNext()) {
				String s = iter.next();
				sb1.append(s).append("\n");
			}
			vh.desc_d.setText(sb1.toString());
			break;
		case Constants.RISK_TYPE_EXPORTED_ACTIVITY:
			StringBuffer sb2 = new StringBuffer();
			sb2.append(mContext.getString(R.string.list_apps)).append("\n");
			sb2.append(getFormatString(AttackSurfaceCollecter.riskActivities));
			vh.desc_d.setText(sb2.toString());
			break;
		case Constants.RISK_TYPE_EXPORTED_SERVICE:
			StringBuffer sb3 = new StringBuffer();
			sb3.append(mContext.getString(R.string.list_apps)).append("\n");
			sb3.append(getFormatString(AttackSurfaceCollecter.riskServices));
			vh.desc_d.setText(sb3.toString());
			break;
		case Constants.RISK_TYPE_EXPORTED_PROVIDER:
			StringBuffer sb4 = new StringBuffer();
			sb4.append(mContext.getString(R.string.list_apps)).append("\n");
			sb4.append(getFormatString(AttackSurfaceCollecter.riskProviders));
			vh.desc_d.setText(sb4.toString());
			break;
		case Constants.RISK_TYPE_EXPORTED_RECEIVER:
			StringBuffer sb5 = new StringBuffer();
			sb5.append(mContext.getString(R.string.list_apps)).append("\n");
			sb5.append(getFormatString(AttackSurfaceCollecter.riskReceivers));
			vh.desc_d.setText(sb5.toString());
			break;
		case Constants.RISK_TYPE_DANGERPERMISSION:
			StringBuffer sb6 = new StringBuffer();
			sb6.append(mContext.getString(R.string.list_apps)).append("\n");
			sb6.append(getFormatString(AttackSurfaceCollecter.dangerPermissions));
			vh.desc_d.setText(sb6.toString());
			break;
		default:
			break;
		}

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private String getFormatString(HashMap<String, ArrayList<String>> inputMap) {
		Iterator<String> iter = inputMap.keySet().iterator(); 
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		while(iter.hasNext()) {
			String key = iter.next();
			sb.append(key).append("\n");
			ArrayList<String> sList = inputMap.get(key);
			Iterator<String> iter2 = sList.iterator(); 
			while(iter2.hasNext()) {
				String value = iter2.next();
				sb.append("\t\t");
				sb.append("[*] ").append(value).append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
