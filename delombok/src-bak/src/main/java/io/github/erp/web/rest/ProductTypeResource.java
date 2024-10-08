package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.ProductTypeRepository;
import io.github.erp.service.ProductTypeQueryService;
import io.github.erp.service.ProductTypeService;
import io.github.erp.service.criteria.ProductTypeCriteria;
import io.github.erp.service.dto.ProductTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ProductType}.
 */
@RestController
@RequestMapping("/api")
public class ProductTypeResource {

    private final Logger log = LoggerFactory.getLogger(ProductTypeResource.class);

    private static final String ENTITY_NAME = "productType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductTypeService productTypeService;

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeQueryService productTypeQueryService;

    public ProductTypeResource(
        ProductTypeService productTypeService,
        ProductTypeRepository productTypeRepository,
        ProductTypeQueryService productTypeQueryService
    ) {
        this.productTypeService = productTypeService;
        this.productTypeRepository = productTypeRepository;
        this.productTypeQueryService = productTypeQueryService;
    }

    /**
     * {@code POST  /product-types} : Create a new productType.
     *
     * @param productTypeDTO the productTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productTypeDTO, or with status {@code 400 (Bad Request)} if the productType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-types")
    public ResponseEntity<ProductTypeDTO> createProductType(@Valid @RequestBody ProductTypeDTO productTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ProductType : {}", productTypeDTO);
        if (productTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new productType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTypeDTO result = productTypeService.save(productTypeDTO);
        return ResponseEntity
            .created(new URI("/api/product-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-types/:id} : Updates an existing productType.
     *
     * @param id the id of the productTypeDTO to save.
     * @param productTypeDTO the productTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTypeDTO,
     * or with status {@code 400 (Bad Request)} if the productTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-types/{id}")
    public ResponseEntity<ProductTypeDTO> updateProductType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductTypeDTO productTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProductType : {}, {}", id, productTypeDTO);
        if (productTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductTypeDTO result = productTypeService.save(productTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-types/:id} : Partial updates given fields of an existing productType, field will ignore if it is null
     *
     * @param id the id of the productTypeDTO to save.
     * @param productTypeDTO the productTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTypeDTO,
     * or with status {@code 400 (Bad Request)} if the productTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductTypeDTO> partialUpdateProductType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductTypeDTO productTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductType partially : {}, {}", id, productTypeDTO);
        if (productTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductTypeDTO> result = productTypeService.partialUpdate(productTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-types} : get all the productTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productTypes in body.
     */
    @GetMapping("/product-types")
    public ResponseEntity<List<ProductTypeDTO>> getAllProductTypes(ProductTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductTypes by criteria: {}", criteria);
        Page<ProductTypeDTO> page = productTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-types/count} : count all the productTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/product-types/count")
    public ResponseEntity<Long> countProductTypes(ProductTypeCriteria criteria) {
        log.debug("REST request to count ProductTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(productTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-types/:id} : get the "id" productType.
     *
     * @param id the id of the productTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-types/{id}")
    public ResponseEntity<ProductTypeDTO> getProductType(@PathVariable Long id) {
        log.debug("REST request to get ProductType : {}", id);
        Optional<ProductTypeDTO> productTypeDTO = productTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productTypeDTO);
    }

    /**
     * {@code DELETE  /product-types/:id} : delete the "id" productType.
     *
     * @param id the id of the productTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-types/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        log.debug("REST request to delete ProductType : {}", id);
        productTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/product-types?query=:query} : search for the productType corresponding
     * to the query.
     *
     * @param query the query of the productType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/product-types")
    public ResponseEntity<List<ProductTypeDTO>> searchProductTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProductTypes for query {}", query);
        Page<ProductTypeDTO> page = productTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
