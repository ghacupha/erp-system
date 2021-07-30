package io.github.erp.web.rest;

import io.github.erp.ErpServiceApp;
import io.github.erp.config.SecurityBeanOverrideConfiguration;
import io.github.erp.domain.PaymentCalculation;
import io.github.erp.repository.PaymentCalculationRepository;
import io.github.erp.repository.search.PaymentCalculationSearchRepository;
import io.github.erp.service.PaymentCalculationService;
import io.github.erp.service.dto.PaymentCalculationDTO;
import io.github.erp.service.mapper.PaymentCalculationMapper;
import io.github.erp.service.dto.PaymentCalculationCriteria;
import io.github.erp.service.PaymentCalculationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaymentCalculationResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ErpServiceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaymentCalculationResourceIT {

    private static final String DEFAULT_PAYMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PAYMENT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PAYMENT_EXPENSE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_EXPENSE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PAYMENT_EXPENSE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_WITHHOLDING_VAT = new BigDecimal(1);
    private static final BigDecimal UPDATED_WITHHOLDING_VAT = new BigDecimal(2);
    private static final BigDecimal SMALLER_WITHHOLDING_VAT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_WITHHOLDING_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_WITHHOLDING_TAX = new BigDecimal(2);
    private static final BigDecimal SMALLER_WITHHOLDING_TAX = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PAYMENT_AMOUNT = new BigDecimal(1 - 1);

    @Autowired
    private PaymentCalculationRepository paymentCalculationRepository;

    @Autowired
    private PaymentCalculationMapper paymentCalculationMapper;

    @Autowired
    private PaymentCalculationService paymentCalculationService;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PaymentCalculationSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentCalculationSearchRepository mockPaymentCalculationSearchRepository;

    @Autowired
    private PaymentCalculationQueryService paymentCalculationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentCalculationMockMvc;

    private PaymentCalculation paymentCalculation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCalculation createEntity(EntityManager em) {
        PaymentCalculation paymentCalculation = new PaymentCalculation()
            .paymentNumber(DEFAULT_PAYMENT_NUMBER)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentCategory(DEFAULT_PAYMENT_CATEGORY)
            .paymentExpense(DEFAULT_PAYMENT_EXPENSE)
            .withholdingVAT(DEFAULT_WITHHOLDING_VAT)
            .withholdingTax(DEFAULT_WITHHOLDING_TAX)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT);
        return paymentCalculation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentCalculation createUpdatedEntity(EntityManager em) {
        PaymentCalculation paymentCalculation = new PaymentCalculation()
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentCategory(UPDATED_PAYMENT_CATEGORY)
            .paymentExpense(UPDATED_PAYMENT_EXPENSE)
            .withholdingVAT(UPDATED_WITHHOLDING_VAT)
            .withholdingTax(UPDATED_WITHHOLDING_TAX)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT);
        return paymentCalculation;
    }

    @BeforeEach
    public void initTest() {
        paymentCalculation = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentCalculation() throws Exception {
        int databaseSizeBeforeCreate = paymentCalculationRepository.findAll().size();
        // Create the PaymentCalculation
        PaymentCalculationDTO paymentCalculationDTO = paymentCalculationMapper.toDto(paymentCalculation);
        restPaymentCalculationMockMvc.perform(post("/api/payment-calculations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCalculationDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentCalculation in the database
        List<PaymentCalculation> paymentCalculationList = paymentCalculationRepository.findAll();
        assertThat(paymentCalculationList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentCalculation testPaymentCalculation = paymentCalculationList.get(paymentCalculationList.size() - 1);
        assertThat(testPaymentCalculation.getPaymentNumber()).isEqualTo(DEFAULT_PAYMENT_NUMBER);
        assertThat(testPaymentCalculation.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPaymentCalculation.getPaymentCategory()).isEqualTo(DEFAULT_PAYMENT_CATEGORY);
        assertThat(testPaymentCalculation.getPaymentExpense()).isEqualTo(DEFAULT_PAYMENT_EXPENSE);
        assertThat(testPaymentCalculation.getWithholdingVAT()).isEqualTo(DEFAULT_WITHHOLDING_VAT);
        assertThat(testPaymentCalculation.getWithholdingTax()).isEqualTo(DEFAULT_WITHHOLDING_TAX);
        assertThat(testPaymentCalculation.getPaymentAmount()).isEqualTo(DEFAULT_PAYMENT_AMOUNT);

        // Validate the PaymentCalculation in Elasticsearch
        verify(mockPaymentCalculationSearchRepository, times(1)).save(testPaymentCalculation);
    }

    @Test
    @Transactional
    public void createPaymentCalculationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentCalculationRepository.findAll().size();

        // Create the PaymentCalculation with an existing ID
        paymentCalculation.setId(1L);
        PaymentCalculationDTO paymentCalculationDTO = paymentCalculationMapper.toDto(paymentCalculation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentCalculationMockMvc.perform(post("/api/payment-calculations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCalculationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentCalculation in the database
        List<PaymentCalculation> paymentCalculationList = paymentCalculationRepository.findAll();
        assertThat(paymentCalculationList).hasSize(databaseSizeBeforeCreate);

        // Validate the PaymentCalculation in Elasticsearch
        verify(mockPaymentCalculationSearchRepository, times(0)).save(paymentCalculation);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculations() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList
        restPaymentCalculationMockMvc.perform(get("/api/payment-calculations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCalculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentCategory").value(hasItem(DEFAULT_PAYMENT_CATEGORY)))
            .andExpect(jsonPath("$.[*].paymentExpense").value(hasItem(DEFAULT_PAYMENT_EXPENSE.intValue())))
            .andExpect(jsonPath("$.[*].withholdingVAT").value(hasItem(DEFAULT_WITHHOLDING_VAT.intValue())))
            .andExpect(jsonPath("$.[*].withholdingTax").value(hasItem(DEFAULT_WITHHOLDING_TAX.intValue())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentCalculation() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get the paymentCalculation
        restPaymentCalculationMockMvc.perform(get("/api/payment-calculations/{id}", paymentCalculation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentCalculation.getId().intValue()))
            .andExpect(jsonPath("$.paymentNumber").value(DEFAULT_PAYMENT_NUMBER))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentCategory").value(DEFAULT_PAYMENT_CATEGORY))
            .andExpect(jsonPath("$.paymentExpense").value(DEFAULT_PAYMENT_EXPENSE.intValue()))
            .andExpect(jsonPath("$.withholdingVAT").value(DEFAULT_WITHHOLDING_VAT.intValue()))
            .andExpect(jsonPath("$.withholdingTax").value(DEFAULT_WITHHOLDING_TAX.intValue()))
            .andExpect(jsonPath("$.paymentAmount").value(DEFAULT_PAYMENT_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getPaymentCalculationsByIdFiltering() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        Long id = paymentCalculation.getId();

        defaultPaymentCalculationShouldBeFound("id.equals=" + id);
        defaultPaymentCalculationShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentCalculationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentCalculationShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentCalculationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentCalculationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentNumber equals to DEFAULT_PAYMENT_NUMBER
        defaultPaymentCalculationShouldBeFound("paymentNumber.equals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentCalculationList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultPaymentCalculationShouldNotBeFound("paymentNumber.equals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentNumber not equals to DEFAULT_PAYMENT_NUMBER
        defaultPaymentCalculationShouldNotBeFound("paymentNumber.notEquals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentCalculationList where paymentNumber not equals to UPDATED_PAYMENT_NUMBER
        defaultPaymentCalculationShouldBeFound("paymentNumber.notEquals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentNumber in DEFAULT_PAYMENT_NUMBER or UPDATED_PAYMENT_NUMBER
        defaultPaymentCalculationShouldBeFound("paymentNumber.in=" + DEFAULT_PAYMENT_NUMBER + "," + UPDATED_PAYMENT_NUMBER);

        // Get all the paymentCalculationList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultPaymentCalculationShouldNotBeFound("paymentNumber.in=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentNumber is not null
        defaultPaymentCalculationShouldBeFound("paymentNumber.specified=true");

        // Get all the paymentCalculationList where paymentNumber is null
        defaultPaymentCalculationShouldNotBeFound("paymentNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentNumberContainsSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentNumber contains DEFAULT_PAYMENT_NUMBER
        defaultPaymentCalculationShouldBeFound("paymentNumber.contains=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentCalculationList where paymentNumber contains UPDATED_PAYMENT_NUMBER
        defaultPaymentCalculationShouldNotBeFound("paymentNumber.contains=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentNumber does not contain DEFAULT_PAYMENT_NUMBER
        defaultPaymentCalculationShouldNotBeFound("paymentNumber.doesNotContain=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the paymentCalculationList where paymentNumber does not contain UPDATED_PAYMENT_NUMBER
        defaultPaymentCalculationShouldBeFound("paymentNumber.doesNotContain=" + UPDATED_PAYMENT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultPaymentCalculationShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentCalculationList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentCalculationShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultPaymentCalculationShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentCalculationList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultPaymentCalculationShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultPaymentCalculationShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the paymentCalculationList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentCalculationShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate is not null
        defaultPaymentCalculationShouldBeFound("paymentDate.specified=true");

        // Get all the paymentCalculationList where paymentDate is null
        defaultPaymentCalculationShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultPaymentCalculationShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentCalculationList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultPaymentCalculationShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultPaymentCalculationShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentCalculationList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultPaymentCalculationShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultPaymentCalculationShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentCalculationList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultPaymentCalculationShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultPaymentCalculationShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentCalculationList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultPaymentCalculationShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentCategory equals to DEFAULT_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldBeFound("paymentCategory.equals=" + DEFAULT_PAYMENT_CATEGORY);

        // Get all the paymentCalculationList where paymentCategory equals to UPDATED_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldNotBeFound("paymentCategory.equals=" + UPDATED_PAYMENT_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentCategory not equals to DEFAULT_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldNotBeFound("paymentCategory.notEquals=" + DEFAULT_PAYMENT_CATEGORY);

        // Get all the paymentCalculationList where paymentCategory not equals to UPDATED_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldBeFound("paymentCategory.notEquals=" + UPDATED_PAYMENT_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentCategory in DEFAULT_PAYMENT_CATEGORY or UPDATED_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldBeFound("paymentCategory.in=" + DEFAULT_PAYMENT_CATEGORY + "," + UPDATED_PAYMENT_CATEGORY);

        // Get all the paymentCalculationList where paymentCategory equals to UPDATED_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldNotBeFound("paymentCategory.in=" + UPDATED_PAYMENT_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentCategory is not null
        defaultPaymentCalculationShouldBeFound("paymentCategory.specified=true");

        // Get all the paymentCalculationList where paymentCategory is null
        defaultPaymentCalculationShouldNotBeFound("paymentCategory.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentCategoryContainsSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentCategory contains DEFAULT_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldBeFound("paymentCategory.contains=" + DEFAULT_PAYMENT_CATEGORY);

        // Get all the paymentCalculationList where paymentCategory contains UPDATED_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldNotBeFound("paymentCategory.contains=" + UPDATED_PAYMENT_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentCategory does not contain DEFAULT_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldNotBeFound("paymentCategory.doesNotContain=" + DEFAULT_PAYMENT_CATEGORY);

        // Get all the paymentCalculationList where paymentCategory does not contain UPDATED_PAYMENT_CATEGORY
        defaultPaymentCalculationShouldBeFound("paymentCategory.doesNotContain=" + UPDATED_PAYMENT_CATEGORY);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense equals to DEFAULT_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldBeFound("paymentExpense.equals=" + DEFAULT_PAYMENT_EXPENSE);

        // Get all the paymentCalculationList where paymentExpense equals to UPDATED_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.equals=" + UPDATED_PAYMENT_EXPENSE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense not equals to DEFAULT_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.notEquals=" + DEFAULT_PAYMENT_EXPENSE);

        // Get all the paymentCalculationList where paymentExpense not equals to UPDATED_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldBeFound("paymentExpense.notEquals=" + UPDATED_PAYMENT_EXPENSE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsInShouldWork() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense in DEFAULT_PAYMENT_EXPENSE or UPDATED_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldBeFound("paymentExpense.in=" + DEFAULT_PAYMENT_EXPENSE + "," + UPDATED_PAYMENT_EXPENSE);

        // Get all the paymentCalculationList where paymentExpense equals to UPDATED_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.in=" + UPDATED_PAYMENT_EXPENSE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense is not null
        defaultPaymentCalculationShouldBeFound("paymentExpense.specified=true");

        // Get all the paymentCalculationList where paymentExpense is null
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense is greater than or equal to DEFAULT_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldBeFound("paymentExpense.greaterThanOrEqual=" + DEFAULT_PAYMENT_EXPENSE);

        // Get all the paymentCalculationList where paymentExpense is greater than or equal to UPDATED_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.greaterThanOrEqual=" + UPDATED_PAYMENT_EXPENSE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense is less than or equal to DEFAULT_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldBeFound("paymentExpense.lessThanOrEqual=" + DEFAULT_PAYMENT_EXPENSE);

        // Get all the paymentCalculationList where paymentExpense is less than or equal to SMALLER_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.lessThanOrEqual=" + SMALLER_PAYMENT_EXPENSE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense is less than DEFAULT_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.lessThan=" + DEFAULT_PAYMENT_EXPENSE);

        // Get all the paymentCalculationList where paymentExpense is less than UPDATED_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldBeFound("paymentExpense.lessThan=" + UPDATED_PAYMENT_EXPENSE);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentExpenseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentExpense is greater than DEFAULT_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldNotBeFound("paymentExpense.greaterThan=" + DEFAULT_PAYMENT_EXPENSE);

        // Get all the paymentCalculationList where paymentExpense is greater than SMALLER_PAYMENT_EXPENSE
        defaultPaymentCalculationShouldBeFound("paymentExpense.greaterThan=" + SMALLER_PAYMENT_EXPENSE);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT equals to DEFAULT_WITHHOLDING_VAT
        defaultPaymentCalculationShouldBeFound("withholdingVAT.equals=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the paymentCalculationList where withholdingVAT equals to UPDATED_WITHHOLDING_VAT
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.equals=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT not equals to DEFAULT_WITHHOLDING_VAT
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.notEquals=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the paymentCalculationList where withholdingVAT not equals to UPDATED_WITHHOLDING_VAT
        defaultPaymentCalculationShouldBeFound("withholdingVAT.notEquals=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsInShouldWork() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT in DEFAULT_WITHHOLDING_VAT or UPDATED_WITHHOLDING_VAT
        defaultPaymentCalculationShouldBeFound("withholdingVAT.in=" + DEFAULT_WITHHOLDING_VAT + "," + UPDATED_WITHHOLDING_VAT);

        // Get all the paymentCalculationList where withholdingVAT equals to UPDATED_WITHHOLDING_VAT
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.in=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT is not null
        defaultPaymentCalculationShouldBeFound("withholdingVAT.specified=true");

        // Get all the paymentCalculationList where withholdingVAT is null
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT is greater than or equal to DEFAULT_WITHHOLDING_VAT
        defaultPaymentCalculationShouldBeFound("withholdingVAT.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the paymentCalculationList where withholdingVAT is greater than or equal to UPDATED_WITHHOLDING_VAT
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.greaterThanOrEqual=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT is less than or equal to DEFAULT_WITHHOLDING_VAT
        defaultPaymentCalculationShouldBeFound("withholdingVAT.lessThanOrEqual=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the paymentCalculationList where withholdingVAT is less than or equal to SMALLER_WITHHOLDING_VAT
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.lessThanOrEqual=" + SMALLER_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT is less than DEFAULT_WITHHOLDING_VAT
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.lessThan=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the paymentCalculationList where withholdingVAT is less than UPDATED_WITHHOLDING_VAT
        defaultPaymentCalculationShouldBeFound("withholdingVAT.lessThan=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingVATIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingVAT is greater than DEFAULT_WITHHOLDING_VAT
        defaultPaymentCalculationShouldNotBeFound("withholdingVAT.greaterThan=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the paymentCalculationList where withholdingVAT is greater than SMALLER_WITHHOLDING_VAT
        defaultPaymentCalculationShouldBeFound("withholdingVAT.greaterThan=" + SMALLER_WITHHOLDING_VAT);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax equals to DEFAULT_WITHHOLDING_TAX
        defaultPaymentCalculationShouldBeFound("withholdingTax.equals=" + DEFAULT_WITHHOLDING_TAX);

        // Get all the paymentCalculationList where withholdingTax equals to UPDATED_WITHHOLDING_TAX
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.equals=" + UPDATED_WITHHOLDING_TAX);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax not equals to DEFAULT_WITHHOLDING_TAX
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.notEquals=" + DEFAULT_WITHHOLDING_TAX);

        // Get all the paymentCalculationList where withholdingTax not equals to UPDATED_WITHHOLDING_TAX
        defaultPaymentCalculationShouldBeFound("withholdingTax.notEquals=" + UPDATED_WITHHOLDING_TAX);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsInShouldWork() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax in DEFAULT_WITHHOLDING_TAX or UPDATED_WITHHOLDING_TAX
        defaultPaymentCalculationShouldBeFound("withholdingTax.in=" + DEFAULT_WITHHOLDING_TAX + "," + UPDATED_WITHHOLDING_TAX);

        // Get all the paymentCalculationList where withholdingTax equals to UPDATED_WITHHOLDING_TAX
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.in=" + UPDATED_WITHHOLDING_TAX);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax is not null
        defaultPaymentCalculationShouldBeFound("withholdingTax.specified=true");

        // Get all the paymentCalculationList where withholdingTax is null
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax is greater than or equal to DEFAULT_WITHHOLDING_TAX
        defaultPaymentCalculationShouldBeFound("withholdingTax.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_TAX);

        // Get all the paymentCalculationList where withholdingTax is greater than or equal to UPDATED_WITHHOLDING_TAX
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.greaterThanOrEqual=" + UPDATED_WITHHOLDING_TAX);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax is less than or equal to DEFAULT_WITHHOLDING_TAX
        defaultPaymentCalculationShouldBeFound("withholdingTax.lessThanOrEqual=" + DEFAULT_WITHHOLDING_TAX);

        // Get all the paymentCalculationList where withholdingTax is less than or equal to SMALLER_WITHHOLDING_TAX
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.lessThanOrEqual=" + SMALLER_WITHHOLDING_TAX);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax is less than DEFAULT_WITHHOLDING_TAX
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.lessThan=" + DEFAULT_WITHHOLDING_TAX);

        // Get all the paymentCalculationList where withholdingTax is less than UPDATED_WITHHOLDING_TAX
        defaultPaymentCalculationShouldBeFound("withholdingTax.lessThan=" + UPDATED_WITHHOLDING_TAX);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByWithholdingTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where withholdingTax is greater than DEFAULT_WITHHOLDING_TAX
        defaultPaymentCalculationShouldNotBeFound("withholdingTax.greaterThan=" + DEFAULT_WITHHOLDING_TAX);

        // Get all the paymentCalculationList where withholdingTax is greater than SMALLER_WITHHOLDING_TAX
        defaultPaymentCalculationShouldBeFound("withholdingTax.greaterThan=" + SMALLER_WITHHOLDING_TAX);
    }


    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount equals to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldBeFound("paymentAmount.equals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentCalculationList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.equals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount not equals to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.notEquals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentCalculationList where paymentAmount not equals to UPDATED_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldBeFound("paymentAmount.notEquals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount in DEFAULT_PAYMENT_AMOUNT or UPDATED_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldBeFound("paymentAmount.in=" + DEFAULT_PAYMENT_AMOUNT + "," + UPDATED_PAYMENT_AMOUNT);

        // Get all the paymentCalculationList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.in=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount is not null
        defaultPaymentCalculationShouldBeFound("paymentAmount.specified=true");

        // Get all the paymentCalculationList where paymentAmount is null
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount is greater than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldBeFound("paymentAmount.greaterThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentCalculationList where paymentAmount is greater than or equal to UPDATED_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.greaterThanOrEqual=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount is less than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldBeFound("paymentAmount.lessThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentCalculationList where paymentAmount is less than or equal to SMALLER_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.lessThanOrEqual=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount is less than DEFAULT_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.lessThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentCalculationList where paymentAmount is less than UPDATED_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldBeFound("paymentAmount.lessThan=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentCalculationsByPaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        // Get all the paymentCalculationList where paymentAmount is greater than DEFAULT_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldNotBeFound("paymentAmount.greaterThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the paymentCalculationList where paymentAmount is greater than SMALLER_PAYMENT_AMOUNT
        defaultPaymentCalculationShouldBeFound("paymentAmount.greaterThan=" + SMALLER_PAYMENT_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentCalculationShouldBeFound(String filter) throws Exception {
        restPaymentCalculationMockMvc.perform(get("/api/payment-calculations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCalculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentCategory").value(hasItem(DEFAULT_PAYMENT_CATEGORY)))
            .andExpect(jsonPath("$.[*].paymentExpense").value(hasItem(DEFAULT_PAYMENT_EXPENSE.intValue())))
            .andExpect(jsonPath("$.[*].withholdingVAT").value(hasItem(DEFAULT_WITHHOLDING_VAT.intValue())))
            .andExpect(jsonPath("$.[*].withholdingTax").value(hasItem(DEFAULT_WITHHOLDING_TAX.intValue())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restPaymentCalculationMockMvc.perform(get("/api/payment-calculations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentCalculationShouldNotBeFound(String filter) throws Exception {
        restPaymentCalculationMockMvc.perform(get("/api/payment-calculations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentCalculationMockMvc.perform(get("/api/payment-calculations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentCalculation() throws Exception {
        // Get the paymentCalculation
        restPaymentCalculationMockMvc.perform(get("/api/payment-calculations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentCalculation() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        int databaseSizeBeforeUpdate = paymentCalculationRepository.findAll().size();

        // Update the paymentCalculation
        PaymentCalculation updatedPaymentCalculation = paymentCalculationRepository.findById(paymentCalculation.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentCalculation are not directly saved in db
        em.detach(updatedPaymentCalculation);
        updatedPaymentCalculation
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentCategory(UPDATED_PAYMENT_CATEGORY)
            .paymentExpense(UPDATED_PAYMENT_EXPENSE)
            .withholdingVAT(UPDATED_WITHHOLDING_VAT)
            .withholdingTax(UPDATED_WITHHOLDING_TAX)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT);
        PaymentCalculationDTO paymentCalculationDTO = paymentCalculationMapper.toDto(updatedPaymentCalculation);

        restPaymentCalculationMockMvc.perform(put("/api/payment-calculations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCalculationDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentCalculation in the database
        List<PaymentCalculation> paymentCalculationList = paymentCalculationRepository.findAll();
        assertThat(paymentCalculationList).hasSize(databaseSizeBeforeUpdate);
        PaymentCalculation testPaymentCalculation = paymentCalculationList.get(paymentCalculationList.size() - 1);
        assertThat(testPaymentCalculation.getPaymentNumber()).isEqualTo(UPDATED_PAYMENT_NUMBER);
        assertThat(testPaymentCalculation.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentCalculation.getPaymentCategory()).isEqualTo(UPDATED_PAYMENT_CATEGORY);
        assertThat(testPaymentCalculation.getPaymentExpense()).isEqualTo(UPDATED_PAYMENT_EXPENSE);
        assertThat(testPaymentCalculation.getWithholdingVAT()).isEqualTo(UPDATED_WITHHOLDING_VAT);
        assertThat(testPaymentCalculation.getWithholdingTax()).isEqualTo(UPDATED_WITHHOLDING_TAX);
        assertThat(testPaymentCalculation.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);

        // Validate the PaymentCalculation in Elasticsearch
        verify(mockPaymentCalculationSearchRepository, times(1)).save(testPaymentCalculation);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentCalculation() throws Exception {
        int databaseSizeBeforeUpdate = paymentCalculationRepository.findAll().size();

        // Create the PaymentCalculation
        PaymentCalculationDTO paymentCalculationDTO = paymentCalculationMapper.toDto(paymentCalculation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentCalculationMockMvc.perform(put("/api/payment-calculations").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentCalculationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentCalculation in the database
        List<PaymentCalculation> paymentCalculationList = paymentCalculationRepository.findAll();
        assertThat(paymentCalculationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentCalculation in Elasticsearch
        verify(mockPaymentCalculationSearchRepository, times(0)).save(paymentCalculation);
    }

    @Test
    @Transactional
    public void deletePaymentCalculation() throws Exception {
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);

        int databaseSizeBeforeDelete = paymentCalculationRepository.findAll().size();

        // Delete the paymentCalculation
        restPaymentCalculationMockMvc.perform(delete("/api/payment-calculations/{id}", paymentCalculation.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentCalculation> paymentCalculationList = paymentCalculationRepository.findAll();
        assertThat(paymentCalculationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PaymentCalculation in Elasticsearch
        verify(mockPaymentCalculationSearchRepository, times(1)).deleteById(paymentCalculation.getId());
    }

    @Test
    @Transactional
    public void searchPaymentCalculation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        paymentCalculationRepository.saveAndFlush(paymentCalculation);
        when(mockPaymentCalculationSearchRepository.search(queryStringQuery("id:" + paymentCalculation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paymentCalculation), PageRequest.of(0, 1), 1));

        // Search the paymentCalculation
        restPaymentCalculationMockMvc.perform(get("/api/_search/payment-calculations?query=id:" + paymentCalculation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentCalculation.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentCategory").value(hasItem(DEFAULT_PAYMENT_CATEGORY)))
            .andExpect(jsonPath("$.[*].paymentExpense").value(hasItem(DEFAULT_PAYMENT_EXPENSE.intValue())))
            .andExpect(jsonPath("$.[*].withholdingVAT").value(hasItem(DEFAULT_WITHHOLDING_VAT.intValue())))
            .andExpect(jsonPath("$.[*].withholdingTax").value(hasItem(DEFAULT_WITHHOLDING_TAX.intValue())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(DEFAULT_PAYMENT_AMOUNT.intValue())));
    }
}
