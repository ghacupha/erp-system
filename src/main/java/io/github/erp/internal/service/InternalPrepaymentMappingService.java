package io.github.erp.internal.service;

import java.util.Optional;

import io.github.erp.service.PrepaymentMappingService;
import io.github.erp.service.dto.PrepaymentMappingDTO;

/**
 * Interface for retrieval of mapping parameters used in the prepayment modules
 */
public interface InternalPrepaymentMappingService extends PrepaymentMappingService {
    
    /**
     * Search for the universallyUniqueMapping corresponding to the query.
     *
     * @param parameterKey the query of the search.
     *
     * @return the parameter mapped to that key
     */
    Optional<PrepaymentMappingDTO> getMapping(String parameterKey);
}
