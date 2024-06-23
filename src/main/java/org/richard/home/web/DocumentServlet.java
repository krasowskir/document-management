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
import java.util.function.Function;
import java.util.stream.Collectors;

@MultipartConfig
@WebServlet(value = {"/api/documents/*"})
public class DocumentServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(DocumentServlet.class);

    private static final String PREFIX = "prefix";
    private static final String FILE_NAME = "fileName";
    private String fileName, prefix;
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
        prefix = req.getParameter(PREFIX);

        fileName = new String(java.util.Base64.getDecoder().decode(req.getParameter(FILE_NAME))).replaceAll("\\n", "");
        log.info("doGet hit with fileName: {} and prefix: {}", fileName, prefix);
        var document = documentService.getDocument(fileName);

        resp.setContentType("application/octet-stream");
        resp.setContentLength(document.length);
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        try (OutputStream out = resp.getOutputStream()){
            out.write(document);
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
