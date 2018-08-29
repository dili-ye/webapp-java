package com.cn.webapp.service.context;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.service.BaseService;
import com.cn.webapp.service.FileService;

import static com.cn.webapp.service.init.WebappServiceRunner.METHODS_NAME;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Order(100) // 尽量后期加载
/**
 * 服务层跳转
 * 
 * @author wb-tjf399322
 *
 */
public class ServiceJumper {

	private static final Logger logger = LoggerFactory.getLogger(ServiceJumper.class);

	@Resource
	ServiceContext serviceContext;

	@Resource
	FileServiceContext fileServiceContext;

	/**
	 * 服务层跳转。
	 * 
	 * @param service： id、name、serviceClass
	 * @param methodName： handlerMethod。value()
	 * @param request： data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response<?> jump(Object service, String methodName, Request request) {
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
	 * @param          serviceType：
	 *                 因为取不到class类型的值，所以这里就按照serviceName去获取service，值为String类型
	 * @param          actionType：被handlerMethod修饰的方法名
	 * @param request
	 * @param response
	 */
	public void jump(String serviceName, String actionType, HttpServletRequest request, HttpServletResponse response) {
		FileService bean = fileServiceContext.getBean(serviceName);
		bean.service(actionType, request, response);
	}
}
