package org.richard.home.config;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class KafkaConnectionFactory {

    private static final Properties properties;
    private static KafkaConnectionFactory INSTANCE;

    static {
        properties = new Properties();
        try (InputStream inputStream = KafkaConnectionFactory.class
                .getClassLoader()
                .getResource("application.properties")
                .openStream()) {
            if (inputStream == null) {
                properties.put("bootstrap.servers", "192.168.0.11:9092");
                properties.put("group.id", "tankstelle-1");
                properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            } else {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private KafkaConnectionFactory() {}

    public static KafkaConnectionFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KafkaConnectionFactory();
        }
        return INSTANCE;
    }

    public static Properties getConnectionProperties() {
        getInstance();
        return KafkaConnectionFactory.properties;
    }
}
