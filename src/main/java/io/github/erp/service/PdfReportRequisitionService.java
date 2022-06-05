package io.github.erp.service;

import io.github.erp.service.dto.PdfReportRequisitionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PdfReportRequisition}.
 */
public interface PdfReportRequisitionService {
    /**
     * Save a pdfReportRequisition.
     *
     * @param pdfReportRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    PdfReportRequisitionDTO save(PdfReportRequisitionDTO pdfReportRequisitionDTO);

    /**
     * Partially updates a pdfReportRequisition.
     *
     * @param pdfReportRequisitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PdfReportRequisitionDTO> partialUpdate(PdfReportRequisitionDTO pdfReportRequisitionDTO);

    /**
     * Get all the pdfReportRequisitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PdfReportRequisitionDTO> findAll(Pageable pageable);

    /**
     * Get all the pdfReportRequisitions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PdfReportRequisitionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pdfReportRequisition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PdfReportRequisitionDTO> findOne(Long id);

    /**
     * Delete the "id" pdfReportRequisition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the pdfReportRequisition corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PdfReportRequisitionDTO> search(String query, Pageable pageable);
}
