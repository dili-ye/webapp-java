package com.cn.webapp.service.impl;

import com.cn.webapp.commons.annotation.AppService;
import com.cn.webapp.commons.annotation.HandlerMethod;
import com.cn.webapp.commons.annotation.Reserved;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.proxy.SpiderProxy;

@AppService(id = 2, name = "spiderService")
public class SpiderServiveImpl extends BaseServiceImpl {

	@Reserved
	SpiderProxy spiderProxy;

	@HandlerMethod("findOneTitleMsg")
	public Response<?> findOneTitleMsg(Request request) {
		return spiderProxy.findOneTitleMsg(request);
	}

	@HandlerMethod("findFemaleBras")
	public Response<?> findFemaleBras(Request request) {
		return spiderProxy.findFemaleBras(request);
	}
}
