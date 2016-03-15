package com.x.potentialrisk;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.x.util.Constants;
import com.x.util.IScanner;
import com.x.util.PackageInfoLoader.PackageInfoLoaderCallback;

public class BasicRiskScanner implements IScanner<ArrayList<PackageInfo>> {

	private Context mContext;
	private LoaderManager mLoaderManager;
	private Handler mHandler;

	/**
	 * @param context: Context
	 * @param pm: PackageManager
	 * @param lm: LoaderManager
	 * @param handler: Handler
	 */
	public BasicRiskScanner(Context context, PackageManager pm, LoaderManager lm, Handler handler) {
		mContext = context;
		mLoaderManager = lm;
		mHandler = handler;
	}

	@Override
	public void start() {
		init();
	}

	private void init() {
		PackageInfoLoaderCallback callback = new PackageInfoLoaderCallback(mContext,this);
		mLoaderManager.initLoader(callback.hashCode(), null, callback);
	}

	@Override
	public void onScanFinished(Loader<ArrayList<PackageInfo>> loader,
			ArrayList<PackageInfo> data) {
		mHandler.sendEmptyMessage(Constants.MSG_TYPE_RISK_TOTAL);
	}
}
