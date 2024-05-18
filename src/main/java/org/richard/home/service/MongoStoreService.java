package org.richard.home.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoStoreService implements DocumentService {


    private static final Logger log = LoggerFactory.getLogger(MongoStoreService.class);
    private GridFSBucket gridFSBucket;
    private static final String BUCKET_NAME;
    private static final String DATABASE;

    static {
        DATABASE = Optional.ofNullable(System.getProperty("database")).orElse("test");
        BUCKET_NAME = Optional.ofNullable(System.getProperty("bucketName")).orElse("documents");
        log.info("database: {} and bucket name: {}", DATABASE, BUCKET_NAME);
    }

    public MongoStoreService(MongoClient mongoClient) {
        gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase(DATABASE), BUCKET_NAME);
    }

    @Override
    public String uploadDocument(InputStream in, String fileName) {
        log.info("uploadDocument invoked");
        ByteBuffer byteBuffer = ByteBuffer.allocate(895558126);
        byteBuffer.clear();
        var gridFsOptions = new GridFSUploadOptions()
                .chunkSizeBytes(1048576)
                .metadata(new Document(new HashMap(Map.of("type", "mp4")
                )));
        return this.gridFSBucket.uploadFromStream(fileName, in, gridFsOptions).toHexString();
    }
}
