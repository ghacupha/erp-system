
/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
package io.github.erp.internal.report.autonomousReport.reportListExport;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WIPOutstandingReportListCSVExportService extends AbstractReportListCSVExportService<WorkInProgressOutstandingReportDTO> implements ReportListExportService<WorkInProgressOutstandingReportDTO> {

    private final InternalApplicationUserDetailService userDetailService;
    private final ApplicationUserMapper applicationUserMapper;


    public WIPOutstandingReportListCSVExportService (
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        AutonomousReportService autonomousReportService,
        InternalApplicationUserDetailService userDetailService,
        ApplicationUserMapper applicationUserMapper) {

        super(reportsProperties, fileStorageService, autonomousReportService);
        this.userDetailService = userDetailService;
        this.applicationUserMapper = applicationUserMapper;
    }

    /**
     * @param reportList List items to be exported
     * @param reportDate report's date (this is the parameter for the report)
     * @param fileName   filename to be used on the file system
     * @param reportName name of the report as is to be saved on the DB
     * @throws IOException can happen
     */
    @Override
    public void  executeReport(List<WorkInProgressOutstandingReportDTO> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

        super.executeReport(reportList, reportDate, fileName, reportName);
    }

    protected ApplicationUserDTO getCreatedBy() {
        return applicationUserMapper.toDto(userDetailService.getCurrentApplicationUser().get());
    }
}
