package com.cn.webapp.proxy;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;

@FeignClient("app-service")
@RequestMapping("/random-server")
public interface RandomProxy extends Proxy {

	@RequestMapping("/create")
	public Response<?> create(Request request);

}
