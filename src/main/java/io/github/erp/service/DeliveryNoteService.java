package io.github.erp.service;

import io.github.erp.service.dto.DeliveryNoteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.DeliveryNote}.
 */
public interface DeliveryNoteService {
    /**
     * Save a deliveryNote.
     *
     * @param deliveryNoteDTO the entity to save.
     * @return the persisted entity.
     */
    DeliveryNoteDTO save(DeliveryNoteDTO deliveryNoteDTO);

    /**
     * Partially updates a deliveryNote.
     *
     * @param deliveryNoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeliveryNoteDTO> partialUpdate(DeliveryNoteDTO deliveryNoteDTO);

    /**
     * Get all the deliveryNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeliveryNoteDTO> findAll(Pageable pageable);

    /**
     * Get all the deliveryNotes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeliveryNoteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" deliveryNote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeliveryNoteDTO> findOne(Long id);

    /**
     * Delete the "id" deliveryNote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the deliveryNote corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeliveryNoteDTO> search(String query, Pageable pageable);
}
