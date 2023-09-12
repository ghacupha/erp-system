package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.repository.ShareHoldingFlagRepository;
import io.github.erp.service.ShareHoldingFlagQueryService;
import io.github.erp.service.ShareHoldingFlagService;
import io.github.erp.service.criteria.ShareHoldingFlagCriteria;
import io.github.erp.service.dto.ShareHoldingFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ShareHoldingFlag}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class ShareHoldingFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(ShareHoldingFlagResourceProd.class);

    private static final String ENTITY_NAME = "shareHoldingFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShareHoldingFlagService shareHoldingFlagService;

    private final ShareHoldingFlagRepository shareHoldingFlagRepository;

    private final ShareHoldingFlagQueryService shareHoldingFlagQueryService;

    public ShareHoldingFlagResourceProd(
        ShareHoldingFlagService shareHoldingFlagService,
        ShareHoldingFlagRepository shareHoldingFlagRepository,
        ShareHoldingFlagQueryService shareHoldingFlagQueryService
    ) {
        this.shareHoldingFlagService = shareHoldingFlagService;
        this.shareHoldingFlagRepository = shareHoldingFlagRepository;
        this.shareHoldingFlagQueryService = shareHoldingFlagQueryService;
    }

    /**
     * {@code POST  /share-holding-flags} : Create a new shareHoldingFlag.
     *
     * @param shareHoldingFlagDTO the shareHoldingFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shareHoldingFlagDTO, or with status {@code 400 (Bad Request)} if the shareHoldingFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/share-holding-flags")
    public ResponseEntity<ShareHoldingFlagDTO> createShareHoldingFlag(@Valid @RequestBody ShareHoldingFlagDTO shareHoldingFlagDTO)
        throws URISyntaxException {
        log.debug("REST request to save ShareHoldingFlag : {}", shareHoldingFlagDTO);
        if (shareHoldingFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new shareHoldingFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShareHoldingFlagDTO result = shareHoldingFlagService.save(shareHoldingFlagDTO);
        return ResponseEntity
            .created(new URI("/api/share-holding-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /share-holding-flags/:id} : Updates an existing shareHoldingFlag.
     *
     * @param id the id of the shareHoldingFlagDTO to save.
     * @param shareHoldingFlagDTO the shareHoldingFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareHoldingFlagDTO,
     * or with status {@code 400 (Bad Request)} if the shareHoldingFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shareHoldingFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/share-holding-flags/{id}")
    public ResponseEntity<ShareHoldingFlagDTO> updateShareHoldingFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShareHoldingFlagDTO shareHoldingFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShareHoldingFlag : {}, {}", id, shareHoldingFlagDTO);
        if (shareHoldingFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareHoldingFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareHoldingFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShareHoldingFlagDTO result = shareHoldingFlagService.save(shareHoldingFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareHoldingFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /share-holding-flags/:id} : Partial updates given fields of an existing shareHoldingFlag, field will ignore if it is null
     *
     * @param id the id of the shareHoldingFlagDTO to save.
     * @param shareHoldingFlagDTO the shareHoldingFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareHoldingFlagDTO,
     * or with status {@code 400 (Bad Request)} if the shareHoldingFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shareHoldingFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shareHoldingFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/share-holding-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShareHoldingFlagDTO> partialUpdateShareHoldingFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShareHoldingFlagDTO shareHoldingFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShareHoldingFlag partially : {}, {}", id, shareHoldingFlagDTO);
        if (shareHoldingFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareHoldingFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareHoldingFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShareHoldingFlagDTO> result = shareHoldingFlagService.partialUpdate(shareHoldingFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareHoldingFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /share-holding-flags} : get all the shareHoldingFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shareHoldingFlags in body.
     */
    @GetMapping("/share-holding-flags")
    public ResponseEntity<List<ShareHoldingFlagDTO>> getAllShareHoldingFlags(ShareHoldingFlagCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShareHoldingFlags by criteria: {}", criteria);
        Page<ShareHoldingFlagDTO> page = shareHoldingFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /share-holding-flags/count} : count all the shareHoldingFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/share-holding-flags/count")
    public ResponseEntity<Long> countShareHoldingFlags(ShareHoldingFlagCriteria criteria) {
        log.debug("REST request to count ShareHoldingFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(shareHoldingFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /share-holding-flags/:id} : get the "id" shareHoldingFlag.
     *
     * @param id the id of the shareHoldingFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shareHoldingFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/share-holding-flags/{id}")
    public ResponseEntity<ShareHoldingFlagDTO> getShareHoldingFlag(@PathVariable Long id) {
        log.debug("REST request to get ShareHoldingFlag : {}", id);
        Optional<ShareHoldingFlagDTO> shareHoldingFlagDTO = shareHoldingFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shareHoldingFlagDTO);
    }

    /**
     * {@code DELETE  /share-holding-flags/:id} : delete the "id" shareHoldingFlag.
     *
     * @param id the id of the shareHoldingFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/share-holding-flags/{id}")
    public ResponseEntity<Void> deleteShareHoldingFlag(@PathVariable Long id) {
        log.debug("REST request to delete ShareHoldingFlag : {}", id);
        shareHoldingFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/share-holding-flags?query=:query} : search for the shareHoldingFlag corresponding
     * to the query.
     *
     * @param query the query of the shareHoldingFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/share-holding-flags")
    public ResponseEntity<List<ShareHoldingFlagDTO>> searchShareHoldingFlags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ShareHoldingFlags for query {}", query);
        Page<ShareHoldingFlagDTO> page = shareHoldingFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
