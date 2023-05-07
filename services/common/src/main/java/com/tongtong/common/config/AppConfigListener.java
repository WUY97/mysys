package com.tongtong.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppConfigListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppConfigListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            System.setProperty("hostName", hostName);
        } catch (UnknownHostException e) {
            logger.error("Exception finding host; error={}", e.getMessage());
        }
    }
}