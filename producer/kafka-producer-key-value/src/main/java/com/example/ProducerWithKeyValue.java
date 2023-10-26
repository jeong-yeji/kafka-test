package com.example;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerWithKeyValue {

    private final static Logger logger = LoggerFactory.getLogger(ProducerWithKeyValue.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        ProducerRecord<String, String> record1 = new ProducerRecord<>(TOPIC_NAME, "Seoul", "Seoul");
        producer.send(record1);
        logger.info("{}", record1);

        ProducerRecord<String, String> record2 = new ProducerRecord<>(TOPIC_NAME, "Daegu", "Daegu");
        producer.send(record2);
        logger.info("{}", record2);

        producer.flush();
        producer.close();
    }
}
