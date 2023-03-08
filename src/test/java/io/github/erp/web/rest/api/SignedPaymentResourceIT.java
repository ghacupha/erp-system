package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.PaymentCategory;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SignedPayment;
import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.repository.SignedPaymentRepository;
import io.github.erp.repository.search.SignedPaymentSearchRepository;
import io.github.erp.service.SignedPaymentService;
import io.github.erp.service.dto.SignedPaymentDTO;
import io.github.erp.service.mapper.SignedPaymentMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.TestUtil;
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
 * Integration tests for the {@link SignedPaymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class SignedPaymentResourceIT {

    private static final String DEFAULT_TRANSACTION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final CurrencyTypes DEFAULT_TRANSACTION_CURRENCY = CurrencyTypes.KES;
    private static final CurrencyTypes UPDATED_TRANSACTION_CURRENCY = CurrencyTypes.USD;

    private static final BigDecimal DEFAULT_TRANSACTION_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_TRANSACTION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal SMALLER_TRANSACTION_AMOUNT = new BigDecimal(0 - 1);

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/signed-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/signed-payments";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SignedPaymentRepository signedPaymentRepository;

    @Mock
    private SignedPaymentRepository signedPaymentRepositoryMock;

    @Autowired
    private SignedPaymentMapper signedPaymentMapper;

    @Mock
    private SignedPaymentService signedPaymentServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SignedPaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private SignedPaymentSearchRepository mockSignedPaymentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSignedPaymentMockMvc;

    private SignedPayment signedPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignedPayment createEntity(EntityManager em) {
        SignedPayment signedPayment = new SignedPayment()
            .transactionNumber(DEFAULT_TRANSACTION_NUMBER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .transactionCurrency(DEFAULT_TRANSACTION_CURRENCY)
            .transactionAmount(DEFAULT_TRANSACTION_AMOUNT)
            .dealerName(DEFAULT_DEALER_NAME)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        return signedPayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SignedPayment createUpdatedEntity(EntityManager em) {
        SignedPayment signedPayment = new SignedPayment()
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionCurrency(UPDATED_TRANSACTION_CURRENCY)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .dealerName(UPDATED_DEALER_NAME)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        return signedPayment;
    }

    @BeforeEach
    public void initTest() {
        signedPayment = createEntity(em);
    }

    @Test
    @Transactional
    void createSignedPayment() throws Exception {
        int databaseSizeBeforeCreate = signedPaymentRepository.findAll().size();
        // Create the SignedPayment
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);
        restSignedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeCreate + 1);
        SignedPayment testSignedPayment = signedPaymentList.get(signedPaymentList.size() - 1);
        assertThat(testSignedPayment.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testSignedPayment.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSignedPayment.getTransactionCurrency()).isEqualTo(DEFAULT_TRANSACTION_CURRENCY);
        assertThat(testSignedPayment.getTransactionAmount()).isEqualByComparingTo(DEFAULT_TRANSACTION_AMOUNT);
        assertThat(testSignedPayment.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testSignedPayment.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testSignedPayment.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(1)).save(testSignedPayment);
    }

    @Test
    @Transactional
    void createSignedPaymentWithExistingId() throws Exception {
        // Create the SignedPayment with an existing ID
        signedPayment.setId(1L);
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        int databaseSizeBeforeCreate = signedPaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(0)).save(signedPayment);
    }

    @Test
    @Transactional
    void checkTransactionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = signedPaymentRepository.findAll().size();
        // set the field null
        signedPayment.setTransactionNumber(null);

        // Create the SignedPayment, which fails.
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        restSignedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = signedPaymentRepository.findAll().size();
        // set the field null
        signedPayment.setTransactionDate(null);

        // Create the SignedPayment, which fails.
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        restSignedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = signedPaymentRepository.findAll().size();
        // set the field null
        signedPayment.setTransactionCurrency(null);

        // Create the SignedPayment, which fails.
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        restSignedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = signedPaymentRepository.findAll().size();
        // set the field null
        signedPayment.setTransactionAmount(null);

        // Create the SignedPayment, which fails.
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        restSignedPaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSignedPayments() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList
        restSignedPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signedPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionCurrency").value(hasItem(DEFAULT_TRANSACTION_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSignedPaymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(signedPaymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSignedPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(signedPaymentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSignedPaymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(signedPaymentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSignedPaymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(signedPaymentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSignedPayment() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get the signedPayment
        restSignedPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, signedPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(signedPayment.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.transactionCurrency").value(DEFAULT_TRANSACTION_CURRENCY.toString()))
            .andExpect(jsonPath("$.transactionAmount").value(sameNumber(DEFAULT_TRANSACTION_AMOUNT)))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }

    @Test
    @Transactional
    void getSignedPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        Long id = signedPayment.getId();

        defaultSignedPaymentShouldBeFound("id.equals=" + id);
        defaultSignedPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultSignedPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSignedPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultSignedPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSignedPaymentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionNumber equals to DEFAULT_TRANSACTION_NUMBER
        defaultSignedPaymentShouldBeFound("transactionNumber.equals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the signedPaymentList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultSignedPaymentShouldNotBeFound("transactionNumber.equals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionNumber not equals to DEFAULT_TRANSACTION_NUMBER
        defaultSignedPaymentShouldNotBeFound("transactionNumber.notEquals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the signedPaymentList where transactionNumber not equals to UPDATED_TRANSACTION_NUMBER
        defaultSignedPaymentShouldBeFound("transactionNumber.notEquals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionNumber in DEFAULT_TRANSACTION_NUMBER or UPDATED_TRANSACTION_NUMBER
        defaultSignedPaymentShouldBeFound("transactionNumber.in=" + DEFAULT_TRANSACTION_NUMBER + "," + UPDATED_TRANSACTION_NUMBER);

        // Get all the signedPaymentList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultSignedPaymentShouldNotBeFound("transactionNumber.in=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionNumber is not null
        defaultSignedPaymentShouldBeFound("transactionNumber.specified=true");

        // Get all the signedPaymentList where transactionNumber is null
        defaultSignedPaymentShouldNotBeFound("transactionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionNumberContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionNumber contains DEFAULT_TRANSACTION_NUMBER
        defaultSignedPaymentShouldBeFound("transactionNumber.contains=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the signedPaymentList where transactionNumber contains UPDATED_TRANSACTION_NUMBER
        defaultSignedPaymentShouldNotBeFound("transactionNumber.contains=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionNumber does not contain DEFAULT_TRANSACTION_NUMBER
        defaultSignedPaymentShouldNotBeFound("transactionNumber.doesNotContain=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the signedPaymentList where transactionNumber does not contain UPDATED_TRANSACTION_NUMBER
        defaultSignedPaymentShouldBeFound("transactionNumber.doesNotContain=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultSignedPaymentShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the signedPaymentList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSignedPaymentShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultSignedPaymentShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the signedPaymentList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultSignedPaymentShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultSignedPaymentShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the signedPaymentList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultSignedPaymentShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate is not null
        defaultSignedPaymentShouldBeFound("transactionDate.specified=true");

        // Get all the signedPaymentList where transactionDate is null
        defaultSignedPaymentShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultSignedPaymentShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the signedPaymentList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultSignedPaymentShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultSignedPaymentShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the signedPaymentList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultSignedPaymentShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultSignedPaymentShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the signedPaymentList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultSignedPaymentShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultSignedPaymentShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the signedPaymentList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultSignedPaymentShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionCurrency equals to DEFAULT_TRANSACTION_CURRENCY
        defaultSignedPaymentShouldBeFound("transactionCurrency.equals=" + DEFAULT_TRANSACTION_CURRENCY);

        // Get all the signedPaymentList where transactionCurrency equals to UPDATED_TRANSACTION_CURRENCY
        defaultSignedPaymentShouldNotBeFound("transactionCurrency.equals=" + UPDATED_TRANSACTION_CURRENCY);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionCurrency not equals to DEFAULT_TRANSACTION_CURRENCY
        defaultSignedPaymentShouldNotBeFound("transactionCurrency.notEquals=" + DEFAULT_TRANSACTION_CURRENCY);

        // Get all the signedPaymentList where transactionCurrency not equals to UPDATED_TRANSACTION_CURRENCY
        defaultSignedPaymentShouldBeFound("transactionCurrency.notEquals=" + UPDATED_TRANSACTION_CURRENCY);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionCurrency in DEFAULT_TRANSACTION_CURRENCY or UPDATED_TRANSACTION_CURRENCY
        defaultSignedPaymentShouldBeFound("transactionCurrency.in=" + DEFAULT_TRANSACTION_CURRENCY + "," + UPDATED_TRANSACTION_CURRENCY);

        // Get all the signedPaymentList where transactionCurrency equals to UPDATED_TRANSACTION_CURRENCY
        defaultSignedPaymentShouldNotBeFound("transactionCurrency.in=" + UPDATED_TRANSACTION_CURRENCY);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionCurrency is not null
        defaultSignedPaymentShouldBeFound("transactionCurrency.specified=true");

        // Get all the signedPaymentList where transactionCurrency is null
        defaultSignedPaymentShouldNotBeFound("transactionCurrency.specified=false");
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount equals to DEFAULT_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldBeFound("transactionAmount.equals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the signedPaymentList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldNotBeFound("transactionAmount.equals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount not equals to DEFAULT_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldNotBeFound("transactionAmount.notEquals=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the signedPaymentList where transactionAmount not equals to UPDATED_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldBeFound("transactionAmount.notEquals=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsInShouldWork() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount in DEFAULT_TRANSACTION_AMOUNT or UPDATED_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldBeFound("transactionAmount.in=" + DEFAULT_TRANSACTION_AMOUNT + "," + UPDATED_TRANSACTION_AMOUNT);

        // Get all the signedPaymentList where transactionAmount equals to UPDATED_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldNotBeFound("transactionAmount.in=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount is not null
        defaultSignedPaymentShouldBeFound("transactionAmount.specified=true");

        // Get all the signedPaymentList where transactionAmount is null
        defaultSignedPaymentShouldNotBeFound("transactionAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount is greater than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldBeFound("transactionAmount.greaterThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the signedPaymentList where transactionAmount is greater than or equal to UPDATED_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldNotBeFound("transactionAmount.greaterThanOrEqual=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount is less than or equal to DEFAULT_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldBeFound("transactionAmount.lessThanOrEqual=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the signedPaymentList where transactionAmount is less than or equal to SMALLER_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldNotBeFound("transactionAmount.lessThanOrEqual=" + SMALLER_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount is less than DEFAULT_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldNotBeFound("transactionAmount.lessThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the signedPaymentList where transactionAmount is less than UPDATED_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldBeFound("transactionAmount.lessThan=" + UPDATED_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByTransactionAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where transactionAmount is greater than DEFAULT_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldNotBeFound("transactionAmount.greaterThan=" + DEFAULT_TRANSACTION_AMOUNT);

        // Get all the signedPaymentList where transactionAmount is greater than SMALLER_TRANSACTION_AMOUNT
        defaultSignedPaymentShouldBeFound("transactionAmount.greaterThan=" + SMALLER_TRANSACTION_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where dealerName equals to DEFAULT_DEALER_NAME
        defaultSignedPaymentShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the signedPaymentList where dealerName equals to UPDATED_DEALER_NAME
        defaultSignedPaymentShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultSignedPaymentShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the signedPaymentList where dealerName not equals to UPDATED_DEALER_NAME
        defaultSignedPaymentShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultSignedPaymentShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the signedPaymentList where dealerName equals to UPDATED_DEALER_NAME
        defaultSignedPaymentShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where dealerName is not null
        defaultSignedPaymentShouldBeFound("dealerName.specified=true");

        // Get all the signedPaymentList where dealerName is null
        defaultSignedPaymentShouldNotBeFound("dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where dealerName contains DEFAULT_DEALER_NAME
        defaultSignedPaymentShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the signedPaymentList where dealerName contains UPDATED_DEALER_NAME
        defaultSignedPaymentShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultSignedPaymentShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the signedPaymentList where dealerName does not contain UPDATED_DEALER_NAME
        defaultSignedPaymentShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the signedPaymentList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the signedPaymentList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the signedPaymentList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where fileUploadToken is not null
        defaultSignedPaymentShouldBeFound("fileUploadToken.specified=true");

        // Get all the signedPaymentList where fileUploadToken is null
        defaultSignedPaymentShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the signedPaymentList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the signedPaymentList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultSignedPaymentShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultSignedPaymentShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the signedPaymentList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultSignedPaymentShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultSignedPaymentShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the signedPaymentList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultSignedPaymentShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultSignedPaymentShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the signedPaymentList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultSignedPaymentShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where compilationToken is not null
        defaultSignedPaymentShouldBeFound("compilationToken.specified=true");

        // Get all the signedPaymentList where compilationToken is null
        defaultSignedPaymentShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultSignedPaymentShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the signedPaymentList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultSignedPaymentShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        // Get all the signedPaymentList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultSignedPaymentShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the signedPaymentList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultSignedPaymentShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);
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
        signedPayment.addPaymentLabel(paymentLabel);
        signedPaymentRepository.saveAndFlush(signedPayment);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the signedPaymentList where paymentLabel equals to paymentLabelId
        defaultSignedPaymentShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the signedPaymentList where paymentLabel equals to (paymentLabelId + 1)
        defaultSignedPaymentShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByPaymentCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);
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
        signedPayment.setPaymentCategory(paymentCategory);
        signedPaymentRepository.saveAndFlush(signedPayment);
        Long paymentCategoryId = paymentCategory.getId();

        // Get all the signedPaymentList where paymentCategory equals to paymentCategoryId
        defaultSignedPaymentShouldBeFound("paymentCategoryId.equals=" + paymentCategoryId);

        // Get all the signedPaymentList where paymentCategory equals to (paymentCategoryId + 1)
        defaultSignedPaymentShouldNotBeFound("paymentCategoryId.equals=" + (paymentCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllSignedPaymentsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);
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
        signedPayment.addPlaceholder(placeholder);
        signedPaymentRepository.saveAndFlush(signedPayment);
        Long placeholderId = placeholder.getId();

        // Get all the signedPaymentList where placeholder equals to placeholderId
        defaultSignedPaymentShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the signedPaymentList where placeholder equals to (placeholderId + 1)
        defaultSignedPaymentShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllSignedPaymentsBySignedPaymentGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);
        SignedPayment signedPaymentGroup;
        if (TestUtil.findAll(em, SignedPayment.class).isEmpty()) {
            signedPaymentGroup = SignedPaymentResourceIT.createEntity(em);
            em.persist(signedPaymentGroup);
            em.flush();
        } else {
            signedPaymentGroup = TestUtil.findAll(em, SignedPayment.class).get(0);
        }
        em.persist(signedPaymentGroup);
        em.flush();
        signedPayment.setSignedPaymentGroup(signedPaymentGroup);
        signedPaymentRepository.saveAndFlush(signedPayment);
        Long signedPaymentGroupId = signedPaymentGroup.getId();

        // Get all the signedPaymentList where signedPaymentGroup equals to signedPaymentGroupId
        defaultSignedPaymentShouldBeFound("signedPaymentGroupId.equals=" + signedPaymentGroupId);

        // Get all the signedPaymentList where signedPaymentGroup equals to (signedPaymentGroupId + 1)
        defaultSignedPaymentShouldNotBeFound("signedPaymentGroupId.equals=" + (signedPaymentGroupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSignedPaymentShouldBeFound(String filter) throws Exception {
        restSignedPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signedPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionCurrency").value(hasItem(DEFAULT_TRANSACTION_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restSignedPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSignedPaymentShouldNotBeFound(String filter) throws Exception {
        restSignedPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSignedPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSignedPayment() throws Exception {
        // Get the signedPayment
        restSignedPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSignedPayment() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();

        // Update the signedPayment
        SignedPayment updatedSignedPayment = signedPaymentRepository.findById(signedPayment.getId()).get();
        // Disconnect from session so that the updates on updatedSignedPayment are not directly saved in db
        em.detach(updatedSignedPayment);
        updatedSignedPayment
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionCurrency(UPDATED_TRANSACTION_CURRENCY)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .dealerName(UPDATED_DEALER_NAME)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(updatedSignedPayment);

        restSignedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signedPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);
        SignedPayment testSignedPayment = signedPaymentList.get(signedPaymentList.size() - 1);
        assertThat(testSignedPayment.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testSignedPayment.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSignedPayment.getTransactionCurrency()).isEqualTo(UPDATED_TRANSACTION_CURRENCY);
        assertThat(testSignedPayment.getTransactionAmount()).isEqualTo(UPDATED_TRANSACTION_AMOUNT);
        assertThat(testSignedPayment.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testSignedPayment.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testSignedPayment.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository).save(testSignedPayment);
    }

    @Test
    @Transactional
    void putNonExistingSignedPayment() throws Exception {
        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();
        signedPayment.setId(count.incrementAndGet());

        // Create the SignedPayment
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, signedPaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(0)).save(signedPayment);
    }

    @Test
    @Transactional
    void putWithIdMismatchSignedPayment() throws Exception {
        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();
        signedPayment.setId(count.incrementAndGet());

        // Create the SignedPayment
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(0)).save(signedPayment);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSignedPayment() throws Exception {
        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();
        signedPayment.setId(count.incrementAndGet());

        // Create the SignedPayment
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignedPaymentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(0)).save(signedPayment);
    }

    @Test
    @Transactional
    void partialUpdateSignedPaymentWithPatch() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();

        // Update the signedPayment using partial update
        SignedPayment partialUpdatedSignedPayment = new SignedPayment();
        partialUpdatedSignedPayment.setId(signedPayment.getId());

        partialUpdatedSignedPayment.transactionAmount(UPDATED_TRANSACTION_AMOUNT).dealerName(UPDATED_DEALER_NAME);

        restSignedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignedPayment))
            )
            .andExpect(status().isOk());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);
        SignedPayment testSignedPayment = signedPaymentList.get(signedPaymentList.size() - 1);
        assertThat(testSignedPayment.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testSignedPayment.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSignedPayment.getTransactionCurrency()).isEqualTo(DEFAULT_TRANSACTION_CURRENCY);
        assertThat(testSignedPayment.getTransactionAmount()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT);
        assertThat(testSignedPayment.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testSignedPayment.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testSignedPayment.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateSignedPaymentWithPatch() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();

        // Update the signedPayment using partial update
        SignedPayment partialUpdatedSignedPayment = new SignedPayment();
        partialUpdatedSignedPayment.setId(signedPayment.getId());

        partialUpdatedSignedPayment
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .transactionCurrency(UPDATED_TRANSACTION_CURRENCY)
            .transactionAmount(UPDATED_TRANSACTION_AMOUNT)
            .dealerName(UPDATED_DEALER_NAME)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restSignedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSignedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSignedPayment))
            )
            .andExpect(status().isOk());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);
        SignedPayment testSignedPayment = signedPaymentList.get(signedPaymentList.size() - 1);
        assertThat(testSignedPayment.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testSignedPayment.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSignedPayment.getTransactionCurrency()).isEqualTo(UPDATED_TRANSACTION_CURRENCY);
        assertThat(testSignedPayment.getTransactionAmount()).isEqualByComparingTo(UPDATED_TRANSACTION_AMOUNT);
        assertThat(testSignedPayment.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testSignedPayment.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testSignedPayment.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingSignedPayment() throws Exception {
        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();
        signedPayment.setId(count.incrementAndGet());

        // Create the SignedPayment
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSignedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, signedPaymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(0)).save(signedPayment);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSignedPayment() throws Exception {
        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();
        signedPayment.setId(count.incrementAndGet());

        // Create the SignedPayment
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(0)).save(signedPayment);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSignedPayment() throws Exception {
        int databaseSizeBeforeUpdate = signedPaymentRepository.findAll().size();
        signedPayment.setId(count.incrementAndGet());

        // Create the SignedPayment
        SignedPaymentDTO signedPaymentDTO = signedPaymentMapper.toDto(signedPayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSignedPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(signedPaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SignedPayment in the database
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(0)).save(signedPayment);
    }

    @Test
    @Transactional
    void deleteSignedPayment() throws Exception {
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);

        int databaseSizeBeforeDelete = signedPaymentRepository.findAll().size();

        // Delete the signedPayment
        restSignedPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, signedPayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SignedPayment> signedPaymentList = signedPaymentRepository.findAll();
        assertThat(signedPaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SignedPayment in Elasticsearch
        verify(mockSignedPaymentSearchRepository, times(1)).deleteById(signedPayment.getId());
    }

    @Test
    @Transactional
    void searchSignedPayment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        signedPaymentRepository.saveAndFlush(signedPayment);
        when(mockSignedPaymentSearchRepository.search("id:" + signedPayment.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(signedPayment), PageRequest.of(0, 1), 1));

        // Search the signedPayment
        restSignedPaymentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + signedPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signedPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionCurrency").value(hasItem(DEFAULT_TRANSACTION_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].transactionAmount").value(hasItem(sameNumber(DEFAULT_TRANSACTION_AMOUNT))))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
