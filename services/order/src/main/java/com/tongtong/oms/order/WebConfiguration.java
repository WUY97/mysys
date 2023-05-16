package com.tongtong.oms.order;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.security.AuthenticationFilter;
import com.tongtong.common.security.AuthorizationFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.EnumSet;

@WebListener
public class WebConfiguration implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.addFilter("AuthenticationFilter", AuthenticationFilter.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, AppConfig.ORDERS_RESOURCE_PATH, AppConfig.ORDERS_RESOURCE_PATH + "/**", AppConfig.CARTS_RESOURCE_PATH, AppConfig.CARTS_RESOURCE_PATH + "/**");

        servletContext.addFilter("AuthorizationFilter", AuthorizationFilter.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, AppConfig.ORDERS_RESOURCE_PATH + "/**", AppConfig.CARTS_RESOURCE_PATH + "/**");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }


}
