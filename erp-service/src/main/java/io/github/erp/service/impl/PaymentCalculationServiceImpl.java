package io.github.erp.service.impl;

import io.github.erp.service.PaymentCalculationService;
import io.github.erp.domain.PaymentCalculation;
import io.github.erp.repository.PaymentCalculationRepository;
import io.github.erp.repository.search.PaymentCalculationSearchRepository;
import io.github.erp.service.dto.PaymentCalculationDTO;
import io.github.erp.service.mapper.PaymentCalculationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link PaymentCalculation}.
 */
@Service
@Transactional
public class PaymentCalculationServiceImpl implements PaymentCalculationService {

    private final Logger log = LoggerFactory.getLogger(PaymentCalculationServiceImpl.class);

    private final PaymentCalculationRepository paymentCalculationRepository;

    private final PaymentCalculationMapper paymentCalculationMapper;

    private final PaymentCalculationSearchRepository paymentCalculationSearchRepository;

    public PaymentCalculationServiceImpl(PaymentCalculationRepository paymentCalculationRepository, PaymentCalculationMapper paymentCalculationMapper, PaymentCalculationSearchRepository paymentCalculationSearchRepository) {
        this.paymentCalculationRepository = paymentCalculationRepository;
        this.paymentCalculationMapper = paymentCalculationMapper;
        this.paymentCalculationSearchRepository = paymentCalculationSearchRepository;
    }

    @Override
    public PaymentCalculationDTO save(PaymentCalculationDTO paymentCalculationDTO) {
        log.debug("Request to save PaymentCalculation : {}", paymentCalculationDTO);
        PaymentCalculation paymentCalculation = paymentCalculationMapper.toEntity(paymentCalculationDTO);
        paymentCalculation = paymentCalculationRepository.save(paymentCalculation);
        PaymentCalculationDTO result = paymentCalculationMapper.toDto(paymentCalculation);
        paymentCalculationSearchRepository.save(paymentCalculation);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCalculationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentCalculations");
        return paymentCalculationRepository.findAll(pageable)
            .map(paymentCalculationMapper::toDto);
    }



    /**
     *  Get all the paymentCalculations where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<PaymentCalculationDTO> findAllWherePaymentIsNull() {
        log.debug("Request to get all paymentCalculations where Payment is null");
        return StreamSupport
            .stream(paymentCalculationRepository.findAll().spliterator(), false)
            .filter(paymentCalculation -> paymentCalculation.getPayment() == null)
            .map(paymentCalculationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCalculationDTO> findOne(Long id) {
        log.debug("Request to get PaymentCalculation : {}", id);
        return paymentCalculationRepository.findById(id)
            .map(paymentCalculationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentCalculation : {}", id);
        paymentCalculationRepository.deleteById(id);
        paymentCalculationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCalculationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentCalculations for query {}", query);
        return paymentCalculationSearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentCalculationMapper::toDto);
    }
}
