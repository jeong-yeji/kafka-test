package com.example;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryableStore {

    private static final Logger log = LoggerFactory.getLogger(QueryableStore.class);
    private static final String APPLICATION_NAME = "global-table-query-store-application";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private static final String ADDRESS_TABLE = "address";
    private static boolean initialize = false;
    private static ReadOnlyKeyValueStore<String, String> keyValueStore;

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KTable<String, String> addressTable = builder.table(ADDRESS_TABLE,
            Materialized.as(ADDRESS_TABLE));

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), properties);
        kafkaStreams.start();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                if (!initialize) {
                    keyValueStore = kafkaStreams.store(
                        StoreQueryParameters.fromNameAndType(ADDRESS_TABLE,
                            QueryableStoreTypes.keyValueStore()));
                    initialize = true;
                }
                printKeyValueStoreData();
            }
        };

        Timer timer = new Timer("Timer");
        long delay = 10000L;
        long interval = 1000L;
        timer.schedule(task, delay, interval);
    }

    private static void printKeyValueStoreData() {
        log.info("=============================");
        KeyValueIterator<String, String> address = keyValueStore.all();
        address.forEachRemaining(keyValue -> log.info(keyValue.toString()));
    }

}