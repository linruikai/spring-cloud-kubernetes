package org.example.product.client;

import org.example.dto.RedisDTO;
import org.example.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "spring-product")
public interface ProductFeignClient {

    @GetMapping("test")
    void test();

    @GetMapping("mysql")
    UserDTO mysql();

    @GetMapping("redis")
    RedisDTO redis();
}
