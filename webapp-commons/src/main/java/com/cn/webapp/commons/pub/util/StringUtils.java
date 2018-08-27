package com.cn.webapp.commons.pub.util;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;

public class StringUtils extends org.springframework.util.StringUtils {
	public static String toJsonStr(Object obj) {
		Assert.isNull(obj, "obj can not be null");
		return JSON.toJSONString(obj);
	}

	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		} else if (obj instanceof String) {
			return ((String) obj).isEmpty();
		}
		return false;
	}

	public static String replace(String s, String oldValue, String newValue) {
		return s.replace(oldValue, newValue);
	}
}
