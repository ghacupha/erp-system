package io.github.erp.internal.service.leases;

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

import io.github.erp.domain.LeasePayment;
import io.github.erp.internal.repository.InternalLeasePaymentRepository;
import io.github.erp.repository.search.LeasePaymentSearchRepository;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.mapper.LeasePaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link LeasePayment}.
 */
@Service
@Transactional
public class InternalLeasePaymentServiceImpl implements InternalLeasePaymentService {

    private final Logger log = LoggerFactory.getLogger(InternalLeasePaymentServiceImpl.class);

    private final InternalLeasePaymentRepository leasePaymentRepository;

    private final LeasePaymentMapper leasePaymentMapper;

    private final LeasePaymentSearchRepository leasePaymentSearchRepository;

    public InternalLeasePaymentServiceImpl(
        InternalLeasePaymentRepository leasePaymentRepository,
        LeasePaymentMapper leasePaymentMapper,
        LeasePaymentSearchRepository leasePaymentSearchRepository
    ) {
        this.leasePaymentRepository = leasePaymentRepository;
        this.leasePaymentMapper = leasePaymentMapper;
        this.leasePaymentSearchRepository = leasePaymentSearchRepository;
    }

    @Override
    public LeasePaymentDTO save(LeasePaymentDTO leasePaymentDTO) {
        log.debug("Request to save LeasePayment : {}", leasePaymentDTO);
        LeasePayment leasePayment = leasePaymentMapper.toEntity(leasePaymentDTO);
        leasePayment = leasePaymentRepository.save(leasePayment);
        LeasePaymentDTO result = leasePaymentMapper.toDto(leasePayment);
        leasePaymentSearchRepository.save(leasePayment);
        return result;
    }

    @Override
    public LeasePaymentDTO update(LeasePaymentDTO leasePaymentDTO) {
        log.debug("Request to save LeasePayment : {}", leasePaymentDTO);
        LeasePayment leasePayment = leasePaymentMapper.toEntity(leasePaymentDTO);
        leasePayment = leasePaymentRepository.save(leasePayment);
        return leasePaymentMapper.toDto(leasePayment);
    }

    @Override
    public Optional<LeasePaymentDTO> partialUpdate(LeasePaymentDTO leasePaymentDTO) {
        log.debug("Request to partially update LeasePayment : {}", leasePaymentDTO);

        return leasePaymentRepository
            .findById(leasePaymentDTO.getId())
            .map(existingLeasePayment -> {
                leasePaymentMapper.partialUpdate(existingLeasePayment, leasePaymentDTO);

                return existingLeasePayment;
            })
            .map(leasePaymentRepository::save)
            .map(savedLeasePayment -> {
                leasePaymentSearchRepository.save(savedLeasePayment);

                return savedLeasePayment;
            })
            .map(leasePaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasePaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeasePayments");
        return leasePaymentRepository.findAll(pageable).map(leasePaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeasePaymentDTO> findOne(Long id) {
        log.debug("Request to get LeasePayment : {}", id);
        return leasePaymentRepository.findById(id).map(leasePaymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeasePayment : {}", id);
        leasePaymentRepository.deleteById(id);
        leasePaymentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasePaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeasePayments for query {}", query);
        return leasePaymentSearchRepository.search(query, pageable).map(leasePaymentMapper::toDto);
    }

    /**
     * @param leaseContractId id of the adjacent lease-contract
     * @return List of lease-payments related to specified lease-contract
     */
    @Override
    public Optional<List<LeasePaymentDTO>> findPaymentsByContractId(Long leaseContractId) {
        log.debug("Request to fetch lease-payment instances adjacent to lease-contract-id: {}", leaseContractId);

        return leasePaymentRepository.findLeasePaymentsForLeaseContract(leaseContractId)
            .map(leasePaymentMapper::toDto);
    }
}
