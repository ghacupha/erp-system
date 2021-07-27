package io.github.erp.web.rest;

import io.github.erp.service.FixedAssetNetBookValueService;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.service.dto.FixedAssetNetBookValueCriteria;
import io.github.erp.service.FixedAssetNetBookValueQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.erp.domain.FixedAssetNetBookValue}.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetNetBookValueResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetNetBookValueResource.class);

    private static final String ENTITY_NAME = "erpServiceFixedAssetNetBookValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FixedAssetNetBookValueService fixedAssetNetBookValueService;

    private final FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService;

    public FixedAssetNetBookValueResource(FixedAssetNetBookValueService fixedAssetNetBookValueService, FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService) {
        this.fixedAssetNetBookValueService = fixedAssetNetBookValueService;
        this.fixedAssetNetBookValueQueryService = fixedAssetNetBookValueQueryService;
    }

    /**
     * {@code POST  /fixed-asset-net-book-values} : Create a new fixedAssetNetBookValue.
     *
     * @param fixedAssetNetBookValueDTO the fixedAssetNetBookValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fixedAssetNetBookValueDTO, or with status {@code 400 (Bad Request)} if the fixedAssetNetBookValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fixed-asset-net-book-values")
    public ResponseEntity<FixedAssetNetBookValueDTO> createFixedAssetNetBookValue(@RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) throws URISyntaxException {
        log.debug("REST request to save FixedAssetNetBookValue : {}", fixedAssetNetBookValueDTO);
        if (fixedAssetNetBookValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new fixedAssetNetBookValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAssetNetBookValueDTO result = fixedAssetNetBookValueService.save(fixedAssetNetBookValueDTO);
        return ResponseEntity.created(new URI("/api/fixed-asset-net-book-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fixed-asset-net-book-values} : Updates an existing fixedAssetNetBookValue.
     *
     * @param fixedAssetNetBookValueDTO the fixedAssetNetBookValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fixedAssetNetBookValueDTO,
     * or with status {@code 400 (Bad Request)} if the fixedAssetNetBookValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fixedAssetNetBookValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fixed-asset-net-book-values")
    public ResponseEntity<FixedAssetNetBookValueDTO> updateFixedAssetNetBookValue(@RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) throws URISyntaxException {
        log.debug("REST request to update FixedAssetNetBookValue : {}", fixedAssetNetBookValueDTO);
        if (fixedAssetNetBookValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FixedAssetNetBookValueDTO result = fixedAssetNetBookValueService.save(fixedAssetNetBookValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fixedAssetNetBookValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fixed-asset-net-book-values} : get all the fixedAssetNetBookValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fixedAssetNetBookValues in body.
     */
    @GetMapping("/fixed-asset-net-book-values")
    public ResponseEntity<List<FixedAssetNetBookValueDTO>> getAllFixedAssetNetBookValues(FixedAssetNetBookValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FixedAssetNetBookValues by criteria: {}", criteria);
        Page<FixedAssetNetBookValueDTO> page = fixedAssetNetBookValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fixed-asset-net-book-values/count} : count all the fixedAssetNetBookValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fixed-asset-net-book-values/count")
    public ResponseEntity<Long> countFixedAssetNetBookValues(FixedAssetNetBookValueCriteria criteria) {
        log.debug("REST request to count FixedAssetNetBookValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(fixedAssetNetBookValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fixed-asset-net-book-values/:id} : get the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the fixedAssetNetBookValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fixedAssetNetBookValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fixed-asset-net-book-values/{id}")
    public ResponseEntity<FixedAssetNetBookValueDTO> getFixedAssetNetBookValue(@PathVariable Long id) {
        log.debug("REST request to get FixedAssetNetBookValue : {}", id);
        Optional<FixedAssetNetBookValueDTO> fixedAssetNetBookValueDTO = fixedAssetNetBookValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAssetNetBookValueDTO);
    }

    /**
     * {@code DELETE  /fixed-asset-net-book-values/:id} : delete the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the fixedAssetNetBookValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fixed-asset-net-book-values/{id}")
    public ResponseEntity<Void> deleteFixedAssetNetBookValue(@PathVariable Long id) {
        log.debug("REST request to delete FixedAssetNetBookValue : {}", id);
        fixedAssetNetBookValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fixed-asset-net-book-values?query=:query} : search for the fixedAssetNetBookValue corresponding
     * to the query.
     *
     * @param query the query of the fixedAssetNetBookValue search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fixed-asset-net-book-values")
    public ResponseEntity<List<FixedAssetNetBookValueDTO>> searchFixedAssetNetBookValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FixedAssetNetBookValues for query {}", query);
        Page<FixedAssetNetBookValueDTO> page = fixedAssetNetBookValueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
