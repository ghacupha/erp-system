package io.github.erp.erp.resources.prepayments;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.PrepaymentOutstandingOverviewReport;
import io.github.erp.domain.PrepaymentOutstandingOverviewReportTuple;
import io.github.erp.internal.repository.InternalPrepaymentOutstandingOverviewReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

import java.time.LocalDate;
import java.util.Optional;

/**
 * REST controller for managing {@link PrepaymentOutstandingOverviewReport}.
 */
@RestController("PrepaymentOutstandingOverviewReportResourceProd")
@RequestMapping("/api/prepayments")
@Transactional
public class PrepaymentOutstandingOverviewReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(PrepaymentOutstandingOverviewReportResourceProd.class);

    private final InternalPrepaymentOutstandingOverviewReportRepository internalPrepaymentOutstandingOverviewReportRepository;

    public PrepaymentOutstandingOverviewReportResourceProd(InternalPrepaymentOutstandingOverviewReportRepository internalPrepaymentOutstandingOverviewReportRepository) {
        this.internalPrepaymentOutstandingOverviewReportRepository = internalPrepaymentOutstandingOverviewReportRepository;
    }

    /**
     * {@code GET  /prepayment-outstanding-overview-reports/:id} : get the "id" prepaymentOutstandingOverviewReport.
     *
     * @param reportDate the date of the prepaymentOutstandingOverviewReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentOutstandingOverviewReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-outstanding-overview-reports")
    public ResponseEntity<PrepaymentOutstandingOverviewReport> getPrepaymentOutstandingOverviewReportById(@RequestParam("reportDate") String reportDate) {
        log.debug("REST request to get PrepaymentOutstandingOverviewReport for date: {}", reportDate);
        Optional<PrepaymentOutstandingOverviewReport> prepaymentOutstandingOverviewReport =
            internalPrepaymentOutstandingOverviewReportRepository.findOneByReportDate(LocalDate.parse(reportDate))
            .map(PrepaymentOutstandingOverviewReportResourceProd::mapPrepaymentOutstandingOverview);

        return ResponseUtil.wrapOrNotFound(prepaymentOutstandingOverviewReport);
    }

    private static PrepaymentOutstandingOverviewReport mapPrepaymentOutstandingOverview(PrepaymentOutstandingOverviewReportTuple prepaymentOutstandingOverviewReportTuple) {
        return new PrepaymentOutstandingOverviewReport()
            .totalPrepaymentAmount(prepaymentOutstandingOverviewReportTuple.getTotalPrepaymentAmount())
            .totalAmortisedAmount(prepaymentOutstandingOverviewReportTuple.getTotalAmortisedAmount())
            .totalOutstandingAmount(prepaymentOutstandingOverviewReportTuple.getTotalOutstandingAmount())
            .numberOfPrepaymentAccounts(prepaymentOutstandingOverviewReportTuple.getNumberOfPrepaymentAccounts());
    }
}
