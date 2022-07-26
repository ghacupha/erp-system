package io.github.erp.service;

import io.github.erp.service.dto.SystemContentTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SystemContentType}.
 */
public interface SystemContentTypeService {
    /**
     * Save a systemContentType.
     *
     * @param systemContentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    SystemContentTypeDTO save(SystemContentTypeDTO systemContentTypeDTO);

    /**
     * Partially updates a systemContentType.
     *
     * @param systemContentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemContentTypeDTO> partialUpdate(SystemContentTypeDTO systemContentTypeDTO);

    /**
     * Get all the systemContentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemContentTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the systemContentTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemContentTypeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" systemContentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemContentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" systemContentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the systemContentType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemContentTypeDTO> search(String query, Pageable pageable);
}
