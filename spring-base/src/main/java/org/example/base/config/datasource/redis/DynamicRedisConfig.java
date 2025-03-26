package org.example.base.config.datasource.redis;

import org.example.base.config.Constant;
import org.example.base.config.GrayHeaderContextHolder;
import org.example.base.config.datasource.DataSourceProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
            connectionFactoryMap.put(key, createRedisConnectionFactory(config));
        });
        return connectionFactoryMap;
    }

    @Bean
    public LettuceConnectionFactory defaultLettuceConnectionFactory() {
        return createRedisConnectionFactory(dataSourceProperties.getRedis().get(Constant.X_DEFAULT_VERSION));
    }

    public LettuceConnectionFactory createRedisConnectionFactory(DataSourceProperties.RedisDataSourceConfig config) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(config.getHost());
        configuration.setPort(config.getPort());
        configuration.setPassword(config.getPassword());
        configuration.setDatabase(config.getDatabase());

        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet(); // 确保初始化
        return factory;
    }


    @Bean
    public RedisTemplate redisTemplate(@Qualifier("redisConnectionFactoryMap") Map<String, RedisConnectionFactory> redisConnectionFactoryMap) {
        DynamicRoutingRedisTemplate template = new DynamicRoutingRedisTemplate();
        RedisConnectionFactory redisConnectionFactory = redisConnectionFactoryMap.get(Constant.X_DEFAULT_VERSION);
        template.setConnectionFactory(redisConnectionFactory);
        template.setTargetConnectionFactories(redisConnectionFactoryMap);

        // 设置序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(@Qualifier("redisConnectionFactoryMap") Map<String, RedisConnectionFactory> redisConnectionFactoryMap) {
        DynamicRoutingStringRedisTemplate template = new DynamicRoutingStringRedisTemplate();
        RedisConnectionFactory redisConnectionFactory = redisConnectionFactoryMap.get(Constant.X_DEFAULT_VERSION);
        template.setConnectionFactory(redisConnectionFactory);
        template.setTargetConnectionFactories(redisConnectionFactoryMap);

        // 设置序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    public static class DynamicRoutingRedisTemplate extends RedisTemplate<String, Object> {
        private Map<String, RedisConnectionFactory> targetConnectionFactories;

        public void setTargetConnectionFactories(Map<String, RedisConnectionFactory> targetConnectionFactories) {
            this.targetConnectionFactories = targetConnectionFactories;
        }

        @NotNull
        @Override
        public RedisConnectionFactory getConnectionFactory() {
            return targetConnectionFactories.getOrDefault(GrayHeaderContextHolder.getGrayHeader(), super.getConnectionFactory());
        }
    }

    public static class DynamicRoutingStringRedisTemplate extends StringRedisTemplate {
        private Map<String, RedisConnectionFactory> targetConnectionFactories;

        public void setTargetConnectionFactories(Map<String, RedisConnectionFactory> targetConnectionFactories) {
            this.targetConnectionFactories = targetConnectionFactories;
        }

        @NotNull
        @Override
        public RedisConnectionFactory getConnectionFactory() {
            return targetConnectionFactories.getOrDefault(GrayHeaderContextHolder.getGrayHeader(), super.getConnectionFactory());
        }
    }
}
