package io.github.erp.erp.resources;

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
 * universally-unique-mapping entity
 */
@RestController
@RequestMapping("/api/configuration")
public class ConfigurationMappingResource {

    private final static Logger log = LoggerFactory.getLogger(ConfigurationMappingResource.class);

    private final InternalUniversallyUniqueMappingService universallyUniqueMappingService;

    public ConfigurationMappingResource(@Qualifier("internalUniversallyUniqueMappingService") InternalUniversallyUniqueMappingService universallyUniqueMappingService) {
        this.universallyUniqueMappingService = universallyUniqueMappingService;
    }

    /**
     * {@code GET  /universally-unique-mappings/:universalKey} : get the "mappedValue" of universallyUniqueMapping given the "universalKey".
     *
     * @param universalKey the key of the universallyUniqueMapping value to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mappedValue, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/universally-unique-mappings/{universalKey}")
    public ResponseEntity<String> getUniversalMapping(@PathVariable String universalKey) {
        log.debug("REST request to get UniversallyUniqueMapping : {}", universalKey);
        Optional<String> uMapping = universallyUniqueMappingService.getMapping(universalKey);
        return ResponseUtil.wrapOrNotFound(uMapping);
    }
}
