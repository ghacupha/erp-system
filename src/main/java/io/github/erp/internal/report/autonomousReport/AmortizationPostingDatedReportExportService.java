
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
package io.github.erp.internal.report.autonomousReport;

import com.hazelcast.map.IMap;
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalAmortizationPostingRepository;
import io.github.erp.internal.report.autonomousReport.reportListExport.ReportListExportService;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AmortizationPostingDatedReportExportService extends AbstractDatedReportExportService<AmortizationPostingReportDTO> implements DatedReportExportService<AmortizationPostingReportDTO> {


    private final InternalAmortizationPostingRepository internalAmortizationPostingRepository;
    private final Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> dtoMapping;

    public AmortizationPostingDatedReportExportService(
        InternalAmortizationPostingRepository internalAmortizationPostingRepository,
        IMap<String, String> prepaymentsReportCache,
        ReportListExportService<AmortizationPostingReportDTO> reportListExportService,
        Mapping<AmortizationPostingReportInternal,
            AmortizationPostingReportDTO> dtoMapping ) {

        super(prepaymentsReportCache, reportListExportService);

        this.internalAmortizationPostingRepository = internalAmortizationPostingRepository;
        this.dtoMapping = dtoMapping;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {
        super.exportReportByDate(reportDate, reportName);
    }

    @Override
    protected List<AmortizationPostingReportDTO> getReportList(LocalDate reportDate) {
        return internalAmortizationPostingRepository.findByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE))
            .map(dtoMapping::toValue2)
            .getContent();
    }

    @Override
    protected String getCacheKey(LocalDate reportDate, String reportName) {
        return reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportName;
    }

}
