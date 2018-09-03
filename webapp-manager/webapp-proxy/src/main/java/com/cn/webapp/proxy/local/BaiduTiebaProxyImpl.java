package com.cn.webapp.proxy.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.commons.socket.SocketClient;
import com.cn.webapp.proxy.BaiduTiebaProxy;

public class BaiduTiebaProxyImpl implements BaiduTiebaProxy {

	private static final Logger logger = LoggerFactory.getLogger(BaiduTiebaProxyImpl.class);

	private static String IP = "localhost";
	private static int PORT = 8888;

	@Override
	public Response<?> findOneTitleMsg(Request request) {
		SocketClient client = new SocketClient(IP, PORT);
		String url = request.getContext().get("url");
		logger.info("start request data:{}", url);
		if (url != null) {
			String msg = client.sendMsg(url);
			return Response.of(msg);
		}
		return Response.of(199);
	}

}
