package com.x.securityscanner.libparsesofile;

/**
 * ELF section header string table (.shstrtab section)
 * @author ouhf1
 *
 */
public class ELFSectionHeaderStringTable {

	private byte[] mSectionHeaderBytes;
	private SoFile mSoFile;
	private int mOffset;
	private int mLen;

	public ELFSectionHeaderStringTable(SoFile soFile, int index) {
		ELFSectionHeaderItem headerItem = new ELFSectionHeaderItem(soFile, index);
		mSoFile = soFile;
		mOffset = headerItem.getSectionOffset();
		mLen = headerItem.getSectionSize();
	}

	/**
	 * Get section name with index
	 * @param index  index of section
	 * @return The name of specific section
	 */
	public String getSectionNameString(int index) {
		if(null == mSectionHeaderBytes) {
			mSectionHeaderBytes = mSoFile.readAriBytes(mOffset, mLen);
		}

		StringBuffer sb = new StringBuffer();

		for(int i=index; i<mSectionHeaderBytes.length ;i++) {
			if(!mSectionHeaderBytes.equals('\0')) {
				sb.append(mSectionHeaderBytes[i]);
				continue;
			}
			sb.append('\0');
			break;
		}

		return sb.toString();
	}
}
