package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.service.dto.AgencyNoticeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.AgencyNotice}.
 */
public interface AgencyNoticeService {
    /**
     * Save a agencyNotice.
     *
     * @param agencyNoticeDTO the entity to save.
     * @return the persisted entity.
     */
    AgencyNoticeDTO save(AgencyNoticeDTO agencyNoticeDTO);

    /**
     * Partially updates a agencyNotice.
     *
     * @param agencyNoticeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgencyNoticeDTO> partialUpdate(AgencyNoticeDTO agencyNoticeDTO);

    /**
     * Get all the agencyNotices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyNoticeDTO> findAll(Pageable pageable);

    /**
     * Get all the agencyNotices with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyNoticeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" agencyNotice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgencyNoticeDTO> findOne(Long id);

    /**
     * Delete the "id" agencyNotice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the agencyNotice corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyNoticeDTO> search(String query, Pageable pageable);
}
