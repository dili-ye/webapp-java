package com.cn.car;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class })
@ComponentScan(basePackages = "com.cn.car")
public class CarAppStater {
	public static void main(String[] args) {
		SpringApplication.run(CarAppStater.class, args);
	}
}
