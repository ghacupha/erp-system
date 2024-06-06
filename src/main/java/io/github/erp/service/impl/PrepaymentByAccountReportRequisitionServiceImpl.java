package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PrepaymentByAccountReportRequisition;
import io.github.erp.repository.PrepaymentByAccountReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentByAccountReportRequisitionSearchRepository;
import io.github.erp.service.PrepaymentByAccountReportRequisitionService;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentByAccountReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrepaymentByAccountReportRequisition}.
 */
@Service
@Transactional
public class PrepaymentByAccountReportRequisitionServiceImpl implements PrepaymentByAccountReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentByAccountReportRequisitionServiceImpl.class);

    private final PrepaymentByAccountReportRequisitionRepository prepaymentByAccountReportRequisitionRepository;

    private final PrepaymentByAccountReportRequisitionMapper prepaymentByAccountReportRequisitionMapper;

    private final PrepaymentByAccountReportRequisitionSearchRepository prepaymentByAccountReportRequisitionSearchRepository;

    public PrepaymentByAccountReportRequisitionServiceImpl(
        PrepaymentByAccountReportRequisitionRepository prepaymentByAccountReportRequisitionRepository,
        PrepaymentByAccountReportRequisitionMapper prepaymentByAccountReportRequisitionMapper,
        PrepaymentByAccountReportRequisitionSearchRepository prepaymentByAccountReportRequisitionSearchRepository
    ) {
        this.prepaymentByAccountReportRequisitionRepository = prepaymentByAccountReportRequisitionRepository;
        this.prepaymentByAccountReportRequisitionMapper = prepaymentByAccountReportRequisitionMapper;
        this.prepaymentByAccountReportRequisitionSearchRepository = prepaymentByAccountReportRequisitionSearchRepository;
    }

    @Override
    public PrepaymentByAccountReportRequisitionDTO save(PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO) {
        log.debug("Request to save PrepaymentByAccountReportRequisition : {}", prepaymentByAccountReportRequisitionDTO);
        PrepaymentByAccountReportRequisition prepaymentByAccountReportRequisition = prepaymentByAccountReportRequisitionMapper.toEntity(
            prepaymentByAccountReportRequisitionDTO
        );
        prepaymentByAccountReportRequisition = prepaymentByAccountReportRequisitionRepository.save(prepaymentByAccountReportRequisition);
        PrepaymentByAccountReportRequisitionDTO result = prepaymentByAccountReportRequisitionMapper.toDto(
            prepaymentByAccountReportRequisition
        );
        prepaymentByAccountReportRequisitionSearchRepository.save(prepaymentByAccountReportRequisition);
        return result;
    }

    @Override
    public Optional<PrepaymentByAccountReportRequisitionDTO> partialUpdate(
        PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO
    ) {
        log.debug("Request to partially update PrepaymentByAccountReportRequisition : {}", prepaymentByAccountReportRequisitionDTO);

        return prepaymentByAccountReportRequisitionRepository
            .findById(prepaymentByAccountReportRequisitionDTO.getId())
            .map(existingPrepaymentByAccountReportRequisition -> {
                prepaymentByAccountReportRequisitionMapper.partialUpdate(
                    existingPrepaymentByAccountReportRequisition,
                    prepaymentByAccountReportRequisitionDTO
                );

                return existingPrepaymentByAccountReportRequisition;
            })
            .map(prepaymentByAccountReportRequisitionRepository::save)
            .map(savedPrepaymentByAccountReportRequisition -> {
                prepaymentByAccountReportRequisitionSearchRepository.save(savedPrepaymentByAccountReportRequisition);

                return savedPrepaymentByAccountReportRequisition;
            })
            .map(prepaymentByAccountReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentByAccountReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentByAccountReportRequisitions");
        return prepaymentByAccountReportRequisitionRepository.findAll(pageable).map(prepaymentByAccountReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentByAccountReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentByAccountReportRequisition : {}", id);
        return prepaymentByAccountReportRequisitionRepository.findById(id).map(prepaymentByAccountReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentByAccountReportRequisition : {}", id);
        prepaymentByAccountReportRequisitionRepository.deleteById(id);
        prepaymentByAccountReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentByAccountReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentByAccountReportRequisitions for query {}", query);
        return prepaymentByAccountReportRequisitionSearchRepository
            .search(query, pageable)
            .map(prepaymentByAccountReportRequisitionMapper::toDto);
    }
}
