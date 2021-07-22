package io.github.erp.service.impl;

import io.github.erp.service.PaymentService;
import io.github.erp.domain.Payment;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.PaymentCalculationRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final PaymentSearchRepository paymentSearchRepository;

    private final PaymentCalculationRepository paymentCalculationRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, PaymentSearchRepository paymentSearchRepository, PaymentCalculationRepository paymentCalculationRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentSearchRepository = paymentSearchRepository;
        this.paymentCalculationRepository = paymentCalculationRepository;
    }

    @Override
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        Long paymentCalculationId = paymentDTO.getPaymentCalculationId();
        paymentCalculationRepository.findById(paymentCalculationId).ifPresent(payment::paymentCalculation);
        payment = paymentRepository.save(payment);
        PaymentDTO result = paymentMapper.toDto(payment);
        paymentSearchRepository.save(payment);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable)
            .map(paymentMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id)
            .map(paymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
        paymentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        return paymentSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentMapper::toDto);
    }
}
