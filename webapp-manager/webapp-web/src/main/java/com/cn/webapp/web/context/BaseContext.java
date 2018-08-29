package com.cn.webapp.web.context;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("baseContext")
public class BaseContext implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	public Object getBootBean() {
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
		if (map != null) {
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			if (iterator.hasNext()) {
				return iterator.next().getValue();
			}
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
