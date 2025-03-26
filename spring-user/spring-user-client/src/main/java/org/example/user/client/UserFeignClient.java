package org.example.user.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "spring-user")
public interface UserFeignClient {
}
