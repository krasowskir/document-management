package org.richard.home.config;

import org.richard.home.service.MyDummyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyDummyService myDummyService(){
        return new MyDummyService("richard", 32);
    }
}
