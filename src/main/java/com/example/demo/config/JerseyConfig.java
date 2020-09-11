package com.example.demo.config;

import com.example.demo.provider.MsiJacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.ws.rs.Path;

@Configuration
public class JerseyConfig extends ResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(JerseyConfig.class);

    public JerseyConfig() {
        scanAndRegisterControllers();
        register(MsiJacksonJsonProvider.class);
    }

    private void scanAndRegisterControllers() {
        final ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Path.class));

        for (BeanDefinition bd : scanner.findCandidateComponents("com.example.demo.controllers")) {
            final String controllerClassName = bd.getBeanClassName();
            try {
                final Class<?> controller = Class.forName(controllerClassName);
                register(controller);
                logger.debug("Registered controller: {}", controller);
            } catch (Exception e) {
                logger.error("Error during register controller: {}", controllerClassName);
            }
        }
    }

}
