package com.cn.car.service.context;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cn.car.service.BaseService;
import com.cn.commons.dto.Request;
import com.cn.commons.dto.Response;
import static com.cn.car.service.init.CarAppServiceRunner.METHODS_NAME;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Order(100) // 尽量后期加载
public class ServiceJumper {

	private static final Logger logger = LoggerFactory.getLogger(ServiceJumper.class);

	@Resource
	ServiceContext serviceContext;

	@SuppressWarnings("unchecked")
	public Response<?> jump(Class<BaseService> service, String methodName, Request request) {
		long startTime = System.currentTimeMillis();
		BaseService bean = serviceContext.getBean(service);
		try {
			Field field = bean.getClass().getDeclaredField(METHODS_NAME);
			Map<String, Method> methods = Map.class.cast(field.get(bean));
			if (methods.containsKey(methodName)) {
				try {
					Object invoke = methods.get(methodName).invoke(bean, request);
					logger.info("jump response:{}", JSON.toJSONString(invoke));
					return (Response<?>) invoke;
				} catch (Exception e) {
					logger.info("jump error:{}", JSON.toJSONString(e));
				}
			}
			logger.info("can't execute method, cause there is no method name:{} in methods", methodName);
		} catch (Exception e) {
			logger.info("can't jump service, cause cannot get field:{}", METHODS_NAME);
		}
		return Response.of(199, System.currentTimeMillis() - startTime);
	}
}
