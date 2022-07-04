package io.github.erp.service;

import io.github.erp.service.dto.SystemModuleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SystemModule}.
 */
public interface SystemModuleService {
    /**
     * Save a systemModule.
     *
     * @param systemModuleDTO the entity to save.
     * @return the persisted entity.
     */
    SystemModuleDTO save(SystemModuleDTO systemModuleDTO);

    /**
     * Partially updates a systemModule.
     *
     * @param systemModuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemModuleDTO> partialUpdate(SystemModuleDTO systemModuleDTO);

    /**
     * Get all the systemModules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemModuleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" systemModule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemModuleDTO> findOne(Long id);

    /**
     * Delete the "id" systemModule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the systemModule corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemModuleDTO> search(String query, Pageable pageable);
}