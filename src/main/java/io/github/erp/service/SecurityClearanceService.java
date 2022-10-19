package io.github.erp.service;

import io.github.erp.service.dto.SecurityClearanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SecurityClearance}.
 */
public interface SecurityClearanceService {
    /**
     * Save a securityClearance.
     *
     * @param securityClearanceDTO the entity to save.
     * @return the persisted entity.
     */
    SecurityClearanceDTO save(SecurityClearanceDTO securityClearanceDTO);

    /**
     * Partially updates a securityClearance.
     *
     * @param securityClearanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SecurityClearanceDTO> partialUpdate(SecurityClearanceDTO securityClearanceDTO);

    /**
     * Get all the securityClearances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SecurityClearanceDTO> findAll(Pageable pageable);

    /**
     * Get all the securityClearances with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SecurityClearanceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" securityClearance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SecurityClearanceDTO> findOne(Long id);

    /**
     * Delete the "id" securityClearance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the securityClearance corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SecurityClearanceDTO> search(String query, Pageable pageable);
}
