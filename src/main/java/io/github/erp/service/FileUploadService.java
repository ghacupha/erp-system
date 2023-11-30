package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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

import io.github.erp.service.dto.FileUploadDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.FileUpload}.
 */
public interface FileUploadService {
    /**
     * Save a fileUpload.
     *
     * @param fileUploadDTO the entity to save.
     * @return the persisted entity.
     */
    FileUploadDTO save(FileUploadDTO fileUploadDTO);

    /**
     * Partially updates a fileUpload.
     *
     * @param fileUploadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileUploadDTO> partialUpdate(FileUploadDTO fileUploadDTO);

    /**
     * Get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileUploadDTO> findAll(Pageable pageable);

    /**
     * Get all the fileUploads with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileUploadDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" fileUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileUploadDTO> findOne(Long id);

    /**
     * Delete the "id" fileUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the fileUpload corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileUploadDTO> search(String query, Pageable pageable);
}
