package io.github.erp.erp.resources.prepayments;

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
