package org.richard.home.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static java.lang.String.format;
class GeneralConfigurationTest {

    @Test
    void testConnectionToMongo(){
        Properties props = new Properties();
        try {
            props.load(
                    Files.newInputStream(
                            Path.of(
                                    GeneralConfiguration.class.getClassLoader().getResource("application.properties").toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String USERNAME = props.getProperty("mongo.db.username");
        String PASSWORD = props.getProperty("mongo.db.password");

        String MONGO_HOST = props.getProperty("mongo.db.host");
        String MONGO_PORT = props.getProperty("mongo.db.port");
        String MONGO_DB_NAME = props.getProperty("mongo.db.name");
        try (MongoClient client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(
                                new ConnectionString(
                                        format("mongodb://%s:%s", MONGO_HOST, MONGO_PORT)))
                        .credential(MongoCredential.createCredential(USERNAME, "admin", PASSWORD.toCharArray()))
                        .build()
        )) {
            client.listDatabaseNames().iterator().forEachRemaining(elem -> System.out.println("database: " + elem));
        }
    }
}