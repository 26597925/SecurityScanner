package com.x.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.x.potentialrisk.AttackSurfaceCollecter;
import com.x.potentialrisk.PotentialRiskAppInfo;

public class PackageInfoLoader extends AsyncTaskLoader<ArrayList<PackageInfo>> {

	public static class PackageInfoLoaderCallback implements LoaderCallbacks<ArrayList<PackageInfo>> {

		private WeakReference<Context> mContextRef;
		private IScanner<ArrayList<PackageInfo>> mListener;

		public PackageInfoLoaderCallback(Context context, IScanner<ArrayList<PackageInfo>> listener) {
			mContextRef = new WeakReference<Context>(context);
			mListener = listener;
		}

		@Override
		public Loader<ArrayList<PackageInfo>> onCreateLoader(int arg0, Bundle arg1) {
			return new PackageInfoLoader(mContextRef.get());
		}

		@Override
		public void onLoadFinished(Loader<ArrayList<PackageInfo>> loader, ArrayList<PackageInfo> data) {
			mListener.onScanFinished(loader,data);
		}

		@Override
		public void onLoaderReset(Loader<ArrayList<PackageInfo>> loader) {

		}
	}

	private Context mContext;

	public PackageInfoLoader(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
		stopLoading();
	}

	@Override
	public ArrayList<PackageInfo> loadInBackground() { 
		PackageManager pm = mContext.getPackageManager();
		ArrayList<PackageInfo> packages = new ArrayList<PackageInfo>();
		if(null == pm) {
			return new ArrayList<PackageInfo>();
		}

		packages = (ArrayList<PackageInfo>) getInstalledPackages(mContext,PackageManager.GET_PERMISSIONS
				| PackageManager.GET_ACTIVITIES 
				| PackageManager.GET_SERVICES
				| PackageManager.GET_PROVIDERS
				| PackageManager.GET_RECEIVERS);

		Log.i(Constants.LOG_TAG,"packages size " + packages.size());

		//		int index = 0;
		//		ArrayList<PackageInfo> sliceData = (ArrayList<PackageInfo>) getInstalledPackages(mContext,PackageManager.GET_PERMISSIONS);
		//		packages.addAll(index, sliceData);
		//		index += sliceData.size();
		//
		//		sliceData = (ArrayList<PackageInfo>) getInstalledPackages(mContext,PackageManager.GET_ACTIVITIES);
		//		packages.addAll(index, sliceData);
		//		index += sliceData.size();
		//
		//		sliceData = (ArrayList<PackageInfo>) getInstalledPackages(mContext,PackageManager.GET_SERVICES);
		//		packages.addAll(index, sliceData);
		//		index += sliceData.size();
		//
		//		sliceData = (ArrayList<PackageInfo>) getInstalledPackages(mContext,PackageManager.GET_PROVIDERS);
		//		packages.addAll(index, sliceData);
		//		index += sliceData.size();
		//
		//		sliceData = (ArrayList<PackageInfo>) getInstalledPackages(mContext,PackageManager.GET_RECEIVERS);
		//		packages.addAll(index, sliceData);
		//		index += sliceData.size();

		Iterator<PackageInfo> iter = packages.iterator();
		AttackSurfaceCollecter collection = new AttackSurfaceCollecter();
		while(iter.hasNext()) {
			PackageInfo pInfo = iter.next();
			collection.collectRiskAppInfo(pm,pInfo);
		}	

		logRiskToFile();

		return new ArrayList<PackageInfo>();
	}	

	private void logRiskToFile() {
		File riskFile = new File(Environment.getExternalStorageDirectory(),"risk_info_file.txt"); 
		BufferedWriter bw=null;
		try {
			bw = new BufferedWriter(new FileWriter(riskFile, false));
			HashMap<String, PotentialRiskAppInfo> riskAppMaps = AttackSurfaceCollecter.riskApps;
			Iterator<String> iterSet = riskAppMaps.keySet().iterator();
			while(iterSet.hasNext()) {
				String pgName = iterSet.next();
				PotentialRiskAppInfo info = riskAppMaps.get(pgName);

				StringBuffer out = new StringBuffer();
				out.append("应用名称：" + info.lable + "\n")
				.append("Package:" + pgName + "\n")
				.append("Version:" + info.version + "\n")
				.append("Data Directory:" + info.dataDir + "\n")
				.append("APK Path:" + info.installPath + "\n");

				out.append("Debuggable: " + "\n");
				out.append("\t" + info.isDebuggable + "\n");
				out.append("\n");

				if(info.exportedActivities.size() > 0) {
					out.append("Exported activities (" + info.exportedActivities.size() + ")"  + "\n");
					Iterator<String> iter = info.exportedActivities.iterator();
					while(iter.hasNext()) {
						String s = iter.next();
						out.append("\t").append(s).append("\n");
					}
					out.append("\n");
				}
				if(info.exportedServices.size() > 0) {
					out.append("Exported services (" + info.exportedServices.size() + ")"  + "\n");
					Iterator<String> iter = info.exportedServices.iterator();
					while(iter.hasNext()) {
						String s = iter.next();
						out.append("\t").append(s).append("\n");
					}
					out.append("\n");
				}

				if(info.exportedProviders.size() > 0) {
					out.append("Exported providers (" + info.exportedProviders.size() + ")"  + "\n");
					Iterator<String> iter = info.exportedProviders.iterator();
					while(iter.hasNext()) {
						String s = iter.next();
						out.append("\t").append(s).append("\n");
					}
					out.append("\n");
				}

				if(info.exportedReceivers.size() > 0) {
					out.append("Exported receivers (" + info.exportedReceivers.size() + ")"  + "\n");
					Iterator<String> iter = info.exportedReceivers.iterator();
					while(iter.hasNext()) {
						String s = iter.next();
						out.append("\t").append(s).append("\n");
					}
					out.append("\n");
				}

				bw.write(out.toString());
				bw.flush();  
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if(bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<PackageInfo> getInstalledPackages(Context context,int flags)
	{
		final PackageManager pm=context.getPackageManager();

		//if it's Android 5.1, no need to do any special work
		//if(VERSION.SDK_INT>=VERSION_CODES.LOLLIPOP_MR1)
		//return pm.getInstalledPackages(flags);
		//else, protect against exception, and use a fallback if needed:

		//try {
		//	return pm.getInstalledPackages(flags);
		//}
		//catch(Exception ignored) {
		//we don't care why it didn't succeed. We'll do it using an alternative way instead
		//}

		Process process;
		List<PackageInfo> result = new ArrayList<>();
		BufferedReader bufferedReader = null;
		try {
			process = Runtime.getRuntime().exec("pm list packages");
			bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line=bufferedReader.readLine())!=null) {
				final String packageName = line.substring(line.indexOf(':')+1);
				if(packageName.equals("android")) 
					continue;
				final PackageInfo packageInfo = pm.getPackageInfo(packageName,flags);
				result.add(packageInfo);
			}
			process.waitFor();
		}
		catch(Exception e) {
			Log.e(Constants.LOG_TAG, "Remain error: ");
			Log.e(Constants.LOG_TAG, e.toString());
			e.printStackTrace();
		}
		finally {
			if(bufferedReader!=null) {
				try {
					bufferedReader.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
}
