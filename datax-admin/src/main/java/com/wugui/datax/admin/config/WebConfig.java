package com.wugui.datax.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 前端静态资源访问
 *
 * @program: com.wugui.datax-all
 * @author: huzekang
 * @create: 2019-06-17 10:40
 **/
@Configuration

public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/index.html");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/static/");
    }
}
