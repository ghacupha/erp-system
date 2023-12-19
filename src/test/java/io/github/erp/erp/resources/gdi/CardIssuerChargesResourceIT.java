package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.repository.CardIssuerChargesRepository;
import io.github.erp.repository.search.CardIssuerChargesSearchRepository;
import io.github.erp.service.dto.CardIssuerChargesDTO;
import io.github.erp.service.mapper.CardIssuerChargesMapper;
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
 * Integration tests for the {CardIssuerChargesResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CardIssuerChargesResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_CARD_FEE_CHARGE_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_CARD_FEE_CHARGE_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_CARD_FEE_CHARGE_IN_LCY = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/granular-data/card-issuer-charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/card-issuer-charges";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardIssuerChargesRepository cardIssuerChargesRepository;

    @Autowired
    private CardIssuerChargesMapper cardIssuerChargesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardIssuerChargesSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardIssuerChargesSearchRepository mockCardIssuerChargesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardIssuerChargesMockMvc;

    private CardIssuerCharges cardIssuerCharges;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardIssuerCharges createEntity(EntityManager em) {
        CardIssuerCharges cardIssuerCharges = new CardIssuerCharges()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .cardFeeChargeInLCY(DEFAULT_CARD_FEE_CHARGE_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        cardIssuerCharges.setBankCode(institutionCode);
        // Add required entity
        CardCategoryType cardCategoryType;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategoryType = CardCategoryTypeResourceIT.createEntity(em);
            em.persist(cardCategoryType);
            em.flush();
        } else {
            cardCategoryType = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        cardIssuerCharges.setCardCategory(cardCategoryType);
        // Add required entity
        CardTypes cardTypes;
        if (TestUtil.findAll(em, CardTypes.class).isEmpty()) {
            cardTypes = CardTypesResourceIT.createEntity(em);
            em.persist(cardTypes);
            em.flush();
        } else {
            cardTypes = TestUtil.findAll(em, CardTypes.class).get(0);
        }
        cardIssuerCharges.setCardType(cardTypes);
        // Add required entity
        CardBrandType cardBrandType;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrandType = CardBrandTypeResourceIT.createEntity(em);
            em.persist(cardBrandType);
            em.flush();
        } else {
            cardBrandType = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        cardIssuerCharges.setCardBrand(cardBrandType);
        // Add required entity
        CardClassType cardClassType;
        if (TestUtil.findAll(em, CardClassType.class).isEmpty()) {
            cardClassType = CardClassTypeResourceIT.createEntity(em);
            em.persist(cardClassType);
            em.flush();
        } else {
            cardClassType = TestUtil.findAll(em, CardClassType.class).get(0);
        }
        cardIssuerCharges.setCardClass(cardClassType);
        // Add required entity
        CardCharges cardCharges;
        if (TestUtil.findAll(em, CardCharges.class).isEmpty()) {
            cardCharges = CardChargesResourceIT.createEntity(em);
            em.persist(cardCharges);
            em.flush();
        } else {
            cardCharges = TestUtil.findAll(em, CardCharges.class).get(0);
        }
        cardIssuerCharges.setCardChargeType(cardCharges);
        return cardIssuerCharges;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardIssuerCharges createUpdatedEntity(EntityManager em) {
        CardIssuerCharges cardIssuerCharges = new CardIssuerCharges()
            .reportingDate(UPDATED_REPORTING_DATE)
            .cardFeeChargeInLCY(UPDATED_CARD_FEE_CHARGE_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        cardIssuerCharges.setBankCode(institutionCode);
        // Add required entity
        CardCategoryType cardCategoryType;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategoryType = CardCategoryTypeResourceIT.createUpdatedEntity(em);
            em.persist(cardCategoryType);
            em.flush();
        } else {
            cardCategoryType = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        cardIssuerCharges.setCardCategory(cardCategoryType);
        // Add required entity
        CardTypes cardTypes;
        if (TestUtil.findAll(em, CardTypes.class).isEmpty()) {
            cardTypes = CardTypesResourceIT.createUpdatedEntity(em);
            em.persist(cardTypes);
            em.flush();
        } else {
            cardTypes = TestUtil.findAll(em, CardTypes.class).get(0);
        }
        cardIssuerCharges.setCardType(cardTypes);
        // Add required entity
        CardBrandType cardBrandType;
        if (TestUtil.findAll(em, CardBrandType.class).isEmpty()) {
            cardBrandType = CardBrandTypeResourceIT.createUpdatedEntity(em);
            em.persist(cardBrandType);
            em.flush();
        } else {
            cardBrandType = TestUtil.findAll(em, CardBrandType.class).get(0);
        }
        cardIssuerCharges.setCardBrand(cardBrandType);
        // Add required entity
        CardClassType cardClassType;
        if (TestUtil.findAll(em, CardClassType.class).isEmpty()) {
            cardClassType = CardClassTypeResourceIT.createUpdatedEntity(em);
            em.persist(cardClassType);
            em.flush();
        } else {
            cardClassType = TestUtil.findAll(em, CardClassType.class).get(0);
        }
        cardIssuerCharges.setCardClass(cardClassType);
        // Add required entity
        CardCharges cardCharges;
        if (TestUtil.findAll(em, CardCharges.class).isEmpty()) {
            cardCharges = CardChargesResourceIT.createUpdatedEntity(em);
            em.persist(cardCharges);
            em.flush();
        } else {
            cardCharges = TestUtil.findAll(em, CardCharges.class).get(0);
        }
        cardIssuerCharges.setCardChargeType(cardCharges);
        return cardIssuerCharges;
    }

    @BeforeEach
    public void initTest() {
        cardIssuerCharges = createEntity(em);
    }

    @Test
    @Transactional
    void createCardIssuerCharges() throws Exception {
        int databaseSizeBeforeCreate = cardIssuerChargesRepository.findAll().size();
        // Create the CardIssuerCharges
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);
        restCardIssuerChargesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeCreate + 1);
        CardIssuerCharges testCardIssuerCharges = cardIssuerChargesList.get(cardIssuerChargesList.size() - 1);
        assertThat(testCardIssuerCharges.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCardIssuerCharges.getCardFeeChargeInLCY()).isEqualByComparingTo(DEFAULT_CARD_FEE_CHARGE_IN_LCY);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(1)).save(testCardIssuerCharges);
    }

    @Test
    @Transactional
    void createCardIssuerChargesWithExistingId() throws Exception {
        // Create the CardIssuerCharges with an existing ID
        cardIssuerCharges.setId(1L);
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        int databaseSizeBeforeCreate = cardIssuerChargesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardIssuerChargesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(0)).save(cardIssuerCharges);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardIssuerChargesRepository.findAll().size();
        // set the field null
        cardIssuerCharges.setReportingDate(null);

        // Create the CardIssuerCharges, which fails.
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        restCardIssuerChargesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardFeeChargeInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardIssuerChargesRepository.findAll().size();
        // set the field null
        cardIssuerCharges.setCardFeeChargeInLCY(null);

        // Create the CardIssuerCharges, which fails.
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        restCardIssuerChargesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardIssuerCharges() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList
        restCardIssuerChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardIssuerCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardFeeChargeInLCY").value(hasItem(sameNumber(DEFAULT_CARD_FEE_CHARGE_IN_LCY))));
    }

    @Test
    @Transactional
    void getCardIssuerCharges() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get the cardIssuerCharges
        restCardIssuerChargesMockMvc
            .perform(get(ENTITY_API_URL_ID, cardIssuerCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardIssuerCharges.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.cardFeeChargeInLCY").value(sameNumber(DEFAULT_CARD_FEE_CHARGE_IN_LCY)));
    }

    @Test
    @Transactional
    void getCardIssuerChargesByIdFiltering() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        Long id = cardIssuerCharges.getId();

        defaultCardIssuerChargesShouldBeFound("id.equals=" + id);
        defaultCardIssuerChargesShouldNotBeFound("id.notEquals=" + id);

        defaultCardIssuerChargesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardIssuerChargesShouldNotBeFound("id.greaterThan=" + id);

        defaultCardIssuerChargesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardIssuerChargesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultCardIssuerChargesShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardIssuerChargesList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardIssuerChargesList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultCardIssuerChargesShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultCardIssuerChargesShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the cardIssuerChargesList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate is not null
        defaultCardIssuerChargesShouldBeFound("reportingDate.specified=true");

        // Get all the cardIssuerChargesList where reportingDate is null
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultCardIssuerChargesShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardIssuerChargesList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultCardIssuerChargesShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardIssuerChargesList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardIssuerChargesList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultCardIssuerChargesShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultCardIssuerChargesShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardIssuerChargesList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultCardIssuerChargesShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY equals to DEFAULT_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldBeFound("cardFeeChargeInLCY.equals=" + DEFAULT_CARD_FEE_CHARGE_IN_LCY);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY equals to UPDATED_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.equals=" + UPDATED_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY not equals to DEFAULT_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.notEquals=" + DEFAULT_CARD_FEE_CHARGE_IN_LCY);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY not equals to UPDATED_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldBeFound("cardFeeChargeInLCY.notEquals=" + UPDATED_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY in DEFAULT_CARD_FEE_CHARGE_IN_LCY or UPDATED_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldBeFound(
            "cardFeeChargeInLCY.in=" + DEFAULT_CARD_FEE_CHARGE_IN_LCY + "," + UPDATED_CARD_FEE_CHARGE_IN_LCY
        );

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY equals to UPDATED_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.in=" + UPDATED_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is not null
        defaultCardIssuerChargesShouldBeFound("cardFeeChargeInLCY.specified=true");

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is null
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is greater than or equal to DEFAULT_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldBeFound("cardFeeChargeInLCY.greaterThanOrEqual=" + DEFAULT_CARD_FEE_CHARGE_IN_LCY);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is greater than or equal to UPDATED_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.greaterThanOrEqual=" + UPDATED_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is less than or equal to DEFAULT_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldBeFound("cardFeeChargeInLCY.lessThanOrEqual=" + DEFAULT_CARD_FEE_CHARGE_IN_LCY);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is less than or equal to SMALLER_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.lessThanOrEqual=" + SMALLER_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is less than DEFAULT_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.lessThan=" + DEFAULT_CARD_FEE_CHARGE_IN_LCY);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is less than UPDATED_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldBeFound("cardFeeChargeInLCY.lessThan=" + UPDATED_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardFeeChargeInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is greater than DEFAULT_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldNotBeFound("cardFeeChargeInLCY.greaterThan=" + DEFAULT_CARD_FEE_CHARGE_IN_LCY);

        // Get all the cardIssuerChargesList where cardFeeChargeInLCY is greater than SMALLER_CARD_FEE_CHARGE_IN_LCY
        defaultCardIssuerChargesShouldBeFound("cardFeeChargeInLCY.greaterThan=" + SMALLER_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
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
        cardIssuerCharges.setBankCode(bankCode);
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        Long bankCodeId = bankCode.getId();

        // Get all the cardIssuerChargesList where bankCode equals to bankCodeId
        defaultCardIssuerChargesShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the cardIssuerChargesList where bankCode equals to (bankCodeId + 1)
        defaultCardIssuerChargesShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        CardCategoryType cardCategory;
        if (TestUtil.findAll(em, CardCategoryType.class).isEmpty()) {
            cardCategory = CardCategoryTypeResourceIT.createEntity(em);
            em.persist(cardCategory);
            em.flush();
        } else {
            cardCategory = TestUtil.findAll(em, CardCategoryType.class).get(0);
        }
        em.persist(cardCategory);
        em.flush();
        cardIssuerCharges.setCardCategory(cardCategory);
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        Long cardCategoryId = cardCategory.getId();

        // Get all the cardIssuerChargesList where cardCategory equals to cardCategoryId
        defaultCardIssuerChargesShouldBeFound("cardCategoryId.equals=" + cardCategoryId);

        // Get all the cardIssuerChargesList where cardCategory equals to (cardCategoryId + 1)
        defaultCardIssuerChargesShouldNotBeFound("cardCategoryId.equals=" + (cardCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
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
        cardIssuerCharges.setCardType(cardType);
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        Long cardTypeId = cardType.getId();

        // Get all the cardIssuerChargesList where cardType equals to cardTypeId
        defaultCardIssuerChargesShouldBeFound("cardTypeId.equals=" + cardTypeId);

        // Get all the cardIssuerChargesList where cardType equals to (cardTypeId + 1)
        defaultCardIssuerChargesShouldNotBeFound("cardTypeId.equals=" + (cardTypeId + 1));
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
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
        cardIssuerCharges.setCardBrand(cardBrand);
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        Long cardBrandId = cardBrand.getId();

        // Get all the cardIssuerChargesList where cardBrand equals to cardBrandId
        defaultCardIssuerChargesShouldBeFound("cardBrandId.equals=" + cardBrandId);

        // Get all the cardIssuerChargesList where cardBrand equals to (cardBrandId + 1)
        defaultCardIssuerChargesShouldNotBeFound("cardBrandId.equals=" + (cardBrandId + 1));
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardClassIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        CardClassType cardClass;
        if (TestUtil.findAll(em, CardClassType.class).isEmpty()) {
            cardClass = CardClassTypeResourceIT.createEntity(em);
            em.persist(cardClass);
            em.flush();
        } else {
            cardClass = TestUtil.findAll(em, CardClassType.class).get(0);
        }
        em.persist(cardClass);
        em.flush();
        cardIssuerCharges.setCardClass(cardClass);
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        Long cardClassId = cardClass.getId();

        // Get all the cardIssuerChargesList where cardClass equals to cardClassId
        defaultCardIssuerChargesShouldBeFound("cardClassId.equals=" + cardClassId);

        // Get all the cardIssuerChargesList where cardClass equals to (cardClassId + 1)
        defaultCardIssuerChargesShouldNotBeFound("cardClassId.equals=" + (cardClassId + 1));
    }

    @Test
    @Transactional
    void getAllCardIssuerChargesByCardChargeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        CardCharges cardChargeType;
        if (TestUtil.findAll(em, CardCharges.class).isEmpty()) {
            cardChargeType = CardChargesResourceIT.createEntity(em);
            em.persist(cardChargeType);
            em.flush();
        } else {
            cardChargeType = TestUtil.findAll(em, CardCharges.class).get(0);
        }
        em.persist(cardChargeType);
        em.flush();
        cardIssuerCharges.setCardChargeType(cardChargeType);
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        Long cardChargeTypeId = cardChargeType.getId();

        // Get all the cardIssuerChargesList where cardChargeType equals to cardChargeTypeId
        defaultCardIssuerChargesShouldBeFound("cardChargeTypeId.equals=" + cardChargeTypeId);

        // Get all the cardIssuerChargesList where cardChargeType equals to (cardChargeTypeId + 1)
        defaultCardIssuerChargesShouldNotBeFound("cardChargeTypeId.equals=" + (cardChargeTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardIssuerChargesShouldBeFound(String filter) throws Exception {
        restCardIssuerChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardIssuerCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardFeeChargeInLCY").value(hasItem(sameNumber(DEFAULT_CARD_FEE_CHARGE_IN_LCY))));

        // Check, that the count call also returns 1
        restCardIssuerChargesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardIssuerChargesShouldNotBeFound(String filter) throws Exception {
        restCardIssuerChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardIssuerChargesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardIssuerCharges() throws Exception {
        // Get the cardIssuerCharges
        restCardIssuerChargesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardIssuerCharges() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();

        // Update the cardIssuerCharges
        CardIssuerCharges updatedCardIssuerCharges = cardIssuerChargesRepository.findById(cardIssuerCharges.getId()).get();
        // Disconnect from session so that the updates on updatedCardIssuerCharges are not directly saved in db
        em.detach(updatedCardIssuerCharges);
        updatedCardIssuerCharges.reportingDate(UPDATED_REPORTING_DATE).cardFeeChargeInLCY(UPDATED_CARD_FEE_CHARGE_IN_LCY);
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(updatedCardIssuerCharges);

        restCardIssuerChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardIssuerChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);
        CardIssuerCharges testCardIssuerCharges = cardIssuerChargesList.get(cardIssuerChargesList.size() - 1);
        assertThat(testCardIssuerCharges.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardIssuerCharges.getCardFeeChargeInLCY()).isEqualTo(UPDATED_CARD_FEE_CHARGE_IN_LCY);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository).save(testCardIssuerCharges);
    }

    @Test
    @Transactional
    void putNonExistingCardIssuerCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();
        cardIssuerCharges.setId(count.incrementAndGet());

        // Create the CardIssuerCharges
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardIssuerChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardIssuerChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(0)).save(cardIssuerCharges);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardIssuerCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();
        cardIssuerCharges.setId(count.incrementAndGet());

        // Create the CardIssuerCharges
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardIssuerChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(0)).save(cardIssuerCharges);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardIssuerCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();
        cardIssuerCharges.setId(count.incrementAndGet());

        // Create the CardIssuerCharges
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardIssuerChargesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(0)).save(cardIssuerCharges);
    }

    @Test
    @Transactional
    void partialUpdateCardIssuerChargesWithPatch() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();

        // Update the cardIssuerCharges using partial update
        CardIssuerCharges partialUpdatedCardIssuerCharges = new CardIssuerCharges();
        partialUpdatedCardIssuerCharges.setId(cardIssuerCharges.getId());

        partialUpdatedCardIssuerCharges.reportingDate(UPDATED_REPORTING_DATE).cardFeeChargeInLCY(UPDATED_CARD_FEE_CHARGE_IN_LCY);

        restCardIssuerChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardIssuerCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardIssuerCharges))
            )
            .andExpect(status().isOk());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);
        CardIssuerCharges testCardIssuerCharges = cardIssuerChargesList.get(cardIssuerChargesList.size() - 1);
        assertThat(testCardIssuerCharges.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardIssuerCharges.getCardFeeChargeInLCY()).isEqualByComparingTo(UPDATED_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void fullUpdateCardIssuerChargesWithPatch() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();

        // Update the cardIssuerCharges using partial update
        CardIssuerCharges partialUpdatedCardIssuerCharges = new CardIssuerCharges();
        partialUpdatedCardIssuerCharges.setId(cardIssuerCharges.getId());

        partialUpdatedCardIssuerCharges.reportingDate(UPDATED_REPORTING_DATE).cardFeeChargeInLCY(UPDATED_CARD_FEE_CHARGE_IN_LCY);

        restCardIssuerChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardIssuerCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardIssuerCharges))
            )
            .andExpect(status().isOk());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);
        CardIssuerCharges testCardIssuerCharges = cardIssuerChargesList.get(cardIssuerChargesList.size() - 1);
        assertThat(testCardIssuerCharges.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardIssuerCharges.getCardFeeChargeInLCY()).isEqualByComparingTo(UPDATED_CARD_FEE_CHARGE_IN_LCY);
    }

    @Test
    @Transactional
    void patchNonExistingCardIssuerCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();
        cardIssuerCharges.setId(count.incrementAndGet());

        // Create the CardIssuerCharges
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardIssuerChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardIssuerChargesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(0)).save(cardIssuerCharges);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardIssuerCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();
        cardIssuerCharges.setId(count.incrementAndGet());

        // Create the CardIssuerCharges
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardIssuerChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(0)).save(cardIssuerCharges);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardIssuerCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardIssuerChargesRepository.findAll().size();
        cardIssuerCharges.setId(count.incrementAndGet());

        // Create the CardIssuerCharges
        CardIssuerChargesDTO cardIssuerChargesDTO = cardIssuerChargesMapper.toDto(cardIssuerCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardIssuerChargesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardIssuerChargesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardIssuerCharges in the database
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(0)).save(cardIssuerCharges);
    }

    @Test
    @Transactional
    void deleteCardIssuerCharges() throws Exception {
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);

        int databaseSizeBeforeDelete = cardIssuerChargesRepository.findAll().size();

        // Delete the cardIssuerCharges
        restCardIssuerChargesMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardIssuerCharges.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardIssuerCharges> cardIssuerChargesList = cardIssuerChargesRepository.findAll();
        assertThat(cardIssuerChargesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardIssuerCharges in Elasticsearch
        verify(mockCardIssuerChargesSearchRepository, times(1)).deleteById(cardIssuerCharges.getId());
    }

    @Test
    @Transactional
    void searchCardIssuerCharges() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardIssuerChargesRepository.saveAndFlush(cardIssuerCharges);
        when(mockCardIssuerChargesSearchRepository.search("id:" + cardIssuerCharges.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardIssuerCharges), PageRequest.of(0, 1), 1));

        // Search the cardIssuerCharges
        restCardIssuerChargesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardIssuerCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardIssuerCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardFeeChargeInLCY").value(hasItem(sameNumber(DEFAULT_CARD_FEE_CHARGE_IN_LCY))));
    }
}
