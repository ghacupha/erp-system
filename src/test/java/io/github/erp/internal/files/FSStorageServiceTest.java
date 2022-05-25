package io.github.erp.internal.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FSStorageServiceTest {

    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        // fileStorageService = new FSStorageService("generated-reports-test");
    }

    @Test
    public void init() {

        fileStorageService.init();
    }
}
