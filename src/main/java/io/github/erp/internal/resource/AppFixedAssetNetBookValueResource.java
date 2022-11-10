package io.github.erp.internal.resource;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.internal.resource.decorator.FixedAssetNetBookValueResourceDecorator;
import io.github.erp.internal.resource.decorator.IFixedAssetNetBookValueResource;
import io.github.erp.service.FixedAssetNetBookValueQueryService;
import io.github.erp.service.criteria.FixedAssetNetBookValueCriteria;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.erp.resources.FixedAssetNetBookValueResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This resource is created primarily for the purpose of defeating the 20-items
 * limit whose configuration we were unable to find
 */
@RestController
@RequestMapping("/api/app")
public class AppFixedAssetNetBookValueResource extends FixedAssetNetBookValueResourceDecorator implements IFixedAssetNetBookValueResource {

    private final FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService;

    private static final Logger log = LoggerFactory.getLogger(AppFixedAssetNetBookValueResource.class);

    public AppFixedAssetNetBookValueResource(FixedAssetNetBookValueResource fixedAssetNetBookValueResource, FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService) {
        super(fixedAssetNetBookValueResource);
        this.fixedAssetNetBookValueQueryService = fixedAssetNetBookValueQueryService;
    }

    /**
     * {@code GET  /fixed-asset-net-book-values} : get all the fixedAssetNetBookValues.
     *
     * Returns all fixed-asset-net-book-value items that fulfill the given criteria in a single page
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fixedAssetNetBookValues in body.
     */
    @GetMapping("/fixed-asset-net-book-values")
    public ResponseEntity<List<FixedAssetNetBookValueDTO>> getAllFixedAssetNetBookValues(FixedAssetNetBookValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FixedAssetNetBookValues by criteria: {}", criteria);

        // Configure pageable with total size consistent with a given criteria
        Pageable customPageable = PageRequest.of(0, Math.toIntExact(fixedAssetNetBookValueQueryService.countByCriteria(criteria)));

        return super.getAllFixedAssetNetBookValues(criteria, customPageable);
    }
}
