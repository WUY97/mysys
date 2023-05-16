package com.tongtong.oms.order;

import com.tongtong.common.config.*;
import com.tongtong.common.security.CORSFilter;
import com.tongtong.oms.cart.dao.CartDaoFactory;
import com.tongtong.oms.order.dao.OrderDaoFactory;
import jakarta.servlet.ServletContextListener;
import org.springframework.beans.factory.annotation.Autowired;
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

@SpringBootApplication(scanBasePackages = {"com.tongtong.oms.order", "com.tongtong.oms.cart"})
@PropertySource(value = { "classpath:config.properties" })
public class WebApplication extends SpringBootServletInitializer {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebApplication.class);
    }

    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilterBean() {
        FilterRegistrationBean<CORSFilter> bean = new FilterRegistrationBean<>();
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

    @Bean("cartDaoFactory")
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean1() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(CartDaoFactory.class);
        return factoryBean;
    }

    @Bean("orderDaoFactory")
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean2() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(OrderDaoFactory.class);
        return factoryBean;
    }

}
