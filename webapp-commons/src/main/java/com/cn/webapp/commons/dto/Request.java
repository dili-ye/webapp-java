package com.cn.webapp.commons.dto;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.cn.webapp.commons.pub.util.UUIDUtil;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5960932972846007724L;
	private int requestId;
	private String userId;
	private String uuid;
	private String actionName;
	private Map<String, String> context;

	public static final Request of(String actionName, HttpServletRequest request) {
		Map<String, String[]> map = request.getParameterMap();
		Map<String, String> data = Maps.newHashMap();
		String userId = null;
		for (Iterator<Entry<String, String[]>> it = map.entrySet().iterator(); it.hasNext();) {
			Entry<String, String[]> e = it.next();
			String[] value = e.getValue();
			if (value != null && value.length > 0) {
				if ("userId".equals(e.getKey())) {
					userId = e.getValue()[0];
				} else {
					if (value.length > 1) {
						data.put(e.getKey(), JSON.toJSONString(value));
					} else {
						data.put(e.getKey(), value[0]);
					}
				}
			}
		}
		return Request.builder().requestId(request.hashCode()).uuid(UUIDUtil.getUUID32()).context(data).userId(userId)
				.actionName(actionName).build();
	}

	public String putParam(String key, String value) {
		if (context == null) {
			context = Maps.newHashMap();
		}
		return context.put(key, value);
	}

	public void putParams(Map<String, String> params) {
		if (params == null || params.isEmpty()) {
			return;
		}
		if (context == null) {
			context = Maps.newHashMap();
		}
		params.forEach((k, v) -> {
			context.put(k, v);
		});
	}
}
