package com.x.securityscanner.libparsesofile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.support.annotation.NonNull;
import android.util.AndroidRuntimeException;

public final class SoFileFactory {
	
	private SoFileFactory() {}
	
	/**
	 * Load so file with specific path
	 * @param filePath  so file path
	 * @return SoFile 
	 * @throws IOException
	 * @see SoFile
	 */
	@NonNull
	public static SoFile loadSoFile(String filePath) throws IOException {
		File soFile = new File(filePath);
		if(!soFile.exists())
			throw new AndroidRuntimeException("So file " + filePath + " not exist.");

		InputStream inputStream = new BufferedInputStream(new FileInputStream(soFile));

		try {
			return SoFile.fromInputStream(inputStream);
		} catch (AndroidRuntimeException ex) {
			ex.printStackTrace();
		} finally {
			inputStream.close();
		}
		
		throw new AndroidRuntimeException(filePath + "  is not a so file.");
	}
}
