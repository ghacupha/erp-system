package io.github.erp.erp.resources.prepayments;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalAmortizationPostingReportRepository;
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

    private final InternalAmortizationPostingReportRepository internalAmortizationPostingRepository;

    private final Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> amortizationPostingReportDTOMapping;

    private final DatedReportExportService<AmortizationPostingReportDTO> amortizationPostingReportExportService;

    public AmortizationPostingReportResourceProd(
            AmortizationPostingReportService amortizationPostingReportService,
            AmortizationPostingReportRepository amortizationPostingReportRepository,
            AmortizationPostingReportQueryService amortizationPostingReportQueryService,
            InternalAmortizationPostingReportRepository internalAmortizationPostingRepository,
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
