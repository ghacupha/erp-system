package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import io.github.erp.internal.service.prepayments.InternalPrepaymentMappingService;
import io.github.erp.internal.service.InternalUniversallyUniqueMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.util.Optional;

/**
 * This resource is designed primarily to fetch parameters that are mapped in the
 * universally-unique-mapping entity.
 * Edit: 2022-08-30
 * Okay this feature turned out to be so useful that we created other domain-specific
 * entities primarily for front-end configuration. For instance we now have and entity
 * called prepayment-mapping in which we configure parameters that are specific to
 * prepayments only
 */
@RestController("configurationMappingResourceProd")
@RequestMapping("/api/configuration")
public class ConfigurationMappingResourceProd {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationMappingResourceProd.class);

    private final InternalPrepaymentMappingService prepaymentMappingService;
    private final InternalUniversallyUniqueMappingService universallyUniqueMappingService;

    public ConfigurationMappingResourceProd(
        @Qualifier("internalUniversallyUniqueMappingService") InternalUniversallyUniqueMappingService universallyUniqueMappingService,
        @Qualifier("internalPrepaymentMappingService") InternalPrepaymentMappingService prepaymentMappingService ) {
        this.universallyUniqueMappingService = universallyUniqueMappingService;
        this.prepaymentMappingService = prepaymentMappingService;
    }

    /**
     * {@code GET  /universally-unique-mappings/:universalKey} : get the "mappedValue" of universallyUniqueMapping given the "universalKey".
     *
     * @param universalKey the key of the universallyUniqueMapping value to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mappedValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/universally-unique-mappings/{universalKey}")
    public ResponseEntity<UniversallyUniqueMapping> getUniversalMapping(@PathVariable String universalKey) {
        log.debug("REST request to get UniversallyUniqueMapping : {}", universalKey);
        Optional<UniversallyUniqueMapping> uMapping = universallyUniqueMappingService.getMapping(universalKey);
        return ResponseUtil.wrapOrNotFound(uMapping);
    }

    /**
     * {@code GET  /prepayment-mappings/:universalKey} : get the "parameter" of prepaymentMapping given the "parameterKey".
     *
     * @param parameterKey the key of the prepaymentMapping value to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mappedValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-mappings/{parameterKey}")
    public ResponseEntity<PrepaymentMappingDTO> getPrepaymentMapping(@PathVariable String parameterKey) {
        log.debug("REST request to get Prepayment mapping for the key : {}", parameterKey);
        Optional<PrepaymentMappingDTO> uMapping = prepaymentMappingService.getMapping(parameterKey);
        return ResponseUtil.wrapOrNotFound(uMapping);
    }
}
