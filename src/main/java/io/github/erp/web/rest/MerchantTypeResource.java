package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.repository.MerchantTypeRepository;
import io.github.erp.service.MerchantTypeQueryService;
import io.github.erp.service.MerchantTypeService;
import io.github.erp.service.criteria.MerchantTypeCriteria;
import io.github.erp.service.dto.MerchantTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.MerchantType}.
 */
@RestController
@RequestMapping("/api")
public class MerchantTypeResource {

    private final Logger log = LoggerFactory.getLogger(MerchantTypeResource.class);

    private static final String ENTITY_NAME = "merchantType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MerchantTypeService merchantTypeService;

    private final MerchantTypeRepository merchantTypeRepository;

    private final MerchantTypeQueryService merchantTypeQueryService;

    public MerchantTypeResource(
        MerchantTypeService merchantTypeService,
        MerchantTypeRepository merchantTypeRepository,
        MerchantTypeQueryService merchantTypeQueryService
    ) {
        this.merchantTypeService = merchantTypeService;
        this.merchantTypeRepository = merchantTypeRepository;
        this.merchantTypeQueryService = merchantTypeQueryService;
    }

    /**
     * {@code POST  /merchant-types} : Create a new merchantType.
     *
     * @param merchantTypeDTO the merchantTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new merchantTypeDTO, or with status {@code 400 (Bad Request)} if the merchantType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/merchant-types")
    public ResponseEntity<MerchantTypeDTO> createMerchantType(@Valid @RequestBody MerchantTypeDTO merchantTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save MerchantType : {}", merchantTypeDTO);
        if (merchantTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new merchantType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MerchantTypeDTO result = merchantTypeService.save(merchantTypeDTO);
        return ResponseEntity
            .created(new URI("/api/merchant-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /merchant-types/:id} : Updates an existing merchantType.
     *
     * @param id the id of the merchantTypeDTO to save.
     * @param merchantTypeDTO the merchantTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchantTypeDTO,
     * or with status {@code 400 (Bad Request)} if the merchantTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the merchantTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/merchant-types/{id}")
    public ResponseEntity<MerchantTypeDTO> updateMerchantType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MerchantTypeDTO merchantTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MerchantType : {}, {}", id, merchantTypeDTO);
        if (merchantTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchantTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MerchantTypeDTO result = merchantTypeService.save(merchantTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, merchantTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /merchant-types/:id} : Partial updates given fields of an existing merchantType, field will ignore if it is null
     *
     * @param id the id of the merchantTypeDTO to save.
     * @param merchantTypeDTO the merchantTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchantTypeDTO,
     * or with status {@code 400 (Bad Request)} if the merchantTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the merchantTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the merchantTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/merchant-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MerchantTypeDTO> partialUpdateMerchantType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MerchantTypeDTO merchantTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MerchantType partially : {}, {}", id, merchantTypeDTO);
        if (merchantTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchantTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MerchantTypeDTO> result = merchantTypeService.partialUpdate(merchantTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, merchantTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /merchant-types} : get all the merchantTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of merchantTypes in body.
     */
    @GetMapping("/merchant-types")
    public ResponseEntity<List<MerchantTypeDTO>> getAllMerchantTypes(MerchantTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MerchantTypes by criteria: {}", criteria);
        Page<MerchantTypeDTO> page = merchantTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /merchant-types/count} : count all the merchantTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/merchant-types/count")
    public ResponseEntity<Long> countMerchantTypes(MerchantTypeCriteria criteria) {
        log.debug("REST request to count MerchantTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(merchantTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /merchant-types/:id} : get the "id" merchantType.
     *
     * @param id the id of the merchantTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the merchantTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/merchant-types/{id}")
    public ResponseEntity<MerchantTypeDTO> getMerchantType(@PathVariable Long id) {
        log.debug("REST request to get MerchantType : {}", id);
        Optional<MerchantTypeDTO> merchantTypeDTO = merchantTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(merchantTypeDTO);
    }

    /**
     * {@code DELETE  /merchant-types/:id} : delete the "id" merchantType.
     *
     * @param id the id of the merchantTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/merchant-types/{id}")
    public ResponseEntity<Void> deleteMerchantType(@PathVariable Long id) {
        log.debug("REST request to delete MerchantType : {}", id);
        merchantTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/merchant-types?query=:query} : search for the merchantType corresponding
     * to the query.
     *
     * @param query the query of the merchantType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/merchant-types")
    public ResponseEntity<List<MerchantTypeDTO>> searchMerchantTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MerchantTypes for query {}", query);
        Page<MerchantTypeDTO> page = merchantTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
