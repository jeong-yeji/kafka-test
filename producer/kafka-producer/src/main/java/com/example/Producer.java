package com.example;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

    private final static Logger logger = LoggerFactory.getLogger(Producer.class);
    private final static String TOPIC_NAME = "test";
    private final static String BOOTSTRAP_SERVERS = "my-kafka:9092";

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // 레코드 전송하기
        ProducerRecord<String, String> record1 = new ProducerRecord<>(TOPIC_NAME, "Seoul");
        producer.send(record1);
        logger.info("{}", record1);

        // 메시지 키를 가진 레코드 전송하기
        ProducerRecord<String, String> record2 = new ProducerRecord<>(TOPIC_NAME, "Daegu", "Daegu");
        producer.send(record2);
        logger.info("{}", record2);

        // 파티션을 지정하여 레코드 전송하기
        int partitionNo = 0;
        ProducerRecord<String, String> record3 = new ProducerRecord<>(TOPIC_NAME, partitionNo, "Busan", "Busan");
        producer.send(record3);
        logger.info("{}", record3);

        // 커스텀 파티셔너를 이용하여 파티션 지정하기
        ProducerRecord<String, String> record4 = new ProducerRecord<>(TOPIC_NAME, "Suwon", "Suwon");
        producer.send(record4);
        logger.info("{}", record4);

        // 레코드 전송 결과 확인하기 - 동기
        ProducerRecord<String, String> record5 = new ProducerRecord<>(TOPIC_NAME, "Incheon", "Incheon");
        producer.send(record5, new ProducerCallback());
        logger.info("{}", record5);

        // 레코드 전송 결과 확인하기 - 비동기
        ProducerRecord<String, String> record6 = new ProducerRecord<>(TOPIC_NAME, "Jeju", "Jeju");
        try {
            RecordMetadata recordMetadata = producer.send(record6).get();
            logger.info("전송한 레코드의 메타 데이터 : {}", recordMetadata.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            producer.flush(); // Accumulator에 있는 모든 데이터 강제 전송
            producer.close(); // Producer를 안전하게 종료
        }
    }
}
