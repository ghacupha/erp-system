
/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
package io.github.erp.internal.service.autonomousReport;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service("prepaymentReportListCSVExportService")
public class PrepaymentReportListCSVExportService implements ReportListExportService<PrepaymentReportDTO> {

    private final ReportsProperties reportsProperties;
    private final FileStorageService fileStorageService;
    private final AutonomousReportService autonomousReportService;
    private final InternalApplicationUserDetailService userDetailService;
    private final ApplicationUserMapper applicationUserMapper;


    public PrepaymentReportListCSVExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService, AutonomousReportService autonomousReportService, InternalApplicationUserDetailService userDetailService, ApplicationUserMapper applicationUserMapper) {
        this.reportsProperties = reportsProperties;
        this.fileStorageService = fileStorageService;
        this.autonomousReportService = autonomousReportService;
        this.userDetailService = userDetailService;
        this.applicationUserMapper = applicationUserMapper;
    }

    public void executeReport(List<PrepaymentReportDTO> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

        ByteArrayOutputStream csvByteArray = CSVDynamicConverterService.convertToCSV(reportList);

        String reportPath = reportsProperties.getReportsDirectory().concat("/").concat(fileName).concat(".csv");

        try (FileOutputStream fileOutputStream = new FileOutputStream(reportPath)) {
            csvByteArray.writeTo(fileOutputStream);
        }

        String fileChecksum = fileStorageService.calculateSha512CheckSum(fileName + ".csv");

            AutonomousReportDTO autoReport = new AutonomousReportDTO();
            autoReport.setReportName(reportName);
            autoReport.setReportParameters("Report Date: " + reportDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            autoReport.setCreatedAt(ZonedDateTime.now());
            autoReport.setReportFilename(UUID.fromString(fileName));
            autoReport.setReportFileContentType("text/csv");
            autoReport.setCreatedBy(getCreatedBy());
            autoReport.setFileChecksum(fileChecksum);
            autoReport.setReportTampered(false);
            // Save report
            autonomousReportService.save(autoReport);
    }

    protected ApplicationUserDTO getCreatedBy() {
        return applicationUserMapper.toDto(userDetailService.getCurrentApplicationUser().get());
    }

}
