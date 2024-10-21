package org.richard.home.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.richard.home.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@WebServlet(value = {"/api/documents/binary/*"})
public class BinaryDocumentServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(BinaryDocumentServlet.class);

    private static final String PREFIX = "prefix";
    private static final String FILE_NAME = "fileName";
    private String fileName, prefix;
    private DocumentService documentService;

    @Override
    public void init() throws ServletException {
        this.documentService = ((AnnotationConfigWebApplicationContext) this.getServletContext().getAttribute("applicationContext")).getBean(DocumentService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return super.getLastModified(req);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getHeader("Content-Type").equals("application/octet-stream")) {
            log.info("BinaryDocumentServlet post mit Content-Type: octet-stream hit!");
            log.info("doPost hit");

            String objectId = null;
            try (InputStream in = req.getInputStream()){
                objectId = documentService.uploadDocument(
                        in,
                        Objects.requireNonNull(req.getHeader("Filename")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            log.info("object id: {}", objectId);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("objectId: " + objectId);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Content-Type was not application/octet-stream");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
