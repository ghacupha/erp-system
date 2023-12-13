package io.github.erp.internal.service;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import com.hazelcast.map.IMap;
import io.github.erp.internal.report.ReportsProperties;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class ReportListCSVExportService<T, S> {

    private final ReportsProperties reportsProperties;

    public ReportListCSVExportService(ReportsProperties reportsProperties) {
        this.reportsProperties = reportsProperties;
    }

    void exportToCSVFile(String reportName, List<T> conversionList) throws IOException {
        ByteArrayOutputStream csvByteArray = CSVDynamicConverterService.convertToCSV(conversionList);

        String reportPath = reportsProperties.getReportsDirectory().concat("/").concat(reportName).concat(".csv");

        try (FileOutputStream fileOutputStream = new FileOutputStream(reportPath)) {
            csvByteArray.writeTo(fileOutputStream);
        }
    }

    public void cacheReport(LocalDate reportDate, S reportMetadata) {
        IMap<String, S> reportsCache = getHazelcastInstanceMap();
        String cacheKey = reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportMetadata;
        reportsCache.put(cacheKey, reportMetadata);
    }

    protected abstract IMap<String,S> getHazelcastInstanceMap();
}
