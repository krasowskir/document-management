package org.richard.home;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.richard.home.config.AppConfig;
import org.richard.home.config.GeneralConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;

@WebListener
public class SpringInitializer implements ServletContextListener {

    private static Logger log = LoggerFactory.getLogger(SpringInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("richard!!! context initialized");
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(sce.getServletContext());
        sce.getServletContext().setAttribute("applicationContext", applicationContext);
        applicationContext.register(AppConfig.class);
        applicationContext.register(GeneralConfiguration.class);
        applicationContext.refresh();
        log.info("spring initialized: {}", Arrays.stream(applicationContext.getBeanDefinitionNames()).anyMatch(name -> name.equals("appConfig")));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

}
