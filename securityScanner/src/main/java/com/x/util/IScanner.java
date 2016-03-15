package com.x.util;

import android.support.v4.content.Loader;

/**
 * Interface for ... 
 */
public interface IScanner<T> {
	
	public void start();
	
	public void onScanFinished(Loader<T> loader, T data);
	
}
