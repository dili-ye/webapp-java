package com.cn.webapp.proxy.local;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.commons.pub.util.PythonProcesser;
import com.cn.webapp.commons.pub.util.StringUtils;
import com.cn.webapp.proxy.TiebaProxy;

public class TiebaProxyImpl implements TiebaProxy {

	private static final Logger logger = LoggerFactory.getLogger(TiebaProxyImpl.class);

	private static final String USER_KEY = "user";
	private static final String TITLE_KEY = "title";
	private static final String commandName = "tieba.py";

	@Override
	public Response<?> delete(Request request) {
		logger.info("request data:{}", JSON.toJSONString(request));
		Map<String, String> context = request.getContext();
		String user = context.getOrDefault(USER_KEY, "").trim();
		String title = context.getOrDefault(TITLE_KEY, "").trim();
		long startTime = System.currentTimeMillis();
		final String actionType = "-d";
		try {
			if (!StringUtils.isEmpty(user)) {
				PythonProcesser.executeWithoutResult(commandName, actionType, "-d", "-u", user);
			}
			if (!StringUtils.isEmpty(title)) {
				PythonProcesser.executeWithoutResult(commandName, actionType, "-d", "-t", title);
			}
			return Response.of(200, System.currentTimeMillis() - startTime);
		} catch (IOException e) {
			logger.info("script execute error!!! \n exception:{}", JSON.toJSONString(e));
		}
		return Response.of(199, System.currentTimeMillis() - startTime);
	}
}
