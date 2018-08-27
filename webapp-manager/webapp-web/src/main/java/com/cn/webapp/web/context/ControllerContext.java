package com.cn.webapp.web.context;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cn.webapp.web.controller.BaseController;
import com.google.common.collect.Maps;

@Component(value = "controllerContext")
public class ControllerContext implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(ControllerContext.class);
	private ApplicationContext applicationContext;
	private Map<String, BaseController> beans = Maps.newHashMap();

	@PostConstruct
	public void init() {
		beans = applicationContext.getBeansOfType(BaseController.class, false, true);
		if (beans == null) {
			beans = Maps.newHashMap();
		}
		logger.info("controller init end");
	}

	public BaseController getBean(String beanId) {
		return beans.get(beanId);
	}

	public boolean containsBean(String beanId) {
		return beans.containsKey(beanId);
	}

	public Collection<BaseController> getBeans() {
		return beans.values();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
