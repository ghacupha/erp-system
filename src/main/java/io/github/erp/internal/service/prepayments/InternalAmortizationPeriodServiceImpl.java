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
import io.github.erp.domain.AmortizationPeriod;
import io.github.erp.internal.repository.InternalAmortizationPeriodRepository;
import io.github.erp.repository.AmortizationPeriodRepository;
import io.github.erp.repository.search.AmortizationPeriodSearchRepository;
import io.github.erp.service.dto.AmortizationPeriodDTO;
import io.github.erp.service.mapper.AmortizationPeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Implementation for managing {@link AmortizationPeriod}.
 */
@Service
@Transactional
public class InternalAmortizationPeriodServiceImpl implements InternalAmortizationPeriodService {

    private final Logger log = LoggerFactory.getLogger(InternalAmortizationPeriodServiceImpl.class);

    private final InternalAmortizationPeriodRepository amortizationPeriodRepository;

    private final AmortizationPeriodMapper amortizationPeriodMapper;

    private final AmortizationPeriodSearchRepository amortizationPeriodSearchRepository;

    public InternalAmortizationPeriodServiceImpl(
        InternalAmortizationPeriodRepository amortizationPeriodRepository,
        AmortizationPeriodMapper amortizationPeriodMapper,
        AmortizationPeriodSearchRepository amortizationPeriodSearchRepository
    ) {
        this.amortizationPeriodRepository = amortizationPeriodRepository;
        this.amortizationPeriodMapper = amortizationPeriodMapper;
        this.amortizationPeriodSearchRepository = amortizationPeriodSearchRepository;
    }

    @Override
    public AmortizationPeriodDTO save(AmortizationPeriodDTO amortizationPeriodDTO) {
        log.debug("Request to save AmortizationPeriod : {}", amortizationPeriodDTO);
        AmortizationPeriod amortizationPeriod = amortizationPeriodMapper.toEntity(amortizationPeriodDTO);
        amortizationPeriod = amortizationPeriodRepository.save(amortizationPeriod);
        AmortizationPeriodDTO result = amortizationPeriodMapper.toDto(amortizationPeriod);
        amortizationPeriodSearchRepository.save(amortizationPeriod);
        return result;
    }

    @Override
    public Optional<AmortizationPeriodDTO> partialUpdate(AmortizationPeriodDTO amortizationPeriodDTO) {
        log.debug("Request to partially update AmortizationPeriod : {}", amortizationPeriodDTO);

        return amortizationPeriodRepository
            .findById(amortizationPeriodDTO.getId())
            .map(existingAmortizationPeriod -> {
                amortizationPeriodMapper.partialUpdate(existingAmortizationPeriod, amortizationPeriodDTO);

                return existingAmortizationPeriod;
            })
            .map(amortizationPeriodRepository::save)
            .map(savedAmortizationPeriod -> {
                amortizationPeriodSearchRepository.save(savedAmortizationPeriod);

                return savedAmortizationPeriod;
            })
            .map(amortizationPeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationPeriodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AmortizationPeriods");
        return amortizationPeriodRepository.findAll(pageable).map(amortizationPeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AmortizationPeriodDTO> findOne(Long id) {
        log.debug("Request to get AmortizationPeriod : {}", id);
        return amortizationPeriodRepository.findById(id).map(amortizationPeriodMapper::toDto);
    }

    /**
     * Get the amortizationPeriod to which the queryDate belongs.
     *
     * @param queryDate the date enclosed by the amortization-period in string.
     * @return the entity.
     */
    @Override
    public Optional<AmortizationPeriodDTO> findOneByDate(String queryDate) {
        log.debug("Request to get AmortizationPeriod to which the date: {} belongs", queryDate);
        return amortizationPeriodRepository.findByDateGiven(LocalDate.parse(queryDate)).map(amortizationPeriodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AmortizationPeriod : {}", id);
        amortizationPeriodRepository.deleteById(id);
        amortizationPeriodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AmortizationPeriodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AmortizationPeriods for query {}", query);
        return amortizationPeriodSearchRepository.search(query, pageable).map(amortizationPeriodMapper::toDto);
    }

    /**
     * Get the nth amortizationPeriod instance in the sequence after the current
     * instance.
     *
     * @param currentPeriodId the id of the current entity.
     * @return the entity.
     */
    @Override
    public Optional<AmortizationPeriodDTO> getNextAmortizationPeriod(Long currentPeriodId, long nthValue) {
        log.debug("Request to fetch the next instance of amortization period after the current instance id: {}", currentPeriodId);

        return amortizationPeriodRepository.getNextAmortizationPeriod(currentPeriodId, nthValue)
            .map(amortizationPeriodMapper::toDto);
    }
}
