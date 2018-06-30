package com.nvr.filemanager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nvr.filemanager.api.handler.AllowedInterceptor;
import com.nvr.filemanager.api.handler.LogInterceptor;

@Configuration
public class ConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	AllowedInterceptor allowedInterceptor;

	@Autowired
	LogInterceptor logInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor).addPathPatterns("/action/*");
		registry.addInterceptor(allowedInterceptor).addPathPatterns("/action/*");
	}
}