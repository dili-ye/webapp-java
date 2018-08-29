package com.cn.webapp.commons.pub.util;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

public class FileUtil {
	/**
	 * 返回所有
	 * 
	 * @param root
	 * @param files
	 * @return
	 */
	public static List<File> parseDir(File root) {
		List<File> files = Lists.newArrayList();
		if (root.isDirectory()) {
			for (File file : root.listFiles()) {
				files.addAll(parseDir(file));
			}
		} else {
			files.add(root);
		}
		return files;
	}
}
