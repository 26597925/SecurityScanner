package com.x.securityscanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.x.potentialrisk.AttackSurfaceCollecter;
import com.x.potentialrisk.PotentialRiskAppInfo;
import com.x.risktest.TestOnClickListener;
import com.x.util.widget.RisksDetailListAdapter2;

public class RisksDetailActivity extends Activity {

	public static void actionStart(Context context) {
		Intent intent = new Intent(context,RisksDetailActivity.class);
		context.startActivity(intent);
	}
	
	private ExpandableListView risksDetailList;
	private RisksDetailListAdapter2 adapter;
	private TestOnClickListener testListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_risks_detail);
		initView();
		initData();
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Log.i("ouhf", "onBackPressed");
		String s = getTopActivity(this);
		if(s != null) {
			Log.i("ouhf", s);
		}
	}
	

	private void initView() {
		risksDetailList = (ExpandableListView) findViewById(R.id.risks_detail_list);
		//risksDetailList.setDivider(null);
		risksDetailList.setGroupIndicator(null);
	}
	
	private void initData() {
		ArrayList<PotentialRiskAppInfo> potentialRiskAppInfos = new ArrayList<PotentialRiskAppInfo>();
		Iterator<String> iter = AttackSurfaceCollecter.riskApps.keySet().iterator();
		while (iter.hasNext()) {
			potentialRiskAppInfos.add(AttackSurfaceCollecter.riskApps.get(iter.next()));			
		}
		testListener = new TestOnClickListener(this);
		adapter = new RisksDetailListAdapter2(this,potentialRiskAppInfos,testListener);
		risksDetailList.setAdapter(adapter);
	}
	
	private String getTopActivity(Activity context)
	{
	     ActivityManager manager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE) ;
	     List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1) ;
	         
	     if(runningTaskInfos != null)
	       return (runningTaskInfos.get(0).topActivity).toString() ;
	          else
	       return null ;
	}
}
