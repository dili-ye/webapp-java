package com.cn.webapp.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;

@FeignClient("app-server")
@RequestMapping("/tieba-server")
public interface TiebaProxy extends Proxy {
	
	@RequestMapping("/delete")
	Response<?> delete(Request request);
}
