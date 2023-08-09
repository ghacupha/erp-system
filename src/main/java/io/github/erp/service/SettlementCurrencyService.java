package io.github.erp.service;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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
import io.github.erp.service.dto.SettlementCurrencyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SettlementCurrency}.
 */
public interface SettlementCurrencyService {
    /**
     * Save a settlementCurrency.
     *
     * @param settlementCurrencyDTO the entity to save.
     * @return the persisted entity.
     */
    SettlementCurrencyDTO save(SettlementCurrencyDTO settlementCurrencyDTO);

    /**
     * Partially updates a settlementCurrency.
     *
     * @param settlementCurrencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SettlementCurrencyDTO> partialUpdate(SettlementCurrencyDTO settlementCurrencyDTO);

    /**
     * Get all the settlementCurrencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementCurrencyDTO> findAll(Pageable pageable);

    /**
     * Get all the settlementCurrencies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementCurrencyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" settlementCurrency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SettlementCurrencyDTO> findOne(Long id);

    /**
     * Delete the "id" settlementCurrency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the settlementCurrency corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementCurrencyDTO> search(String query, Pageable pageable);
}
