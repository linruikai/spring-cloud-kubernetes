package org.example.base.config.datasource.kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.base.config.Constant;
import org.example.base.config.GrayHeaderContextHolder;

import java.util.Map;
import java.util.Objects;

public class CustomProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {


    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        String topic = record.topic();
        String header = GrayHeaderContextHolder.getGrayHeader();
        if (Objects.nonNull(header)) {
            topic = topic + "-" + header; // 正则匹配topic
            Headers headers = record.headers();
            headers.add(new RecordHeader(Constant.X_GRAY_VERSION, header.getBytes()));
        }
        // 在这里修改消息，例如在值的前面添加前缀
        return new ProducerRecord<>(topic, record.partition(), record.timestamp(), record.key(), record.value(), record.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // 处理发送确认或异常
    }

    @Override
    public void close() {
        // 在关闭拦截器时执行的操作
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // 配置拦截器
    }
}

