package org.example.base.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring")
public class DataSourceProperties {
    private Map<String, DataSourceConfig> datasource;
    private Map<String, RedisDataSourceConfig> redis;

    @Data
    public static class DataSourceConfig {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }

    @Data
    public static class RedisDataSourceConfig {
        private Integer database;
        private String host;
        private Integer port;
        private String password;
    }
}

