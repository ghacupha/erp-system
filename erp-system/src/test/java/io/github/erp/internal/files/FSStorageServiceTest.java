package io.github.erp.internal.files;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.report.ReportsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FSStorageServiceTest {

    private ReportsFSStorageService storageService;

    @BeforeEach
    void setUp() {
        ReportsProperties properties = new ReportsProperties();
        properties.setReportsDirectory("src/test/resources/test-working-directory/");

        storageService = new ReportsFSStorageService(properties);
    }

    @Test
    void calculateMD5CheckSum() {

        String actualHASH = "2A808290098FD8B4FB33851392275E2B";

        String md = storageService.calculateMD5CheckSum("assetNetBookValue.xlsx");

        assertThat(md).isEqualTo(actualHASH);
    }
}
