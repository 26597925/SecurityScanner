package com.x.util;

public class Constants {

	public static final String LOG_TAG = "XSecurityScanner"; 
	
	/** Parse all strings in *.dex file */
	public static final int PARSE_STRING = 1;
	/** Parse all classes in *.dex file */
	public static final int PARSE_CLASS = 2;
	/** Parse all methods in a specific class */
	public static final int PARSE_METHODS_IN_CLASS = 3;
	/** Parse all fields in a specific class */
	public static final int PARSE_FIELDS_IN_CLASS = 4;
	/** Parse all opcodes in a specific class */
	public static final int PARSE_OPCODES_IN_CLASS = 5;

	/** Represents CVE-2014-8609 */
	public static final int VULN_TYPE_CVE_2014_8609 = 1;
	/** Represents CVE_2014_7911 */
	public static final int VULN_TYPE_CVE_2014_7911 = 2;
	/** Represents CVE_2015_1474 */
	public static final int VULN_TYPE_CVE_2015_1474 = 3;
	
	/** Represents application that allows debugging */
	public static final int	RISK_TYPE_DEBUGGABLE = 1;
	/** Represents exported activity*/
	public static final int	RISK_TYPE_EXPORTED_ACTIVITY = 2;
	/** Represents exported service*/
	public static final int	RISK_TYPE_EXPORTED_SERVICE = 3;
	/** Represents exported provider*/
	public static final int	RISK_TYPE_EXPORTED_PROVIDER = 4;
	/** Represents exported broadcast receiver*/
	public static final int	RISK_TYPE_EXPORTED_RECEIVER = 5;
	/** Represents danger permission*/
	public static final int	RISK_TYPE_DANGERPERMISSION = 6;
	
	/** Message type for handler*/
	public static final int MSG_TYPE_VULN_CVE20148609 = 1;	
	public static final int MSG_TYPE_VULN_CVE20147911 = 2;	
	public static final int MSG_TYPE_VULN_CVE20151474 = 3;	
	public static final int MSG_TYPE_RISK_TOTAL = 4;
	
}
