package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.NbvCompilationReportItem;
import io.github.erp.repository.NbvCompilationReportItemRepository;
import io.github.erp.repository.search.NbvCompilationReportItemSearchRepository;
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
 * REST controller for managing {@link io.github.erp.domain.NbvCompilationReportItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NbvCompilationReportItemResource {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationReportItemResource.class);

    private final NbvCompilationReportItemRepository nbvCompilationReportItemRepository;

    private final NbvCompilationReportItemSearchRepository nbvCompilationReportItemSearchRepository;

    public NbvCompilationReportItemResource(
        NbvCompilationReportItemRepository nbvCompilationReportItemRepository,
        NbvCompilationReportItemSearchRepository nbvCompilationReportItemSearchRepository
    ) {
        this.nbvCompilationReportItemRepository = nbvCompilationReportItemRepository;
        this.nbvCompilationReportItemSearchRepository = nbvCompilationReportItemSearchRepository;
    }

    /**
     * {@code GET  /nbv-compilation-report-items} : get all the nbvCompilationReportItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nbvCompilationReportItems in body.
     */
    @GetMapping("/nbv-compilation-report-items")
    public ResponseEntity<List<NbvCompilationReportItem>> getAllNbvCompilationReportItems(Pageable pageable) {
        log.debug("REST request to get a page of NbvCompilationReportItems");
        Page<NbvCompilationReportItem> page = nbvCompilationReportItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nbv-compilation-report-items/:id} : get the "id" nbvCompilationReportItem.
     *
     * @param id the id of the nbvCompilationReportItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nbvCompilationReportItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nbv-compilation-report-items/{id}")
    public ResponseEntity<NbvCompilationReportItem> getNbvCompilationReportItem(@PathVariable Long id) {
        log.debug("REST request to get NbvCompilationReportItem : {}", id);
        Optional<NbvCompilationReportItem> nbvCompilationReportItem = nbvCompilationReportItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nbvCompilationReportItem);
    }

    /**
     * {@code SEARCH  /_search/nbv-compilation-report-items?query=:query} : search for the nbvCompilationReportItem corresponding
     * to the query.
     *
     * @param query the query of the nbvCompilationReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nbv-compilation-report-items")
    public ResponseEntity<List<NbvCompilationReportItem>> searchNbvCompilationReportItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NbvCompilationReportItems for query {}", query);
        Page<NbvCompilationReportItem> page = nbvCompilationReportItemSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
