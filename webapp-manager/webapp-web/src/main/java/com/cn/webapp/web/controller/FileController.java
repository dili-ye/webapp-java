package com.cn.webapp.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.webapp.service.context.ServiceJumper;

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

	@RequestMapping(value = "/upload/{serviceType}/{actionType}", method = { RequestMethod.POST })
	public void uploadFile(@PathVariable String serviceType, @PathVariable String actionType,
			HttpServletRequest request, HttpServletResponse response) {
		jumper.jump(serviceType, actionType, request, response);
		logger.info("upload end");
	}

	@RequestMapping(value = "/download/{serviceType}/{actionType}", method = { RequestMethod.POST, RequestMethod.GET })
	public void downloadFile(@PathVariable String serviceType, @PathVariable String actionType,
			HttpServletRequest request, HttpServletResponse response) {
		jumper.jump(serviceType, actionType, request, response);
		logger.info("download end");
	}
}
