package com.x.securityscanner.libparsesofile;

/***
 * ELF section header
 * @author ouhf1
 */
public class ELFSectionHeaderItem {

	private static final int SECTION_HEADER_ITEM_SIZE = 0x28;

//	private static final int SHT_NULL = 0;
//	private static final int SHT_PROGBITS = 1;
//	private static final int SHT_SYMTAB = 2;
//	private static final int SHT_STRTAB = 3;
//	private static final int SHT_RELA = 4;
//	private static final int SHT_HASH = 5;
//	private static final int SHT_DYNAMIC = 6;
//	private static final int SHT_NOTE = 7;
//	private static final int SHT_NOBITS = 8;
//	private static final int SHT_REL = 9;
//	private static final int SHT_SHLIB = 10;
//	private static final int SHT_DYNSYM = 11;
//
//	private static final int SHT_LOPROC = 0x70000000;
//	private static final int SHT_HIPROC = 0x7FFFFFFF;
//	private static final int SHT_LOUSER = 0x80000000;
//	private static final int SHT_HIUSER = 0x8FFFFFFF;


	private int mOffset;
	private SoFile mSoFile;

	public ELFSectionHeaderItem(SoFile soFile, int index) {
		this.mSoFile = soFile;
		ELFHeaderItem headerItem = new ELFHeaderItem(mSoFile);
		this.mOffset = headerItem.getSectionHeaderOffset() + index*SECTION_HEADER_ITEM_SIZE;
	}

	public int getSectionNameIndex() {
		return mSoFile.readInt(mOffset + 0);
	}

	public int getSectionType() {
		return mSoFile.readInt(mOffset + 4);
	}

	public int getSectionFLag() {
		return mSoFile.readInt(mOffset + 8);
	}

	public int getSectionAddr() {
		return mSoFile.readInt(mOffset + 12);
	}

	public int getSectionOffset() {
		return mSoFile.readInt(mOffset + 16);
	}

	public int getSectionSize() {
		return mSoFile.readInt(mOffset + 20);
	}

	public int getSectionLink() {
		return mSoFile.readInt(mOffset + 24);
	}

	public int getSectionInfo() {
		return mSoFile.readInt(mOffset + 28);
	}

	public int getSectionAddrAlign() {
		return mSoFile.readInt(mOffset + 32);
	}

	public int getSectionEntrySize() {
		return mSoFile.readInt(mOffset + 36);
	}
}
