package io.github.erp.service;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
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
import io.github.erp.service.dto.CustomerIDDocumentTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.CustomerIDDocumentType}.
 */
public interface CustomerIDDocumentTypeService {
    /**
     * Save a customerIDDocumentType.
     *
     * @param customerIDDocumentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerIDDocumentTypeDTO save(CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO);

    /**
     * Partially updates a customerIDDocumentType.
     *
     * @param customerIDDocumentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerIDDocumentTypeDTO> partialUpdate(CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO);

    /**
     * Get all the customerIDDocumentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerIDDocumentTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the customerIDDocumentTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerIDDocumentTypeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" customerIDDocumentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerIDDocumentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" customerIDDocumentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the customerIDDocumentType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerIDDocumentTypeDTO> search(String query, Pageable pageable);
}
