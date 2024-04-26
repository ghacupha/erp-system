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

import io.github.erp.repository.NbvReportRepository;
import io.github.erp.service.NbvReportQueryService;
import io.github.erp.service.NbvReportService;
import io.github.erp.service.criteria.NbvReportCriteria;
import io.github.erp.service.dto.NbvReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.NbvReport}.
 */
@RestController
@RequestMapping("/api")
public class NbvReportResource {

    private final Logger log = LoggerFactory.getLogger(NbvReportResource.class);

    private static final String ENTITY_NAME = "nbvReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NbvReportService nbvReportService;

    private final NbvReportRepository nbvReportRepository;

    private final NbvReportQueryService nbvReportQueryService;

    public NbvReportResource(
        NbvReportService nbvReportService,
        NbvReportRepository nbvReportRepository,
        NbvReportQueryService nbvReportQueryService
    ) {
        this.nbvReportService = nbvReportService;
        this.nbvReportRepository = nbvReportRepository;
        this.nbvReportQueryService = nbvReportQueryService;
    }

    /**
     * {@code POST  /nbv-reports} : Create a new nbvReport.
     *
     * @param nbvReportDTO the nbvReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nbvReportDTO, or with status {@code 400 (Bad Request)} if the nbvReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nbv-reports")
    public ResponseEntity<NbvReportDTO> createNbvReport(@Valid @RequestBody NbvReportDTO nbvReportDTO) throws URISyntaxException {
        log.debug("REST request to save NbvReport : {}", nbvReportDTO);
        if (nbvReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new nbvReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NbvReportDTO result = nbvReportService.save(nbvReportDTO);
        return ResponseEntity
            .created(new URI("/api/nbv-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nbv-reports/:id} : Updates an existing nbvReport.
     *
     * @param id the id of the nbvReportDTO to save.
     * @param nbvReportDTO the nbvReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nbvReportDTO,
     * or with status {@code 400 (Bad Request)} if the nbvReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nbvReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nbv-reports/{id}")
    public ResponseEntity<NbvReportDTO> updateNbvReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NbvReportDTO nbvReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NbvReport : {}, {}", id, nbvReportDTO);
        if (nbvReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nbvReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nbvReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NbvReportDTO result = nbvReportService.save(nbvReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nbvReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nbv-reports/:id} : Partial updates given fields of an existing nbvReport, field will ignore if it is null
     *
     * @param id the id of the nbvReportDTO to save.
     * @param nbvReportDTO the nbvReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nbvReportDTO,
     * or with status {@code 400 (Bad Request)} if the nbvReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nbvReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nbvReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nbv-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NbvReportDTO> partialUpdateNbvReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NbvReportDTO nbvReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NbvReport partially : {}, {}", id, nbvReportDTO);
        if (nbvReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nbvReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nbvReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NbvReportDTO> result = nbvReportService.partialUpdate(nbvReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nbvReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nbv-reports} : get all the nbvReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nbvReports in body.
     */
    @GetMapping("/nbv-reports")
    public ResponseEntity<List<NbvReportDTO>> getAllNbvReports(NbvReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get NbvReports by criteria: {}", criteria);
        Page<NbvReportDTO> page = nbvReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nbv-reports/count} : count all the nbvReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nbv-reports/count")
    public ResponseEntity<Long> countNbvReports(NbvReportCriteria criteria) {
        log.debug("REST request to count NbvReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(nbvReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nbv-reports/:id} : get the "id" nbvReport.
     *
     * @param id the id of the nbvReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nbvReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nbv-reports/{id}")
    public ResponseEntity<NbvReportDTO> getNbvReport(@PathVariable Long id) {
        log.debug("REST request to get NbvReport : {}", id);
        Optional<NbvReportDTO> nbvReportDTO = nbvReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nbvReportDTO);
    }

    /**
     * {@code DELETE  /nbv-reports/:id} : delete the "id" nbvReport.
     *
     * @param id the id of the nbvReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nbv-reports/{id}")
    public ResponseEntity<Void> deleteNbvReport(@PathVariable Long id) {
        log.debug("REST request to delete NbvReport : {}", id);
        nbvReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/nbv-reports?query=:query} : search for the nbvReport corresponding
     * to the query.
     *
     * @param query the query of the nbvReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nbv-reports")
    public ResponseEntity<List<NbvReportDTO>> searchNbvReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NbvReports for query {}", query);
        Page<NbvReportDTO> page = nbvReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
