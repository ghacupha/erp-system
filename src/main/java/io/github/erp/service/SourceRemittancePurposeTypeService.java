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
import io.github.erp.service.dto.SourceRemittancePurposeTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.SourceRemittancePurposeType}.
 */
public interface SourceRemittancePurposeTypeService {
    /**
     * Save a sourceRemittancePurposeType.
     *
     * @param sourceRemittancePurposeTypeDTO the entity to save.
     * @return the persisted entity.
     */
    SourceRemittancePurposeTypeDTO save(SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO);

    /**
     * Partially updates a sourceRemittancePurposeType.
     *
     * @param sourceRemittancePurposeTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SourceRemittancePurposeTypeDTO> partialUpdate(SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO);

    /**
     * Get all the sourceRemittancePurposeTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SourceRemittancePurposeTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sourceRemittancePurposeType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SourceRemittancePurposeTypeDTO> findOne(Long id);

    /**
     * Delete the "id" sourceRemittancePurposeType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the sourceRemittancePurposeType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SourceRemittancePurposeTypeDTO> search(String query, Pageable pageable);
}
