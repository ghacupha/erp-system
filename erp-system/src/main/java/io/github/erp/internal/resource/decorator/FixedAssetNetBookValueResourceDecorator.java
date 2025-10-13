package io.github.erp.internal.resource.decorator;

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
import io.github.erp.service.criteria.FixedAssetNetBookValueCriteria;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.erp.resources.assets.FixedAssetNetBookValueResourceProd;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

public class FixedAssetNetBookValueResourceDecorator implements IFixedAssetNetBookValueResource {

    private final FixedAssetNetBookValueResourceProd fixedAssetNetBookValueResource;

    public FixedAssetNetBookValueResourceDecorator(FixedAssetNetBookValueResourceProd fixedAssetNetBookValueResource) {
        this.fixedAssetNetBookValueResource = fixedAssetNetBookValueResource;
    }

    /**
     * {@code POST  /fixed-asset-net-book-values} : Create a new fixedAssetNetBookValue.
     *
     * @param fixedAssetNetBookValueDTO the fixedAssetNetBookValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fixedAssetNetBookValueDTO, or with status {@code 400 (Bad Request)} if the fixedAssetNetBookValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fixed-asset-net-book-values")
    public ResponseEntity<FixedAssetNetBookValueDTO> createFixedAssetNetBookValue(@RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) throws URISyntaxException{
        return this.fixedAssetNetBookValueResource.createFixedAssetNetBookValue(fixedAssetNetBookValueDTO);
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
    public ResponseEntity<FixedAssetNetBookValueDTO> updateFixedAssetNetBookValue(@PathVariable(value = "id", required = false) final Long id, @RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) throws URISyntaxException {
        return this.fixedAssetNetBookValueResource.updateFixedAssetNetBookValue(id, fixedAssetNetBookValueDTO);
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
        return this.fixedAssetNetBookValueResource.getAllFixedAssetNetBookValues(criteria, pageable);
    }

    /**
     * {@code GET  /fixed-asset-net-book-values/count} : count all the fixedAssetNetBookValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fixed-asset-net-book-values/count")
    public ResponseEntity<Long> countFixedAssetNetBookValues(FixedAssetNetBookValueCriteria criteria) {
        return this.fixedAssetNetBookValueResource.countFixedAssetNetBookValues(criteria);
    }

    /**
     * {@code GET  /fixed-asset-net-book-values/:id} : get the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the fixedAssetNetBookValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fixedAssetNetBookValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fixed-asset-net-book-values/{id}")
    public ResponseEntity<FixedAssetNetBookValueDTO> getFixedAssetNetBookValue(@PathVariable Long id) {
        return this.fixedAssetNetBookValueResource.getFixedAssetNetBookValue(id);
    }

    /**
     * {@code DELETE  /fixed-asset-net-book-values/:id} : delete the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the fixedAssetNetBookValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fixed-asset-net-book-values/{id}")
    public ResponseEntity<Void> deleteFixedAssetNetBookValue(@PathVariable Long id) {
        return this.fixedAssetNetBookValueResource.deleteFixedAssetNetBookValue(id);
    }

    /**
     * {@code SEARCH  /_search/fixed-asset-net-book-values?query=:query} : search for the fixedAssetNetBookValue corresponding
     * to the query.
     *
     * @param query    the query of the fixedAssetNetBookValue search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fixed-asset-net-book-values")
    public ResponseEntity<List<FixedAssetNetBookValueDTO>> searchFixedAssetNetBookValues(@RequestParam String query, Pageable pageable) {
        return this.fixedAssetNetBookValueResource.searchFixedAssetNetBookValues(query, pageable);
    }
}
