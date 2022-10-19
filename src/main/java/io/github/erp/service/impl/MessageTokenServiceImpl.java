package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.MessageToken;
import io.github.erp.repository.MessageTokenRepository;
import io.github.erp.repository.search.MessageTokenSearchRepository;
import io.github.erp.service.MessageTokenService;
import io.github.erp.service.dto.MessageTokenDTO;
import io.github.erp.service.mapper.MessageTokenMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MessageToken}.
 */
@Service
@Transactional
public class MessageTokenServiceImpl implements MessageTokenService {

    private final Logger log = LoggerFactory.getLogger(MessageTokenServiceImpl.class);

    private final MessageTokenRepository messageTokenRepository;

    private final MessageTokenMapper messageTokenMapper;

    private final MessageTokenSearchRepository messageTokenSearchRepository;

    public MessageTokenServiceImpl(
        MessageTokenRepository messageTokenRepository,
        MessageTokenMapper messageTokenMapper,
        MessageTokenSearchRepository messageTokenSearchRepository
    ) {
        this.messageTokenRepository = messageTokenRepository;
        this.messageTokenMapper = messageTokenMapper;
        this.messageTokenSearchRepository = messageTokenSearchRepository;
    }

    @Override
    public MessageTokenDTO save(MessageTokenDTO messageTokenDTO) {
        log.debug("Request to save MessageToken : {}", messageTokenDTO);
        MessageToken messageToken = messageTokenMapper.toEntity(messageTokenDTO);
        messageToken = messageTokenRepository.save(messageToken);
        MessageTokenDTO result = messageTokenMapper.toDto(messageToken);
        messageTokenSearchRepository.save(messageToken);
        return result;
    }

    @Override
    public Optional<MessageTokenDTO> partialUpdate(MessageTokenDTO messageTokenDTO) {
        log.debug("Request to partially update MessageToken : {}", messageTokenDTO);

        return messageTokenRepository
            .findById(messageTokenDTO.getId())
            .map(existingMessageToken -> {
                messageTokenMapper.partialUpdate(existingMessageToken, messageTokenDTO);

                return existingMessageToken;
            })
            .map(messageTokenRepository::save)
            .map(savedMessageToken -> {
                messageTokenSearchRepository.save(savedMessageToken);

                return savedMessageToken;
            })
            .map(messageTokenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageTokenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MessageTokens");
        return messageTokenRepository.findAll(pageable).map(messageTokenMapper::toDto);
    }

    public Page<MessageTokenDTO> findAllWithEagerRelationships(Pageable pageable) {
        return messageTokenRepository.findAllWithEagerRelationships(pageable).map(messageTokenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MessageTokenDTO> findOne(Long id) {
        log.debug("Request to get MessageToken : {}", id);
        return messageTokenRepository.findOneWithEagerRelationships(id).map(messageTokenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MessageToken : {}", id);
        messageTokenRepository.deleteById(id);
        messageTokenSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageTokenDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MessageTokens for query {}", query);
        return messageTokenSearchRepository.search(query, pageable).map(messageTokenMapper::toDto);
    }
}
