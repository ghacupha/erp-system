package io.github.erp.service;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.1.9-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.service.dto.BusinessDocumentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.BusinessDocument}.
 */
public interface BusinessDocumentService {
    /**
     * Save a businessDocument.
     *
     * @param businessDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    BusinessDocumentDTO save(BusinessDocumentDTO businessDocumentDTO);

    /**
     * Partially updates a businessDocument.
     *
     * @param businessDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessDocumentDTO> partialUpdate(BusinessDocumentDTO businessDocumentDTO);

    /**
     * Get all the businessDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessDocumentDTO> findAll(Pageable pageable);

    /**
     * Get all the businessDocuments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessDocumentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" businessDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" businessDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the businessDocument corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessDocumentDTO> search(String query, Pageable pageable);
}
