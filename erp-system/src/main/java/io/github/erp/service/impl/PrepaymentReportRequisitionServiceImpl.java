package io.github.erp.service.impl;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PrepaymentReportRequisition;
import io.github.erp.repository.PrepaymentReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentReportRequisitionSearchRepository;
import io.github.erp.service.PrepaymentReportRequisitionService;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PrepaymentReportRequisition}.
 */
@Service
@Transactional
public class PrepaymentReportRequisitionServiceImpl implements PrepaymentReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(PrepaymentReportRequisitionServiceImpl.class);

    private final PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository;

    private final PrepaymentReportRequisitionMapper prepaymentReportRequisitionMapper;

    private final PrepaymentReportRequisitionSearchRepository prepaymentReportRequisitionSearchRepository;

    public PrepaymentReportRequisitionServiceImpl(
        PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository,
        PrepaymentReportRequisitionMapper prepaymentReportRequisitionMapper,
        PrepaymentReportRequisitionSearchRepository prepaymentReportRequisitionSearchRepository
    ) {
        this.prepaymentReportRequisitionRepository = prepaymentReportRequisitionRepository;
        this.prepaymentReportRequisitionMapper = prepaymentReportRequisitionMapper;
        this.prepaymentReportRequisitionSearchRepository = prepaymentReportRequisitionSearchRepository;
    }

    @Override
    public PrepaymentReportRequisitionDTO save(PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO) {
        log.debug("Request to save PrepaymentReportRequisition : {}", prepaymentReportRequisitionDTO);
        PrepaymentReportRequisition prepaymentReportRequisition = prepaymentReportRequisitionMapper.toEntity(
            prepaymentReportRequisitionDTO
        );
        prepaymentReportRequisition = prepaymentReportRequisitionRepository.save(prepaymentReportRequisition);
        PrepaymentReportRequisitionDTO result = prepaymentReportRequisitionMapper.toDto(prepaymentReportRequisition);
        prepaymentReportRequisitionSearchRepository.save(prepaymentReportRequisition);
        return result;
    }

    @Override
    public Optional<PrepaymentReportRequisitionDTO> partialUpdate(PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO) {
        log.debug("Request to partially update PrepaymentReportRequisition : {}", prepaymentReportRequisitionDTO);

        return prepaymentReportRequisitionRepository
            .findById(prepaymentReportRequisitionDTO.getId())
            .map(existingPrepaymentReportRequisition -> {
                prepaymentReportRequisitionMapper.partialUpdate(existingPrepaymentReportRequisition, prepaymentReportRequisitionDTO);

                return existingPrepaymentReportRequisition;
            })
            .map(prepaymentReportRequisitionRepository::save)
            .map(savedPrepaymentReportRequisition -> {
                prepaymentReportRequisitionSearchRepository.save(savedPrepaymentReportRequisition);

                return savedPrepaymentReportRequisition;
            })
            .map(prepaymentReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentReportRequisitions");
        return prepaymentReportRequisitionRepository.findAll(pageable).map(prepaymentReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentReportRequisition : {}", id);
        return prepaymentReportRequisitionRepository.findById(id).map(prepaymentReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentReportRequisition : {}", id);
        prepaymentReportRequisitionRepository.deleteById(id);
        prepaymentReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentReportRequisitions for query {}", query);
        return prepaymentReportRequisitionSearchRepository.search(query, pageable).map(prepaymentReportRequisitionMapper::toDto);
    }
}
