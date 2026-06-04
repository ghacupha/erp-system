package io.github.erp.internal.service.prepayments;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.domain.PrepaymentMarshalling;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.repository.search.PrepaymentMarshallingSearchRepository;
import io.github.erp.service.PrepaymentMarshallingService;
import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import io.github.erp.service.mapper.PrepaymentMarshallingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PrepaymentMarshalling}.
 */
@Service
@Transactional
public class InternalPrepaymentMarshallingServiceImpl implements InternalPrepaymentMarshallingService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentMarshallingServiceImpl.class);

    private final PrepaymentMarshallingRepository prepaymentMarshallingRepository;

    private final PrepaymentMarshallingMapper prepaymentMarshallingMapper;

    private final PrepaymentMarshallingSearchRepository prepaymentMarshallingSearchRepository;

    private final InternalApplicationUserDetailService applicationUserDetailService;

    public InternalPrepaymentMarshallingServiceImpl(
        PrepaymentMarshallingRepository prepaymentMarshallingRepository,
        PrepaymentMarshallingMapper prepaymentMarshallingMapper,
        PrepaymentMarshallingSearchRepository prepaymentMarshallingSearchRepository,
        InternalApplicationUserDetailService applicationUserDetailService
    ) {
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentMarshallingMapper = prepaymentMarshallingMapper;
        this.prepaymentMarshallingSearchRepository = prepaymentMarshallingSearchRepository;
        this.applicationUserDetailService = applicationUserDetailService;
    }

    @Override
    public PrepaymentMarshallingDTO save(PrepaymentMarshallingDTO prepaymentMarshallingDTO) {
        log.debug("Request to save PrepaymentMarshalling : {}", prepaymentMarshallingDTO);
        applicationUserDetailService.getCurrentApplicationUser().ifPresent(prepaymentMarshallingDTO::setCreatedBy);
        if (prepaymentMarshallingDTO.getPostingDate() == null) {
            prepaymentMarshallingDTO.setPostingDate(LocalDate.now());
        }
        PrepaymentMarshalling prepaymentMarshalling = prepaymentMarshallingMapper.toEntity(prepaymentMarshallingDTO);
        prepaymentMarshalling = prepaymentMarshallingRepository.save(prepaymentMarshalling);
        PrepaymentMarshallingDTO result = prepaymentMarshallingMapper.toDto(prepaymentMarshalling);
        prepaymentMarshallingSearchRepository.save(prepaymentMarshalling);
        return result;
    }

    @Override
    public Optional<PrepaymentMarshallingDTO> partialUpdate(PrepaymentMarshallingDTO prepaymentMarshallingDTO) {
        log.debug("Request to partially update PrepaymentMarshalling : {}", prepaymentMarshallingDTO);

        return prepaymentMarshallingRepository
            .findById(prepaymentMarshallingDTO.getId())
            .map(existingPrepaymentMarshalling -> {
                prepaymentMarshallingMapper.partialUpdate(existingPrepaymentMarshalling, prepaymentMarshallingDTO);

                return existingPrepaymentMarshalling;
            })
            .map(prepaymentMarshallingRepository::save)
            .map(savedPrepaymentMarshalling -> {
                prepaymentMarshallingSearchRepository.save(savedPrepaymentMarshalling);

                return savedPrepaymentMarshalling;
            })
            .map(prepaymentMarshallingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentMarshallingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentMarshallings");
        return prepaymentMarshallingRepository.findAll(pageable).map(prepaymentMarshallingMapper::toDto);
    }

    public Page<PrepaymentMarshallingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prepaymentMarshallingRepository.findAllWithEagerRelationships(pageable).map(prepaymentMarshallingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentMarshallingDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentMarshalling : {}", id);
        return prepaymentMarshallingRepository.findOneWithEagerRelationships(id).map(prepaymentMarshallingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentMarshalling : {}", id);
        prepaymentMarshallingRepository.deleteById(id);
        prepaymentMarshallingSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentMarshallingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentMarshallings for query {}", query);
        return prepaymentMarshallingSearchRepository.search(query, pageable).map(prepaymentMarshallingMapper::toDto);
    }
}
