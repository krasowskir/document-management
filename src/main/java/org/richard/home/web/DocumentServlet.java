package org.richard.home.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.richard.home.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet(value = {"/api/documents/*"})
public class DocumentServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(DocumentServlet.class);

    private static final String PREFIX = "prefix";
    private static final String FILE_NAME = "fileName";
    private static final String OBJECT_ID_QUERY_PARAM = "objectId";
    private DocumentService documentService;
    Function<Part, String> extractFileNameFromHeader = elem ->
            Arrays.stream(elem.getHeader("Content-Disposition")
                            .split(";"))
                    .filter(token -> token.contains("filename"))
                    .findFirst().get()
                    .split("=")[1]
                    .replaceAll("\"", "")
                    .strip();


    @Override
    public void init() {
        this.documentService = ((AnnotationConfigWebApplicationContext) this.getServletContext().getAttribute("applicationContext")).getBean(DocumentService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var fileName = req.getParameter(FILE_NAME);
            byte[] document = null;
            if (fileName == null) {
                var objectId = Objects.requireNonNull(req.getParameter(OBJECT_ID_QUERY_PARAM));
                if (objectId.isBlank()) {
                    throw new IllegalArgumentException("fileName or objectId needs to be provided to obtain a file!");
                } else {
                    log.info("doGet hit with fileName: {} and objectId: {}", null, objectId);
                    document = documentService.getDocumentByObjectId(objectId);
                }
            } else {
                log.info("doGet hit with fileName: {} and objectId: {}", fileName, null);
                document = documentService.getDocument(fileName);
            }

            log.info("document was: {} byte long", document.length);
            resp.setContentType("application/octet-stream");
            resp.setContentLength(document.length);
            resp.setHeader("Content-Disposition", "attachment; filename=logo");
            try (OutputStream out = resp.getOutputStream()) {
                out.write(document);
                out.flush();
            }
            log.info("finished transferring file!");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getOutputStream().println(e.getMessage());
            resp.getOutputStream().flush();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("doPost hit");
        try (PrintWriter pout = resp.getWriter()) {
            req.getParameterNames().asIterator().forEachRemaining(param -> log.info("param: {}", param));
            req.getHeaderNames().asIterator().forEachRemaining(header -> log.info("header: {}", header + "=" + req.getHeader(header)));
            String objectId = req.getParts().stream()
                    .filter(part -> part.getSize() > 0)
                    .map(elem -> {
                                try {
                                    return documentService.uploadDocument(elem.getInputStream(),
                                            extractFileNameFromHeader.apply(elem));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
                    .collect(Collectors.joining());

            log.info("object id: {}", objectId);
            pout.write("objectId: " + objectId);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }


}
