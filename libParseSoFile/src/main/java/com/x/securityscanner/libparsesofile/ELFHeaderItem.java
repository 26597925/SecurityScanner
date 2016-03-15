package com.x.securityscanner.libparsesofile;

import android.util.AndroidRuntimeException;

/***
 * ELF Header
 * @author ouhf1
 */
public class ELFHeaderItem {

	private static final byte[] MAGIC_VALUES= new byte[] { 
		0x7f, 
		0x45,  //E
		0x4c,  //L
		0x46}; //F
	
	//private static final int ITEM_SIZE = 0x34;

	private static final int SECTION_HEADER_OFFSET_IN_ELF_HEADER = 0x20;
	private static final int PROGRAM_HEADER_OFFSET_IN_ELF_HEADER = 0x1C;
	private static final int SECTION_HEADER_NUM_IN_ELF_HEADER = 0x30;
	private static final int PROGRAM_HEADER_NUM_IN_ELF_HEADER = 0x2C;
	private static final int SECTION_HEADER_STRING_TABLE_INDEX_IN_ELF_HEADER = 0x32;

	private SoFile mSoFile;

	public ELFHeaderItem(SoFile soFile) {
		mSoFile = soFile;
		if(null == mSoFile) 
			throw new AndroidRuntimeException("so file is null");
	}

	public int getSectionHeaderOffset() {
		return mSoFile.readSmallUint(SECTION_HEADER_OFFSET_IN_ELF_HEADER);
	}

	public int getSectionHeaderNum() {
		return mSoFile.readUshort(SECTION_HEADER_NUM_IN_ELF_HEADER);
	}

	public int getProgramHeaderOffset() {
		return mSoFile.readSmallUint(PROGRAM_HEADER_OFFSET_IN_ELF_HEADER);
	}

	public int getProgramHeaderNum() {
		return mSoFile.readUshort(PROGRAM_HEADER_NUM_IN_ELF_HEADER);
	}

	public int getSectionHeaderStringTableIndex() {
		return mSoFile.readUshort(SECTION_HEADER_STRING_TABLE_INDEX_IN_ELF_HEADER);
	}

	/***
	 * Verify magic character of ELF-formatted file
	 * @param buf  buffer stores file content
	 * @param offset  offset to the begin of file
	 * @return true if this file is ELF-formatted file, otherwise false 
	 */
	public static boolean verifyMagic(byte[] buf, int offset) {
		// verifies the magic value
		if (buf.length - offset < 4) {
			return false;
		}

		boolean matches = true;
		for (int i=0; i<MAGIC_VALUES.length; i++) {
			byte expected = MAGIC_VALUES[i];
			matches = true;
			if (buf[offset + i] != expected) {
				matches = false;
				break;
			}
		}
		return matches;
	}
}
