package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.repository.CardUsageInformationRepository;
import io.github.erp.repository.search.CardUsageInformationSearchRepository;
import io.github.erp.service.dto.CardUsageInformationDTO;
import io.github.erp.service.mapper.CardUsageInformationMapper;
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
 * Integration tests for the CardUsageInformationResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CardUsageInformationResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS = 0;
    private static final Integer UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS = 1;
    private static final Integer SMALLER_TOTAL_NUMBER_OF_LIVE_CARDS = 0 - 1;

    private static final Integer DEFAULT_TOTAL_ACTIVE_CARDS = 0;
    private static final Integer UPDATED_TOTAL_ACTIVE_CARDS = 1;
    private static final Integer SMALLER_TOTAL_ACTIVE_CARDS = 0 - 1;

    private static final Integer DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE = 0;
    private static final Integer UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE = 1;
    private static final Integer SMALLER_TOTAL_NUMBER_OF_TRANSACTIONS_DONE = 0 - 1;

    private static final BigDecimal DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/granular-data/card-usage-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/card-usage-informations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardUsageInformationRepository cardUsageInformationRepository;

    @Autowired
    private CardUsageInformationMapper cardUsageInformationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardUsageInformationSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardUsageInformationSearchRepository mockCardUsageInformationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardUsageInformationMockMvc;

    private CardUsageInformation cardUsageInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardUsageInformation createEntity(EntityManager em) {
        CardUsageInformation cardUsageInformation = new CardUsageInformation()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .totalNumberOfLiveCards(DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS)
            .totalActiveCards(DEFAULT_TOTAL_ACTIVE_CARDS)
            .totalNumberOfTransactionsDone(DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE)
            .totalValueOfTransactionsDoneInLCY(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        cardUsageInformation.setBankCode(institutionCode);
        // Add required entity
        CardTypes cardTypes;
        if (TestUtil.findAll(em, CardTypes.class).isEmpty()) {
            cardTypes = CardTypesResourceIT.createEntity(em);
            em.persist(cardTypes);
            em.flush();
        } else {
            cardTypes = TestUtil.findAll(em, CardTypes.class).get(0);
        }
        cardUsageInformation.setCardType(cardTypes);
        // Add required entity
        CardBrandType cardBrandType;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrandType = CardBrandTypeResourceIT.createEntity(em);
            em.persist(cardBrandType);
            em.flush();
        } else {
            cardBrandType = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        cardUsageInformation.setCardBrand(cardBrandType);
        // Add required entity
        CardCategoryType cardCategoryType;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategoryType = CardCategoryTypeResourceIT.createEntity(em);
            em.persist(cardCategoryType);
            em.flush();
        } else {
            cardCategoryType = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        cardUsageInformation.setCardCategoryType(cardCategoryType);
        // Add required entity
        BankTransactionType bankTransactionType;
        if (TestUtil.findAll(em, BankTransactionType.class).isEmpty()) {
            bankTransactionType = BankTransactionTypeResourceIT.createEntity(em);
            em.persist(bankTransactionType);
            em.flush();
        } else {
            bankTransactionType = TestUtil.findAll(em, BankTransactionType.class).get(0);
        }
        cardUsageInformation.setTransactionType(bankTransactionType);
        // Add required entity
        ChannelType channelType;
        if (TestUtil.findAll(em, ChannelType.class).isEmpty()) {
            channelType = ChannelTypeResourceIT.createEntity(em);
            em.persist(channelType);
            em.flush();
        } else {
            channelType = TestUtil.findAll(em, ChannelType.class).get(0);
        }
        cardUsageInformation.setChannelType(channelType);
        // Add required entity
        CardState cardState;
        if (TestUtil.findAll(em, CardState.class).isEmpty()) {
            cardState = CardStateResourceIT.createEntity(em);
            em.persist(cardState);
            em.flush();
        } else {
            cardState = TestUtil.findAll(em, CardState.class).get(0);
        }
        cardUsageInformation.setCardState(cardState);
        return cardUsageInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardUsageInformation createUpdatedEntity(EntityManager em) {
        CardUsageInformation cardUsageInformation = new CardUsageInformation()
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfLiveCards(UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS)
            .totalActiveCards(UPDATED_TOTAL_ACTIVE_CARDS)
            .totalNumberOfTransactionsDone(UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE)
            .totalValueOfTransactionsDoneInLCY(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        cardUsageInformation.setBankCode(institutionCode);
        // Add required entity
        CardTypes cardTypes;
        if (TestUtil.findAll(em, CardTypes.class).isEmpty()) {
            cardTypes = CardTypesResourceIT.createUpdatedEntity(em);
            em.persist(cardTypes);
            em.flush();
        } else {
            cardTypes = TestUtil.findAll(em, CardTypes.class).get(0);
        }
        cardUsageInformation.setCardType(cardTypes);
        // Add required entity
        CardBrandType cardBrandType;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrandType = CardBrandTypeResourceIT.createUpdatedEntity(em);
            em.persist(cardBrandType);
            em.flush();
        } else {
            cardBrandType = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        cardUsageInformation.setCardBrand(cardBrandType);
        // Add required entity
        CardCategoryType cardCategoryType;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategoryType = CardCategoryTypeResourceIT.createUpdatedEntity(em);
            em.persist(cardCategoryType);
            em.flush();
        } else {
            cardCategoryType = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        cardUsageInformation.setCardCategoryType(cardCategoryType);
        // Add required entity
        BankTransactionType bankTransactionType;
        if (TestUtil.findAll(em, BankTransactionType.class).isEmpty()) {
            bankTransactionType = BankTransactionTypeResourceIT.createUpdatedEntity(em);
            em.persist(bankTransactionType);
            em.flush();
        } else {
            bankTransactionType = TestUtil.findAll(em, BankTransactionType.class).get(0);
        }
        cardUsageInformation.setTransactionType(bankTransactionType);
        // Add required entity
        ChannelType channelType;
        if (TestUtil.findAll(em, ChannelType.class).isEmpty()) {
            channelType = ChannelTypeResourceIT.createUpdatedEntity(em);
            em.persist(channelType);
            em.flush();
        } else {
            channelType = TestUtil.findAll(em, ChannelType.class).get(0);
        }
        cardUsageInformation.setChannelType(channelType);
        // Add required entity
        CardState cardState;
        if (TestUtil.findAll(em, CardState.class).isEmpty()) {
            cardState = CardStateResourceIT.createUpdatedEntity(em);
            em.persist(cardState);
            em.flush();
        } else {
            cardState = TestUtil.findAll(em, CardState.class).get(0);
        }
        cardUsageInformation.setCardState(cardState);
        return cardUsageInformation;
    }

    @BeforeEach
    public void initTest() {
        cardUsageInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createCardUsageInformation() throws Exception {
        int databaseSizeBeforeCreate = cardUsageInformationRepository.findAll().size();
        // Create the CardUsageInformation
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);
        restCardUsageInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeCreate + 1);
        CardUsageInformation testCardUsageInformation = cardUsageInformationList.get(cardUsageInformationList.size() - 1);
        assertThat(testCardUsageInformation.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCardUsageInformation.getTotalNumberOfLiveCards()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalActiveCards()).isEqualTo(DEFAULT_TOTAL_ACTIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalNumberOfTransactionsDone()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
        assertThat(testCardUsageInformation.getTotalValueOfTransactionsDoneInLCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(1)).save(testCardUsageInformation);
    }

    @Test
    @Transactional
    void createCardUsageInformationWithExistingId() throws Exception {
        // Create the CardUsageInformation with an existing ID
        cardUsageInformation.setId(1L);
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        int databaseSizeBeforeCreate = cardUsageInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardUsageInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(0)).save(cardUsageInformation);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardUsageInformationRepository.findAll().size();
        // set the field null
        cardUsageInformation.setReportingDate(null);

        // Create the CardUsageInformation, which fails.
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        restCardUsageInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNumberOfLiveCardsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardUsageInformationRepository.findAll().size();
        // set the field null
        cardUsageInformation.setTotalNumberOfLiveCards(null);

        // Create the CardUsageInformation, which fails.
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        restCardUsageInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalActiveCardsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardUsageInformationRepository.findAll().size();
        // set the field null
        cardUsageInformation.setTotalActiveCards(null);

        // Create the CardUsageInformation, which fails.
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        restCardUsageInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNumberOfTransactionsDoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardUsageInformationRepository.findAll().size();
        // set the field null
        cardUsageInformation.setTotalNumberOfTransactionsDone(null);

        // Create the CardUsageInformation, which fails.
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        restCardUsageInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalValueOfTransactionsDoneInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardUsageInformationRepository.findAll().size();
        // set the field null
        cardUsageInformation.setTotalValueOfTransactionsDoneInLCY(null);

        // Create the CardUsageInformation, which fails.
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        restCardUsageInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardUsageInformations() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList
        restCardUsageInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardUsageInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfLiveCards").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS)))
            .andExpect(jsonPath("$.[*].totalActiveCards").value(hasItem(DEFAULT_TOTAL_ACTIVE_CARDS)))
            .andExpect(jsonPath("$.[*].totalNumberOfTransactionsDone").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE)))
            .andExpect(
                jsonPath("$.[*].totalValueOfTransactionsDoneInLCY")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY)))
            );
    }

    @Test
    @Transactional
    void getCardUsageInformation() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get the cardUsageInformation
        restCardUsageInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, cardUsageInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardUsageInformation.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.totalNumberOfLiveCards").value(DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS))
            .andExpect(jsonPath("$.totalActiveCards").value(DEFAULT_TOTAL_ACTIVE_CARDS))
            .andExpect(jsonPath("$.totalNumberOfTransactionsDone").value(DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE))
            .andExpect(jsonPath("$.totalValueOfTransactionsDoneInLCY").value(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY)));
    }

    @Test
    @Transactional
    void getCardUsageInformationsByIdFiltering() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        Long id = cardUsageInformation.getId();

        defaultCardUsageInformationShouldBeFound("id.equals=" + id);
        defaultCardUsageInformationShouldNotBeFound("id.notEquals=" + id);

        defaultCardUsageInformationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardUsageInformationShouldNotBeFound("id.greaterThan=" + id);

        defaultCardUsageInformationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardUsageInformationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultCardUsageInformationShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardUsageInformationList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardUsageInformationShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultCardUsageInformationShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardUsageInformationList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultCardUsageInformationShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultCardUsageInformationShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the cardUsageInformationList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardUsageInformationShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate is not null
        defaultCardUsageInformationShouldBeFound("reportingDate.specified=true");

        // Get all the cardUsageInformationList where reportingDate is null
        defaultCardUsageInformationShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultCardUsageInformationShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardUsageInformationList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultCardUsageInformationShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultCardUsageInformationShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardUsageInformationList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultCardUsageInformationShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultCardUsageInformationShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardUsageInformationList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultCardUsageInformationShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultCardUsageInformationShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardUsageInformationList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultCardUsageInformationShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards equals to DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalNumberOfLiveCards.equals=" + DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards equals to UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.equals=" + UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards not equals to DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.notEquals=" + DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards not equals to UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalNumberOfLiveCards.notEquals=" + UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsInShouldWork() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards in DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS or UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldBeFound(
            "totalNumberOfLiveCards.in=" + DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS + "," + UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS
        );

        // Get all the cardUsageInformationList where totalNumberOfLiveCards equals to UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.in=" + UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is not null
        defaultCardUsageInformationShouldBeFound("totalNumberOfLiveCards.specified=true");

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is null
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.specified=false");
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is greater than or equal to DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalNumberOfLiveCards.greaterThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is greater than or equal to UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.greaterThanOrEqual=" + UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is less than or equal to DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalNumberOfLiveCards.lessThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is less than or equal to SMALLER_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.lessThanOrEqual=" + SMALLER_TOTAL_NUMBER_OF_LIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsLessThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is less than DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.lessThan=" + DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is less than UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalNumberOfLiveCards.lessThan=" + UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfLiveCardsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is greater than DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfLiveCards.greaterThan=" + DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);

        // Get all the cardUsageInformationList where totalNumberOfLiveCards is greater than SMALLER_TOTAL_NUMBER_OF_LIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalNumberOfLiveCards.greaterThan=" + SMALLER_TOTAL_NUMBER_OF_LIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards equals to DEFAULT_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalActiveCards.equals=" + DEFAULT_TOTAL_ACTIVE_CARDS);

        // Get all the cardUsageInformationList where totalActiveCards equals to UPDATED_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.equals=" + UPDATED_TOTAL_ACTIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards not equals to DEFAULT_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.notEquals=" + DEFAULT_TOTAL_ACTIVE_CARDS);

        // Get all the cardUsageInformationList where totalActiveCards not equals to UPDATED_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalActiveCards.notEquals=" + UPDATED_TOTAL_ACTIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsInShouldWork() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards in DEFAULT_TOTAL_ACTIVE_CARDS or UPDATED_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalActiveCards.in=" + DEFAULT_TOTAL_ACTIVE_CARDS + "," + UPDATED_TOTAL_ACTIVE_CARDS);

        // Get all the cardUsageInformationList where totalActiveCards equals to UPDATED_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.in=" + UPDATED_TOTAL_ACTIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards is not null
        defaultCardUsageInformationShouldBeFound("totalActiveCards.specified=true");

        // Get all the cardUsageInformationList where totalActiveCards is null
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.specified=false");
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards is greater than or equal to DEFAULT_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalActiveCards.greaterThanOrEqual=" + DEFAULT_TOTAL_ACTIVE_CARDS);

        // Get all the cardUsageInformationList where totalActiveCards is greater than or equal to UPDATED_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.greaterThanOrEqual=" + UPDATED_TOTAL_ACTIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards is less than or equal to DEFAULT_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalActiveCards.lessThanOrEqual=" + DEFAULT_TOTAL_ACTIVE_CARDS);

        // Get all the cardUsageInformationList where totalActiveCards is less than or equal to SMALLER_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.lessThanOrEqual=" + SMALLER_TOTAL_ACTIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsLessThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards is less than DEFAULT_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.lessThan=" + DEFAULT_TOTAL_ACTIVE_CARDS);

        // Get all the cardUsageInformationList where totalActiveCards is less than UPDATED_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalActiveCards.lessThan=" + UPDATED_TOTAL_ACTIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalActiveCardsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalActiveCards is greater than DEFAULT_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldNotBeFound("totalActiveCards.greaterThan=" + DEFAULT_TOTAL_ACTIVE_CARDS);

        // Get all the cardUsageInformationList where totalActiveCards is greater than SMALLER_TOTAL_ACTIVE_CARDS
        defaultCardUsageInformationShouldBeFound("totalActiveCards.greaterThan=" + SMALLER_TOTAL_ACTIVE_CARDS);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone equals to DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldBeFound("totalNumberOfTransactionsDone.equals=" + DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone equals to UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfTransactionsDone.equals=" + UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone not equals to DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfTransactionsDone.notEquals=" + DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone not equals to UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldBeFound("totalNumberOfTransactionsDone.notEquals=" + UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsInShouldWork() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone in DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE or UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldBeFound(
            "totalNumberOfTransactionsDone.in=" +
            DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE +
            "," +
            UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        );

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone equals to UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfTransactionsDone.in=" + UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is not null
        defaultCardUsageInformationShouldBeFound("totalNumberOfTransactionsDone.specified=true");

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is null
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfTransactionsDone.specified=false");
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is greater than or equal to DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldBeFound(
            "totalNumberOfTransactionsDone.greaterThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        );

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is greater than or equal to UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldNotBeFound(
            "totalNumberOfTransactionsDone.greaterThanOrEqual=" + UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is less than or equal to DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldBeFound(
            "totalNumberOfTransactionsDone.lessThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        );

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is less than or equal to SMALLER_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldNotBeFound(
            "totalNumberOfTransactionsDone.lessThanOrEqual=" + SMALLER_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsLessThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is less than DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldNotBeFound("totalNumberOfTransactionsDone.lessThan=" + DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is less than UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldBeFound("totalNumberOfTransactionsDone.lessThan=" + UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalNumberOfTransactionsDoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is greater than DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldNotBeFound(
            "totalNumberOfTransactionsDone.greaterThan=" + DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        );

        // Get all the cardUsageInformationList where totalNumberOfTransactionsDone is greater than SMALLER_TOTAL_NUMBER_OF_TRANSACTIONS_DONE
        defaultCardUsageInformationShouldBeFound("totalNumberOfTransactionsDone.greaterThan=" + SMALLER_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY equals to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldBeFound(
            "totalValueOfTransactionsDoneInLCY.equals=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY equals to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldNotBeFound(
            "totalValueOfTransactionsDoneInLCY.equals=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY not equals to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldNotBeFound(
            "totalValueOfTransactionsDoneInLCY.notEquals=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY not equals to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldBeFound(
            "totalValueOfTransactionsDoneInLCY.notEquals=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY in DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY or UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldBeFound(
            "totalValueOfTransactionsDoneInLCY.in=" +
            DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY +
            "," +
            UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY equals to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldNotBeFound(
            "totalValueOfTransactionsDoneInLCY.in=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is not null
        defaultCardUsageInformationShouldBeFound("totalValueOfTransactionsDoneInLCY.specified=true");

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is null
        defaultCardUsageInformationShouldNotBeFound("totalValueOfTransactionsDoneInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is greater than or equal to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldBeFound(
            "totalValueOfTransactionsDoneInLCY.greaterThanOrEqual=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is greater than or equal to UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldNotBeFound(
            "totalValueOfTransactionsDoneInLCY.greaterThanOrEqual=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is less than or equal to DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldBeFound(
            "totalValueOfTransactionsDoneInLCY.lessThanOrEqual=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is less than or equal to SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldNotBeFound(
            "totalValueOfTransactionsDoneInLCY.lessThanOrEqual=" + SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is less than DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldNotBeFound(
            "totalValueOfTransactionsDoneInLCY.lessThan=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is less than UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldBeFound(
            "totalValueOfTransactionsDoneInLCY.lessThan=" + UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTotalValueOfTransactionsDoneInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is greater than DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldNotBeFound(
            "totalValueOfTransactionsDoneInLCY.greaterThan=" + DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );

        // Get all the cardUsageInformationList where totalValueOfTransactionsDoneInLCY is greater than SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        defaultCardUsageInformationShouldBeFound(
            "totalValueOfTransactionsDoneInLCY.greaterThan=" + SMALLER_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
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
        cardUsageInformation.setBankCode(bankCode);
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        Long bankCodeId = bankCode.getId();

        // Get all the cardUsageInformationList where bankCode equals to bankCodeId
        defaultCardUsageInformationShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the cardUsageInformationList where bankCode equals to (bankCodeId + 1)
        defaultCardUsageInformationShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByCardTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        CardTypes cardType;
        if (TestUtil.findAll(em, CardTypes.class).isEmpty()) {
            cardType = CardTypesResourceIT.createEntity(em);
            em.persist(cardType);
            em.flush();
        } else {
            cardType = TestUtil.findAll(em, CardTypes.class).get(0);
        }
        em.persist(cardType);
        em.flush();
        cardUsageInformation.setCardType(cardType);
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        Long cardTypeId = cardType.getId();

        // Get all the cardUsageInformationList where cardType equals to cardTypeId
        defaultCardUsageInformationShouldBeFound("cardTypeId.equals=" + cardTypeId);

        // Get all the cardUsageInformationList where cardType equals to (cardTypeId + 1)
        defaultCardUsageInformationShouldNotBeFound("cardTypeId.equals=" + (cardTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByCardBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        CardBrandType cardBrand;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrand = CardBrandTypeResourceIT.createEntity(em);
            em.persist(cardBrand);
            em.flush();
        } else {
            cardBrand = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        em.persist(cardBrand);
        em.flush();
        cardUsageInformation.setCardBrand(cardBrand);
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        Long cardBrandId = cardBrand.getId();

        // Get all the cardUsageInformationList where cardBrand equals to cardBrandId
        defaultCardUsageInformationShouldBeFound("cardBrandId.equals=" + cardBrandId);

        // Get all the cardUsageInformationList where cardBrand equals to (cardBrandId + 1)
        defaultCardUsageInformationShouldNotBeFound("cardBrandId.equals=" + (cardBrandId + 1));
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByCardCategoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        CardCategoryType cardCategoryType;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategoryType = CardCategoryTypeResourceIT.createEntity(em);
            em.persist(cardCategoryType);
            em.flush();
        } else {
            cardCategoryType = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        em.persist(cardCategoryType);
        em.flush();
        cardUsageInformation.setCardCategoryType(cardCategoryType);
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        Long cardCategoryTypeId = cardCategoryType.getId();

        // Get all the cardUsageInformationList where cardCategoryType equals to cardCategoryTypeId
        defaultCardUsageInformationShouldBeFound("cardCategoryTypeId.equals=" + cardCategoryTypeId);

        // Get all the cardUsageInformationList where cardCategoryType equals to (cardCategoryTypeId + 1)
        defaultCardUsageInformationShouldNotBeFound("cardCategoryTypeId.equals=" + (cardCategoryTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        BankTransactionType transactionType;
        if (TestUtil.findAll(em, BankTransactionType.class).isEmpty()) {
            transactionType = BankTransactionTypeResourceIT.createEntity(em);
            em.persist(transactionType);
            em.flush();
        } else {
            transactionType = TestUtil.findAll(em, BankTransactionType.class).get(0);
        }
        em.persist(transactionType);
        em.flush();
        cardUsageInformation.setTransactionType(transactionType);
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        Long transactionTypeId = transactionType.getId();

        // Get all the cardUsageInformationList where transactionType equals to transactionTypeId
        defaultCardUsageInformationShouldBeFound("transactionTypeId.equals=" + transactionTypeId);

        // Get all the cardUsageInformationList where transactionType equals to (transactionTypeId + 1)
        defaultCardUsageInformationShouldNotBeFound("transactionTypeId.equals=" + (transactionTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByChannelTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
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
        cardUsageInformation.setChannelType(channelType);
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        Long channelTypeId = channelType.getId();

        // Get all the cardUsageInformationList where channelType equals to channelTypeId
        defaultCardUsageInformationShouldBeFound("channelTypeId.equals=" + channelTypeId);

        // Get all the cardUsageInformationList where channelType equals to (channelTypeId + 1)
        defaultCardUsageInformationShouldNotBeFound("channelTypeId.equals=" + (channelTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardUsageInformationsByCardStateIsEqualToSomething() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        CardState cardState;
        if (TestUtil.findAll(em, CardState.class).isEmpty()) {
            cardState = CardStateResourceIT.createEntity(em);
            em.persist(cardState);
            em.flush();
        } else {
            cardState = TestUtil.findAll(em, CardState.class).get(0);
        }
        em.persist(cardState);
        em.flush();
        cardUsageInformation.setCardState(cardState);
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        Long cardStateId = cardState.getId();

        // Get all the cardUsageInformationList where cardState equals to cardStateId
        defaultCardUsageInformationShouldBeFound("cardStateId.equals=" + cardStateId);

        // Get all the cardUsageInformationList where cardState equals to (cardStateId + 1)
        defaultCardUsageInformationShouldNotBeFound("cardStateId.equals=" + (cardStateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardUsageInformationShouldBeFound(String filter) throws Exception {
        restCardUsageInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardUsageInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfLiveCards").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS)))
            .andExpect(jsonPath("$.[*].totalActiveCards").value(hasItem(DEFAULT_TOTAL_ACTIVE_CARDS)))
            .andExpect(jsonPath("$.[*].totalNumberOfTransactionsDone").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE)))
            .andExpect(
                jsonPath("$.[*].totalValueOfTransactionsDoneInLCY")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY)))
            );

        // Check, that the count call also returns 1
        restCardUsageInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardUsageInformationShouldNotBeFound(String filter) throws Exception {
        restCardUsageInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardUsageInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardUsageInformation() throws Exception {
        // Get the cardUsageInformation
        restCardUsageInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardUsageInformation() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();

        // Update the cardUsageInformation
        CardUsageInformation updatedCardUsageInformation = cardUsageInformationRepository.findById(cardUsageInformation.getId()).get();
        // Disconnect from session so that the updates on updatedCardUsageInformation are not directly saved in db
        em.detach(updatedCardUsageInformation);
        updatedCardUsageInformation
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfLiveCards(UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS)
            .totalActiveCards(UPDATED_TOTAL_ACTIVE_CARDS)
            .totalNumberOfTransactionsDone(UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE)
            .totalValueOfTransactionsDoneInLCY(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(updatedCardUsageInformation);

        restCardUsageInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardUsageInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);
        CardUsageInformation testCardUsageInformation = cardUsageInformationList.get(cardUsageInformationList.size() - 1);
        assertThat(testCardUsageInformation.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardUsageInformation.getTotalNumberOfLiveCards()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalActiveCards()).isEqualTo(UPDATED_TOTAL_ACTIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalNumberOfTransactionsDone()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
        assertThat(testCardUsageInformation.getTotalValueOfTransactionsDoneInLCY())
            .isEqualTo(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository).save(testCardUsageInformation);
    }

    @Test
    @Transactional
    void putNonExistingCardUsageInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();
        cardUsageInformation.setId(count.incrementAndGet());

        // Create the CardUsageInformation
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardUsageInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardUsageInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(0)).save(cardUsageInformation);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardUsageInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();
        cardUsageInformation.setId(count.incrementAndGet());

        // Create the CardUsageInformation
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardUsageInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(0)).save(cardUsageInformation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardUsageInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();
        cardUsageInformation.setId(count.incrementAndGet());

        // Create the CardUsageInformation
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardUsageInformationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(0)).save(cardUsageInformation);
    }

    @Test
    @Transactional
    void partialUpdateCardUsageInformationWithPatch() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();

        // Update the cardUsageInformation using partial update
        CardUsageInformation partialUpdatedCardUsageInformation = new CardUsageInformation();
        partialUpdatedCardUsageInformation.setId(cardUsageInformation.getId());

        partialUpdatedCardUsageInformation.reportingDate(UPDATED_REPORTING_DATE).totalActiveCards(UPDATED_TOTAL_ACTIVE_CARDS);

        restCardUsageInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardUsageInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardUsageInformation))
            )
            .andExpect(status().isOk());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);
        CardUsageInformation testCardUsageInformation = cardUsageInformationList.get(cardUsageInformationList.size() - 1);
        assertThat(testCardUsageInformation.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardUsageInformation.getTotalNumberOfLiveCards()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalActiveCards()).isEqualTo(UPDATED_TOTAL_ACTIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalNumberOfTransactionsDone()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
        assertThat(testCardUsageInformation.getTotalValueOfTransactionsDoneInLCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);
    }

    @Test
    @Transactional
    void fullUpdateCardUsageInformationWithPatch() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();

        // Update the cardUsageInformation using partial update
        CardUsageInformation partialUpdatedCardUsageInformation = new CardUsageInformation();
        partialUpdatedCardUsageInformation.setId(cardUsageInformation.getId());

        partialUpdatedCardUsageInformation
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfLiveCards(UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS)
            .totalActiveCards(UPDATED_TOTAL_ACTIVE_CARDS)
            .totalNumberOfTransactionsDone(UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE)
            .totalValueOfTransactionsDoneInLCY(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);

        restCardUsageInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardUsageInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardUsageInformation))
            )
            .andExpect(status().isOk());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);
        CardUsageInformation testCardUsageInformation = cardUsageInformationList.get(cardUsageInformationList.size() - 1);
        assertThat(testCardUsageInformation.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardUsageInformation.getTotalNumberOfLiveCards()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_LIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalActiveCards()).isEqualTo(UPDATED_TOTAL_ACTIVE_CARDS);
        assertThat(testCardUsageInformation.getTotalNumberOfTransactionsDone()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_TRANSACTIONS_DONE);
        assertThat(testCardUsageInformation.getTotalValueOfTransactionsDoneInLCY())
            .isEqualByComparingTo(UPDATED_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY);
    }

    @Test
    @Transactional
    void patchNonExistingCardUsageInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();
        cardUsageInformation.setId(count.incrementAndGet());

        // Create the CardUsageInformation
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardUsageInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardUsageInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(0)).save(cardUsageInformation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardUsageInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();
        cardUsageInformation.setId(count.incrementAndGet());

        // Create the CardUsageInformation
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardUsageInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(0)).save(cardUsageInformation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardUsageInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardUsageInformationRepository.findAll().size();
        cardUsageInformation.setId(count.incrementAndGet());

        // Create the CardUsageInformation
        CardUsageInformationDTO cardUsageInformationDTO = cardUsageInformationMapper.toDto(cardUsageInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardUsageInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardUsageInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardUsageInformation in the database
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(0)).save(cardUsageInformation);
    }

    @Test
    @Transactional
    void deleteCardUsageInformation() throws Exception {
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);

        int databaseSizeBeforeDelete = cardUsageInformationRepository.findAll().size();

        // Delete the cardUsageInformation
        restCardUsageInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardUsageInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardUsageInformation> cardUsageInformationList = cardUsageInformationRepository.findAll();
        assertThat(cardUsageInformationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardUsageInformation in Elasticsearch
        verify(mockCardUsageInformationSearchRepository, times(1)).deleteById(cardUsageInformation.getId());
    }

    @Test
    @Transactional
    void searchCardUsageInformation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardUsageInformationRepository.saveAndFlush(cardUsageInformation);
        when(mockCardUsageInformationSearchRepository.search("id:" + cardUsageInformation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardUsageInformation), PageRequest.of(0, 1), 1));

        // Search the cardUsageInformation
        restCardUsageInformationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardUsageInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardUsageInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfLiveCards").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_LIVE_CARDS)))
            .andExpect(jsonPath("$.[*].totalActiveCards").value(hasItem(DEFAULT_TOTAL_ACTIVE_CARDS)))
            .andExpect(jsonPath("$.[*].totalNumberOfTransactionsDone").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_TRANSACTIONS_DONE)))
            .andExpect(
                jsonPath("$.[*].totalValueOfTransactionsDoneInLCY")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_VALUE_OF_TRANSACTIONS_DONE_IN_LCY)))
            );
    }
}
