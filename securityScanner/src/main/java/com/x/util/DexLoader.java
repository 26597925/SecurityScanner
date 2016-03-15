package com.x.util;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.x.securityscanner.libparsedexfile.DexParserProxy;

/** 
 *  Load *.dex file asynchronous
 */
public class DexLoader extends AsyncTaskLoader<HashSet<String>> {

	/**
	 * Callback for *.dex file loader.
	 * @see DexLoader
	 */
	public static class DexLoaderCallback implements LoaderCallbacks<HashSet<String>> {

		private WeakReference<Context> mContextRef;
		private String mDexfilePath;
		private String mClassName;
		private int mFlag;
		private IScanner<HashSet<String>> mListener;

		public DexLoaderCallback(Context context, String dexfilePath, String className, int flag, IScanner<HashSet<String>> listener) {
			mContextRef = new WeakReference<Context>(context);
			mDexfilePath = dexfilePath;
			mClassName = className;
			mFlag = flag;
			mListener = listener;
		}

		@Override
		public Loader<HashSet<String>> onCreateLoader(int arg0, Bundle arg1) {
			return new DexLoader(mContextRef.get(), mDexfilePath, mClassName, mFlag);
		}

		@Override
		public void onLoadFinished(Loader<HashSet<String>> loader,
				HashSet<String> data) {
			mListener.onScanFinished(loader,data);
		}

		@Override
		public void onLoaderReset(Loader<HashSet<String>> loader) {

		}
	}

	private String mDexfilePath;
	private String mClassName;
	private int mFlag;

	public DexLoader(Context context, String dexfilePath, String className, int flag) {
		super(context);
		mDexfilePath = dexfilePath;
		mClassName = className;
		mFlag = flag;
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
	public HashSet<String> loadInBackground() {

		DexParserProxy parser = new DexParserProxy(mDexfilePath);
		HashSet<String> featureSet = new HashSet<String>();

		switch (mFlag) {
		case Constants.PARSE_STRING:
			featureSet = parser.getStringInDex(); 
			break;
		case Constants.PARSE_CLASS:
			featureSet = parser.getClassInDex(); 
			break;
		case Constants.PARSE_METHODS_IN_CLASS:
			featureSet = parser.getMethodsInClass(mClassName); 
			break;
		case Constants.PARSE_FIELDS_IN_CLASS:
			featureSet = parser.getFieldsInClass(mClassName); 
			break;
		case Constants.PARSE_OPCODES_IN_CLASS:
			featureSet = parser.getOpcodesInClass(mClassName); 
			break;
		default:
			break;
		}
		return featureSet;
	}
}
