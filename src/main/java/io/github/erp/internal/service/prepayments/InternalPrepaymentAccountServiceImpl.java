package io.github.erp.internal.service.prepayments;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.PrepaymentAccount;
import io.github.erp.internal.repository.InternalPrepaymentAccountRepository;
import io.github.erp.internal.utilities.NextIntegerFiller;
import io.github.erp.repository.SettlementRepository;
import io.github.erp.repository.search.PrepaymentAccountSearchRepository;
import io.github.erp.service.dto.PrepaymentAccountDTO;
import io.github.erp.service.mapper.PrepaymentAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PrepaymentAccount}.
 */
@Service
@Transactional
public class InternalPrepaymentAccountServiceImpl implements InternalPrepaymentAccountService {

    private final Logger log = LoggerFactory.getLogger(InternalPrepaymentAccountServiceImpl.class);

    private final InternalPrepaymentAccountRepository prepaymentAccountRepository;

    private final PrepaymentAccountMapper prepaymentAccountMapper;

    private final SettlementRepository settlementRepository;

    private final PrepaymentAccountSearchRepository prepaymentAccountSearchRepository;

    public InternalPrepaymentAccountServiceImpl(
        InternalPrepaymentAccountRepository prepaymentAccountRepository,
        PrepaymentAccountMapper prepaymentAccountMapper,
        SettlementRepository settlementRepository, PrepaymentAccountSearchRepository prepaymentAccountSearchRepository
    ) {
        this.prepaymentAccountRepository = prepaymentAccountRepository;
        this.prepaymentAccountMapper = prepaymentAccountMapper;
        this.settlementRepository = settlementRepository;
        this.prepaymentAccountSearchRepository = prepaymentAccountSearchRepository;
    }

    PrepaymentAccount checkIfExistsSettlement(PrepaymentAccount prepaymentAccount) {
        if (prepaymentAccount.getPrepaymentTransaction() != null) {
            Long settlementId = prepaymentAccount.getPrepaymentTransaction().getId();
            boolean exists = settlementRepository.existsById(settlementId);
            if (!exists) {
                throw new EntityNotFoundException("Settlement with id " + settlementId + " not found");
            }
        } else {
            throw new EntityNotFoundException("No prepayment-transaction entity relationship found");
        }
        return prepaymentAccount;
    }

    @Override
    public PrepaymentAccountDTO save(PrepaymentAccountDTO prepaymentAccountDTO) {
        log.debug("Request to save PrepaymentAccount : {}", prepaymentAccountDTO);

        PrepaymentAccount prepaymentAccount = checkIfExistsSettlement(prepaymentAccountMapper.toEntity(prepaymentAccountDTO));
        prepaymentAccount = prepaymentAccountRepository.save(prepaymentAccount);
        PrepaymentAccountDTO result = prepaymentAccountMapper.toDto(prepaymentAccount);
        prepaymentAccountSearchRepository.save(prepaymentAccount);
        return result;
    }

    @Override
    public Optional<PrepaymentAccountDTO> partialUpdate(PrepaymentAccountDTO prepaymentAccountDTO) {
        log.debug("Request to partially update PrepaymentAccount : {}", prepaymentAccountDTO);

        return prepaymentAccountRepository
            .findById(prepaymentAccountDTO.getId())
            .map(existingPrepaymentAccount -> {
                prepaymentAccountMapper.partialUpdate(existingPrepaymentAccount, prepaymentAccountDTO);

                return existingPrepaymentAccount;
            })
            .map(prepaymentAccountRepository::save)
            .map(savedPrepaymentAccount -> {
                prepaymentAccountSearchRepository.save(savedPrepaymentAccount);

                return savedPrepaymentAccount;
            })
            .map(prepaymentAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PrepaymentAccounts");
        return prepaymentAccountRepository.findAll(pageable).map(prepaymentAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Long calculateNextCatalogueNumber() {
        log.debug("Request to get next catalogue number");
        return NextIntegerFiller.fillNext(prepaymentAccountRepository.findAllIds());
    }

    public Page<PrepaymentAccountDTO> findAllWithEagerRelationships(Pageable pageable) {
        return prepaymentAccountRepository.findAllWithEagerRelationships(pageable).map(prepaymentAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrepaymentAccountDTO> findOne(Long id) {
        log.debug("Request to get PrepaymentAccount : {}", id);
        return prepaymentAccountRepository.findOneWithEagerRelationships(id).map(prepaymentAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrepaymentAccount : {}", id);
        prepaymentAccountRepository.deleteById(id);
        prepaymentAccountSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PrepaymentAccounts for query {}", query);
        return prepaymentAccountSearchRepository.search(query, pageable).map(prepaymentAccountMapper::toDto);
    }
}
