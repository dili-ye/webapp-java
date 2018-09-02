package com.cn.webapp.proxy.context;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cn.webapp.commons.annotation.ReserveProxy;
import com.cn.webapp.commons.loader.WebClassLoader;
import com.cn.webapp.proxy.Proxy;
import com.google.common.collect.Maps;

@Component("proxyContext")
public class ProxyContext implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	private static final String proxyPackage = "com.webapp.web.proxy";
	private Map<String, Proxy> proxys = Maps.newHashMap();

	@PostConstruct
	void init() {
		proxys = applicationContext.getBeansOfType(Proxy.class, false, true);
		initResolve();
	}

	// 类加载器加载
	void initResolve() {
		String path = this.getClass().getClassLoader().getResource("").getPath();
		File f = new File(path + File.separator + proxyPackage.replaceAll("\\.", "/"));
		Map<String, Object> proxys = WebClassLoader.createObjectsByLocation(f);
		for (Iterator<Entry<String, Object>> it = proxys.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			try {
				Object val = e.getValue();
				if (val instanceof Proxy && val.getClass().getAnnotation(ReserveProxy.class) != null) {
					this.proxys.put(e.getKey(), Proxy.class.cast(val));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Map<String, Proxy> getBeans() {
		return proxys;
	}

	public Proxy getBean(Class<?> clazz) {
		Iterator<Proxy> iterator = proxys.values().iterator();
		while(iterator.hasNext()) {
			Proxy proxy = iterator.next();
			if (clazz.isAssignableFrom(proxy.getClass())) {
				return proxy;
			}
		}
		return null;
	}
}
