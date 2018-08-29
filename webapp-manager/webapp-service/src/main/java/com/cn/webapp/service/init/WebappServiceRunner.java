package com.cn.webapp.service.init;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cn.webapp.commons.annotation.HandlerMethod;
import com.cn.webapp.service.BaseService;
import com.cn.webapp.service.FileService;
import com.cn.webapp.service.context.ServiceContext;
import com.cn.webapp.service.context.FileServiceContext;
import com.google.common.collect.Maps;

@Component
@Order(10)
public class WebappServiceRunner implements ApplicationRunner {
	/**
	 * 初始化service中的methods
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebappServiceRunner.class);

	private static final String MAPPING_SEPARATOR = ".";
	public static final String METHODS_NAME = "methods";
	private static final String BASE_METHOD_CANSERVICE = "canService";
	private static final String BASE_METHOD_SERVICE = "service";

	@Resource
	ServiceContext serviceContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(JSON.toJSONString(args));
		}
		initBaseService();
		initFileService();
	}

	protected void initBaseService() {
		Collection<BaseService> beans = serviceContext.getBeans(null);
		for (BaseService service : beans) {
			assignMethods(service);
		}
	}

	@Resource
	FileServiceContext fileServiceContext;

	protected void initFileService() {
		Collection<FileService> beans = fileServiceContext.getBeans(null);
		for (FileService service : beans) {
			assignMethods(service);
		}
	}

	/**
	 * 给service对象赋值
	 * 
	 * @param service
	 */
	private void assignMethods(Object service) {
		Class<?> clazz = service.getClass();
		String suffix = null;
		Predicate<Method> p = m -> m.getAnnotation(Override.class) != null
				|| m.getAnnotation(HandlerMethod.class) != null && !BASE_METHOD_CANSERVICE.equals(m.getName())
						&& !BASE_METHOD_SERVICE.equals(m.getName());
		Map<String, Method> methodMap = Maps.newHashMap();
		for (; clazz != null; clazz = clazz.getSuperclass()) {
			Method[] ms = clazz.getDeclaredMethods();
			HandlerMethod mapping = clazz.getAnnotation(HandlerMethod.class);
			if (mapping != null && !"".equals(mapping.value())) {
				suffix = mapping.value();
			}
			if (ms != null && ms.length > 0) {
				List<Method> list = Arrays.asList(ms).stream().filter(p).collect(Collectors.toList());
				for (Method m : list) {
					HandlerMethod annotation = m.getAnnotation(HandlerMethod.class);
					if (annotation != null) {
						String key = suffix == null ? annotation.value()
								: suffix + MAPPING_SEPARATOR + annotation.value();
						methodMap.put(key, m);
					}
				}
			}
		}

		for (clazz = service.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
			try {
				Field declaredField = clazz.getDeclaredField(METHODS_NAME);
				declaredField.setAccessible(true);
				declaredField.set(service, methodMap);
				declaredField.setAccessible(false);
				logger.info("methods init success, class is :{} , methods size :{}", service.getClass().getName(),
						methodMap.size());
				break;
			} catch (Exception e) {
				logger.info("methods init error, class is :{}", service.getClass().getName());
			}
		}

	}
}
