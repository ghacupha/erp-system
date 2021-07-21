package io.github.erp.web.rest;

import io.github.erp.ErpServiceApp;
import io.github.erp.config.SecurityBeanOverrideConfiguration;
import io.github.erp.domain.PaymentRequisition;
import io.github.erp.domain.Payment;
import io.github.erp.repository.PaymentRequisitionRepository;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import io.github.erp.service.mapper.PaymentRequisitionMapper;
import io.github.erp.service.dto.PaymentRequisitionCriteria;
import io.github.erp.service.PaymentRequisitionQueryService;

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
 * Integration tests for the {@link PaymentRequisitionResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ErpServiceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaymentRequisitionResourceIT {

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_INVOICED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVOICED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INVOICED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DISBURSEMENT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISBURSEMENT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISBURSEMENT_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VATABLE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_VATABLE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_VATABLE_AMOUNT = new BigDecimal(1 - 1);

    @Autowired
    private PaymentRequisitionRepository paymentRequisitionRepository;

    @Autowired
    private PaymentRequisitionMapper paymentRequisitionMapper;

    @Autowired
    private PaymentRequisitionService paymentRequisitionService;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PaymentRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private PaymentRequisitionSearchRepository mockPaymentRequisitionSearchRepository;

    @Autowired
    private PaymentRequisitionQueryService paymentRequisitionQueryService;

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
            .dealerName(DEFAULT_DEALER_NAME)
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
            .dealerName(UPDATED_DEALER_NAME)
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
    public void createPaymentRequisition() throws Exception {
        int databaseSizeBeforeCreate = paymentRequisitionRepository.findAll().size();
        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);
        restPaymentRequisitionMockMvc.perform(post("/api/payment-requisitions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentRequisition testPaymentRequisition = paymentRequisitionList.get(paymentRequisitionList.size() - 1);
        assertThat(testPaymentRequisition.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualTo(DEFAULT_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualTo(DEFAULT_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getVatableAmount()).isEqualTo(DEFAULT_VATABLE_AMOUNT);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(1)).save(testPaymentRequisition);
    }

    @Test
    @Transactional
    public void createPaymentRequisitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRequisitionRepository.findAll().size();

        // Create the PaymentRequisition with an existing ID
        paymentRequisition.setId(1L);
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentRequisitionMockMvc.perform(post("/api/payment-requisitions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }


    @Test
    @Transactional
    public void getAllPaymentRequisitions() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList
        restPaymentRequisitionMockMvc.perform(get("/api/payment-requisitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(DEFAULT_INVOICED_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(DEFAULT_DISBURSEMENT_COST.intValue())))
            .andExpect(jsonPath("$.[*].vatableAmount").value(hasItem(DEFAULT_VATABLE_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentRequisition() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get the paymentRequisition
        restPaymentRequisitionMockMvc.perform(get("/api/payment-requisitions/{id}", paymentRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentRequisition.getId().intValue()))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.invoicedAmount").value(DEFAULT_INVOICED_AMOUNT.intValue()))
            .andExpect(jsonPath("$.disbursementCost").value(DEFAULT_DISBURSEMENT_COST.intValue()))
            .andExpect(jsonPath("$.vatableAmount").value(DEFAULT_VATABLE_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getPaymentRequisitionsByIdFiltering() throws Exception {
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
    public void getAllPaymentRequisitionsByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName equals to DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName equals to UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName not equals to UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName equals to UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName is not null
        defaultPaymentRequisitionShouldBeFound("dealerName.specified=true");

        // Get all the paymentRequisitionList where dealerName is null
        defaultPaymentRequisitionShouldNotBeFound("dealerName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentRequisitionsByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName contains DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName contains UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName does not contain UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }


    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount equals to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.equals=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.equals=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount not equals to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.notEquals=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount not equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.notEquals=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount in DEFAULT_INVOICED_AMOUNT or UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.in=" + DEFAULT_INVOICED_AMOUNT + "," + UPDATED_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount equals to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.in=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is not null
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.specified=true");

        // Get all the paymentRequisitionList where invoicedAmount is null
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is greater than or equal to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.greaterThanOrEqual=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is greater than or equal to UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.greaterThanOrEqual=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is less than or equal to DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.lessThanOrEqual=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is less than or equal to SMALLER_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.lessThanOrEqual=" + SMALLER_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is less than DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.lessThan=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is less than UPDATED_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.lessThan=" + UPDATED_INVOICED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByInvoicedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where invoicedAmount is greater than DEFAULT_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("invoicedAmount.greaterThan=" + DEFAULT_INVOICED_AMOUNT);

        // Get all the paymentRequisitionList where invoicedAmount is greater than SMALLER_INVOICED_AMOUNT
        defaultPaymentRequisitionShouldBeFound("invoicedAmount.greaterThan=" + SMALLER_INVOICED_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost equals to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.equals=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost equals to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.equals=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost not equals to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.notEquals=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost not equals to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.notEquals=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost in DEFAULT_DISBURSEMENT_COST or UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.in=" + DEFAULT_DISBURSEMENT_COST + "," + UPDATED_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost equals to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.in=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is not null
        defaultPaymentRequisitionShouldBeFound("disbursementCost.specified=true");

        // Get all the paymentRequisitionList where disbursementCost is null
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is greater than or equal to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.greaterThanOrEqual=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is greater than or equal to UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.greaterThanOrEqual=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is less than or equal to DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.lessThanOrEqual=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is less than or equal to SMALLER_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.lessThanOrEqual=" + SMALLER_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is less than DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.lessThan=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is less than UPDATED_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.lessThan=" + UPDATED_DISBURSEMENT_COST);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByDisbursementCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where disbursementCost is greater than DEFAULT_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldNotBeFound("disbursementCost.greaterThan=" + DEFAULT_DISBURSEMENT_COST);

        // Get all the paymentRequisitionList where disbursementCost is greater than SMALLER_DISBURSEMENT_COST
        defaultPaymentRequisitionShouldBeFound("disbursementCost.greaterThan=" + SMALLER_DISBURSEMENT_COST);
    }


    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount equals to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.equals=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount equals to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.equals=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount not equals to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.notEquals=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount not equals to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.notEquals=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount in DEFAULT_VATABLE_AMOUNT or UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.in=" + DEFAULT_VATABLE_AMOUNT + "," + UPDATED_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount equals to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.in=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is not null
        defaultPaymentRequisitionShouldBeFound("vatableAmount.specified=true");

        // Get all the paymentRequisitionList where vatableAmount is null
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is greater than or equal to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.greaterThanOrEqual=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is greater than or equal to UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.greaterThanOrEqual=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is less than or equal to DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.lessThanOrEqual=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is less than or equal to SMALLER_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.lessThanOrEqual=" + SMALLER_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is less than DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.lessThan=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is less than UPDATED_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.lessThan=" + UPDATED_VATABLE_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentRequisitionsByVatableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where vatableAmount is greater than DEFAULT_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("vatableAmount.greaterThan=" + DEFAULT_VATABLE_AMOUNT);

        // Get all the paymentRequisitionList where vatableAmount is greater than SMALLER_VATABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("vatableAmount.greaterThan=" + SMALLER_VATABLE_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPaymentRequisitionsByRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Payment requisition = PaymentResourceIT.createEntity(em);
        em.persist(requisition);
        em.flush();
        paymentRequisition.addRequisition(requisition);
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Long requisitionId = requisition.getId();

        // Get all the paymentRequisitionList where requisition equals to requisitionId
        defaultPaymentRequisitionShouldBeFound("requisitionId.equals=" + requisitionId);

        // Get all the paymentRequisitionList where requisition equals to requisitionId + 1
        defaultPaymentRequisitionShouldNotBeFound("requisitionId.equals=" + (requisitionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentRequisitionShouldBeFound(String filter) throws Exception {
        restPaymentRequisitionMockMvc.perform(get("/api/payment-requisitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(DEFAULT_INVOICED_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(DEFAULT_DISBURSEMENT_COST.intValue())))
            .andExpect(jsonPath("$.[*].vatableAmount").value(hasItem(DEFAULT_VATABLE_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restPaymentRequisitionMockMvc.perform(get("/api/payment-requisitions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentRequisitionShouldNotBeFound(String filter) throws Exception {
        restPaymentRequisitionMockMvc.perform(get("/api/payment-requisitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentRequisitionMockMvc.perform(get("/api/payment-requisitions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentRequisition() throws Exception {
        // Get the paymentRequisition
        restPaymentRequisitionMockMvc.perform(get("/api/payment-requisitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentRequisition() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();

        // Update the paymentRequisition
        PaymentRequisition updatedPaymentRequisition = paymentRequisitionRepository.findById(paymentRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentRequisition are not directly saved in db
        em.detach(updatedPaymentRequisition);
        updatedPaymentRequisition
            .dealerName(UPDATED_DEALER_NAME)
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .vatableAmount(UPDATED_VATABLE_AMOUNT);
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(updatedPaymentRequisition);

        restPaymentRequisitionMockMvc.perform(put("/api/payment-requisitions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);
        PaymentRequisition testPaymentRequisition = paymentRequisitionList.get(paymentRequisitionList.size() - 1);
        assertThat(testPaymentRequisition.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualTo(UPDATED_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getVatableAmount()).isEqualTo(UPDATED_VATABLE_AMOUNT);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(1)).save(testPaymentRequisition);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentRequisition() throws Exception {
        int databaseSizeBeforeUpdate = paymentRequisitionRepository.findAll().size();

        // Create the PaymentRequisition
        PaymentRequisitionDTO paymentRequisitionDTO = paymentRequisitionMapper.toDto(paymentRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentRequisitionMockMvc.perform(put("/api/payment-requisitions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentRequisitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentRequisition in the database
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(0)).save(paymentRequisition);
    }

    @Test
    @Transactional
    public void deletePaymentRequisition() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        int databaseSizeBeforeDelete = paymentRequisitionRepository.findAll().size();

        // Delete the paymentRequisition
        restPaymentRequisitionMockMvc.perform(delete("/api/payment-requisitions/{id}", paymentRequisition.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentRequisition> paymentRequisitionList = paymentRequisitionRepository.findAll();
        assertThat(paymentRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PaymentRequisition in Elasticsearch
        verify(mockPaymentRequisitionSearchRepository, times(1)).deleteById(paymentRequisition.getId());
    }

    @Test
    @Transactional
    public void searchPaymentRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        when(mockPaymentRequisitionSearchRepository.search(queryStringQuery("id:" + paymentRequisition.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paymentRequisition), PageRequest.of(0, 1), 1));

        // Search the paymentRequisition
        restPaymentRequisitionMockMvc.perform(get("/api/_search/payment-requisitions?query=id:" + paymentRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(DEFAULT_INVOICED_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(DEFAULT_DISBURSEMENT_COST.intValue())))
            .andExpect(jsonPath("$.[*].vatableAmount").value(hasItem(DEFAULT_VATABLE_AMOUNT.intValue())));
    }
}
