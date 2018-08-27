package com.cn.webapp.service;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;

public interface BaseService {
	/**
	 * 能否请求
	 * 
	 * @param request
	 * @return
	 */
	boolean canService(Request request);

	/**
	 * 处理请求
	 * 
	 * @param request
	 * @return
	 */
	Response<?> service(Request request);

	/**
	 * 用于service层级跳转
	 */
	Response<?> jumpService(Object service, String method, Request request);
}