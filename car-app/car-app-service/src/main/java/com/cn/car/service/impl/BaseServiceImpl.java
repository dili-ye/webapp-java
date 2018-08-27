package com.cn.car.service.impl;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cn.car.service.BaseService;
import com.cn.car.service.context.ServiceJumper;
import com.cn.commons.annotation.AppService;
import com.cn.commons.annotation.HandlerMethod;
import com.cn.commons.dto.Request;
import com.cn.commons.dto.Response;
import com.google.common.collect.Maps;

@AppService(id = 0, name = "baseService")
public class BaseServiceImpl implements BaseService {

	private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	protected Map<String, Method> methods = Maps.newHashMap();

	@Resource
	ServiceJumper jumper;
	
	/**
	 * 示例
	 * @param request
	 * @return
	 */
	@HandlerMethod("default")
	public Response<?> execute(Request request) {
		return Response.of(request);
	}

	@Override
	public boolean canService(Request request) {
		logger.info("request:{}", JSON.toJSONString(request));
		return methods.containsKey(request.getActionType());
	}

	@Override
	public Response<?> service(Request request) {
		String actionType = request.getActionType();
		Method method = methods.get(actionType);
		try {
			Object invoke = method.invoke(this, request);
			return (Response<?>) invoke;
		} catch (Exception e) {
			Response<Object> response = Response.builder().exception(e).status(199).build();
			return response;
		}
	}

	@Override
	public Response<?> jumpService(Class<BaseService> serviceClass, String method, Request request) {
		return jumper.jump(serviceClass, method, request);
	}
}
