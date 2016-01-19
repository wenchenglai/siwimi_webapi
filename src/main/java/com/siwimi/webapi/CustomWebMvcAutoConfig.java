package com.siwimi.webapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CustomWebMvcAutoConfig extends WebMvcConfigurerAdapter {

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        	"classpath:/META-INF/resources/", "classpath:/resources/",
        	"classpath:/static/", "classpath:/public/"};
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS_from_file = {
    	"file:/home/siwimi/image/events/", "file:///C:/image/events/"};
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
		registry.addResourceHandler("/image/events/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS_from_file);
		super.addResourceHandlers(registry);
	}
}
