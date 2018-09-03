package com.cn.webapp.service.impl;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.webapp.commons.annotation.AppService;
import com.cn.webapp.service.FileService;
import com.google.common.collect.Maps;

@AppService(id = 0, name = "default")
public class FileServiceImpl implements FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	protected Map<String, Method> methods = Maps.newHashMap();

	@Override
	public boolean canService(String actionType, HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	@Override
	public void service(String actionType, HttpServletRequest request, HttpServletResponse response) {
		// TODO
	}
}
