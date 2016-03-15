package com.x.securityscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.x.util.widget.VulnsDetailListAdapter;

public class VulnsDetailActivity extends Activity {

	public static void actionStart(Context context) {
		Intent intent = new Intent(context,VulnsDetailActivity.class);
		context.startActivity(intent);
	}
	
	private ListView vulnsDetailList;
	private VulnsDetailListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_vulns_detail);
		initView();
		initData();
	}
	
	private void initView() {
		vulnsDetailList = (ListView) findViewById(R.id.vulns_detail_list);
		vulnsDetailList.setDivider(null);
	}
	
	private void initData() {
		adapter = new VulnsDetailListAdapter(this,MainActivity.getVulnResult());
		vulnsDetailList.setAdapter(adapter);
	}
}
