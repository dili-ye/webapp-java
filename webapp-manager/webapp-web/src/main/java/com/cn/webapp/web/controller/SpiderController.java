package com.cn.webapp.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.webapp.commons.annotation.ExecuteService;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.service.impl.SpiderServiveImpl;

@RequestMapping("/spider")
@Controller
@ExecuteService(executeClasses = SpiderServiveImpl.class)
public class SpiderController extends BaseController {

	@RequestMapping(value = "/{serviceName}", method = { RequestMethod.POST })
	@ResponseBody
	public Response<?> spider(@PathVariable String serviceName, HttpServletRequest request) {
		Request req = Request.of(serviceName, request);
		return service(req);
	}

	@RequestMapping(value = "/{page}", method = { RequestMethod.GET })
	public String page(@PathVariable String page, HttpServletRequest request) {
		request.setAttribute("service", "spider");
		page = "spider/" + page;
		return super.page(page, request);
	}
}
