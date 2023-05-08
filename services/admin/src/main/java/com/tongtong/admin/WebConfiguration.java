package com.tongtong.admin;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.security.AuthenticationInterceptor;
import com.tongtong.common.security.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .order(1)
                .addPathPatterns(AppConfig.ADMIN_RESOURCE_PATH + "/**")
                .excludePathPatterns("/admin/status/**");

        registry.addInterceptor(new AuthorizationInterceptor())
                .order(2)
                .addPathPatterns(AppConfig.ADMIN_RESOURCE_PATH + "/**")
                .excludePathPatterns("/admin/status/**");
    }
}