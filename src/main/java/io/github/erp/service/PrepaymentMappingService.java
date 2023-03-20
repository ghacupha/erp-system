package io.github.erp.service;

import io.github.erp.service.dto.PrepaymentMappingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentMapping}.
 */
public interface PrepaymentMappingService {
    /**
     * Save a prepaymentMapping.
     *
     * @param prepaymentMappingDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentMappingDTO save(PrepaymentMappingDTO prepaymentMappingDTO);

    /**
     * Partially updates a prepaymentMapping.
     *
     * @param prepaymentMappingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentMappingDTO> partialUpdate(PrepaymentMappingDTO prepaymentMappingDTO);

    /**
     * Get all the prepaymentMappings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMappingDTO> findAll(Pageable pageable);

    /**
     * Get all the prepaymentMappings with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMappingDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" prepaymentMapping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentMappingDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentMapping.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentMapping corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMappingDTO> search(String query, Pageable pageable);
}
