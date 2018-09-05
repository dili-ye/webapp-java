package com.cn.webapp.proxy.local;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.webapp.commons.annotation.ReserveProxy;
import com.cn.webapp.commons.dto.Request;
import com.cn.webapp.commons.dto.Response;
import com.cn.webapp.commons.socket.SocketClient;
import com.cn.webapp.proxy.SpiderProxy;

@ReserveProxy
public class SpiderProxyImpl implements SpiderProxy {

	private static final Logger logger = LoggerFactory.getLogger(SpiderProxyImpl.class);

	private static String IP = "localhost";
	private static int PORT = 8888;
	private static final String ACTION_TYPE = "actionType";
	private static final String STATUS_CODE = "status";
	private static final String MESSAGE_CODE = "message";
	private static final String SUCCESS_CODE = "success";
	private static final String ERROR_CODE = "error";

	private Response<?> sendData(Request request) {
		Map<String, String> context = request.getContext();
		if (context.size() != 0) {
			SocketClient client = new SocketClient(IP, PORT);
			context.put(ACTION_TYPE, request.getActionName());
			String data = JSON.toJSONString(context);
			logger.info("start request data:{}", data);
			try {
				String msg = client.sendMsg(data);
				logger.info("socket response:{}", msg);
				JSONObject json = JSONObject.parseObject(msg);
				if (json.getString(STATUS_CODE).equals(ERROR_CODE)) {
					throw new RuntimeException(json.getString(MESSAGE_CODE));
				}
				return Response.of(msg);
			} catch (Exception e) {
				Response resp = Response.of(199);
				resp.setException(e);
				return resp;
			}
		}
		return Response.of(199);
	}

	@Override
	public Response<?> baiduTieba(Request request) {
		return sendData(request);
	}

	@Override
	public Response<?> findFemaleBras(Request request) {
		return sendData(request);
	}
}
