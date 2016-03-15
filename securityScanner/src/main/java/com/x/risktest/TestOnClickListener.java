package com.x.risktest;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.x.potentialrisk.AttackSurfaceCollecter;
import com.x.potentialrisk.PotentialRiskAppInfo;
import com.x.securityscanner.libparselog.LogcatUtil;
import com.x.util.Constants;

public class TestOnClickListener implements View.OnClickListener {

	private Context mContext;

	public TestOnClickListener(Context context) {
		mContext = context;
	}

	public Context getContext() {
		return mContext;
	}

	public class MyAsynTack extends AsyncTask<String, Void , Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(String... params) {

			//String packageName = params[0];
			String packageName = "sina.mobile.tianqitong";
			PotentialRiskAppInfo riskInfo = AttackSurfaceCollecter.riskApps.get(packageName);
			if(null == riskInfo)
				return null;

			ArrayList<String> exportedActivities = riskInfo.exportedActivities;

			if(exportedActivities.size() > 0) {
				Iterator<String> iter = exportedActivities.iterator();
				while(iter.hasNext()) {				
					String s = iter.next();
					ComponentName cp = new ComponentName(packageName,s);
					Intent i = new Intent();
					i.setComponent(cp);

					try {
						Log.i(Constants.LOG_TAG ,packageName + "  " + s);
						getContext().startActivity(i);		
					} catch (ActivityNotFoundException e) {
						Log.e(Constants.LOG_TAG , "ActivityNotFoundException exception: " + e.toString());
					}

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						Log.e(Constants.LOG_TAG , "Sleep exception: " + e.toString());
						e.printStackTrace();
					}
				}
			}

			//ArrayList<String> exportedServices = riskInfo.exportedServices;
			//ArrayList<String> exportedReceivers = riskInfo.exportedReceivers;
			//ArrayList<String> exportedProviders = riskInfo.exportedProviders;

			return null;
		}

	}

	@Override
	public void onClick(View v) {

		String tag = (String) v.getTag();
		if(null == tag) {
			Log.e(Constants.LOG_TAG , "Package name for testing is null.");
			return;
		}
		
		if(tag.equals("PARSE_TAG")) {
			LogcatUtil.ParseLog(mContext);
		} else {
			LogcatUtil.StartLog(mContext,false);
			MyAsynTack task = new MyAsynTack();
			task.execute(tag);
		}

	}

}
