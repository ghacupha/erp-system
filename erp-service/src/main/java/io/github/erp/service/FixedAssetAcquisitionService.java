package io.github.erp.service;

import io.github.erp.service.dto.FixedAssetAcquisitionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.FixedAssetAcquisition}.
 */
public interface FixedAssetAcquisitionService {

    /**
     * Save a fixedAssetAcquisition.
     *
     * @param fixedAssetAcquisitionDTO the entity to save.
     * @return the persisted entity.
     */
    FixedAssetAcquisitionDTO save(FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO);

    /**
     * Get all the fixedAssetAcquisitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetAcquisitionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" fixedAssetAcquisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FixedAssetAcquisitionDTO> findOne(Long id);

    /**
     * Delete the "id" fixedAssetAcquisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fixedAssetAcquisition corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FixedAssetAcquisitionDTO> search(String query, Pageable pageable);
}
