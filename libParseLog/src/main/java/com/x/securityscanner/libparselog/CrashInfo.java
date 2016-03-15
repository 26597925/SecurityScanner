package com.x.securityscanner.libparselog;

public class CrashInfo {

	public final int CRASH_REASON_UNDEF = -1;
	public final int CRASH_REASON_START_ACTIVITY = 1;
	public final int CRASH_REASON_START_SERVICE = 2;
	public final int CRASH_REASON_BROADCAST = 3;
	
	private String mPackageName;
	private String mComponentName;
	private int mCrashReason;
	
	public String getmPackageName() {
		return mPackageName;
	}
	public void setmPackageName(String packageName) {
		this.mPackageName = packageName;
	}
	public String getmComponentName() {
		return mComponentName;
	}
	public void setmComponentName(String componentName) {
		this.mComponentName = componentName;
	}
	public int getmCrashReason() {
		return mCrashReason;
	}
	public void setmCrashReason(int crashReason) {
		this.mCrashReason = crashReason;
	}
}
