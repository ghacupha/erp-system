package io.github.erp.service.impl;

import io.github.erp.service.PaymentCategoryService;
import io.github.erp.domain.PaymentCategory;
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.mapper.PaymentCategoryMapper;
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
 * Service Implementation for managing {@link PaymentCategory}.
 */
@Service
@Transactional
public class PaymentCategoryServiceImpl implements PaymentCategoryService {

    private final Logger log = LoggerFactory.getLogger(PaymentCategoryServiceImpl.class);

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final PaymentCategoryMapper paymentCategoryMapper;

    private final PaymentCategorySearchRepository paymentCategorySearchRepository;

    public PaymentCategoryServiceImpl(PaymentCategoryRepository paymentCategoryRepository, PaymentCategoryMapper paymentCategoryMapper, PaymentCategorySearchRepository paymentCategorySearchRepository) {
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.paymentCategoryMapper = paymentCategoryMapper;
        this.paymentCategorySearchRepository = paymentCategorySearchRepository;
    }

    @Override
    public PaymentCategoryDTO save(PaymentCategoryDTO paymentCategoryDTO) {
        log.debug("Request to save PaymentCategory : {}", paymentCategoryDTO);
        PaymentCategory paymentCategory = paymentCategoryMapper.toEntity(paymentCategoryDTO);
        paymentCategory = paymentCategoryRepository.save(paymentCategory);
        PaymentCategoryDTO result = paymentCategoryMapper.toDto(paymentCategory);
        paymentCategorySearchRepository.save(paymentCategory);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentCategories");
        return paymentCategoryRepository.findAll(pageable)
            .map(paymentCategoryMapper::toDto);
    }



    /**
     *  Get all the paymentCategories where Payment is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<PaymentCategoryDTO> findAllWherePaymentIsNull() {
        log.debug("Request to get all paymentCategories where Payment is null");
        return StreamSupport
            .stream(paymentCategoryRepository.findAll().spliterator(), false)
            .filter(paymentCategory -> paymentCategory.getPayment() == null)
            .map(paymentCategoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentCategoryDTO> findOne(Long id) {
        log.debug("Request to get PaymentCategory : {}", id);
        return paymentCategoryRepository.findById(id)
            .map(paymentCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentCategory : {}", id);
        paymentCategoryRepository.deleteById(id);
        paymentCategorySearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentCategories for query {}", query);
        return paymentCategorySearchRepository.search(queryStringQuery(query), pageable)
            .map(paymentCategoryMapper::toDto);
    }
}
