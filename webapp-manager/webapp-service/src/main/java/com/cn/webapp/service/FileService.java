package com.cn.webapp.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {

	boolean canService(String actionType, HttpServletRequest request, HttpServletResponse response);

	void service(String actionType, HttpServletRequest request, HttpServletResponse response);

}