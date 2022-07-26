package io.github.erp.service;

import io.github.erp.service.dto.ReportStatusDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ReportStatus}.
 */
public interface ReportStatusService {
    /**
     * Save a reportStatus.
     *
     * @param reportStatusDTO the entity to save.
     * @return the persisted entity.
     */
    ReportStatusDTO save(ReportStatusDTO reportStatusDTO);

    /**
     * Partially updates a reportStatus.
     *
     * @param reportStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportStatusDTO> partialUpdate(ReportStatusDTO reportStatusDTO);

    /**
     * Get all the reportStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportStatusDTO> findAll(Pageable pageable);

    /**
     * Get all the reportStatuses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportStatusDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reportStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportStatusDTO> findOne(Long id);

    /**
     * Delete the "id" reportStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reportStatus corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportStatusDTO> search(String query, Pageable pageable);
}
