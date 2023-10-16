package io.github.erp.service;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.service.dto.AccountAttributeMetadataDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.AccountAttributeMetadata}.
 */
public interface AccountAttributeMetadataService {
    /**
     * Save a accountAttributeMetadata.
     *
     * @param accountAttributeMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    AccountAttributeMetadataDTO save(AccountAttributeMetadataDTO accountAttributeMetadataDTO);

    /**
     * Partially updates a accountAttributeMetadata.
     *
     * @param accountAttributeMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountAttributeMetadataDTO> partialUpdate(AccountAttributeMetadataDTO accountAttributeMetadataDTO);

    /**
     * Get all the accountAttributeMetadata.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountAttributeMetadataDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accountAttributeMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountAttributeMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" accountAttributeMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the accountAttributeMetadata corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountAttributeMetadataDTO> search(String query, Pageable pageable);
}
