package com.tongtong.product;

import com.tongtong.common.config.AppConfig;
import com.tongtong.common.security.AuthorizationFilter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.EnumSet;

@WebListener
public class WebConfiguration implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        servletContext.addFilter("AuthorizationFilter", AuthorizationFilter.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, AppConfig.AUTHORIZATION_RESOURCE_PATH + "/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}


