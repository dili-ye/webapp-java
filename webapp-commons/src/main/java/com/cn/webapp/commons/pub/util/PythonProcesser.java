package com.cn.webapp.commons.pub.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import com.cn.webapp.commons.exception.ScriptException;
import com.google.common.collect.Lists;

public class PythonProcesser {

	private static String commandPath = "/home/tangjf/programs/workspace/java/springboot/webapp-script/python/";
	private static String exe = "python3";
	private static String encoding = "utf-8";

	public static List<String> execute(String commandName, String... params) throws ScriptException {
		List<String> result = Lists.newArrayList();
		String[] cmdArr = new String[params.length + 2];

		if (APPUtil.isWin()) {
			commandPath = "D:\\springboot\\webapp-script\\python\\";
			exe = "python";
			encoding = "gbk";
		}
		cmdArr[0] = exe;
		cmdArr[1] = commandPath + commandName;
		for (int i = 0; i < params.length; i++) {
			cmdArr[2 + i] = params[i];
		}
		try {
			Process process = Runtime.getRuntime().exec(cmdArr);
			InputStream inputStream = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
			String str = null;
			while ((str = reader.readLine()) != null) {
				if (str.toLowerCase().contains("exception:")) {
					throw new ScriptException();
				}
				result.add(str);
			}
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void executeWithoutResult(String commandName, String... params) throws IOException{
		String[] cmdArr = new String[params.length + 2];
		if (APPUtil.isWin()) {
			commandPath = "D:\\springboot\\webapp-script\\python\\";
			exe = "python";
			encoding = "gbk";
		}
		cmdArr[0] = exe;
		cmdArr[1] = commandPath + commandName;
		for (int i = 0; i < params.length; i++) {
			cmdArr[2 + i] = params[i];
		}
		Runtime.getRuntime().exec(cmdArr);
	}

	public static void main(String[] args) throws ScriptException {
		System.out.println(PythonProcesser.commandPath);
		System.out.println(System.getProperty("user.dir"));
		System.err.println(PythonProcesser.class.getResource("/").getPath());
		System.out.println(System.getProperty("java.class.path"));
		System.out.println(Charset.defaultCharset());
		System.out.println(System.getProperty("file.encoding"));
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
