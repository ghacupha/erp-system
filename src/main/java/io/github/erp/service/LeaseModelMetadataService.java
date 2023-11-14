package io.github.erp.service;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.service.dto.LeaseModelMetadataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.LeaseModelMetadata}.
 */
public interface LeaseModelMetadataService {
    /**
     * Save a leaseModelMetadata.
     *
     * @param leaseModelMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseModelMetadataDTO save(LeaseModelMetadataDTO leaseModelMetadataDTO);

    /**
     * Partially updates a leaseModelMetadata.
     *
     * @param leaseModelMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaseModelMetadataDTO> partialUpdate(LeaseModelMetadataDTO leaseModelMetadataDTO);

    /**
     * Get all the leaseModelMetadata.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseModelMetadataDTO> findAll(Pageable pageable);

    /**
     * Get all the leaseModelMetadata with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseModelMetadataDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" leaseModelMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseModelMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" leaseModelMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leaseModelMetadata corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseModelMetadataDTO> search(String query, Pageable pageable);
}
