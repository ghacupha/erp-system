package io.github.erp.service;

import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.DepreciationBatchSequence}.
 */
public interface DepreciationBatchSequenceService {
    /**
     * Save a depreciationBatchSequence.
     *
     * @param depreciationBatchSequenceDTO the entity to save.
     * @return the persisted entity.
     */
    DepreciationBatchSequenceDTO save(DepreciationBatchSequenceDTO depreciationBatchSequenceDTO);

    /**
     * Partially updates a depreciationBatchSequence.
     *
     * @param depreciationBatchSequenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepreciationBatchSequenceDTO> partialUpdate(DepreciationBatchSequenceDTO depreciationBatchSequenceDTO);

    /**
     * Get all the depreciationBatchSequences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationBatchSequenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depreciationBatchSequence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepreciationBatchSequenceDTO> findOne(Long id);

    /**
     * Delete the "id" depreciationBatchSequence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the depreciationBatchSequence corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationBatchSequenceDTO> search(String query, Pageable pageable);
}
