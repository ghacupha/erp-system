package io.github.erp.web.rest;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Invoice;
import io.github.erp.domain.Payment;
import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.repository.InvoiceRepository;
import io.github.erp.repository.search.InvoiceSearchRepository;
import io.github.erp.service.criteria.InvoiceCriteria;
import io.github.erp.service.dto.InvoiceDTO;
import io.github.erp.service.mapper.InvoiceMapper;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INVOICE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_INVOICE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVOICE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INVOICE_AMOUNT = new BigDecimal(1 - 1);

    private static final CurrencyTypes DEFAULT_CURRENCY = CurrencyTypes.KES;
    private static final CurrencyTypes UPDATED_CURRENCY = CurrencyTypes.USD;

    private static final Double DEFAULT_CONVERSION_RATE = 1.00D;
    private static final Double UPDATED_CONVERSION_RATE = 2D;
    private static final Double SMALLER_CONVERSION_RATE = 1.00D - 1D;

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/invoices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InvoiceSearchRepositoryMockConfiguration
     */
    @Autowired
    private InvoiceSearchRepository mockInvoiceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceNumber(DEFAULT_INVOICE_NUMBER)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .invoiceAmount(DEFAULT_INVOICE_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .conversionRate(DEFAULT_CONVERSION_RATE);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .invoiceAmount(UPDATED_INVOICE_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .conversionRate(UPDATED_CONVERSION_RATE);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoice.getInvoiceAmount()).isEqualByComparingTo(DEFAULT_INVOICE_AMOUNT);
        assertThat(testInvoice.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testInvoice.getConversionRate()).isEqualTo(DEFAULT_CONVERSION_RATE);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).save(testInvoice);
    }

    @Test
    @Transactional
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceNumber(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setCurrency(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConversionRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setConversionRate(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(sameNumber(DEFAULT_INVOICE_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].conversionRate").value(hasItem(DEFAULT_CONVERSION_RATE.doubleValue())));
    }

    @Test
    @Transactional
    void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.invoiceAmount").value(sameNumber(DEFAULT_INVOICE_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.conversionRate").value(DEFAULT_CONVERSION_RATE.doubleValue()));
    }

    @Test
    @Transactional
    void getInvoicesByIdFiltering() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        Long id = invoice.getId();

        defaultInvoiceShouldBeFound("id.equals=" + id);
        defaultInvoiceShouldNotBeFound("id.notEquals=" + id);

        defaultInvoiceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.greaterThan=" + id);

        defaultInvoiceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInvoiceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber equals to DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.equals=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.equals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber not equals to DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.notEquals=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber not equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.notEquals=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber in DEFAULT_INVOICE_NUMBER or UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.in=" + DEFAULT_INVOICE_NUMBER + "," + UPDATED_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber equals to UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.in=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber is not null
        defaultInvoiceShouldBeFound("invoiceNumber.specified=true");

        // Get all the invoiceList where invoiceNumber is null
        defaultInvoiceShouldNotBeFound("invoiceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceNumberContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber contains DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.contains=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber contains UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.contains=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceNumber does not contain DEFAULT_INVOICE_NUMBER
        defaultInvoiceShouldNotBeFound("invoiceNumber.doesNotContain=" + DEFAULT_INVOICE_NUMBER);

        // Get all the invoiceList where invoiceNumber does not contain UPDATED_INVOICE_NUMBER
        defaultInvoiceShouldBeFound("invoiceNumber.doesNotContain=" + UPDATED_INVOICE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate equals to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.equals=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.equals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate not equals to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.notEquals=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate not equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.notEquals=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate in DEFAULT_INVOICE_DATE or UPDATED_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.in=" + DEFAULT_INVOICE_DATE + "," + UPDATED_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate equals to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.in=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate is not null
        defaultInvoiceShouldBeFound("invoiceDate.specified=true");

        // Get all the invoiceList where invoiceDate is null
        defaultInvoiceShouldNotBeFound("invoiceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate is greater than or equal to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.greaterThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate is greater than or equal to UPDATED_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.greaterThanOrEqual=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate is less than or equal to DEFAULT_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.lessThanOrEqual=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate is less than or equal to SMALLER_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.lessThanOrEqual=" + SMALLER_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate is less than DEFAULT_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.lessThan=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate is less than UPDATED_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.lessThan=" + UPDATED_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceDate is greater than DEFAULT_INVOICE_DATE
        defaultInvoiceShouldNotBeFound("invoiceDate.greaterThan=" + DEFAULT_INVOICE_DATE);

        // Get all the invoiceList where invoiceDate is greater than SMALLER_INVOICE_DATE
        defaultInvoiceShouldBeFound("invoiceDate.greaterThan=" + SMALLER_INVOICE_DATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount equals to DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.equals=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount equals to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.equals=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount not equals to DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.notEquals=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount not equals to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.notEquals=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount in DEFAULT_INVOICE_AMOUNT or UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.in=" + DEFAULT_INVOICE_AMOUNT + "," + UPDATED_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount equals to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.in=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount is not null
        defaultInvoiceShouldBeFound("invoiceAmount.specified=true");

        // Get all the invoiceList where invoiceAmount is null
        defaultInvoiceShouldNotBeFound("invoiceAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount is greater than or equal to DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.greaterThanOrEqual=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount is greater than or equal to UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.greaterThanOrEqual=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount is less than or equal to DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.lessThanOrEqual=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount is less than or equal to SMALLER_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.lessThanOrEqual=" + SMALLER_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount is less than DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.lessThan=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount is less than UPDATED_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.lessThan=" + UPDATED_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByInvoiceAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceAmount is greater than DEFAULT_INVOICE_AMOUNT
        defaultInvoiceShouldNotBeFound("invoiceAmount.greaterThan=" + DEFAULT_INVOICE_AMOUNT);

        // Get all the invoiceList where invoiceAmount is greater than SMALLER_INVOICE_AMOUNT
        defaultInvoiceShouldBeFound("invoiceAmount.greaterThan=" + SMALLER_INVOICE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency equals to DEFAULT_CURRENCY
        defaultInvoiceShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the invoiceList where currency equals to UPDATED_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency not equals to DEFAULT_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the invoiceList where currency not equals to UPDATED_CURRENCY
        defaultInvoiceShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultInvoiceShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the invoiceList where currency equals to UPDATED_CURRENCY
        defaultInvoiceShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllInvoicesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where currency is not null
        defaultInvoiceShouldBeFound("currency.specified=true");

        // Get all the invoiceList where currency is null
        defaultInvoiceShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate equals to DEFAULT_CONVERSION_RATE
        defaultInvoiceShouldBeFound("conversionRate.equals=" + DEFAULT_CONVERSION_RATE);

        // Get all the invoiceList where conversionRate equals to UPDATED_CONVERSION_RATE
        defaultInvoiceShouldNotBeFound("conversionRate.equals=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate not equals to DEFAULT_CONVERSION_RATE
        defaultInvoiceShouldNotBeFound("conversionRate.notEquals=" + DEFAULT_CONVERSION_RATE);

        // Get all the invoiceList where conversionRate not equals to UPDATED_CONVERSION_RATE
        defaultInvoiceShouldBeFound("conversionRate.notEquals=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate in DEFAULT_CONVERSION_RATE or UPDATED_CONVERSION_RATE
        defaultInvoiceShouldBeFound("conversionRate.in=" + DEFAULT_CONVERSION_RATE + "," + UPDATED_CONVERSION_RATE);

        // Get all the invoiceList where conversionRate equals to UPDATED_CONVERSION_RATE
        defaultInvoiceShouldNotBeFound("conversionRate.in=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate is not null
        defaultInvoiceShouldBeFound("conversionRate.specified=true");

        // Get all the invoiceList where conversionRate is null
        defaultInvoiceShouldNotBeFound("conversionRate.specified=false");
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate is greater than or equal to DEFAULT_CONVERSION_RATE
        defaultInvoiceShouldBeFound("conversionRate.greaterThanOrEqual=" + DEFAULT_CONVERSION_RATE);

        // Get all the invoiceList where conversionRate is greater than or equal to UPDATED_CONVERSION_RATE
        defaultInvoiceShouldNotBeFound("conversionRate.greaterThanOrEqual=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate is less than or equal to DEFAULT_CONVERSION_RATE
        defaultInvoiceShouldBeFound("conversionRate.lessThanOrEqual=" + DEFAULT_CONVERSION_RATE);

        // Get all the invoiceList where conversionRate is less than or equal to SMALLER_CONVERSION_RATE
        defaultInvoiceShouldNotBeFound("conversionRate.lessThanOrEqual=" + SMALLER_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate is less than DEFAULT_CONVERSION_RATE
        defaultInvoiceShouldNotBeFound("conversionRate.lessThan=" + DEFAULT_CONVERSION_RATE);

        // Get all the invoiceList where conversionRate is less than UPDATED_CONVERSION_RATE
        defaultInvoiceShouldBeFound("conversionRate.lessThan=" + UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByConversionRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where conversionRate is greater than DEFAULT_CONVERSION_RATE
        defaultInvoiceShouldNotBeFound("conversionRate.greaterThan=" + DEFAULT_CONVERSION_RATE);

        // Get all the invoiceList where conversionRate is greater than SMALLER_CONVERSION_RATE
        defaultInvoiceShouldBeFound("conversionRate.greaterThan=" + SMALLER_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void getAllInvoicesByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        invoice.setPayment(payment);
        invoiceRepository.saveAndFlush(invoice);
        Long paymentId = payment.getId();

        // Get all the invoiceList where payment equals to paymentId
        defaultInvoiceShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the invoiceList where payment equals to (paymentId + 1)
        defaultInvoiceShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    @Test
    @Transactional
    void getAllInvoicesByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        Dealer dealer = DealerResourceIT.createEntity(em);
        em.persist(dealer);
        em.flush();
        invoice.setDealer(dealer);
        invoiceRepository.saveAndFlush(invoice);
        Long dealerId = dealer.getId();

        // Get all the invoiceList where dealer equals to dealerId
        defaultInvoiceShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the invoiceList where dealer equals to (dealerId + 1)
        defaultInvoiceShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(sameNumber(DEFAULT_INVOICE_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].conversionRate").value(hasItem(DEFAULT_CONVERSION_RATE.doubleValue())));

        // Check, that the count call also returns 1
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .invoiceAmount(UPDATED_INVOICE_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .conversionRate(UPDATED_CONVERSION_RATE);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoice.getInvoiceAmount()).isEqualTo(UPDATED_INVOICE_AMOUNT);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getConversionRate()).isEqualTo(UPDATED_CONVERSION_RATE);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository).save(testInvoice);
    }

    @Test
    @Transactional
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice.invoiceAmount(UPDATED_INVOICE_AMOUNT).currency(UPDATED_CURRENCY).conversionRate(UPDATED_CONVERSION_RATE);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoice.getInvoiceAmount()).isEqualByComparingTo(UPDATED_INVOICE_AMOUNT);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getConversionRate()).isEqualTo(UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .invoiceNumber(UPDATED_INVOICE_NUMBER)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .invoiceAmount(UPDATED_INVOICE_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .conversionRate(UPDATED_CONVERSION_RATE);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoice.getInvoiceAmount()).isEqualByComparingTo(UPDATED_INVOICE_AMOUNT);
        assertThat(testInvoice.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInvoice.getConversionRate()).isEqualTo(UPDATED_CONVERSION_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(count.incrementAndGet());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(0)).save(invoice);
    }

    @Test
    @Transactional
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Invoice in Elasticsearch
        verify(mockInvoiceSearchRepository, times(1)).deleteById(invoice.getId());
    }

    @Test
    @Transactional
    void searchInvoice() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        when(mockInvoiceSearchRepository.search(queryStringQuery("id:" + invoice.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(invoice), PageRequest.of(0, 1), 1));

        // Search the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER)))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].invoiceAmount").value(hasItem(sameNumber(DEFAULT_INVOICE_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].conversionRate").value(hasItem(DEFAULT_CONVERSION_RATE.doubleValue())));
    }
}
