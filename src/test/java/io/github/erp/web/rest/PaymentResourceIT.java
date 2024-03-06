package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Payment;
import io.github.erp.domain.Payment;
import io.github.erp.domain.PaymentCategory;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.PaymentService;
import io.github.erp.service.criteria.PaymentCriteria;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.mapper.PaymentMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

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

    private static final BigDecimal DEFAULT_INVOICED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVOICED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INVOICED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final CurrencyTypes DEFAULT_SETTLEMENT_CURRENCY = CurrencyTypes.KES;
    private static final CurrencyTypes UPDATED_SETTLEMENT_CURRENCY = CurrencyTypes.USD;

    private static final byte[] DEFAULT_CALCULATION_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CALCULATION_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CALCULATION_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CALCULATION_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PURCHASE_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PURCHASE_ORDER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/payments";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Autowired
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentService paymentServiceMock;

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
            .invoicedAmount(DEFAULT_INVOICED_AMOUNT)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .settlementCurrency(DEFAULT_SETTLEMENT_CURRENCY)
            .calculationFile(DEFAULT_CALCULATION_FILE)
            .calculationFileContentType(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)
            .dealerName(DEFAULT_DEALER_NAME)
            .purchaseOrderNumber(DEFAULT_PURCHASE_ORDER_NUMBER)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
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
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .settlementCurrency(UPDATED_SETTLEMENT_CURRENCY)
            .calculationFile(UPDATED_CALCULATION_FILE)
            .calculationFileContentType(UPDATED_CALCULATION_FILE_CONTENT_TYPE)
            .dealerName(UPDATED_DEALER_NAME)
            .purchaseOrderNumber(UPDATED_PURCHASE_ORDER_NUMBER)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
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
        assertThat(testPayment.getInvoicedAmount()).isEqualByComparingTo(DEFAULT_INVOICED_AMOUNT);
        assertThat(testPayment.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testPayment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPayment.getSettlementCurrency()).isEqualTo(DEFAULT_SETTLEMENT_CURRENCY);
        assertThat(testPayment.getCalculationFile()).isEqualTo(DEFAULT_CALCULATION_FILE);
        assertThat(testPayment.getCalculationFileContentType()).isEqualTo(DEFAULT_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testPayment.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testPayment.getPurchaseOrderNumber()).isEqualTo(DEFAULT_PURCHASE_ORDER_NUMBER);
        assertThat(testPayment.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testPayment.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

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
    void checkSettlementCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setSettlementCurrency(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].settlementCurrency").value(hasItem(DEFAULT_SETTLEMENT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].calculationFileContentType").value(hasItem(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].calculationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].purchaseOrderNumber").value(hasItem(DEFAULT_PURCHASE_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentServiceMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.invoicedAmount").value(sameNumber(DEFAULT_INVOICED_AMOUNT)))
            .andExpect(jsonPath("$.paymentAmount").value(sameNumber(DEFAULT_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.settlementCurrency").value(DEFAULT_SETTLEMENT_CURRENCY.toString()))
            .andExpect(jsonPath("$.calculationFileContentType").value(DEFAULT_CALCULATION_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.calculationFile").value(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE)))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.purchaseOrderNumber").value(DEFAULT_PURCHASE_ORDER_NUMBER))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
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
    void getAllPaymentsByInvoicedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount equals to DEFAULT_INVOICED_AMOUNT
        defaultPaymentShouldBeFound("invoicedAmount.equals=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentList where invoicedAmount equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentShouldNotBeFound("invoicedAmount.equals=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByInvoicedAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount not equals to DEFAULT_INVOICED_AMOUNT
        defaultPaymentShouldNotBeFound("invoicedAmount.notEquals=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentList where invoicedAmount not equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentShouldBeFound("invoicedAmount.notEquals=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByInvoicedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount in DEFAULT_INVOICED_AMOUNT or UPDATED_INVOICED_AMOUNT
        defaultPaymentShouldBeFound("invoicedAmount.in=" + DEFAULT_INVOICED_AMOUNT + "," + UPDATED_INVOICED_AMOUNT);

        // Get all the paymentList where invoicedAmount equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentShouldNotBeFound("invoicedAmount.in=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByInvoicedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount is not null
        defaultPaymentShouldBeFound("invoicedAmount.specified=true");

        // Get all the paymentList where invoicedAmount is null
        defaultPaymentShouldNotBeFound("invoicedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByInvoicedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount is greater than or equal to DEFAULT_INVOICED_AMOUNT
        defaultPaymentShouldBeFound("invoicedAmount.greaterThanOrEqual=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentList where invoicedAmount is greater than or equal to UPDATED_INVOICED_AMOUNT
        defaultPaymentShouldNotBeFound("invoicedAmount.greaterThanOrEqual=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByInvoicedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount is less than or equal to DEFAULT_INVOICED_AMOUNT
        defaultPaymentShouldBeFound("invoicedAmount.lessThanOrEqual=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentList where invoicedAmount is less than or equal to SMALLER_INVOICED_AMOUNT
        defaultPaymentShouldNotBeFound("invoicedAmount.lessThanOrEqual=" + SMALLER_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByInvoicedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount is less than DEFAULT_INVOICED_AMOUNT
        defaultPaymentShouldNotBeFound("invoicedAmount.lessThan=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentList where invoicedAmount is less than UPDATED_INVOICED_AMOUNT
        defaultPaymentShouldBeFound("invoicedAmount.lessThan=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByInvoicedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where invoicedAmount is greater than DEFAULT_INVOICED_AMOUNT
        defaultPaymentShouldNotBeFound("invoicedAmount.greaterThan=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentList where invoicedAmount is greater than SMALLER_INVOICED_AMOUNT
        defaultPaymentShouldBeFound("invoicedAmount.greaterThan=" + SMALLER_INVOICED_AMOUNT);
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
    void getAllPaymentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where description equals to DEFAULT_DESCRIPTION
        defaultPaymentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the paymentList where description equals to UPDATED_DESCRIPTION
        defaultPaymentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where description not equals to DEFAULT_DESCRIPTION
        defaultPaymentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the paymentList where description not equals to UPDATED_DESCRIPTION
        defaultPaymentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPaymentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the paymentList where description equals to UPDATED_DESCRIPTION
        defaultPaymentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where description is not null
        defaultPaymentShouldBeFound("description.specified=true");

        // Get all the paymentList where description is null
        defaultPaymentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where description contains DEFAULT_DESCRIPTION
        defaultPaymentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the paymentList where description contains UPDATED_DESCRIPTION
        defaultPaymentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where description does not contain DEFAULT_DESCRIPTION
        defaultPaymentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the paymentList where description does not contain UPDATED_DESCRIPTION
        defaultPaymentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where settlementCurrency equals to DEFAULT_SETTLEMENT_CURRENCY
        defaultPaymentShouldBeFound("settlementCurrency.equals=" + DEFAULT_SETTLEMENT_CURRENCY);

        // Get all the paymentList where settlementCurrency equals to UPDATED_SETTLEMENT_CURRENCY
        defaultPaymentShouldNotBeFound("settlementCurrency.equals=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsBySettlementCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where settlementCurrency not equals to DEFAULT_SETTLEMENT_CURRENCY
        defaultPaymentShouldNotBeFound("settlementCurrency.notEquals=" + DEFAULT_SETTLEMENT_CURRENCY);

        // Get all the paymentList where settlementCurrency not equals to UPDATED_SETTLEMENT_CURRENCY
        defaultPaymentShouldBeFound("settlementCurrency.notEquals=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsBySettlementCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where settlementCurrency in DEFAULT_SETTLEMENT_CURRENCY or UPDATED_SETTLEMENT_CURRENCY
        defaultPaymentShouldBeFound("settlementCurrency.in=" + DEFAULT_SETTLEMENT_CURRENCY + "," + UPDATED_SETTLEMENT_CURRENCY);

        // Get all the paymentList where settlementCurrency equals to UPDATED_SETTLEMENT_CURRENCY
        defaultPaymentShouldNotBeFound("settlementCurrency.in=" + UPDATED_SETTLEMENT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsBySettlementCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where settlementCurrency is not null
        defaultPaymentShouldBeFound("settlementCurrency.specified=true");

        // Get all the paymentList where settlementCurrency is null
        defaultPaymentShouldNotBeFound("settlementCurrency.specified=false");
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
    void getAllPaymentsByPurchaseOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where purchaseOrderNumber equals to DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldBeFound("purchaseOrderNumber.equals=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the paymentList where purchaseOrderNumber equals to UPDATED_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldNotBeFound("purchaseOrderNumber.equals=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPurchaseOrderNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where purchaseOrderNumber not equals to DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldNotBeFound("purchaseOrderNumber.notEquals=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the paymentList where purchaseOrderNumber not equals to UPDATED_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldBeFound("purchaseOrderNumber.notEquals=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPurchaseOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where purchaseOrderNumber in DEFAULT_PURCHASE_ORDER_NUMBER or UPDATED_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldBeFound("purchaseOrderNumber.in=" + DEFAULT_PURCHASE_ORDER_NUMBER + "," + UPDATED_PURCHASE_ORDER_NUMBER);

        // Get all the paymentList where purchaseOrderNumber equals to UPDATED_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldNotBeFound("purchaseOrderNumber.in=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPurchaseOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where purchaseOrderNumber is not null
        defaultPaymentShouldBeFound("purchaseOrderNumber.specified=true");

        // Get all the paymentList where purchaseOrderNumber is null
        defaultPaymentShouldNotBeFound("purchaseOrderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPurchaseOrderNumberContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where purchaseOrderNumber contains DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldBeFound("purchaseOrderNumber.contains=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the paymentList where purchaseOrderNumber contains UPDATED_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldNotBeFound("purchaseOrderNumber.contains=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByPurchaseOrderNumberNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where purchaseOrderNumber does not contain DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldNotBeFound("purchaseOrderNumber.doesNotContain=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the paymentList where purchaseOrderNumber does not contain UPDATED_PURCHASE_ORDER_NUMBER
        defaultPaymentShouldBeFound("purchaseOrderNumber.doesNotContain=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentsByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the paymentList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where fileUploadToken is not null
        defaultPaymentShouldBeFound("fileUploadToken.specified=true");

        // Get all the paymentList where fileUploadToken is null
        defaultPaymentShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultPaymentShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultPaymentShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultPaymentShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the paymentList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where compilationToken is not null
        defaultPaymentShouldBeFound("compilationToken.specified=true");

        // Get all the paymentList where compilationToken is null
        defaultPaymentShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultPaymentShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultPaymentShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultPaymentShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultPaymentShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        PaymentLabel paymentLabel;
        if (TestUtil.findAll(em, PaymentLabel.class).isEmpty()) {
            paymentLabel = PaymentLabelResourceIT.createEntity(em);
            em.persist(paymentLabel);
            em.flush();
        } else {
            paymentLabel = TestUtil.findAll(em, PaymentLabel.class).get(0);
        }
        em.persist(paymentLabel);
        em.flush();
        payment.addPaymentLabel(paymentLabel);
        paymentRepository.saveAndFlush(payment);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the paymentList where paymentLabel equals to paymentLabelId
        defaultPaymentShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the paymentList where paymentLabel equals to (paymentLabelId + 1)
        defaultPaymentShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        PaymentCategory paymentCategory;
        if (TestUtil.findAll(em, PaymentCategory.class).isEmpty()) {
            paymentCategory = PaymentCategoryResourceIT.createEntity(em);
            em.persist(paymentCategory);
            em.flush();
        } else {
            paymentCategory = TestUtil.findAll(em, PaymentCategory.class).get(0);
        }
        em.persist(paymentCategory);
        em.flush();
        payment.setPaymentCategory(paymentCategory);
        paymentRepository.saveAndFlush(payment);
        Long paymentCategoryId = paymentCategory.getId();

        // Get all the paymentList where paymentCategory equals to paymentCategoryId
        defaultPaymentShouldBeFound("paymentCategoryId.equals=" + paymentCategoryId);

        // Get all the paymentList where paymentCategory equals to (paymentCategoryId + 1)
        defaultPaymentShouldNotBeFound("paymentCategoryId.equals=" + (paymentCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
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
        payment.addPlaceholder(placeholder);
        paymentRepository.saveAndFlush(payment);
        Long placeholderId = placeholder.getId();

        // Get all the paymentList where placeholder equals to placeholderId
        defaultPaymentShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the paymentList where placeholder equals to (placeholderId + 1)
        defaultPaymentShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        Payment paymentGroup;
        if (TestUtil.findAll(em, Payment.class).isEmpty()) {
            paymentGroup = PaymentResourceIT.createEntity(em);
            em.persist(paymentGroup);
            em.flush();
        } else {
            paymentGroup = TestUtil.findAll(em, Payment.class).get(0);
        }
        em.persist(paymentGroup);
        em.flush();
        payment.setPaymentGroup(paymentGroup);
        paymentRepository.saveAndFlush(payment);
        Long paymentGroupId = paymentGroup.getId();

        // Get all the paymentList where paymentGroup equals to paymentGroupId
        defaultPaymentShouldBeFound("paymentGroupId.equals=" + paymentGroupId);

        // Get all the paymentList where paymentGroup equals to (paymentGroupId + 1)
        defaultPaymentShouldNotBeFound("paymentGroupId.equals=" + (paymentGroupId + 1));
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
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].settlementCurrency").value(hasItem(DEFAULT_SETTLEMENT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].calculationFileContentType").value(hasItem(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].calculationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].purchaseOrderNumber").value(hasItem(DEFAULT_PURCHASE_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

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
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .settlementCurrency(UPDATED_SETTLEMENT_CURRENCY)
            .calculationFile(UPDATED_CALCULATION_FILE)
            .calculationFileContentType(UPDATED_CALCULATION_FILE_CONTENT_TYPE)
            .dealerName(UPDATED_DEALER_NAME)
            .purchaseOrderNumber(UPDATED_PURCHASE_ORDER_NUMBER)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
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
        assertThat(testPayment.getInvoicedAmount()).isEqualTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPayment.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testPayment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayment.getSettlementCurrency()).isEqualTo(UPDATED_SETTLEMENT_CURRENCY);
        assertThat(testPayment.getCalculationFile()).isEqualTo(UPDATED_CALCULATION_FILE);
        assertThat(testPayment.getCalculationFileContentType()).isEqualTo(UPDATED_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testPayment.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testPayment.getPurchaseOrderNumber()).isEqualTo(UPDATED_PURCHASE_ORDER_NUMBER);
        assertThat(testPayment.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPayment.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

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

        partialUpdatedPayment
            .paymentDate(UPDATED_PAYMENT_DATE)
            .description(UPDATED_DESCRIPTION)
            .calculationFile(UPDATED_CALCULATION_FILE)
            .calculationFileContentType(UPDATED_CALCULATION_FILE_CONTENT_TYPE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);

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
        assertThat(testPayment.getInvoicedAmount()).isEqualByComparingTo(DEFAULT_INVOICED_AMOUNT);
        assertThat(testPayment.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testPayment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayment.getSettlementCurrency()).isEqualTo(DEFAULT_SETTLEMENT_CURRENCY);
        assertThat(testPayment.getCalculationFile()).isEqualTo(UPDATED_CALCULATION_FILE);
        assertThat(testPayment.getCalculationFileContentType()).isEqualTo(UPDATED_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testPayment.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testPayment.getPurchaseOrderNumber()).isEqualTo(DEFAULT_PURCHASE_ORDER_NUMBER);
        assertThat(testPayment.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPayment.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
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
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .settlementCurrency(UPDATED_SETTLEMENT_CURRENCY)
            .calculationFile(UPDATED_CALCULATION_FILE)
            .calculationFileContentType(UPDATED_CALCULATION_FILE_CONTENT_TYPE)
            .dealerName(UPDATED_DEALER_NAME)
            .purchaseOrderNumber(UPDATED_PURCHASE_ORDER_NUMBER)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

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
        assertThat(testPayment.getInvoicedAmount()).isEqualByComparingTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPayment.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testPayment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPayment.getSettlementCurrency()).isEqualTo(UPDATED_SETTLEMENT_CURRENCY);
        assertThat(testPayment.getCalculationFile()).isEqualTo(UPDATED_CALCULATION_FILE);
        assertThat(testPayment.getCalculationFileContentType()).isEqualTo(UPDATED_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testPayment.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testPayment.getPurchaseOrderNumber()).isEqualTo(UPDATED_PURCHASE_ORDER_NUMBER);
        assertThat(testPayment.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPayment.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
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
        when(mockPaymentSearchRepository.search("id:" + payment.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(payment), PageRequest.of(0, 1), 1));

        // Search the payment
        restPaymentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].settlementCurrency").value(hasItem(DEFAULT_SETTLEMENT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].calculationFileContentType").value(hasItem(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].calculationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].purchaseOrderNumber").value(hasItem(DEFAULT_PURCHASE_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
