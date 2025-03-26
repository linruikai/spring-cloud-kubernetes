package org.example.detail.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.example.base.config.GrayHeaderContextHolder;
import org.example.detail.service.IDetailService;
import org.example.entity.Detail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private IDetailService detailService;
    @KafkaListener(topicPattern = "${spring.kafka.topics.topic-a}") // ^topic-a(?:-.+)? 正则匹配，以topic-a开头，后面紧接字符- 的所有topic  topic-a  topic-a-gray
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message: {} from topic:{}",  record.value(), record.topic());
        Detail detail = detailService.getById(1);
        log.info("detail:{}", detail);
    }
}
