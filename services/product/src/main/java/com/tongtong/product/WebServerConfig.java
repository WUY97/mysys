package com.tongtong.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebServerConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Value("${port.product}")
    private int port;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setPort(port);
    }
}