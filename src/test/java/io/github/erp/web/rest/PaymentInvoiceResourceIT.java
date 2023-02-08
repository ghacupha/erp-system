package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
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
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.DeliveryNote;
import io.github.erp.domain.JobSheet;
import io.github.erp.domain.PaymentInvoice;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.PurchaseOrder;
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.repository.PaymentInvoiceRepository;
import io.github.erp.repository.search.PaymentInvoiceSearchRepository;
import io.github.erp.service.PaymentInvoiceService;
import io.github.erp.service.criteria.PaymentInvoiceCriteria;
import io.github.erp.service.dto.PaymentInvoiceDTO;
import io.github.erp.service.mapper.PaymentInvoiceMapper;
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
 * Integration tests for the {@link PaymentInvoiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
class PaymentInvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INVOICE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_INVOICE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVOICE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INVOICE_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/payment-invoices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentInvoiceRepository paymentInvoiceRepository;

    @Mock
    private PaymentInvoiceRepository paymentInvoiceRepositoryMock;

    @Autowired
    private PaymentInvoiceMapper paymentInvoiceMapper;

    @Mock
    private PaymentInvoiceService paymentInvoiceServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PaymentInvoiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentInvoiceSearchRepository mockPaymentInvoiceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentInvoiceMockMvc;

    private PaymentInvoice paymentInvoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentInvoice createEntity(EntityManager em) {
        PaymentInvoice paymentInvoice = new PaymentInvoice()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .invoiceAmount(DEFAULT_INVOICE_AMOUNT)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        paymentInvoice.setSettlementCurrency(settlementCurrency);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        paymentInvoice.setBiller(dealer);
        return paymentInvoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentInvoice createUpdatedEntity(EntityManager em) {
        PaymentInvoice paymentInvoice = new PaymentInvoice()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .invoiceAmount(UPDATED_INVOICE_AMOUNT)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createUpdatedEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        paymentInvoice.setSettlementCurrency(settlementCurrency);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        paymentInvoice.setBiller(dealer);
        return paymentInvoice;
    }

    @BeforeEach
    public void initTest() {
        paymentInvoice = createEntity(em);
    }

    @Test
    @Transactional
    void createPaymentInvoice() throws Exception {
        int databaseSizeBeforeCreate = paymentInvoiceRepository.findAll().size();
        // Create the PaymentInvoice
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);
        restPaymentInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentInvoice testPaymentInvoice = paymentInvoiceList.get(paymentInvoiceList.size() - 1);
        assertThat(testPaymentInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testPaymentInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testPaymentInvoice.getInvoiceAmount()).isEqualByComparingTo(DEFAULT_INVOICE_AMOUNT);
        assertThat(testPaymentInvoice.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentInvoice.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
        assertThat(testPaymentInvoice.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(1)).save(testPaymentInvoice);
    }

    @Test
    @Transactional
    void createPaymentInvoiceWithExistingId() throws Exception {
        // Create the PaymentInvoice with an existing ID
        paymentInvoice.setId(1L);
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        int databaseSizeBeforeCreate = paymentInvoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(0)).save(paymentInvoice);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentInvoiceRepository.findAll().size();
        // set the field null
        paymentInvoice.setInvoiceNumber(null);

        // Create the PaymentInvoice, which fails.
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        restPaymentInvoiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentInvoices() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList
        restPaymentInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(sameNumber(DEFAULT_INVOICE_AMOUNT))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentInvoicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(paymentInvoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentInvoiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentInvoicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paymentInvoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaymentInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paymentInvoiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPaymentInvoice() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get the paymentInvoice
        restPaymentInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentInvoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.invoiceAmount").value(sameNumber(DEFAULT_INVOICE_AMOUNT)))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    void getPaymentInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        Long id = paymentInvoice.getId();

        defaultPaymentInvoiceShouldBeFound("id.equals=" + id);
        defaultPaymentInvoiceShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentInvoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentInvoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentInvoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentInvoiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceNumber equals to DEFAULT_INVOICE_NUMBER
        defaultPaymentInvoiceShouldBeFound("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER);

        // Get all the paymentInvoiceList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultPaymentInvoiceShouldNotBeFound("invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceNumber not equals to DEFAULT_INVOICE_NUMBER
        defaultPaymentInvoiceShouldNotBeFound("invoiceNumber.notEquals=" + DEFAULT_INVOICE_NUMBER);

        // Get all the paymentInvoiceList where invoiceNumber not equals to UPDATED_INVOICE_NUMBER
        defaultPaymentInvoiceShouldBeFound("invoiceNumber.notEquals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceNumber in DEFAULT_INVOICE_NUMBER or UPDATED_INVOICE_NUMBER
        defaultPaymentInvoiceShouldBeFound("invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER);

        // Get all the paymentInvoiceList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultPaymentInvoiceShouldNotBeFound("invoiceNumber.in=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceNumber is not null
        defaultPaymentInvoiceShouldBeFound("invoiceNumber.specified=true");

        // Get all the paymentInvoiceList where invoiceNumber is null
        defaultPaymentInvoiceShouldNotBeFound("invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceNumber contains DEFAULT_INVOICE_NUMBER
        defaultPaymentInvoiceShouldBeFound("invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER);

        // Get all the paymentInvoiceList where invoiceNumber contains UPDATED_INVOICE_NUMBER
        defaultPaymentInvoiceShouldNotBeFound("invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceNumber does not contain DEFAULT_INVOICE_NUMBER
        defaultPaymentInvoiceShouldNotBeFound("invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER);

        // Get all the paymentInvoiceList where invoiceNumber does not contain UPDATED_INVOICE_NUMBER
        defaultPaymentInvoiceShouldBeFound("invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate equals to DEFAULT_INVOICE_DATE
        defaultPaymentInvoiceShouldBeFound("invoiceDate.equals=" + DEFAULT_INVOICE_DATE);

        // Get all the paymentInvoiceList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate not equals to DEFAULT_INVOICE_DATE
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.notEquals=" + DEFAULT_INVOICE_DATE);

        // Get all the paymentInvoiceList where invoiceDate not equals to UPDATED_INVOICE_DATE
        defaultPaymentInvoiceShouldBeFound("invoiceDate.notEquals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate in DEFAULT_INVOICE_DATE or UPDATED_INVOICE_DATE
        defaultPaymentInvoiceShouldBeFound("invoiceDate.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE);

        // Get all the paymentInvoiceList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.in=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate is not null
        defaultPaymentInvoiceShouldBeFound("invoiceDate.specified=true");

        // Get all the paymentInvoiceList where invoiceDate is null
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate is greater than or equal to DEFAULT_INVOICE_DATE
        defaultPaymentInvoiceShouldBeFound("invoiceDate.greaterThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the paymentInvoiceList where invoiceDate is greater than or equal to UPDATED_INVOICE_DATE
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.greaterThanOrEqual=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate is less than or equal to DEFAULT_INVOICE_DATE
        defaultPaymentInvoiceShouldBeFound("invoiceDate.lessThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the paymentInvoiceList where invoiceDate is less than or equal to SMALLER_INVOICE_DATE
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.lessThanOrEqual=" + SMALLER_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate is less than DEFAULT_INVOICE_DATE
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.lessThan=" + DEFAULT_INVOICE_DATE);

        // Get all the paymentInvoiceList where invoiceDate is less than UPDATED_INVOICE_DATE
        defaultPaymentInvoiceShouldBeFound("invoiceDate.lessThan=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceDate is greater than DEFAULT_INVOICE_DATE
        defaultPaymentInvoiceShouldNotBeFound("invoiceDate.greaterThan=" + DEFAULT_INVOICE_DATE);

        // Get all the paymentInvoiceList where invoiceDate is greater than SMALLER_INVOICE_DATE
        defaultPaymentInvoiceShouldBeFound("invoiceDate.greaterThan=" + SMALLER_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount equals to DEFAULT_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.equals=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the paymentInvoiceList where invoiceAmount equals to UPDATED_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.equals=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount not equals to DEFAULT_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.notEquals=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the paymentInvoiceList where invoiceAmount not equals to UPDATED_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.notEquals=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount in DEFAULT_INVOICE_AMOUNT or UPDATED_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.in=" + DEFAULT_INVOICE_AMOUNT + "," + UPDATED_INVOICE_AMOUNT);

        // Get all the paymentInvoiceList where invoiceAmount equals to UPDATED_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.in=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount is not null
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.specified=true");

        // Get all the paymentInvoiceList where invoiceAmount is null
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount is greater than or equal to DEFAULT_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.greaterThanOrEqual=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the paymentInvoiceList where invoiceAmount is greater than or equal to UPDATED_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.greaterThanOrEqual=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount is less than or equal to DEFAULT_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.lessThanOrEqual=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the paymentInvoiceList where invoiceAmount is less than or equal to SMALLER_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.lessThanOrEqual=" + SMALLER_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount is less than DEFAULT_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.lessThan=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the paymentInvoiceList where invoiceAmount is less than UPDATED_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.lessThan=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByInvoiceAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where invoiceAmount is greater than DEFAULT_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldNotBeFound("invoiceAmount.greaterThan=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the paymentInvoiceList where invoiceAmount is greater than SMALLER_INVOICE_AMOUNT
        defaultPaymentInvoiceShouldBeFound("invoiceAmount.greaterThan=" + SMALLER_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentInvoiceList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentInvoiceList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the paymentInvoiceList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where fileUploadToken is not null
        defaultPaymentInvoiceShouldBeFound("fileUploadToken.specified=true");

        // Get all the paymentInvoiceList where fileUploadToken is null
        defaultPaymentInvoiceShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentInvoiceList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentInvoiceList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentInvoiceShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentInvoiceList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentInvoiceList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the paymentInvoiceList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where compilationToken is not null
        defaultPaymentInvoiceShouldBeFound("compilationToken.specified=true");

        // Get all the paymentInvoiceList where compilationToken is null
        defaultPaymentInvoiceShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentInvoiceList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        // Get all the paymentInvoiceList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentInvoiceList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultPaymentInvoiceShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByPurchaseOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrder);
        em.flush();
        paymentInvoice.addPurchaseOrder(purchaseOrder);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the paymentInvoiceList where purchaseOrder equals to purchaseOrderId
        defaultPaymentInvoiceShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the paymentInvoiceList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultPaymentInvoiceShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
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
        paymentInvoice.addPlaceholder(placeholder);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long placeholderId = placeholder.getId();

        // Get all the paymentInvoiceList where placeholder equals to placeholderId
        defaultPaymentInvoiceShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the paymentInvoiceList where placeholder equals to (placeholderId + 1)
        defaultPaymentInvoiceShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
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
        paymentInvoice.addPaymentLabel(paymentLabel);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the paymentInvoiceList where paymentLabel equals to paymentLabelId
        defaultPaymentInvoiceShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the paymentInvoiceList where paymentLabel equals to (paymentLabelId + 1)
        defaultPaymentInvoiceShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(settlementCurrency);
        em.flush();
        paymentInvoice.setSettlementCurrency(settlementCurrency);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the paymentInvoiceList where settlementCurrency equals to settlementCurrencyId
        defaultPaymentInvoiceShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the paymentInvoiceList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultPaymentInvoiceShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByBillerIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Dealer biller;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            biller = DealerResourceIT.createEntity(em);
            em.persist(biller);
            em.flush();
        } else {
            biller = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(biller);
        em.flush();
        paymentInvoice.setBiller(biller);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long billerId = biller.getId();

        // Get all the paymentInvoiceList where biller equals to billerId
        defaultPaymentInvoiceShouldBeFound("billerId.equals=" + billerId);

        // Get all the paymentInvoiceList where biller equals to (billerId + 1)
        defaultPaymentInvoiceShouldNotBeFound("billerId.equals=" + (billerId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByDeliveryNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        DeliveryNote deliveryNote;
        if (TestUtil.findAll(em, DeliveryNote.class).isEmpty()) {
            deliveryNote = DeliveryNoteResourceIT.createEntity(em);
            em.persist(deliveryNote);
            em.flush();
        } else {
            deliveryNote = TestUtil.findAll(em, DeliveryNote.class).get(0);
        }
        em.persist(deliveryNote);
        em.flush();
        paymentInvoice.addDeliveryNote(deliveryNote);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long deliveryNoteId = deliveryNote.getId();

        // Get all the paymentInvoiceList where deliveryNote equals to deliveryNoteId
        defaultPaymentInvoiceShouldBeFound("deliveryNoteId.equals=" + deliveryNoteId);

        // Get all the paymentInvoiceList where deliveryNote equals to (deliveryNoteId + 1)
        defaultPaymentInvoiceShouldNotBeFound("deliveryNoteId.equals=" + (deliveryNoteId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByJobSheetIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        JobSheet jobSheet;
        if (TestUtil.findAll(em, JobSheet.class).isEmpty()) {
            jobSheet = JobSheetResourceIT.createEntity(em);
            em.persist(jobSheet);
            em.flush();
        } else {
            jobSheet = TestUtil.findAll(em, JobSheet.class).get(0);
        }
        em.persist(jobSheet);
        em.flush();
        paymentInvoice.addJobSheet(jobSheet);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long jobSheetId = jobSheet.getId();

        // Get all the paymentInvoiceList where jobSheet equals to jobSheetId
        defaultPaymentInvoiceShouldBeFound("jobSheetId.equals=" + jobSheetId);

        // Get all the paymentInvoiceList where jobSheet equals to (jobSheetId + 1)
        defaultPaymentInvoiceShouldNotBeFound("jobSheetId.equals=" + (jobSheetId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentInvoicesByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(businessDocument);
        em.flush();
        paymentInvoice.addBusinessDocument(businessDocument);
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        Long businessDocumentId = businessDocument.getId();

        // Get all the paymentInvoiceList where businessDocument equals to businessDocumentId
        defaultPaymentInvoiceShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the paymentInvoiceList where businessDocument equals to (businessDocumentId + 1)
        defaultPaymentInvoiceShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentInvoiceShouldBeFound(String filter) throws Exception {
        restPaymentInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(sameNumber(DEFAULT_INVOICE_AMOUNT))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));

        // Check, that the count call also returns 1
        restPaymentInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentInvoiceShouldNotBeFound(String filter) throws Exception {
        restPaymentInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaymentInvoice() throws Exception {
        // Get the paymentInvoice
        restPaymentInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaymentInvoice() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();

        // Update the paymentInvoice
        PaymentInvoice updatedPaymentInvoice = paymentInvoiceRepository.findById(paymentInvoice.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentInvoice are not directly saved in db
        em.detach(updatedPaymentInvoice);
        updatedPaymentInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .invoiceAmount(UPDATED_INVOICE_AMOUNT)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS);
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(updatedPaymentInvoice);

        restPaymentInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentInvoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);
        PaymentInvoice testPaymentInvoice = paymentInvoiceList.get(paymentInvoiceList.size() - 1);
        assertThat(testPaymentInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testPaymentInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testPaymentInvoice.getInvoiceAmount()).isEqualTo(UPDATED_INVOICE_AMOUNT);
        assertThat(testPaymentInvoice.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentInvoice.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
        assertThat(testPaymentInvoice.getRemarks()).isEqualTo(UPDATED_REMARKS);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository).save(testPaymentInvoice);
    }

    @Test
    @Transactional
    void putNonExistingPaymentInvoice() throws Exception {
        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();
        paymentInvoice.setId(count.incrementAndGet());

        // Create the PaymentInvoice
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentInvoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(0)).save(paymentInvoice);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentInvoice() throws Exception {
        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();
        paymentInvoice.setId(count.incrementAndGet());

        // Create the PaymentInvoice
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(0)).save(paymentInvoice);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentInvoice() throws Exception {
        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();
        paymentInvoice.setId(count.incrementAndGet());

        // Create the PaymentInvoice
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(0)).save(paymentInvoice);
    }

    @Test
    @Transactional
    void partialUpdatePaymentInvoiceWithPatch() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();

        // Update the paymentInvoice using partial update
        PaymentInvoice partialUpdatedPaymentInvoice = new PaymentInvoice();
        partialUpdatedPaymentInvoice.setId(paymentInvoice.getId());

        partialUpdatedPaymentInvoice.invoiceNumber(UPDATED_INVOICE_NUMBER).invoiceAmount(UPDATED_INVOICE_AMOUNT);

        restPaymentInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentInvoice))
            )
            .andExpect(status().isOk());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);
        PaymentInvoice testPaymentInvoice = paymentInvoiceList.get(paymentInvoiceList.size() - 1);
        assertThat(testPaymentInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testPaymentInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testPaymentInvoice.getInvoiceAmount()).isEqualByComparingTo(UPDATED_INVOICE_AMOUNT);
        assertThat(testPaymentInvoice.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentInvoice.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
        assertThat(testPaymentInvoice.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdatePaymentInvoiceWithPatch() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();

        // Update the paymentInvoice using partial update
        PaymentInvoice partialUpdatedPaymentInvoice = new PaymentInvoice();
        partialUpdatedPaymentInvoice.setId(paymentInvoice.getId());

        partialUpdatedPaymentInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .invoiceAmount(UPDATED_INVOICE_AMOUNT)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS);

        restPaymentInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentInvoice))
            )
            .andExpect(status().isOk());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);
        PaymentInvoice testPaymentInvoice = paymentInvoiceList.get(paymentInvoiceList.size() - 1);
        assertThat(testPaymentInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testPaymentInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testPaymentInvoice.getInvoiceAmount()).isEqualByComparingTo(UPDATED_INVOICE_AMOUNT);
        assertThat(testPaymentInvoice.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentInvoice.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
        assertThat(testPaymentInvoice.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingPaymentInvoice() throws Exception {
        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();
        paymentInvoice.setId(count.incrementAndGet());

        // Create the PaymentInvoice
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentInvoiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(0)).save(paymentInvoice);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentInvoice() throws Exception {
        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();
        paymentInvoice.setId(count.incrementAndGet());

        // Create the PaymentInvoice
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(0)).save(paymentInvoice);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentInvoice() throws Exception {
        int databaseSizeBeforeUpdate = paymentInvoiceRepository.findAll().size();
        paymentInvoice.setId(count.incrementAndGet());

        // Create the PaymentInvoice
        PaymentInvoiceDTO paymentInvoiceDTO = paymentInvoiceMapper.toDto(paymentInvoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentInvoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentInvoice in the database
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(0)).save(paymentInvoice);
    }

    @Test
    @Transactional
    void deletePaymentInvoice() throws Exception {
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);

        int databaseSizeBeforeDelete = paymentInvoiceRepository.findAll().size();

        // Delete the paymentInvoice
        restPaymentInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentInvoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentInvoice> paymentInvoiceList = paymentInvoiceRepository.findAll();
        assertThat(paymentInvoiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PaymentInvoice in Elasticsearch
        verify(mockPaymentInvoiceSearchRepository, times(1)).deleteById(paymentInvoice.getId());
    }

    @Test
    @Transactional
    void searchPaymentInvoice() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        paymentInvoiceRepository.saveAndFlush(paymentInvoice);
        when(mockPaymentInvoiceSearchRepository.search("id:" + paymentInvoice.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paymentInvoice), PageRequest.of(0, 1), 1));

        // Search the paymentInvoice
        restPaymentInvoiceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + paymentInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(sameNumber(DEFAULT_INVOICE_AMOUNT))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }
}
