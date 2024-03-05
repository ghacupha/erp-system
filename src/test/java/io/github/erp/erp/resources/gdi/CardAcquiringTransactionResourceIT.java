package io.github.erp.erp.resources.gdi;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.repository.CardAcquiringTransactionRepository;
import io.github.erp.repository.search.CardAcquiringTransactionSearchRepository;
import io.github.erp.service.dto.CardAcquiringTransactionDTO;
import io.github.erp.service.mapper.CardAcquiringTransactionMapper;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the CardAcquiringTransactionResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CardAcquiringTransactionResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TERMINAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_TERMINAL_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_TRANSACTIONS = 0;
    private static final Integer UPDATED_NUMBER_OF_TRANSACTIONS = 1;
    private static final Integer SMALLER_NUMBER_OF_TRANSACTIONS = 0 - 1;

    private static final BigDecimal DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_VALUE_OF_TRANSACTIONS_IN_LCY = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/granular-data/card-acquiring-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/card-acquiring-transactions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardAcquiringTransactionRepository cardAcquiringTransactionRepository;

    @Autowired
    private CardAcquiringTransactionMapper cardAcquiringTransactionMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardAcquiringTransactionSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardAcquiringTransactionSearchRepository mockCardAcquiringTransactionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardAcquiringTransactionMockMvc;

    private CardAcquiringTransaction cardAcquiringTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardAcquiringTransaction createEntity(EntityManager em) {
        CardAcquiringTransaction cardAcquiringTransaction = new CardAcquiringTransaction()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .terminalId(DEFAULT_TERMINAL_ID)
            .numberOfTransactions(DEFAULT_NUMBER_OF_TRANSACTIONS)
            .valueOfTransactionsInLCY(DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        cardAcquiringTransaction.setBankCode(institutionCode);
        // Add required entity
        ChannelType channelType;
        if (TestUtil.findAll(em, ChannelType.class).isEmpty()) {
            channelType = ChannelTypeResourceIT.createEntity(em);
            em.persist(channelType);
            em.flush();
        } else {
            channelType = TestUtil.findAll(em, ChannelType.class).get(0);
        }
        cardAcquiringTransaction.setChannelType(channelType);
        // Add required entity
        CardBrandType cardBrandType;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrandType = CardBrandTypeResourceIT.createEntity(em);
            em.persist(cardBrandType);
            em.flush();
        } else {
            cardBrandType = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        cardAcquiringTransaction.setCardBrandType(cardBrandType);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        cardAcquiringTransaction.setCurrencyOfTransaction(isoCurrencyCode);
        // Add required entity
        CardCategoryType cardCategoryType;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategoryType = CardCategoryTypeResourceIT.createEntity(em);
            em.persist(cardCategoryType);
            em.flush();
        } else {
            cardCategoryType = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        cardAcquiringTransaction.setCardIssuerCategory(cardCategoryType);
        return cardAcquiringTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardAcquiringTransaction createUpdatedEntity(EntityManager em) {
        CardAcquiringTransaction cardAcquiringTransaction = new CardAcquiringTransaction()
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .numberOfTransactions(UPDATED_NUMBER_OF_TRANSACTIONS)
            .valueOfTransactionsInLCY(UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        cardAcquiringTransaction.setBankCode(institutionCode);
        // Add required entity
        ChannelType channelType;
        if (TestUtil.findAll(em, ChannelType.class).isEmpty()) {
            channelType = ChannelTypeResourceIT.createUpdatedEntity(em);
            em.persist(channelType);
            em.flush();
        } else {
            channelType = TestUtil.findAll(em, ChannelType.class).get(0);
        }
        cardAcquiringTransaction.setChannelType(channelType);
        // Add required entity
        CardBrandType cardBrandType;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrandType = CardBrandTypeResourceIT.createUpdatedEntity(em);
            em.persist(cardBrandType);
            em.flush();
        } else {
            cardBrandType = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        cardAcquiringTransaction.setCardBrandType(cardBrandType);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createUpdatedEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        cardAcquiringTransaction.setCurrencyOfTransaction(isoCurrencyCode);
        // Add required entity
        CardCategoryType cardCategoryType;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategoryType = CardCategoryTypeResourceIT.createUpdatedEntity(em);
            em.persist(cardCategoryType);
            em.flush();
        } else {
            cardCategoryType = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        cardAcquiringTransaction.setCardIssuerCategory(cardCategoryType);
        return cardAcquiringTransaction;
    }

    @BeforeEach
    public void initTest() {
        cardAcquiringTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createCardAcquiringTransaction() throws Exception {
        int databaseSizeBeforeCreate = cardAcquiringTransactionRepository.findAll().size();
        // Create the CardAcquiringTransaction
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);
        restCardAcquiringTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        CardAcquiringTransaction testCardAcquiringTransaction = cardAcquiringTransactionList.get(cardAcquiringTransactionList.size() - 1);
        assertThat(testCardAcquiringTransaction.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCardAcquiringTransaction.getTerminalId()).isEqualTo(DEFAULT_TERMINAL_ID);
        assertThat(testCardAcquiringTransaction.getNumberOfTransactions()).isEqualTo(DEFAULT_NUMBER_OF_TRANSACTIONS);
        assertThat(testCardAcquiringTransaction.getValueOfTransactionsInLCY()).isEqualByComparingTo(DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(1)).save(testCardAcquiringTransaction);
    }

    @Test
    @Transactional
    void createCardAcquiringTransactionWithExistingId() throws Exception {
        // Create the CardAcquiringTransaction with an existing ID
        cardAcquiringTransaction.setId(1L);
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        int databaseSizeBeforeCreate = cardAcquiringTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardAcquiringTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(0)).save(cardAcquiringTransaction);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardAcquiringTransactionRepository.findAll().size();
        // set the field null
        cardAcquiringTransaction.setReportingDate(null);

        // Create the CardAcquiringTransaction, which fails.
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        restCardAcquiringTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardAcquiringTransactionRepository.findAll().size();
        // set the field null
        cardAcquiringTransaction.setTerminalId(null);

        // Create the CardAcquiringTransaction, which fails.
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        restCardAcquiringTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfTransactionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardAcquiringTransactionRepository.findAll().size();
        // set the field null
        cardAcquiringTransaction.setNumberOfTransactions(null);

        // Create the CardAcquiringTransaction, which fails.
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        restCardAcquiringTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueOfTransactionsInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardAcquiringTransactionRepository.findAll().size();
        // set the field null
        cardAcquiringTransaction.setValueOfTransactionsInLCY(null);

        // Create the CardAcquiringTransaction, which fails.
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        restCardAcquiringTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactions() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList
        restCardAcquiringTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardAcquiringTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID)))
            .andExpect(jsonPath("$.[*].numberOfTransactions").value(hasItem(DEFAULT_NUMBER_OF_TRANSACTIONS)))
            .andExpect(jsonPath("$.[*].valueOfTransactionsInLCY").value(hasItem(sameNumber(DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY))));
    }

    @Test
    @Transactional
    void getCardAcquiringTransaction() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get the cardAcquiringTransaction
        restCardAcquiringTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, cardAcquiringTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardAcquiringTransaction.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.terminalId").value(DEFAULT_TERMINAL_ID))
            .andExpect(jsonPath("$.numberOfTransactions").value(DEFAULT_NUMBER_OF_TRANSACTIONS))
            .andExpect(jsonPath("$.valueOfTransactionsInLCY").value(sameNumber(DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY)));
    }

    @Test
    @Transactional
    void getCardAcquiringTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        Long id = cardAcquiringTransaction.getId();

        defaultCardAcquiringTransactionShouldBeFound("id.equals=" + id);
        defaultCardAcquiringTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultCardAcquiringTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardAcquiringTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultCardAcquiringTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardAcquiringTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardAcquiringTransactionList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardAcquiringTransactionList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the cardAcquiringTransactionList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate is not null
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.specified=true");

        // Get all the cardAcquiringTransactionList where reportingDate is null
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardAcquiringTransactionList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardAcquiringTransactionList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardAcquiringTransactionList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultCardAcquiringTransactionShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardAcquiringTransactionList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultCardAcquiringTransactionShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByTerminalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where terminalId equals to DEFAULT_TERMINAL_ID
        defaultCardAcquiringTransactionShouldBeFound("terminalId.equals=" + DEFAULT_TERMINAL_ID);

        // Get all the cardAcquiringTransactionList where terminalId equals to UPDATED_TERMINAL_ID
        defaultCardAcquiringTransactionShouldNotBeFound("terminalId.equals=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByTerminalIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where terminalId not equals to DEFAULT_TERMINAL_ID
        defaultCardAcquiringTransactionShouldNotBeFound("terminalId.notEquals=" + DEFAULT_TERMINAL_ID);

        // Get all the cardAcquiringTransactionList where terminalId not equals to UPDATED_TERMINAL_ID
        defaultCardAcquiringTransactionShouldBeFound("terminalId.notEquals=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByTerminalIdIsInShouldWork() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where terminalId in DEFAULT_TERMINAL_ID or UPDATED_TERMINAL_ID
        defaultCardAcquiringTransactionShouldBeFound("terminalId.in=" + DEFAULT_TERMINAL_ID + "," + UPDATED_TERMINAL_ID);

        // Get all the cardAcquiringTransactionList where terminalId equals to UPDATED_TERMINAL_ID
        defaultCardAcquiringTransactionShouldNotBeFound("terminalId.in=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByTerminalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where terminalId is not null
        defaultCardAcquiringTransactionShouldBeFound("terminalId.specified=true");

        // Get all the cardAcquiringTransactionList where terminalId is null
        defaultCardAcquiringTransactionShouldNotBeFound("terminalId.specified=false");
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByTerminalIdContainsSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where terminalId contains DEFAULT_TERMINAL_ID
        defaultCardAcquiringTransactionShouldBeFound("terminalId.contains=" + DEFAULT_TERMINAL_ID);

        // Get all the cardAcquiringTransactionList where terminalId contains UPDATED_TERMINAL_ID
        defaultCardAcquiringTransactionShouldNotBeFound("terminalId.contains=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByTerminalIdNotContainsSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where terminalId does not contain DEFAULT_TERMINAL_ID
        defaultCardAcquiringTransactionShouldNotBeFound("terminalId.doesNotContain=" + DEFAULT_TERMINAL_ID);

        // Get all the cardAcquiringTransactionList where terminalId does not contain UPDATED_TERMINAL_ID
        defaultCardAcquiringTransactionShouldBeFound("terminalId.doesNotContain=" + UPDATED_TERMINAL_ID);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions equals to DEFAULT_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldBeFound("numberOfTransactions.equals=" + DEFAULT_NUMBER_OF_TRANSACTIONS);

        // Get all the cardAcquiringTransactionList where numberOfTransactions equals to UPDATED_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.equals=" + UPDATED_NUMBER_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions not equals to DEFAULT_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.notEquals=" + DEFAULT_NUMBER_OF_TRANSACTIONS);

        // Get all the cardAcquiringTransactionList where numberOfTransactions not equals to UPDATED_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldBeFound("numberOfTransactions.notEquals=" + UPDATED_NUMBER_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsInShouldWork() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions in DEFAULT_NUMBER_OF_TRANSACTIONS or UPDATED_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldBeFound(
            "numberOfTransactions.in=" + DEFAULT_NUMBER_OF_TRANSACTIONS + "," + UPDATED_NUMBER_OF_TRANSACTIONS
        );

        // Get all the cardAcquiringTransactionList where numberOfTransactions equals to UPDATED_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.in=" + UPDATED_NUMBER_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is not null
        defaultCardAcquiringTransactionShouldBeFound("numberOfTransactions.specified=true");

        // Get all the cardAcquiringTransactionList where numberOfTransactions is null
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.specified=false");
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is greater than or equal to DEFAULT_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldBeFound("numberOfTransactions.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_TRANSACTIONS);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is greater than or equal to UPDATED_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.greaterThanOrEqual=" + UPDATED_NUMBER_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is less than or equal to DEFAULT_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldBeFound("numberOfTransactions.lessThanOrEqual=" + DEFAULT_NUMBER_OF_TRANSACTIONS);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is less than or equal to SMALLER_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.lessThanOrEqual=" + SMALLER_NUMBER_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsLessThanSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is less than DEFAULT_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.lessThan=" + DEFAULT_NUMBER_OF_TRANSACTIONS);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is less than UPDATED_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldBeFound("numberOfTransactions.lessThan=" + UPDATED_NUMBER_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByNumberOfTransactionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is greater than DEFAULT_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldNotBeFound("numberOfTransactions.greaterThan=" + DEFAULT_NUMBER_OF_TRANSACTIONS);

        // Get all the cardAcquiringTransactionList where numberOfTransactions is greater than SMALLER_NUMBER_OF_TRANSACTIONS
        defaultCardAcquiringTransactionShouldBeFound("numberOfTransactions.greaterThan=" + SMALLER_NUMBER_OF_TRANSACTIONS);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY equals to DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldBeFound("valueOfTransactionsInLCY.equals=" + DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY equals to UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldNotBeFound("valueOfTransactionsInLCY.equals=" + UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY not equals to DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldNotBeFound("valueOfTransactionsInLCY.notEquals=" + DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY not equals to UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldBeFound("valueOfTransactionsInLCY.notEquals=" + UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY in DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY or UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldBeFound(
            "valueOfTransactionsInLCY.in=" + DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY + "," + UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        );

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY equals to UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldNotBeFound("valueOfTransactionsInLCY.in=" + UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is not null
        defaultCardAcquiringTransactionShouldBeFound("valueOfTransactionsInLCY.specified=true");

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is null
        defaultCardAcquiringTransactionShouldNotBeFound("valueOfTransactionsInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is greater than or equal to DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldBeFound("valueOfTransactionsInLCY.greaterThanOrEqual=" + DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is greater than or equal to UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldNotBeFound(
            "valueOfTransactionsInLCY.greaterThanOrEqual=" + UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is less than or equal to DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldBeFound("valueOfTransactionsInLCY.lessThanOrEqual=" + DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is less than or equal to SMALLER_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldNotBeFound("valueOfTransactionsInLCY.lessThanOrEqual=" + SMALLER_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is less than DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldNotBeFound("valueOfTransactionsInLCY.lessThan=" + DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is less than UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldBeFound("valueOfTransactionsInLCY.lessThan=" + UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByValueOfTransactionsInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is greater than DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldNotBeFound("valueOfTransactionsInLCY.greaterThan=" + DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Get all the cardAcquiringTransactionList where valueOfTransactionsInLCY is greater than SMALLER_VALUE_OF_TRANSACTIONS_IN_LCY
        defaultCardAcquiringTransactionShouldBeFound("valueOfTransactionsInLCY.greaterThan=" + SMALLER_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        InstitutionCode bankCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            bankCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(bankCode);
            em.flush();
        } else {
            bankCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        em.persist(bankCode);
        em.flush();
        cardAcquiringTransaction.setBankCode(bankCode);
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        Long bankCodeId = bankCode.getId();

        // Get all the cardAcquiringTransactionList where bankCode equals to bankCodeId
        defaultCardAcquiringTransactionShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the cardAcquiringTransactionList where bankCode equals to (bankCodeId + 1)
        defaultCardAcquiringTransactionShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByChannelTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        ChannelType channelType;
        if (TestUtil.findAll(em, ChannelType.class).isEmpty()) {
            channelType = ChannelTypeResourceIT.createEntity(em);
            em.persist(channelType);
            em.flush();
        } else {
            channelType = TestUtil.findAll(em, ChannelType.class).get(0);
        }
        em.persist(channelType);
        em.flush();
        cardAcquiringTransaction.setChannelType(channelType);
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        Long channelTypeId = channelType.getId();

        // Get all the cardAcquiringTransactionList where channelType equals to channelTypeId
        defaultCardAcquiringTransactionShouldBeFound("channelTypeId.equals=" + channelTypeId);

        // Get all the cardAcquiringTransactionList where channelType equals to (channelTypeId + 1)
        defaultCardAcquiringTransactionShouldNotBeFound("channelTypeId.equals=" + (channelTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByCardBrandTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        CardBrandType cardBrandType;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrandType = CardBrandTypeResourceIT.createEntity(em);
            em.persist(cardBrandType);
            em.flush();
        } else {
            cardBrandType = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        em.persist(cardBrandType);
        em.flush();
        cardAcquiringTransaction.setCardBrandType(cardBrandType);
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        Long cardBrandTypeId = cardBrandType.getId();

        // Get all the cardAcquiringTransactionList where cardBrandType equals to cardBrandTypeId
        defaultCardAcquiringTransactionShouldBeFound("cardBrandTypeId.equals=" + cardBrandTypeId);

        // Get all the cardAcquiringTransactionList where cardBrandType equals to (cardBrandTypeId + 1)
        defaultCardAcquiringTransactionShouldNotBeFound("cardBrandTypeId.equals=" + (cardBrandTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByCurrencyOfTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        IsoCurrencyCode currencyOfTransaction;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            currencyOfTransaction = IsoCurrencyCodeResourceIT.createEntity(em);
            em.persist(currencyOfTransaction);
            em.flush();
        } else {
            currencyOfTransaction = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        em.persist(currencyOfTransaction);
        em.flush();
        cardAcquiringTransaction.setCurrencyOfTransaction(currencyOfTransaction);
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        Long currencyOfTransactionId = currencyOfTransaction.getId();

        // Get all the cardAcquiringTransactionList where currencyOfTransaction equals to currencyOfTransactionId
        defaultCardAcquiringTransactionShouldBeFound("currencyOfTransactionId.equals=" + currencyOfTransactionId);

        // Get all the cardAcquiringTransactionList where currencyOfTransaction equals to (currencyOfTransactionId + 1)
        defaultCardAcquiringTransactionShouldNotBeFound("currencyOfTransactionId.equals=" + (currencyOfTransactionId + 1));
    }

    @Test
    @Transactional
    void getAllCardAcquiringTransactionsByCardIssuerCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        CardCategoryType cardIssuerCategory;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardIssuerCategory = CardCategoryTypeResourceIT.createEntity(em);
            em.persist(cardIssuerCategory);
            em.flush();
        } else {
            cardIssuerCategory = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        em.persist(cardIssuerCategory);
        em.flush();
        cardAcquiringTransaction.setCardIssuerCategory(cardIssuerCategory);
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        Long cardIssuerCategoryId = cardIssuerCategory.getId();

        // Get all the cardAcquiringTransactionList where cardIssuerCategory equals to cardIssuerCategoryId
        defaultCardAcquiringTransactionShouldBeFound("cardIssuerCategoryId.equals=" + cardIssuerCategoryId);

        // Get all the cardAcquiringTransactionList where cardIssuerCategory equals to (cardIssuerCategoryId + 1)
        defaultCardAcquiringTransactionShouldNotBeFound("cardIssuerCategoryId.equals=" + (cardIssuerCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardAcquiringTransactionShouldBeFound(String filter) throws Exception {
        restCardAcquiringTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardAcquiringTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID)))
            .andExpect(jsonPath("$.[*].numberOfTransactions").value(hasItem(DEFAULT_NUMBER_OF_TRANSACTIONS)))
            .andExpect(jsonPath("$.[*].valueOfTransactionsInLCY").value(hasItem(sameNumber(DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY))));

        // Check, that the count call also returns 1
        restCardAcquiringTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardAcquiringTransactionShouldNotBeFound(String filter) throws Exception {
        restCardAcquiringTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardAcquiringTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardAcquiringTransaction() throws Exception {
        // Get the cardAcquiringTransaction
        restCardAcquiringTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardAcquiringTransaction() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();

        // Update the cardAcquiringTransaction
        CardAcquiringTransaction updatedCardAcquiringTransaction = cardAcquiringTransactionRepository
            .findById(cardAcquiringTransaction.getId())
            .get();
        // Disconnect from session so that the updates on updatedCardAcquiringTransaction are not directly saved in db
        em.detach(updatedCardAcquiringTransaction);
        updatedCardAcquiringTransaction
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .numberOfTransactions(UPDATED_NUMBER_OF_TRANSACTIONS)
            .valueOfTransactionsInLCY(UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(updatedCardAcquiringTransaction);

        restCardAcquiringTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardAcquiringTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);
        CardAcquiringTransaction testCardAcquiringTransaction = cardAcquiringTransactionList.get(cardAcquiringTransactionList.size() - 1);
        assertThat(testCardAcquiringTransaction.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardAcquiringTransaction.getTerminalId()).isEqualTo(UPDATED_TERMINAL_ID);
        assertThat(testCardAcquiringTransaction.getNumberOfTransactions()).isEqualTo(UPDATED_NUMBER_OF_TRANSACTIONS);
        assertThat(testCardAcquiringTransaction.getValueOfTransactionsInLCY()).isEqualTo(UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository).save(testCardAcquiringTransaction);
    }

    @Test
    @Transactional
    void putNonExistingCardAcquiringTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();
        cardAcquiringTransaction.setId(count.incrementAndGet());

        // Create the CardAcquiringTransaction
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardAcquiringTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardAcquiringTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(0)).save(cardAcquiringTransaction);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardAcquiringTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();
        cardAcquiringTransaction.setId(count.incrementAndGet());

        // Create the CardAcquiringTransaction
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardAcquiringTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(0)).save(cardAcquiringTransaction);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardAcquiringTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();
        cardAcquiringTransaction.setId(count.incrementAndGet());

        // Create the CardAcquiringTransaction
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardAcquiringTransactionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(0)).save(cardAcquiringTransaction);
    }

    @Test
    @Transactional
    void partialUpdateCardAcquiringTransactionWithPatch() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();

        // Update the cardAcquiringTransaction using partial update
        CardAcquiringTransaction partialUpdatedCardAcquiringTransaction = new CardAcquiringTransaction();
        partialUpdatedCardAcquiringTransaction.setId(cardAcquiringTransaction.getId());

        partialUpdatedCardAcquiringTransaction
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .numberOfTransactions(UPDATED_NUMBER_OF_TRANSACTIONS)
            .valueOfTransactionsInLCY(UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);

        restCardAcquiringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardAcquiringTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardAcquiringTransaction))
            )
            .andExpect(status().isOk());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);
        CardAcquiringTransaction testCardAcquiringTransaction = cardAcquiringTransactionList.get(cardAcquiringTransactionList.size() - 1);
        assertThat(testCardAcquiringTransaction.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardAcquiringTransaction.getTerminalId()).isEqualTo(UPDATED_TERMINAL_ID);
        assertThat(testCardAcquiringTransaction.getNumberOfTransactions()).isEqualTo(UPDATED_NUMBER_OF_TRANSACTIONS);
        assertThat(testCardAcquiringTransaction.getValueOfTransactionsInLCY()).isEqualByComparingTo(UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void fullUpdateCardAcquiringTransactionWithPatch() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();

        // Update the cardAcquiringTransaction using partial update
        CardAcquiringTransaction partialUpdatedCardAcquiringTransaction = new CardAcquiringTransaction();
        partialUpdatedCardAcquiringTransaction.setId(cardAcquiringTransaction.getId());

        partialUpdatedCardAcquiringTransaction
            .reportingDate(UPDATED_REPORTING_DATE)
            .terminalId(UPDATED_TERMINAL_ID)
            .numberOfTransactions(UPDATED_NUMBER_OF_TRANSACTIONS)
            .valueOfTransactionsInLCY(UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);

        restCardAcquiringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardAcquiringTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardAcquiringTransaction))
            )
            .andExpect(status().isOk());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);
        CardAcquiringTransaction testCardAcquiringTransaction = cardAcquiringTransactionList.get(cardAcquiringTransactionList.size() - 1);
        assertThat(testCardAcquiringTransaction.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardAcquiringTransaction.getTerminalId()).isEqualTo(UPDATED_TERMINAL_ID);
        assertThat(testCardAcquiringTransaction.getNumberOfTransactions()).isEqualTo(UPDATED_NUMBER_OF_TRANSACTIONS);
        assertThat(testCardAcquiringTransaction.getValueOfTransactionsInLCY()).isEqualByComparingTo(UPDATED_VALUE_OF_TRANSACTIONS_IN_LCY);
    }

    @Test
    @Transactional
    void patchNonExistingCardAcquiringTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();
        cardAcquiringTransaction.setId(count.incrementAndGet());

        // Create the CardAcquiringTransaction
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardAcquiringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardAcquiringTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(0)).save(cardAcquiringTransaction);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardAcquiringTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();
        cardAcquiringTransaction.setId(count.incrementAndGet());

        // Create the CardAcquiringTransaction
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardAcquiringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(0)).save(cardAcquiringTransaction);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardAcquiringTransaction() throws Exception {
        int databaseSizeBeforeUpdate = cardAcquiringTransactionRepository.findAll().size();
        cardAcquiringTransaction.setId(count.incrementAndGet());

        // Create the CardAcquiringTransaction
        CardAcquiringTransactionDTO cardAcquiringTransactionDTO = cardAcquiringTransactionMapper.toDto(cardAcquiringTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardAcquiringTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardAcquiringTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardAcquiringTransaction in the database
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(0)).save(cardAcquiringTransaction);
    }

    @Test
    @Transactional
    void deleteCardAcquiringTransaction() throws Exception {
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);

        int databaseSizeBeforeDelete = cardAcquiringTransactionRepository.findAll().size();

        // Delete the cardAcquiringTransaction
        restCardAcquiringTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardAcquiringTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardAcquiringTransaction> cardAcquiringTransactionList = cardAcquiringTransactionRepository.findAll();
        assertThat(cardAcquiringTransactionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardAcquiringTransaction in Elasticsearch
        verify(mockCardAcquiringTransactionSearchRepository, times(1)).deleteById(cardAcquiringTransaction.getId());
    }

    @Test
    @Transactional
    void searchCardAcquiringTransaction() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardAcquiringTransactionRepository.saveAndFlush(cardAcquiringTransaction);
        when(mockCardAcquiringTransactionSearchRepository.search("id:" + cardAcquiringTransaction.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardAcquiringTransaction), PageRequest.of(0, 1), 1));

        // Search the cardAcquiringTransaction
        restCardAcquiringTransactionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardAcquiringTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardAcquiringTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminalId").value(hasItem(DEFAULT_TERMINAL_ID)))
            .andExpect(jsonPath("$.[*].numberOfTransactions").value(hasItem(DEFAULT_NUMBER_OF_TRANSACTIONS)))
            .andExpect(jsonPath("$.[*].valueOfTransactionsInLCY").value(hasItem(sameNumber(DEFAULT_VALUE_OF_TRANSACTIONS_IN_LCY))));
    }
}
