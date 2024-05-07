package io.github.erp.internal.report;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.internal.files.FileStorageService;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * This service receives a list of items and the intended filename and creates a
 * csv report and files it in the report-directory with the appropriate filename
 * @param <T> Type of data in the list for export
 */
public class CSVListExportService<T> {


    private final ReportsProperties reportsProperties;
    private final FileStorageService fileStorageService;

    public CSVListExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService) {
        this.reportsProperties = reportsProperties;
        this.fileStorageService = fileStorageService;
    }

    /**
     * @param reportList List items to be exported
     * @param fileName   filename to be used on the file system
     * @throws IOException can happen
     */
    public String executeReport(List<T> reportList, String fileName) throws IOException {

        ByteArrayOutputStream csvByteArray = CSVDynamicConverterService.convertToCSV(reportList);

        String reportPath = reportsProperties.getReportsDirectory().concat("/").concat(fileName).concat(".csv");

        try (FileOutputStream fileOutputStream = new FileOutputStream(reportPath)) {
            csvByteArray.writeTo(fileOutputStream);
        }

        return fileStorageService.calculateSha512CheckSum(fileName + ".csv");
    }
}
