package com.edu.bupt.pcs.consult.config;
//import com.bupt.pcs.examservice.interceptor.AuthenticationInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author wzz
 * @Date 19-4-9 20:50
 */
@Configuration
@EnableWebMvc
public class InterceptorConfig implements WebMvcConfigurer {
    //@Override
    //public void addInterceptors(InterceptorRegistry registry) {
    //    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
    //    registry.addInterceptor(authenticationInterceptor())
    //            .addPathPatterns("/**");
    //}
    //@Bean
    //public AuthenticationInterceptor authenticationInterceptor() {
    //    return new AuthenticationInterceptor();
    //}
    /*
       配置swagger
     */
    @Override
    public  void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "OPTIONS", "PUT")
//                .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin","token","Access-Control-Allow-Credentials",
//                        "Access-Control-Request-Method", "Access-Control-Request-Headers","Access-Control-Allow-Origin")
////                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//                .allowCredentials(true).maxAge(3600);
//    }



}
