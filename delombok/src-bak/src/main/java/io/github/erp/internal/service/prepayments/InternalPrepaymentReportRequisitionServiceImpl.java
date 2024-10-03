package io.github.erp.internal.service.prepayments;

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
import io.github.erp.domain.PrepaymentReportRequisition;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.repository.PrepaymentReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentReportRequisitionSearchRepository;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentReportRequisitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PrepaymentReportRequisition}.
 */
@Service
@Transactional
public class InternalPrepaymentReportRequisitionServiceImpl implements InternalPrepaymentReportRequisitionService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentReportRequisitionServiceImpl.class);

    private final PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository;

    private final PrepaymentReportRequisitionMapper prepaymentReportRequisitionMapper;

    private final PrepaymentReportRequisitionSearchRepository prepaymentReportRequisitionSearchRepository;

    private final InternalApplicationUserDetailService internalApplicationUserDetailService;

    public InternalPrepaymentReportRequisitionServiceImpl(
        PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository,
        PrepaymentReportRequisitionMapper prepaymentReportRequisitionMapper,
        PrepaymentReportRequisitionSearchRepository prepaymentReportRequisitionSearchRepository,
        InternalApplicationUserDetailService internalApplicationUserDetailService) {
        this.prepaymentReportRequisitionRepository = prepaymentReportRequisitionRepository;
        this.prepaymentReportRequisitionMapper = prepaymentReportRequisitionMapper;
        this.prepaymentReportRequisitionSearchRepository = prepaymentReportRequisitionSearchRepository;
        this.internalApplicationUserDetailService = internalApplicationUserDetailService;
    }

    @Override
    public PrepaymentReportRequisitionDTO save(PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO) {
        log.debug("Request to save PrepaymentReportRequisition : {}", prepaymentReportRequisitionDTO);

        // Update user details
        if (prepaymentReportRequisitionDTO.getId() == null) {
            internalApplicationUserDetailService.getCurrentApplicationUser().ifPresent(prepaymentReportRequisitionDTO::setRequestedBy);
        }

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

        final Optional<PrepaymentReportRequisitionDTO>[] reportRequisitionDTO = new Optional[]{Optional.empty()};

        internalApplicationUserDetailService.getCurrentApplicationUser().ifPresent(appUser -> {

            prepaymentReportRequisitionRepository.findById(id).map(prepaymentReportRequisitionMapper::toDto)
            .ifPresent(report -> {
                report.setLastAccessedBy(appUser);
                reportRequisitionDTO[0] = Optional.of(save(report));
            });
        });

        return reportRequisitionDTO[0];
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
