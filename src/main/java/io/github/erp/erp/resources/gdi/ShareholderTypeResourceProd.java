package io.github.erp.erp.resources.gdi;

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
import io.github.erp.repository.ShareholderTypeRepository;
import io.github.erp.service.ShareholderTypeQueryService;
import io.github.erp.service.ShareholderTypeService;
import io.github.erp.service.criteria.ShareholderTypeCriteria;
import io.github.erp.service.dto.ShareholderTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ShareholderType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class ShareholderTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(ShareholderTypeResourceProd.class);

    private static final String ENTITY_NAME = "shareholderType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShareholderTypeService shareholderTypeService;

    private final ShareholderTypeRepository shareholderTypeRepository;

    private final ShareholderTypeQueryService shareholderTypeQueryService;

    public ShareholderTypeResourceProd(
        ShareholderTypeService shareholderTypeService,
        ShareholderTypeRepository shareholderTypeRepository,
        ShareholderTypeQueryService shareholderTypeQueryService
    ) {
        this.shareholderTypeService = shareholderTypeService;
        this.shareholderTypeRepository = shareholderTypeRepository;
        this.shareholderTypeQueryService = shareholderTypeQueryService;
    }

    /**
     * {@code POST  /shareholder-types} : Create a new shareholderType.
     *
     * @param shareholderTypeDTO the shareholderTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shareholderTypeDTO, or with status {@code 400 (Bad Request)} if the shareholderType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shareholder-types")
    public ResponseEntity<ShareholderTypeDTO> createShareholderType(@Valid @RequestBody ShareholderTypeDTO shareholderTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ShareholderType : {}", shareholderTypeDTO);
        if (shareholderTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new shareholderType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShareholderTypeDTO result = shareholderTypeService.save(shareholderTypeDTO);
        return ResponseEntity
            .created(new URI("/api/shareholder-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shareholder-types/:id} : Updates an existing shareholderType.
     *
     * @param id the id of the shareholderTypeDTO to save.
     * @param shareholderTypeDTO the shareholderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareholderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the shareholderTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shareholderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shareholder-types/{id}")
    public ResponseEntity<ShareholderTypeDTO> updateShareholderType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShareholderTypeDTO shareholderTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShareholderType : {}, {}", id, shareholderTypeDTO);
        if (shareholderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareholderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareholderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShareholderTypeDTO result = shareholderTypeService.save(shareholderTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareholderTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shareholder-types/:id} : Partial updates given fields of an existing shareholderType, field will ignore if it is null
     *
     * @param id the id of the shareholderTypeDTO to save.
     * @param shareholderTypeDTO the shareholderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shareholderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the shareholderTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shareholderTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shareholderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shareholder-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShareholderTypeDTO> partialUpdateShareholderType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShareholderTypeDTO shareholderTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShareholderType partially : {}, {}", id, shareholderTypeDTO);
        if (shareholderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shareholderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shareholderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShareholderTypeDTO> result = shareholderTypeService.partialUpdate(shareholderTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shareholderTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shareholder-types} : get all the shareholderTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shareholderTypes in body.
     */
    @GetMapping("/shareholder-types")
    public ResponseEntity<List<ShareholderTypeDTO>> getAllShareholderTypes(ShareholderTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShareholderTypes by criteria: {}", criteria);
        Page<ShareholderTypeDTO> page = shareholderTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shareholder-types/count} : count all the shareholderTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/shareholder-types/count")
    public ResponseEntity<Long> countShareholderTypes(ShareholderTypeCriteria criteria) {
        log.debug("REST request to count ShareholderTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(shareholderTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shareholder-types/:id} : get the "id" shareholderType.
     *
     * @param id the id of the shareholderTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shareholderTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shareholder-types/{id}")
    public ResponseEntity<ShareholderTypeDTO> getShareholderType(@PathVariable Long id) {
        log.debug("REST request to get ShareholderType : {}", id);
        Optional<ShareholderTypeDTO> shareholderTypeDTO = shareholderTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shareholderTypeDTO);
    }

    /**
     * {@code DELETE  /shareholder-types/:id} : delete the "id" shareholderType.
     *
     * @param id the id of the shareholderTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shareholder-types/{id}")
    public ResponseEntity<Void> deleteShareholderType(@PathVariable Long id) {
        log.debug("REST request to delete ShareholderType : {}", id);
        shareholderTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/shareholder-types?query=:query} : search for the shareholderType corresponding
     * to the query.
     *
     * @param query the query of the shareholderType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/shareholder-types")
    public ResponseEntity<List<ShareholderTypeDTO>> searchShareholderTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ShareholderTypes for query {}", query);
        Page<ShareholderTypeDTO> page = shareholderTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
