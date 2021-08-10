package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PaymentRequisition;
import io.github.erp.repository.PaymentRequisitionRepository;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import io.github.erp.service.mapper.PaymentRequisitionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PaymentRequisition}.
 */
@Service
@Transactional
public class PaymentRequisitionServiceImpl implements PaymentRequisitionService {

    private final Logger log = LoggerFactory.getLogger(PaymentRequisitionServiceImpl.class);

    private final PaymentRequisitionRepository paymentRequisitionRepository;

    private final PaymentRequisitionMapper paymentRequisitionMapper;

    private final PaymentRequisitionSearchRepository paymentRequisitionSearchRepository;

    public PaymentRequisitionServiceImpl(
        PaymentRequisitionRepository paymentRequisitionRepository,
        PaymentRequisitionMapper paymentRequisitionMapper,
        PaymentRequisitionSearchRepository paymentRequisitionSearchRepository
    ) {
        this.paymentRequisitionRepository = paymentRequisitionRepository;
        this.paymentRequisitionMapper = paymentRequisitionMapper;
        this.paymentRequisitionSearchRepository = paymentRequisitionSearchRepository;
    }

    @Override
    public PaymentRequisitionDTO save(PaymentRequisitionDTO paymentRequisitionDTO) {
        log.debug("Request to save PaymentRequisition : {}", paymentRequisitionDTO);
        PaymentRequisition paymentRequisition = paymentRequisitionMapper.toEntity(paymentRequisitionDTO);
        paymentRequisition = paymentRequisitionRepository.save(paymentRequisition);
        PaymentRequisitionDTO result = paymentRequisitionMapper.toDto(paymentRequisition);
        paymentRequisitionSearchRepository.save(paymentRequisition);
        return result;
    }

    @Override
    public Optional<PaymentRequisitionDTO> partialUpdate(PaymentRequisitionDTO paymentRequisitionDTO) {
        log.debug("Request to partially update PaymentRequisition : {}", paymentRequisitionDTO);

        return paymentRequisitionRepository
            .findById(paymentRequisitionDTO.getId())
            .map(
                existingPaymentRequisition -> {
                    paymentRequisitionMapper.partialUpdate(existingPaymentRequisition, paymentRequisitionDTO);

                    return existingPaymentRequisition;
                }
            )
            .map(paymentRequisitionRepository::save)
            .map(
                savedPaymentRequisition -> {
                    paymentRequisitionSearchRepository.save(savedPaymentRequisition);

                    return savedPaymentRequisition;
                }
            )
            .map(paymentRequisitionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentRequisitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentRequisitions");
        return paymentRequisitionRepository.findAll(pageable).map(paymentRequisitionMapper::toDto);
    }

    /**
     *  Get all the paymentRequisitions where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentRequisitionDTO> findAllWherePaymentIsNull() {
        log.debug("Request to get all paymentRequisitions where Payment is null");
        return StreamSupport
            .stream(paymentRequisitionRepository.findAll().spliterator(), false)
            .filter(paymentRequisition -> paymentRequisition.getPayment() == null)
            .map(paymentRequisitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentRequisitionDTO> findOne(Long id) {
        log.debug("Request to get PaymentRequisition : {}", id);
        return paymentRequisitionRepository.findById(id).map(paymentRequisitionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentRequisition : {}", id);
        paymentRequisitionRepository.deleteById(id);
        paymentRequisitionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentRequisitionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentRequisitions for query {}", query);
        return paymentRequisitionSearchRepository.search(queryStringQuery(query), pageable).map(paymentRequisitionMapper::toDto);
    }
}
