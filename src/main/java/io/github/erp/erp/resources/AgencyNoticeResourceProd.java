package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.repository.AgencyNoticeRepository;
import io.github.erp.service.AgencyNoticeQueryService;
import io.github.erp.service.AgencyNoticeService;
import io.github.erp.service.criteria.AgencyNoticeCriteria;
import io.github.erp.service.dto.AgencyNoticeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AgencyNotice}.
 */
@RestController("agencyNoticeResourceProd")
@RequestMapping("/api/taxes")
public class AgencyNoticeResourceProd {

    private final Logger log = LoggerFactory.getLogger(AgencyNoticeResourceProd.class);

    private static final String ENTITY_NAME = "agencyNotice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgencyNoticeService agencyNoticeService;

    private final AgencyNoticeRepository agencyNoticeRepository;

    private final AgencyNoticeQueryService agencyNoticeQueryService;

    public AgencyNoticeResourceProd(
        AgencyNoticeService agencyNoticeService,
        AgencyNoticeRepository agencyNoticeRepository,
        AgencyNoticeQueryService agencyNoticeQueryService
    ) {
        this.agencyNoticeService = agencyNoticeService;
        this.agencyNoticeRepository = agencyNoticeRepository;
        this.agencyNoticeQueryService = agencyNoticeQueryService;
    }

    /**
     * {@code POST  /agency-notices} : Create a new agencyNotice.
     *
     * @param agencyNoticeDTO the agencyNoticeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agencyNoticeDTO, or with status {@code 400 (Bad Request)} if the agencyNotice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agency-notices")
    public ResponseEntity<AgencyNoticeDTO> createAgencyNotice(@Valid @RequestBody AgencyNoticeDTO agencyNoticeDTO)
        throws URISyntaxException {
        log.debug("REST request to save AgencyNotice : {}", agencyNoticeDTO);
        if (agencyNoticeDTO.getId() != null) {
            throw new BadRequestAlertException("A new agencyNotice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgencyNoticeDTO result = agencyNoticeService.save(agencyNoticeDTO);
        return ResponseEntity
            .created(new URI("/api/agency-notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agency-notices/:id} : Updates an existing agencyNotice.
     *
     * @param id the id of the agencyNoticeDTO to save.
     * @param agencyNoticeDTO the agencyNoticeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyNoticeDTO,
     * or with status {@code 400 (Bad Request)} if the agencyNoticeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agencyNoticeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agency-notices/{id}")
    public ResponseEntity<AgencyNoticeDTO> updateAgencyNotice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgencyNoticeDTO agencyNoticeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AgencyNotice : {}, {}", id, agencyNoticeDTO);
        if (agencyNoticeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyNoticeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyNoticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AgencyNoticeDTO result = agencyNoticeService.save(agencyNoticeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agencyNoticeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agency-notices/:id} : Partial updates given fields of an existing agencyNotice, field will ignore if it is null
     *
     * @param id the id of the agencyNoticeDTO to save.
     * @param agencyNoticeDTO the agencyNoticeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agencyNoticeDTO,
     * or with status {@code 400 (Bad Request)} if the agencyNoticeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agencyNoticeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agencyNoticeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agency-notices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgencyNoticeDTO> partialUpdateAgencyNotice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgencyNoticeDTO agencyNoticeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgencyNotice partially : {}, {}", id, agencyNoticeDTO);
        if (agencyNoticeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agencyNoticeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agencyNoticeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgencyNoticeDTO> result = agencyNoticeService.partialUpdate(agencyNoticeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agencyNoticeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agency-notices} : get all the agencyNotices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencyNotices in body.
     */
    @GetMapping("/agency-notices")
    public ResponseEntity<List<AgencyNoticeDTO>> getAllAgencyNotices(AgencyNoticeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AgencyNotices by criteria: {}", criteria);
        Page<AgencyNoticeDTO> page = agencyNoticeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agency-notices/count} : count all the agencyNotices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/agency-notices/count")
    public ResponseEntity<Long> countAgencyNotices(AgencyNoticeCriteria criteria) {
        log.debug("REST request to count AgencyNotices by criteria: {}", criteria);
        return ResponseEntity.ok().body(agencyNoticeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agency-notices/:id} : get the "id" agencyNotice.
     *
     * @param id the id of the agencyNoticeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agencyNoticeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agency-notices/{id}")
    public ResponseEntity<AgencyNoticeDTO> getAgencyNotice(@PathVariable Long id) {
        log.debug("REST request to get AgencyNotice : {}", id);
        Optional<AgencyNoticeDTO> agencyNoticeDTO = agencyNoticeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agencyNoticeDTO);
    }

    /**
     * {@code DELETE  /agency-notices/:id} : delete the "id" agencyNotice.
     *
     * @param id the id of the agencyNoticeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agency-notices/{id}")
    public ResponseEntity<Void> deleteAgencyNotice(@PathVariable Long id) {
        log.debug("REST request to delete AgencyNotice : {}", id);
        agencyNoticeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/agency-notices?query=:query} : search for the agencyNotice corresponding
     * to the query.
     *
     * @param query the query of the agencyNotice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/agency-notices")
    public ResponseEntity<List<AgencyNoticeDTO>> searchAgencyNotices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AgencyNotices for query {}", query);
        Page<AgencyNoticeDTO> page = agencyNoticeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
