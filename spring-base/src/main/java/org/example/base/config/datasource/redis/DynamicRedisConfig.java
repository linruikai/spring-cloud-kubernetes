package org.example.base.config.datasource.redis;

import org.example.base.config.Constant;
import org.example.base.config.GrayHeaderContextHolder;
import org.example.base.config.datasource.DataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicRedisConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @RefreshScope
    public Map<String, RedisConnectionFactory> redisConnectionFactoryMap() {
        Map<String, RedisConnectionFactory> connectionFactoryMap = new HashMap<>();
        dataSourceProperties.getRedis().forEach((key, config) -> {
            LettuceConnectionFactory factory = createRedisConnectionFactory(config);
            connectionFactoryMap.put(key, factory);
        });
        return connectionFactoryMap;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(@Qualifier("redisConnectionFactoryMap") Map<String, RedisConnectionFactory> redisConnectionFactoryMap) {
        DynamicRoutingStringRedisTemplate dynamicRoutingStringRedisTemplate = new DynamicRoutingStringRedisTemplate();
        dynamicRoutingStringRedisTemplate.setConnectionFactory(redisConnectionFactoryMap.get(Constant.X_BASE_VERSION)); // Default ConnectionFactory
        dynamicRoutingStringRedisTemplate.setTargetConnectionFactories(redisConnectionFactoryMap);
        return dynamicRoutingStringRedisTemplate;
    }

    private LettuceConnectionFactory createRedisConnectionFactory(DataSourceProperties.RedisDataSourceConfig config) {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(config.getHost(), config.getPort());
        factory.setPassword(config.getPassword());
        factory.setDatabase(config.getDatabase());
        factory.afterPropertiesSet();
        return factory;
    }

    public static class DynamicRoutingStringRedisTemplate extends StringRedisTemplate {
        private Map<String, RedisConnectionFactory> targetConnectionFactories;

        public void setTargetConnectionFactories(Map<String, RedisConnectionFactory> targetConnectionFactories) {
            this.targetConnectionFactories = targetConnectionFactories;
        }

        @Override
        public RedisConnectionFactory getConnectionFactory() {
            return targetConnectionFactories.getOrDefault(GrayHeaderContextHolder.getGrayHeader(), super.getConnectionFactory());
        }
    }
}
