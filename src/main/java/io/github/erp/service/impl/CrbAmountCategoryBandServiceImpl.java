package io.github.erp.service.impl;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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

import io.github.erp.domain.CrbAmountCategoryBand;
import io.github.erp.repository.CrbAmountCategoryBandRepository;
import io.github.erp.repository.search.CrbAmountCategoryBandSearchRepository;
import io.github.erp.service.CrbAmountCategoryBandService;
import io.github.erp.service.dto.CrbAmountCategoryBandDTO;
import io.github.erp.service.mapper.CrbAmountCategoryBandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CrbAmountCategoryBand}.
 */
@Service
@Transactional
public class CrbAmountCategoryBandServiceImpl implements CrbAmountCategoryBandService {

    private final Logger log = LoggerFactory.getLogger(CrbAmountCategoryBandServiceImpl.class);

    private final CrbAmountCategoryBandRepository crbAmountCategoryBandRepository;

    private final CrbAmountCategoryBandMapper crbAmountCategoryBandMapper;

    private final CrbAmountCategoryBandSearchRepository crbAmountCategoryBandSearchRepository;

    public CrbAmountCategoryBandServiceImpl(
        CrbAmountCategoryBandRepository crbAmountCategoryBandRepository,
        CrbAmountCategoryBandMapper crbAmountCategoryBandMapper,
        CrbAmountCategoryBandSearchRepository crbAmountCategoryBandSearchRepository
    ) {
        this.crbAmountCategoryBandRepository = crbAmountCategoryBandRepository;
        this.crbAmountCategoryBandMapper = crbAmountCategoryBandMapper;
        this.crbAmountCategoryBandSearchRepository = crbAmountCategoryBandSearchRepository;
    }

    @Override
    public CrbAmountCategoryBandDTO save(CrbAmountCategoryBandDTO crbAmountCategoryBandDTO) {
        log.debug("Request to save CrbAmountCategoryBand : {}", crbAmountCategoryBandDTO);
        CrbAmountCategoryBand crbAmountCategoryBand = crbAmountCategoryBandMapper.toEntity(crbAmountCategoryBandDTO);
        crbAmountCategoryBand = crbAmountCategoryBandRepository.save(crbAmountCategoryBand);
        CrbAmountCategoryBandDTO result = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);
        crbAmountCategoryBandSearchRepository.save(crbAmountCategoryBand);
        return result;
    }

    @Override
    public Optional<CrbAmountCategoryBandDTO> partialUpdate(CrbAmountCategoryBandDTO crbAmountCategoryBandDTO) {
        log.debug("Request to partially update CrbAmountCategoryBand : {}", crbAmountCategoryBandDTO);

        return crbAmountCategoryBandRepository
            .findById(crbAmountCategoryBandDTO.getId())
            .map(existingCrbAmountCategoryBand -> {
                crbAmountCategoryBandMapper.partialUpdate(existingCrbAmountCategoryBand, crbAmountCategoryBandDTO);

                return existingCrbAmountCategoryBand;
            })
            .map(crbAmountCategoryBandRepository::save)
            .map(savedCrbAmountCategoryBand -> {
                crbAmountCategoryBandSearchRepository.save(savedCrbAmountCategoryBand);

                return savedCrbAmountCategoryBand;
            })
            .map(crbAmountCategoryBandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAmountCategoryBandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CrbAmountCategoryBands");
        return crbAmountCategoryBandRepository.findAll(pageable).map(crbAmountCategoryBandMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrbAmountCategoryBandDTO> findOne(Long id) {
        log.debug("Request to get CrbAmountCategoryBand : {}", id);
        return crbAmountCategoryBandRepository.findById(id).map(crbAmountCategoryBandMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CrbAmountCategoryBand : {}", id);
        crbAmountCategoryBandRepository.deleteById(id);
        crbAmountCategoryBandSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrbAmountCategoryBandDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CrbAmountCategoryBands for query {}", query);
        return crbAmountCategoryBandSearchRepository.search(query, pageable).map(crbAmountCategoryBandMapper::toDto);
    }
}
