package io.github.erp.service;

import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.FixedAssetNetBookValue}.
 */
public interface FixedAssetNetBookValueService {
    /**
     * Save a fixedAssetNetBookValue.
     *
     * @param fixedAssetNetBookValueDTO the entity to save.
     * @return the persisted entity.
     */
    FixedAssetNetBookValueDTO save(FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO);

    /**
     * Partially updates a fixedAssetNetBookValue.
     *
     * @param fixedAssetNetBookValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FixedAssetNetBookValueDTO> partialUpdate(FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO);

    /**
     * Get all the fixedAssetNetBookValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetNetBookValueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FixedAssetNetBookValueDTO> findOne(Long id);

    /**
     * Delete the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fixedAssetNetBookValue corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetNetBookValueDTO> search(String query, Pageable pageable);
}
