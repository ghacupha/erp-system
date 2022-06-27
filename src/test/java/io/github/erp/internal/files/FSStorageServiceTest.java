package io.github.erp.internal.files;

import io.github.erp.internal.report.ReportsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FSStorageServiceTest {

    private FSStorageService storageService;

    @BeforeEach
    void setUp() {
        ReportsProperties properties = new ReportsProperties();
        properties.setReportsDirectory("src/test/resources/test-working-directory/");

        storageService = new FSStorageService(properties);
    }

    @Test
    void calculateMD5CheckSum() {

        String actualHASH = "2A808290098FD8B4FB33851392275E2B";

        String md = storageService.calculateMD5CheckSum("assetNetBookValue.xlsx");

        assertThat(md).isEqualTo(actualHASH);
    }
}
