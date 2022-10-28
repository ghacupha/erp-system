package io.github.erp.internal.files;

/*-
 * Erp System - Mark III No 3 (Caleb Series) Server ver 0.1.3-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
