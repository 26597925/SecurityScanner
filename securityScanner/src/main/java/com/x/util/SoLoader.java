package com.x.util;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import com.x.securityscanner.libparsesofile.SoParserProxy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/** 
 *  Load *.so file asynchronous
 */
public class SoLoader extends AsyncTaskLoader<HashSet<String>> {

	/**
	 * Callback for *.so file loader.
	 * @see SoLoader
	 */
	public static class SoLoaderCallback implements LoaderCallbacks<HashSet<String>> {

		private WeakReference<Context> mContextRef;
		private String mSoFilePath;
		private IScanner<HashSet<String>> mListener;

		public SoLoaderCallback(Context context, String soFilePath, IScanner<HashSet<String>> listener) {
			mContextRef = new WeakReference<Context>(context);
			mSoFilePath = soFilePath;
			mListener = listener;
		}

		@Override
		public Loader<HashSet<String>> onCreateLoader(int arg0, Bundle arg1) {
			return new SoLoader(mContextRef.get(), mSoFilePath);
		}

		@Override
		public void onLoadFinished(Loader<HashSet<String>> loader, HashSet<String> data) {
			mListener.onScanFinished(loader,data);
		}

		@Override
		public void onLoaderReset(Loader<HashSet<String>> loader) {

		}
	}

	private String mSoFilePath;

	public SoLoader(Context context, String soFilePath) {
		super(context);
		mSoFilePath = soFilePath;
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
		SoParserProxy proxy = new SoParserProxy(mSoFilePath);
		return proxy.getStringInRoDataSection();
	}
}
