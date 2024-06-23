package org.richard.home.config;

import com.mongodb.client.MongoClient;
import org.richard.home.service.DocumentService;
import org.richard.home.service.MongoStoreService;
import org.richard.home.service.MyDummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyDummyService myDummyService(){
        return new MyDummyService("richard", 32);
    }

    @Bean
    @Autowired
    public DocumentService documentService(MongoClient mongoClient){
        return new MongoStoreService(mongoClient);
    }
}
