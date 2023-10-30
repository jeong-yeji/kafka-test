package com.example.springkafkatest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private static final String TOPIC = "test";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AlarmRepository alarmRepository;

    public Alarm sendMessageTo(String topic, String message) {
        if (topic == null) {
            topic = TOPIC;
        }

        log.info("Produce message to {} : {}", topic, message);
        this.kafkaTemplate.send(topic, message);
        return save(topic, message);
    }

    public Alarm save(String topic, String message) {
        return alarmRepository.save(Alarm.builder().topic(topic).message(message).build());
    }
}
