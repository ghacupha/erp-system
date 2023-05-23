package io.github.erp.service;

import io.github.erp.service.dto.ReportTemplateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ReportTemplate}.
 */
public interface ReportTemplateService {
    /**
     * Save a reportTemplate.
     *
     * @param reportTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    ReportTemplateDTO save(ReportTemplateDTO reportTemplateDTO);

    /**
     * Partially updates a reportTemplate.
     *
     * @param reportTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportTemplateDTO> partialUpdate(ReportTemplateDTO reportTemplateDTO);

    /**
     * Get all the reportTemplates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportTemplateDTO> findAll(Pageable pageable);

    /**
     * Get all the reportTemplates with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportTemplateDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reportTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportTemplateDTO> findOne(Long id);

    /**
     * Delete the "id" reportTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reportTemplate corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportTemplateDTO> search(String query, Pageable pageable);
}
