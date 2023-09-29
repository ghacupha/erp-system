package io.github.erp.service.impl;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.FraudCategoryFlag;
import io.github.erp.repository.FraudCategoryFlagRepository;
import io.github.erp.repository.search.FraudCategoryFlagSearchRepository;
import io.github.erp.service.FraudCategoryFlagService;
import io.github.erp.service.dto.FraudCategoryFlagDTO;
import io.github.erp.service.mapper.FraudCategoryFlagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FraudCategoryFlag}.
 */
@Service
@Transactional
public class FraudCategoryFlagServiceImpl implements FraudCategoryFlagService {

    private final Logger log = LoggerFactory.getLogger(FraudCategoryFlagServiceImpl.class);

    private final FraudCategoryFlagRepository fraudCategoryFlagRepository;

    private final FraudCategoryFlagMapper fraudCategoryFlagMapper;

    private final FraudCategoryFlagSearchRepository fraudCategoryFlagSearchRepository;

    public FraudCategoryFlagServiceImpl(
        FraudCategoryFlagRepository fraudCategoryFlagRepository,
        FraudCategoryFlagMapper fraudCategoryFlagMapper,
        FraudCategoryFlagSearchRepository fraudCategoryFlagSearchRepository
    ) {
        this.fraudCategoryFlagRepository = fraudCategoryFlagRepository;
        this.fraudCategoryFlagMapper = fraudCategoryFlagMapper;
        this.fraudCategoryFlagSearchRepository = fraudCategoryFlagSearchRepository;
    }

    @Override
    public FraudCategoryFlagDTO save(FraudCategoryFlagDTO fraudCategoryFlagDTO) {
        log.debug("Request to save FraudCategoryFlag : {}", fraudCategoryFlagDTO);
        FraudCategoryFlag fraudCategoryFlag = fraudCategoryFlagMapper.toEntity(fraudCategoryFlagDTO);
        fraudCategoryFlag = fraudCategoryFlagRepository.save(fraudCategoryFlag);
        FraudCategoryFlagDTO result = fraudCategoryFlagMapper.toDto(fraudCategoryFlag);
        fraudCategoryFlagSearchRepository.save(fraudCategoryFlag);
        return result;
    }

    @Override
    public Optional<FraudCategoryFlagDTO> partialUpdate(FraudCategoryFlagDTO fraudCategoryFlagDTO) {
        log.debug("Request to partially update FraudCategoryFlag : {}", fraudCategoryFlagDTO);

        return fraudCategoryFlagRepository
            .findById(fraudCategoryFlagDTO.getId())
            .map(existingFraudCategoryFlag -> {
                fraudCategoryFlagMapper.partialUpdate(existingFraudCategoryFlag, fraudCategoryFlagDTO);

                return existingFraudCategoryFlag;
            })
            .map(fraudCategoryFlagRepository::save)
            .map(savedFraudCategoryFlag -> {
                fraudCategoryFlagSearchRepository.save(savedFraudCategoryFlag);

                return savedFraudCategoryFlag;
            })
            .map(fraudCategoryFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudCategoryFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FraudCategoryFlags");
        return fraudCategoryFlagRepository.findAll(pageable).map(fraudCategoryFlagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FraudCategoryFlagDTO> findOne(Long id) {
        log.debug("Request to get FraudCategoryFlag : {}", id);
        return fraudCategoryFlagRepository.findById(id).map(fraudCategoryFlagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FraudCategoryFlag : {}", id);
        fraudCategoryFlagRepository.deleteById(id);
        fraudCategoryFlagSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FraudCategoryFlagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FraudCategoryFlags for query {}", query);
        return fraudCategoryFlagSearchRepository.search(query, pageable).map(fraudCategoryFlagMapper::toDto);
    }
}
