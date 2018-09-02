package com.cn.webapp.proxy.local;

import java.util.List;

import com.cn.webapp.commons.annotation.ReserveProxy;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.proxy.MusicProxy;

@ReserveProxy
public class MusicProxyImpl implements MusicProxy{

	@Override
	public Response<List<String>> search(Request req) {
		return null;
	}

	@Override
	public Response<String> download(Request req) {
		return null;
	}

}
