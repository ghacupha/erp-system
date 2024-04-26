package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.PrepaymentMarshalling}.
 */
public interface PrepaymentMarshallingService {
    /**
     * Save a prepaymentMarshalling.
     *
     * @param prepaymentMarshallingDTO the entity to save.
     * @return the persisted entity.
     */
    PrepaymentMarshallingDTO save(PrepaymentMarshallingDTO prepaymentMarshallingDTO);

    /**
     * Partially updates a prepaymentMarshalling.
     *
     * @param prepaymentMarshallingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrepaymentMarshallingDTO> partialUpdate(PrepaymentMarshallingDTO prepaymentMarshallingDTO);

    /**
     * Get all the prepaymentMarshallings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMarshallingDTO> findAll(Pageable pageable);

    /**
     * Get all the prepaymentMarshallings with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMarshallingDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" prepaymentMarshalling.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrepaymentMarshallingDTO> findOne(Long id);

    /**
     * Delete the "id" prepaymentMarshalling.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the prepaymentMarshalling corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrepaymentMarshallingDTO> search(String query, Pageable pageable);
}
