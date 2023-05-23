package io.github.erp.service;

import io.github.erp.service.dto.CreditNoteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CreditNote}.
 */
public interface CreditNoteService {
    /**
     * Save a creditNote.
     *
     * @param creditNoteDTO the entity to save.
     * @return the persisted entity.
     */
    CreditNoteDTO save(CreditNoteDTO creditNoteDTO);

    /**
     * Partially updates a creditNote.
     *
     * @param creditNoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CreditNoteDTO> partialUpdate(CreditNoteDTO creditNoteDTO);

    /**
     * Get all the creditNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CreditNoteDTO> findAll(Pageable pageable);

    /**
     * Get all the creditNotes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CreditNoteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" creditNote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CreditNoteDTO> findOne(Long id);

    /**
     * Delete the "id" creditNote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the creditNote corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CreditNoteDTO> search(String query, Pageable pageable);
}
