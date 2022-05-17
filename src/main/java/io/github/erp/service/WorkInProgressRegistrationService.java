package io.github.erp.service;

import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.WorkInProgressRegistration}.
 */
public interface WorkInProgressRegistrationService {
    /**
     * Save a workInProgressRegistration.
     *
     * @param workInProgressRegistrationDTO the entity to save.
     * @return the persisted entity.
     */
    WorkInProgressRegistrationDTO save(WorkInProgressRegistrationDTO workInProgressRegistrationDTO);

    /**
     * Partially updates a workInProgressRegistration.
     *
     * @param workInProgressRegistrationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkInProgressRegistrationDTO> partialUpdate(WorkInProgressRegistrationDTO workInProgressRegistrationDTO);

    /**
     * Get all the workInProgressRegistrations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressRegistrationDTO> findAll(Pageable pageable);

    /**
     * Get all the workInProgressRegistrations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressRegistrationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workInProgressRegistration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkInProgressRegistrationDTO> findOne(Long id);

    /**
     * Delete the "id" workInProgressRegistration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workInProgressRegistration corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressRegistrationDTO> search(String query, Pageable pageable);
}
