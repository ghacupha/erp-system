package io.github.erp.service.impl;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.domain.SignedPayment;
import io.github.erp.repository.SignedPaymentRepository;
import io.github.erp.repository.search.SignedPaymentSearchRepository;
import io.github.erp.service.SignedPaymentService;
import io.github.erp.service.dto.SignedPaymentDTO;
import io.github.erp.service.mapper.SignedPaymentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SignedPayment}.
 */
@Service
@Transactional
public class SignedPaymentServiceImpl implements SignedPaymentService {

    private final Logger log = LoggerFactory.getLogger(SignedPaymentServiceImpl.class);

    private final SignedPaymentRepository signedPaymentRepository;

    private final SignedPaymentMapper signedPaymentMapper;

    private final SignedPaymentSearchRepository signedPaymentSearchRepository;

    public SignedPaymentServiceImpl(
        SignedPaymentRepository signedPaymentRepository,
        SignedPaymentMapper signedPaymentMapper,
        SignedPaymentSearchRepository signedPaymentSearchRepository
    ) {
        this.signedPaymentRepository = signedPaymentRepository;
        this.signedPaymentMapper = signedPaymentMapper;
        this.signedPaymentSearchRepository = signedPaymentSearchRepository;
    }

    @Override
    public SignedPaymentDTO save(SignedPaymentDTO signedPaymentDTO) {
        log.debug("Request to save SignedPayment : {}", signedPaymentDTO);
        SignedPayment signedPayment = signedPaymentMapper.toEntity(signedPaymentDTO);
        signedPayment = signedPaymentRepository.save(signedPayment);
        SignedPaymentDTO result = signedPaymentMapper.toDto(signedPayment);
        signedPaymentSearchRepository.save(signedPayment);
        return result;
    }

    @Override
    public Optional<SignedPaymentDTO> partialUpdate(SignedPaymentDTO signedPaymentDTO) {
        log.debug("Request to partially update SignedPayment : {}", signedPaymentDTO);

        return signedPaymentRepository
            .findById(signedPaymentDTO.getId())
            .map(existingSignedPayment -> {
                signedPaymentMapper.partialUpdate(existingSignedPayment, signedPaymentDTO);

                return existingSignedPayment;
            })
            .map(signedPaymentRepository::save)
            .map(savedSignedPayment -> {
                signedPaymentSearchRepository.save(savedSignedPayment);

                return savedSignedPayment;
            })
            .map(signedPaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SignedPaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SignedPayments");
        return signedPaymentRepository.findAll(pageable).map(signedPaymentMapper::toDto);
    }

    public Page<SignedPaymentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return signedPaymentRepository.findAllWithEagerRelationships(pageable).map(signedPaymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SignedPaymentDTO> findOne(Long id) {
        log.debug("Request to get SignedPayment : {}", id);
        return signedPaymentRepository.findOneWithEagerRelationships(id).map(signedPaymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SignedPayment : {}", id);
        signedPaymentRepository.deleteById(id);
        signedPaymentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SignedPaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SignedPayments for query {}", query);
        return signedPaymentSearchRepository.search(query, pageable).map(signedPaymentMapper::toDto);
    }
}
