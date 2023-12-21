package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.InterestCalcMethod;
import io.github.erp.repository.InterestCalcMethodRepository;
import io.github.erp.repository.search.InterestCalcMethodSearchRepository;
import io.github.erp.service.InterestCalcMethodService;
import io.github.erp.service.dto.InterestCalcMethodDTO;
import io.github.erp.service.mapper.InterestCalcMethodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InterestCalcMethod}.
 */
@Service
@Transactional
public class InterestCalcMethodServiceImpl implements InterestCalcMethodService {

    private final Logger log = LoggerFactory.getLogger(InterestCalcMethodServiceImpl.class);

    private final InterestCalcMethodRepository interestCalcMethodRepository;

    private final InterestCalcMethodMapper interestCalcMethodMapper;

    private final InterestCalcMethodSearchRepository interestCalcMethodSearchRepository;

    public InterestCalcMethodServiceImpl(
        InterestCalcMethodRepository interestCalcMethodRepository,
        InterestCalcMethodMapper interestCalcMethodMapper,
        InterestCalcMethodSearchRepository interestCalcMethodSearchRepository
    ) {
        this.interestCalcMethodRepository = interestCalcMethodRepository;
        this.interestCalcMethodMapper = interestCalcMethodMapper;
        this.interestCalcMethodSearchRepository = interestCalcMethodSearchRepository;
    }

    @Override
    public InterestCalcMethodDTO save(InterestCalcMethodDTO interestCalcMethodDTO) {
        log.debug("Request to save InterestCalcMethod : {}", interestCalcMethodDTO);
        InterestCalcMethod interestCalcMethod = interestCalcMethodMapper.toEntity(interestCalcMethodDTO);
        interestCalcMethod = interestCalcMethodRepository.save(interestCalcMethod);
        InterestCalcMethodDTO result = interestCalcMethodMapper.toDto(interestCalcMethod);
        interestCalcMethodSearchRepository.save(interestCalcMethod);
        return result;
    }

    @Override
    public Optional<InterestCalcMethodDTO> partialUpdate(InterestCalcMethodDTO interestCalcMethodDTO) {
        log.debug("Request to partially update InterestCalcMethod : {}", interestCalcMethodDTO);

        return interestCalcMethodRepository
            .findById(interestCalcMethodDTO.getId())
            .map(existingInterestCalcMethod -> {
                interestCalcMethodMapper.partialUpdate(existingInterestCalcMethod, interestCalcMethodDTO);

                return existingInterestCalcMethod;
            })
            .map(interestCalcMethodRepository::save)
            .map(savedInterestCalcMethod -> {
                interestCalcMethodSearchRepository.save(savedInterestCalcMethod);

                return savedInterestCalcMethod;
            })
            .map(interestCalcMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterestCalcMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InterestCalcMethods");
        return interestCalcMethodRepository.findAll(pageable).map(interestCalcMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InterestCalcMethodDTO> findOne(Long id) {
        log.debug("Request to get InterestCalcMethod : {}", id);
        return interestCalcMethodRepository.findById(id).map(interestCalcMethodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InterestCalcMethod : {}", id);
        interestCalcMethodRepository.deleteById(id);
        interestCalcMethodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterestCalcMethodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InterestCalcMethods for query {}", query);
        return interestCalcMethodSearchRepository.search(query, pageable).map(interestCalcMethodMapper::toDto);
    }
}
