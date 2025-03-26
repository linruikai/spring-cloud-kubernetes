package org.example.detail.client;

import org.example.dto.RedisDTO;
import org.example.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "spring-detail")
public interface DetailFeignClient {
    @GetMapping("test")
    void test();

    @GetMapping("mysql")
    UserDTO mysql();

    @GetMapping("redis")
    RedisDTO redis();
}
