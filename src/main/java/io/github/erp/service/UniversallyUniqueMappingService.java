package io.github.erp.service;

import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.UniversallyUniqueMapping}.
 */
public interface UniversallyUniqueMappingService {
    /**
     * Save a universallyUniqueMapping.
     *
     * @param universallyUniqueMappingDTO the entity to save.
     * @return the persisted entity.
     */
    UniversallyUniqueMappingDTO save(UniversallyUniqueMappingDTO universallyUniqueMappingDTO);

    /**
     * Partially updates a universallyUniqueMapping.
     *
     * @param universallyUniqueMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UniversallyUniqueMappingDTO> partialUpdate(UniversallyUniqueMappingDTO universallyUniqueMappingDTO);

    /**
     * Get all the universallyUniqueMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversallyUniqueMappingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" universallyUniqueMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UniversallyUniqueMappingDTO> findOne(Long id);

    /**
     * Delete the "id" universallyUniqueMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the universallyUniqueMapping corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniversallyUniqueMappingDTO> search(String query, Pageable pageable);
}
