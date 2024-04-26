package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.WorkInProgressReport;
import io.github.erp.repository.WorkInProgressReportRepository;
import io.github.erp.repository.search.WorkInProgressReportSearchRepository;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.WorkInProgressReport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkInProgressReportResource {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressReportResource.class);

    private final WorkInProgressReportRepository workInProgressReportRepository;

    private final WorkInProgressReportSearchRepository workInProgressReportSearchRepository;

    public WorkInProgressReportResource(
        WorkInProgressReportRepository workInProgressReportRepository,
        WorkInProgressReportSearchRepository workInProgressReportSearchRepository
    ) {
        this.workInProgressReportRepository = workInProgressReportRepository;
        this.workInProgressReportSearchRepository = workInProgressReportSearchRepository;
    }

    /**
     * {@code GET  /work-in-progress-reports} : get all the workInProgressReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressReports in body.
     */
    @GetMapping("/work-in-progress-reports")
    public ResponseEntity<List<WorkInProgressReport>> getAllWorkInProgressReports(Pageable pageable) {
        log.debug("REST request to get a page of WorkInProgressReports");
        Page<WorkInProgressReport> page = workInProgressReportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-reports/:id} : get the "id" workInProgressReport.
     *
     * @param id the id of the workInProgressReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-reports/{id}")
    public ResponseEntity<WorkInProgressReport> getWorkInProgressReport(@PathVariable Long id) {
        log.debug("REST request to get WorkInProgressReport : {}", id);
        Optional<WorkInProgressReport> workInProgressReport = workInProgressReportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workInProgressReport);
    }

    /**
     * {@code SEARCH  /_search/work-in-progress-reports?query=:query} : search for the workInProgressReport corresponding
     * to the query.
     *
     * @param query the query of the workInProgressReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-in-progress-reports")
    public ResponseEntity<List<WorkInProgressReport>> searchWorkInProgressReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WorkInProgressReports for query {}", query);
        Page<WorkInProgressReport> page = workInProgressReportSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
