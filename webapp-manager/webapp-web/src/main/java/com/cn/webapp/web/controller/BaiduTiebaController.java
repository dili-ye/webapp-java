package com.cn.webapp.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.webapp.commons.annotation.ExecuteService;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.service.impl.BaiduTiebaSpiderImpl;

@RequestMapping("/tieba")
@RestController
@ExecuteService(executeClasses = BaiduTiebaSpiderImpl.class)
public class BaiduTiebaController extends BaseController {

	@RequestMapping("/{actionName}")
	public Response<?> findOneTitleMsg(@PathVariable String actionName, HttpServletRequest request) {
		Request req = Request.of(actionName, request);
		return service(req);
	}
}
