package com.cn.webapp.web.init;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cn.webapp.commons.annotation.ExecuteService;
import com.cn.webapp.service.BaseService;
import com.cn.webapp.service.context.ServiceContext;
import com.cn.webapp.web.context.ControllerContext;
import com.cn.webapp.web.controller.BaseController;
import com.google.common.collect.Sets;

@Component
@Order(11) // after service init end
public class WebappControllerRunner implements ApplicationRunner {

	private static final String SERVICES_NAME = "services";

	private static final Logger logger = LoggerFactory.getLogger(WebappControllerRunner.class);

	@Resource
	ControllerContext controllerContext;

	@Resource
	ServiceContext serviceContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(JSON.toJSONString(args));
		}
		initBaseController();
	}

	/**
	 * init fileController
	 */
	protected void initFileController() {

	}

	/**
	 * 初始化Controller中的service
	 */
	protected void initBaseController() {
		Collection<BaseController> beans = controllerContext.getBeans();
		for (BaseController controller : beans) {
			Class<?> clazz = controller.getClass();
			ExecuteService controllerExecutors = clazz.getAnnotation(ExecuteService.class);
			Set<Object> serviceTypes = generateServiceTypes(controllerExecutors);
			for (; clazz != null; clazz = clazz.getSuperclass()) {
				try {
					Field declaredField = clazz.getDeclaredField(SERVICES_NAME);
					// 添加field上的注解的service
					serviceTypes.addAll(generateServiceTypes(declaredField.getAnnotation(ExecuteService.class)));
					// 找不到执行的service就继续遍历
					if (serviceTypes.size() == 0) {
						continue;
					}
					declaredField.setAccessible(true);
					declaredField.set(controller,
							serviceContext.getBeans(serviceTypes.toArray()).toArray(new BaseService[] {}));
					declaredField.setAccessible(false);
					logger.info("service init success, class is :{} , services size :{}",
							controller.getClass().getName(), serviceTypes.size());
					break;
				} catch (Exception e) {
					logger.info("service init error, exception :{}", JSON.toJSONString(e));
				}
			}
		}
	}

	private Set<Object> generateServiceTypes(ExecuteService executors) {
		Set<Object> serviceTypes = Sets.newHashSet();
		if (executors == null) {
			return serviceTypes;
		}
		long[] ids = executors.execiteIds();
		Class<?>[] classes = executors.executeClasses();
		String[] names = executors.executeNames();
		if (ids != null && ids.length > 0) {
			for (long id : ids) {
				serviceTypes.add(id);
			}
		}
		if (names != null && names.length > 0) {
			for (String name : names) {
				serviceTypes.add(name);
			}
		}
		if (classes != null && classes.length > 0) {
			for (Class<?> c : classes) {
				serviceTypes.add(c);
			}
		}
		return serviceTypes;
	}
}
