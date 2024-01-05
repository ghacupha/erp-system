
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

import com.hazelcast.map.IMap;
import io.github.erp.domain.PrepaymentAccountReportTuple;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalPrepaymentAccountReportRepository;
import io.github.erp.internal.service.autonomousReport.reportListExport.ReportListExportService;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@Service("prepaymentByAccountReportListCSVExportService")
public class PrepaymentByAccountReportListCSVExportService extends AbstractDatedReportExportService<PrepaymentAccountReportDTO>  implements DatedReportExportService<PrepaymentAccountReportDTO> {

    private final InternalPrepaymentAccountReportRepository internalPrepaymentAccountReportRepository;

    public final IMap<String, String> prepaymentsReportCache;

    private final Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> prepaymentAccountReportTupleMapper;

    public PrepaymentByAccountReportListCSVExportService(
        InternalPrepaymentAccountReportRepository internalPrepaymentAccountReportRepository,
        IMap<String, String> prepaymentsReportCache,
        Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> prepaymentAccountReportTupleMapper,
        ReportListExportService<PrepaymentAccountReportDTO> reportListExportService) {

        super(prepaymentsReportCache, reportListExportService);
        this.internalPrepaymentAccountReportRepository = internalPrepaymentAccountReportRepository;
        this.prepaymentsReportCache = prepaymentsReportCache;
        this.prepaymentAccountReportTupleMapper = prepaymentAccountReportTupleMapper;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {
        super.exportReportByDate(reportDate, reportName);
    }

    @Override
    protected List<PrepaymentAccountReportDTO> getReportList(LocalDate reportDate) {
        return internalPrepaymentAccountReportRepository.findAllByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE))
            .map(prepaymentAccountReportTupleMapper::toValue2)
            .getContent();
    }

    @Override
    protected String getCacheKey(LocalDate reportDate, String reportName) {
        return reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportName;
    }
}
