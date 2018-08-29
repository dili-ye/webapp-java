package com.cn.webapp.commons.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cn.webapp.commons.pub.util.FileUtil;
import com.google.common.collect.Maps;

public class WebClassLoader extends ClassLoader {

	private static final WebClassLoader loader = new WebClassLoader();

	public static Class<?> findClassByNameAndLocation(String name) throws ClassNotFoundException {
		byte[] datas = loader.loadClassData(name); // 将class文件的数据读入到byte数组中
		name = loader.convert(name);
		if (name.endsWith("Impl")) {
			return loader.defineClass(name, datas, 0, datas.length);// 通过byte数组加载Class对象
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param pack
	 *            类所在的包名 eg:com.szelink.test. 后面需要带个"."
	 * @param location
	 *            包所在的路径
	 * @return
	 */
	public static Map<String, Object> createObjectsByLocation(File location) {
		// 列出所有的.class文件
		List<File> lists = FileUtil.parseDir(location);
		Map<String, Object> map = Maps.newHashMap();
		if (lists.size() == 0) {
			return map;
		}
		for (File f : lists) {
			String name = f.getName();
			name = name.substring(0, f.getName().indexOf(".class")).replace(name.charAt(0) + "",
					new String(name.charAt(0) + "").toLowerCase());
			Class<?> c = null;
			try {
				c = Class.forName(loader.convert(f.getAbsolutePath()));
				if (!c.isInterface()) {
					map.put(name, c.newInstance());
				}
			} catch (Exception e) {
			}
		}
		return map;
	}

	protected byte[] loadClassData(String classFile) {
		return loadClassData(new File(classFile));
	}

	protected byte[] loadClassData(File classFile) {
		byte[] datas = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(classFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int b;
			while ((b = fis.read()) != -1) {
				bos.write(b);
			}
			datas = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return datas;
	}

	protected byte[] loadClassData(String name, File location) {
		return loadClassData(new File(location, name));
	}

	private String convert(String pack) {
		String path = new File(this.getClass().getClassLoader().getResource("").getPath()).getAbsolutePath();
		String result = pack.replace(path + File.separator, "");
		result = result.replace('/', '.');
		result = result.replace('\"', '.');// 将所有的\转成.
		result = result.replace('\\', '.');
		result = result.replace(".class", "");
		return result;
	}
}
