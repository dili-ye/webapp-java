package com.cn.car.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.car.service.context.ServiceJumper;

/**
 * 用于文件上传下载的controller
 * 
 * @author wb-tjf399322
 *
 */
@Controller("/file")
public class FileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Resource
	ServiceJumper jumper;
	
	/**
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/upload/{serviceType}/{actionType}", method = { RequestMethod.POST })
	public void uploadFile(@PathVariable Object serviceType ,@PathVariable String actionType, HttpServletRequest request, HttpServletResponse response) {
		jumper.jump(serviceType, actionType, request, response);
		logger.info("service end");
	}
	/**
	 * 
	 * @param actionType
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/download/{serviceType}/{actionType}", method = { RequestMethod.POST ,RequestMethod.GET})
	public void downloadFile(@PathVariable Object serviceType ,@PathVariable String actionType, HttpServletRequest request,
			HttpServletResponse response) {
		
	}
}
