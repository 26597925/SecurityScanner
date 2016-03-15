package com.x.securityscanner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.x.potentialrisk.AttackSurfaceCollecter;
import com.x.potentialrisk.BasicRiskScanner;
import com.x.potentialrisk.BasicRiskResult;
import com.x.util.Constants;
import com.x.util.ListUtil;
import com.x.util.widget.RisksBriefListAdapter;
import com.x.util.widget.VulnsBriefListAdapter;
import com.x.vulnerability.CVE20147911Scanner;
import com.x.vulnerability.CVE20148609Scanner;
import com.x.vulnerability.CVE20151474Scanner;
import com.x.vulnerability.VulnBriefResult;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
		
	private View mVulnLayout = null;
	private View mRiskLayout = null;
	private View mScoreLayout = null;
	private TextView mVulnInfoLink = null;
	private TextView mRiskInfoLink = null;

	private ListView mVulnsListView = null;
	private ListView mRiskListView = null;

	private VulnsBriefListAdapter mVulnsBriefListAdapter;
	private RisksBriefListAdapter mRisksBriefListAdapter;

	private static ArrayList<VulnBriefResult> vulnResult = new ArrayList<VulnBriefResult>();
	private static ArrayList<BasicRiskResult> basicRiskResults = new ArrayList<BasicRiskResult>();

	private static boolean allVulnLoadFinished = false;

	public static ArrayList<VulnBriefResult> getVulnResult() {
		return vulnResult;
	}

	public static ArrayList<BasicRiskResult> getRisksBriefResults() {
		return basicRiskResults;
	}

	public static class MyHandler extends Handler {
		WeakReference<Context> mContextReference;

		MyHandler(Context context) {
			mContextReference = new WeakReference<Context>(context);
		}

		@Override
		public void handleMessage(Message msg) {
			final Context context = mContextReference.get();
			MainActivity ref = (MainActivity) context;
			switch (msg.what) {
			case Constants.MSG_TYPE_VULN_CVE20148609:
				if(!allVulnLoadFinished) {
					vulnResult.add(new VulnBriefResult(Constants.VULN_TYPE_CVE_2014_8609, msg.arg1 > 0 ? true : false));
					ref.showVulnsBriefInfo();
				}
				break;
			case Constants.MSG_TYPE_VULN_CVE20147911:
				if(!allVulnLoadFinished) {
					vulnResult.add(new VulnBriefResult(Constants.VULN_TYPE_CVE_2014_7911, msg.arg1 > 0 ? true : false));
					ref.showVulnsBriefInfo();
				}
				break;
			case Constants.MSG_TYPE_VULN_CVE20151474:
				if(!allVulnLoadFinished) {
					vulnResult.add(new VulnBriefResult(Constants.VULN_TYPE_CVE_2015_1474, msg.arg1 > 0 ? true : false));
					ref.showVulnsBriefInfo();
				}
				break;
			case Constants.MSG_TYPE_RISK_TOTAL:
				ref.showRisksBriefInfo();
				break;
			default:
				break;
			}
		}
	}

	private MyHandler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(Constants.LOG_TAG,"onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_main);
		initView();
		initData();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.i(Constants.LOG_TAG,"onBackPressed");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(Constants.LOG_TAG,"onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(Constants.LOG_TAG,"onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(Constants.LOG_TAG,"onResume");
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		Log.i(Constants.LOG_TAG,"onPostResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(Constants.LOG_TAG,"onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(Constants.LOG_TAG,"onStop");
	}

	private void initView() {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);     

		FrameLayout frame = (FrameLayout) findViewById(R.id.frame_score);
		mScoreLayout = inflater.inflate(R.layout.card_score,null);
		frame.addView(mScoreLayout);

		frame = (FrameLayout) findViewById(R.id.frame_vuln);
		mVulnLayout = inflater.inflate(R.layout.card_vuln,null);  
		frame.addView(mVulnLayout);

		frame = (FrameLayout) findViewById(R.id.frame_risk);
		mRiskLayout = inflater.inflate(R.layout.card_risk,null);  
		frame.addView(mRiskLayout);

		mRiskListView = ListUtil.CreateListView(this);
		LinearLayout content = (LinearLayout) mRiskLayout.findViewById(R.id.risk_details);
		content.addView(mRiskListView);		

		mVulnsListView = ListUtil.CreateListView(this);
		LinearLayout content2 = (LinearLayout) mVulnLayout.findViewById(R.id.v_details);
		content2.addView(mVulnsListView);

		mVulnsBriefListAdapter = new VulnsBriefListAdapter(this, vulnResult);
		mRisksBriefListAdapter = new RisksBriefListAdapter(this, basicRiskResults);

		mVulnInfoLink = (TextView) mVulnLayout.findViewById(R.id.v_info_link);
		mVulnInfoLink.setOnClickListener(this);

		mRiskInfoLink = (TextView) mRiskLayout.findViewById(R.id.risk_info_link);
		mRiskInfoLink.setOnClickListener(this);
	}

	private void initData() {
		vulnResult.clear();
		allVulnLoadFinished = false;
		mHandler = new MyHandler(this);

		mVulnLayout.findViewById(R.id.v_progress_bar).setVisibility(View.VISIBLE);

		LoaderManager lm = getSupportLoaderManager();
		PackageManager pm = getPackageManager();


		CVE20148609Scanner cVE20148609Scanner = new CVE20148609Scanner(this, pm, lm, mHandler);
		cVE20148609Scanner.start();


		CVE20147911Scanner cVE20147911Scanner = new CVE20147911Scanner(this, lm, mHandler);
		cVE20147911Scanner.start();

		CVE20151474Scanner cVE20151474Scanner = new CVE20151474Scanner(this, lm, mHandler);
		cVE20151474Scanner.start();

		BasicRiskScanner basicRiskScanner = new BasicRiskScanner(this, pm, lm, mHandler);
		basicRiskScanner.start();

	}

	public void showVulnsBriefInfo() {
		if(3 == vulnResult.size()) {
			allVulnLoadFinished = true;
			mVulnLayout.findViewById(R.id.v_progress_bar).setVisibility(View.GONE);
			mVulnsListView.setAdapter(mVulnsBriefListAdapter);
			ListUtil.setListViewHeightBasedOnChildren(mVulnsListView,mVulnsBriefListAdapter);
			mVulnInfoLink.setVisibility(View.VISIBLE);
		}
	} 

	public void showRisksBriefInfo() {

		basicRiskResults.clear();

		if(AttackSurfaceCollecter.debuggableApps.size() > 0) {
			basicRiskResults.add(new BasicRiskResult(
					Constants.RISK_TYPE_DEBUGGABLE, AttackSurfaceCollecter.debuggableApps.size()));
		}
		if(AttackSurfaceCollecter.dangerPermissions.size() > 0) {
			basicRiskResults.add(new BasicRiskResult(
					Constants.RISK_TYPE_DANGERPERMISSION, AttackSurfaceCollecter.dangerPermissions.size()));
		}

		Iterator iter = AttackSurfaceCollecter.riskActivities.keySet().iterator();
		int num = 0;
		while(iter.hasNext()) {
			num += AttackSurfaceCollecter.riskActivities.get(iter.next()).size();
		}
		if(num > 0) {
			basicRiskResults.add(new BasicRiskResult(
					Constants.RISK_TYPE_EXPORTED_ACTIVITY, num));
		}

		iter = AttackSurfaceCollecter.riskServices.keySet().iterator();
		num = 0;
		while(iter.hasNext()) {
			num += AttackSurfaceCollecter.riskServices.get(iter.next()).size();
		}
		if(num > 0) {
			basicRiskResults.add(new BasicRiskResult(
					Constants.RISK_TYPE_EXPORTED_SERVICE, num));
		}

		iter = AttackSurfaceCollecter.riskProviders.keySet().iterator();
		num = 0;
		while(iter.hasNext()) {
			num += AttackSurfaceCollecter.riskProviders.get(iter.next()).size();
		}
		if(num > 0) {
			basicRiskResults.add(new BasicRiskResult(
					Constants.RISK_TYPE_EXPORTED_PROVIDER, num));
		}

		iter = AttackSurfaceCollecter.riskReceivers.keySet().iterator();
		num = 0;
		while(iter.hasNext()) {
			num += AttackSurfaceCollecter.riskReceivers.get(iter.next()).size();
		}
		if(num > 0) {
			basicRiskResults.add(new BasicRiskResult(
					Constants.RISK_TYPE_EXPORTED_RECEIVER, num));
		}

		mRiskLayout.findViewById(R.id.risk_progress_bar).setVisibility(View.GONE);
		mRiskListView.setAdapter(mRisksBriefListAdapter);
		ListUtil.setListViewHeightBasedOnChildren(mRiskListView,mRisksBriefListAdapter);

		mRiskLayout.findViewById(R.id.risk_info_link).setVisibility(View.VISIBLE);
	}


	@Override
	public void onClick(View v) {
		if(R.id.v_info_link == v.getId()) {
			VulnsDetailActivity.actionStart(this);
		} else if (R.id.risk_info_link == v.getId()) {
			RisksDetailActivity.actionStart(this);
		} else {

		}
	} 
}