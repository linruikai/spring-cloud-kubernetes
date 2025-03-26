package org.example.base.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            String version = GrayHeaderContextHolder.getGrayHeader();
            if (Objects.nonNull(version)) {
                template.header(Constant.X_GRAY_VERSION, version);
            }
        };
    }


}
