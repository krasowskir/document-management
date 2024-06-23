package org.richard.home.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.richard.home.config.ApplicationPropertyConstants.DATABASE_NAME;
import static org.richard.home.config.ApplicationPropertyConstants.MONGO_BUCKET_NAME;

public class MongoStoreService implements DocumentService {


    private static final Logger log = LoggerFactory.getLogger(MongoStoreService.class);
    private GridFSBucket gridFSBucket;

    private MongoClient mongoClient;
    private static final String DATABASE, BUCKET_NAME;

    static {
        DATABASE = Optional.ofNullable(System.getProperty(DATABASE_NAME)).orElse("test");
        BUCKET_NAME = Optional.ofNullable(System.getProperty("bucket-name")).orElse("documents");
        log.info("database: {} and bucket name: {}", DATABASE_NAME, MONGO_BUCKET_NAME);
    }

    public MongoStoreService(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        gridFSBucket = GridFSBuckets.create(this.mongoClient.getDatabase(DATABASE), BUCKET_NAME);

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

    @Override
    public byte[] getDocument(String fileName) throws IOException {
//        this.mongoClient.getDatabase(DATABASE).getCollection()

        return gridFSBucket.openDownloadStream(fileName).readAllBytes();
    }
}
