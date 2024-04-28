package org.richard.home.web;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DocumentServlet extends HttpServlet {

    private MongoClient mongoClient;
    private GridFSBucket gridFSBucket;
    private static String BUCKET_NAME = "documents";
    private static Logger log = LoggerFactory.getLogger(DocumentServlet.class);

    @Override
    public void init() throws ServletException {
        this.mongoClient = ContextLoaderListener.getCurrentWebApplicationContext().getBean(MongoClient.class);
        gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase("test"), BUCKET_NAME);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("get method hit succesfully");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPost hit");
        try (PrintWriter pout = resp.getWriter()) {
            req.getParameterNames().asIterator().forEachRemaining(param -> log.info("param: {}", param));
            req.getHeaderNames().asIterator().forEachRemaining(header -> log.info("header: {}", header + "=" +  req.getHeader(header) ));
            ByteBuffer byteBuffer = ByteBuffer.allocate(895558126);
            byteBuffer.clear();
            req.getParts().stream().forEach(elem -> {
                try (var elemInputStream = elem.getInputStream()){
                    byteBuffer.put(elemInputStream.readAllBytes());
                    log.info("part size: {}", byteBuffer.position());
                } catch (IOException e) {
                    log.error("error {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            byteBuffer.flip();
            var gridFsOptions = new GridFSUploadOptions()
                    .chunkSizeBytes(1048576)
                    .metadata(new Document(new HashMap(Map.of("type", "mp4")
                    )));
            ObjectId objectId = this.gridFSBucket.uploadFromStream("video.mp4", new ByteArrayInputStream(byteBuffer.array()), gridFsOptions);
            log.info("object id: {}", objectId);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
