package io.github.erp.service;

import io.github.erp.service.dto.DepreciationEntryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.DepreciationEntry}.
 */
public interface DepreciationEntryService {
    /**
     * Save a depreciationEntry.
     *
     * @param depreciationEntryDTO the entity to save.
     * @return the persisted entity.
     */
    DepreciationEntryDTO save(DepreciationEntryDTO depreciationEntryDTO);

    /**
     * Partially updates a depreciationEntry.
     *
     * @param depreciationEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepreciationEntryDTO> partialUpdate(DepreciationEntryDTO depreciationEntryDTO);

    /**
     * Get all the depreciationEntries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationEntryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depreciationEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepreciationEntryDTO> findOne(Long id);

    /**
     * Delete the "id" depreciationEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the depreciationEntry corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationEntryDTO> search(String query, Pageable pageable);
}
