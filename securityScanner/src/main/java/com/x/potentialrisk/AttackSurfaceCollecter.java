package com.x.potentialrisk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

public class AttackSurfaceCollecter {

	public static HashMap<String, PotentialRiskAppInfo> riskApps = new HashMap<String, PotentialRiskAppInfo>();
	public static HashMap<String, ArrayList<String>> riskActivities = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> riskReceivers = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> riskProviders = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> riskServices = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> dangerPermissions = new HashMap<String, ArrayList<String>>();
	public static HashSet<String> debuggableApps = new HashSet<String>();


	public void collectRiskAppInfo(PackageManager pm, PackageInfo packageInfo) {	

		//Log.i(Constants.LOG_TAG,"Enter " + packageInfo.packageName);

		PotentialRiskAppInfo potentialRiskAppInfo = new PotentialRiskAppInfo();

		//potentialRiskAppInfo.icon = pm.getApplicationIcon(packageInfo.applicationInfo);
		potentialRiskAppInfo.lable = pm.getApplicationLabel(packageInfo.applicationInfo).toString();
		potentialRiskAppInfo.packageName = packageInfo.packageName;
		potentialRiskAppInfo.version = packageInfo.versionName;
		potentialRiskAppInfo.dataDir = packageInfo.applicationInfo.dataDir;
		potentialRiskAppInfo.installPath = packageInfo.applicationInfo.publicSourceDir;
		potentialRiskAppInfo.isDebuggable = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

		boolean riskButNoNeedTest = false;
		if(!debuggableApps.contains(packageInfo.packageName) 
				&& potentialRiskAppInfo.isDebuggable) {
			riskButNoNeedTest = true;
			potentialRiskAppInfo.needTest = false;
			debuggableApps.add(packageInfo.packageName);
		}

		ActivityInfo[] activityInfos = packageInfo.activities;
		if(null != activityInfos) {
			for(int i=0; i<activityInfos.length; i++) {
				if(activityInfos[i].exported 
						&& !potentialRiskAppInfo.exportedActivities.contains(activityInfos[i].name)
						&& !isLaunchActivity(pm, packageInfo.packageName, activityInfos[i].name)) {
					potentialRiskAppInfo.needTest = true;
					potentialRiskAppInfo.exportedActivities.add(activityInfos[i].name);
				}
			}
			if(!riskActivities.containsKey(packageInfo.packageName) 
					&& potentialRiskAppInfo.exportedActivities.size() > 0) {
				riskActivities.put(packageInfo.packageName, potentialRiskAppInfo.exportedActivities);
			}
		}

		ActivityInfo[] receiverInfos = packageInfo.receivers;
		if(null != receiverInfos) {
			for(int i=0; i<receiverInfos.length; i++) {
				if(receiverInfos[i].exported 
						&& !potentialRiskAppInfo.exportedReceivers.contains(receiverInfos[i].name)) {
					potentialRiskAppInfo.needTest = true;
					potentialRiskAppInfo.exportedReceivers.add(receiverInfos[i].name);
				}
			}
			if(!riskReceivers.containsKey(packageInfo.packageName) 
					&& potentialRiskAppInfo.exportedReceivers.size() > 0) {
				riskReceivers.put(packageInfo.packageName, potentialRiskAppInfo.exportedReceivers);
			}
		}

		ProviderInfo[] providerInfos = packageInfo.providers;
		if(null != providerInfos) {
			for(int i=0; i<providerInfos.length; i++) {
				if(providerInfos[i].exported 
						&& !potentialRiskAppInfo.exportedProviders.contains(providerInfos[i].name)) {
					potentialRiskAppInfo.needTest = true;
					potentialRiskAppInfo.exportedProviders.add(providerInfos[i].name);
				}
			}
			if(!riskProviders.containsKey(packageInfo.packageName) 
					&& potentialRiskAppInfo.exportedProviders.size() > 0) {
				riskProviders.put(packageInfo.packageName, potentialRiskAppInfo.exportedProviders);
			}
		}

		ServiceInfo[] serviceInfos = packageInfo.services;
		if(null != serviceInfos) {
			for(int i=0; i<serviceInfos.length; i++) {
				if(serviceInfos[i].exported 
						&& !potentialRiskAppInfo.exportedServices.contains(serviceInfos[i].name)) {
					potentialRiskAppInfo.needTest = true;
					potentialRiskAppInfo.exportedServices.add(serviceInfos[i].name);
				}
			}
			if(!riskServices.containsKey(packageInfo.packageName) 
					&& potentialRiskAppInfo.exportedServices.size() > 0) {
				riskServices.put(packageInfo.packageName, potentialRiskAppInfo.exportedServices);
			}
		}

		PermissionInfo[] dPermissionInfos = packageInfo.permissions;
		if(null != dPermissionInfos) {
			for(int i=0; i<dPermissionInfos.length; i++) {
				if(dPermissionInfos[i].protectionLevel == PermissionInfo.PROTECTION_DANGEROUS
						&& !potentialRiskAppInfo.dangerPermissions.contains(dPermissionInfos[i].name)) {
					potentialRiskAppInfo.needTest = true;
					potentialRiskAppInfo.dangerPermissions.add(dPermissionInfos[i].name);
				}
			}

			if(!dangerPermissions.containsKey(packageInfo.packageName) 
					&& potentialRiskAppInfo.dangerPermissions.size() > 0) {
				dangerPermissions.put(packageInfo.packageName, potentialRiskAppInfo.dangerPermissions);
			}
		}

		if(!riskApps.containsKey(packageInfo.packageName) 
				&& (riskButNoNeedTest
				|| potentialRiskAppInfo.needTest)) {
			riskApps.put(packageInfo.packageName, potentialRiskAppInfo);
		}

	}

	public void collectDebuggableApps(PackageInfo packageInfo) {	
		if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
			if(!debuggableApps.contains(packageInfo.packageName)) {
				debuggableApps.add(packageInfo.packageName);
			}
		}
	}

	private boolean isLaunchActivity(PackageManager pm, String packageName, String activityName) {

		Intent i = pm.getLaunchIntentForPackage(packageName);
		if(i != null) {
			ComponentName cp = i.getComponent();
			if(cp != null) {
				String pgString = cp.getPackageName();
				String clString = cp.getClassName();

				if(clString.equals(activityName)) {
					return true;
				}
				//Log.i(Constants.LOG_TAG,pgString + "  " + clString);
			}
		}

		return false;
	}

	//	public void collectRiskActivities(PackageInfo packageInfo) {
	//		String packageName = packageInfo.packageName;
	//		if(!riskActivities.containsKey(packageName)) {
	//			ArrayList<String> risks = new ArrayList<String>();
	//			ActivityInfo[] activityInfos = packageInfo.activities;
	//			if(null == activityInfos) {
	//				return;
	//			}
	//			for(int i=0; i<activityInfos.length; i++) {
	//				if(activityInfos[i].exported) {
	//					risks.add(activityInfos[i].name);
	//				}
	//			}
	//			if(risks.size() > 0) {
	//				riskActivities.put(packageName, risks);
	//			}
	//		}
	//	}
	//
	//	public void collectRiskReceivers(PackageInfo packageInfo) {
	//		String packageName = packageInfo.packageName;
	//		if(!riskReceivers.containsKey(packageName)) {
	//			ArrayList<String> risks = new ArrayList<String>();
	//			ActivityInfo[] activityInfos = packageInfo.receivers;
	//			if(null == activityInfos) {
	//				return;
	//			}
	//			for(int i=0; i<activityInfos.length; i++) {
	//				if(activityInfos[i].exported) {
	//					risks.add(activityInfos[i].name);
	//				}
	//			}
	//			if(risks.size() > 0) {
	//				riskReceivers.put(packageName, risks);
	//			}
	//		}
	//	}
	//
	//	public void collectRiskProviders(PackageInfo packageInfo) {
	//		String packageName = packageInfo.packageName;
	//		if(!riskProviders.containsKey(packageName)) {
	//			ArrayList<String> risks = new ArrayList<String>();
	//			ProviderInfo[] providerInfos = packageInfo.providers;
	//			if(null == providerInfos) {
	//				return;
	//			}
	//			for(int i=0; i<providerInfos.length; i++) {
	//				if(providerInfos[i].exported) {
	//					risks.add(providerInfos[i].name);
	//				}
	//			}
	//			if(risks.size() > 0) {
	//				riskProviders.put(packageName, risks);
	//			}
	//		}
	//	}
	//
	//
	//	public void collectRiskServices(PackageInfo packageInfo) {
	//		String packageName = packageInfo.packageName;
	//		if(!riskServices.containsKey(packageName)) {
	//			ArrayList<String> risks = new ArrayList<String>();
	//			ServiceInfo[] serviceInfos = packageInfo.services;
	//			if(null == serviceInfos) {
	//				return;
	//			}
	//			for(int i=0; i<serviceInfos.length; i++) {
	//				if(serviceInfos[i].exported) {
	//					risks.add(serviceInfos[i].name);
	//				}
	//			}
	//			if(risks.size() > 0) {
	//				riskServices.put(packageName, risks);
	//			}
	//		}
	//	}
	//
	//	public void collectContainDangerPermissionApps(PackageInfo packageInfo) {
	//		String packageName = packageInfo.packageName;
	//		if(!dangerPermissions.containsKey(packageName)) {
	//			ArrayList<String> risks = new ArrayList<String>();
	//			PermissionInfo[] dPermissionInfos = packageInfo.permissions;
	//			if(null == dPermissionInfos) {
	//				return;
	//			}
	//			for(int i=0; i<dPermissionInfos.length; i++) {
	//				if(dPermissionInfos[i].protectionLevel == PermissionInfo.PROTECTION_DANGEROUS) {
	//					risks.add(dPermissionInfos[i].name);
	//				}
	//			}
	//			if(risks.size() > 0) {
	//				dangerPermissions.put(packageName, risks);
	//			}	
	//		}
	//	}
}
