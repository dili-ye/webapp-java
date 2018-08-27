package com.cn.webapp.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cn.webapp.commons.annotation.ExecuteService;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.service.BaseService;
import com.cn.webapp.service.impl.BaseServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 * 公共controller
 * 
 * @author wb-tjf399322
 *
 */
@Controller
@ExecuteService(executeClasses = BaseServiceImpl.class)
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
	@RequestMapping(value = "/default-execute", method = { RequestMethod.GET })
	@ResponseBody
	public Response<?> execute(HttpServletRequest request) {
		return service(Request.of("default", request));
	}

	protected Response<?> service(Request request) {
		String requestData = JSON.toJSONString(request);
		logger.info("request:{}", requestData);
		if (services == null || services.length == 0) {
			logger.info("no service in the controller:{}", this.getClass().getName());
			throw new RuntimeException("no service error!!!");
		}
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
	 * @param exception exception
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
	 * @param exception exception
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
