package com.x.util.widget;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.x.securityscanner.R;
import com.x.util.Constants;
import com.x.vulnerability.VulnBriefResult;

public class VulnsDetailListAdapter extends BaseAdapter {

	class ViewHolder {
		ImageView icon;
		TextView desc;
		TextView desc_d;
	}

	private Context mContext;
	private ArrayList<VulnBriefResult> mContents;

	public VulnsDetailListAdapter(Context context, ArrayList<VulnBriefResult> vulnResult) {
		mContext = context;
		mContents = vulnResult;
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
		return mContents.get(position).vulnType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vuln_detail, parent, false);

			vh.icon = (ImageView) convertView.findViewById(R.id.icon);
			vh.desc =  (TextView) convertView.findViewById(R.id.desc);
			vh.desc_d = (TextView) convertView.findViewById(R.id.desc_d);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		VulnBriefResult vResult = mContents.get(position);
		if(vResult.result) {
			vh.icon.setImageResource(R.drawable.error_icon);
		} else {
			vh.icon.setImageResource(R.drawable.right_icon);
		}

		switch (vResult.vulnType) {
		case Constants.VULN_TYPE_CVE_2014_8609:
			vh.desc.setText(mContext.getString(R.string.vuln_settings_priv_leakage) 
					+ ": "
					+ (vResult.result ? mContext.getString(R.string.vuln_vulnerable) : mContext.getString(R.string.vuln_patched)));
			vh.desc_d.setText(mContext.getString(R.string.vuln_settings_priv_leakage_d));
			break;
		case Constants.VULN_TYPE_CVE_2014_7911:
			vh.desc.setText(mContext.getString(R.string.vuln_sys_serialize)
					+ ": "
					+ (vResult.result ? mContext.getString(R.string.vuln_vulnerable) : mContext.getString(R.string.vuln_patched)));
			vh.desc_d.setText(mContext.getString(R.string.vuln_sys_serialize_d));
			break;
		case Constants.VULN_TYPE_CVE_2015_1474:
			vh.desc.setText(mContext.getString(R.string.vuln_buf_overflow)
					+ ": "
					+ (vResult.result ? mContext.getString(R.string.vuln_vulnerable) : mContext.getString(R.string.vuln_patched)));
			vh.desc_d.setText(mContext.getString(R.string.vuln_buf_overflow_d));
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
