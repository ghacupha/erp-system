package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 9 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.PaymentRequisition;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PaymentRequisitionRepository;
import io.github.erp.repository.search.PaymentRequisitionSearchRepository;
import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import io.github.erp.service.mapper.PaymentRequisitionMapper;
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
 * Integration tests for the {@link PaymentRequisitionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PAYMENTS_USER", "FIXED_ASSETS_USER"})
class PaymentRequisitionResourceIT {

    private static final LocalDate DEFAULT_RECEPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RECEPTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RECEPTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRIEF_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BRIEF_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REQUISITION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REQUISITION_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_INVOICED_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INVOICED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INVOICED_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DISBURSEMENT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISBURSEMENT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISBURSEMENT_COST = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TAXABLE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXABLE_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TAXABLE_AMOUNT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_REQUISITION_PROCESSED = false;
    private static final Boolean UPDATED_REQUISITION_PROCESSED = true;

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments/payment-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payments/_search/payment-requisitions";

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
            .receptionDate(DEFAULT_RECEPTION_DATE)
            .dealerName(DEFAULT_DEALER_NAME)
            .briefDescription(DEFAULT_BRIEF_DESCRIPTION)
            .requisitionNumber(DEFAULT_REQUISITION_NUMBER)
            .invoicedAmount(DEFAULT_INVOICED_AMOUNT)
            .disbursementCost(DEFAULT_DISBURSEMENT_COST)
            .taxableAmount(DEFAULT_TAXABLE_AMOUNT)
            .requisitionProcessed(DEFAULT_REQUISITION_PROCESSED)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
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
            .receptionDate(UPDATED_RECEPTION_DATE)
            .dealerName(UPDATED_DEALER_NAME)
            .briefDescription(UPDATED_BRIEF_DESCRIPTION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .requisitionProcessed(UPDATED_REQUISITION_PROCESSED)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
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
        assertThat(testPaymentRequisition.getReceptionDate()).isEqualTo(DEFAULT_RECEPTION_DATE);
        assertThat(testPaymentRequisition.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testPaymentRequisition.getBriefDescription()).isEqualTo(DEFAULT_BRIEF_DESCRIPTION);
        assertThat(testPaymentRequisition.getRequisitionNumber()).isEqualTo(DEFAULT_REQUISITION_NUMBER);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualByComparingTo(DEFAULT_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualByComparingTo(DEFAULT_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getTaxableAmount()).isEqualByComparingTo(DEFAULT_TAXABLE_AMOUNT);
        assertThat(testPaymentRequisition.getRequisitionProcessed()).isEqualTo(DEFAULT_REQUISITION_PROCESSED);
        assertThat(testPaymentRequisition.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentRequisition.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

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
            .andExpect(jsonPath("$.[*].receptionDate").value(hasItem(DEFAULT_RECEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].briefDescription").value(hasItem(DEFAULT_BRIEF_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].requisitionNumber").value(hasItem(DEFAULT_REQUISITION_NUMBER)))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(sameNumber(DEFAULT_DISBURSEMENT_COST))))
            .andExpect(jsonPath("$.[*].taxableAmount").value(hasItem(sameNumber(DEFAULT_TAXABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].requisitionProcessed").value(hasItem(DEFAULT_REQUISITION_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
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
            .andExpect(jsonPath("$.receptionDate").value(DEFAULT_RECEPTION_DATE.toString()))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.briefDescription").value(DEFAULT_BRIEF_DESCRIPTION))
            .andExpect(jsonPath("$.requisitionNumber").value(DEFAULT_REQUISITION_NUMBER))
            .andExpect(jsonPath("$.invoicedAmount").value(sameNumber(DEFAULT_INVOICED_AMOUNT)))
            .andExpect(jsonPath("$.disbursementCost").value(sameNumber(DEFAULT_DISBURSEMENT_COST)))
            .andExpect(jsonPath("$.taxableAmount").value(sameNumber(DEFAULT_TAXABLE_AMOUNT)))
            .andExpect(jsonPath("$.requisitionProcessed").value(DEFAULT_REQUISITION_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
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
    void getAllPaymentRequisitionsByReceptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate equals to DEFAULT_RECEPTION_DATE
        defaultPaymentRequisitionShouldBeFound("receptionDate.equals=" + DEFAULT_RECEPTION_DATE);

        // Get all the paymentRequisitionList where receptionDate equals to UPDATED_RECEPTION_DATE
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.equals=" + UPDATED_RECEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByReceptionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate not equals to DEFAULT_RECEPTION_DATE
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.notEquals=" + DEFAULT_RECEPTION_DATE);

        // Get all the paymentRequisitionList where receptionDate not equals to UPDATED_RECEPTION_DATE
        defaultPaymentRequisitionShouldBeFound("receptionDate.notEquals=" + UPDATED_RECEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByReceptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate in DEFAULT_RECEPTION_DATE or UPDATED_RECEPTION_DATE
        defaultPaymentRequisitionShouldBeFound("receptionDate.in=" + DEFAULT_RECEPTION_DATE + "," + UPDATED_RECEPTION_DATE);

        // Get all the paymentRequisitionList where receptionDate equals to UPDATED_RECEPTION_DATE
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.in=" + UPDATED_RECEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByReceptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate is not null
        defaultPaymentRequisitionShouldBeFound("receptionDate.specified=true");

        // Get all the paymentRequisitionList where receptionDate is null
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByReceptionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate is greater than or equal to DEFAULT_RECEPTION_DATE
        defaultPaymentRequisitionShouldBeFound("receptionDate.greaterThanOrEqual=" + DEFAULT_RECEPTION_DATE);

        // Get all the paymentRequisitionList where receptionDate is greater than or equal to UPDATED_RECEPTION_DATE
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.greaterThanOrEqual=" + UPDATED_RECEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByReceptionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate is less than or equal to DEFAULT_RECEPTION_DATE
        defaultPaymentRequisitionShouldBeFound("receptionDate.lessThanOrEqual=" + DEFAULT_RECEPTION_DATE);

        // Get all the paymentRequisitionList where receptionDate is less than or equal to SMALLER_RECEPTION_DATE
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.lessThanOrEqual=" + SMALLER_RECEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByReceptionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate is less than DEFAULT_RECEPTION_DATE
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.lessThan=" + DEFAULT_RECEPTION_DATE);

        // Get all the paymentRequisitionList where receptionDate is less than UPDATED_RECEPTION_DATE
        defaultPaymentRequisitionShouldBeFound("receptionDate.lessThan=" + UPDATED_RECEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByReceptionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where receptionDate is greater than DEFAULT_RECEPTION_DATE
        defaultPaymentRequisitionShouldNotBeFound("receptionDate.greaterThan=" + DEFAULT_RECEPTION_DATE);

        // Get all the paymentRequisitionList where receptionDate is greater than SMALLER_RECEPTION_DATE
        defaultPaymentRequisitionShouldBeFound("receptionDate.greaterThan=" + SMALLER_RECEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName equals to DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName equals to UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName not equals to UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName equals to UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName is not null
        defaultPaymentRequisitionShouldBeFound("dealerName.specified=true");

        // Get all the paymentRequisitionList where dealerName is null
        defaultPaymentRequisitionShouldNotBeFound("dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName contains DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName contains UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultPaymentRequisitionShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the paymentRequisitionList where dealerName does not contain UPDATED_DEALER_NAME
        defaultPaymentRequisitionShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByBriefDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where briefDescription equals to DEFAULT_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldBeFound("briefDescription.equals=" + DEFAULT_BRIEF_DESCRIPTION);

        // Get all the paymentRequisitionList where briefDescription equals to UPDATED_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldNotBeFound("briefDescription.equals=" + UPDATED_BRIEF_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByBriefDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where briefDescription not equals to DEFAULT_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldNotBeFound("briefDescription.notEquals=" + DEFAULT_BRIEF_DESCRIPTION);

        // Get all the paymentRequisitionList where briefDescription not equals to UPDATED_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldBeFound("briefDescription.notEquals=" + UPDATED_BRIEF_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByBriefDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where briefDescription in DEFAULT_BRIEF_DESCRIPTION or UPDATED_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldBeFound("briefDescription.in=" + DEFAULT_BRIEF_DESCRIPTION + "," + UPDATED_BRIEF_DESCRIPTION);

        // Get all the paymentRequisitionList where briefDescription equals to UPDATED_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldNotBeFound("briefDescription.in=" + UPDATED_BRIEF_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByBriefDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where briefDescription is not null
        defaultPaymentRequisitionShouldBeFound("briefDescription.specified=true");

        // Get all the paymentRequisitionList where briefDescription is null
        defaultPaymentRequisitionShouldNotBeFound("briefDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByBriefDescriptionContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where briefDescription contains DEFAULT_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldBeFound("briefDescription.contains=" + DEFAULT_BRIEF_DESCRIPTION);

        // Get all the paymentRequisitionList where briefDescription contains UPDATED_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldNotBeFound("briefDescription.contains=" + UPDATED_BRIEF_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByBriefDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where briefDescription does not contain DEFAULT_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldNotBeFound("briefDescription.doesNotContain=" + DEFAULT_BRIEF_DESCRIPTION);

        // Get all the paymentRequisitionList where briefDescription does not contain UPDATED_BRIEF_DESCRIPTION
        defaultPaymentRequisitionShouldBeFound("briefDescription.doesNotContain=" + UPDATED_BRIEF_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionNumber equals to DEFAULT_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldBeFound("requisitionNumber.equals=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the paymentRequisitionList where requisitionNumber equals to UPDATED_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldNotBeFound("requisitionNumber.equals=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionNumber not equals to DEFAULT_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldNotBeFound("requisitionNumber.notEquals=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the paymentRequisitionList where requisitionNumber not equals to UPDATED_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldBeFound("requisitionNumber.notEquals=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionNumber in DEFAULT_REQUISITION_NUMBER or UPDATED_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldBeFound("requisitionNumber.in=" + DEFAULT_REQUISITION_NUMBER + "," + UPDATED_REQUISITION_NUMBER);

        // Get all the paymentRequisitionList where requisitionNumber equals to UPDATED_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldNotBeFound("requisitionNumber.in=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionNumber is not null
        defaultPaymentRequisitionShouldBeFound("requisitionNumber.specified=true");

        // Get all the paymentRequisitionList where requisitionNumber is null
        defaultPaymentRequisitionShouldNotBeFound("requisitionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionNumberContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionNumber contains DEFAULT_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldBeFound("requisitionNumber.contains=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the paymentRequisitionList where requisitionNumber contains UPDATED_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldNotBeFound("requisitionNumber.contains=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionNumber does not contain DEFAULT_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldNotBeFound("requisitionNumber.doesNotContain=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the paymentRequisitionList where requisitionNumber does not contain UPDATED_REQUISITION_NUMBER
        defaultPaymentRequisitionShouldBeFound("requisitionNumber.doesNotContain=" + UPDATED_REQUISITION_NUMBER);
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
    void getAllPaymentRequisitionsByTaxableAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount equals to DEFAULT_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("taxableAmount.equals=" + DEFAULT_TAXABLE_AMOUNT);

        // Get all the paymentRequisitionList where taxableAmount equals to UPDATED_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.equals=" + UPDATED_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByTaxableAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount not equals to DEFAULT_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.notEquals=" + DEFAULT_TAXABLE_AMOUNT);

        // Get all the paymentRequisitionList where taxableAmount not equals to UPDATED_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("taxableAmount.notEquals=" + UPDATED_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByTaxableAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount in DEFAULT_TAXABLE_AMOUNT or UPDATED_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("taxableAmount.in=" + DEFAULT_TAXABLE_AMOUNT + "," + UPDATED_TAXABLE_AMOUNT);

        // Get all the paymentRequisitionList where taxableAmount equals to UPDATED_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.in=" + UPDATED_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByTaxableAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount is not null
        defaultPaymentRequisitionShouldBeFound("taxableAmount.specified=true");

        // Get all the paymentRequisitionList where taxableAmount is null
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByTaxableAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount is greater than or equal to DEFAULT_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("taxableAmount.greaterThanOrEqual=" + DEFAULT_TAXABLE_AMOUNT);

        // Get all the paymentRequisitionList where taxableAmount is greater than or equal to UPDATED_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.greaterThanOrEqual=" + UPDATED_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByTaxableAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount is less than or equal to DEFAULT_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("taxableAmount.lessThanOrEqual=" + DEFAULT_TAXABLE_AMOUNT);

        // Get all the paymentRequisitionList where taxableAmount is less than or equal to SMALLER_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.lessThanOrEqual=" + SMALLER_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByTaxableAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount is less than DEFAULT_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.lessThan=" + DEFAULT_TAXABLE_AMOUNT);

        // Get all the paymentRequisitionList where taxableAmount is less than UPDATED_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("taxableAmount.lessThan=" + UPDATED_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByTaxableAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where taxableAmount is greater than DEFAULT_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldNotBeFound("taxableAmount.greaterThan=" + DEFAULT_TAXABLE_AMOUNT);

        // Get all the paymentRequisitionList where taxableAmount is greater than SMALLER_TAXABLE_AMOUNT
        defaultPaymentRequisitionShouldBeFound("taxableAmount.greaterThan=" + SMALLER_TAXABLE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionProcessed equals to DEFAULT_REQUISITION_PROCESSED
        defaultPaymentRequisitionShouldBeFound("requisitionProcessed.equals=" + DEFAULT_REQUISITION_PROCESSED);

        // Get all the paymentRequisitionList where requisitionProcessed equals to UPDATED_REQUISITION_PROCESSED
        defaultPaymentRequisitionShouldNotBeFound("requisitionProcessed.equals=" + UPDATED_REQUISITION_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionProcessedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionProcessed not equals to DEFAULT_REQUISITION_PROCESSED
        defaultPaymentRequisitionShouldNotBeFound("requisitionProcessed.notEquals=" + DEFAULT_REQUISITION_PROCESSED);

        // Get all the paymentRequisitionList where requisitionProcessed not equals to UPDATED_REQUISITION_PROCESSED
        defaultPaymentRequisitionShouldBeFound("requisitionProcessed.notEquals=" + UPDATED_REQUISITION_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionProcessed in DEFAULT_REQUISITION_PROCESSED or UPDATED_REQUISITION_PROCESSED
        defaultPaymentRequisitionShouldBeFound(
            "requisitionProcessed.in=" + DEFAULT_REQUISITION_PROCESSED + "," + UPDATED_REQUISITION_PROCESSED
        );

        // Get all the paymentRequisitionList where requisitionProcessed equals to UPDATED_REQUISITION_PROCESSED
        defaultPaymentRequisitionShouldNotBeFound("requisitionProcessed.in=" + UPDATED_REQUISITION_PROCESSED);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByRequisitionProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where requisitionProcessed is not null
        defaultPaymentRequisitionShouldBeFound("requisitionProcessed.specified=true");

        // Get all the paymentRequisitionList where requisitionProcessed is null
        defaultPaymentRequisitionShouldNotBeFound("requisitionProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentRequisitionList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentRequisitionList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the paymentRequisitionList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where fileUploadToken is not null
        defaultPaymentRequisitionShouldBeFound("fileUploadToken.specified=true");

        // Get all the paymentRequisitionList where fileUploadToken is null
        defaultPaymentRequisitionShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentRequisitionList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the paymentRequisitionList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultPaymentRequisitionShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentRequisitionList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentRequisitionList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the paymentRequisitionList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where compilationToken is not null
        defaultPaymentRequisitionShouldBeFound("compilationToken.specified=true");

        // Get all the paymentRequisitionList where compilationToken is null
        defaultPaymentRequisitionShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentRequisitionList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);

        // Get all the paymentRequisitionList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the paymentRequisitionList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultPaymentRequisitionShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPaymentRequisitionsByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
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
        paymentRequisition.addPaymentLabel(paymentLabel);
        paymentRequisitionRepository.saveAndFlush(paymentRequisition);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the paymentRequisitionList where paymentLabel equals to paymentLabelId
        defaultPaymentRequisitionShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the paymentRequisitionList where paymentLabel equals to (paymentLabelId + 1)
        defaultPaymentRequisitionShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
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
            .andExpect(jsonPath("$.[*].receptionDate").value(hasItem(DEFAULT_RECEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].briefDescription").value(hasItem(DEFAULT_BRIEF_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].requisitionNumber").value(hasItem(DEFAULT_REQUISITION_NUMBER)))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(sameNumber(DEFAULT_DISBURSEMENT_COST))))
            .andExpect(jsonPath("$.[*].taxableAmount").value(hasItem(sameNumber(DEFAULT_TAXABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].requisitionProcessed").value(hasItem(DEFAULT_REQUISITION_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

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
            .receptionDate(UPDATED_RECEPTION_DATE)
            .dealerName(UPDATED_DEALER_NAME)
            .briefDescription(UPDATED_BRIEF_DESCRIPTION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .requisitionProcessed(UPDATED_REQUISITION_PROCESSED)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
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
        assertThat(testPaymentRequisition.getReceptionDate()).isEqualTo(UPDATED_RECEPTION_DATE);
        assertThat(testPaymentRequisition.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testPaymentRequisition.getBriefDescription()).isEqualTo(UPDATED_BRIEF_DESCRIPTION);
        assertThat(testPaymentRequisition.getRequisitionNumber()).isEqualTo(UPDATED_REQUISITION_NUMBER);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualTo(UPDATED_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getTaxableAmount()).isEqualTo(UPDATED_TAXABLE_AMOUNT);
        assertThat(testPaymentRequisition.getRequisitionProcessed()).isEqualTo(UPDATED_REQUISITION_PROCESSED);
        assertThat(testPaymentRequisition.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentRequisition.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

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
            .receptionDate(UPDATED_RECEPTION_DATE)
            .dealerName(UPDATED_DEALER_NAME)
            .briefDescription(UPDATED_BRIEF_DESCRIPTION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .requisitionProcessed(UPDATED_REQUISITION_PROCESSED)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);

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
        assertThat(testPaymentRequisition.getReceptionDate()).isEqualTo(UPDATED_RECEPTION_DATE);
        assertThat(testPaymentRequisition.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testPaymentRequisition.getBriefDescription()).isEqualTo(UPDATED_BRIEF_DESCRIPTION);
        assertThat(testPaymentRequisition.getRequisitionNumber()).isEqualTo(UPDATED_REQUISITION_NUMBER);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualByComparingTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualByComparingTo(UPDATED_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getTaxableAmount()).isEqualByComparingTo(DEFAULT_TAXABLE_AMOUNT);
        assertThat(testPaymentRequisition.getRequisitionProcessed()).isEqualTo(UPDATED_REQUISITION_PROCESSED);
        assertThat(testPaymentRequisition.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentRequisition.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
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
            .receptionDate(UPDATED_RECEPTION_DATE)
            .dealerName(UPDATED_DEALER_NAME)
            .briefDescription(UPDATED_BRIEF_DESCRIPTION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .invoicedAmount(UPDATED_INVOICED_AMOUNT)
            .disbursementCost(UPDATED_DISBURSEMENT_COST)
            .taxableAmount(UPDATED_TAXABLE_AMOUNT)
            .requisitionProcessed(UPDATED_REQUISITION_PROCESSED)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

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
        assertThat(testPaymentRequisition.getReceptionDate()).isEqualTo(UPDATED_RECEPTION_DATE);
        assertThat(testPaymentRequisition.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testPaymentRequisition.getBriefDescription()).isEqualTo(UPDATED_BRIEF_DESCRIPTION);
        assertThat(testPaymentRequisition.getRequisitionNumber()).isEqualTo(UPDATED_REQUISITION_NUMBER);
        assertThat(testPaymentRequisition.getInvoicedAmount()).isEqualByComparingTo(UPDATED_INVOICED_AMOUNT);
        assertThat(testPaymentRequisition.getDisbursementCost()).isEqualByComparingTo(UPDATED_DISBURSEMENT_COST);
        assertThat(testPaymentRequisition.getTaxableAmount()).isEqualByComparingTo(UPDATED_TAXABLE_AMOUNT);
        assertThat(testPaymentRequisition.getRequisitionProcessed()).isEqualTo(UPDATED_REQUISITION_PROCESSED);
        assertThat(testPaymentRequisition.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPaymentRequisition.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
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
            .andExpect(jsonPath("$.[*].receptionDate").value(hasItem(DEFAULT_RECEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].briefDescription").value(hasItem(DEFAULT_BRIEF_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].requisitionNumber").value(hasItem(DEFAULT_REQUISITION_NUMBER)))
            .andExpect(jsonPath("$.[*].invoicedAmount").value(hasItem(sameNumber(DEFAULT_INVOICED_AMOUNT))))
            .andExpect(jsonPath("$.[*].disbursementCost").value(hasItem(sameNumber(DEFAULT_DISBURSEMENT_COST))))
            .andExpect(jsonPath("$.[*].taxableAmount").value(hasItem(sameNumber(DEFAULT_TAXABLE_AMOUNT))))
            .andExpect(jsonPath("$.[*].requisitionProcessed").value(hasItem(DEFAULT_REQUISITION_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
