package com.tongtong.auth;

import com.tongtong.auth.core.AuthMgrFactory;
import com.tongtong.common.config.AppConfigListener;
import com.tongtong.common.config.EnvConfig;
import com.tongtong.common.security.CORSFilter;
import com.tongtong.common.security.JJwtUtility;
import com.tongtong.common.security.JwtUtility;
import jakarta.servlet.ServletContextListener;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = {"com.tongtong.auth"})
@PropertySource(value = {"classpath:config.properties"})
public class WebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebApplication.class);
    }

    @Bean
    public JwtUtility jwtUtility() {
        return new JJwtUtility();
    }

    @Bean
    public FilterRegistrationBean corsFilterBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new CORSFilter());
        bean.setOrder(2);
        return bean;
    }

    @Bean
    public EnvConfig envConfig(Environment environment) {
        EnvConfig envConfigBean = new EnvConfig();
        envConfigBean.setEnvironment(environment);
        return envConfigBean;
    }

    @Bean
    public ServletListenerRegistrationBean<ServletContextListener>
    appConfigListenerRegistration() {
        AppConfigListener appConfigListener = new AppConfigListener();
        ServletListenerRegistrationBean<ServletContextListener> bean =
                new ServletListenerRegistrationBean<>();
        bean.setListener(appConfigListener);
        return bean;
    }

    @Bean("authMgrFactory")
    public FactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(AuthMgrFactory.class);
        return factoryBean;
    }

}