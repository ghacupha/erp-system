package io.github.erp.erp.resources.prepayments;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.service.PrepaymentMarshallingQueryService;
import io.github.erp.service.PrepaymentMarshallingService;
import io.github.erp.service.criteria.PrepaymentMarshallingCriteria;
import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.PrepaymentMarshalling}.
 */
@RestController("prepaymentMarshallingResourceProd")
@RequestMapping("/api/prepayments")
public class PrepaymentMarshallingResourceProd {

    private final Logger log = LoggerFactory.getLogger(PrepaymentMarshallingResourceProd.class);

    private static final String ENTITY_NAME = "prepaymentMarshalling";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrepaymentMarshallingService prepaymentMarshallingService;

    private final PrepaymentMarshallingRepository prepaymentMarshallingRepository;

    private final PrepaymentMarshallingQueryService prepaymentMarshallingQueryService;

    public PrepaymentMarshallingResourceProd(
        PrepaymentMarshallingService prepaymentMarshallingService,
        PrepaymentMarshallingRepository prepaymentMarshallingRepository,
        PrepaymentMarshallingQueryService prepaymentMarshallingQueryService
    ) {
        this.prepaymentMarshallingService = prepaymentMarshallingService;
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentMarshallingQueryService = prepaymentMarshallingQueryService;
    }

    /**
     * {@code POST  /prepayment-marshallings} : Create a new prepaymentMarshalling.
     *
     * @param prepaymentMarshallingDTO the prepaymentMarshallingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentMarshallingDTO, or with status {@code 400 (Bad Request)} if the prepaymentMarshalling has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-marshallings")
    public ResponseEntity<PrepaymentMarshallingDTO> createPrepaymentMarshalling(
        @Valid @RequestBody PrepaymentMarshallingDTO prepaymentMarshallingDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PrepaymentMarshalling : {}", prepaymentMarshallingDTO);
        if (prepaymentMarshallingDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepaymentMarshalling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaymentMarshallingDTO result = prepaymentMarshallingService.save(prepaymentMarshallingDTO);
        return ResponseEntity
            .created(new URI("/api/prepayment-marshallings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prepayment-marshallings/:id} : Updates an existing prepaymentMarshalling.
     *
     * @param id the id of the prepaymentMarshallingDTO to save.
     * @param prepaymentMarshallingDTO the prepaymentMarshallingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentMarshallingDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentMarshallingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentMarshallingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-marshallings/{id}")
    public ResponseEntity<PrepaymentMarshallingDTO> updatePrepaymentMarshalling(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrepaymentMarshallingDTO prepaymentMarshallingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentMarshalling : {}, {}", id, prepaymentMarshallingDTO);
        if (prepaymentMarshallingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentMarshallingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentMarshallingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentMarshallingDTO result = prepaymentMarshallingService.save(prepaymentMarshallingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentMarshallingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-marshallings/:id} : Partial updates given fields of an existing prepaymentMarshalling, field will ignore if it is null
     *
     * @param id the id of the prepaymentMarshallingDTO to save.
     * @param prepaymentMarshallingDTO the prepaymentMarshallingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentMarshallingDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentMarshallingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentMarshallingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentMarshallingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prepayment-marshallings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrepaymentMarshallingDTO> partialUpdatePrepaymentMarshalling(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrepaymentMarshallingDTO prepaymentMarshallingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrepaymentMarshalling partially : {}, {}", id, prepaymentMarshallingDTO);
        if (prepaymentMarshallingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentMarshallingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentMarshallingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentMarshallingDTO> result = prepaymentMarshallingService.partialUpdate(prepaymentMarshallingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentMarshallingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prepayment-marshallings} : get all the prepaymentMarshallings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentMarshallings in body.
     */
    @GetMapping("/prepayment-marshallings")
    public ResponseEntity<List<PrepaymentMarshallingDTO>> getAllPrepaymentMarshallings(
        PrepaymentMarshallingCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentMarshallings by criteria: {}", criteria);
        Page<PrepaymentMarshallingDTO> page = prepaymentMarshallingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-marshallings/count} : count all the prepaymentMarshallings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-marshallings/count")
    public ResponseEntity<Long> countPrepaymentMarshallings(PrepaymentMarshallingCriteria criteria) {
        log.debug("REST request to count PrepaymentMarshallings by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentMarshallingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-marshallings/:id} : get the "id" prepaymentMarshalling.
     *
     * @param id the id of the prepaymentMarshallingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentMarshallingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-marshallings/{id}")
    public ResponseEntity<PrepaymentMarshallingDTO> getPrepaymentMarshalling(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentMarshalling : {}", id);
        Optional<PrepaymentMarshallingDTO> prepaymentMarshallingDTO = prepaymentMarshallingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentMarshallingDTO);
    }

    /**
     * {@code DELETE  /prepayment-marshallings/:id} : delete the "id" prepaymentMarshalling.
     *
     * @param id the id of the prepaymentMarshallingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-marshallings/{id}")
    public ResponseEntity<Void> deletePrepaymentMarshalling(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentMarshalling : {}", id);
        prepaymentMarshallingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-marshallings?query=:query} : search for the prepaymentMarshalling corresponding
     * to the query.
     *
     * @param query the query of the prepaymentMarshalling search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-marshallings")
    public ResponseEntity<List<PrepaymentMarshallingDTO>> searchPrepaymentMarshallings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrepaymentMarshallings for query {}", query);
        Page<PrepaymentMarshallingDTO> page = prepaymentMarshallingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
