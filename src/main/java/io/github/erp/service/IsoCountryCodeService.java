package io.github.erp.service;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.5.0
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
import io.github.erp.service.dto.IsoCountryCodeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.IsoCountryCode}.
 */
public interface IsoCountryCodeService {
    /**
     * Save a isoCountryCode.
     *
     * @param isoCountryCodeDTO the entity to save.
     * @return the persisted entity.
     */
    IsoCountryCodeDTO save(IsoCountryCodeDTO isoCountryCodeDTO);

    /**
     * Partially updates a isoCountryCode.
     *
     * @param isoCountryCodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IsoCountryCodeDTO> partialUpdate(IsoCountryCodeDTO isoCountryCodeDTO);

    /**
     * Get all the isoCountryCodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IsoCountryCodeDTO> findAll(Pageable pageable);

    /**
     * Get all the isoCountryCodes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IsoCountryCodeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" isoCountryCode.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IsoCountryCodeDTO> findOne(Long id);

    /**
     * Delete the "id" isoCountryCode.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the isoCountryCode corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IsoCountryCodeDTO> search(String query, Pageable pageable);
}
