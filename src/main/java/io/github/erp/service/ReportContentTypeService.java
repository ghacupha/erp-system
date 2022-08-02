package io.github.erp.service;

import io.github.erp.service.dto.ReportContentTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ReportContentType}.
 */
public interface ReportContentTypeService {
    /**
     * Save a reportContentType.
     *
     * @param reportContentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ReportContentTypeDTO save(ReportContentTypeDTO reportContentTypeDTO);

    /**
     * Partially updates a reportContentType.
     *
     * @param reportContentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportContentTypeDTO> partialUpdate(ReportContentTypeDTO reportContentTypeDTO);

    /**
     * Get all the reportContentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportContentTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the reportContentTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportContentTypeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reportContentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportContentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" reportContentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reportContentType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportContentTypeDTO> search(String query, Pageable pageable);
}
