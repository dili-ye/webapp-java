package com.cn.webapp.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.webapp.commons.annotation.ExecuteService;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.service.impl.SpiderServiveImpl;

@RequestMapping("/spider")
@Controller
@ExecuteService(executeClasses = SpiderServiveImpl.class)
public class SpiderController extends BaseController {

	@RequestMapping(value = "/{actionName}", method = { RequestMethod.POST })
	public Response<?> findOneTitleMsg(@PathVariable String actionName, HttpServletRequest request) {
		Request req = Request.of(actionName, request);
		return service(req);
	}

	@RequestMapping(value = "/{service}", method = { RequestMethod.GET })
	public String page(@PathVariable String service, HttpServletRequest request) {
		request.setAttribute("service", service);
		service = "spider/" + service;
		return super.page(service, request);
	}
}
