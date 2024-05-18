package org.richard.home;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.richard.home.config.AppConfig;
import org.richard.home.config.GeneralConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;

@WebListener
public class SpringInitializer implements ServletContextListener, WebApplicationInitializer {

    private static Logger log = LoggerFactory.getLogger(SpringInitializer.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("richard!!! context initialized");
        onStartup(sce.getServletContext());
        /*
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setConfigLocation("org.richard.home");
        applicationContext.register(GeneralConfiguration.class);
        applicationContext.register(AppConfig.class);
        applicationContext.refresh();

        WebAppContext context = new WebAppContext();
        context.addEventListener(new ContextLoaderListener(applicationContext));
        log.info("spring initialized: {}", applicationContext);
         */
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(servletContext);
        servletContext.setAttribute("applicationContext", applicationContext);
        applicationContext.register(AppConfig.class);
        applicationContext.register(GeneralConfiguration.class);
        applicationContext.refresh();
        log.info("spring initialized: {}", Arrays.stream(applicationContext.getBeanDefinitionNames()).anyMatch(name -> name.equals("appConfig")));
    }
}
