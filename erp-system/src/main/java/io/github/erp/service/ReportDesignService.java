package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.service.dto.ReportDesignDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.ReportDesign}.
 */
public interface ReportDesignService {
    /**
     * Save a reportDesign.
     *
     * @param reportDesignDTO the entity to save.
     * @return the persisted entity.
     */
    ReportDesignDTO save(ReportDesignDTO reportDesignDTO);

    /**
     * Partially updates a reportDesign.
     *
     * @param reportDesignDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportDesignDTO> partialUpdate(ReportDesignDTO reportDesignDTO);

    /**
     * Get all the reportDesigns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportDesignDTO> findAll(Pageable pageable);

    /**
     * Get all the reportDesigns with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportDesignDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reportDesign.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportDesignDTO> findOne(Long id);

    /**
     * Delete the "id" reportDesign.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reportDesign corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportDesignDTO> search(String query, Pageable pageable);
}
