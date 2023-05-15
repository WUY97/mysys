package com.tongtong.auth;

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
                .addPathPatterns(AppConfig.AUTHORIZATION_RESOURCE_PATH + "/**")
                .excludePathPatterns(AppConfig.AUTHORIZATION_RESOURCE_PATH
                        + AppConfig.AUTH_TOKEN_PATH)
                .excludePathPatterns(AppConfig.AUTHORIZATION_RESOURCE_PATH
                        + AppConfig.AUTH_TOKEN_USER_PATH)
                .addPathPatterns(AppConfig.USERS_PROFILE_RESOURCE_PATH)
                .addPathPatterns(AppConfig.USERS_PROFILE_RESOURCE_PATH + "/**");

        registry.addInterceptor(new AuthorizationInterceptor())
                .order(2)
                .addPathPatterns(AppConfig.AUTHORIZATION_RESOURCE_PATH + "/**")
                .excludePathPatterns(AppConfig.AUTHORIZATION_RESOURCE_PATH
                        + AppConfig.AUTH_TOKEN_PATH)
                .excludePathPatterns(AppConfig.AUTHORIZATION_RESOURCE_PATH
                        + AppConfig.AUTH_TOKEN_USER_PATH)
                .addPathPatterns(AppConfig.USERS_PROFILE_RESOURCE_PATH + "/**");
    }
}