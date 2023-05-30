package com.nagarro.advanced.framework.reader;

import com.nagarro.advanced.framework.config.ProjectConfig;
import com.nagarro.advanced.framework.exception.ResourceFileNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileResourcesReaderTest {

    private static FileResourcesReader fileReader;
    private static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void setUp() {
        fileReader = new FileResourcesReader();
        context = new AnnotationConfigApplicationContext(ProjectConfig.class);
    }

    @Test
    void shouldReturnTheContentOfFileWhenFileExist() {
        //GIVEN
        String actualContent = "Bestseller from the writer Katharine McGee!";

        //WHEN
        String expectedContent = fileReader.readDataFromResourcesFolder(context, "book.txt");

        //THEN
        assertEquals(actualContent, expectedContent);
    }

    @Test
    void shouldThrowResourceFileNotFoundExceptionWhenFileDoesntExist() {
        assertThrows(ResourceFileNotFoundException.class, () ->
                fileReader.readDataFromResourcesFolder(context, "notExist.txt"));
    }
}
