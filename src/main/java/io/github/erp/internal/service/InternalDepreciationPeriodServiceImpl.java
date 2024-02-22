package io.github.erp.internal.service;

import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.internal.repository.InternalDepreciationPeriodRepository;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.repository.search.DepreciationPeriodSearchRepository;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.impl.DepreciationPeriodServiceImpl;
import io.github.erp.service.mapper.DepreciationPeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class InternalDepreciationPeriodServiceImpl implements InternalDepreciationPeriodService {

    private final Logger log = LoggerFactory.getLogger(InternalDepreciationPeriodServiceImpl.class);

    private final InternalDepreciationPeriodRepository depreciationPeriodRepository;

    private final DepreciationPeriodMapper depreciationPeriodMapper;

    private final DepreciationPeriodSearchRepository depreciationPeriodSearchRepository;

    public InternalDepreciationPeriodServiceImpl(InternalDepreciationPeriodRepository depreciationPeriodRepository, DepreciationPeriodMapper depreciationPeriodMapper, DepreciationPeriodSearchRepository depreciationPeriodSearchRepository) {
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.depreciationPeriodMapper = depreciationPeriodMapper;
        this.depreciationPeriodSearchRepository = depreciationPeriodSearchRepository;
    }

    @Override
    public DepreciationPeriodDTO save(DepreciationPeriodDTO depreciationPeriodDTO) {
        log.debug("Request to save DepreciationPeriod : {}", depreciationPeriodDTO);
        DepreciationPeriod depreciationPeriod = depreciationPeriodMapper.toEntity(depreciationPeriodDTO);
        depreciationPeriod = depreciationPeriodRepository.save(depreciationPeriod);
        DepreciationPeriodDTO result = depreciationPeriodMapper.toDto(depreciationPeriod);
        depreciationPeriodSearchRepository.save(depreciationPeriod);
        return result;
    }

    @Override
    public Optional<DepreciationPeriodDTO> partialUpdate(DepreciationPeriodDTO depreciationPeriodDTO) {
        log.debug("Request to partially update DepreciationPeriod : {}", depreciationPeriodDTO);

        return depreciationPeriodRepository
            .findById(depreciationPeriodDTO.getId())
            .map(existingDepreciationPeriod -> {
                depreciationPeriodMapper.partialUpdate(existingDepreciationPeriod, depreciationPeriodDTO);

                return existingDepreciationPeriod;
            })
            .map(depreciationPeriodRepository::save)
            .map(savedDepreciationPeriod -> {
                depreciationPeriodSearchRepository.save(savedDepreciationPeriod);

                return savedDepreciationPeriod;
            })
            .map(depreciationPeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationPeriodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DepreciationPeriods");
        return depreciationPeriodRepository.findAll(pageable).map(depreciationPeriodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepreciationPeriodDTO> findOne(Long id) {
        log.debug("Request to get DepreciationPeriod : {}", id);
        return depreciationPeriodRepository.findByIdEquals(id).map(depreciationPeriodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DepreciationPeriod : {}", id);
        depreciationPeriodRepository.deleteById(id);
        depreciationPeriodSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepreciationPeriodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DepreciationPeriods for query {}", query);
        return depreciationPeriodSearchRepository.search(query, pageable).map(depreciationPeriodMapper::toDto);
    }
}
