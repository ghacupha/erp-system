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
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalAmortizationPostingRepository;
import io.github.erp.internal.report.autonomousReport.DatedReportExportService;
import io.github.erp.repository.AmortizationPostingReportRepository;
import io.github.erp.service.AmortizationPostingReportQueryService;
import io.github.erp.service.AmortizationPostingReportService;
import io.github.erp.service.criteria.AmortizationPostingReportCriteria;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.AmortizationPostingReport}.
 */
@RestController
@RequestMapping("/api/prepayments")
public class AmortizationPostingReportResourceProd {

    private final static String REPORT_NAME = "amortization-posting-report";

    private final Logger log = LoggerFactory.getLogger(AmortizationPostingReportResourceProd.class);

    private final AmortizationPostingReportService amortizationPostingReportService;

    private final AmortizationPostingReportRepository amortizationPostingReportRepository;

    private final AmortizationPostingReportQueryService amortizationPostingReportQueryService;

    private final InternalAmortizationPostingRepository internalAmortizationPostingRepository;

    private final Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> amortizationPostingReportDTOMapping;

    private final DatedReportExportService<AmortizationPostingReportDTO> amortizationPostingReportExportService;

    public AmortizationPostingReportResourceProd(
        AmortizationPostingReportService amortizationPostingReportService,
        AmortizationPostingReportRepository amortizationPostingReportRepository,
        AmortizationPostingReportQueryService amortizationPostingReportQueryService,
        InternalAmortizationPostingRepository internalAmortizationPostingRepository,
        Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> amortizationPostingReportDTOMapping,
        DatedReportExportService<AmortizationPostingReportDTO> amortizationPostingReportExportService) {
        this.amortizationPostingReportService = amortizationPostingReportService;
        this.amortizationPostingReportRepository = amortizationPostingReportRepository;
        this.amortizationPostingReportQueryService = amortizationPostingReportQueryService;
        this.internalAmortizationPostingRepository = internalAmortizationPostingRepository;
        this.amortizationPostingReportDTOMapping = amortizationPostingReportDTOMapping;
        this.amortizationPostingReportExportService = amortizationPostingReportExportService;
    }

    /**
     * {@code GET  /amortization-posting-reports/reported} : get all the amortizationPostingReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amortizationPostingReports in body.
     */
    @GetMapping("/amortization-posting-reports/reported")
    public ResponseEntity<List<AmortizationPostingReportDTO>> getAllAmortizationPostingReports(
        @RequestParam("reportDate") String reportDate,
        Pageable pageable
    ) throws IOException {
        log.debug("REST request to get AmortizationPostingReports by criteria: {}", reportDate);

        Page<AmortizationPostingReportDTO> page =
            internalAmortizationPostingRepository.findByReportDate(LocalDate.parse(reportDate), pageable)
            .map(amortizationPostingReportDTOMapping::toValue2);

        createCSVReport(reportDate);


        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Async void createCSVReport(String reportDate) throws IOException {

        amortizationPostingReportExportService.exportReportByDate(LocalDate.parse(reportDate), REPORT_NAME);
    }

    /**
     * {@code GET  /amortization-posting-reports} : get all the amortizationPostingReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amortizationPostingReports in body.
     */
    @GetMapping("/amortization-posting-reports")
    public ResponseEntity<List<AmortizationPostingReportDTO>> getAllAmortizationPostingReports(
        AmortizationPostingReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AmortizationPostingReports by criteria: {}", criteria);
        Page<AmortizationPostingReportDTO> page = amortizationPostingReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /amortization-posting-reports/count} : count all the amortizationPostingReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/amortization-posting-reports/count")
    public ResponseEntity<Long> countAmortizationPostingReports(AmortizationPostingReportCriteria criteria) {
        log.debug("REST request to count AmortizationPostingReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(amortizationPostingReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /amortization-posting-reports/:id} : get the "id" amortizationPostingReport.
     *
     * @param id the id of the amortizationPostingReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amortizationPostingReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amortization-posting-reports/{id}")
    public ResponseEntity<AmortizationPostingReportDTO> getAmortizationPostingReport(@PathVariable Long id) {
        log.debug("REST request to get AmortizationPostingReport : {}", id);
        Optional<AmortizationPostingReportDTO> amortizationPostingReportDTO = amortizationPostingReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amortizationPostingReportDTO);
    }

    /**
     * {@code SEARCH  /_search/amortization-posting-reports?query=:query} : search for the amortizationPostingReport corresponding
     * to the query.
     *
     * @param query the query of the amortizationPostingReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/amortization-posting-reports")
    public ResponseEntity<List<AmortizationPostingReportDTO>> searchAmortizationPostingReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of AmortizationPostingReports for query {}", query);
        Page<AmortizationPostingReportDTO> page = amortizationPostingReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
