package io.github.erp.service.impl;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.AmortizationPostingReportRequisition;
import io.github.erp.repository.AmortizationPostingReportRequisitionRepository;
import io.github.erp.repository.search.AmortizationPostingReportRequisitionSearchRepository;
import io.github.erp.service.AmortizationPostingReportRequisitionService;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import io.github.erp.service.mapper.AmortizationPostingReportRequisitionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AmortizationPostingReportRequisition}.
 */
@Service
@Transactional
public class AmortizationPostingReportRequisitionServiceImpl implements AmortizationPostingReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(AmortizationPostingReportRequisitionServiceImpl.class);

    private final AmortizationPostingReportRequisitionRepository amortizationPostingReportRequisitionRepository;

    private final AmortizationPostingReportRequisitionMapper amortizationPostingReportRequisitionMapper;

    private final AmortizationPostingReportRequisitionSearchRepository amortizationPostingReportRequisitionSearchRepository;

    public AmortizationPostingReportRequisitionServiceImpl(
        AmortizationPostingReportRequisitionRepository amortizationPostingReportRequisitionRepository,
        AmortizationPostingReportRequisitionMapper amortizationPostingReportRequisitionMapper,
        AmortizationPostingReportRequisitionSearchRepository amortizationPostingReportRequisitionSearchRepository
    ) {
        this.amortizationPostingReportRequisitionRepository = amortizationPostingReportRequisitionRepository;
        this.amortizationPostingReportRequisitionMapper = amortizationPostingReportRequisitionMapper;
        this.amortizationPostingReportRequisitionSearchRepository = amortizationPostingReportRequisitionSearchRepository;
    }

    @Override
    public AmortizationPostingReportRequisitionDTO save(AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO) {
        log.debug("Request to save AmortizationPostingReportRequisition : {}", amortizationPostingReportRequisitionDTO);
        AmortizationPostingReportRequisition amortizationPostingReportRequisition = amortizationPostingReportRequisitionMapper.toEntity(
            amortizationPostingReportRequisitionDTO
        );
        amortizationPostingReportRequisition = amortizationPostingReportRequisitionRepository.save(amortizationPostingReportRequisition);
        AmortizationPostingReportRequisitionDTO result = amortizationPostingReportRequisitionMapper.toDto(
            amortizationPostingReportRequisition
        );
        amortizationPostingReportRequisitionSearchRepository.save(amortizationPostingReportRequisition);
        return result;
    }

    @Override
    public Optional<AmortizationPostingReportRequisitionDTO> partialUpdate(
        AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO
    ) {
        log.debug("Request to partially update AmortizationPostingReportRequisition : {}", amortizationPostingReportRequisitionDTO);

        return amortizationPostingReportRequisitionRepository
            .findById(amortizationPostingReportRequisitionDTO.getId())
            .map(existingAmortizationPostingReportRequisition -> {
                amortizationPostingReportRequisitionMapper.partialUpdate(
                    existingAmortizationPostingReportRequisition,
                    amortizationPostingReportRequisitionDTO
                );

                return existingAmortizationPostingReportRequisition;
            })
            .map(amortizationPostingReportRequisitionRepository::save)
            .map(savedAmortizationPostingReportRequisition -> {
                amortizationPostingReportRequisitionSearchRepository.save(savedAmortizationPostingReportRequisition);

                return savedAmortizationPostingReportRequisition;
            })
            .map(amortizationPostingReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationPostingReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmortizationPostingReportRequisitions");
        return amortizationPostingReportRequisitionRepository.findAll(pageable).map(amortizationPostingReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AmortizationPostingReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get AmortizationPostingReportRequisition : {}", id);
        return amortizationPostingReportRequisitionRepository.findById(id).map(amortizationPostingReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AmortizationPostingReportRequisition : {}", id);
        amortizationPostingReportRequisitionRepository.deleteById(id);
        amortizationPostingReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationPostingReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmortizationPostingReportRequisitions for query {}", query);
        return amortizationPostingReportRequisitionSearchRepository
            .search(query, pageable)
            .map(amortizationPostingReportRequisitionMapper::toDto);
    }
}
