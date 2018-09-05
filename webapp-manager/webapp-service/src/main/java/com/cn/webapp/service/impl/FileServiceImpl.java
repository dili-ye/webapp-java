package com.cn.webapp.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cn.webapp.commons.annotation.AppService;
import com.cn.webapp.service.FileService;
import com.google.common.collect.Maps;

@AppService(id = 0, name = "default")
public class FileServiceImpl implements FileService {

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	protected Map<String, Method> methods = Maps.newHashMap();

	@Override
	public boolean canService(String actionType, HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

	@Override
	public void service(String actionType, HttpServletRequest request, HttpServletResponse response) {
		String picPath = request.getParameter("picPath");
		File f = new File(picPath);
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			BufferedImage bi = null;
			int getImgCount = 0;
			while (bi == null) {
				try {
					getImgCount++;
					bi = ImageIO.read(f);
					if (bi != null || getImgCount > 3000 / 300) {
						break;
					}
				} catch (Exception e) {
					logger.error("file read fail....getImgCount:{}", getImgCount);
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			if (bi != null) {
				ImageIO.write(bi, "png", outputStream);
			}
		} catch (IOException e) {
			logger.info("file read error:{}", JSON.toJSONString(e));
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
