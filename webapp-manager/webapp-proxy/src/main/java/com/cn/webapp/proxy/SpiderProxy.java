package com.cn.webapp.proxy;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;

public interface SpiderProxy extends Proxy {
	Response<?> baiduTieba(Request request);

	Response<?> findFemaleBras(Request request);
}
