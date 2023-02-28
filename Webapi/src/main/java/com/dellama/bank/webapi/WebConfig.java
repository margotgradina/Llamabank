package com.dellama.bank.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MyResourceTransformer resourceTransformer;

    public WebConfig(MyResourceTransformer resourceTransformer) {
        this.resourceTransformer = resourceTransformer;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(false)
                .addTransformer(resourceTransformer);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // Enable cross-origin request handling for the specified path pattern.
                // Exact path mapping URIs (such as "/admin") are supported as well as Ant-style path patterns (such as "/admin/**").
                .addMapping("/*")
                .allowedOrigins("*")
                // .allowedOriginPatterns("")
                .allowCredentials(false)
                .allowedHeaders("*")
                .exposedHeaders("*")
                .maxAge(60 *30)
                .allowedMethods("*")
        ;

    }
}