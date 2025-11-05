package io.github.erp.internal.service.leases;

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
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.internal.service.applicationUser.CurrentUserContext;
import io.github.erp.internal.service.leases.InternalLeaseLiabilityScheduleItemService;
import io.github.erp.repository.LeaseLiabilityCompilationRepository;
import io.github.erp.repository.search.LeaseLiabilityCompilationSearchRepository;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.mapper.LeaseLiabilityCompilationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaseLiabilityCompilation}.
 */
@Service
@Transactional
public class InternalLeaseLiabilityCompilationServiceImpl implements InternalLeaseLiabilityCompilationService {

    private final Logger log = LoggerFactory.getLogger(InternalLeaseLiabilityCompilationServiceImpl.class);

    private final LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository;

    private final LeaseLiabilityCompilationMapper leaseLiabilityCompilationMapper;

    private final LeaseLiabilityCompilationSearchRepository leaseLiabilityCompilationSearchRepository;

    private final InternalLeaseLiabilityScheduleItemService leaseLiabilityScheduleItemService;

    public InternalLeaseLiabilityCompilationServiceImpl(
        LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository,
        LeaseLiabilityCompilationMapper leaseLiabilityCompilationMapper,
        LeaseLiabilityCompilationSearchRepository leaseLiabilityCompilationSearchRepository,
        InternalLeaseLiabilityScheduleItemService leaseLiabilityScheduleItemService
    ) {
        this.leaseLiabilityCompilationRepository = leaseLiabilityCompilationRepository;
        this.leaseLiabilityCompilationMapper = leaseLiabilityCompilationMapper;
        this.leaseLiabilityCompilationSearchRepository = leaseLiabilityCompilationSearchRepository;
        this.leaseLiabilityScheduleItemService = leaseLiabilityScheduleItemService;
    }

    @Override
    public LeaseLiabilityCompilationDTO save(LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO) {
        log.debug("Request to save LeaseLiabilityCompilation : {}", leaseLiabilityCompilationDTO);

        LeaseLiabilityCompilation leaseLiabilityCompilation = leaseLiabilityCompilationMapper.toEntity(leaseLiabilityCompilationDTO);

        leaseLiabilityCompilation.setRequestedBy(CurrentUserContext.getCurrentUser());

        leaseLiabilityCompilation = leaseLiabilityCompilationRepository.save(leaseLiabilityCompilation);
        LeaseLiabilityCompilationDTO result = leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilation);
        leaseLiabilityCompilationSearchRepository.save(leaseLiabilityCompilation);
        return result;
    }

    @Override
    public Optional<LeaseLiabilityCompilationDTO> partialUpdate(LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO) {
        log.debug("Request to partially update LeaseLiabilityCompilation : {}", leaseLiabilityCompilationDTO);

        return leaseLiabilityCompilationRepository
            .findById(leaseLiabilityCompilationDTO.getId())
            .map(existingLeaseLiabilityCompilation -> {
                leaseLiabilityCompilationMapper.partialUpdate(existingLeaseLiabilityCompilation, leaseLiabilityCompilationDTO);

                return existingLeaseLiabilityCompilation;
            })
            .map(leaseLiabilityCompilationRepository::save)
            .map(savedLeaseLiabilityCompilation -> {
                leaseLiabilityCompilationSearchRepository.save(savedLeaseLiabilityCompilation);

                return savedLeaseLiabilityCompilation;
            })
            .map(leaseLiabilityCompilationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityCompilationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilityCompilations");
        return leaseLiabilityCompilationRepository.findAll(pageable).map(leaseLiabilityCompilationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityCompilationDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiabilityCompilation : {}", id);
        return leaseLiabilityCompilationRepository.findById(id).map(leaseLiabilityCompilationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiabilityCompilation : {}", id);
        leaseLiabilityCompilationRepository.deleteById(id);
        leaseLiabilityCompilationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityCompilationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilityCompilations for query {}", query);
        return leaseLiabilityCompilationSearchRepository.search(query, pageable).map(leaseLiabilityCompilationMapper::toDto);
    }

    @Override
    public int updateScheduleItemActivation(Long compilationId, boolean active) {
        log.debug("Request to update activation flag for compilation {} to {}", compilationId, active);
        return leaseLiabilityScheduleItemService.updateActivationByCompilation(compilationId, active);
    }
}
