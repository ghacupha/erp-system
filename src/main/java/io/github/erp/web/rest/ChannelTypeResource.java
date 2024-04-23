package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

import io.github.erp.repository.ChannelTypeRepository;
import io.github.erp.service.ChannelTypeQueryService;
import io.github.erp.service.ChannelTypeService;
import io.github.erp.service.criteria.ChannelTypeCriteria;
import io.github.erp.service.dto.ChannelTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ChannelType}.
 */
@RestController
@RequestMapping("/api")
public class ChannelTypeResource {

    private final Logger log = LoggerFactory.getLogger(ChannelTypeResource.class);

    private static final String ENTITY_NAME = "channelType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChannelTypeService channelTypeService;

    private final ChannelTypeRepository channelTypeRepository;

    private final ChannelTypeQueryService channelTypeQueryService;

    public ChannelTypeResource(
        ChannelTypeService channelTypeService,
        ChannelTypeRepository channelTypeRepository,
        ChannelTypeQueryService channelTypeQueryService
    ) {
        this.channelTypeService = channelTypeService;
        this.channelTypeRepository = channelTypeRepository;
        this.channelTypeQueryService = channelTypeQueryService;
    }

    /**
     * {@code POST  /channel-types} : Create a new channelType.
     *
     * @param channelTypeDTO the channelTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new channelTypeDTO, or with status {@code 400 (Bad Request)} if the channelType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/channel-types")
    public ResponseEntity<ChannelTypeDTO> createChannelType(@Valid @RequestBody ChannelTypeDTO channelTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ChannelType : {}", channelTypeDTO);
        if (channelTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new channelType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChannelTypeDTO result = channelTypeService.save(channelTypeDTO);
        return ResponseEntity
            .created(new URI("/api/channel-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /channel-types/:id} : Updates an existing channelType.
     *
     * @param id the id of the channelTypeDTO to save.
     * @param channelTypeDTO the channelTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated channelTypeDTO,
     * or with status {@code 400 (Bad Request)} if the channelTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the channelTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/channel-types/{id}")
    public ResponseEntity<ChannelTypeDTO> updateChannelType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChannelTypeDTO channelTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChannelType : {}, {}", id, channelTypeDTO);
        if (channelTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, channelTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!channelTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChannelTypeDTO result = channelTypeService.save(channelTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, channelTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /channel-types/:id} : Partial updates given fields of an existing channelType, field will ignore if it is null
     *
     * @param id the id of the channelTypeDTO to save.
     * @param channelTypeDTO the channelTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated channelTypeDTO,
     * or with status {@code 400 (Bad Request)} if the channelTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the channelTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the channelTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/channel-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChannelTypeDTO> partialUpdateChannelType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChannelTypeDTO channelTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChannelType partially : {}, {}", id, channelTypeDTO);
        if (channelTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, channelTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!channelTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChannelTypeDTO> result = channelTypeService.partialUpdate(channelTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, channelTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /channel-types} : get all the channelTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of channelTypes in body.
     */
    @GetMapping("/channel-types")
    public ResponseEntity<List<ChannelTypeDTO>> getAllChannelTypes(ChannelTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChannelTypes by criteria: {}", criteria);
        Page<ChannelTypeDTO> page = channelTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /channel-types/count} : count all the channelTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/channel-types/count")
    public ResponseEntity<Long> countChannelTypes(ChannelTypeCriteria criteria) {
        log.debug("REST request to count ChannelTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(channelTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /channel-types/:id} : get the "id" channelType.
     *
     * @param id the id of the channelTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the channelTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/channel-types/{id}")
    public ResponseEntity<ChannelTypeDTO> getChannelType(@PathVariable Long id) {
        log.debug("REST request to get ChannelType : {}", id);
        Optional<ChannelTypeDTO> channelTypeDTO = channelTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(channelTypeDTO);
    }

    /**
     * {@code DELETE  /channel-types/:id} : delete the "id" channelType.
     *
     * @param id the id of the channelTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/channel-types/{id}")
    public ResponseEntity<Void> deleteChannelType(@PathVariable Long id) {
        log.debug("REST request to delete ChannelType : {}", id);
        channelTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/channel-types?query=:query} : search for the channelType corresponding
     * to the query.
     *
     * @param query the query of the channelType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/channel-types")
    public ResponseEntity<List<ChannelTypeDTO>> searchChannelTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ChannelTypes for query {}", query);
        Page<ChannelTypeDTO> page = channelTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
