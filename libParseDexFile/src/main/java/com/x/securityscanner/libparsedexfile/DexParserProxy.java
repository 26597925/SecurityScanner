package com.x.securityscanner.libparsedexfile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.dexbacked.DexBackedClassDef;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedField;
import org.jf.dexlib2.dexbacked.DexBackedMethod;

import android.util.Log;

/**
 * Proxy class for *.dex file parse. It allows user to interact with the dex file parser in smali/backsmali library.  
 */
public class DexParserProxy {
	
	private static final String TAG = "DexParserProxy";
	private String mDexFilePath;
	private DexBackedDexFile mDexFile;

	public DexParserProxy(String filePath) {
		mDexFilePath = filePath;
	}

	private DexBackedDexFile getDexFile() {
		if(null == mDexFile) {
			try {
				mDexFile = DexFileFactory.loadDexFile(mDexFilePath,0,false);
			} catch (IOException e) {
				Log.e(TAG, "Load dex file " + mDexFilePath + " failed.");
				e.printStackTrace();
			}
		}

		if(null == mDexFile)
			throw new NullPointerException();

		return mDexFile;
	}

	/**
	 * Get all strings in a specific dex file.
	 * @return HashSet<String> strings set
	 */
	public HashSet<String> getStringInDex() {
		HashSet<String> stringInfoSet = new HashSet<String>();
		int stringCount = getDexFile().getStringCount();
		for(int i=0;i<stringCount;i++) {
			stringInfoSet.add(getDexFile().getString(i));
		}
		return stringInfoSet;
	}

	/**
	 * Get all classes name in a specific dex file.
	 * @return HashSet<String> class name set
	 */
	public HashSet<String> getClassInDex() {
		HashSet<String> classInfoSet = new HashSet<String>();
		Set<? extends DexBackedClassDef> classDefs = getDexFile().getClasses();
		Iterator<? extends DexBackedClassDef> iter = classDefs.iterator();
		while(iter.hasNext()) {
			DexBackedClassDef classElement = iter.next();
			classInfoSet.add(classElement.getType());
		}
		return classInfoSet;
	}

	/**
	 * Get all methods name in a specific class.
	 * @return HashSet<String> method name set
	 */
	public HashSet<String> getMethodsInClass(String className) {
		Set<? extends DexBackedClassDef> classDefs = getDexFile().getClasses();
		DexBackedClassDef targetClassDef = null;
		Iterator<? extends DexBackedClassDef> iter = classDefs.iterator();
		while(iter.hasNext()) {
			DexBackedClassDef tmpClassDef = iter.next();
			if(tmpClassDef.getType().contains(className)) {
				targetClassDef = tmpClassDef;
				break;
			}
		}
		
		HashSet<String> methodsInfoSet = new HashSet<String>();
		if(targetClassDef != null) {
			Iterator<? extends DexBackedMethod> iterMethods = targetClassDef.getMethods().iterator();
			while(iterMethods.hasNext()) {
				DexBackedMethod method = iterMethods.next();
				methodsInfoSet.add(method.getName());
			}
		}
		
		return methodsInfoSet;
	}
	
	/**
	 * Get all fields name in a specific class.
	 * @return HashSet<String> field name set
	 */
	public HashSet<String> getFieldsInClass(String className) {
		Set<? extends DexBackedClassDef> classDefs = getDexFile().getClasses();
		DexBackedClassDef targetClassDef = null;
		Iterator<? extends DexBackedClassDef> iter = classDefs.iterator();
		while(iter.hasNext()) {
			DexBackedClassDef tmpClassDef = iter.next();
			if(tmpClassDef.getType().contains(className)) {
				targetClassDef = tmpClassDef;
				break;
			}
		}
		
		HashSet<String> fieldsInfoSet = new HashSet<String>();
		if(targetClassDef != null) {
			Iterator<? extends DexBackedField> iterFields = targetClassDef.getFields().iterator();
			while(iterFields.hasNext()) {
				DexBackedField field = iterFields.next();
				fieldsInfoSet.add(field.getName());
			}
		}
		
		return fieldsInfoSet;
		
	}
	
	/**
	 * Get all operation codes in a specific dex file.
	 * @return HashSet<String> operation code set
	 */
	public HashSet<String> getOpcodesInClass(String className) {
		//TODO
		HashSet<String> classInfoSet = new HashSet<String>();
		return classInfoSet;
	}
	
}
