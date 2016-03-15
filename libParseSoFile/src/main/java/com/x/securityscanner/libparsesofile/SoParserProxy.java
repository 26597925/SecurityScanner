package com.x.securityscanner.libparsesofile;

import java.io.IOException;
import java.util.HashSet;

import android.util.Log;

public class SoParserProxy {
	private static final String TAG = "SoParserProxy";

	private String mSoFilePath;
	private SoFile mSoFile;

	ELFHeaderItem mElfHeader; 
	int mSectionHeaderNum;
	int mShstrtabSectionIndex;

	public SoParserProxy(String filePath) {
		mSoFilePath = filePath;
	}

	private SoFile getSoFile() {
		if(null == mSoFile) {
			try {
				mSoFile = SoFileFactory.loadSoFile(mSoFilePath);
			} catch (IOException e) {
				Log.e(TAG, "Load so file " + mSoFilePath + " failed.");
				e.printStackTrace();
			}
			
			init();
		}

		if(null == mSoFile)
			throw new NullPointerException();

		return mSoFile;
	}

	private void init() {
		if(null == mSoFile)
			throw new NullPointerException();
		mElfHeader = new ELFHeaderItem(mSoFile);
		mSectionHeaderNum = mElfHeader.getSectionHeaderNum();
	}

	/**
	 * Get constant string in .rodata section
	 * @return Strings in .rodata section
	 */
	public HashSet<String> getStringInRoDataSection() {
		ELFSectionHeaderStringTable sectionNameSection = new ELFSectionHeaderStringTable(getSoFile(), mShstrtabSectionIndex);
		HashSet<String> result = new HashSet<String>();
		for(int i=0; i<mSectionHeaderNum; i++) {
			ELFSectionHeaderItem elfSection = new ELFSectionHeaderItem(getSoFile(), i);
			if(".rodata".equals(sectionNameSection.getSectionNameString(elfSection.getSectionNameIndex()))) {
				int offset = elfSection.getSectionOffset();
				int len = elfSection.getSectionSize();
				byte[] buf = getSoFile().readAriBytes(offset, len);
				
				StringBuffer sb = new StringBuffer();
				for(int index=0; index<buf.length ;index++) {
					if(!buf.equals('\0')) {
						sb.append(buf[i]);
						continue;
					}
					sb.append('\0');
					result.add(sb.toString());
				}
				break;
			}
		}
		return result;
	}
}
