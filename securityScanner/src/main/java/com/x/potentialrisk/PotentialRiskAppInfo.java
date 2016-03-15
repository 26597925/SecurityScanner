package com.x.potentialrisk;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

public class PotentialRiskAppInfo {
	public boolean needTest = false;
	public Drawable icon; 
	public String lable;
	public String packageName;
	public String version;
	public String dataDir;
	public String installPath;
	public boolean isDebuggable = false;
	public ArrayList<String> exportedActivities = new ArrayList<String>();
	public ArrayList<String> exportedServices = new ArrayList<String>();
	public ArrayList<String> exportedProviders = new ArrayList<String>();
	public ArrayList<String> exportedReceivers = new ArrayList<String>();
	public ArrayList<String> dangerPermissions = new ArrayList<String>();
}
