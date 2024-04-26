package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.service.dto.BusinessTeamDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.BusinessTeam}.
 */
public interface BusinessTeamService {
    /**
     * Save a businessTeam.
     *
     * @param businessTeamDTO the entity to save.
     * @return the persisted entity.
     */
    BusinessTeamDTO save(BusinessTeamDTO businessTeamDTO);

    /**
     * Partially updates a businessTeam.
     *
     * @param businessTeamDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessTeamDTO> partialUpdate(BusinessTeamDTO businessTeamDTO);

    /**
     * Get all the businessTeams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessTeamDTO> findAll(Pageable pageable);

    /**
     * Get the "id" businessTeam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessTeamDTO> findOne(Long id);

    /**
     * Delete the "id" businessTeam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the businessTeam corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessTeamDTO> search(String query, Pageable pageable);
}
