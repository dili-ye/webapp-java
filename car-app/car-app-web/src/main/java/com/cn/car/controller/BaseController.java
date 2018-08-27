package com.cn.car.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cn.car.service.BaseService;
import com.cn.commons.dto.Request;
import com.cn.commons.dto.Response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 * 公共controller
 * 
 * @author wb-tjf399322
 *
 */
@Controller
public class BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	BaseService[] services;

	@RequestMapping(value = "/{page}")
	public String page(@PathVariable String page, HttpServletRequest request) {
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
		request.setAttribute("basePath", basePath);
		return page;
	}

	/**
	 * 示例： 如何执行
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/default-execute")
	public Response<?> execute(HttpServletRequest request) {
		return service(Request.of(request));
	}

	protected Response<?> service(Request request) {
		String requestData = JSON.toJSONString(request);
		logger.info("request:{}", requestData);
		for (BaseService service : services) {
			if (service.canService(request)) {
				try {
					return service.service(request);
				} catch (Exception e) {
					logger.info("service execute error, cause exception is :{}", JSON.toJSONString(e.getMessage()));
					throw new RuntimeException(
							"service execute error, cause exception is :" + JSON.toJSONString(e.getMessage()));
				}
			}
		}
		logger.info("can't execute, cause request is : " + requestData);
		throw new RuntimeException("can't execute, cause request is : " + requestData);
	}

	/**
	 * 500统一异常处理
	 * 
	 * @param exception
	 *            exception
	 * @return
	 */
	@ExceptionHandler({ RuntimeException.class })
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView processException(RuntimeException exception) {
		logger.info("自定义异常处理-RuntimeException");
		ModelAndView m = new ModelAndView();
		m.addObject("error", exception);
		m.setViewName("/error/500");
		return m;
	}

	/**
	 * 404统一异常处理
	 * 
	 * @param exception
	 *            exception
	 * @return
	 */
	@ExceptionHandler({ Exception.class })
	@ResponseStatus(HttpStatus.OK)
	public ModelAndView processException(Exception exception) {
		logger.info("自定义异常处理-Exception");
		ModelAndView m = new ModelAndView();
		m.addObject("error", "page not found!!!");
		m.setViewName("/error/404");
		return m;
	}
}
