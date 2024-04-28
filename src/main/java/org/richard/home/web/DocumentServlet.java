package org.richard.home.web;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DocumentServlet extends HttpServlet {

    private MongoClient mongoClient;
    private GridFSBucket gridFSBucket;
    private static String BUCKET_NAME = "documents";
    private static Logger log = LoggerFactory.getLogger(DocumentServlet.class);

    @Override
    public void init() {
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
            final String[] filename = new String[1];
            req.getParts().stream().forEach(elem -> {
                try (var elemInputStream = elem.getInputStream()){
                    byteBuffer.put(elemInputStream.readAllBytes());
                    filename[0] = extractFileName(elem);
                    log.info("part size: {}", byteBuffer.position());
                } catch (IOException e) {
                    log.error("error {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            byte[] finalByteArr = new byte[byteBuffer.position()];
            byteBuffer.flip();
            byteBuffer.get(finalByteArr, 0, byteBuffer.limit());
            var gridFsOptions = new GridFSUploadOptions()
                    .chunkSizeBytes(1048576)
                    .metadata(new Document(new HashMap(Map.of("type", "mp4")
                    )));
            ObjectId objectId = this.gridFSBucket.uploadFromStream(filename[0], new ByteArrayInputStream(finalByteArr), gridFsOptions);
            log.info("object id: {}", objectId);
        }
    }

    /*
    POST /api/document HTTP/1.1
    Host: localhost:8080
    User-Agent: curl/7.79.1
    Accept: /
    Content-Length: 211
    Content-Type: multipart/form-data; boundary=------------------------9dd36b0576a69928

    --------------------------9dd36b0576a69928
    Content-Disposition: form-data; name="testFile"; filename="tikataka.txt"
    Content-Type: text/plain

    Richard is best!

    --------------------------9dd36b0576a69928--
     */
    private static String extractFileName(Part elem) {
        return Arrays.stream(elem.getHeader("Content-Disposition")
                        .split(";"))
                .filter(token -> token.contains("filename"))
                .findFirst().get()
                .split("=")[1]
                .replaceAll("\"","")
                .strip();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

}
