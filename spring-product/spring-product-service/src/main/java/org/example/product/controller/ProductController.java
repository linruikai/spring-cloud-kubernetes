package org.example.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.detail.client.DetailFeignClient;
import org.example.dto.RedisDTO;
import org.example.dto.UserDTO;
import org.example.entity.Product;
import org.example.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    private DetailFeignClient detailFeignClient;
    @Autowired
    private IProductService productService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${test.key}")
    private String testKey;
    @GetMapping("test")
    public String test() {
        log.info("this is from product {}", testKey);
        detailFeignClient.test();
        return "this is from product " + testKey;
    }
    @GetMapping("mysql")
    public UserDTO mysql() {
        Product product = productService.getById(1);
        UserDTO userDTO = detailFeignClient.mysql();
        userDTO.setProductName(product.getName());
        return userDTO;
    }

    @GetMapping("redis")
    public RedisDTO redis() {
        String b = redisTemplate.opsForValue().get("b");
        RedisDTO redisDTO = detailFeignClient.redis();
        redisDTO.setKeyB(b);
        return redisDTO;
    }
}
