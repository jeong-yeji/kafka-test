package com.example;

import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class KStreamJoinKTable {

    private static final String APPLICATION_NAME = "order-join-application";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private static final String ADDRESS_TABLE = "address";
    private static final String ORDER_STREAM = "order";
    private static final String ORDER_JOIN_STREAM = "order_join";

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // source processor
        StreamsBuilder builder = new StreamsBuilder();
        KTable<String, String> addressTable = builder.table(ADDRESS_TABLE);
        KStream<String, String> orderStream = builder.stream(ORDER_STREAM);

        // stream processor + sink processor
        orderStream.join(addressTable, (order, address) -> order + " send to " + address)
            .to(ORDER_JOIN_STREAM);

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), properties);
        kafkaStreams.start();
    }

}
