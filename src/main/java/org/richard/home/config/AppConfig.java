package org.richard.home.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.client.MongoClient;
import org.richard.home.service.DocumentService;
import org.richard.home.service.EventPublisherService;
import org.richard.home.service.MongoStoreService;
import org.richard.home.service.MyDummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyDummyService myDummyService() {
        return new MyDummyService("richard", 32);
    }

    @Bean
    @Autowired
    public DocumentService documentService(MongoClient mongoClient, EventPublisherService eventPublisherService) {
        return new MongoStoreService(mongoClient, eventPublisherService);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    @Autowired
    public EventPublisherService eventPublisherService(ObjectMapper objectMapper) {
        return new EventPublisherService(objectMapper);
    }
}
