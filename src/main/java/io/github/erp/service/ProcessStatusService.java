package io.github.erp.service;

import io.github.erp.service.dto.ProcessStatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ProcessStatus}.
 */
public interface ProcessStatusService {
    /**
     * Save a processStatus.
     *
     * @param processStatusDTO the entity to save.
     * @return the persisted entity.
     */
    ProcessStatusDTO save(ProcessStatusDTO processStatusDTO);

    /**
     * Partially updates a processStatus.
     *
     * @param processStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProcessStatusDTO> partialUpdate(ProcessStatusDTO processStatusDTO);

    /**
     * Get all the processStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessStatusDTO> findAll(Pageable pageable);

    /**
     * Get all the processStatuses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessStatusDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" processStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcessStatusDTO> findOne(Long id);

    /**
     * Delete the "id" processStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the processStatus corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessStatusDTO> search(String query, Pageable pageable);
}
