package com.example.springkafkatest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducer producer;

    @GetMapping("/")
    public String test() {
        return "kafka test";
    }

    @PostMapping("/")
    public String sendMessageTo(@RequestParam(value = "topic", required = false) String topic,
        @RequestParam("message") String message) {
        Alarm alarm = this.producer.sendMessageTo(topic, message);
        return String.format("Send message to %s", alarm.toString());
    }
}
