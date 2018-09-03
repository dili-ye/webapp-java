package com.cn.webapp.web.context;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cn.webapp.commons.context.CommonsContext;
import com.cn.webapp.web.controller.BaseController;

@Component(value = "controllerContext")
public class ControllerContext extends CommonsContext<BaseController> {

	private static final Logger logger = LoggerFactory.getLogger(ControllerContext.class);

	@PostConstruct
	public void init() {
		super.init();
		logger.info("controller init end");
	}
}
