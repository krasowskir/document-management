package org.richard.home.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.richard.home.config.KafkaConnectionFactory;
import org.richard.home.domain.DocumentUploadedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EventPublisherService {
    private static final Logger log = LoggerFactory.getLogger(EventPublisherService.class);
    private final String topic;
    private final KafkaProducer<String, String> kafkaProducer;
    private ObjectMapper objectMapper;

    public EventPublisherService(ObjectMapper objectMapper) {
        Properties kafkaProps = KafkaConnectionFactory.getConnectionProperties();
        topic = kafkaProps.getProperty("topic");
        this.kafkaProducer = new KafkaProducer<String,String>(kafkaProps);
        this.objectMapper = objectMapper;
    }

    public RecordMetadata publishEvent(DocumentUploadedEvent documentUploadedEvent) throws JsonProcessingException {
        var eventAsText = objectMapper.writeValueAsString(documentUploadedEvent);
        var producerEvent = new ProducerRecord<String, String>(topic, documentUploadedEvent.getId().toString(), eventAsText);
        try {
            return kafkaProducer.send(producerEvent).get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("could not send event to kafka!");
            throw new RuntimeException(e);
        } catch (ExecutionException | InterruptedException e) {
            log.error("could not send event to kafka!");
            throw new RuntimeException(e);
        }
    }

}
