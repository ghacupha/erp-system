package io.github.erp.erp.resources.assets;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.repository.NetBookValueEntryRepository;
import io.github.erp.service.NetBookValueEntryQueryService;
import io.github.erp.service.NetBookValueEntryService;
import io.github.erp.service.criteria.NetBookValueEntryCriteria;
import io.github.erp.service.dto.NetBookValueEntryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.NetBookValueEntry}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class NetBookValueEntryResourceProd {

    private final Logger log = LoggerFactory.getLogger(NetBookValueEntryResourceProd.class);

    private static final String ENTITY_NAME = "netBookValueEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NetBookValueEntryService netBookValueEntryService;

    private final NetBookValueEntryRepository netBookValueEntryRepository;

    private final NetBookValueEntryQueryService netBookValueEntryQueryService;

    public NetBookValueEntryResourceProd(
        NetBookValueEntryService netBookValueEntryService,
        NetBookValueEntryRepository netBookValueEntryRepository,
        NetBookValueEntryQueryService netBookValueEntryQueryService
    ) {
        this.netBookValueEntryService = netBookValueEntryService;
        this.netBookValueEntryRepository = netBookValueEntryRepository;
        this.netBookValueEntryQueryService = netBookValueEntryQueryService;
    }

    /**
     * {@code POST  /net-book-value-entries} : Create a new netBookValueEntry.
     *
     * @param netBookValueEntryDTO the netBookValueEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new netBookValueEntryDTO, or with status {@code 400 (Bad Request)} if the netBookValueEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/net-book-value-entries")
    public ResponseEntity<NetBookValueEntryDTO> createNetBookValueEntry(@Valid @RequestBody NetBookValueEntryDTO netBookValueEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to save NetBookValueEntry : {}", netBookValueEntryDTO);
        if (netBookValueEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new netBookValueEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NetBookValueEntryDTO result = netBookValueEntryService.save(netBookValueEntryDTO);
        return ResponseEntity
            .created(new URI("/api/net-book-value-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /net-book-value-entries/:id} : Updates an existing netBookValueEntry.
     *
     * @param id the id of the netBookValueEntryDTO to save.
     * @param netBookValueEntryDTO the netBookValueEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated netBookValueEntryDTO,
     * or with status {@code 400 (Bad Request)} if the netBookValueEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the netBookValueEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/net-book-value-entries/{id}")
    public ResponseEntity<NetBookValueEntryDTO> updateNetBookValueEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NetBookValueEntryDTO netBookValueEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NetBookValueEntry : {}, {}", id, netBookValueEntryDTO);
        if (netBookValueEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, netBookValueEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!netBookValueEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NetBookValueEntryDTO result = netBookValueEntryService.save(netBookValueEntryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, netBookValueEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /net-book-value-entries/:id} : Partial updates given fields of an existing netBookValueEntry, field will ignore if it is null
     *
     * @param id the id of the netBookValueEntryDTO to save.
     * @param netBookValueEntryDTO the netBookValueEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated netBookValueEntryDTO,
     * or with status {@code 400 (Bad Request)} if the netBookValueEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the netBookValueEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the netBookValueEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/net-book-value-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NetBookValueEntryDTO> partialUpdateNetBookValueEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NetBookValueEntryDTO netBookValueEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NetBookValueEntry partially : {}, {}", id, netBookValueEntryDTO);
        if (netBookValueEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, netBookValueEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!netBookValueEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NetBookValueEntryDTO> result = netBookValueEntryService.partialUpdate(netBookValueEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, netBookValueEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /net-book-value-entries} : get all the netBookValueEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of netBookValueEntries in body.
     */
    @GetMapping("/net-book-value-entries")
    public ResponseEntity<List<NetBookValueEntryDTO>> getAllNetBookValueEntries(NetBookValueEntryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get NetBookValueEntries by criteria: {}", criteria);
        Page<NetBookValueEntryDTO> page = netBookValueEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /net-book-value-entries/count} : count all the netBookValueEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/net-book-value-entries/count")
    public ResponseEntity<Long> countNetBookValueEntries(NetBookValueEntryCriteria criteria) {
        log.debug("REST request to count NetBookValueEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(netBookValueEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /net-book-value-entries/:id} : get the "id" netBookValueEntry.
     *
     * @param id the id of the netBookValueEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the netBookValueEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/net-book-value-entries/{id}")
    public ResponseEntity<NetBookValueEntryDTO> getNetBookValueEntry(@PathVariable Long id) {
        log.debug("REST request to get NetBookValueEntry : {}", id);
        Optional<NetBookValueEntryDTO> netBookValueEntryDTO = netBookValueEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(netBookValueEntryDTO);
    }

    /**
     * {@code DELETE  /net-book-value-entries/:id} : delete the "id" netBookValueEntry.
     *
     * @param id the id of the netBookValueEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/net-book-value-entries/{id}")
    public ResponseEntity<Void> deleteNetBookValueEntry(@PathVariable Long id) {
        log.debug("REST request to delete NetBookValueEntry : {}", id);
        netBookValueEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/net-book-value-entries?query=:query} : search for the netBookValueEntry corresponding
     * to the query.
     *
     * @param query the query of the netBookValueEntry search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/net-book-value-entries")
    public ResponseEntity<List<NetBookValueEntryDTO>> searchNetBookValueEntries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NetBookValueEntries for query {}", query);
        Page<NetBookValueEntryDTO> page = netBookValueEntryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
