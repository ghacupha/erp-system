package io.github.erp.internal.report;

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
import io.github.erp.internal.files.FileStorageService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * This service creates a CSV file given a list of items and a file name
 * @param <T>
 */
public abstract class AbstractReportListCSVExportService<T> {

    private final ReportsProperties reportsProperties;
    private final FileStorageService fileStorageService;

    public AbstractReportListCSVExportService(ReportsProperties reportsProperties, FileStorageService fileStorageService) {
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

    protected String getReportParameters() {
        StringBuilder parameters = new StringBuilder();

        if (getPeriodCode() != null) {
            parameters.append("Period: ").append(getPeriodCode()).append(";   ");
        }

        if (getAssetCategoryName() != null && !getAssetCategoryName().isEmpty()) {
            parameters.append("Category: ").append(getAssetCategoryName()).append(";   ");
        }

        if (getOutletCode() != null && !getOutletCode().isEmpty()) {
            parameters.append("Outlet Code: ").append(getOutletCode()).append("; ");
        }

        return parameters.toString();
    }


    protected abstract String getOutletCode();

    protected abstract String getAssetCategoryName();

    protected abstract String getPeriodCode();

}
