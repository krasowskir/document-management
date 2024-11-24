package org.richard.home.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.richard.home.config.GeneralConfiguration;
import org.richard.home.domain.DocumentUploadedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.richard.home.config.ApplicationPropertyConstants.*;

public class MongoStoreService implements DocumentService {


    private static final Logger log = LoggerFactory.getLogger(MongoStoreService.class);
    private GridFSBucket gridFSBucket;

    private MongoClient mongoClient;
    private static final String DATABASE, BUCKET_NAME;

    private EventPublisherService eventPublisherService;

    static {
        DATABASE = Optional.ofNullable(System.getProperty(DATABASE_NAME)).orElse("test");
        BUCKET_NAME = Optional.ofNullable(System.getProperty("bucket-name")).orElse("documents");
        log.info("database: {} and bucket name: {}", DATABASE_NAME, MONGO_BUCKET_NAME);
    }

    public MongoStoreService(MongoClient mongoClient, EventPublisherService eventPublisherService) {
        this.mongoClient = mongoClient;
        gridFSBucket = GridFSBuckets.create(this.mongoClient.getDatabase(DATABASE), BUCKET_NAME);
        this.eventPublisherService = eventPublisherService;

    }

    @Override
    public String uploadDocument(InputStream in, String fileName) throws JsonProcessingException {
        log.info("uploadDocument invoked");
        ByteBuffer byteBuffer = ByteBuffer.allocate(895558126);
        byteBuffer.clear();
        var gridFsOptions = new GridFSUploadOptions()
                .chunkSizeBytes(1048576)
                .metadata(new Document(new HashMap(Map.of("type", "png") //"mp4"
                )));
        String objectId = this.gridFSBucket.uploadFromStream(fileName, in, gridFsOptions).toHexString();
        var documentUploadedEvent = DocumentUploadedEvent.create(fileName, objectId);
        var sent = eventPublisherService.publishEvent(documentUploadedEvent);

        log.info("sent event to topic: {} and offset: {}", sent.topic(), sent.offset());
        return objectId;
    }

    @Override
    public byte[] getDocument(String fileName) throws IOException {
//        this.mongoClient.getDatabase(DATABASE).getCollection()

        try (InputStream in = gridFSBucket.openDownloadStream(fileName)){
            return in.readAllBytes();
        } catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }


    @Override
    public byte[] getDocumentByObjectId(String objectId) {
        var validObjectIdAsText = Objects.requireNonNull(objectId.trim());
        log.info("validated objectId: {}", validObjectIdAsText);
        try (InputStream in = gridFSBucket.openDownloadStream(new ObjectId(validObjectIdAsText))){
            return in.readAllBytes();
        } catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}
