package com.cn.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface AppService {
	// 容器的id
	String value() default "";
	// 匹配标示符
	long id();
	// 中文
	String name();
	
	boolean enabled() default true;
}
