package com.nagarro.advanced.framework.util;

import com.nagarro.advanced.framework.aspect.BookServiceAspect;

import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingConfiguration {

    private final Logger logger = Logger.getLogger(LoggingConfiguration.class.getName());

    public Logger getLogger() {
        return logger;
    }

    static {
        try {
            InputStream stream = BookServiceAspect.class.getClassLoader()
                    .getResourceAsStream("logging.properties");
            LogManager.getLogManager().readConfiguration(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
