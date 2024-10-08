package io.github.erp.internal.service.leases;

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
import io.github.erp.domain.LeaseAmortizationSchedule;
import io.github.erp.internal.repository.InternalLeaseAmortizationScheduleRepository;
import io.github.erp.repository.search.LeaseAmortizationScheduleSearchRepository;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.mapper.LeaseAmortizationScheduleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaseAmortizationSchedule}.
 */
@Service
@Transactional
public class InternalLeaseAmortizationScheduleServiceImpl implements InternalLeaseAmortizationScheduleService {

    private final Logger log = LoggerFactory.getLogger(InternalLeaseAmortizationScheduleServiceImpl.class);

    private final InternalLeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository;

    private final LeaseAmortizationScheduleMapper leaseAmortizationScheduleMapper;

    private final LeaseAmortizationScheduleSearchRepository leaseAmortizationScheduleSearchRepository;

    public InternalLeaseAmortizationScheduleServiceImpl(
        InternalLeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository,
        LeaseAmortizationScheduleMapper leaseAmortizationScheduleMapper,
        LeaseAmortizationScheduleSearchRepository leaseAmortizationScheduleSearchRepository
    ) {
        this.leaseAmortizationScheduleRepository = leaseAmortizationScheduleRepository;
        this.leaseAmortizationScheduleMapper = leaseAmortizationScheduleMapper;
        this.leaseAmortizationScheduleSearchRepository = leaseAmortizationScheduleSearchRepository;
    }

    @Override
    public LeaseAmortizationScheduleDTO save(LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO) {
        log.debug("Request to save LeaseAmortizationSchedule : {}", leaseAmortizationScheduleDTO);
        LeaseAmortizationSchedule leaseAmortizationSchedule = leaseAmortizationScheduleMapper.toEntity(leaseAmortizationScheduleDTO);
        leaseAmortizationSchedule = leaseAmortizationScheduleRepository.save(leaseAmortizationSchedule);
        LeaseAmortizationScheduleDTO result = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);
        leaseAmortizationScheduleSearchRepository.save(leaseAmortizationSchedule);
        return result;
    }

    @Override
    public Optional<LeaseAmortizationScheduleDTO> partialUpdate(LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO) {
        log.debug("Request to partially update LeaseAmortizationSchedule : {}", leaseAmortizationScheduleDTO);

        return leaseAmortizationScheduleRepository
            .findById(leaseAmortizationScheduleDTO.getId())
            .map(existingLeaseAmortizationSchedule -> {
                leaseAmortizationScheduleMapper.partialUpdate(existingLeaseAmortizationSchedule, leaseAmortizationScheduleDTO);

                return existingLeaseAmortizationSchedule;
            })
            .map(leaseAmortizationScheduleRepository::save)
            .map(savedLeaseAmortizationSchedule -> {
                leaseAmortizationScheduleSearchRepository.save(savedLeaseAmortizationSchedule);

                return savedLeaseAmortizationSchedule;
            })
            .map(leaseAmortizationScheduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseAmortizationScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseAmortizationSchedules");
        return leaseAmortizationScheduleRepository.findAll(pageable).map(leaseAmortizationScheduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseAmortizationScheduleDTO> findOne(Long id) {
        log.debug("Request to get LeaseAmortizationSchedule : {}", id);
        return leaseAmortizationScheduleRepository.findById(id).map(leaseAmortizationScheduleMapper::toDto);
    }

    /**
     * Returns the amortization-schedule that is adjacent to the IFRS16 booking id
     *
     * @param bookingId value in the IFRS16 lease contract instance
     * @return Schedule instance
     */
    @Override
    public Optional<LeaseAmortizationScheduleDTO> findOneByBookingId(String bookingId) {
        log.debug("Request to get LeaseAmortizationSchedule for booking id : {}", bookingId);

        return leaseAmortizationScheduleRepository.findAdjacentScheduleByBookingId(bookingId).map(leaseAmortizationScheduleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseAmortizationSchedule : {}", id);
        leaseAmortizationScheduleRepository.deleteById(id);
        leaseAmortizationScheduleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseAmortizationScheduleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseAmortizationSchedules for query {}", query);
        return leaseAmortizationScheduleSearchRepository.search(query, pageable).map(leaseAmortizationScheduleMapper::toDto);
    }
}
