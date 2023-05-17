package com.tongtong.product;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.config.AppConfigListener;
import com.tongtong.common.config.EnvConfig;
import com.tongtong.common.security.AuthenticationFilter;
import com.tongtong.common.security.CORSFilter;
import com.tongtong.product.dao.ProductDaoFactory;
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
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = {"com.tongtong.product"})
public class WebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebApplication.class);
    }

    @Bean
    public FilterRegistrationBean authenticationFilterBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.addUrlPatterns(AppConfig.PRODUCTS_RESOURCE_PATH + "/*");
        bean.setFilter(new AuthenticationFilter());
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public FilterRegistrationBean corsFilterBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new CORSFilter());
        bean.setOrder(3);
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

    @Bean("productDaoFactory")
    public FactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(ProductDaoFactory.class);
        return factoryBean;
    }

}