package com.imooc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="com.imooc")
@PropertySource("classpath:resource.properties")
public class ResourceConfig {

    private String zookeeperServer;
    private String AppServerUrl;

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public String getAppServerUrl() {
        return AppServerUrl;
    }

    public void setAppServerUrl(String appServerUrl) {
        this.AppServerUrl = appServerUrl;
    }
}