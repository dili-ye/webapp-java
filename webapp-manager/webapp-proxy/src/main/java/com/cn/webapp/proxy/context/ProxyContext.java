package com.cn.webapp.proxy.context;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.cn.webapp.commons.annotation.ReserveProxy;
import com.cn.webapp.commons.context.CommonsContext;
import com.cn.webapp.commons.loader.WebClassLoader;
import com.cn.webapp.proxy.Proxy;

@Component("proxyContext")
public class ProxyContext extends CommonsContext<Proxy> {
	private static final String proxyPackage = "com.cn.webapp.proxy";

	@PostConstruct
	public void init() {
		super.init();
		initResolve();
	}

	// 类加载器加载
	void initResolve() {
		String path = this.getClass().getResource("").getPath();
		String name = this.getClass().getName();
		String simpleName = this.getClass().getSimpleName();
		name = name.replaceAll("\\.", "/").replace("/"+simpleName, "");
		path = path.replace(name, "");
		File f = new File(path);
		Map<String, Object> proxys = WebClassLoader.createObjectsByLocation(f);
		for (Iterator<Entry<String, Object>> it = proxys.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			try {
				Object val = e.getValue();
				if (val instanceof Proxy && val.getClass().getAnnotation(ReserveProxy.class) != null) {
					this.beanMap.put(e.getKey(), Proxy.class.cast(val));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public Proxy getBean(Object beanType) {
		if (beanType instanceof Class) {
			Class<?> clazz = Class.class.cast(beanType);
			Iterator<Proxy> iterator = beanMap.values().iterator();
			while (iterator.hasNext()) {
				Proxy proxy = iterator.next();
				if (clazz.isAssignableFrom(proxy.getClass())) {
					return proxy;
				}
			}
		}
		return null;
	}
}
