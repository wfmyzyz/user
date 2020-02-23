package com.wfmyzyz.user.config;

import com.wfmyzyz.user.interceptor.ControllerBackInterceptor;
import com.wfmyzyz.user.interceptor.ControllerBaseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author admin
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    @Autowired
    private ControllerBaseInterceptor controllerBaseInterceptor;
    @Autowired
    private ControllerBackInterceptor controllerBackInterceptor;

    /**
    * 允许跨域
    * @param registry
    */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedHeaders("*").allowedOrigins("*").allowedMethods("*");
    }
    /**
    * 拦截器
    * @param registry
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(controllerBaseInterceptor).addPathPatterns("/base/**");
        registry.addInterceptor(controllerBackInterceptor).addPathPatterns("/back/**");
    }

    /**
    * 文件路劲映射
    * @param registry
    */
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/outimg/**").addResourceLocations("file:E:/ideawork/IDEA/book/");
    }*/

}