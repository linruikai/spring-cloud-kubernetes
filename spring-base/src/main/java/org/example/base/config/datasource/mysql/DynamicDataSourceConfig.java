package org.example.base.config.datasource.mysql;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.base.config.Constant;
import org.example.base.config.GrayHeaderContextHolder;
import org.example.base.config.datasource.DataSourceProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DynamicDataSourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    @RefreshScope
    public DynamicDataSource dataSource() {
        DynamicDataSource routingDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        dataSourceProperties.getDatasource().forEach((key, value) -> {
            log.info("读取到的mysql配置 {}",key);
            targetDataSources.put(key, createDataSource(value));
        });
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get(Constant.X_DEFAULT_VERSION)); // Default DataSource
        routingDataSource.setTargetDataSources(targetDataSources);
        return routingDataSource;
    }

    private DataSource createDataSource(DataSourceProperties.DataSourceConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    public static class DynamicDataSource extends AbstractRoutingDataSource {
        @NotNull
        @Override
        protected Object determineCurrentLookupKey() {
            return GrayHeaderContextHolder.getGrayHeader();
        }
    }
}

