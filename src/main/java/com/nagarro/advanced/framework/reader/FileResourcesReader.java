package com.nagarro.advanced.framework.reader;


import com.nagarro.advanced.framework.exception.ResourceFileNotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileResourcesReader {

    public String readDataFromResourcesFolder(AnnotationConfigApplicationContext context, String filePath) {
        Resource resource = context.getResource(filePath);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new ResourceFileNotFoundException("File doesn't exist!", e);
        }
    }
}