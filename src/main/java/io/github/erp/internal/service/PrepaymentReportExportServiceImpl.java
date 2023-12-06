package io.github.erp.internal.service;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

import io.github.erp.domain.PrepaymentReportTuple;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.repository.InternalPrepaymentReportRepository;
import io.github.erp.service.dto.PrepaymentReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

@Transactional
@Service("prepaymentReportExportService")
public class PrepaymentReportExportServiceImpl extends ReportListCSVExportService<PrepaymentReportDTO> implements DatedReportExportService {

    private final InternalPrepaymentReportRepository prepaymentReportRepository;

    public PrepaymentReportExportServiceImpl(ReportsProperties reportsProperties, InternalPrepaymentReportRepository prepaymentReportRepository) {
        super(reportsProperties);
        this.prepaymentReportRepository = prepaymentReportRepository;
    }

    @Override
    public void findAllByReportDate(LocalDate reportDate, String reportName) throws IOException {

        Page<PrepaymentReportDTO> result = prepaymentReportRepository.findAllByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE))
            .map(PrepaymentReportExportServiceImpl::mapPrepaymentReport);

        super.exportToCSVFile(reportName, result.getContent());

    }

    private static PrepaymentReportDTO mapPrepaymentReport(PrepaymentReportTuple prepaymentReportTuple) {

        PrepaymentReportDTO report = new PrepaymentReportDTO();
        report.setId(prepaymentReportTuple.getId());
        report.setCatalogueNumber(prepaymentReportTuple.getCatalogueNumber());
        report.setParticulars(prepaymentReportTuple.getParticulars());
        report.setDealerName(prepaymentReportTuple.getDealerName());
        report.setPaymentNumber(prepaymentReportTuple.getPaymentNumber());
        report.setPaymentDate(prepaymentReportTuple.getPaymentDate());
        report.setCurrencyCode(prepaymentReportTuple.getCurrencyCode());
        report.setPrepaymentAmount(prepaymentReportTuple.getPrepaymentAmount());
        report.setAmortisedAmount(prepaymentReportTuple.getAmortisedAmount());
        report.setOutstandingAmount(prepaymentReportTuple.getOutstandingAmount());

        return report;
    }
}
