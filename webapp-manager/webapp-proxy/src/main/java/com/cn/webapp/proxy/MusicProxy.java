package com.cn.webapp.proxy;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;

@FeignClient("app-service")
@RequestMapping("/random-server")
public interface MusicProxy extends Proxy{
	
	@RequestMapping(method = RequestMethod.POST, value = "/search")
	public Response<List<String>> search(Request req);
	
	@RequestMapping(method = RequestMethod.POST ,value ="/download")
	public Response<String> download(Request req);
}
