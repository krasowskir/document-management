package org.richard.home.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.richard.home.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DocumentServlet extends HttpServlet {

    private static Logger log = LoggerFactory.getLogger(DocumentServlet.class);

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
        this.documentService = (DocumentService) this.getServletContext().getAttribute("documentService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println("get method hit succesfully");
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
