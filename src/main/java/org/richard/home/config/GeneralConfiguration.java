package org.richard.home.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static java.lang.String.format;


@Configuration
public class GeneralConfiguration {
    private static final Logger log = LoggerFactory.getLogger(GeneralConfiguration.class);
    private static String MONGO_HOST, MONGO_PORT, MONGO_DB_NAME, MONGO_USERNAME, MONGO_PASSWORD, MONGO_BUCKET;

    static {
        Properties props = new Properties();
        try {
            props.load(
                    Files.newInputStream(
                            Path.of(
                                    GeneralConfiguration.class.getClassLoader().getResource("application.properties").toURI())));
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        GeneralConfiguration.MONGO_HOST = props.getProperty(ApplicationPropertyConstants.MONGO_HOST);
        GeneralConfiguration.MONGO_PORT = props.getProperty(ApplicationPropertyConstants.MONGO_PORT);
        GeneralConfiguration.MONGO_DB_NAME = props.getProperty(ApplicationPropertyConstants.MONGO_DB_NAME);
        GeneralConfiguration.MONGO_BUCKET = props.getProperty(ApplicationPropertyConstants.MONGO_BUCKET_NAME);
        GeneralConfiguration.MONGO_USERNAME = props.getProperty(ApplicationPropertyConstants.MONGO_USERNAME);
        GeneralConfiguration.MONGO_PASSWORD = props.getProperty(ApplicationPropertyConstants.MONGO_PASSWORD);
    }

    public GeneralConfiguration() {
    }


    @Bean
    public MongoClient mongoClient() {

        return MongoClients.create(
                MongoClientSettings.builder()

                        .applyConnectionString(
                                new ConnectionString(
                                        format("mongodb://%s:%s", MONGO_HOST, MONGO_PORT)))
                        .credential(MongoCredential.createCredential(MONGO_USERNAME, "admin", MONGO_PASSWORD.toCharArray()))
                        .build()
        );
    }

}
