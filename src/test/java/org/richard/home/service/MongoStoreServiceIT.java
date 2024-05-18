package org.richard.home.service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static java.lang.String.format;

@Testcontainers
class MongoStoreServiceIT {

    private MongoClient mongoClient;

    private GridFSBucket testGridFsClient;
    private static String BUCKET_NAME = "testBucket";
    private static String MONGO_DB_NAME = "testDB";
    private static MongoDatabase mongoDatabase;

    @Container
    public GenericContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:4.4.29"));


    @BeforeEach
    void setup() {
        String MONGO_HOST = "localhost";
        int MONGO_PORT = mongo.getFirstMappedPort();
        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(
                                new ConnectionString(
                                        format("mongodb://%s:%s/%s", MONGO_HOST, MONGO_PORT, MONGO_DB_NAME)))
                        .build());

        mongoDatabase = mongoClient.getDatabase(MONGO_DB_NAME);
        testGridFsClient = GridFSBuckets.create(mongoClient.getDatabase(MONGO_DB_NAME), BUCKET_NAME);
    }


    @Test
    void shouldUploadDocuments() throws IOException, InterruptedException {
        //given
        System.setProperty("database", MONGO_DB_NAME);
        System.setProperty("bucketName", BUCKET_NAME);
        var objectUnderTest = new MongoStoreService(mongoClient);

        //when
        objectUnderTest.uploadDocument(this.getClass().getClassLoader().getResource("files/myFile.txt").openStream(),"myFile.txt");

        //then
        Thread t1 = new Thread(() -> {
            Assertions.assertThat(mongoDatabase.getCollection(BUCKET_NAME + ".files").find().first().size()).isGreaterThan(0) ;
        });
        t1.start();
        t1.join(3000);
    }
}