package io.github.erp.internal.service.prepayments;

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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import io.github.erp.internal.service.ConfigurationMappingNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.erp.domain.PrepaymentMapping;
import io.github.erp.internal.repository.InternalPrepaymentMappingRepository;
import io.github.erp.repository.PrepaymentMappingRepository;
import io.github.erp.repository.search.PrepaymentMappingSearchRepository;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import io.github.erp.service.mapper.PrepaymentMappingMapper;

/**
 * Custom service for prepayment-mapping with access to the internal-prepayment-mapping-repository
 *
 * This is currently mapped as primary due to ambiguity with the original class,Should problems persist
 *  the plan is to implement a similar API but expose it via a different but similar interface in the
 *  services.
 */

@Service("internalPrepaymentMappingService")
@Transactional
public class InternalPrepaymentMappingServiceImpl implements InternalPrepaymentMappingService {

    private static final Logger log = LoggerFactory.getLogger(InternalPrepaymentMappingService.class);

    private final PrepaymentMappingMapper prepaymentMappingMapper;
    private final InternalPrepaymentMappingRepository internalPrepaymentMappingRepository;

    private final PrepaymentMappingSearchRepository prepaymentMappingSearchRepository;

    public InternalPrepaymentMappingServiceImpl(
        InternalPrepaymentMappingRepository internalPrepaymentMappingRepository,
        PrepaymentMappingRepository prepaymentMappingRepository,
        PrepaymentMappingMapper prepaymentMappingMapper,
        PrepaymentMappingSearchRepository prepaymentMappingSearchRepository, PrepaymentMappingSearchRepository prepaymentMappingSearchRepository1) {
       this.internalPrepaymentMappingRepository = internalPrepaymentMappingRepository;
       this.prepaymentMappingMapper = prepaymentMappingMapper;

        this.prepaymentMappingSearchRepository = prepaymentMappingSearchRepository1;
    }

    @Override
    public Optional<PrepaymentMappingDTO> getMapping(String parameterKey) {
        AtomicReference<PrepaymentMapping> val = new AtomicReference<>();
        internalPrepaymentMappingRepository.findByParameterKeyEquals(parameterKey).ifPresentOrElse(
            val::set,
            () -> {throw new ConfigurationMappingNotFoundException("Sorry, couldn't find mapping for " + parameterKey + " Are sure you have that configured?"); }
        );
        return Optional.of(prepaymentMappingMapper.toDto(val.get()));
    }

    @Override
    public PrepaymentMappingDTO save(PrepaymentMappingDTO prepaymentMappingDTO) {
        log.debug("Request to save PrepaymentMapping : {}", prepaymentMappingDTO);
        PrepaymentMapping prepaymentMapping = prepaymentMappingMapper.toEntity(prepaymentMappingDTO);
        prepaymentMapping = internalPrepaymentMappingRepository.save(prepaymentMapping);
        PrepaymentMappingDTO result = prepaymentMappingMapper.toDto(prepaymentMapping);
        prepaymentMappingSearchRepository.save(prepaymentMapping);
        return result;
    }

    @Override
    public Optional<PrepaymentMappingDTO> partialUpdate(PrepaymentMappingDTO prepaymentMappingDTO) {
        log.debug("Request to partially update PrepaymentMapping : {}", prepaymentMappingDTO);

        return internalPrepaymentMappingRepository
            .findById(prepaymentMappingDTO.getId())
            .map(existingPrepaymentMapping -> {
                prepaymentMappingMapper.partialUpdate(existingPrepaymentMapping, prepaymentMappingDTO);

                return existingPrepaymentMapping;
            })
            .map(internalPrepaymentMappingRepository::save)
            .map(savedPrepaymentMapping -> {
                prepaymentMappingSearchRepository.save(savedPrepaymentMapping);

                return savedPrepaymentMapping;
            })
            .map(prepaymentMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentMappingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentMappings");
        return internalPrepaymentMappingRepository.findAll(pageable).map(prepaymentMappingMapper::toDto);
    }

    public Page<PrepaymentMappingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return internalPrepaymentMappingRepository.findAllWithEagerRelationships(pageable).map(prepaymentMappingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentMappingDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentMapping : {}", id);
        return internalPrepaymentMappingRepository.findOneWithEagerRelationships(id).map(prepaymentMappingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentMapping : {}", id);
        internalPrepaymentMappingRepository.deleteById(id);
        prepaymentMappingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentMappingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentMappings for query {}", query);
        return prepaymentMappingSearchRepository.search(query, pageable).map(prepaymentMappingMapper::toDto);
    }

}
