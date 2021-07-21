package io.github.erp.service;

import io.github.erp.service.dto.TaxReferenceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.TaxReference}.
 */
public interface TaxReferenceService {

    /**
     * Save a taxReference.
     *
     * @param taxReferenceDTO the entity to save.
     * @return the persisted entity.
     */
    TaxReferenceDTO save(TaxReferenceDTO taxReferenceDTO);

    /**
     * Get all the taxReferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxReferenceDTO> findAll(Pageable pageable);


    /**
     * Get the "id" taxReference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxReferenceDTO> findOne(Long id);

    /**
     * Delete the "id" taxReference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the taxReference corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxReferenceDTO> search(String query, Pageable pageable);
}
