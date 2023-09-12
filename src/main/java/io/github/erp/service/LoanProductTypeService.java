package io.github.erp.service;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.service.dto.LoanProductTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.LoanProductType}.
 */
public interface LoanProductTypeService {
    /**
     * Save a loanProductType.
     *
     * @param loanProductTypeDTO the entity to save.
     * @return the persisted entity.
     */
    LoanProductTypeDTO save(LoanProductTypeDTO loanProductTypeDTO);

    /**
     * Partially updates a loanProductType.
     *
     * @param loanProductTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoanProductTypeDTO> partialUpdate(LoanProductTypeDTO loanProductTypeDTO);

    /**
     * Get all the loanProductTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoanProductTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loanProductType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoanProductTypeDTO> findOne(Long id);

    /**
     * Delete the "id" loanProductType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the loanProductType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoanProductTypeDTO> search(String query, Pageable pageable);
}
