package com.tongtong.common.config;

import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class EnvConfig {

    private static final String USE_SERVICE_REGISTRY = "eureka.client.registerWithEureka";

    private static Properties properties = new Properties();
    private static final String PROP_FILE_NAME = "config";

    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public EnvConfig() {
        initialize();
    }

    public void initialize() {

        String propFileName = PROP_FILE_NAME + ".properties";

        try {
            InputStream in = EnvConfig.class.getClassLoader().getResourceAsStream(propFileName);
            if (in != null) {
                properties.load(in);
            } else {
                System.out.println("Can't find " + propFileName);
            }
        } catch (IOException e1) {
            System.out.println("Can't find " + propFileName);
        }
        System.getProperties().stringPropertyNames().
                forEach(key -> {
                    if (properties.containsKey(key)) properties.setProperty(key, System.getProperty(key));
                });
        System.getenv().keySet().
                forEach(key -> {
                    if (properties.containsKey(key)) properties.setProperty(key, System.getenv(key));
                });
        String[] otherEnv = {"server.log.level", "server.log.path"};
        for (String key : otherEnv) {
            if (System.getenv(key) != null) {
                properties.setProperty(key, System.getenv(key));
            }
        }
        Map<String, String> envLog = new HashMap<>();
        SortedSet<String> sortedProps = new TreeSet<>();
        sortedProps.addAll(properties.stringPropertyNames());
        sortedProps.forEach(x -> envLog.put(x, properties.getProperty(x)));
    }

    public boolean useServiceRegistry() {
        return Boolean.parseBoolean(properties.getProperty(USE_SERVICE_REGISTRY));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
