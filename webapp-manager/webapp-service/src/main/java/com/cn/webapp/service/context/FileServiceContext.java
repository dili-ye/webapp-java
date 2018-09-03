package com.cn.webapp.service.context;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cn.webapp.commons.annotation.AppService;
import com.cn.webapp.commons.context.CommonsContext;
import com.cn.webapp.service.FileService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Component(value = "fileServiceContext")
@Order(50)
public class FileServiceContext extends CommonsContext<FileService> {

	private static final Logger logger = LoggerFactory.getLogger(FileServiceContext.class);

	private Map<Long, FileService> idServices = Maps.newHashMap();

	private Map<String, FileService> nameServices = Maps.newHashMap();

	private Map<Class<? extends FileService>, FileService> clazzServices = Maps.newHashMap();

	@PostConstruct
	public void init() {
		super.init();
		beanMap = beanMap.entrySet().stream().filter(bean -> {
			FileService service = bean.getValue();
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
		logger.info("fileServiceContext init end");
	}

	public FileService getBean(Object beanType) {
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

	public Collection<FileService> getBeans(Object[] beanTypes) {
		if (beanTypes == null || beanTypes.length == 0) {
			return beanMap.values();
		}
		Set<FileService> services = Sets.newHashSet();
		for (Object beanType : beanTypes) {
			if (containsBean(beanType)) {
				services.add(getBean(beanType));
			}
		}
		return services;
	}

}
