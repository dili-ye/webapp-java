package com.cn.webapp.service.impl;

import com.cn.webapp.commons.annotation.HandlerMethod;
import com.cn.webapp.commons.annotation.Reserved;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.proxy.BaiduTiebaProxy;

public class BaiduTiebaSpiderImpl extends BaseServiceImpl {

	@Reserved
	BaiduTiebaProxy baiduTiebaProxy;

	@HandlerMethod("findOneTitleMsg")
	public Response<?> findOneTitleMsg(Request request) {
		return baiduTiebaProxy.findOneTitleMsg(request);
	}
}
