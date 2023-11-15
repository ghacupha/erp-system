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

import io.github.erp.domain.FiscalQuarter;
import io.github.erp.repository.FiscalQuarterRepository;
import io.github.erp.repository.search.FiscalQuarterSearchRepository;
import io.github.erp.service.FiscalQuarterService;
import io.github.erp.service.dto.FiscalQuarterDTO;
import io.github.erp.service.mapper.FiscalQuarterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FiscalQuarter}.
 */
@Service
@Transactional
public class FiscalQuarterServiceImpl implements FiscalQuarterService {

    private final Logger log = LoggerFactory.getLogger(FiscalQuarterServiceImpl.class);

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterMapper fiscalQuarterMapper;

    private final FiscalQuarterSearchRepository fiscalQuarterSearchRepository;

    public FiscalQuarterServiceImpl(
        FiscalQuarterRepository fiscalQuarterRepository,
        FiscalQuarterMapper fiscalQuarterMapper,
        FiscalQuarterSearchRepository fiscalQuarterSearchRepository
    ) {
        this.fiscalQuarterRepository = fiscalQuarterRepository;
        this.fiscalQuarterMapper = fiscalQuarterMapper;
        this.fiscalQuarterSearchRepository = fiscalQuarterSearchRepository;
    }

    @Override
    public FiscalQuarterDTO save(FiscalQuarterDTO fiscalQuarterDTO) {
        log.debug("Request to save FiscalQuarter : {}", fiscalQuarterDTO);
        FiscalQuarter fiscalQuarter = fiscalQuarterMapper.toEntity(fiscalQuarterDTO);
        fiscalQuarter = fiscalQuarterRepository.save(fiscalQuarter);
        FiscalQuarterDTO result = fiscalQuarterMapper.toDto(fiscalQuarter);
        fiscalQuarterSearchRepository.save(fiscalQuarter);
        return result;
    }

    @Override
    public Optional<FiscalQuarterDTO> partialUpdate(FiscalQuarterDTO fiscalQuarterDTO) {
        log.debug("Request to partially update FiscalQuarter : {}", fiscalQuarterDTO);

        return fiscalQuarterRepository
            .findById(fiscalQuarterDTO.getId())
            .map(existingFiscalQuarter -> {
                fiscalQuarterMapper.partialUpdate(existingFiscalQuarter, fiscalQuarterDTO);

                return existingFiscalQuarter;
            })
            .map(fiscalQuarterRepository::save)
            .map(savedFiscalQuarter -> {
                fiscalQuarterSearchRepository.save(savedFiscalQuarter);

                return savedFiscalQuarter;
            })
            .map(fiscalQuarterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalQuarterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FiscalQuarters");
        return fiscalQuarterRepository.findAll(pageable).map(fiscalQuarterMapper::toDto);
    }

    public Page<FiscalQuarterDTO> findAllWithEagerRelationships(Pageable pageable) {
        return fiscalQuarterRepository.findAllWithEagerRelationships(pageable).map(fiscalQuarterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FiscalQuarterDTO> findOne(Long id) {
        log.debug("Request to get FiscalQuarter : {}", id);
        return fiscalQuarterRepository.findOneWithEagerRelationships(id).map(fiscalQuarterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FiscalQuarter : {}", id);
        fiscalQuarterRepository.deleteById(id);
        fiscalQuarterSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FiscalQuarterDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FiscalQuarters for query {}", query);
        return fiscalQuarterSearchRepository.search(query, pageable).map(fiscalQuarterMapper::toDto);
    }
}
