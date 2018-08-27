package com.cn.car.web.init;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cn.car.service.BaseService;
import com.cn.car.service.context.ServiceContext;
import com.cn.car.web.context.ControllerContext;
import com.cn.car.web.controller.BaseController;
import com.cn.commons.annotation.ExecuteService;
import com.google.common.collect.Lists;

@Component
@Order(11) // after service init end
public class CarAppControllerRunner implements ApplicationRunner {

	private static final String SERVICES_NAME = "services";

	private static final Logger logger = LoggerFactory.getLogger(CarAppControllerRunner.class);

	@Resource
	ControllerContext controllerContext;

	@Resource
	ServiceContext serviceContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug(JSON.toJSONString(args));
		}
		initController();
	}

	/**
	 * 初始化Controller中的service
	 */
	protected void initController() {
		Collection<BaseController> beans = controllerContext.getBeans();
		for (BaseController controller : beans) {
			Class<?> clazz = controller.getClass();
			List<Object> serviceTypes = Lists.newArrayList();
			ExecuteService annotation = clazz.getAnnotation(ExecuteService.class);
			if (annotation != null) {
				long[] ids = annotation.execiteIds();
				Class<?>[] classes = annotation.executeClasses();
				String[] names = annotation.executeNames();
				if (ids != null && ids.length > 0) {
					for (long id : ids) {
						serviceTypes.add(id);
					}
				} else if (names != null && names.length > 0) {
					for (String name : names) {
						serviceTypes.add(name);
					}
				} else if (classes != null && classes.length > 0) {
					for (Class<?> c : classes) {
						serviceTypes.add(c);
					}
				}
			}
			if (serviceTypes.size() == 0) {
				continue;
			}
			for (; clazz != null; clazz = clazz.getSuperclass()) {
				try {
					Field declaredField = clazz.getDeclaredField(SERVICES_NAME);
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
}
