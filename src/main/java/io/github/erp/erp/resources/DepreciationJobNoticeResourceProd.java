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
import io.github.erp.repository.DepreciationJobNoticeRepository;
import io.github.erp.service.DepreciationJobNoticeQueryService;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.criteria.DepreciationJobNoticeCriteria;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.DepreciationJobNotice}.
 */
@RestController("depreciationJobNoticeResourceProd")
@RequestMapping("/api/fixed-asset")
public class DepreciationJobNoticeResourceProd {

    private final Logger log = LoggerFactory.getLogger(DepreciationJobNoticeResourceProd.class);

    private static final String ENTITY_NAME = "depreciationJobNoticeProd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepreciationJobNoticeService depreciationJobNoticeService;

    private final DepreciationJobNoticeRepository depreciationJobNoticeRepository;

    private final DepreciationJobNoticeQueryService depreciationJobNoticeQueryService;

    public DepreciationJobNoticeResourceProd(
        DepreciationJobNoticeService depreciationJobNoticeService,
        DepreciationJobNoticeRepository depreciationJobNoticeRepository,
        DepreciationJobNoticeQueryService depreciationJobNoticeQueryService
    ) {
        this.depreciationJobNoticeService = depreciationJobNoticeService;
        this.depreciationJobNoticeRepository = depreciationJobNoticeRepository;
        this.depreciationJobNoticeQueryService = depreciationJobNoticeQueryService;
    }

    /**
     * {@code POST  /depreciation-job-notices} : Create a new depreciationJobNotice.
     *
     * @param depreciationJobNoticeDTO the depreciationJobNoticeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depreciationJobNoticeDTO, or with status {@code 400 (Bad Request)} if the depreciationJobNotice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depreciation-job-notices")
    public ResponseEntity<DepreciationJobNoticeDTO> createDepreciationJobNotice(
        @Valid @RequestBody DepreciationJobNoticeDTO depreciationJobNoticeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DepreciationJobNotice : {}", depreciationJobNoticeDTO);
        if (depreciationJobNoticeDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciationJobNotice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationJobNoticeDTO result = depreciationJobNoticeService.save(depreciationJobNoticeDTO);
        return ResponseEntity
            .created(new URI("/api/depreciation-job-notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depreciation-job-notices/:id} : Updates an existing depreciationJobNotice.
     *
     * @param id the id of the depreciationJobNoticeDTO to save.
     * @param depreciationJobNoticeDTO the depreciationJobNoticeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationJobNoticeDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationJobNoticeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depreciationJobNoticeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depreciation-job-notices/{id}")
    public ResponseEntity<DepreciationJobNoticeDTO> updateDepreciationJobNotice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepreciationJobNoticeDTO depreciationJobNoticeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepreciationJobNotice : {}, {}", id, depreciationJobNoticeDTO);
        if (depreciationJobNoticeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationJobNoticeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationJobNoticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepreciationJobNoticeDTO result = depreciationJobNoticeService.save(depreciationJobNoticeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationJobNoticeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depreciation-job-notices/:id} : Partial updates given fields of an existing depreciationJobNotice, field will ignore if it is null
     *
     * @param id the id of the depreciationJobNoticeDTO to save.
     * @param depreciationJobNoticeDTO the depreciationJobNoticeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationJobNoticeDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationJobNoticeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depreciationJobNoticeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depreciationJobNoticeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depreciation-job-notices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepreciationJobNoticeDTO> partialUpdateDepreciationJobNotice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepreciationJobNoticeDTO depreciationJobNoticeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepreciationJobNotice partially : {}, {}", id, depreciationJobNoticeDTO);
        if (depreciationJobNoticeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationJobNoticeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationJobNoticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepreciationJobNoticeDTO> result = depreciationJobNoticeService.partialUpdate(depreciationJobNoticeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationJobNoticeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depreciation-job-notices} : get all the depreciationJobNotices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationJobNotices in body.
     */
    @GetMapping("/depreciation-job-notices")
    public ResponseEntity<List<DepreciationJobNoticeDTO>> getAllDepreciationJobNotices(
        DepreciationJobNoticeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get DepreciationJobNotices by criteria: {}", criteria);
        Page<DepreciationJobNoticeDTO> page = depreciationJobNoticeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-job-notices/count} : count all the depreciationJobNotices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-job-notices/count")
    public ResponseEntity<Long> countDepreciationJobNotices(DepreciationJobNoticeCriteria criteria) {
        log.debug("REST request to count DepreciationJobNotices by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationJobNoticeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-job-notices/:id} : get the "id" depreciationJobNotice.
     *
     * @param id the id of the depreciationJobNoticeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationJobNoticeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-job-notices/{id}")
    public ResponseEntity<DepreciationJobNoticeDTO> getDepreciationJobNotice(@PathVariable Long id) {
        log.debug("REST request to get DepreciationJobNotice : {}", id);
        Optional<DepreciationJobNoticeDTO> depreciationJobNoticeDTO = depreciationJobNoticeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationJobNoticeDTO);
    }

    /**
     * {@code DELETE  /depreciation-job-notices/:id} : delete the "id" depreciationJobNotice.
     *
     * @param id the id of the depreciationJobNoticeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depreciation-job-notices/{id}")
    public ResponseEntity<Void> deleteDepreciationJobNotice(@PathVariable Long id) {
        log.debug("REST request to delete DepreciationJobNotice : {}", id);
        depreciationJobNoticeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/depreciation-job-notices?query=:query} : search for the depreciationJobNotice corresponding
     * to the query.
     *
     * @param query the query of the depreciationJobNotice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-job-notices")
    public ResponseEntity<List<DepreciationJobNoticeDTO>> searchDepreciationJobNotices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepreciationJobNotices for query {}", query);
        Page<DepreciationJobNoticeDTO> page = depreciationJobNoticeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
