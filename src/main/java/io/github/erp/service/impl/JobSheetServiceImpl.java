package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.JobSheet;
import io.github.erp.repository.JobSheetRepository;
import io.github.erp.repository.search.JobSheetSearchRepository;
import io.github.erp.service.JobSheetService;
import io.github.erp.service.dto.JobSheetDTO;
import io.github.erp.service.mapper.JobSheetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link JobSheet}.
 */
@Service
@Transactional
public class JobSheetServiceImpl implements JobSheetService {

    private final Logger log = LoggerFactory.getLogger(JobSheetServiceImpl.class);

    private final JobSheetRepository jobSheetRepository;

    private final JobSheetMapper jobSheetMapper;

    private final JobSheetSearchRepository jobSheetSearchRepository;

    public JobSheetServiceImpl(
        JobSheetRepository jobSheetRepository,
        JobSheetMapper jobSheetMapper,
        JobSheetSearchRepository jobSheetSearchRepository
    ) {
        this.jobSheetRepository = jobSheetRepository;
        this.jobSheetMapper = jobSheetMapper;
        this.jobSheetSearchRepository = jobSheetSearchRepository;
    }

    @Override
    public JobSheetDTO save(JobSheetDTO jobSheetDTO) {
        log.debug("Request to save JobSheet : {}", jobSheetDTO);
        JobSheet jobSheet = jobSheetMapper.toEntity(jobSheetDTO);
        jobSheet = jobSheetRepository.save(jobSheet);
        JobSheetDTO result = jobSheetMapper.toDto(jobSheet);
        jobSheetSearchRepository.save(jobSheet);
        return result;
    }

    @Override
    public Optional<JobSheetDTO> partialUpdate(JobSheetDTO jobSheetDTO) {
        log.debug("Request to partially update JobSheet : {}", jobSheetDTO);

        return jobSheetRepository
            .findById(jobSheetDTO.getId())
            .map(existingJobSheet -> {
                jobSheetMapper.partialUpdate(existingJobSheet, jobSheetDTO);

                return existingJobSheet;
            })
            .map(jobSheetRepository::save)
            .map(savedJobSheet -> {
                jobSheetSearchRepository.save(savedJobSheet);

                return savedJobSheet;
            })
            .map(jobSheetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobSheetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all JobSheets");
        return jobSheetRepository.findAll(pageable).map(jobSheetMapper::toDto);
    }

    public Page<JobSheetDTO> findAllWithEagerRelationships(Pageable pageable) {
        return jobSheetRepository.findAllWithEagerRelationships(pageable).map(jobSheetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobSheetDTO> findOne(Long id) {
        log.debug("Request to get JobSheet : {}", id);
        return jobSheetRepository.findOneWithEagerRelationships(id).map(jobSheetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete JobSheet : {}", id);
        jobSheetRepository.deleteById(id);
        jobSheetSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobSheetDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of JobSheets for query {}", query);
        return jobSheetSearchRepository.search(query, pageable).map(jobSheetMapper::toDto);
    }
}
