package com.x.util.widget;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.potentialrisk.PotentialRiskAppInfo;
import com.x.risktest.TestOnClickListener;
import com.x.securityscanner.R;

public class RisksDetailListAdapter2 extends BaseExpandableListAdapter {

	class ViewHolder {
		ImageView icon;
		TextView name;
		TextView desc;
		Button test;
		Button parse;
	}

	private Context mContext;
	private ArrayList<PotentialRiskAppInfo> mGroups;
	private TestOnClickListener mTestListener;

	public RisksDetailListAdapter2(Context context, ArrayList<PotentialRiskAppInfo> potentialRiskAppInfos , TestOnClickListener listener) {
		mContext = context;
		mGroups = potentialRiskAppInfos;
		mTestListener = listener;
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
		return null;
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_title2, parent, false);
			vh.icon = (ImageView) convertView.findViewById(R.id.app_icon);
			vh.name = (TextView) convertView.findViewById(R.id.app_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		PotentialRiskAppInfo potentialRiskAppInfo = mGroups.get(groupPosition); 
		if(potentialRiskAppInfo.icon == null) {
			vh.icon.setImageResource(R.mipmap.ic_launcher);
		} else {
			vh.icon.setImageDrawable(potentialRiskAppInfo.icon);
		}
		vh.name.setText(potentialRiskAppInfo.lable);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_risk_detail2, parent, false);
			vh.desc =  (TextView) convertView.findViewById(R.id.desc_d);
			vh.test = (Button) convertView.findViewById(R.id.test);
			vh.test.setOnClickListener(mTestListener);
			vh.parse = (Button) convertView.findViewById(R.id.parse);
			vh.parse.setOnClickListener(mTestListener);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		PotentialRiskAppInfo potentialRiskAppInfo = mGroups.get(groupPosition); 
		
		String desc = getFormatString(potentialRiskAppInfo);
		vh.desc.setText(desc);
		
		if(potentialRiskAppInfo.needTest) {
			vh.test.setVisibility(View.VISIBLE);
			vh.test.setTag(potentialRiskAppInfo.packageName);
			vh.parse.setVisibility(View.VISIBLE);
			vh.parse.setTag("PARSE_TAG");
		} else {
			vh.test.setVisibility(View.GONE);
			vh.parse.setVisibility(View.GONE);
		}

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private String getFormatString(PotentialRiskAppInfo potentialRiskAppInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");

		Iterator<String> iter = null;
		sb.append("Basic Infomation:" + "\n\n");

		sb.append("\t\tName: " + potentialRiskAppInfo.packageName + "\n");
		sb.append("\t\tVersion: " + potentialRiskAppInfo.version + "\n");
		sb.append("\t\tData: " + potentialRiskAppInfo.dataDir + "\n");
		sb.append("\t\tAPK: " + potentialRiskAppInfo.installPath + "\n");
		sb.append("\n");

		sb.append("Debuggable: " + "\n\n");
		sb.append("\t\t" + potentialRiskAppInfo.isDebuggable + "\n");
		sb.append("\n");
		if(potentialRiskAppInfo.exportedActivities.size() > 0) {
			sb.append("Exported Activities: " + "\n\n");
			iter = potentialRiskAppInfo.exportedActivities.iterator();
			while(iter.hasNext()) {
				String s = iter.next();
				sb.append("\t\t").append(s).append("\n");
			}
			sb.append("\n");
		}
		if(potentialRiskAppInfo.exportedServices.size() > 0) {
			sb.append("Exported Services: " + "\n\n");
			iter = potentialRiskAppInfo.exportedServices.iterator();
			while(iter.hasNext()) {
				String s = iter.next();
				sb.append("\t\t").append(s).append("\n");
			}
			sb.append("\n");
		}

		if(potentialRiskAppInfo.exportedProviders.size() > 0) {
			sb.append("Exported Providers: " + "\n\n");
			iter = potentialRiskAppInfo.exportedProviders.iterator();
			while(iter.hasNext()) {
				String s = iter.next();
				sb.append("\t\t").append(s).append("\n");
			}
			sb.append("\n");
		}

		if(potentialRiskAppInfo.exportedReceivers.size() > 0) {
			sb.append("Exported Receivers: " + "\n\n");
			iter = potentialRiskAppInfo.exportedReceivers.iterator();
			while(iter.hasNext()) {
				String s = iter.next();
				sb.append("\t\t").append(s).append("\n");
			}
			sb.append("\n");
		}

		if(potentialRiskAppInfo.dangerPermissions.size() > 0) {
			sb.append("Dangerous Permission: " + "\n\n");
			iter = potentialRiskAppInfo.dangerPermissions.iterator();
			while(iter.hasNext()) {
				String s = iter.next();
				sb.append("\t\t").append(s).append("\n");
			}

			sb.append("\n");
		}

		return sb.toString();
	}
}
