package com.cn.webapp.proxy;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;

public interface BaiduTiebaProxy {
	Response<?> findOneTitleMsg(Request request);
}
