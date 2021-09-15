package io.github.erp.web.rest;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Payment;
import io.github.erp.domain.PaymentRequisition;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PaymentRequisitionRepository;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.service.criteria.PaymentRequisitionCriteria;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import io.github.erp.service.mapper.PaymentRequisitionMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PaymentRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentRequisitionResourceIT {

    private static final BigDecimal DEFAULT_INVOICED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVOICED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INVOICED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DISBURSEMENT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISBURSEMENT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISBURSEMENT_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VATABLE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_VATABLE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_VATABLE_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/payment-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/payment-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRequisitionRepository paymentRequisitionRepository;

    @Mock
    private PaymentRequisitionRepository paymentRequisitionRepositoryMock;

    @Autowired
    private PaymentRequisitionMapper paymentRequisitionMapper;

    @Mock
    private PaymentRequisitionService paymentRequisitionServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PaymentRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentRequisitionSearchRepository mockPaymentRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentRequisitionMockMvc;

    private PaymentRequisition paymentRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentRequisition createEntity(EntityManager em) {
        PaymentRequisition paymentRequisition = new PaymentRequisition()
            .invoicedAmount(DEFAULT_INVOICED_AMOUNT)
            .disbursementCost(DEFAULT_DISBURSEMENT_COST)
            .vatableAmount(DEFAULT_VATABLE_AMOUNT);
        return paymentRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentRequisition createUpdatedEntity(EntityManager em) {
        PaymentRequisition paymentRequisition = new PaymentRequisition()
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .vatableAmount(UPDATED_VATABLE_AMOUNT);
        return paymentRequisition;
    }

    @BeforeEach
    public void initTest() {
        paymentRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentRequisition() throws Exception {
        int databaseSizeBeforeCreate = paymentRequisitionRepository.findAll().size();
        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);
        restPaymentRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentRequisition testPaymentRequisition = paymentRequisitionList.get(paymentRequisitionList.size() - 1);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualByComparingTo(DEFAULT_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualByComparingTo(DEFAULT_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getVatableAmount()).isEqualByComparingTo(DEFAULT_VATABLE_AMOUNT);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(1)).save(testPaymentRequisition);
    }

    @Test
    @Transactional
    void createPaymentRequisitionWithExistingId() throws Exception {
        // Create the PaymentRequisition with an existing ID
        paymentRequisition.setId(1L);
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        int databaseSizeBeforeCreate = paymentRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitions() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList
        restPaymentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(sameNumber(DEFAULT_DISBURSEMENT_COST))))
            .andExpect(jsonPath("$.[*].vatableAmount").value(hasItem(sameNumber(DEFAULT_VATABLE_AMOUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentRequisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentRequisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPaymentRequisition() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get the paymentRequisition
        restPaymentRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentRequisition.getId().intValue()))
            .andExpect(jsonPath("$.invoicedAmount").value(sameNumber(DEFAULT_INVOICED_AMOUNT)))
            .andExpect(jsonPath("$.disbursementCost").value(sameNumber(DEFAULT_DISBURSEMENT_COST)))
            .andExpect(jsonPath("$.vatableAmount").value(sameNumber(DEFAULT_VATABLE_AMOUNT)));
    }

    @Test
    @Transactional
    void getPaymentRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        Long id = paymentRequisition.getId();

        defaultPaymentRequisitionShouldBeFound("id.equals=" + id);
        defaultPaymentRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount equals to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.equals=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.equals=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount not equals to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.notEquals=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount not equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.notEquals=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount in DEFAULT_INVOICED_AMOUNT or UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.in=" + DEFAULT_INVOICED_AMOUNT + "," + UPDATED_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.in=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is not null
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.specified=true");

        // Get all the paymentRequisitionList where invoicedAmount is null
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is greater than or equal to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.greaterThanOrEqual=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is greater than or equal to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.greaterThanOrEqual=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is less than or equal to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.lessThanOrEqual=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is less than or equal to SMALLER_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.lessThanOrEqual=" + SMALLER_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is less than DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.lessThan=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is less than UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.lessThan=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByInvoicedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is greater than DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.greaterThan=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is greater than SMALLER_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.greaterThan=" + SMALLER_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost equals to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.equals=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost equals to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.equals=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost not equals to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.notEquals=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost not equals to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.notEquals=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost in DEFAULT_DISBURSEMENT_COST or UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.in=" + DEFAULT_DISBURSEMENT_COST + "," + UPDATED_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost equals to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.in=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is not null
        defaultPaymentRequisitionShouldBeFound("disbursementCost.specified=true");

        // Get all the paymentRequisitionList where disbursementCost is null
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is greater than or equal to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.greaterThanOrEqual=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is greater than or equal to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.greaterThanOrEqual=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is less than or equal to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.lessThanOrEqual=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is less than or equal to SMALLER_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.lessThanOrEqual=" + SMALLER_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is less than DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.lessThan=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is less than UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.lessThan=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDisbursementCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is greater than DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.greaterThan=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is greater than SMALLER_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.greaterThan=" + SMALLER_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount equals to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.equals=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount equals to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.equals=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount not equals to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.notEquals=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount not equals to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.notEquals=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount in DEFAULT_VATABLE_AMOUNT or UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.in=" + DEFAULT_VATABLE_AMOUNT + "," + UPDATED_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount equals to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.in=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is not null
        defaultPaymentRequisitionShouldBeFound("vatableAmount.specified=true");

        // Get all the paymentRequisitionList where vatableAmount is null
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is greater than or equal to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.greaterThanOrEqual=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is greater than or equal to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.greaterThanOrEqual=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is less than or equal to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.lessThanOrEqual=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is less than or equal to SMALLER_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.lessThanOrEqual=" + SMALLER_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is less than DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.lessThan=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is less than UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.lessThan=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByVatableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is greater than DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.greaterThan=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is greater than SMALLER_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.greaterThan=" + SMALLER_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Payment payment;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            payment = PaymentResourceIT.createEntity(em);
            em.persist(payment);
            em.flush();
        } else {
            payment = TestUtil.findAll(em, Payment.class).get(0);
        }
        em.persist(payment);
        em.flush();
        paymentRequisition.setPayment(payment);
        payment.setPaymentRequisition(paymentRequisition);
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Long paymentId = payment.getId();

        // Get all the paymentRequisitionList where payment equals to paymentId
        defaultPaymentRequisitionShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the paymentRequisitionList where payment equals to (paymentId + 1)
        defaultPaymentRequisitionShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealer);
        em.flush();
        paymentRequisition.setDealer(dealer);
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Long dealerId = dealer.getId();

        // Get all the paymentRequisitionList where dealer equals to dealerId
        defaultPaymentRequisitionShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the paymentRequisitionList where dealer equals to (dealerId + 1)
        defaultPaymentRequisitionShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        paymentRequisition.addPlaceholder(placeholder);
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Long placeholderId = placeholder.getId();

        // Get all the paymentRequisitionList where placeholder equals to placeholderId
        defaultPaymentRequisitionShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the paymentRequisitionList where placeholder equals to (placeholderId + 1)
        defaultPaymentRequisitionShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentRequisitionShouldBeFound(String filter) throws Exception {
        restPaymentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(sameNumber(DEFAULT_DISBURSEMENT_COST))))
            .andExpect(jsonPath("$.[*].vatableAmount").value(hasItem(sameNumber(DEFAULT_VATABLE_AMOUNT))));

        // Check, that the count call also returns 1
        restPaymentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentRequisitionShouldNotBeFound(String filter) throws Exception {
        restPaymentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentRequisition() throws Exception {
        // Get the paymentRequisition
        restPaymentRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentRequisition() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();

        // Update the paymentRequisition
        PaymentRequisition updatedPaymentRequisition = paymentRequisitionRepository.findById(paymentRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentRequisition are not directly saved in db
        em.detach(updatedPaymentRequisition);
        updatedPaymentRequisition
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .vatableAmount(UPDATED_VATABLE_AMOUNT);
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(updatedPaymentRequisition);

        restPaymentRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PaymentRequisition testPaymentRequisition = paymentRequisitionList.get(paymentRequisitionList.size() - 1);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualTo(UPDATED_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getVatableAmount()).isEqualTo(UPDATED_VATABLE_AMOUNT);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository).save(testPaymentRequisition);
    }

    @Test
    @Transactional
    void putNonExistingPaymentRequisition() throws Exception {
        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();
        paymentRequisition.setId(count.incrementAndGet());

        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentRequisition() throws Exception {
        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();
        paymentRequisition.setId(count.incrementAndGet());

        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentRequisition() throws Exception {
        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();
        paymentRequisition.setId(count.incrementAndGet());

        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    void partialUpdatePaymentRequisitionWithPatch() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();

        // Update the paymentRequisition using partial update
        PaymentRequisition partialUpdatedPaymentRequisition = new PaymentRequisition();
        partialUpdatedPaymentRequisition.setId(paymentRequisition.getId());

        partialUpdatedPaymentRequisition
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .vatableAmount(UPDATED_VATABLE_AMOUNT);

        restPaymentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PaymentRequisition testPaymentRequisition = paymentRequisitionList.get(paymentRequisitionList.size() - 1);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualByComparingTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualByComparingTo(UPDATED_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getVatableAmount()).isEqualByComparingTo(UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void fullUpdatePaymentRequisitionWithPatch() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();

        // Update the paymentRequisition using partial update
        PaymentRequisition partialUpdatedPaymentRequisition = new PaymentRequisition();
        partialUpdatedPaymentRequisition.setId(paymentRequisition.getId());

        partialUpdatedPaymentRequisition
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .vatableAmount(UPDATED_VATABLE_AMOUNT);

        restPaymentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentRequisition))
            )
            .andExpect(status().isOk());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PaymentRequisition testPaymentRequisition = paymentRequisitionList.get(paymentRequisitionList.size() - 1);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualByComparingTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualByComparingTo(UPDATED_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getVatableAmount()).isEqualByComparingTo(UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentRequisition() throws Exception {
        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();
        paymentRequisition.setId(count.incrementAndGet());

        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentRequisition() throws Exception {
        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();
        paymentRequisition.setId(count.incrementAndGet());

        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentRequisition() throws Exception {
        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();
        paymentRequisition.setId(count.incrementAndGet());

        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    void deletePaymentRequisition() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        int databaseSizeBeforeDelete = paymentRequisitionRepository.findAll().size();

        // Delete the paymentRequisition
        restPaymentRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(1)).deleteById(paymentRequisition.getId());
    }

    @Test
    @Transactional
    void searchPaymentRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        when(mockPaymentRequisitionSearchRepository.search("id:" + paymentRequisition.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paymentRequisition), PageRequest.of(0, 1), 1));

        // Search the paymentRequisition
        restPaymentRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + paymentRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(sameNumber(DEFAULT_DISBURSEMENT_COST))))
            .andExpect(jsonPath("$.[*].vatableAmount").value(hasItem(sameNumber(DEFAULT_VATABLE_AMOUNT))));
    }
}
