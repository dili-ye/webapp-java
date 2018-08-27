package com.cn.car.service.context;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cn.car.service.BaseService;
import com.cn.car.service.FileService;
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
	BaseServiceContext serviceContext;

	@Resource
	FileServiceContext fileServiceContext;

	@SuppressWarnings("unchecked")
	public Response<?> jump(Class<BaseService> service, String methodName, Request request) {
		long startTime = System.currentTimeMillis();
		BaseService bean = serviceContext.getBean(service);
		try {
			Field field = bean.getClass().getDeclaredField(METHODS_NAME);
			Map<String, Method> methods = Map.class.cast(field.get(bean));
			if (methods.containsKey(methodName)) {
				try {
					Response<?> response = (Response<?>) methods.get(methodName).invoke(bean, request);
					response.setTime(System.currentTimeMillis() - startTime);
					logger.info("jump response:{}", JSON.toJSONString(response));
					return response;
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
	
	/**
	 * 用于fileService的跳转
	 * @param service
	 * @param actionType
	 * @param request
	 * @param response
	 */
	public void jump(Object serviceName, String actionType, HttpServletRequest request,
			HttpServletResponse response) {
		FileService bean = fileServiceContext.getBean(serviceName);
		bean.service(actionType, request, response);
	}
}
