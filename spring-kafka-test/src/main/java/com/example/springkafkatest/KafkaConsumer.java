package com.example.springkafkatest;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

//    @KafkaListener(topics = "#{'${spring.kafka.topics}'.split('\\\\')}", groupId = "test-group")
    @KafkaListener(topicPattern = "^test(\\.([a-z]+))*$")
    public void consume(String message) throws IOException {
        log.info("Consumed message : {}", message);
    }

}
