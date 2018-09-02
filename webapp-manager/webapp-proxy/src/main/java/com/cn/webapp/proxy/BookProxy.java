package com.cn.webapp.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.commons.exception.ScriptException;

@FeignClient("app-service")
@RequestMapping("/book-server")
public interface BookProxy extends Proxy{
	
	@RequestMapping("/search")
	Response<?> search(Request request) throws ScriptException;
}
