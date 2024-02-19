package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CreditCardFacility;
import io.github.erp.domain.CreditCardOwnership;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.IsoCurrencyCode;
import io.github.erp.repository.CreditCardFacilityRepository;
import io.github.erp.repository.search.CreditCardFacilitySearchRepository;
import io.github.erp.service.criteria.CreditCardFacilityCriteria;
import io.github.erp.service.dto.CreditCardFacilityDTO;
import io.github.erp.service.mapper.CreditCardFacilityMapper;
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
 * Integration tests for the {@link CreditCardFacilityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CreditCardFacilityResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS = 0;
    private static final Integer UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS = 1;
    private static final Integer SMALLER_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS = 0 - 1;

    private static final BigDecimal DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_CCY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_LCY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY = new BigDecimal(0 - 1);

    private static final String ENTITY_API_URL = "/api/credit-card-facilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/credit-card-facilities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreditCardFacilityRepository creditCardFacilityRepository;

    @Autowired
    private CreditCardFacilityMapper creditCardFacilityMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CreditCardFacilitySearchRepositoryMockConfiguration
     */
    @Autowired
    private CreditCardFacilitySearchRepository mockCreditCardFacilitySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditCardFacilityMockMvc;

    private CreditCardFacility creditCardFacility;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCardFacility createEntity(EntityManager em) {
        CreditCardFacility creditCardFacility = new CreditCardFacility()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .totalNumberOfActiveCreditCards(DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS)
            .totalCreditCardLimitsInCCY(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY)
            .totalCreditCardLimitsInLCY(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY)
            .totalCreditCardAmountUtilisedInCCY(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)
            .totalCreditCardAmountUtilisedInLcy(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)
            .totalNPACreditCardAmountInFCY(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)
            .totalNPACreditCardAmountInLCY(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        creditCardFacility.setBankCode(institutionCode);
        // Add required entity
        CreditCardOwnership creditCardOwnership;
        if (TestUtil.findAll(em, CreditCardOwnership.class).isEmpty()) {
            creditCardOwnership = CreditCardOwnershipResourceIT.createEntity(em);
            em.persist(creditCardOwnership);
            em.flush();
        } else {
            creditCardOwnership = TestUtil.findAll(em, CreditCardOwnership.class).get(0);
        }
        creditCardFacility.setCustomerCategory(creditCardOwnership);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        creditCardFacility.setCurrencyCode(isoCurrencyCode);
        return creditCardFacility;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCardFacility createUpdatedEntity(EntityManager em) {
        CreditCardFacility creditCardFacility = new CreditCardFacility()
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfActiveCreditCards(UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS)
            .totalCreditCardLimitsInCCY(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY)
            .totalCreditCardLimitsInLCY(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY)
            .totalCreditCardAmountUtilisedInCCY(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)
            .totalCreditCardAmountUtilisedInLcy(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)
            .totalNPACreditCardAmountInFCY(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)
            .totalNPACreditCardAmountInLCY(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        creditCardFacility.setBankCode(institutionCode);
        // Add required entity
        CreditCardOwnership creditCardOwnership;
        if (TestUtil.findAll(em, CreditCardOwnership.class).isEmpty()) {
            creditCardOwnership = CreditCardOwnershipResourceIT.createUpdatedEntity(em);
            em.persist(creditCardOwnership);
            em.flush();
        } else {
            creditCardOwnership = TestUtil.findAll(em, CreditCardOwnership.class).get(0);
        }
        creditCardFacility.setCustomerCategory(creditCardOwnership);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createUpdatedEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        creditCardFacility.setCurrencyCode(isoCurrencyCode);
        return creditCardFacility;
    }

    @BeforeEach
    public void initTest() {
        creditCardFacility = createEntity(em);
    }

    @Test
    @Transactional
    void createCreditCardFacility() throws Exception {
        int databaseSizeBeforeCreate = creditCardFacilityRepository.findAll().size();
        // Create the CreditCardFacility
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);
        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeCreate + 1);
        CreditCardFacility testCreditCardFacility = creditCardFacilityList.get(creditCardFacilityList.size() - 1);
        assertThat(testCreditCardFacility.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCreditCardFacility.getTotalNumberOfActiveCreditCards()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInCCY()).isEqualByComparingTo(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInLCY()).isEqualByComparingTo(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInCCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInLcy())
            .isEqualByComparingTo(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInFCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInLCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(1)).save(testCreditCardFacility);
    }

    @Test
    @Transactional
    void createCreditCardFacilityWithExistingId() throws Exception {
        // Create the CreditCardFacility with an existing ID
        creditCardFacility.setId(1L);
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        int databaseSizeBeforeCreate = creditCardFacilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeCreate);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(0)).save(creditCardFacility);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setReportingDate(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNumberOfActiveCreditCardsIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setTotalNumberOfActiveCreditCards(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalCreditCardLimitsInCCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setTotalCreditCardLimitsInCCY(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalCreditCardLimitsInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setTotalCreditCardLimitsInLCY(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalCreditCardAmountUtilisedInCCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setTotalCreditCardAmountUtilisedInCCY(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalCreditCardAmountUtilisedInLcyIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setTotalCreditCardAmountUtilisedInLcy(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNPACreditCardAmountInFCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setTotalNPACreditCardAmountInFCY(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNPACreditCardAmountInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardFacilityRepository.findAll().size();
        // set the field null
        creditCardFacility.setTotalNPACreditCardAmountInLCY(null);

        // Create the CreditCardFacility, which fails.
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilities() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList
        restCreditCardFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCardFacility.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfActiveCreditCards").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS)))
            .andExpect(jsonPath("$.[*].totalCreditCardLimitsInCCY").value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY))))
            .andExpect(jsonPath("$.[*].totalCreditCardLimitsInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY))))
            .andExpect(
                jsonPath("$.[*].totalCreditCardAmountUtilisedInCCY")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalCreditCardAmountUtilisedInLcy")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalNPACreditCardAmountInFCY").value(hasItem(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalNPACreditCardAmountInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY)))
            );
    }

    @Test
    @Transactional
    void getCreditCardFacility() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get the creditCardFacility
        restCreditCardFacilityMockMvc
            .perform(get(ENTITY_API_URL_ID, creditCardFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditCardFacility.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.totalNumberOfActiveCreditCards").value(DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS))
            .andExpect(jsonPath("$.totalCreditCardLimitsInCCY").value(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY)))
            .andExpect(jsonPath("$.totalCreditCardLimitsInLCY").value(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY)))
            .andExpect(jsonPath("$.totalCreditCardAmountUtilisedInCCY").value(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)))
            .andExpect(jsonPath("$.totalCreditCardAmountUtilisedInLcy").value(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)))
            .andExpect(jsonPath("$.totalNPACreditCardAmountInFCY").value(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)))
            .andExpect(jsonPath("$.totalNPACreditCardAmountInLCY").value(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY)));
    }

    @Test
    @Transactional
    void getCreditCardFacilitiesByIdFiltering() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        Long id = creditCardFacility.getId();

        defaultCreditCardFacilityShouldBeFound("id.equals=" + id);
        defaultCreditCardFacilityShouldNotBeFound("id.notEquals=" + id);

        defaultCreditCardFacilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCreditCardFacilityShouldNotBeFound("id.greaterThan=" + id);

        defaultCreditCardFacilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCreditCardFacilityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultCreditCardFacilityShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the creditCardFacilityList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the creditCardFacilityList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultCreditCardFacilityShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultCreditCardFacilityShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the creditCardFacilityList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate is not null
        defaultCreditCardFacilityShouldBeFound("reportingDate.specified=true");

        // Get all the creditCardFacilityList where reportingDate is null
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultCreditCardFacilityShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the creditCardFacilityList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultCreditCardFacilityShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the creditCardFacilityList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the creditCardFacilityList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultCreditCardFacilityShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultCreditCardFacilityShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the creditCardFacilityList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultCreditCardFacilityShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards equals to DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldBeFound("totalNumberOfActiveCreditCards.equals=" + DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards equals to UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldNotBeFound("totalNumberOfActiveCreditCards.equals=" + UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards not equals to DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNumberOfActiveCreditCards.notEquals=" + DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        );

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards not equals to UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldBeFound("totalNumberOfActiveCreditCards.notEquals=" + UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards in DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS or UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldBeFound(
            "totalNumberOfActiveCreditCards.in=" +
            DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS +
            "," +
            UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        );

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards equals to UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldNotBeFound("totalNumberOfActiveCreditCards.in=" + UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is not null
        defaultCreditCardFacilityShouldBeFound("totalNumberOfActiveCreditCards.specified=true");

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is null
        defaultCreditCardFacilityShouldNotBeFound("totalNumberOfActiveCreditCards.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is greater than or equal to DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldBeFound(
            "totalNumberOfActiveCreditCards.greaterThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        );

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is greater than or equal to UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNumberOfActiveCreditCards.greaterThanOrEqual=" + UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is less than or equal to DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldBeFound(
            "totalNumberOfActiveCreditCards.lessThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        );

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is less than or equal to SMALLER_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNumberOfActiveCreditCards.lessThanOrEqual=" + SMALLER_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is less than DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldNotBeFound("totalNumberOfActiveCreditCards.lessThan=" + DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is less than UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldBeFound("totalNumberOfActiveCreditCards.lessThan=" + UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNumberOfActiveCreditCardsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is greater than DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNumberOfActiveCreditCards.greaterThan=" + DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        );

        // Get all the creditCardFacilityList where totalNumberOfActiveCreditCards is greater than SMALLER_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS
        defaultCreditCardFacilityShouldBeFound("totalNumberOfActiveCreditCards.greaterThan=" + SMALLER_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY equals to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInCCY.equals=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY equals to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInCCY.equals=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY not equals to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInCCY.notEquals=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY not equals to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInCCY.notEquals=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY in DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY or UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardLimitsInCCY.in=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY + "," + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY equals to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInCCY.in=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is not null
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInCCY.specified=true");

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is null
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInCCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is greater than or equal to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInCCY.greaterThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is greater than or equal to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardLimitsInCCY.greaterThanOrEqual=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is less than or equal to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInCCY.lessThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is less than or equal to SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInCCY.lessThanOrEqual=" + SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is less than DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInCCY.lessThan=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is less than UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInCCY.lessThan=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInCCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is greater than DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInCCY.greaterThan=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInCCY is greater than SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_CCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInCCY.greaterThan=" + SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY equals to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInLCY.equals=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY equals to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInLCY.equals=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY not equals to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInLCY.notEquals=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY not equals to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInLCY.notEquals=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY in DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY or UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardLimitsInLCY.in=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY + "," + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY equals to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInLCY.in=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is not null
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInLCY.specified=true");

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is null
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is greater than or equal to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInLCY.greaterThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is greater than or equal to UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardLimitsInLCY.greaterThanOrEqual=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is less than or equal to DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInLCY.lessThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is less than or equal to SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInLCY.lessThanOrEqual=" + SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is less than DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInLCY.lessThan=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is less than UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInLCY.lessThan=" + UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardLimitsInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is greater than DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardLimitsInLCY.greaterThan=" + DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);

        // Get all the creditCardFacilityList where totalCreditCardLimitsInLCY is greater than SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalCreditCardLimitsInLCY.greaterThan=" + SMALLER_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY equals to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInCCY.equals=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY equals to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInCCY.equals=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY not equals to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInCCY.notEquals=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY not equals to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInCCY.notEquals=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY in DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY or UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInCCY.in=" +
            DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY +
            "," +
            UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY equals to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInCCY.in=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is not null
        defaultCreditCardFacilityShouldBeFound("totalCreditCardAmountUtilisedInCCY.specified=true");

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is null
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardAmountUtilisedInCCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is greater than or equal to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInCCY.greaterThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is greater than or equal to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInCCY.greaterThanOrEqual=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is less than or equal to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInCCY.lessThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is less than or equal to SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInCCY.lessThanOrEqual=" + SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is less than DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInCCY.lessThan=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is less than UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInCCY.lessThan=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInCCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is greater than DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInCCY.greaterThan=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInCCY is greater than SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInCCY.greaterThan=" + SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy equals to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInLcy.equals=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy equals to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInLcy.equals=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy not equals to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInLcy.notEquals=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy not equals to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInLcy.notEquals=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy in DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY or UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInLcy.in=" +
            DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY +
            "," +
            UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy equals to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInLcy.in=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is not null
        defaultCreditCardFacilityShouldBeFound("totalCreditCardAmountUtilisedInLcy.specified=true");

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is null
        defaultCreditCardFacilityShouldNotBeFound("totalCreditCardAmountUtilisedInLcy.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is greater than or equal to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInLcy.greaterThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is greater than or equal to UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInLcy.greaterThanOrEqual=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is less than or equal to DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInLcy.lessThanOrEqual=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is less than or equal to SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInLcy.lessThanOrEqual=" + SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is less than DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInLcy.lessThan=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is less than UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInLcy.lessThan=" + UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalCreditCardAmountUtilisedInLcyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is greater than DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalCreditCardAmountUtilisedInLcy.greaterThan=" + DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );

        // Get all the creditCardFacilityList where totalCreditCardAmountUtilisedInLcy is greater than SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalCreditCardAmountUtilisedInLcy.greaterThan=" + SMALLER_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY equals to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInFCY.equals=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY equals to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInFCY.equals=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY not equals to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInFCY.notEquals=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY not equals to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInFCY.notEquals=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY in DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY or UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldBeFound(
            "totalNPACreditCardAmountInFCY.in=" +
            DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY +
            "," +
            UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY equals to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInFCY.in=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is not null
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInFCY.specified=true");

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is null
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInFCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is greater than or equal to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldBeFound(
            "totalNPACreditCardAmountInFCY.greaterThanOrEqual=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is greater than or equal to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNPACreditCardAmountInFCY.greaterThanOrEqual=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is less than or equal to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldBeFound(
            "totalNPACreditCardAmountInFCY.lessThanOrEqual=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is less than or equal to SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNPACreditCardAmountInFCY.lessThanOrEqual=" + SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is less than DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInFCY.lessThan=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is less than UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInFCY.lessThan=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInFCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is greater than DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNPACreditCardAmountInFCY.greaterThan=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInFCY is greater than SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInFCY.greaterThan=" + SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY equals to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInLCY.equals=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY equals to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInLCY.equals=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY not equals to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInLCY.notEquals=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY not equals to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInLCY.notEquals=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY in DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY or UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalNPACreditCardAmountInLCY.in=" +
            DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY +
            "," +
            UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY equals to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInLCY.in=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is not null
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInLCY.specified=true");

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is null
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is greater than or equal to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalNPACreditCardAmountInLCY.greaterThanOrEqual=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is greater than or equal to UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNPACreditCardAmountInLCY.greaterThanOrEqual=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is less than or equal to DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldBeFound(
            "totalNPACreditCardAmountInLCY.lessThanOrEqual=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is less than or equal to SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNPACreditCardAmountInLCY.lessThanOrEqual=" + SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is less than DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound("totalNPACreditCardAmountInLCY.lessThan=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is less than UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInLCY.lessThan=" + UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByTotalNPACreditCardAmountInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is greater than DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldNotBeFound(
            "totalNPACreditCardAmountInLCY.greaterThan=" + DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        );

        // Get all the creditCardFacilityList where totalNPACreditCardAmountInLCY is greater than SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY
        defaultCreditCardFacilityShouldBeFound("totalNPACreditCardAmountInLCY.greaterThan=" + SMALLER_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);
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
        creditCardFacility.setBankCode(bankCode);
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);
        Long bankCodeId = bankCode.getId();

        // Get all the creditCardFacilityList where bankCode equals to bankCodeId
        defaultCreditCardFacilityShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the creditCardFacilityList where bankCode equals to (bankCodeId + 1)
        defaultCreditCardFacilityShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByCustomerCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);
        CreditCardOwnership customerCategory;
        if (TestUtil.findAll(em, CreditCardOwnership.class).isEmpty()) {
            customerCategory = CreditCardOwnershipResourceIT.createEntity(em);
            em.persist(customerCategory);
            em.flush();
        } else {
            customerCategory = TestUtil.findAll(em, CreditCardOwnership.class).get(0);
        }
        em.persist(customerCategory);
        em.flush();
        creditCardFacility.setCustomerCategory(customerCategory);
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);
        Long customerCategoryId = customerCategory.getId();

        // Get all the creditCardFacilityList where customerCategory equals to customerCategoryId
        defaultCreditCardFacilityShouldBeFound("customerCategoryId.equals=" + customerCategoryId);

        // Get all the creditCardFacilityList where customerCategory equals to (customerCategoryId + 1)
        defaultCreditCardFacilityShouldNotBeFound("customerCategoryId.equals=" + (customerCategoryId + 1));
    }

    @Test
    @Transactional
    void getAllCreditCardFacilitiesByCurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);
        IsoCurrencyCode currencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            currencyCode = IsoCurrencyCodeResourceIT.createEntity(em);
            em.persist(currencyCode);
            em.flush();
        } else {
            currencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        em.persist(currencyCode);
        em.flush();
        creditCardFacility.setCurrencyCode(currencyCode);
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);
        Long currencyCodeId = currencyCode.getId();

        // Get all the creditCardFacilityList where currencyCode equals to currencyCodeId
        defaultCreditCardFacilityShouldBeFound("currencyCodeId.equals=" + currencyCodeId);

        // Get all the creditCardFacilityList where currencyCode equals to (currencyCodeId + 1)
        defaultCreditCardFacilityShouldNotBeFound("currencyCodeId.equals=" + (currencyCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCreditCardFacilityShouldBeFound(String filter) throws Exception {
        restCreditCardFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCardFacility.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfActiveCreditCards").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS)))
            .andExpect(jsonPath("$.[*].totalCreditCardLimitsInCCY").value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY))))
            .andExpect(jsonPath("$.[*].totalCreditCardLimitsInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY))))
            .andExpect(
                jsonPath("$.[*].totalCreditCardAmountUtilisedInCCY")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalCreditCardAmountUtilisedInLcy")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalNPACreditCardAmountInFCY").value(hasItem(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalNPACreditCardAmountInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY)))
            );

        // Check, that the count call also returns 1
        restCreditCardFacilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCreditCardFacilityShouldNotBeFound(String filter) throws Exception {
        restCreditCardFacilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCreditCardFacilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCreditCardFacility() throws Exception {
        // Get the creditCardFacility
        restCreditCardFacilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCreditCardFacility() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();

        // Update the creditCardFacility
        CreditCardFacility updatedCreditCardFacility = creditCardFacilityRepository.findById(creditCardFacility.getId()).get();
        // Disconnect from session so that the updates on updatedCreditCardFacility are not directly saved in db
        em.detach(updatedCreditCardFacility);
        updatedCreditCardFacility
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfActiveCreditCards(UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS)
            .totalCreditCardLimitsInCCY(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY)
            .totalCreditCardLimitsInLCY(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY)
            .totalCreditCardAmountUtilisedInCCY(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)
            .totalCreditCardAmountUtilisedInLcy(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)
            .totalNPACreditCardAmountInFCY(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)
            .totalNPACreditCardAmountInLCY(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(updatedCreditCardFacility);

        restCreditCardFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardFacilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);
        CreditCardFacility testCreditCardFacility = creditCardFacilityList.get(creditCardFacilityList.size() - 1);
        assertThat(testCreditCardFacility.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCreditCardFacility.getTotalNumberOfActiveCreditCards()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInCCY()).isEqualTo(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInLCY()).isEqualTo(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInCCY())
            .isEqualTo(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInLcy())
            .isEqualTo(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInFCY()).isEqualTo(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInLCY()).isEqualTo(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository).save(testCreditCardFacility);
    }

    @Test
    @Transactional
    void putNonExistingCreditCardFacility() throws Exception {
        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();
        creditCardFacility.setId(count.incrementAndGet());

        // Create the CreditCardFacility
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardFacilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(0)).save(creditCardFacility);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreditCardFacility() throws Exception {
        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();
        creditCardFacility.setId(count.incrementAndGet());

        // Create the CreditCardFacility
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardFacilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(0)).save(creditCardFacility);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreditCardFacility() throws Exception {
        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();
        creditCardFacility.setId(count.incrementAndGet());

        // Create the CreditCardFacility
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardFacilityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(0)).save(creditCardFacility);
    }

    @Test
    @Transactional
    void partialUpdateCreditCardFacilityWithPatch() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();

        // Update the creditCardFacility using partial update
        CreditCardFacility partialUpdatedCreditCardFacility = new CreditCardFacility();
        partialUpdatedCreditCardFacility.setId(creditCardFacility.getId());

        partialUpdatedCreditCardFacility
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalCreditCardLimitsInLCY(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY)
            .totalCreditCardAmountUtilisedInCCY(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)
            .totalNPACreditCardAmountInFCY(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);

        restCreditCardFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCardFacility.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditCardFacility))
            )
            .andExpect(status().isOk());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);
        CreditCardFacility testCreditCardFacility = creditCardFacilityList.get(creditCardFacilityList.size() - 1);
        assertThat(testCreditCardFacility.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCreditCardFacility.getTotalNumberOfActiveCreditCards()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInCCY()).isEqualByComparingTo(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInLCY()).isEqualByComparingTo(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInCCY())
            .isEqualByComparingTo(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInLcy())
            .isEqualByComparingTo(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInFCY())
            .isEqualByComparingTo(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInLCY())
            .isEqualByComparingTo(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
    }

    @Test
    @Transactional
    void fullUpdateCreditCardFacilityWithPatch() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();

        // Update the creditCardFacility using partial update
        CreditCardFacility partialUpdatedCreditCardFacility = new CreditCardFacility();
        partialUpdatedCreditCardFacility.setId(creditCardFacility.getId());

        partialUpdatedCreditCardFacility
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfActiveCreditCards(UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS)
            .totalCreditCardLimitsInCCY(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY)
            .totalCreditCardLimitsInLCY(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY)
            .totalCreditCardAmountUtilisedInCCY(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)
            .totalCreditCardAmountUtilisedInLcy(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)
            .totalNPACreditCardAmountInFCY(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)
            .totalNPACreditCardAmountInLCY(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);

        restCreditCardFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCardFacility.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditCardFacility))
            )
            .andExpect(status().isOk());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);
        CreditCardFacility testCreditCardFacility = creditCardFacilityList.get(creditCardFacilityList.size() - 1);
        assertThat(testCreditCardFacility.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCreditCardFacility.getTotalNumberOfActiveCreditCards()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInCCY()).isEqualByComparingTo(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardLimitsInLCY()).isEqualByComparingTo(UPDATED_TOTAL_CREDIT_CARD_LIMITS_IN_LCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInCCY())
            .isEqualByComparingTo(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY);
        assertThat(testCreditCardFacility.getTotalCreditCardAmountUtilisedInLcy())
            .isEqualByComparingTo(UPDATED_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInFCY())
            .isEqualByComparingTo(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY);
        assertThat(testCreditCardFacility.getTotalNPACreditCardAmountInLCY())
            .isEqualByComparingTo(UPDATED_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY);
    }

    @Test
    @Transactional
    void patchNonExistingCreditCardFacility() throws Exception {
        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();
        creditCardFacility.setId(count.incrementAndGet());

        // Create the CreditCardFacility
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creditCardFacilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(0)).save(creditCardFacility);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreditCardFacility() throws Exception {
        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();
        creditCardFacility.setId(count.incrementAndGet());

        // Create the CreditCardFacility
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(0)).save(creditCardFacility);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreditCardFacility() throws Exception {
        int databaseSizeBeforeUpdate = creditCardFacilityRepository.findAll().size();
        creditCardFacility.setId(count.incrementAndGet());

        // Create the CreditCardFacility
        CreditCardFacilityDTO creditCardFacilityDTO = creditCardFacilityMapper.toDto(creditCardFacility);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardFacilityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardFacilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCardFacility in the database
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(0)).save(creditCardFacility);
    }

    @Test
    @Transactional
    void deleteCreditCardFacility() throws Exception {
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);

        int databaseSizeBeforeDelete = creditCardFacilityRepository.findAll().size();

        // Delete the creditCardFacility
        restCreditCardFacilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, creditCardFacility.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CreditCardFacility> creditCardFacilityList = creditCardFacilityRepository.findAll();
        assertThat(creditCardFacilityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CreditCardFacility in Elasticsearch
        verify(mockCreditCardFacilitySearchRepository, times(1)).deleteById(creditCardFacility.getId());
    }

    @Test
    @Transactional
    void searchCreditCardFacility() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        creditCardFacilityRepository.saveAndFlush(creditCardFacility);
        when(mockCreditCardFacilitySearchRepository.search("id:" + creditCardFacility.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(creditCardFacility), PageRequest.of(0, 1), 1));

        // Search the creditCardFacility
        restCreditCardFacilityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + creditCardFacility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCardFacility.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfActiveCreditCards").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_ACTIVE_CREDIT_CARDS)))
            .andExpect(jsonPath("$.[*].totalCreditCardLimitsInCCY").value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_CCY))))
            .andExpect(jsonPath("$.[*].totalCreditCardLimitsInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_LIMITS_IN_LCY))))
            .andExpect(
                jsonPath("$.[*].totalCreditCardAmountUtilisedInCCY")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_CCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalCreditCardAmountUtilisedInLcy")
                    .value(hasItem(sameNumber(DEFAULT_TOTAL_CREDIT_CARD_AMOUNT_UTILISED_IN_LCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalNPACreditCardAmountInFCY").value(hasItem(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_FCY)))
            )
            .andExpect(
                jsonPath("$.[*].totalNPACreditCardAmountInLCY").value(hasItem(sameNumber(DEFAULT_TOTAL_NPA_CREDIT_CARD_AMOUNT_IN_LCY)))
            );
    }
}
