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

import io.github.erp.domain.NbvReportItem;
import io.github.erp.repository.NbvReportItemRepository;
import io.github.erp.repository.search.NbvReportItemSearchRepository;
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
 * REST controller for managing {@link io.github.erp.domain.NbvReportItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NbvReportItemResource {

    private final Logger log = LoggerFactory.getLogger(NbvReportItemResource.class);

    private final NbvReportItemRepository nbvReportItemRepository;

    private final NbvReportItemSearchRepository nbvReportItemSearchRepository;

    public NbvReportItemResource(
        NbvReportItemRepository nbvReportItemRepository,
        NbvReportItemSearchRepository nbvReportItemSearchRepository
    ) {
        this.nbvReportItemRepository = nbvReportItemRepository;
        this.nbvReportItemSearchRepository = nbvReportItemSearchRepository;
    }

    /**
     * {@code GET  /nbv-report-items} : get all the nbvReportItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nbvReportItems in body.
     */
    @GetMapping("/nbv-report-items")
    public ResponseEntity<List<NbvReportItem>> getAllNbvReportItems(Pageable pageable) {
        log.debug("REST request to get a page of NbvReportItems");
        Page<NbvReportItem> page = nbvReportItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nbv-report-items/:id} : get the "id" nbvReportItem.
     *
     * @param id the id of the nbvReportItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nbvReportItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nbv-report-items/{id}")
    public ResponseEntity<NbvReportItem> getNbvReportItem(@PathVariable Long id) {
        log.debug("REST request to get NbvReportItem : {}", id);
        Optional<NbvReportItem> nbvReportItem = nbvReportItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nbvReportItem);
    }

    /**
     * {@code SEARCH  /_search/nbv-report-items?query=:query} : search for the nbvReportItem corresponding
     * to the query.
     *
     * @param query the query of the nbvReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nbv-report-items")
    public ResponseEntity<List<NbvReportItem>> searchNbvReportItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NbvReportItems for query {}", query);
        Page<NbvReportItem> page = nbvReportItemSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
