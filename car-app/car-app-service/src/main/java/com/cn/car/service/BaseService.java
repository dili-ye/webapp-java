package com.cn.car.service;

import com.cn.commons.dto.Request;
import com.cn.commons.dto.Response;

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
	Response<?> jumpService(Class<BaseService> serviceClass, String method, Request request);
}
