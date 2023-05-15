package com.tongtong.common.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppConfigListener implements ServletContextListener {

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
            System.out.println("Failed to get host name");
        }
    }
}