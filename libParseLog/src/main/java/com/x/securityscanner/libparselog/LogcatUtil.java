package com.x.securityscanner.libparselog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import android.content.Context;
import android.util.Log;

public class LogcatUtil {

	public static void StartLog(Context context, boolean cleanFirst) {
		if(cleanFirst) {
			File logFile = new File(context.getFilesDir() + "/log.log");
			if(logFile.exists()) {
				logFile.delete();
			}
			try {
				Runtime.getRuntime().exec("/system/bin/logcat -c ");
			} catch (IOException e) {
				Log.i(Constants.LOG_TAG,"[CMD] \"/system/bin/logcat -c\" failed");
				e.printStackTrace();
			}
		}

//		StringBuffer cmd = new StringBuffer();
//		cmd.append("logcat -f ");
//		cmd.append(context.getFilesDir() + "/log.log ");
//		cmd.append("-s AndroidRuntime:E ");

		ProcessBuilder pb = new ProcessBuilder(new String [] {"/system/bin/logcat", 
				"-f" , context.getFilesDir() + "/log.log",
				"-s" , "AndroidRuntime:E"
				});
		
		try {
			pb.start();
			//Runtime.getRuntime().exec(cmd.toString());
		} catch (IOException e) {
			//Log.i(Constants.LOG_TAG,"[CMD] " + cmd.toString() + " failed");
			Log.i(Constants.LOG_TAG,"[CMD]  failed");
			e.printStackTrace();
		}
	}

	public static HashSet<CrashInfo> ParseLog(Context context)  {
		HashSet<CrashInfo> ci = new HashSet<CrashInfo>();
		File logFile = new File(context.getFilesDir() + "/log.log");
		if(!logFile.exists()) {
			return ci;
		}

		try {
			BufferedReader in = new BufferedReader(new FileReader(logFile));
			try {
				final String target = "ComponentInfo{";
				String line = in.readLine();
				while(line != null) {
					if(!line.contains(target)) {
						line = in.readLine();
						continue;
					}

					int start = line.indexOf(target);
					if(-1 == start) {
						line = in.readLine();
						continue;
					}
					start += target.length();

					int end = line.indexOf("}");
					if(-1 == end) {
						line = in.readLine();
						continue;
					}

					String subsString = line.substring(start, end);
					Log.i(Constants.LOG_TAG,subsString);

					line = in.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return ci;
	}
}
