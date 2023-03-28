package io.github.erp.service;

import io.github.erp.service.dto.AlgorithmDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.Algorithm}.
 */
public interface AlgorithmService {
    /**
     * Save a algorithm.
     *
     * @param algorithmDTO the entity to save.
     * @return the persisted entity.
     */
    AlgorithmDTO save(AlgorithmDTO algorithmDTO);

    /**
     * Partially updates a algorithm.
     *
     * @param algorithmDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlgorithmDTO> partialUpdate(AlgorithmDTO algorithmDTO);

    /**
     * Get all the algorithms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlgorithmDTO> findAll(Pageable pageable);

    /**
     * Get all the algorithms with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlgorithmDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" algorithm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlgorithmDTO> findOne(Long id);

    /**
     * Delete the "id" algorithm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the algorithm corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AlgorithmDTO> search(String query, Pageable pageable);
}
