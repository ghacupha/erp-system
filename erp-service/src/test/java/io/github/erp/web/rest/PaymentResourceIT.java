package io.github.erp.web.rest;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Invoice;
import io.github.erp.domain.Payment;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.criteria.PaymentCriteria;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.mapper.PaymentMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final String DEFAULT_PAYMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/payments";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentSearchRepository mockPaymentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .paymentNumber(DEFAULT_PAYMENT_NUMBER)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .dealerName(DEFAULT_DEALER_NAME);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .dealerName(UPDATED_DEALER_NAME);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentNumber()).isEqualTo(DEFAULT_PAYMENT_NUMBER);
        assertThat(testPayment.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPayment.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testPayment.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).save(testPayment);
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.paymentNumber").value(DEFAULT_PAYMENT_NUMBER))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(sameNumber(DEFAULT_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentShouldBeFound("id.equals=" + id);
        defaultPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentNumber equals to DEFAULT_PAYMENT_NUMBER
        defaultPaymentShouldBeFound("paymentNumber.equals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultPaymentShouldNotBeFound("paymentNumber.equals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentNumber not equals to DEFAULT_PAYMENT_NUMBER
        defaultPaymentShouldNotBeFound("paymentNumber.notEquals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentList where paymentNumber not equals to UPDATED_PAYMENT_NUMBER
        defaultPaymentShouldBeFound("paymentNumber.notEquals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentNumber in DEFAULT_PAYMENT_NUMBER or UPDATED_PAYMENT_NUMBER
        defaultPaymentShouldBeFound("paymentNumber.in=" + DEFAULT_PAYMENT_NUMBER + "," + UPDATED_PAYMENT_NUMBER);

        // Get all the paymentList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultPaymentShouldNotBeFound("paymentNumber.in=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentNumber is not null
        defaultPaymentShouldBeFound("paymentNumber.specified=true");

        // Get all the paymentList where paymentNumber is null
        defaultPaymentShouldNotBeFound("paymentNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentNumberContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentNumber contains DEFAULT_PAYMENT_NUMBER
        defaultPaymentShouldBeFound("paymentNumber.contains=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentList where paymentNumber contains UPDATED_PAYMENT_NUMBER
        defaultPaymentShouldNotBeFound("paymentNumber.contains=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentNumber does not contain DEFAULT_PAYMENT_NUMBER
        defaultPaymentShouldNotBeFound("paymentNumber.doesNotContain=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentList where paymentNumber does not contain UPDATED_PAYMENT_NUMBER
        defaultPaymentShouldBeFound("paymentNumber.doesNotContain=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the paymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is not null
        defaultPaymentShouldBeFound("paymentDate.specified=true");

        // Get all the paymentList where paymentDate is null
        defaultPaymentShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount equals to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentShouldBeFound("paymentAmount.equals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultPaymentShouldNotBeFound("paymentAmount.equals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount not equals to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentShouldNotBeFound("paymentAmount.notEquals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentList where paymentAmount not equals to UPDATED_PAYMENT_AMOUNT
        defaultPaymentShouldBeFound("paymentAmount.notEquals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount in DEFAULT_PAYMENT_AMOUNT or UPDATED_PAYMENT_AMOUNT
        defaultPaymentShouldBeFound("paymentAmount.in=" + DEFAULT_PAYMENT_AMOUNT + "," + UPDATED_PAYMENT_AMOUNT);

        // Get all the paymentList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultPaymentShouldNotBeFound("paymentAmount.in=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount is not null
        defaultPaymentShouldBeFound("paymentAmount.specified=true");

        // Get all the paymentList where paymentAmount is null
        defaultPaymentShouldNotBeFound("paymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount is greater than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentShouldBeFound("paymentAmount.greaterThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentList where paymentAmount is greater than or equal to UPDATED_PAYMENT_AMOUNT
        defaultPaymentShouldNotBeFound("paymentAmount.greaterThanOrEqual=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount is less than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentShouldBeFound("paymentAmount.lessThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentList where paymentAmount is less than or equal to SMALLER_PAYMENT_AMOUNT
        defaultPaymentShouldNotBeFound("paymentAmount.lessThanOrEqual=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount is less than DEFAULT_PAYMENT_AMOUNT
        defaultPaymentShouldNotBeFound("paymentAmount.lessThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentList where paymentAmount is less than UPDATED_PAYMENT_AMOUNT
        defaultPaymentShouldBeFound("paymentAmount.lessThan=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentAmount is greater than DEFAULT_PAYMENT_AMOUNT
        defaultPaymentShouldNotBeFound("paymentAmount.greaterThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentList where paymentAmount is greater than SMALLER_PAYMENT_AMOUNT
        defaultPaymentShouldBeFound("paymentAmount.greaterThan=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where dealerName equals to DEFAULT_DEALER_NAME
        defaultPaymentShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the paymentList where dealerName equals to UPDATED_DEALER_NAME
        defaultPaymentShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentsByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultPaymentShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the paymentList where dealerName not equals to UPDATED_DEALER_NAME
        defaultPaymentShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentsByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultPaymentShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the paymentList where dealerName equals to UPDATED_DEALER_NAME
        defaultPaymentShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentsByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where dealerName is not null
        defaultPaymentShouldBeFound("dealerName.specified=true");

        // Get all the paymentList where dealerName is null
        defaultPaymentShouldNotBeFound("dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where dealerName contains DEFAULT_DEALER_NAME
        defaultPaymentShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the paymentList where dealerName contains UPDATED_DEALER_NAME
        defaultPaymentShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentsByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultPaymentShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the paymentList where dealerName does not contain UPDATED_DEALER_NAME
        defaultPaymentShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentsByOwnedInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        Invoice ownedInvoice = InvoiceResourceIT.createEntity(em);
        em.persist(ownedInvoice);
        em.flush();
        payment.addOwnedInvoice(ownedInvoice);
        paymentRepository.saveAndFlush(payment);
        Long ownedInvoiceId = ownedInvoice.getId();

        // Get all the paymentList where ownedInvoice equals to ownedInvoiceId
        defaultPaymentShouldBeFound("ownedInvoiceId.equals=" + ownedInvoiceId);

        // Get all the paymentList where ownedInvoice equals to (ownedInvoiceId + 1)
        defaultPaymentShouldNotBeFound("ownedInvoiceId.equals=" + (ownedInvoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .dealerName(UPDATED_DEALER_NAME);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentNumber()).isEqualTo(UPDATED_PAYMENT_NUMBER);
        assertThat(testPayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPayment.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testPayment.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository).save(testPayment);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.paymentDate(UPDATED_PAYMENT_DATE);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentNumber()).isEqualTo(DEFAULT_PAYMENT_NUMBER);
        assertThat(testPayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPayment.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testPayment.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .dealerName(UPDATED_DEALER_NAME);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentNumber()).isEqualTo(UPDATED_PAYMENT_NUMBER);
        assertThat(testPayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPayment.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testPayment.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(0)).save(payment);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Payment in Elasticsearch
        verify(mockPaymentSearchRepository, times(1)).deleteById(payment.getId());
    }

    @Test
    @Transactional
    void searchPayment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        when(mockPaymentSearchRepository.search(queryStringQuery("id:" + payment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payment), PageRequest.of(0, 1), 1));

        // Search the payment
        restPaymentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)));
    }
}
