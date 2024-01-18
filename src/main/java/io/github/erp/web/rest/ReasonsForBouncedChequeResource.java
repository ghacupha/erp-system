package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.ReasonsForBouncedChequeRepository;
import io.github.erp.service.ReasonsForBouncedChequeQueryService;
import io.github.erp.service.ReasonsForBouncedChequeService;
import io.github.erp.service.criteria.ReasonsForBouncedChequeCriteria;
import io.github.erp.service.dto.ReasonsForBouncedChequeDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.ReasonsForBouncedCheque}.
 */
@RestController
@RequestMapping("/api")
public class ReasonsForBouncedChequeResource {

    private final Logger log = LoggerFactory.getLogger(ReasonsForBouncedChequeResource.class);

    private static final String ENTITY_NAME = "reasonsForBouncedCheque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReasonsForBouncedChequeService reasonsForBouncedChequeService;

    private final ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository;

    private final ReasonsForBouncedChequeQueryService reasonsForBouncedChequeQueryService;

    public ReasonsForBouncedChequeResource(
        ReasonsForBouncedChequeService reasonsForBouncedChequeService,
        ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository,
        ReasonsForBouncedChequeQueryService reasonsForBouncedChequeQueryService
    ) {
        this.reasonsForBouncedChequeService = reasonsForBouncedChequeService;
        this.reasonsForBouncedChequeRepository = reasonsForBouncedChequeRepository;
        this.reasonsForBouncedChequeQueryService = reasonsForBouncedChequeQueryService;
    }

    /**
     * {@code POST  /reasons-for-bounced-cheques} : Create a new reasonsForBouncedCheque.
     *
     * @param reasonsForBouncedChequeDTO the reasonsForBouncedChequeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reasonsForBouncedChequeDTO, or with status {@code 400 (Bad Request)} if the reasonsForBouncedCheque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reasons-for-bounced-cheques")
    public ResponseEntity<ReasonsForBouncedChequeDTO> createReasonsForBouncedCheque(
        @Valid @RequestBody ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ReasonsForBouncedCheque : {}", reasonsForBouncedChequeDTO);
        if (reasonsForBouncedChequeDTO.getId() != null) {
            throw new BadRequestAlertException("A new reasonsForBouncedCheque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReasonsForBouncedChequeDTO result = reasonsForBouncedChequeService.save(reasonsForBouncedChequeDTO);
        return ResponseEntity
            .created(new URI("/api/reasons-for-bounced-cheques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reasons-for-bounced-cheques/:id} : Updates an existing reasonsForBouncedCheque.
     *
     * @param id the id of the reasonsForBouncedChequeDTO to save.
     * @param reasonsForBouncedChequeDTO the reasonsForBouncedChequeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reasonsForBouncedChequeDTO,
     * or with status {@code 400 (Bad Request)} if the reasonsForBouncedChequeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reasonsForBouncedChequeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reasons-for-bounced-cheques/{id}")
    public ResponseEntity<ReasonsForBouncedChequeDTO> updateReasonsForBouncedCheque(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReasonsForBouncedCheque : {}, {}", id, reasonsForBouncedChequeDTO);
        if (reasonsForBouncedChequeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reasonsForBouncedChequeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reasonsForBouncedChequeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReasonsForBouncedChequeDTO result = reasonsForBouncedChequeService.save(reasonsForBouncedChequeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reasonsForBouncedChequeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reasons-for-bounced-cheques/:id} : Partial updates given fields of an existing reasonsForBouncedCheque, field will ignore if it is null
     *
     * @param id the id of the reasonsForBouncedChequeDTO to save.
     * @param reasonsForBouncedChequeDTO the reasonsForBouncedChequeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reasonsForBouncedChequeDTO,
     * or with status {@code 400 (Bad Request)} if the reasonsForBouncedChequeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reasonsForBouncedChequeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reasonsForBouncedChequeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reasons-for-bounced-cheques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReasonsForBouncedChequeDTO> partialUpdateReasonsForBouncedCheque(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReasonsForBouncedCheque partially : {}, {}", id, reasonsForBouncedChequeDTO);
        if (reasonsForBouncedChequeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reasonsForBouncedChequeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reasonsForBouncedChequeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReasonsForBouncedChequeDTO> result = reasonsForBouncedChequeService.partialUpdate(reasonsForBouncedChequeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reasonsForBouncedChequeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reasons-for-bounced-cheques} : get all the reasonsForBouncedCheques.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reasonsForBouncedCheques in body.
     */
    @GetMapping("/reasons-for-bounced-cheques")
    public ResponseEntity<List<ReasonsForBouncedChequeDTO>> getAllReasonsForBouncedCheques(
        ReasonsForBouncedChequeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ReasonsForBouncedCheques by criteria: {}", criteria);
        Page<ReasonsForBouncedChequeDTO> page = reasonsForBouncedChequeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reasons-for-bounced-cheques/count} : count all the reasonsForBouncedCheques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reasons-for-bounced-cheques/count")
    public ResponseEntity<Long> countReasonsForBouncedCheques(ReasonsForBouncedChequeCriteria criteria) {
        log.debug("REST request to count ReasonsForBouncedCheques by criteria: {}", criteria);
        return ResponseEntity.ok().body(reasonsForBouncedChequeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reasons-for-bounced-cheques/:id} : get the "id" reasonsForBouncedCheque.
     *
     * @param id the id of the reasonsForBouncedChequeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reasonsForBouncedChequeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reasons-for-bounced-cheques/{id}")
    public ResponseEntity<ReasonsForBouncedChequeDTO> getReasonsForBouncedCheque(@PathVariable Long id) {
        log.debug("REST request to get ReasonsForBouncedCheque : {}", id);
        Optional<ReasonsForBouncedChequeDTO> reasonsForBouncedChequeDTO = reasonsForBouncedChequeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reasonsForBouncedChequeDTO);
    }

    /**
     * {@code DELETE  /reasons-for-bounced-cheques/:id} : delete the "id" reasonsForBouncedCheque.
     *
     * @param id the id of the reasonsForBouncedChequeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reasons-for-bounced-cheques/{id}")
    public ResponseEntity<Void> deleteReasonsForBouncedCheque(@PathVariable Long id) {
        log.debug("REST request to delete ReasonsForBouncedCheque : {}", id);
        reasonsForBouncedChequeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/reasons-for-bounced-cheques?query=:query} : search for the reasonsForBouncedCheque corresponding
     * to the query.
     *
     * @param query the query of the reasonsForBouncedCheque search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/reasons-for-bounced-cheques")
    public ResponseEntity<List<ReasonsForBouncedChequeDTO>> searchReasonsForBouncedCheques(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReasonsForBouncedCheques for query {}", query);
        Page<ReasonsForBouncedChequeDTO> page = reasonsForBouncedChequeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
