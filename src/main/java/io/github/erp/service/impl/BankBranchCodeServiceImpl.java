package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.BankBranchCode;
import io.github.erp.repository.BankBranchCodeRepository;
import io.github.erp.repository.search.BankBranchCodeSearchRepository;
import io.github.erp.service.BankBranchCodeService;
import io.github.erp.service.dto.BankBranchCodeDTO;
import io.github.erp.service.mapper.BankBranchCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BankBranchCode}.
 */
@Service
@Transactional
public class BankBranchCodeServiceImpl implements BankBranchCodeService {

    private final Logger log = LoggerFactory.getLogger(BankBranchCodeServiceImpl.class);

    private final BankBranchCodeRepository bankBranchCodeRepository;

    private final BankBranchCodeMapper bankBranchCodeMapper;

    private final BankBranchCodeSearchRepository bankBranchCodeSearchRepository;

    public BankBranchCodeServiceImpl(
        BankBranchCodeRepository bankBranchCodeRepository,
        BankBranchCodeMapper bankBranchCodeMapper,
        BankBranchCodeSearchRepository bankBranchCodeSearchRepository
    ) {
        this.bankBranchCodeRepository = bankBranchCodeRepository;
        this.bankBranchCodeMapper = bankBranchCodeMapper;
        this.bankBranchCodeSearchRepository = bankBranchCodeSearchRepository;
    }

    @Override
    public BankBranchCodeDTO save(BankBranchCodeDTO bankBranchCodeDTO) {
        log.debug("Request to save BankBranchCode : {}", bankBranchCodeDTO);
        BankBranchCode bankBranchCode = bankBranchCodeMapper.toEntity(bankBranchCodeDTO);
        bankBranchCode = bankBranchCodeRepository.save(bankBranchCode);
        BankBranchCodeDTO result = bankBranchCodeMapper.toDto(bankBranchCode);
        bankBranchCodeSearchRepository.save(bankBranchCode);
        return result;
    }

    @Override
    public Optional<BankBranchCodeDTO> partialUpdate(BankBranchCodeDTO bankBranchCodeDTO) {
        log.debug("Request to partially update BankBranchCode : {}", bankBranchCodeDTO);

        return bankBranchCodeRepository
            .findById(bankBranchCodeDTO.getId())
            .map(existingBankBranchCode -> {
                bankBranchCodeMapper.partialUpdate(existingBankBranchCode, bankBranchCodeDTO);

                return existingBankBranchCode;
            })
            .map(bankBranchCodeRepository::save)
            .map(savedBankBranchCode -> {
                bankBranchCodeSearchRepository.save(savedBankBranchCode);

                return savedBankBranchCode;
            })
            .map(bankBranchCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankBranchCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BankBranchCodes");
        return bankBranchCodeRepository.findAll(pageable).map(bankBranchCodeMapper::toDto);
    }

    public Page<BankBranchCodeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bankBranchCodeRepository.findAllWithEagerRelationships(pageable).map(bankBranchCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BankBranchCodeDTO> findOne(Long id) {
        log.debug("Request to get BankBranchCode : {}", id);
        return bankBranchCodeRepository.findOneWithEagerRelationships(id).map(bankBranchCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BankBranchCode : {}", id);
        bankBranchCodeRepository.deleteById(id);
        bankBranchCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankBranchCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BankBranchCodes for query {}", query);
        return bankBranchCodeSearchRepository.search(query, pageable).map(bankBranchCodeMapper::toDto);
    }
}
