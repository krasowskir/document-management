package org.richard.home.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.richard.home.service.MyDummyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.IOException;

@WebServlet(value = {"/api/player/*"})
public class PlayerServlet extends HttpServlet {

    private MyDummyService myResource;
    private static final Logger log = LoggerFactory.getLogger(PlayerServlet.class);

    public PlayerServlet() {
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        log.info("service method was called...");
        super.service(req, res);
    }

    @Override
    public void destroy() {
        log.info("destroy method was called...");
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("init method was called...");
        super.init(config);
    }

    @Override
    public void init() {
        log.info("init method without args was called...");
        this.myResource = ((AnnotationConfigWebApplicationContext) this.getServletContext().getAttribute("applicationContext")).getBean(MyDummyService.class);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("real path of file in servlet context: {}", req.getServletContext().getRealPath("rich-file"));
        switch (req.getParameterNames().nextElement()) {
            case "age" -> {
                resp.getWriter().write(String.format("get age of me: %s \n", this.myResource.getAge()));
            }
            case "name" -> {
                resp.getWriter().write(String.format("get name of me: %s", myResource.getName()));
            }
        }
    }
}
