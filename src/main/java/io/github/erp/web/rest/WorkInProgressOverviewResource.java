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

import io.github.erp.domain.WorkInProgressOverview;
import io.github.erp.repository.WorkInProgressOverviewRepository;
import io.github.erp.repository.search.WorkInProgressOverviewSearchRepository;
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
 * REST controller for managing {@link io.github.erp.domain.WorkInProgressOverview}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkInProgressOverviewResource {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOverviewResource.class);

    private final WorkInProgressOverviewRepository workInProgressOverviewRepository;

    private final WorkInProgressOverviewSearchRepository workInProgressOverviewSearchRepository;

    public WorkInProgressOverviewResource(
        WorkInProgressOverviewRepository workInProgressOverviewRepository,
        WorkInProgressOverviewSearchRepository workInProgressOverviewSearchRepository
    ) {
        this.workInProgressOverviewRepository = workInProgressOverviewRepository;
        this.workInProgressOverviewSearchRepository = workInProgressOverviewSearchRepository;
    }

    /**
     * {@code GET  /work-in-progress-overviews} : get all the workInProgressOverviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressOverviews in body.
     */
    @GetMapping("/work-in-progress-overviews")
    public ResponseEntity<List<WorkInProgressOverview>> getAllWorkInProgressOverviews(Pageable pageable) {
        log.debug("REST request to get a page of WorkInProgressOverviews");
        Page<WorkInProgressOverview> page = workInProgressOverviewRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-overviews/:id} : get the "id" workInProgressOverview.
     *
     * @param id the id of the workInProgressOverview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressOverview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-overviews/{id}")
    public ResponseEntity<WorkInProgressOverview> getWorkInProgressOverview(@PathVariable Long id) {
        log.debug("REST request to get WorkInProgressOverview : {}", id);
        Optional<WorkInProgressOverview> workInProgressOverview = workInProgressOverviewRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workInProgressOverview);
    }

    /**
     * {@code SEARCH  /_search/work-in-progress-overviews?query=:query} : search for the workInProgressOverview corresponding
     * to the query.
     *
     * @param query the query of the workInProgressOverview search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-in-progress-overviews")
    public ResponseEntity<List<WorkInProgressOverview>> searchWorkInProgressOverviews(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WorkInProgressOverviews for query {}", query);
        Page<WorkInProgressOverview> page = workInProgressOverviewSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
