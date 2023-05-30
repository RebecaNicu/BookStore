package com.nagarro.advanced.framework.aspect;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.nagarro.advanced.framework.util.LoggingConfiguration;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BookServiceAspect {

    private final Logger logger;

    public BookServiceAspect() {
        LoggingConfiguration loggingConfiguration = new LoggingConfiguration();
        logger = loggingConfiguration.getLogger();
    }

    @Around("execution(* com.nagarro.advanced.framework.service.BookService.findByIsbn(..))")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object value = null;
        try {
            value = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "After invoking getName() method. Return value=" +  value);
        return value;
    }
}