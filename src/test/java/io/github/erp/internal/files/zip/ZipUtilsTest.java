package io.github.erp.internal.files.zip;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilsTest {

    @Test
    void zipFile() throws Exception {

        //File file = getFile("files/invoice-zip-test.xlsx");

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("files/invoice-zip-test.xlsx").getFile());

        ZipUtils.zipFile(
            "src/test/resources/files/invoice-zip-test.zip",
            "zipped-invoice",
            file
        );

        // File zippedFile = getFile("files/invoice-zip-test.zip");

        File zippedFile = new File(classLoader.getResource("files/invoice-zip-test.zip").getFile());

        // TODO assertTrue(zippedFile.getAbsolutePath().endsWith("files/invoice-zip-test.zip"));

    }

    @NotNull
    private File getFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }
}
