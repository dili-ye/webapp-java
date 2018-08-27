package com.cn.car.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cn.car.service.FileService;
import com.cn.car.service.context.ServiceJumper;

@Service
public class FileServiceImpl implements FileService {
	
	@Resource
	ServiceJumper jumper;
	
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public boolean canService(String actionType, HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	@Override
	public void service(String actionType, HttpServletRequest request, HttpServletResponse response) {
		String serviceName = request.getParameter("serviceName");
		jumper.jump(serviceName, actionType, request, response);
	}
}
