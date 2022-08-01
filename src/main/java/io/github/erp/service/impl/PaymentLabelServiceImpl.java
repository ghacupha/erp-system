package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PaymentLabel;
import io.github.erp.repository.PaymentLabelRepository;
import io.github.erp.repository.search.PaymentLabelSearchRepository;
import io.github.erp.service.PaymentLabelService;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.service.mapper.PaymentLabelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentLabel}.
 */
@Service
@Transactional
public class PaymentLabelServiceImpl implements PaymentLabelService {

    private final Logger log = LoggerFactory.getLogger(PaymentLabelServiceImpl.class);

    private final PaymentLabelRepository paymentLabelRepository;

    private final PaymentLabelMapper paymentLabelMapper;

    private final PaymentLabelSearchRepository paymentLabelSearchRepository;

    public PaymentLabelServiceImpl(
        PaymentLabelRepository paymentLabelRepository,
        PaymentLabelMapper paymentLabelMapper,
        PaymentLabelSearchRepository paymentLabelSearchRepository
    ) {
        this.paymentLabelRepository = paymentLabelRepository;
        this.paymentLabelMapper = paymentLabelMapper;
        this.paymentLabelSearchRepository = paymentLabelSearchRepository;
    }

    @Override
    public PaymentLabelDTO save(PaymentLabelDTO paymentLabelDTO) {
        log.debug("Request to save PaymentLabel : {}", paymentLabelDTO);
        PaymentLabel paymentLabel = paymentLabelMapper.toEntity(paymentLabelDTO);
        paymentLabel = paymentLabelRepository.save(paymentLabel);
        PaymentLabelDTO result = paymentLabelMapper.toDto(paymentLabel);
        paymentLabelSearchRepository.save(paymentLabel);
        return result;
    }

    @Override
    public Optional<PaymentLabelDTO> partialUpdate(PaymentLabelDTO paymentLabelDTO) {
        log.debug("Request to partially update PaymentLabel : {}", paymentLabelDTO);

        return paymentLabelRepository
            .findById(paymentLabelDTO.getId())
            .map(existingPaymentLabel -> {
                paymentLabelMapper.partialUpdate(existingPaymentLabel, paymentLabelDTO);

                return existingPaymentLabel;
            })
            .map(paymentLabelRepository::save)
            .map(savedPaymentLabel -> {
                paymentLabelSearchRepository.save(savedPaymentLabel);

                return savedPaymentLabel;
            })
            .map(paymentLabelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentLabelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentLabels");
        return paymentLabelRepository.findAll(pageable).map(paymentLabelMapper::toDto);
    }

    public Page<PaymentLabelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentLabelRepository.findAllWithEagerRelationships(pageable).map(paymentLabelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentLabelDTO> findOne(Long id) {
        log.debug("Request to get PaymentLabel : {}", id);
        return paymentLabelRepository.findOneWithEagerRelationships(id).map(paymentLabelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentLabel : {}", id);
        paymentLabelRepository.deleteById(id);
        paymentLabelSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentLabelDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentLabels for query {}", query);
        return paymentLabelSearchRepository.search(query, pageable).map(paymentLabelMapper::toDto);
    }
}
