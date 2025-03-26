package org.example.detail.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.detail.service.IDetailService;
import org.example.dto.RedisDTO;
import org.example.dto.UserDTO;
import org.example.entity.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DetailController {
    @Autowired
    private IDetailService detailService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${test.key}")
    private String testKey;
    @GetMapping("test")
    public String test() {
        log.info("this is from detail {}", testKey);
        return "this is from detail " + testKey;
    }
    @GetMapping("mysql")
    public UserDTO mysql() {
        Detail detail = detailService.getById(1);
        UserDTO userDTO = new UserDTO();
        userDTO.setDetailName(detail.getName());
        return userDTO;
    }

    @GetMapping("redis")
    public RedisDTO redis() {
        String c = redisTemplate.opsForValue().get("c");
        RedisDTO redisDTO = new RedisDTO();
        redisDTO.setKeyC(c);
        return redisDTO;
    }
}