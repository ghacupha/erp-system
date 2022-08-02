package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.MfbBranchCode;
import io.github.erp.repository.MfbBranchCodeRepository;
import io.github.erp.repository.search.MfbBranchCodeSearchRepository;
import io.github.erp.service.MfbBranchCodeService;
import io.github.erp.service.dto.MfbBranchCodeDTO;
import io.github.erp.service.mapper.MfbBranchCodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MfbBranchCode}.
 */
@Service
@Transactional
public class MfbBranchCodeServiceImpl implements MfbBranchCodeService {

    private final Logger log = LoggerFactory.getLogger(MfbBranchCodeServiceImpl.class);

    private final MfbBranchCodeRepository mfbBranchCodeRepository;

    private final MfbBranchCodeMapper mfbBranchCodeMapper;

    private final MfbBranchCodeSearchRepository mfbBranchCodeSearchRepository;

    public MfbBranchCodeServiceImpl(
        MfbBranchCodeRepository mfbBranchCodeRepository,
        MfbBranchCodeMapper mfbBranchCodeMapper,
        MfbBranchCodeSearchRepository mfbBranchCodeSearchRepository
    ) {
        this.mfbBranchCodeRepository = mfbBranchCodeRepository;
        this.mfbBranchCodeMapper = mfbBranchCodeMapper;
        this.mfbBranchCodeSearchRepository = mfbBranchCodeSearchRepository;
    }

    @Override
    public MfbBranchCodeDTO save(MfbBranchCodeDTO mfbBranchCodeDTO) {
        log.debug("Request to save MfbBranchCode : {}", mfbBranchCodeDTO);
        MfbBranchCode mfbBranchCode = mfbBranchCodeMapper.toEntity(mfbBranchCodeDTO);
        mfbBranchCode = mfbBranchCodeRepository.save(mfbBranchCode);
        MfbBranchCodeDTO result = mfbBranchCodeMapper.toDto(mfbBranchCode);
        mfbBranchCodeSearchRepository.save(mfbBranchCode);
        return result;
    }

    @Override
    public Optional<MfbBranchCodeDTO> partialUpdate(MfbBranchCodeDTO mfbBranchCodeDTO) {
        log.debug("Request to partially update MfbBranchCode : {}", mfbBranchCodeDTO);

        return mfbBranchCodeRepository
            .findById(mfbBranchCodeDTO.getId())
            .map(existingMfbBranchCode -> {
                mfbBranchCodeMapper.partialUpdate(existingMfbBranchCode, mfbBranchCodeDTO);

                return existingMfbBranchCode;
            })
            .map(mfbBranchCodeRepository::save)
            .map(savedMfbBranchCode -> {
                mfbBranchCodeSearchRepository.save(savedMfbBranchCode);

                return savedMfbBranchCode;
            })
            .map(mfbBranchCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MfbBranchCodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MfbBranchCodes");
        return mfbBranchCodeRepository.findAll(pageable).map(mfbBranchCodeMapper::toDto);
    }

    public Page<MfbBranchCodeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mfbBranchCodeRepository.findAllWithEagerRelationships(pageable).map(mfbBranchCodeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MfbBranchCodeDTO> findOne(Long id) {
        log.debug("Request to get MfbBranchCode : {}", id);
        return mfbBranchCodeRepository.findOneWithEagerRelationships(id).map(mfbBranchCodeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MfbBranchCode : {}", id);
        mfbBranchCodeRepository.deleteById(id);
        mfbBranchCodeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MfbBranchCodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MfbBranchCodes for query {}", query);
        return mfbBranchCodeSearchRepository.search(query, pageable).map(mfbBranchCodeMapper::toDto);
    }
}
