package io.github.erp.service;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.service.dto.ReportContentTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ReportContentType}.
 */
public interface ReportContentTypeService {
    /**
     * Save a reportContentType.
     *
     * @param reportContentTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ReportContentTypeDTO save(ReportContentTypeDTO reportContentTypeDTO);

    /**
     * Partially updates a reportContentType.
     *
     * @param reportContentTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportContentTypeDTO> partialUpdate(ReportContentTypeDTO reportContentTypeDTO);

    /**
     * Get all the reportContentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportContentTypeDTO> findAll(Pageable pageable);

    /**
     * Get all the reportContentTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportContentTypeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reportContentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportContentTypeDTO> findOne(Long id);

    /**
     * Delete the "id" reportContentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reportContentType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportContentTypeDTO> search(String query, Pageable pageable);
}
