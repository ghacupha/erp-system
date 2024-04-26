package io.github.erp.internal.files;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
