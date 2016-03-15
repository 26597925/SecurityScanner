package com.x.potentialrisk;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PathPermission;
import android.content.pm.ProviderInfo;
import android.os.Handler;
import android.os.Message;
import android.os.PatternMatcher;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.x.util.Constants;
import com.x.util.DexLoader.DexLoaderCallback;
import com.x.util.IScanner;

public class ProviderRiskScanner implements IScanner<HashSet<String>>  {
	
	public static HashMap<String, HashSet<String>> providerUris = new HashMap<String, HashSet<String>>();
	
	private Context mContext;
	private LoaderManager mLoaderManager;
	private PackageInfo mPackageInfo;
	private MyHandler myHandler;
	
	public PackageInfo getmPackageInfo() {
		return mPackageInfo;
	}

	public static class MyHandler extends Handler {
		
		private WeakReference<ProviderRiskScanner> mProviderScanner;
		
		public MyHandler(ProviderRiskScanner pScanner) {
			mProviderScanner = new WeakReference<ProviderRiskScanner>(pScanner);
		}

		@Override
		public void handleMessage(Message msg) {
			if(mProviderScanner.get() != null) {
				ProviderRiskScanner pScanner = mProviderScanner.get();
				pScanner.findContentUris();
			}			
	    }
	}
	
	public ProviderRiskScanner(Context context, LoaderManager lm) {
		mContext = context;
		mLoaderManager = lm;
		myHandler = new MyHandler(this);
	}
	
	public void startScan(PackageInfo packageInfo) {
		if(providerUris.containsKey(mPackageInfo.packageName)) {
			return;
		}
		String apkPath = packageInfo.applicationInfo.publicSourceDir;
		DexLoaderCallback callback = new DexLoaderCallback(mContext,apkPath,null,Constants.PARSE_STRING,this);
		mLoaderManager.initLoader(callback.hashCode(), null, callback);
	}
	
	private void findContentUris() {
		if(providerUris.containsKey(mPackageInfo.packageName)) {
			return;
		}
		
		HashSet<String> uris = new HashSet<String>();
		ProviderInfo[] providers = mPackageInfo.providers;
		for(int i=0;i<providers.length;i++) {
			ProviderInfo info = providers[i];
			
			HashSet<String> paths = new HashSet<String>();
			PatternMatcher[] patternMatchers = info.uriPermissionPatterns;
			for(PatternMatcher pMatcher : patternMatchers) {
				paths.add(pMatcher.getPath());
			}
			
			PathPermission[] pathPermissions = info.pathPermissions;
			for(PathPermission pathPermission : pathPermissions) {
				paths.add(pathPermission.getPath());
			}
			
			String[] authorities = info.authority.split(";");
			for(String s : authorities) {
				uris.add("content://" + s + "/");
				Iterator<String> iter = paths.iterator();
				while(iter.hasNext()) {
					uris.add("content://" + s + iter.next());
				}
			}
		}
		
	
		providerUris.put(mPackageInfo.packageName, uris);
		
		HashSet<String> uris2 = providerUris.get(mPackageInfo.packageName);
		Iterator<String> iter2 = uris2.iterator();
		while(iter2.hasNext()) {
			String s = iter2.next();
			if(s.endsWith("/")) {
				int index = s.lastIndexOf('/');
				StringBuffer sb = new StringBuffer(s);
				sb.deleteCharAt(index);				
				uris2.add(sb.toString());
			}
			else {
				uris2.add(s + "/");
			}
		}
		
	}

	@Override
	public void onScanFinished(Loader<HashSet<String>> loader,
			HashSet<String> data) {
		Iterator<String> iter = data.iterator();
		HashSet<String> uris = new HashSet<String>();
		while(iter.hasNext()) {
			String s = iter.next();
			String upper = s.toUpperCase(Locale.getDefault());
			if(upper.contains("CONTENT://")) {
				uris.add(s);
			}
		}
		
		providerUris.put(mPackageInfo.packageName, uris);
		myHandler.sendEmptyMessage(0);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}
	
}
