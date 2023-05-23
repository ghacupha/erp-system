package io.github.erp.service;

import io.github.erp.service.dto.AssetWarrantyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.AssetWarranty}.
 */
public interface AssetWarrantyService {
    /**
     * Save a assetWarranty.
     *
     * @param assetWarrantyDTO the entity to save.
     * @return the persisted entity.
     */
    AssetWarrantyDTO save(AssetWarrantyDTO assetWarrantyDTO);

    /**
     * Partially updates a assetWarranty.
     *
     * @param assetWarrantyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssetWarrantyDTO> partialUpdate(AssetWarrantyDTO assetWarrantyDTO);

    /**
     * Get all the assetWarranties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetWarrantyDTO> findAll(Pageable pageable);

    /**
     * Get all the assetWarranties with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetWarrantyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" assetWarranty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssetWarrantyDTO> findOne(Long id);

    /**
     * Delete the "id" assetWarranty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the assetWarranty corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssetWarrantyDTO> search(String query, Pageable pageable);
}
