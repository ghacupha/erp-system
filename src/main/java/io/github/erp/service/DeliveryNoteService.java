package io.github.erp.service;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
