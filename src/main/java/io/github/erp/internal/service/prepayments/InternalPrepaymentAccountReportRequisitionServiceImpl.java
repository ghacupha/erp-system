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
import io.github.erp.domain.PrepaymentAccountReportRequisition;
import io.github.erp.repository.PrepaymentAccountReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentAccountReportRequisitionSearchRepository;
import io.github.erp.service.PrepaymentAccountReportRequisitionService;
import io.github.erp.service.dto.PrepaymentAccountReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentAccountReportRequisitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PrepaymentAccountReportRequisition}.
 */
@Service
@Transactional
public class InternalPrepaymentAccountReportRequisitionServiceImpl implements InternalPrepaymentAccountReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentAccountReportRequisitionServiceImpl.class);

    private final PrepaymentAccountReportRequisitionRepository prepaymentAccountReportRequisitionRepository;

    private final PrepaymentAccountReportRequisitionMapper prepaymentAccountReportRequisitionMapper;

    private final PrepaymentAccountReportRequisitionSearchRepository prepaymentAccountReportRequisitionSearchRepository;

    public InternalPrepaymentAccountReportRequisitionServiceImpl(
        PrepaymentAccountReportRequisitionRepository prepaymentAccountReportRequisitionRepository,
        PrepaymentAccountReportRequisitionMapper prepaymentAccountReportRequisitionMapper,
        PrepaymentAccountReportRequisitionSearchRepository prepaymentAccountReportRequisitionSearchRepository
    ) {
        this.prepaymentAccountReportRequisitionRepository = prepaymentAccountReportRequisitionRepository;
        this.prepaymentAccountReportRequisitionMapper = prepaymentAccountReportRequisitionMapper;
        this.prepaymentAccountReportRequisitionSearchRepository = prepaymentAccountReportRequisitionSearchRepository;
    }

    @Override
    public PrepaymentAccountReportRequisitionDTO save(PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO) {
        log.debug("Request to save PrepaymentAccountReportRequisition : {}", prepaymentAccountReportRequisitionDTO);
        PrepaymentAccountReportRequisition prepaymentAccountReportRequisition = prepaymentAccountReportRequisitionMapper.toEntity(
            prepaymentAccountReportRequisitionDTO
        );
        prepaymentAccountReportRequisition = prepaymentAccountReportRequisitionRepository.save(prepaymentAccountReportRequisition);
        PrepaymentAccountReportRequisitionDTO result = prepaymentAccountReportRequisitionMapper.toDto(prepaymentAccountReportRequisition);
        prepaymentAccountReportRequisitionSearchRepository.save(prepaymentAccountReportRequisition);
        return result;
    }

    @Override
    public Optional<PrepaymentAccountReportRequisitionDTO> partialUpdate(
        PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO
    ) {
        log.debug("Request to partially update PrepaymentAccountReportRequisition : {}", prepaymentAccountReportRequisitionDTO);

        return prepaymentAccountReportRequisitionRepository
            .findById(prepaymentAccountReportRequisitionDTO.getId())
            .map(existingPrepaymentAccountReportRequisition -> {
                prepaymentAccountReportRequisitionMapper.partialUpdate(
                    existingPrepaymentAccountReportRequisition,
                    prepaymentAccountReportRequisitionDTO
                );

                return existingPrepaymentAccountReportRequisition;
            })
            .map(prepaymentAccountReportRequisitionRepository::save)
            .map(savedPrepaymentAccountReportRequisition -> {
                prepaymentAccountReportRequisitionSearchRepository.save(savedPrepaymentAccountReportRequisition);

                return savedPrepaymentAccountReportRequisition;
            })
            .map(prepaymentAccountReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountReportRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentAccountReportRequisitions");
        return prepaymentAccountReportRequisitionRepository.findAll(pageable).map(prepaymentAccountReportRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentAccountReportRequisitionDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentAccountReportRequisition : {}", id);
        return prepaymentAccountReportRequisitionRepository.findById(id).map(prepaymentAccountReportRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentAccountReportRequisition : {}", id);
        prepaymentAccountReportRequisitionRepository.deleteById(id);
        prepaymentAccountReportRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountReportRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentAccountReportRequisitions for query {}", query);
        return prepaymentAccountReportRequisitionSearchRepository
            .search(query, pageable)
            .map(prepaymentAccountReportRequisitionMapper::toDto);
    }
}
