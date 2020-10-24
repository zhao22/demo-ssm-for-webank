package com.seanzx.configuration;

import com.seanzx.common.email.EmailConfigure;
import com.seanzx.common.email.EmailSender;
import com.seanzx.common.email.EmailSenderDecorator;
import com.seanzx.common.email.WebankEmailConfigure;
import com.seanzx.interceptor.HttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * Web Mvc 配置类,将拦截器加入链中
 * @author zhaoxin
 * @date 2020/10/21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private HttpRequestInterceptor httpRequestInterceptor;
    @Autowired
    private WebankEmailConfigure webankEmailConfigure;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpRequestInterceptor)
                .excludePathPatterns(Arrays.asList("/swagger-ui.html",
                        "/swagger-resources/*",
                        "/error",
                        "/webjars/*"));
    }


    @Bean
    public EmailSenderDecorator emailSenderDecorator() {
        return new EmailSenderDecorator(new EmailSender(webankEmailConfigure));
    }
}
