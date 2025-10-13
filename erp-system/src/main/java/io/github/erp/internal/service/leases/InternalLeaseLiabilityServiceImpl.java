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
import io.github.erp.domain.LeaseLiability;
import io.github.erp.internal.repository.InternalLeaseLiabilityRepository;
import io.github.erp.repository.search.LeaseLiabilitySearchRepository;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import io.github.erp.service.mapper.LeaseLiabilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaseLiability}.
 */
@Service
@Transactional
public class InternalLeaseLiabilityServiceImpl implements InternalLeaseLiabilityService {

    private final Logger log = LoggerFactory.getLogger(InternalLeaseLiabilityServiceImpl.class);

    private final InternalLeaseLiabilityRepository leaseLiabilityRepository;

    private final LeaseLiabilityMapper leaseLiabilityMapper;

    private final LeaseLiabilitySearchRepository leaseLiabilitySearchRepository;

    public InternalLeaseLiabilityServiceImpl(
        InternalLeaseLiabilityRepository leaseLiabilityRepository,
        LeaseLiabilityMapper leaseLiabilityMapper,
        LeaseLiabilitySearchRepository leaseLiabilitySearchRepository
    ) {
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.leaseLiabilityMapper = leaseLiabilityMapper;
        this.leaseLiabilitySearchRepository = leaseLiabilitySearchRepository;
    }

    @Override
    public LeaseLiabilityDTO save(LeaseLiabilityDTO leaseLiabilityDTO) {
        log.debug("Request to save LeaseLiability : {}", leaseLiabilityDTO);
        LeaseLiability leaseLiability = leaseLiabilityMapper.toEntity(leaseLiabilityDTO);
        leaseLiability = leaseLiabilityRepository.save(leaseLiability);
        LeaseLiabilityDTO result = leaseLiabilityMapper.toDto(leaseLiability);
        leaseLiabilitySearchRepository.save(leaseLiability);
        return result;
    }

    @Override
    public LeaseLiabilityDTO update(LeaseLiabilityDTO leaseLiabilityDTO) {
        log.debug("Request to save LeaseLiability : {}", leaseLiabilityDTO);
        LeaseLiability leaseLiability = leaseLiabilityMapper.toEntity(leaseLiabilityDTO);
        leaseLiability = leaseLiabilityRepository.save(leaseLiability);
        return leaseLiabilityMapper.toDto(leaseLiability);
    }

    @Override
    public Optional<LeaseLiabilityDTO> partialUpdate(LeaseLiabilityDTO leaseLiabilityDTO) {
        log.debug("Request to partially update LeaseLiability : {}", leaseLiabilityDTO);

        return leaseLiabilityRepository
            .findById(leaseLiabilityDTO.getId())
            .map(existingLeaseLiability -> {
                leaseLiabilityMapper.partialUpdate(existingLeaseLiability, leaseLiabilityDTO);

                return existingLeaseLiability;
            })
            .map(leaseLiabilityRepository::save)
            .map(savedLeaseLiability -> {
                leaseLiabilitySearchRepository.save(savedLeaseLiability);

                return savedLeaseLiability;
            })
            .map(leaseLiabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseLiabilities");
        return leaseLiabilityRepository.findAll(pageable).map(leaseLiabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseLiabilityDTO> findOne(Long id) {
        log.debug("Request to get LeaseLiability : {}", id);
        return leaseLiabilityRepository.findById(id).map(leaseLiabilityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseLiability : {}", id);
        leaseLiabilityRepository.deleteById(id);
        leaseLiabilitySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseLiabilities for query {}", query);
        return leaseLiabilitySearchRepository.search(query, pageable).map(leaseLiabilityMapper::toDto);
    }

    /**
     * Lease liability items due for lease liability amortization
     *
     * @param leaseLiabilityCompilationRequestId id of the requisition entity instance
     * @param batchJobIdentifier                 id of the current compilation spring batch job
     * @return List of leases for processing
     */
    @Override
    public Optional<List<LeaseLiabilityDTO>> getCompilationAdjacentMetadataItems(long leaseLiabilityCompilationRequestId, String batchJobIdentifier) {

        return Optional.of(leaseLiabilityRepository.findAll())
            .map(leaseLiabilityMapper::toDto);
    }
}
