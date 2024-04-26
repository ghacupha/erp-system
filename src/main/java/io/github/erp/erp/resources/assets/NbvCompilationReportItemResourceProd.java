package io.github.erp.erp.resources.assets;

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
import io.github.erp.domain.NbvCompilationReportItem;
import io.github.erp.repository.NbvCompilationReportItemRepository;
import io.github.erp.repository.search.NbvCompilationReportItemSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link NbvCompilationReportItem}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
@Transactional
public class NbvCompilationReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationReportItemResourceProd.class);

    private final NbvCompilationReportItemRepository nbvCompilationReportItemRepository;

    private final NbvCompilationReportItemSearchRepository nbvCompilationReportItemSearchRepository;

    public NbvCompilationReportItemResourceProd(
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
