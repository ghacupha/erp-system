package io.github.erp.erp.resources.prepayments;

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
import io.github.erp.domain.PrepaymentReportTuple;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalPrepaymentReportRepository;
import io.github.erp.internal.service.autonomousReport.DatedReportExportService;
import io.github.erp.service.PrepaymentReportQueryService;
import io.github.erp.service.PrepaymentReportService;
import io.github.erp.service.criteria.PrepaymentReportCriteria;
import io.github.erp.service.dto.PrepaymentReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * REST controller for managing {@link io.github.erp.domain.PrepaymentReport}.
 */
@RestController("PrepaymentReportResourceProd")
@RequestMapping("/api/prepayments")
public class PrepaymentReportResourceProd {

    private final static String REPORT_NAME = "prepayments-outstanding-report";

    private final Logger log = LoggerFactory.getLogger(PrepaymentReportResourceProd.class);

    private final PrepaymentReportService prepaymentReportService;

    private final PrepaymentReportQueryService prepaymentReportQueryService;

    private final InternalPrepaymentReportRepository internalPrepaymentReportRepository;

    private final DatedReportExportService<PrepaymentReportDTO> prepaymentReportExportService;

    private final Mapping<PrepaymentReportTuple, PrepaymentReportDTO> prepaymentReportDTOMapping;

    public PrepaymentReportResourceProd(
        PrepaymentReportService prepaymentReportService,
        PrepaymentReportQueryService prepaymentReportQueryService,
        InternalPrepaymentReportRepository internalPrepaymentReportRepository,
        DatedReportExportService<PrepaymentReportDTO> prepaymentReportExportService,
        Mapping<PrepaymentReportTuple, PrepaymentReportDTO> prepaymentReportDTOMapping) {
        this.prepaymentReportService = prepaymentReportService;
        this.prepaymentReportQueryService = prepaymentReportQueryService;
        this.internalPrepaymentReportRepository = internalPrepaymentReportRepository;
        this.prepaymentReportExportService = prepaymentReportExportService;
        this.prepaymentReportDTOMapping = prepaymentReportDTOMapping;
    }

    /**
     * {@code GET  /prepayment-reports} : get all the prepaymentReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentReports in body.
     */
    @GetMapping("/prepayment-reports")
    public ResponseEntity<List<PrepaymentReportDTO>> getAllPrepaymentReports(PrepaymentReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PrepaymentReports by criteria: {}", criteria);
        Page<PrepaymentReportDTO> page = prepaymentReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-reports} : get all the prepaymentReports.
     *
     * @param pageable the pagination information.
     * @param reportDate the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentReports in body.
     */
    @GetMapping("/prepayment-reports/reported")
    public ResponseEntity<List<PrepaymentReportDTO>> getAllPrepaymentReportsByReportDate(@RequestParam("reportDate") String reportDate, Pageable pageable) {
        log.debug("REST request to get PrepaymentReports by criteria: {}", reportDate);

        Page<PrepaymentReportDTO> page =
            internalPrepaymentReportRepository.findAllByReportDate(LocalDate.parse(reportDate), pageable)
            .map(prepaymentReportDTOMapping::toValue2);

        exportCSVReport(LocalDate.parse(reportDate));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Async
    void exportCSVReport(LocalDate reportDate) {

        try {
            // TODO Persist autonomous report details
            prepaymentReportExportService.exportReportByDate(reportDate, REPORT_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static PrepaymentReportDTO mapPrepaymentReport(PrepaymentReportTuple prepaymentReportTuple) {
//
//        PrepaymentReportDTO report = new PrepaymentReportDTO();
//        report.setId(prepaymentReportTuple.getId());
//        report.setCatalogueNumber(prepaymentReportTuple.getCatalogueNumber());
//        report.setParticulars(prepaymentReportTuple.getParticulars());
//        report.setDealerName(prepaymentReportTuple.getDealerName());
//        report.setPaymentNumber(prepaymentReportTuple.getPaymentNumber());
//        report.setPaymentDate(prepaymentReportTuple.getPaymentDate());
//        report.setCurrencyCode(prepaymentReportTuple.getCurrencyCode());
//        report.setPrepaymentAmount(prepaymentReportTuple.getPrepaymentAmount());
//        report.setAmortisedAmount(prepaymentReportTuple.getAmortisedAmount());
//        report.setOutstandingAmount(prepaymentReportTuple.getOutstandingAmount());
//
//        return report;
//    }

    /**
     * {@code GET  /prepayment-reports/count} : count all the prepaymentReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-reports/count")
    public ResponseEntity<Long> countPrepaymentReports(PrepaymentReportCriteria criteria) {
        log.debug("REST request to count PrepaymentReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-reports/:id} : get the "id" prepaymentReport.
     *
     * @param id the id of the prepaymentReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-reports/reported/{id}")
    public ResponseEntity<PrepaymentReportDTO> getPrepaymentReportByReportDate(@RequestParam("reportDate") String reportDate, @PathVariable Long id) {
        log.debug("REST request to get PrepaymentReport : {}, dated: {}", id, reportDate);
        Optional<PrepaymentReportDTO> prepaymentReportDTO =
            internalPrepaymentReportRepository.findOneByReportDate(LocalDate.parse(reportDate), id)
                .map(prepaymentReportDTOMapping::toValue2);

        return ResponseUtil.wrapOrNotFound(prepaymentReportDTO);
    }

    /**
     * {@code GET  /prepayment-reports/:id} : get the "id" prepaymentReport.
     *
     * @param id the id of the prepaymentReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-reports/{id}")
    public ResponseEntity<PrepaymentReportDTO> getPrepaymentReport(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentReport : {}", id);
        Optional<PrepaymentReportDTO> prepaymentReportDTO = prepaymentReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentReportDTO);
    }

    /**
     * {@code SEARCH  /_search/prepayment-reports?query=:query} : search for the prepaymentReport corresponding
     * to the query.
     *
     * @param query the query of the prepaymentReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-reports")
    public ResponseEntity<List<PrepaymentReportDTO>> searchPrepaymentReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrepaymentReports for query {}", query);
        Page<PrepaymentReportDTO> page = prepaymentReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
