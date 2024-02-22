package io.github.erp.internal.service;

import io.github.erp.service.dto.DepreciationPeriodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InternalDepreciationPeriodService {

    /**
     * Save a depreciationPeriod.
     *
     * @param depreciationPeriodDTO the entity to save.
     * @return the persisted entity.
     */
    DepreciationPeriodDTO save(DepreciationPeriodDTO depreciationPeriodDTO);

    /**
     * Partially updates a depreciationPeriod.
     *
     * @param depreciationPeriodDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepreciationPeriodDTO> partialUpdate(DepreciationPeriodDTO depreciationPeriodDTO);

    /**
     * Get all the depreciationPeriods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationPeriodDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depreciationPeriod.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepreciationPeriodDTO> findOne(Long id);

    /**
     * Delete the "id" depreciationPeriod.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the depreciationPeriod corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationPeriodDTO> search(String query, Pageable pageable);
}
