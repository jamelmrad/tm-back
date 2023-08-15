package com.telcotek.apigateway.config;

import com.telcotek.apigateway.logger.VisitorLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    VisitorLogger visitorLogger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(visitorLogger);
    }
}