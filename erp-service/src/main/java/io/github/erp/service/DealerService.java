package io.github.erp.service;

import io.github.erp.service.dto.DealerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.erp.domain.Dealer}.
 */
public interface DealerService {

    /**
     * Save a dealer.
     *
     * @param dealerDTO the entity to save.
     * @return the persisted entity.
     */
    DealerDTO save(DealerDTO dealerDTO);

    /**
     * Get all the dealers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DealerDTO> findAll(Pageable pageable);

    /**
     * Get all the dealers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<DealerDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" dealer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DealerDTO> findOne(Long id);

    /**
     * Delete the "id" dealer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the dealer corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DealerDTO> search(String query, Pageable pageable);
}
