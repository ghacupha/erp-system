package io.github.erp.service;

import io.github.erp.service.dto.DepreciationJobDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.DepreciationJob}.
 */
public interface DepreciationJobService {
    /**
     * Save a depreciationJob.
     *
     * @param depreciationJobDTO the entity to save.
     * @return the persisted entity.
     */
    DepreciationJobDTO save(DepreciationJobDTO depreciationJobDTO);

    /**
     * Partially updates a depreciationJob.
     *
     * @param depreciationJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepreciationJobDTO> partialUpdate(DepreciationJobDTO depreciationJobDTO);

    /**
     * Get all the depreciationJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationJobDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depreciationJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepreciationJobDTO> findOne(Long id);

    /**
     * Delete the "id" depreciationJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the depreciationJob corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationJobDTO> search(String query, Pageable pageable);
}
