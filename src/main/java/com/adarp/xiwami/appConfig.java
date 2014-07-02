package com.adarp.xiwami;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class appConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
    	registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    	registry.addResourceHandler("/css/**").addResourceLocations("/css/");
    	registry.addResourceHandler("/img/**").addResourceLocations("/img/");
    	registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }
}
