package io.github.erp.service;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.service.dto.LoanApplicationTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.LoanApplicationType}.
 */
public interface LoanApplicationTypeService {
    /**
     * Save a loanApplicationType.
     *
     * @param loanApplicationTypeDTO the entity to save.
     * @return the persisted entity.
     */
    LoanApplicationTypeDTO save(LoanApplicationTypeDTO loanApplicationTypeDTO);

    /**
     * Partially updates a loanApplicationType.
     *
     * @param loanApplicationTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LoanApplicationTypeDTO> partialUpdate(LoanApplicationTypeDTO loanApplicationTypeDTO);

    /**
     * Get all the loanApplicationTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoanApplicationTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" loanApplicationType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LoanApplicationTypeDTO> findOne(Long id);

    /**
     * Delete the "id" loanApplicationType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the loanApplicationType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LoanApplicationTypeDTO> search(String query, Pageable pageable);
}
