package io.github.erp.service;

import io.github.erp.service.dto.LeaseContractDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeaseContract}.
 */
public interface LeaseContractService {
    /**
     * Save a leaseContract.
     *
     * @param leaseContractDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseContractDTO save(LeaseContractDTO leaseContractDTO);

    /**
     * Partially updates a leaseContract.
     *
     * @param leaseContractDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaseContractDTO> partialUpdate(LeaseContractDTO leaseContractDTO);

    /**
     * Get all the leaseContracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseContractDTO> findAll(Pageable pageable);

    /**
     * Get all the leaseContracts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseContractDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" leaseContract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseContractDTO> findOne(Long id);

    /**
     * Delete the "id" leaseContract.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leaseContract corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseContractDTO> search(String query, Pageable pageable);
}
