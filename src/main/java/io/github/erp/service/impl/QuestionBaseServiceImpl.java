package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.QuestionBase;
import io.github.erp.repository.QuestionBaseRepository;
import io.github.erp.repository.search.QuestionBaseSearchRepository;
import io.github.erp.service.QuestionBaseService;
import io.github.erp.service.dto.QuestionBaseDTO;
import io.github.erp.service.mapper.QuestionBaseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link QuestionBase}.
 */
@Service
@Transactional
public class QuestionBaseServiceImpl implements QuestionBaseService {

    private final Logger log = LoggerFactory.getLogger(QuestionBaseServiceImpl.class);

    private final QuestionBaseRepository questionBaseRepository;

    private final QuestionBaseMapper questionBaseMapper;

    private final QuestionBaseSearchRepository questionBaseSearchRepository;

    public QuestionBaseServiceImpl(
        QuestionBaseRepository questionBaseRepository,
        QuestionBaseMapper questionBaseMapper,
        QuestionBaseSearchRepository questionBaseSearchRepository
    ) {
        this.questionBaseRepository = questionBaseRepository;
        this.questionBaseMapper = questionBaseMapper;
        this.questionBaseSearchRepository = questionBaseSearchRepository;
    }

    @Override
    public QuestionBaseDTO save(QuestionBaseDTO questionBaseDTO) {
        log.debug("Request to save QuestionBase : {}", questionBaseDTO);
        QuestionBase questionBase = questionBaseMapper.toEntity(questionBaseDTO);
        questionBase = questionBaseRepository.save(questionBase);
        QuestionBaseDTO result = questionBaseMapper.toDto(questionBase);
        questionBaseSearchRepository.save(questionBase);
        return result;
    }

    @Override
    public Optional<QuestionBaseDTO> partialUpdate(QuestionBaseDTO questionBaseDTO) {
        log.debug("Request to partially update QuestionBase : {}", questionBaseDTO);

        return questionBaseRepository
            .findById(questionBaseDTO.getId())
            .map(existingQuestionBase -> {
                questionBaseMapper.partialUpdate(existingQuestionBase, questionBaseDTO);

                return existingQuestionBase;
            })
            .map(questionBaseRepository::save)
            .map(savedQuestionBase -> {
                questionBaseSearchRepository.save(savedQuestionBase);

                return savedQuestionBase;
            })
            .map(questionBaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionBaseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionBases");
        return questionBaseRepository.findAll(pageable).map(questionBaseMapper::toDto);
    }

    public Page<QuestionBaseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return questionBaseRepository.findAllWithEagerRelationships(pageable).map(questionBaseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionBaseDTO> findOne(Long id) {
        log.debug("Request to get QuestionBase : {}", id);
        return questionBaseRepository.findOneWithEagerRelationships(id).map(questionBaseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QuestionBase : {}", id);
        questionBaseRepository.deleteById(id);
        questionBaseSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionBaseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of QuestionBases for query {}", query);
        return questionBaseSearchRepository.search(query, pageable).map(questionBaseMapper::toDto);
    }
}
