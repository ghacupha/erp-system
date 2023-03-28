package io.github.erp.service;

import io.github.erp.service.dto.WorkProjectRegisterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.WorkProjectRegister}.
 */
public interface WorkProjectRegisterService {
    /**
     * Save a workProjectRegister.
     *
     * @param workProjectRegisterDTO the entity to save.
     * @return the persisted entity.
     */
    WorkProjectRegisterDTO save(WorkProjectRegisterDTO workProjectRegisterDTO);

    /**
     * Partially updates a workProjectRegister.
     *
     * @param workProjectRegisterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkProjectRegisterDTO> partialUpdate(WorkProjectRegisterDTO workProjectRegisterDTO);

    /**
     * Get all the workProjectRegisters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkProjectRegisterDTO> findAll(Pageable pageable);

    /**
     * Get all the workProjectRegisters with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkProjectRegisterDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workProjectRegister.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkProjectRegisterDTO> findOne(Long id);

    /**
     * Delete the "id" workProjectRegister.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the workProjectRegister corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkProjectRegisterDTO> search(String query, Pageable pageable);
}
