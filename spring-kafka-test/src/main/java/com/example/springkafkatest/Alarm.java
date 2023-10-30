package com.example.springkafkatest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String message;

    @Builder
    private Alarm(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s : %s", topic, message);
    }
}
