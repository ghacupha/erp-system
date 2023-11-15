package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.internal.repository.InternalWIPOutstandingReportRepository;
import io.github.erp.repository.WorkInProgressOutstandingReportRepository;
import io.github.erp.service.WorkInProgressOutstandingReportQueryService;
import io.github.erp.service.WorkInProgressOutstandingReportService;
import io.github.erp.service.criteria.WorkInProgressOutstandingReportCriteria;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link io.github.erp.domain.WorkInProgressOutstandingReport}.
 */
@RestController("WorkInProgressOutstandingReportResourceProd")
@RequestMapping("/api/fixed-asset")
public class WorkInProgressOutstandingReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOutstandingReportResourceProd.class);

    private final WorkInProgressOutstandingReportService workInProgressOutstandingReportService;

    private final WorkInProgressOutstandingReportRepository workInProgressOutstandingReportRepository;

    private final WorkInProgressOutstandingReportQueryService workInProgressOutstandingReportQueryService;

    private final InternalWIPOutstandingReportRepository internalWIPOutstandingReportRepository;

    private final WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper;

    public WorkInProgressOutstandingReportResourceProd(
        WorkInProgressOutstandingReportService workInProgressOutstandingReportService,
        WorkInProgressOutstandingReportRepository workInProgressOutstandingReportRepository,
        WorkInProgressOutstandingReportQueryService workInProgressOutstandingReportQueryService,
        InternalWIPOutstandingReportRepository internalWIPOutstandingReportRepository, WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper) {
        this.workInProgressOutstandingReportService = workInProgressOutstandingReportService;
        this.workInProgressOutstandingReportRepository = workInProgressOutstandingReportRepository;
        this.workInProgressOutstandingReportQueryService = workInProgressOutstandingReportQueryService;
        this.internalWIPOutstandingReportRepository = internalWIPOutstandingReportRepository;
        this.workInProgressOutstandingReportMapper = workInProgressOutstandingReportMapper;
    }

    /**
     * {@code GET  /work-in-progress-outstanding-reports} : get all the workInProgressOutstandingReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressOutstandingReports in body.
     */
    @GetMapping("/work-in-progress-outstanding-reports")
    public ResponseEntity<List<WorkInProgressOutstandingReportDTO>> getAllWorkInProgressOutstandingReports(
        WorkInProgressOutstandingReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkInProgressOutstandingReports by criteria: {}", criteria);
        Page<WorkInProgressOutstandingReportDTO> page = workInProgressOutstandingReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-outstanding-reports} : get all the workInProgressOutstandingReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressOutstandingReports in body.
     */
    @GetMapping("/work-in-progress-outstanding-reports/reported")
    public ResponseEntity<List<WorkInProgressOutstandingReportDTO>> getAllWorkInProgressOutstandingReportsByReportDate(
        @RequestParam("reportDate") String reportDate,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkInProgressOutstandingReports by criteria, report-date: {}", reportDate);

        Page<WorkInProgressOutstandingReportDTO> page =
            internalWIPOutstandingReportRepository.findByReportDate(LocalDate.parse(reportDate), pageable)
                .map(WorkInProgressOutstandingReportResourceProd::convertToDTO);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    private static WorkInProgressOutstandingReportDTO convertToDTO(WorkInProgressOutstandingReportREPO repo) {
        WorkInProgressOutstandingReportDTO dto = new WorkInProgressOutstandingReportDTO();

        dto.setId(repo.getId());
        dto.setSequenceNumber(repo.getSequenceNumber());
        dto.setParticulars(repo.getParticulars());
        dto.setDealerName(repo.getDealerName());
        dto.setIso4217Code(repo.getIso4217Code());
        dto.setInstalmentAmount(repo.getInstalmentAmount());
        dto.setTotalTransferAmount(repo.getTotalTransferAmount());
        dto.setOutstandingAmount(repo.getOutstandingAmount());

        return dto;
    }

    /**
     * {@code GET  /work-in-progress-outstanding-reports/count} : count all the workInProgressOutstandingReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/work-in-progress-outstanding-reports/count")
    public ResponseEntity<Long> countWorkInProgressOutstandingReports(WorkInProgressOutstandingReportCriteria criteria) {
        log.debug("REST request to count WorkInProgressOutstandingReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(workInProgressOutstandingReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /work-in-progress-outstanding-reports/:id} : get the "id" workInProgressOutstandingReport.
     *
     * @param id the id of the workInProgressOutstandingReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressOutstandingReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-outstanding-reports/{id}")
    public ResponseEntity<WorkInProgressOutstandingReportDTO> getWorkInProgressOutstandingReport(@PathVariable Long id) {
        log.debug("REST request to get WorkInProgressOutstandingReport : {}", id);
        Optional<WorkInProgressOutstandingReportDTO> workInProgressOutstandingReportDTO = workInProgressOutstandingReportService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(workInProgressOutstandingReportDTO);
    }

    /**
     * {@code GET  /work-in-progress-outstanding-reports/:id} : get the "id" workInProgressOutstandingReport.
     *
     * @param id the id of the workInProgressOutstandingReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressOutstandingReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-outstanding-reports/reported/{id}")
    public ResponseEntity<WorkInProgressOutstandingReportDTO> getWorkInProgressOutstandingReportByDate(
        @RequestParam("reportDate") String reportDate,
        @PathVariable Long id) {
        log.debug("REST request to get WorkInProgressOutstandingReport : {}", id);

        Optional<WorkInProgressOutstandingReportDTO> workInProgressOutstandingReportDTO =
            internalWIPOutstandingReportRepository.findByReportDate(LocalDate.parse(reportDate), id)
            .map(WorkInProgressOutstandingReportResourceProd::convertToDTO);


        return ResponseUtil.wrapOrNotFound(workInProgressOutstandingReportDTO);
    }

    /**
     * {@code SEARCH  /_search/work-in-progress-outstanding-reports?query=:query} : search for the workInProgressOutstandingReport corresponding
     * to the query.
     *
     * @param query the query of the workInProgressOutstandingReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-in-progress-outstanding-reports")
    public ResponseEntity<List<WorkInProgressOutstandingReportDTO>> searchWorkInProgressOutstandingReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of WorkInProgressOutstandingReports for query {}", query);
        Page<WorkInProgressOutstandingReportDTO> page = workInProgressOutstandingReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
