package org.richard.home.async;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.richard.home.config.KafkaConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class KafkaListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaListener.class);

    public KafkaListener() {}

    public void start() {
        try (KafkaConsumer<String, String> kafkaConsumer =
                new KafkaConsumer<>(KafkaConnectionFactory.getConnectionProperties())) {
            kafkaConsumer.subscribe(List.of("uploads"));
            while (true) {
                log.info("polling of events...");
                ConsumerRecords<String, String> events = kafkaConsumer.poll(Duration.of(5, ChronoUnit.SECONDS));
                if (events.isEmpty()) {
                    log.info("no events received...");
                } else {
                    events.iterator().forEachRemaining(event -> log.info("event received: {}", event));
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
