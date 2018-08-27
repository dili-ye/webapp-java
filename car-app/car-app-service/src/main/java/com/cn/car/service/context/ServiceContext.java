package com.cn.car.service.context;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cn.car.service.BaseService;
import com.cn.commons.annotation.AppService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Component(value = "serviceContext")
public class ServiceContext implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(ServiceContext.class);

	private ApplicationContext applicationContext;

	private Map<String, BaseService> beans = Maps.newHashMap();

	private Map<Long, BaseService> idServices = Maps.newHashMap();

	private Map<String, BaseService> nameServices = Maps.newHashMap();

	private Map<Class<? extends BaseService>, BaseService> clazzServices = Maps.newHashMap();

	@PostConstruct
	public void init() {
		beans = applicationContext.getBeansOfType(BaseService.class, false, true);
		beans = beans.entrySet().stream().filter(bean -> {
			BaseService service = bean.getValue();
			AppService component = service.getClass().getAnnotation(AppService.class);
			if (component != null && component.enabled()) {
				idServices.put(component.id(), service);
				nameServices.put(component.name(), service);
				clazzServices.put(service.getClass(), service);
				return true;
			}
			logger.info("service :{} enabled is false", service.getClass().getName());
			return false;
		}).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}

	public BaseService getBean(Object beanType) {
		if (beanType instanceof Long) {
			return idServices.get(beanType);
		} else if (beanType instanceof String) {
			return nameServices.get(beanType);
		} else if (beanType instanceof Class) {
			return clazzServices.get(beanType);
		}
		return null;
	}

	public boolean containsBean(Object beanType) {
		if (beanType instanceof Long) {
			return idServices.containsKey(beanType);
		} else if (beanType instanceof String) {
			return nameServices.containsKey(beanType);
		} else if (beanType instanceof Class) {
			return clazzServices.containsKey(beanType);
		}
		return false;
	}

	public Collection<BaseService> getBeans(Object[] beanTypes) {
		if (beanTypes == null || beanTypes.length == 0) {
			return beans.values();
		}
		Set<BaseService> services = Sets.newHashSet();
		for (Object beanType : beanTypes) {
			if (containsBean(beanType)) {
				services.add(getBean(beanType));
			}
		}
		return services;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
