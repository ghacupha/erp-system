package io.github.erp.service;

import io.github.erp.service.dto.WorkInProgressTransferDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.WorkInProgressTransfer}.
 */
public interface WorkInProgressTransferService {
    /**
     * Save a workInProgressTransfer.
     *
     * @param workInProgressTransferDTO the entity to save.
     * @return the persisted entity.
     */
    WorkInProgressTransferDTO save(WorkInProgressTransferDTO workInProgressTransferDTO);

    /**
     * Partially updates a workInProgressTransfer.
     *
     * @param workInProgressTransferDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkInProgressTransferDTO> partialUpdate(WorkInProgressTransferDTO workInProgressTransferDTO);

    /**
     * Get all the workInProgressTransfers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressTransferDTO> findAll(Pageable pageable);

    /**
     * Get all the workInProgressTransfers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressTransferDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workInProgressTransfer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkInProgressTransferDTO> findOne(Long id);

    /**
     * Delete the "id" workInProgressTransfer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workInProgressTransfer corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkInProgressTransferDTO> search(String query, Pageable pageable);
}
