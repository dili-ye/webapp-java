package com.cn.car.service.impl;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cn.car.service.BaseService;
import com.cn.car.service.context.ServiceJumper;
import com.cn.commons.constants.ServerConstants;
import com.cn.commons.dto.Request;
import com.cn.commons.dto.Response;
import com.google.common.collect.Maps;

@Component
public class BaseServiceImpl implements BaseService {

	private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

	protected Map<String, Method> methods = Maps.newHashMap();
	
	@Resource
	ServiceJumper jumper;

	@Override
	public boolean canService(Request request) {
		logger.info("request:{}", JSON.toJSONString(request));
		Map<String, String> data = request.getContext();
		if (data != null && data.size() > 0) {
			return methods.containsKey(data.get(ServerConstants.ACTION_TYPE));
		}
		return false;
	}

	@Override
	public Response<?> service(Request request) {
		String actionType = request.getContext().get(ServerConstants.ACTION_TYPE);
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
