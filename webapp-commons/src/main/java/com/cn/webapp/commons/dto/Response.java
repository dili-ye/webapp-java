package com.cn.webapp.commons.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9110481311874366732L;

	// 响应业务状态
	private Integer status;

	private Long time;
	// 响应消息
	private String msg;

	private Throwable exception;

	// 响应中的数据
	private T data;

	@SuppressWarnings("rawtypes")
	public static <T> Response of(Integer status, Integer time, String msg, T data) {
		return Response.builder().status(status).msg(msg).data(data).build();
	}

	@SuppressWarnings("rawtypes")
	public static <T> Response of(Integer status, Long time, String msg) {
		return Response.builder().status(status).msg(msg).time(time).build();
	}

	@SuppressWarnings("rawtypes")
	public static <T> Response of(Integer status, String msg) {
		return Response.builder().status(status).msg(msg).build();
	}

	@SuppressWarnings("rawtypes")
	public static <T> Response of(Integer status) {
		return Response.builder().status(status).build();
	}

	@SuppressWarnings("rawtypes")
	public static <T> Response of(Integer status, Long time) {
		return Response.builder().status(status).time(time).build();
	}

	@SuppressWarnings("rawtypes")
	public static <T> Response of(T data) {
		return Response.builder().status(200).data(data).build();
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", msg=" + msg + ", data=" + data.toString() + "]";
	}

}
