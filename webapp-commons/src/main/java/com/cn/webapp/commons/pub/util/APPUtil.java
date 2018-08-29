package com.cn.webapp.commons.pub.util;

public class APPUtil {
	public static boolean isWin() {
		return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
	}
}
