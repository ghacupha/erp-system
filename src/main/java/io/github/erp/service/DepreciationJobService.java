package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.service.dto.DepreciationJobDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.DepreciationJob}.
 */
public interface DepreciationJobService {
    /**
     * Save a depreciationJob.
     *
     * @param depreciationJobDTO the entity to save.
     * @return the persisted entity.
     */
    DepreciationJobDTO save(DepreciationJobDTO depreciationJobDTO);

    /**
     * Partially updates a depreciationJob.
     *
     * @param depreciationJobDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DepreciationJobDTO> partialUpdate(DepreciationJobDTO depreciationJobDTO);

    /**
     * Get all the depreciationJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationJobDTO> findAll(Pageable pageable);

    /**
     * Get the "id" depreciationJob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DepreciationJobDTO> findOne(Long id);

    /**
     * Delete the "id" depreciationJob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the depreciationJob corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DepreciationJobDTO> search(String query, Pageable pageable);
}
