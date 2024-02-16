package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.repository.AssetAdditionsReportRepository;
import io.github.erp.service.AssetAdditionsReportQueryService;
import io.github.erp.service.AssetAdditionsReportService;
import io.github.erp.service.criteria.AssetAdditionsReportCriteria;
import io.github.erp.service.dto.AssetAdditionsReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetAdditionsReport}.
 */
@RestController
@RequestMapping("/api")
public class AssetAdditionsReportResource {

    private final Logger log = LoggerFactory.getLogger(AssetAdditionsReportResource.class);

    private static final String ENTITY_NAME = "assetAdditionsReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetAdditionsReportService assetAdditionsReportService;

    private final AssetAdditionsReportRepository assetAdditionsReportRepository;

    private final AssetAdditionsReportQueryService assetAdditionsReportQueryService;

    public AssetAdditionsReportResource(
        AssetAdditionsReportService assetAdditionsReportService,
        AssetAdditionsReportRepository assetAdditionsReportRepository,
        AssetAdditionsReportQueryService assetAdditionsReportQueryService
    ) {
        this.assetAdditionsReportService = assetAdditionsReportService;
        this.assetAdditionsReportRepository = assetAdditionsReportRepository;
        this.assetAdditionsReportQueryService = assetAdditionsReportQueryService;
    }

    /**
     * {@code POST  /asset-additions-reports} : Create a new assetAdditionsReport.
     *
     * @param assetAdditionsReportDTO the assetAdditionsReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetAdditionsReportDTO, or with status {@code 400 (Bad Request)} if the assetAdditionsReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-additions-reports")
    public ResponseEntity<AssetAdditionsReportDTO> createAssetAdditionsReport(
        @Valid @RequestBody AssetAdditionsReportDTO assetAdditionsReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AssetAdditionsReport : {}", assetAdditionsReportDTO);
        if (assetAdditionsReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetAdditionsReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetAdditionsReportDTO result = assetAdditionsReportService.save(assetAdditionsReportDTO);
        return ResponseEntity
            .created(new URI("/api/asset-additions-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-additions-reports/:id} : Updates an existing assetAdditionsReport.
     *
     * @param id the id of the assetAdditionsReportDTO to save.
     * @param assetAdditionsReportDTO the assetAdditionsReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAdditionsReportDTO,
     * or with status {@code 400 (Bad Request)} if the assetAdditionsReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetAdditionsReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-additions-reports/{id}")
    public ResponseEntity<AssetAdditionsReportDTO> updateAssetAdditionsReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetAdditionsReportDTO assetAdditionsReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetAdditionsReport : {}, {}", id, assetAdditionsReportDTO);
        if (assetAdditionsReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetAdditionsReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetAdditionsReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetAdditionsReportDTO result = assetAdditionsReportService.save(assetAdditionsReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetAdditionsReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-additions-reports/:id} : Partial updates given fields of an existing assetAdditionsReport, field will ignore if it is null
     *
     * @param id the id of the assetAdditionsReportDTO to save.
     * @param assetAdditionsReportDTO the assetAdditionsReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAdditionsReportDTO,
     * or with status {@code 400 (Bad Request)} if the assetAdditionsReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetAdditionsReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetAdditionsReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-additions-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetAdditionsReportDTO> partialUpdateAssetAdditionsReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetAdditionsReportDTO assetAdditionsReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetAdditionsReport partially : {}, {}", id, assetAdditionsReportDTO);
        if (assetAdditionsReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetAdditionsReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetAdditionsReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetAdditionsReportDTO> result = assetAdditionsReportService.partialUpdate(assetAdditionsReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetAdditionsReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-additions-reports} : get all the assetAdditionsReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetAdditionsReports in body.
     */
    @GetMapping("/asset-additions-reports")
    public ResponseEntity<List<AssetAdditionsReportDTO>> getAllAssetAdditionsReports(
        AssetAdditionsReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AssetAdditionsReports by criteria: {}", criteria);
        Page<AssetAdditionsReportDTO> page = assetAdditionsReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-additions-reports/count} : count all the assetAdditionsReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-additions-reports/count")
    public ResponseEntity<Long> countAssetAdditionsReports(AssetAdditionsReportCriteria criteria) {
        log.debug("REST request to count AssetAdditionsReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetAdditionsReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-additions-reports/:id} : get the "id" assetAdditionsReport.
     *
     * @param id the id of the assetAdditionsReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetAdditionsReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-additions-reports/{id}")
    public ResponseEntity<AssetAdditionsReportDTO> getAssetAdditionsReport(@PathVariable Long id) {
        log.debug("REST request to get AssetAdditionsReport : {}", id);
        Optional<AssetAdditionsReportDTO> assetAdditionsReportDTO = assetAdditionsReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetAdditionsReportDTO);
    }

    /**
     * {@code DELETE  /asset-additions-reports/:id} : delete the "id" assetAdditionsReport.
     *
     * @param id the id of the assetAdditionsReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-additions-reports/{id}")
    public ResponseEntity<Void> deleteAssetAdditionsReport(@PathVariable Long id) {
        log.debug("REST request to delete AssetAdditionsReport : {}", id);
        assetAdditionsReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-additions-reports?query=:query} : search for the assetAdditionsReport corresponding
     * to the query.
     *
     * @param query the query of the assetAdditionsReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-additions-reports")
    public ResponseEntity<List<AssetAdditionsReportDTO>> searchAssetAdditionsReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetAdditionsReports for query {}", query);
        Page<AssetAdditionsReportDTO> page = assetAdditionsReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
