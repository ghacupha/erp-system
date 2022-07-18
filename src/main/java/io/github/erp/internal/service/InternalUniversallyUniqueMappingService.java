package io.github.erp.internal.service;

import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.service.UniversallyUniqueMappingService;

import java.util.Optional;

/**
 * This service extends the functionality of the common service to simply select values that
 * are mapped to a particular key and are called in various parts of the programming for instance
 * to search out common values or preferred inputs. This has helped us avoid hard-coding business
 * logic into the app
 */
public interface InternalUniversallyUniqueMappingService extends UniversallyUniqueMappingService {

    /**
     * Search for the universallyUniqueMapping corresponding to the query.
     *
     * @param universalKey the query of the search.
     *
     * @return the value mapped to that key
     */
    Optional<UniversallyUniqueMapping> getMapping(String universalKey);
}
