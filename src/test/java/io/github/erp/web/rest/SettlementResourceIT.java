package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.3
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.PaymentCategory;
import io.github.erp.domain.PaymentInvoice;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.Settlement;
import io.github.erp.domain.Settlement;
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.repository.SettlementRepository;
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.service.SettlementService;
import io.github.erp.service.criteria.SettlementCriteria;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.mapper.SettlementMapper;
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
 * Integration tests for the {@link SettlementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SettlementResourceIT {

    private static final String DEFAULT_PAYMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CALCULATION_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CALCULATION_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CALCULATION_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CALCULATION_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/settlements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/settlements";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SettlementRepository settlementRepository;

    @Mock
    private SettlementRepository settlementRepositoryMock;

    @Autowired
    private SettlementMapper settlementMapper;

    @Mock
    private SettlementService settlementServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SettlementSearchRepositoryMockConfiguration
     */
    @Autowired
    private SettlementSearchRepository mockSettlementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettlementMockMvc;

    private Settlement settlement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Settlement createEntity(EntityManager em) {
        Settlement settlement = new Settlement()
            .paymentNumber(DEFAULT_PAYMENT_NUMBER)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .calculationFile(DEFAULT_CALCULATION_FILE)
            .calculationFileContentType(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)
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
        settlement.setSettlementCurrency(settlementCurrency);
        // Add required entity
        PaymentCategory paymentCategory;
        if (TestUtil.findAll(em, PaymentCategory.class).isEmpty()) {
            paymentCategory = PaymentCategoryResourceIT.createEntity(em);
            em.persist(paymentCategory);
            em.flush();
        } else {
            paymentCategory = TestUtil.findAll(em, PaymentCategory.class).get(0);
        }
        settlement.setPaymentCategory(paymentCategory);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        settlement.setBiller(dealer);
        return settlement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Settlement createUpdatedEntity(EntityManager em) {
        Settlement settlement = new Settlement()
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .calculationFile(UPDATED_CALCULATION_FILE)
            .calculationFileContentType(UPDATED_CALCULATION_FILE_CONTENT_TYPE)
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
        settlement.setSettlementCurrency(settlementCurrency);
        // Add required entity
        PaymentCategory paymentCategory;
        if (TestUtil.findAll(em, PaymentCategory.class).isEmpty()) {
            paymentCategory = PaymentCategoryResourceIT.createUpdatedEntity(em);
            em.persist(paymentCategory);
            em.flush();
        } else {
            paymentCategory = TestUtil.findAll(em, PaymentCategory.class).get(0);
        }
        settlement.setPaymentCategory(paymentCategory);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        settlement.setBiller(dealer);
        return settlement;
    }

    @BeforeEach
    public void initTest() {
        settlement = createEntity(em);
    }

    @Test
    @Transactional
    void createSettlement() throws Exception {
        int databaseSizeBeforeCreate = settlementRepository.findAll().size();
        // Create the Settlement
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);
        restSettlementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlementDTO)))
            .andExpect(status().isCreated());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeCreate + 1);
        Settlement testSettlement = settlementList.get(settlementList.size() - 1);
        assertThat(testSettlement.getPaymentNumber()).isEqualTo(DEFAULT_PAYMENT_NUMBER);
        assertThat(testSettlement.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testSettlement.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testSettlement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSettlement.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testSettlement.getCalculationFile()).isEqualTo(DEFAULT_CALCULATION_FILE);
        assertThat(testSettlement.getCalculationFileContentType()).isEqualTo(DEFAULT_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testSettlement.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testSettlement.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
        assertThat(testSettlement.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(1)).save(testSettlement);
    }

    @Test
    @Transactional
    void createSettlementWithExistingId() throws Exception {
        // Create the Settlement with an existing ID
        settlement.setId(1L);
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);

        int databaseSizeBeforeCreate = settlementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettlementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeCreate);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(0)).save(settlement);
    }

    @Test
    @Transactional
    void getAllSettlements() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList
        restSettlementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlement.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].calculationFileContentType").value(hasItem(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].calculationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettlementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(settlementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettlementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(settlementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettlementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(settlementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettlementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(settlementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSettlement() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get the settlement
        restSettlementMockMvc
            .perform(get(ENTITY_API_URL_ID, settlement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(settlement.getId().intValue()))
            .andExpect(jsonPath("$.paymentNumber").value(DEFAULT_PAYMENT_NUMBER))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(sameNumber(DEFAULT_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.calculationFileContentType").value(DEFAULT_CALCULATION_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.calculationFile").value(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE)))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    void getSettlementsByIdFiltering() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        Long id = settlement.getId();

        defaultSettlementShouldBeFound("id.equals=" + id);
        defaultSettlementShouldNotBeFound("id.notEquals=" + id);

        defaultSettlementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSettlementShouldNotBeFound("id.greaterThan=" + id);

        defaultSettlementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSettlementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentNumber equals to DEFAULT_PAYMENT_NUMBER
        defaultSettlementShouldBeFound("paymentNumber.equals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the settlementList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultSettlementShouldNotBeFound("paymentNumber.equals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentNumber not equals to DEFAULT_PAYMENT_NUMBER
        defaultSettlementShouldNotBeFound("paymentNumber.notEquals=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the settlementList where paymentNumber not equals to UPDATED_PAYMENT_NUMBER
        defaultSettlementShouldBeFound("paymentNumber.notEquals=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentNumber in DEFAULT_PAYMENT_NUMBER or UPDATED_PAYMENT_NUMBER
        defaultSettlementShouldBeFound("paymentNumber.in=" + DEFAULT_PAYMENT_NUMBER + "," + UPDATED_PAYMENT_NUMBER);

        // Get all the settlementList where paymentNumber equals to UPDATED_PAYMENT_NUMBER
        defaultSettlementShouldNotBeFound("paymentNumber.in=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentNumber is not null
        defaultSettlementShouldBeFound("paymentNumber.specified=true");

        // Get all the settlementList where paymentNumber is null
        defaultSettlementShouldNotBeFound("paymentNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentNumberContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentNumber contains DEFAULT_PAYMENT_NUMBER
        defaultSettlementShouldBeFound("paymentNumber.contains=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the settlementList where paymentNumber contains UPDATED_PAYMENT_NUMBER
        defaultSettlementShouldNotBeFound("paymentNumber.contains=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentNumber does not contain DEFAULT_PAYMENT_NUMBER
        defaultSettlementShouldNotBeFound("paymentNumber.doesNotContain=" + DEFAULT_PAYMENT_NUMBER);

        // Get all the settlementList where paymentNumber does not contain UPDATED_PAYMENT_NUMBER
        defaultSettlementShouldBeFound("paymentNumber.doesNotContain=" + UPDATED_PAYMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultSettlementShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the settlementList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultSettlementShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultSettlementShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the settlementList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultSettlementShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultSettlementShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the settlementList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultSettlementShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate is not null
        defaultSettlementShouldBeFound("paymentDate.specified=true");

        // Get all the settlementList where paymentDate is null
        defaultSettlementShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultSettlementShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the settlementList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultSettlementShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultSettlementShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the settlementList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultSettlementShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultSettlementShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the settlementList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultSettlementShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultSettlementShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the settlementList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultSettlementShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount equals to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementShouldBeFound("paymentAmount.equals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultSettlementShouldNotBeFound("paymentAmount.equals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount not equals to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementShouldNotBeFound("paymentAmount.notEquals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementList where paymentAmount not equals to UPDATED_PAYMENT_AMOUNT
        defaultSettlementShouldBeFound("paymentAmount.notEquals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount in DEFAULT_PAYMENT_AMOUNT or UPDATED_PAYMENT_AMOUNT
        defaultSettlementShouldBeFound("paymentAmount.in=" + DEFAULT_PAYMENT_AMOUNT + "," + UPDATED_PAYMENT_AMOUNT);

        // Get all the settlementList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultSettlementShouldNotBeFound("paymentAmount.in=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount is not null
        defaultSettlementShouldBeFound("paymentAmount.specified=true");

        // Get all the settlementList where paymentAmount is null
        defaultSettlementShouldNotBeFound("paymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount is greater than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementShouldBeFound("paymentAmount.greaterThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementList where paymentAmount is greater than or equal to UPDATED_PAYMENT_AMOUNT
        defaultSettlementShouldNotBeFound("paymentAmount.greaterThanOrEqual=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount is less than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementShouldBeFound("paymentAmount.lessThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementList where paymentAmount is less than or equal to SMALLER_PAYMENT_AMOUNT
        defaultSettlementShouldNotBeFound("paymentAmount.lessThanOrEqual=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount is less than DEFAULT_PAYMENT_AMOUNT
        defaultSettlementShouldNotBeFound("paymentAmount.lessThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementList where paymentAmount is less than UPDATED_PAYMENT_AMOUNT
        defaultSettlementShouldBeFound("paymentAmount.lessThan=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where paymentAmount is greater than DEFAULT_PAYMENT_AMOUNT
        defaultSettlementShouldNotBeFound("paymentAmount.greaterThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementList where paymentAmount is greater than SMALLER_PAYMENT_AMOUNT
        defaultSettlementShouldBeFound("paymentAmount.greaterThan=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where description equals to DEFAULT_DESCRIPTION
        defaultSettlementShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the settlementList where description equals to UPDATED_DESCRIPTION
        defaultSettlementShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where description not equals to DEFAULT_DESCRIPTION
        defaultSettlementShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the settlementList where description not equals to UPDATED_DESCRIPTION
        defaultSettlementShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSettlementShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the settlementList where description equals to UPDATED_DESCRIPTION
        defaultSettlementShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where description is not null
        defaultSettlementShouldBeFound("description.specified=true");

        // Get all the settlementList where description is null
        defaultSettlementShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where description contains DEFAULT_DESCRIPTION
        defaultSettlementShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the settlementList where description contains UPDATED_DESCRIPTION
        defaultSettlementShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where description does not contain DEFAULT_DESCRIPTION
        defaultSettlementShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the settlementList where description does not contain UPDATED_DESCRIPTION
        defaultSettlementShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where notes equals to DEFAULT_NOTES
        defaultSettlementShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the settlementList where notes equals to UPDATED_NOTES
        defaultSettlementShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllSettlementsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where notes not equals to DEFAULT_NOTES
        defaultSettlementShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the settlementList where notes not equals to UPDATED_NOTES
        defaultSettlementShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllSettlementsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultSettlementShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the settlementList where notes equals to UPDATED_NOTES
        defaultSettlementShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllSettlementsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where notes is not null
        defaultSettlementShouldBeFound("notes.specified=true");

        // Get all the settlementList where notes is null
        defaultSettlementShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementsByNotesContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where notes contains DEFAULT_NOTES
        defaultSettlementShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the settlementList where notes contains UPDATED_NOTES
        defaultSettlementShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllSettlementsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where notes does not contain DEFAULT_NOTES
        defaultSettlementShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the settlementList where notes does not contain UPDATED_NOTES
        defaultSettlementShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllSettlementsByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the settlementList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where fileUploadToken is not null
        defaultSettlementShouldBeFound("fileUploadToken.specified=true");

        // Get all the settlementList where fileUploadToken is null
        defaultSettlementShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementsByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultSettlementShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultSettlementShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultSettlementShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultSettlementShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultSettlementShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the settlementList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultSettlementShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where compilationToken is not null
        defaultSettlementShouldBeFound("compilationToken.specified=true");

        // Get all the settlementList where compilationToken is null
        defaultSettlementShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementsByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultSettlementShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultSettlementShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        // Get all the settlementList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultSettlementShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultSettlementShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
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
        settlement.addPlaceholder(placeholder);
        settlementRepository.saveAndFlush(settlement);
        Long placeholderId = placeholder.getId();

        // Get all the settlementList where placeholder equals to placeholderId
        defaultSettlementShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the settlementList where placeholder equals to (placeholderId + 1)
        defaultSettlementShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
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
        settlement.setSettlementCurrency(settlementCurrency);
        settlementRepository.saveAndFlush(settlement);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the settlementList where settlementCurrency equals to settlementCurrencyId
        defaultSettlementShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the settlementList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultSettlementShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
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
        settlement.addPaymentLabel(paymentLabel);
        settlementRepository.saveAndFlush(settlement);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the settlementList where paymentLabel equals to paymentLabelId
        defaultSettlementShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the settlementList where paymentLabel equals to (paymentLabelId + 1)
        defaultSettlementShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
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
        settlement.setPaymentCategory(paymentCategory);
        settlementRepository.saveAndFlush(settlement);
        Long paymentCategoryId = paymentCategory.getId();

        // Get all the settlementList where paymentCategory equals to paymentCategoryId
        defaultSettlementShouldBeFound("paymentCategoryId.equals=" + paymentCategoryId);

        // Get all the settlementList where paymentCategory equals to (paymentCategoryId + 1)
        defaultSettlementShouldNotBeFound("paymentCategoryId.equals=" + (paymentCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsByGroupSettlementIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
        Settlement groupSettlement;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            groupSettlement = SettlementResourceIT.createEntity(em);
            em.persist(groupSettlement);
            em.flush();
        } else {
            groupSettlement = TestUtil.findAll(em, Settlement.class).get(0);
        }
        em.persist(groupSettlement);
        em.flush();
        settlement.setGroupSettlement(groupSettlement);
        settlementRepository.saveAndFlush(settlement);
        Long groupSettlementId = groupSettlement.getId();

        // Get all the settlementList where groupSettlement equals to groupSettlementId
        defaultSettlementShouldBeFound("groupSettlementId.equals=" + groupSettlementId);

        // Get all the settlementList where groupSettlement equals to (groupSettlementId + 1)
        defaultSettlementShouldNotBeFound("groupSettlementId.equals=" + (groupSettlementId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsByBillerIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
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
        settlement.setBiller(biller);
        settlementRepository.saveAndFlush(settlement);
        Long billerId = biller.getId();

        // Get all the settlementList where biller equals to billerId
        defaultSettlementShouldBeFound("billerId.equals=" + billerId);

        // Get all the settlementList where biller equals to (billerId + 1)
        defaultSettlementShouldNotBeFound("billerId.equals=" + (billerId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsByPaymentInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
        PaymentInvoice paymentInvoice;
        if (TestUtil.findAll(em, PaymentInvoice.class).isEmpty()) {
            paymentInvoice = PaymentInvoiceResourceIT.createEntity(em);
            em.persist(paymentInvoice);
            em.flush();
        } else {
            paymentInvoice = TestUtil.findAll(em, PaymentInvoice.class).get(0);
        }
        em.persist(paymentInvoice);
        em.flush();
        settlement.addPaymentInvoice(paymentInvoice);
        settlementRepository.saveAndFlush(settlement);
        Long paymentInvoiceId = paymentInvoice.getId();

        // Get all the settlementList where paymentInvoice equals to paymentInvoiceId
        defaultSettlementShouldBeFound("paymentInvoiceId.equals=" + paymentInvoiceId);

        // Get all the settlementList where paymentInvoice equals to (paymentInvoiceId + 1)
        defaultSettlementShouldNotBeFound("paymentInvoiceId.equals=" + (paymentInvoiceId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsBySignatoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
        Dealer signatories;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            signatories = DealerResourceIT.createEntity(em);
            em.persist(signatories);
            em.flush();
        } else {
            signatories = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(signatories);
        em.flush();
        settlement.addSignatories(signatories);
        settlementRepository.saveAndFlush(settlement);
        Long signatoriesId = signatories.getId();

        // Get all the settlementList where signatories equals to signatoriesId
        defaultSettlementShouldBeFound("signatoriesId.equals=" + signatoriesId);

        // Get all the settlementList where signatories equals to (signatoriesId + 1)
        defaultSettlementShouldNotBeFound("signatoriesId.equals=" + (signatoriesId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementsByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
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
        settlement.addBusinessDocument(businessDocument);
        settlementRepository.saveAndFlush(settlement);
        Long businessDocumentId = businessDocument.getId();

        // Get all the settlementList where businessDocument equals to businessDocumentId
        defaultSettlementShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the settlementList where businessDocument equals to (businessDocumentId + 1)
        defaultSettlementShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSettlementShouldBeFound(String filter) throws Exception {
        restSettlementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlement.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].calculationFileContentType").value(hasItem(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].calculationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));

        // Check, that the count call also returns 1
        restSettlementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSettlementShouldNotBeFound(String filter) throws Exception {
        restSettlementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSettlementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSettlement() throws Exception {
        // Get the settlement
        restSettlementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSettlement() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();

        // Update the settlement
        Settlement updatedSettlement = settlementRepository.findById(settlement.getId()).get();
        // Disconnect from session so that the updates on updatedSettlement are not directly saved in db
        em.detach(updatedSettlement);
        updatedSettlement
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .calculationFile(UPDATED_CALCULATION_FILE)
            .calculationFileContentType(UPDATED_CALCULATION_FILE_CONTENT_TYPE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS);
        SettlementDTO settlementDTO = settlementMapper.toDto(updatedSettlement);

        restSettlementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settlementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);
        Settlement testSettlement = settlementList.get(settlementList.size() - 1);
        assertThat(testSettlement.getPaymentNumber()).isEqualTo(UPDATED_PAYMENT_NUMBER);
        assertThat(testSettlement.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testSettlement.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testSettlement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlement.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testSettlement.getCalculationFile()).isEqualTo(UPDATED_CALCULATION_FILE);
        assertThat(testSettlement.getCalculationFileContentType()).isEqualTo(UPDATED_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testSettlement.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testSettlement.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
        assertThat(testSettlement.getRemarks()).isEqualTo(UPDATED_REMARKS);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository).save(testSettlement);
    }

    @Test
    @Transactional
    void putNonExistingSettlement() throws Exception {
        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();
        settlement.setId(count.incrementAndGet());

        // Create the Settlement
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settlementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(0)).save(settlement);
    }

    @Test
    @Transactional
    void putWithIdMismatchSettlement() throws Exception {
        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();
        settlement.setId(count.incrementAndGet());

        // Create the Settlement
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(0)).save(settlement);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSettlement() throws Exception {
        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();
        settlement.setId(count.incrementAndGet());

        // Create the Settlement
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settlementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(0)).save(settlement);
    }

    @Test
    @Transactional
    void partialUpdateSettlementWithPatch() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();

        // Update the settlement using partial update
        Settlement partialUpdatedSettlement = new Settlement();
        partialUpdatedSettlement.setId(settlement.getId());

        partialUpdatedSettlement.paymentNumber(UPDATED_PAYMENT_NUMBER).paymentDate(UPDATED_PAYMENT_DATE).description(UPDATED_DESCRIPTION);

        restSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlement))
            )
            .andExpect(status().isOk());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);
        Settlement testSettlement = settlementList.get(settlementList.size() - 1);
        assertThat(testSettlement.getPaymentNumber()).isEqualTo(UPDATED_PAYMENT_NUMBER);
        assertThat(testSettlement.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testSettlement.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testSettlement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlement.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testSettlement.getCalculationFile()).isEqualTo(DEFAULT_CALCULATION_FILE);
        assertThat(testSettlement.getCalculationFileContentType()).isEqualTo(DEFAULT_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testSettlement.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testSettlement.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
        assertThat(testSettlement.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateSettlementWithPatch() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();

        // Update the settlement using partial update
        Settlement partialUpdatedSettlement = new Settlement();
        partialUpdatedSettlement.setId(settlement.getId());

        partialUpdatedSettlement
            .paymentNumber(UPDATED_PAYMENT_NUMBER)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .calculationFile(UPDATED_CALCULATION_FILE)
            .calculationFileContentType(UPDATED_CALCULATION_FILE_CONTENT_TYPE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS);

        restSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlement))
            )
            .andExpect(status().isOk());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);
        Settlement testSettlement = settlementList.get(settlementList.size() - 1);
        assertThat(testSettlement.getPaymentNumber()).isEqualTo(UPDATED_PAYMENT_NUMBER);
        assertThat(testSettlement.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testSettlement.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testSettlement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlement.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testSettlement.getCalculationFile()).isEqualTo(UPDATED_CALCULATION_FILE);
        assertThat(testSettlement.getCalculationFileContentType()).isEqualTo(UPDATED_CALCULATION_FILE_CONTENT_TYPE);
        assertThat(testSettlement.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testSettlement.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
        assertThat(testSettlement.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingSettlement() throws Exception {
        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();
        settlement.setId(count.incrementAndGet());

        // Create the Settlement
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, settlementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(0)).save(settlement);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSettlement() throws Exception {
        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();
        settlement.setId(count.incrementAndGet());

        // Create the Settlement
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(0)).save(settlement);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSettlement() throws Exception {
        int databaseSizeBeforeUpdate = settlementRepository.findAll().size();
        settlement.setId(count.incrementAndGet());

        // Create the Settlement
        SettlementDTO settlementDTO = settlementMapper.toDto(settlement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(settlementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Settlement in the database
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(0)).save(settlement);
    }

    @Test
    @Transactional
    void deleteSettlement() throws Exception {
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);

        int databaseSizeBeforeDelete = settlementRepository.findAll().size();

        // Delete the settlement
        restSettlementMockMvc
            .perform(delete(ENTITY_API_URL_ID, settlement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Settlement> settlementList = settlementRepository.findAll();
        assertThat(settlementList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Settlement in Elasticsearch
        verify(mockSettlementSearchRepository, times(1)).deleteById(settlement.getId());
    }

    @Test
    @Transactional
    void searchSettlement() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        settlementRepository.saveAndFlush(settlement);
        when(mockSettlementSearchRepository.search("id:" + settlement.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(settlement), PageRequest.of(0, 1), 1));

        // Search the settlement
        restSettlementMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + settlement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlement.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentNumber").value(hasItem(DEFAULT_PAYMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].calculationFileContentType").value(hasItem(DEFAULT_CALCULATION_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].calculationFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CALCULATION_FILE))))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }
}
