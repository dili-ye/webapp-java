package com.cn.car.service.impl;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.car.service.FileService;
import com.google.common.collect.Maps;

public class FileServiceImpl implements FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	protected Map<String, Method> methods = Maps.newHashMap();

	@Override
	public boolean canService(String actionType, HttpServletRequest request, HttpServletResponse response) {
		return methods.containsKey(actionType);
	}

	@Override
	public void service(String actionType, HttpServletRequest request, HttpServletResponse response) {
		Method method = methods.get(actionType);
		try {
			method.invoke(this, actionType, request, response);
		} catch (Exception e) {
			logger.error("service error !!! actionType is:{}", actionType);
		}
	}
}
