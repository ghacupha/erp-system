package io.github.erp.service;

/*-
 * Erp System - Mark III No 13 (Caleb Series) Server ver 1.1.3-SNAPSHOT
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
import io.github.erp.service.dto.ContractMetadataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ContractMetadata}.
 */
public interface ContractMetadataService {
    /**
     * Save a contractMetadata.
     *
     * @param contractMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    ContractMetadataDTO save(ContractMetadataDTO contractMetadataDTO);

    /**
     * Partially updates a contractMetadata.
     *
     * @param contractMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContractMetadataDTO> partialUpdate(ContractMetadataDTO contractMetadataDTO);

    /**
     * Get all the contractMetadata.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractMetadataDTO> findAll(Pageable pageable);

    /**
     * Get all the contractMetadata with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractMetadataDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" contractMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContractMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" contractMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contractMetadata corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractMetadataDTO> search(String query, Pageable pageable);
}
