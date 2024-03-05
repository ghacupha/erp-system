package io.github.erp.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.service.dto.SecurityTenureDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SecurityTenure}.
 */
public interface SecurityTenureService {
    /**
     * Save a securityTenure.
     *
     * @param securityTenureDTO the entity to save.
     * @return the persisted entity.
     */
    SecurityTenureDTO save(SecurityTenureDTO securityTenureDTO);

    /**
     * Partially updates a securityTenure.
     *
     * @param securityTenureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SecurityTenureDTO> partialUpdate(SecurityTenureDTO securityTenureDTO);

    /**
     * Get all the securityTenures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SecurityTenureDTO> findAll(Pageable pageable);

    /**
     * Get the "id" securityTenure.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SecurityTenureDTO> findOne(Long id);

    /**
     * Delete the "id" securityTenure.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the securityTenure corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SecurityTenureDTO> search(String query, Pageable pageable);
}
