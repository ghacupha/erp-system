package io.github.erp.internal.resource.decorator;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.service.dto.FixedAssetNetBookValueCriteria;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.web.rest.FixedAssetNetBookValueResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

public class FixedAssetNetBookValueResourceDecorator implements IFixedAssetNetBookValueResource {

    private final FixedAssetNetBookValueResource fixedAssetNetBookValueResource;

    public FixedAssetNetBookValueResourceDecorator(FixedAssetNetBookValueResource fixedAssetNetBookValueResource) {
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
    public ResponseEntity<FixedAssetNetBookValueDTO> updateFixedAssetNetBookValue(@RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO) throws URISyntaxException {
        return this.fixedAssetNetBookValueResource.updateFixedAssetNetBookValue(fixedAssetNetBookValueDTO);
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