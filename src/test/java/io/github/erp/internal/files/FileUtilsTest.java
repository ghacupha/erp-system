package io.github.erp.internal.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;

import static io.github.erp.internal.framework.excel.ExcelTestUtil.readFile;
import static io.github.erp.internal.framework.excel.ExcelTestUtil.toBytes;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FileUtilsTest {

    private String reportbMd5 = null;
    private String reportcMd5 = null;


    @BeforeEach
    void setUp() throws IOException {
        byte[] reportb = toBytes(readFile("76436b.jrxml"));
        byte[] reportc = toBytes(readFile("76436c.jrxml"));

        String reportbMd5 = DigestUtils.md5DigestAsHex(reportb);
        String reportcMd5 = DigestUtils.md5DigestAsHex(reportc);
    }

    @Test
    void frameworkCalculatesTheSameMD5RegardlessOfFileName() {

        assertThat(reportbMd5).isEqualTo(reportcMd5);
    }

    @Test
    void fileChecksOut() {
    }
}
