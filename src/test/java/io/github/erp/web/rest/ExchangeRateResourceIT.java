package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ExchangeRate;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.IsoCurrencyCode;
import io.github.erp.repository.ExchangeRateRepository;
import io.github.erp.repository.search.ExchangeRateSearchRepository;
import io.github.erp.service.criteria.ExchangeRateCriteria;
import io.github.erp.service.dto.ExchangeRateDTO;
import io.github.erp.service.mapper.ExchangeRateMapper;
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
 * Integration tests for the {@link ExchangeRateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExchangeRateResourceIT {

    private static final LocalDate DEFAULT_BUSINESS_REPORTING_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BUSINESS_REPORTING_DAY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BUSINESS_REPORTING_DAY = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_BUYING_RATE = 1D;
    private static final Double UPDATED_BUYING_RATE = 2D;
    private static final Double SMALLER_BUYING_RATE = 1D - 1D;

    private static final Double DEFAULT_SELLING_RATE = 1D;
    private static final Double UPDATED_SELLING_RATE = 2D;
    private static final Double SMALLER_SELLING_RATE = 1D - 1D;

    private static final Double DEFAULT_MEAN_RATE = 1D;
    private static final Double UPDATED_MEAN_RATE = 2D;
    private static final Double SMALLER_MEAN_RATE = 1D - 1D;

    private static final Double DEFAULT_CLOSING_BID_RATE = 1D;
    private static final Double UPDATED_CLOSING_BID_RATE = 2D;
    private static final Double SMALLER_CLOSING_BID_RATE = 1D - 1D;

    private static final Double DEFAULT_CLOSING_OFFER_RATE = 1D;
    private static final Double UPDATED_CLOSING_OFFER_RATE = 2D;
    private static final Double SMALLER_CLOSING_OFFER_RATE = 1D - 1D;

    private static final Double DEFAULT_USD_CROSS_RATE = 1D;
    private static final Double UPDATED_USD_CROSS_RATE = 2D;
    private static final Double SMALLER_USD_CROSS_RATE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/exchange-rates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/exchange-rates";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ExchangeRateSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExchangeRateSearchRepository mockExchangeRateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExchangeRateMockMvc;

    private ExchangeRate exchangeRate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExchangeRate createEntity(EntityManager em) {
        ExchangeRate exchangeRate = new ExchangeRate()
            .businessReportingDay(DEFAULT_BUSINESS_REPORTING_DAY)
            .buyingRate(DEFAULT_BUYING_RATE)
            .sellingRate(DEFAULT_SELLING_RATE)
            .meanRate(DEFAULT_MEAN_RATE)
            .closingBidRate(DEFAULT_CLOSING_BID_RATE)
            .closingOfferRate(DEFAULT_CLOSING_OFFER_RATE)
            .usdCrossRate(DEFAULT_USD_CROSS_RATE);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        exchangeRate.setInstitutionCode(institutionCode);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        exchangeRate.setCurrencyCode(isoCurrencyCode);
        return exchangeRate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExchangeRate createUpdatedEntity(EntityManager em) {
        ExchangeRate exchangeRate = new ExchangeRate()
            .businessReportingDay(UPDATED_BUSINESS_REPORTING_DAY)
            .buyingRate(UPDATED_BUYING_RATE)
            .sellingRate(UPDATED_SELLING_RATE)
            .meanRate(UPDATED_MEAN_RATE)
            .closingBidRate(UPDATED_CLOSING_BID_RATE)
            .closingOfferRate(UPDATED_CLOSING_OFFER_RATE)
            .usdCrossRate(UPDATED_USD_CROSS_RATE);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        exchangeRate.setInstitutionCode(institutionCode);
        // Add required entity
        IsoCurrencyCode isoCurrencyCode;
        if (TestUtil.findAll(em, IsoCurrencyCode.class).isEmpty()) {
            isoCurrencyCode = IsoCurrencyCodeResourceIT.createUpdatedEntity(em);
            em.persist(isoCurrencyCode);
            em.flush();
        } else {
            isoCurrencyCode = TestUtil.findAll(em, IsoCurrencyCode.class).get(0);
        }
        exchangeRate.setCurrencyCode(isoCurrencyCode);
        return exchangeRate;
    }

    @BeforeEach
    public void initTest() {
        exchangeRate = createEntity(em);
    }

    @Test
    @Transactional
    void createExchangeRate() throws Exception {
        int databaseSizeBeforeCreate = exchangeRateRepository.findAll().size();
        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);
        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeCreate + 1);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getBusinessReportingDay()).isEqualTo(DEFAULT_BUSINESS_REPORTING_DAY);
        assertThat(testExchangeRate.getBuyingRate()).isEqualTo(DEFAULT_BUYING_RATE);
        assertThat(testExchangeRate.getSellingRate()).isEqualTo(DEFAULT_SELLING_RATE);
        assertThat(testExchangeRate.getMeanRate()).isEqualTo(DEFAULT_MEAN_RATE);
        assertThat(testExchangeRate.getClosingBidRate()).isEqualTo(DEFAULT_CLOSING_BID_RATE);
        assertThat(testExchangeRate.getClosingOfferRate()).isEqualTo(DEFAULT_CLOSING_OFFER_RATE);
        assertThat(testExchangeRate.getUsdCrossRate()).isEqualTo(DEFAULT_USD_CROSS_RATE);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(1)).save(testExchangeRate);
    }

    @Test
    @Transactional
    void createExchangeRateWithExistingId() throws Exception {
        // Create the ExchangeRate with an existing ID
        exchangeRate.setId(1L);
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        int databaseSizeBeforeCreate = exchangeRateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeCreate);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(0)).save(exchangeRate);
    }

    @Test
    @Transactional
    void checkBusinessReportingDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRateRepository.findAll().size();
        // set the field null
        exchangeRate.setBusinessReportingDay(null);

        // Create the ExchangeRate, which fails.
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBuyingRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRateRepository.findAll().size();
        // set the field null
        exchangeRate.setBuyingRate(null);

        // Create the ExchangeRate, which fails.
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSellingRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRateRepository.findAll().size();
        // set the field null
        exchangeRate.setSellingRate(null);

        // Create the ExchangeRate, which fails.
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMeanRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRateRepository.findAll().size();
        // set the field null
        exchangeRate.setMeanRate(null);

        // Create the ExchangeRate, which fails.
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClosingBidRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRateRepository.findAll().size();
        // set the field null
        exchangeRate.setClosingBidRate(null);

        // Create the ExchangeRate, which fails.
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClosingOfferRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRateRepository.findAll().size();
        // set the field null
        exchangeRate.setClosingOfferRate(null);

        // Create the ExchangeRate, which fails.
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsdCrossRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exchangeRateRepository.findAll().size();
        // set the field null
        exchangeRate.setUsdCrossRate(null);

        // Create the ExchangeRate, which fails.
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        restExchangeRateMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExchangeRates() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exchangeRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessReportingDay").value(hasItem(DEFAULT_BUSINESS_REPORTING_DAY.toString())))
            .andExpect(jsonPath("$.[*].buyingRate").value(hasItem(DEFAULT_BUYING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellingRate").value(hasItem(DEFAULT_SELLING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].meanRate").value(hasItem(DEFAULT_MEAN_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].closingBidRate").value(hasItem(DEFAULT_CLOSING_BID_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].closingOfferRate").value(hasItem(DEFAULT_CLOSING_OFFER_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].usdCrossRate").value(hasItem(DEFAULT_USD_CROSS_RATE.doubleValue())));
    }

    @Test
    @Transactional
    void getExchangeRate() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get the exchangeRate
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL_ID, exchangeRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exchangeRate.getId().intValue()))
            .andExpect(jsonPath("$.businessReportingDay").value(DEFAULT_BUSINESS_REPORTING_DAY.toString()))
            .andExpect(jsonPath("$.buyingRate").value(DEFAULT_BUYING_RATE.doubleValue()))
            .andExpect(jsonPath("$.sellingRate").value(DEFAULT_SELLING_RATE.doubleValue()))
            .andExpect(jsonPath("$.meanRate").value(DEFAULT_MEAN_RATE.doubleValue()))
            .andExpect(jsonPath("$.closingBidRate").value(DEFAULT_CLOSING_BID_RATE.doubleValue()))
            .andExpect(jsonPath("$.closingOfferRate").value(DEFAULT_CLOSING_OFFER_RATE.doubleValue()))
            .andExpect(jsonPath("$.usdCrossRate").value(DEFAULT_USD_CROSS_RATE.doubleValue()));
    }

    @Test
    @Transactional
    void getExchangeRatesByIdFiltering() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        Long id = exchangeRate.getId();

        defaultExchangeRateShouldBeFound("id.equals=" + id);
        defaultExchangeRateShouldNotBeFound("id.notEquals=" + id);

        defaultExchangeRateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExchangeRateShouldNotBeFound("id.greaterThan=" + id);

        defaultExchangeRateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExchangeRateShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay equals to DEFAULT_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldBeFound("businessReportingDay.equals=" + DEFAULT_BUSINESS_REPORTING_DAY);

        // Get all the exchangeRateList where businessReportingDay equals to UPDATED_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldNotBeFound("businessReportingDay.equals=" + UPDATED_BUSINESS_REPORTING_DAY);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay not equals to DEFAULT_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldNotBeFound("businessReportingDay.notEquals=" + DEFAULT_BUSINESS_REPORTING_DAY);

        // Get all the exchangeRateList where businessReportingDay not equals to UPDATED_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldBeFound("businessReportingDay.notEquals=" + UPDATED_BUSINESS_REPORTING_DAY);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsInShouldWork() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay in DEFAULT_BUSINESS_REPORTING_DAY or UPDATED_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldBeFound(
            "businessReportingDay.in=" + DEFAULT_BUSINESS_REPORTING_DAY + "," + UPDATED_BUSINESS_REPORTING_DAY
        );

        // Get all the exchangeRateList where businessReportingDay equals to UPDATED_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldNotBeFound("businessReportingDay.in=" + UPDATED_BUSINESS_REPORTING_DAY);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay is not null
        defaultExchangeRateShouldBeFound("businessReportingDay.specified=true");

        // Get all the exchangeRateList where businessReportingDay is null
        defaultExchangeRateShouldNotBeFound("businessReportingDay.specified=false");
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay is greater than or equal to DEFAULT_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldBeFound("businessReportingDay.greaterThanOrEqual=" + DEFAULT_BUSINESS_REPORTING_DAY);

        // Get all the exchangeRateList where businessReportingDay is greater than or equal to UPDATED_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldNotBeFound("businessReportingDay.greaterThanOrEqual=" + UPDATED_BUSINESS_REPORTING_DAY);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay is less than or equal to DEFAULT_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldBeFound("businessReportingDay.lessThanOrEqual=" + DEFAULT_BUSINESS_REPORTING_DAY);

        // Get all the exchangeRateList where businessReportingDay is less than or equal to SMALLER_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldNotBeFound("businessReportingDay.lessThanOrEqual=" + SMALLER_BUSINESS_REPORTING_DAY);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsLessThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay is less than DEFAULT_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldNotBeFound("businessReportingDay.lessThan=" + DEFAULT_BUSINESS_REPORTING_DAY);

        // Get all the exchangeRateList where businessReportingDay is less than UPDATED_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldBeFound("businessReportingDay.lessThan=" + UPDATED_BUSINESS_REPORTING_DAY);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBusinessReportingDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where businessReportingDay is greater than DEFAULT_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldNotBeFound("businessReportingDay.greaterThan=" + DEFAULT_BUSINESS_REPORTING_DAY);

        // Get all the exchangeRateList where businessReportingDay is greater than SMALLER_BUSINESS_REPORTING_DAY
        defaultExchangeRateShouldBeFound("businessReportingDay.greaterThan=" + SMALLER_BUSINESS_REPORTING_DAY);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate equals to DEFAULT_BUYING_RATE
        defaultExchangeRateShouldBeFound("buyingRate.equals=" + DEFAULT_BUYING_RATE);

        // Get all the exchangeRateList where buyingRate equals to UPDATED_BUYING_RATE
        defaultExchangeRateShouldNotBeFound("buyingRate.equals=" + UPDATED_BUYING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate not equals to DEFAULT_BUYING_RATE
        defaultExchangeRateShouldNotBeFound("buyingRate.notEquals=" + DEFAULT_BUYING_RATE);

        // Get all the exchangeRateList where buyingRate not equals to UPDATED_BUYING_RATE
        defaultExchangeRateShouldBeFound("buyingRate.notEquals=" + UPDATED_BUYING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsInShouldWork() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate in DEFAULT_BUYING_RATE or UPDATED_BUYING_RATE
        defaultExchangeRateShouldBeFound("buyingRate.in=" + DEFAULT_BUYING_RATE + "," + UPDATED_BUYING_RATE);

        // Get all the exchangeRateList where buyingRate equals to UPDATED_BUYING_RATE
        defaultExchangeRateShouldNotBeFound("buyingRate.in=" + UPDATED_BUYING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate is not null
        defaultExchangeRateShouldBeFound("buyingRate.specified=true");

        // Get all the exchangeRateList where buyingRate is null
        defaultExchangeRateShouldNotBeFound("buyingRate.specified=false");
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate is greater than or equal to DEFAULT_BUYING_RATE
        defaultExchangeRateShouldBeFound("buyingRate.greaterThanOrEqual=" + DEFAULT_BUYING_RATE);

        // Get all the exchangeRateList where buyingRate is greater than or equal to UPDATED_BUYING_RATE
        defaultExchangeRateShouldNotBeFound("buyingRate.greaterThanOrEqual=" + UPDATED_BUYING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate is less than or equal to DEFAULT_BUYING_RATE
        defaultExchangeRateShouldBeFound("buyingRate.lessThanOrEqual=" + DEFAULT_BUYING_RATE);

        // Get all the exchangeRateList where buyingRate is less than or equal to SMALLER_BUYING_RATE
        defaultExchangeRateShouldNotBeFound("buyingRate.lessThanOrEqual=" + SMALLER_BUYING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsLessThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate is less than DEFAULT_BUYING_RATE
        defaultExchangeRateShouldNotBeFound("buyingRate.lessThan=" + DEFAULT_BUYING_RATE);

        // Get all the exchangeRateList where buyingRate is less than UPDATED_BUYING_RATE
        defaultExchangeRateShouldBeFound("buyingRate.lessThan=" + UPDATED_BUYING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByBuyingRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where buyingRate is greater than DEFAULT_BUYING_RATE
        defaultExchangeRateShouldNotBeFound("buyingRate.greaterThan=" + DEFAULT_BUYING_RATE);

        // Get all the exchangeRateList where buyingRate is greater than SMALLER_BUYING_RATE
        defaultExchangeRateShouldBeFound("buyingRate.greaterThan=" + SMALLER_BUYING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate equals to DEFAULT_SELLING_RATE
        defaultExchangeRateShouldBeFound("sellingRate.equals=" + DEFAULT_SELLING_RATE);

        // Get all the exchangeRateList where sellingRate equals to UPDATED_SELLING_RATE
        defaultExchangeRateShouldNotBeFound("sellingRate.equals=" + UPDATED_SELLING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate not equals to DEFAULT_SELLING_RATE
        defaultExchangeRateShouldNotBeFound("sellingRate.notEquals=" + DEFAULT_SELLING_RATE);

        // Get all the exchangeRateList where sellingRate not equals to UPDATED_SELLING_RATE
        defaultExchangeRateShouldBeFound("sellingRate.notEquals=" + UPDATED_SELLING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsInShouldWork() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate in DEFAULT_SELLING_RATE or UPDATED_SELLING_RATE
        defaultExchangeRateShouldBeFound("sellingRate.in=" + DEFAULT_SELLING_RATE + "," + UPDATED_SELLING_RATE);

        // Get all the exchangeRateList where sellingRate equals to UPDATED_SELLING_RATE
        defaultExchangeRateShouldNotBeFound("sellingRate.in=" + UPDATED_SELLING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate is not null
        defaultExchangeRateShouldBeFound("sellingRate.specified=true");

        // Get all the exchangeRateList where sellingRate is null
        defaultExchangeRateShouldNotBeFound("sellingRate.specified=false");
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate is greater than or equal to DEFAULT_SELLING_RATE
        defaultExchangeRateShouldBeFound("sellingRate.greaterThanOrEqual=" + DEFAULT_SELLING_RATE);

        // Get all the exchangeRateList where sellingRate is greater than or equal to UPDATED_SELLING_RATE
        defaultExchangeRateShouldNotBeFound("sellingRate.greaterThanOrEqual=" + UPDATED_SELLING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate is less than or equal to DEFAULT_SELLING_RATE
        defaultExchangeRateShouldBeFound("sellingRate.lessThanOrEqual=" + DEFAULT_SELLING_RATE);

        // Get all the exchangeRateList where sellingRate is less than or equal to SMALLER_SELLING_RATE
        defaultExchangeRateShouldNotBeFound("sellingRate.lessThanOrEqual=" + SMALLER_SELLING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsLessThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate is less than DEFAULT_SELLING_RATE
        defaultExchangeRateShouldNotBeFound("sellingRate.lessThan=" + DEFAULT_SELLING_RATE);

        // Get all the exchangeRateList where sellingRate is less than UPDATED_SELLING_RATE
        defaultExchangeRateShouldBeFound("sellingRate.lessThan=" + UPDATED_SELLING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesBySellingRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where sellingRate is greater than DEFAULT_SELLING_RATE
        defaultExchangeRateShouldNotBeFound("sellingRate.greaterThan=" + DEFAULT_SELLING_RATE);

        // Get all the exchangeRateList where sellingRate is greater than SMALLER_SELLING_RATE
        defaultExchangeRateShouldBeFound("sellingRate.greaterThan=" + SMALLER_SELLING_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate equals to DEFAULT_MEAN_RATE
        defaultExchangeRateShouldBeFound("meanRate.equals=" + DEFAULT_MEAN_RATE);

        // Get all the exchangeRateList where meanRate equals to UPDATED_MEAN_RATE
        defaultExchangeRateShouldNotBeFound("meanRate.equals=" + UPDATED_MEAN_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate not equals to DEFAULT_MEAN_RATE
        defaultExchangeRateShouldNotBeFound("meanRate.notEquals=" + DEFAULT_MEAN_RATE);

        // Get all the exchangeRateList where meanRate not equals to UPDATED_MEAN_RATE
        defaultExchangeRateShouldBeFound("meanRate.notEquals=" + UPDATED_MEAN_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsInShouldWork() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate in DEFAULT_MEAN_RATE or UPDATED_MEAN_RATE
        defaultExchangeRateShouldBeFound("meanRate.in=" + DEFAULT_MEAN_RATE + "," + UPDATED_MEAN_RATE);

        // Get all the exchangeRateList where meanRate equals to UPDATED_MEAN_RATE
        defaultExchangeRateShouldNotBeFound("meanRate.in=" + UPDATED_MEAN_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate is not null
        defaultExchangeRateShouldBeFound("meanRate.specified=true");

        // Get all the exchangeRateList where meanRate is null
        defaultExchangeRateShouldNotBeFound("meanRate.specified=false");
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate is greater than or equal to DEFAULT_MEAN_RATE
        defaultExchangeRateShouldBeFound("meanRate.greaterThanOrEqual=" + DEFAULT_MEAN_RATE);

        // Get all the exchangeRateList where meanRate is greater than or equal to UPDATED_MEAN_RATE
        defaultExchangeRateShouldNotBeFound("meanRate.greaterThanOrEqual=" + UPDATED_MEAN_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate is less than or equal to DEFAULT_MEAN_RATE
        defaultExchangeRateShouldBeFound("meanRate.lessThanOrEqual=" + DEFAULT_MEAN_RATE);

        // Get all the exchangeRateList where meanRate is less than or equal to SMALLER_MEAN_RATE
        defaultExchangeRateShouldNotBeFound("meanRate.lessThanOrEqual=" + SMALLER_MEAN_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsLessThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate is less than DEFAULT_MEAN_RATE
        defaultExchangeRateShouldNotBeFound("meanRate.lessThan=" + DEFAULT_MEAN_RATE);

        // Get all the exchangeRateList where meanRate is less than UPDATED_MEAN_RATE
        defaultExchangeRateShouldBeFound("meanRate.lessThan=" + UPDATED_MEAN_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByMeanRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where meanRate is greater than DEFAULT_MEAN_RATE
        defaultExchangeRateShouldNotBeFound("meanRate.greaterThan=" + DEFAULT_MEAN_RATE);

        // Get all the exchangeRateList where meanRate is greater than SMALLER_MEAN_RATE
        defaultExchangeRateShouldBeFound("meanRate.greaterThan=" + SMALLER_MEAN_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate equals to DEFAULT_CLOSING_BID_RATE
        defaultExchangeRateShouldBeFound("closingBidRate.equals=" + DEFAULT_CLOSING_BID_RATE);

        // Get all the exchangeRateList where closingBidRate equals to UPDATED_CLOSING_BID_RATE
        defaultExchangeRateShouldNotBeFound("closingBidRate.equals=" + UPDATED_CLOSING_BID_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate not equals to DEFAULT_CLOSING_BID_RATE
        defaultExchangeRateShouldNotBeFound("closingBidRate.notEquals=" + DEFAULT_CLOSING_BID_RATE);

        // Get all the exchangeRateList where closingBidRate not equals to UPDATED_CLOSING_BID_RATE
        defaultExchangeRateShouldBeFound("closingBidRate.notEquals=" + UPDATED_CLOSING_BID_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsInShouldWork() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate in DEFAULT_CLOSING_BID_RATE or UPDATED_CLOSING_BID_RATE
        defaultExchangeRateShouldBeFound("closingBidRate.in=" + DEFAULT_CLOSING_BID_RATE + "," + UPDATED_CLOSING_BID_RATE);

        // Get all the exchangeRateList where closingBidRate equals to UPDATED_CLOSING_BID_RATE
        defaultExchangeRateShouldNotBeFound("closingBidRate.in=" + UPDATED_CLOSING_BID_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate is not null
        defaultExchangeRateShouldBeFound("closingBidRate.specified=true");

        // Get all the exchangeRateList where closingBidRate is null
        defaultExchangeRateShouldNotBeFound("closingBidRate.specified=false");
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate is greater than or equal to DEFAULT_CLOSING_BID_RATE
        defaultExchangeRateShouldBeFound("closingBidRate.greaterThanOrEqual=" + DEFAULT_CLOSING_BID_RATE);

        // Get all the exchangeRateList where closingBidRate is greater than or equal to UPDATED_CLOSING_BID_RATE
        defaultExchangeRateShouldNotBeFound("closingBidRate.greaterThanOrEqual=" + UPDATED_CLOSING_BID_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate is less than or equal to DEFAULT_CLOSING_BID_RATE
        defaultExchangeRateShouldBeFound("closingBidRate.lessThanOrEqual=" + DEFAULT_CLOSING_BID_RATE);

        // Get all the exchangeRateList where closingBidRate is less than or equal to SMALLER_CLOSING_BID_RATE
        defaultExchangeRateShouldNotBeFound("closingBidRate.lessThanOrEqual=" + SMALLER_CLOSING_BID_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsLessThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate is less than DEFAULT_CLOSING_BID_RATE
        defaultExchangeRateShouldNotBeFound("closingBidRate.lessThan=" + DEFAULT_CLOSING_BID_RATE);

        // Get all the exchangeRateList where closingBidRate is less than UPDATED_CLOSING_BID_RATE
        defaultExchangeRateShouldBeFound("closingBidRate.lessThan=" + UPDATED_CLOSING_BID_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingBidRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingBidRate is greater than DEFAULT_CLOSING_BID_RATE
        defaultExchangeRateShouldNotBeFound("closingBidRate.greaterThan=" + DEFAULT_CLOSING_BID_RATE);

        // Get all the exchangeRateList where closingBidRate is greater than SMALLER_CLOSING_BID_RATE
        defaultExchangeRateShouldBeFound("closingBidRate.greaterThan=" + SMALLER_CLOSING_BID_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate equals to DEFAULT_CLOSING_OFFER_RATE
        defaultExchangeRateShouldBeFound("closingOfferRate.equals=" + DEFAULT_CLOSING_OFFER_RATE);

        // Get all the exchangeRateList where closingOfferRate equals to UPDATED_CLOSING_OFFER_RATE
        defaultExchangeRateShouldNotBeFound("closingOfferRate.equals=" + UPDATED_CLOSING_OFFER_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate not equals to DEFAULT_CLOSING_OFFER_RATE
        defaultExchangeRateShouldNotBeFound("closingOfferRate.notEquals=" + DEFAULT_CLOSING_OFFER_RATE);

        // Get all the exchangeRateList where closingOfferRate not equals to UPDATED_CLOSING_OFFER_RATE
        defaultExchangeRateShouldBeFound("closingOfferRate.notEquals=" + UPDATED_CLOSING_OFFER_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsInShouldWork() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate in DEFAULT_CLOSING_OFFER_RATE or UPDATED_CLOSING_OFFER_RATE
        defaultExchangeRateShouldBeFound("closingOfferRate.in=" + DEFAULT_CLOSING_OFFER_RATE + "," + UPDATED_CLOSING_OFFER_RATE);

        // Get all the exchangeRateList where closingOfferRate equals to UPDATED_CLOSING_OFFER_RATE
        defaultExchangeRateShouldNotBeFound("closingOfferRate.in=" + UPDATED_CLOSING_OFFER_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate is not null
        defaultExchangeRateShouldBeFound("closingOfferRate.specified=true");

        // Get all the exchangeRateList where closingOfferRate is null
        defaultExchangeRateShouldNotBeFound("closingOfferRate.specified=false");
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate is greater than or equal to DEFAULT_CLOSING_OFFER_RATE
        defaultExchangeRateShouldBeFound("closingOfferRate.greaterThanOrEqual=" + DEFAULT_CLOSING_OFFER_RATE);

        // Get all the exchangeRateList where closingOfferRate is greater than or equal to UPDATED_CLOSING_OFFER_RATE
        defaultExchangeRateShouldNotBeFound("closingOfferRate.greaterThanOrEqual=" + UPDATED_CLOSING_OFFER_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate is less than or equal to DEFAULT_CLOSING_OFFER_RATE
        defaultExchangeRateShouldBeFound("closingOfferRate.lessThanOrEqual=" + DEFAULT_CLOSING_OFFER_RATE);

        // Get all the exchangeRateList where closingOfferRate is less than or equal to SMALLER_CLOSING_OFFER_RATE
        defaultExchangeRateShouldNotBeFound("closingOfferRate.lessThanOrEqual=" + SMALLER_CLOSING_OFFER_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsLessThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate is less than DEFAULT_CLOSING_OFFER_RATE
        defaultExchangeRateShouldNotBeFound("closingOfferRate.lessThan=" + DEFAULT_CLOSING_OFFER_RATE);

        // Get all the exchangeRateList where closingOfferRate is less than UPDATED_CLOSING_OFFER_RATE
        defaultExchangeRateShouldBeFound("closingOfferRate.lessThan=" + UPDATED_CLOSING_OFFER_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByClosingOfferRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where closingOfferRate is greater than DEFAULT_CLOSING_OFFER_RATE
        defaultExchangeRateShouldNotBeFound("closingOfferRate.greaterThan=" + DEFAULT_CLOSING_OFFER_RATE);

        // Get all the exchangeRateList where closingOfferRate is greater than SMALLER_CLOSING_OFFER_RATE
        defaultExchangeRateShouldBeFound("closingOfferRate.greaterThan=" + SMALLER_CLOSING_OFFER_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate equals to DEFAULT_USD_CROSS_RATE
        defaultExchangeRateShouldBeFound("usdCrossRate.equals=" + DEFAULT_USD_CROSS_RATE);

        // Get all the exchangeRateList where usdCrossRate equals to UPDATED_USD_CROSS_RATE
        defaultExchangeRateShouldNotBeFound("usdCrossRate.equals=" + UPDATED_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate not equals to DEFAULT_USD_CROSS_RATE
        defaultExchangeRateShouldNotBeFound("usdCrossRate.notEquals=" + DEFAULT_USD_CROSS_RATE);

        // Get all the exchangeRateList where usdCrossRate not equals to UPDATED_USD_CROSS_RATE
        defaultExchangeRateShouldBeFound("usdCrossRate.notEquals=" + UPDATED_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsInShouldWork() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate in DEFAULT_USD_CROSS_RATE or UPDATED_USD_CROSS_RATE
        defaultExchangeRateShouldBeFound("usdCrossRate.in=" + DEFAULT_USD_CROSS_RATE + "," + UPDATED_USD_CROSS_RATE);

        // Get all the exchangeRateList where usdCrossRate equals to UPDATED_USD_CROSS_RATE
        defaultExchangeRateShouldNotBeFound("usdCrossRate.in=" + UPDATED_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate is not null
        defaultExchangeRateShouldBeFound("usdCrossRate.specified=true");

        // Get all the exchangeRateList where usdCrossRate is null
        defaultExchangeRateShouldNotBeFound("usdCrossRate.specified=false");
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate is greater than or equal to DEFAULT_USD_CROSS_RATE
        defaultExchangeRateShouldBeFound("usdCrossRate.greaterThanOrEqual=" + DEFAULT_USD_CROSS_RATE);

        // Get all the exchangeRateList where usdCrossRate is greater than or equal to UPDATED_USD_CROSS_RATE
        defaultExchangeRateShouldNotBeFound("usdCrossRate.greaterThanOrEqual=" + UPDATED_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate is less than or equal to DEFAULT_USD_CROSS_RATE
        defaultExchangeRateShouldBeFound("usdCrossRate.lessThanOrEqual=" + DEFAULT_USD_CROSS_RATE);

        // Get all the exchangeRateList where usdCrossRate is less than or equal to SMALLER_USD_CROSS_RATE
        defaultExchangeRateShouldNotBeFound("usdCrossRate.lessThanOrEqual=" + SMALLER_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsLessThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate is less than DEFAULT_USD_CROSS_RATE
        defaultExchangeRateShouldNotBeFound("usdCrossRate.lessThan=" + DEFAULT_USD_CROSS_RATE);

        // Get all the exchangeRateList where usdCrossRate is less than UPDATED_USD_CROSS_RATE
        defaultExchangeRateShouldBeFound("usdCrossRate.lessThan=" + UPDATED_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByUsdCrossRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        // Get all the exchangeRateList where usdCrossRate is greater than DEFAULT_USD_CROSS_RATE
        defaultExchangeRateShouldNotBeFound("usdCrossRate.greaterThan=" + DEFAULT_USD_CROSS_RATE);

        // Get all the exchangeRateList where usdCrossRate is greater than SMALLER_USD_CROSS_RATE
        defaultExchangeRateShouldBeFound("usdCrossRate.greaterThan=" + SMALLER_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void getAllExchangeRatesByInstitutionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        em.persist(institutionCode);
        em.flush();
        exchangeRate.setInstitutionCode(institutionCode);
        exchangeRateRepository.saveAndFlush(exchangeRate);
        Long institutionCodeId = institutionCode.getId();

        // Get all the exchangeRateList where institutionCode equals to institutionCodeId
        defaultExchangeRateShouldBeFound("institutionCodeId.equals=" + institutionCodeId);

        // Get all the exchangeRateList where institutionCode equals to (institutionCodeId + 1)
        defaultExchangeRateShouldNotBeFound("institutionCodeId.equals=" + (institutionCodeId + 1));
    }

    @Test
    @Transactional
    void getAllExchangeRatesByCurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);
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
        exchangeRate.setCurrencyCode(currencyCode);
        exchangeRateRepository.saveAndFlush(exchangeRate);
        Long currencyCodeId = currencyCode.getId();

        // Get all the exchangeRateList where currencyCode equals to currencyCodeId
        defaultExchangeRateShouldBeFound("currencyCodeId.equals=" + currencyCodeId);

        // Get all the exchangeRateList where currencyCode equals to (currencyCodeId + 1)
        defaultExchangeRateShouldNotBeFound("currencyCodeId.equals=" + (currencyCodeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExchangeRateShouldBeFound(String filter) throws Exception {
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exchangeRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessReportingDay").value(hasItem(DEFAULT_BUSINESS_REPORTING_DAY.toString())))
            .andExpect(jsonPath("$.[*].buyingRate").value(hasItem(DEFAULT_BUYING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellingRate").value(hasItem(DEFAULT_SELLING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].meanRate").value(hasItem(DEFAULT_MEAN_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].closingBidRate").value(hasItem(DEFAULT_CLOSING_BID_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].closingOfferRate").value(hasItem(DEFAULT_CLOSING_OFFER_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].usdCrossRate").value(hasItem(DEFAULT_USD_CROSS_RATE.doubleValue())));

        // Check, that the count call also returns 1
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExchangeRateShouldNotBeFound(String filter) throws Exception {
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExchangeRateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExchangeRate() throws Exception {
        // Get the exchangeRate
        restExchangeRateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExchangeRate() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();

        // Update the exchangeRate
        ExchangeRate updatedExchangeRate = exchangeRateRepository.findById(exchangeRate.getId()).get();
        // Disconnect from session so that the updates on updatedExchangeRate are not directly saved in db
        em.detach(updatedExchangeRate);
        updatedExchangeRate
            .businessReportingDay(UPDATED_BUSINESS_REPORTING_DAY)
            .buyingRate(UPDATED_BUYING_RATE)
            .sellingRate(UPDATED_SELLING_RATE)
            .meanRate(UPDATED_MEAN_RATE)
            .closingBidRate(UPDATED_CLOSING_BID_RATE)
            .closingOfferRate(UPDATED_CLOSING_OFFER_RATE)
            .usdCrossRate(UPDATED_USD_CROSS_RATE);
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(updatedExchangeRate);

        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exchangeRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getBusinessReportingDay()).isEqualTo(UPDATED_BUSINESS_REPORTING_DAY);
        assertThat(testExchangeRate.getBuyingRate()).isEqualTo(UPDATED_BUYING_RATE);
        assertThat(testExchangeRate.getSellingRate()).isEqualTo(UPDATED_SELLING_RATE);
        assertThat(testExchangeRate.getMeanRate()).isEqualTo(UPDATED_MEAN_RATE);
        assertThat(testExchangeRate.getClosingBidRate()).isEqualTo(UPDATED_CLOSING_BID_RATE);
        assertThat(testExchangeRate.getClosingOfferRate()).isEqualTo(UPDATED_CLOSING_OFFER_RATE);
        assertThat(testExchangeRate.getUsdCrossRate()).isEqualTo(UPDATED_USD_CROSS_RATE);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository).save(testExchangeRate);
    }

    @Test
    @Transactional
    void putNonExistingExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exchangeRateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(0)).save(exchangeRate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(0)).save(exchangeRate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(0)).save(exchangeRate);
    }

    @Test
    @Transactional
    void partialUpdateExchangeRateWithPatch() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();

        // Update the exchangeRate using partial update
        ExchangeRate partialUpdatedExchangeRate = new ExchangeRate();
        partialUpdatedExchangeRate.setId(exchangeRate.getId());

        partialUpdatedExchangeRate
            .businessReportingDay(UPDATED_BUSINESS_REPORTING_DAY)
            .closingBidRate(UPDATED_CLOSING_BID_RATE)
            .closingOfferRate(UPDATED_CLOSING_OFFER_RATE);

        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExchangeRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExchangeRate))
            )
            .andExpect(status().isOk());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getBusinessReportingDay()).isEqualTo(UPDATED_BUSINESS_REPORTING_DAY);
        assertThat(testExchangeRate.getBuyingRate()).isEqualTo(DEFAULT_BUYING_RATE);
        assertThat(testExchangeRate.getSellingRate()).isEqualTo(DEFAULT_SELLING_RATE);
        assertThat(testExchangeRate.getMeanRate()).isEqualTo(DEFAULT_MEAN_RATE);
        assertThat(testExchangeRate.getClosingBidRate()).isEqualTo(UPDATED_CLOSING_BID_RATE);
        assertThat(testExchangeRate.getClosingOfferRate()).isEqualTo(UPDATED_CLOSING_OFFER_RATE);
        assertThat(testExchangeRate.getUsdCrossRate()).isEqualTo(DEFAULT_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void fullUpdateExchangeRateWithPatch() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();

        // Update the exchangeRate using partial update
        ExchangeRate partialUpdatedExchangeRate = new ExchangeRate();
        partialUpdatedExchangeRate.setId(exchangeRate.getId());

        partialUpdatedExchangeRate
            .businessReportingDay(UPDATED_BUSINESS_REPORTING_DAY)
            .buyingRate(UPDATED_BUYING_RATE)
            .sellingRate(UPDATED_SELLING_RATE)
            .meanRate(UPDATED_MEAN_RATE)
            .closingBidRate(UPDATED_CLOSING_BID_RATE)
            .closingOfferRate(UPDATED_CLOSING_OFFER_RATE)
            .usdCrossRate(UPDATED_USD_CROSS_RATE);

        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExchangeRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExchangeRate))
            )
            .andExpect(status().isOk());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);
        ExchangeRate testExchangeRate = exchangeRateList.get(exchangeRateList.size() - 1);
        assertThat(testExchangeRate.getBusinessReportingDay()).isEqualTo(UPDATED_BUSINESS_REPORTING_DAY);
        assertThat(testExchangeRate.getBuyingRate()).isEqualTo(UPDATED_BUYING_RATE);
        assertThat(testExchangeRate.getSellingRate()).isEqualTo(UPDATED_SELLING_RATE);
        assertThat(testExchangeRate.getMeanRate()).isEqualTo(UPDATED_MEAN_RATE);
        assertThat(testExchangeRate.getClosingBidRate()).isEqualTo(UPDATED_CLOSING_BID_RATE);
        assertThat(testExchangeRate.getClosingOfferRate()).isEqualTo(UPDATED_CLOSING_OFFER_RATE);
        assertThat(testExchangeRate.getUsdCrossRate()).isEqualTo(UPDATED_USD_CROSS_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exchangeRateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(0)).save(exchangeRate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(0)).save(exchangeRate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExchangeRate() throws Exception {
        int databaseSizeBeforeUpdate = exchangeRateRepository.findAll().size();
        exchangeRate.setId(count.incrementAndGet());

        // Create the ExchangeRate
        ExchangeRateDTO exchangeRateDTO = exchangeRateMapper.toDto(exchangeRate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExchangeRateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exchangeRateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExchangeRate in the database
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(0)).save(exchangeRate);
    }

    @Test
    @Transactional
    void deleteExchangeRate() throws Exception {
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);

        int databaseSizeBeforeDelete = exchangeRateRepository.findAll().size();

        // Delete the exchangeRate
        restExchangeRateMockMvc
            .perform(delete(ENTITY_API_URL_ID, exchangeRate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExchangeRate> exchangeRateList = exchangeRateRepository.findAll();
        assertThat(exchangeRateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ExchangeRate in Elasticsearch
        verify(mockExchangeRateSearchRepository, times(1)).deleteById(exchangeRate.getId());
    }

    @Test
    @Transactional
    void searchExchangeRate() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        exchangeRateRepository.saveAndFlush(exchangeRate);
        when(mockExchangeRateSearchRepository.search("id:" + exchangeRate.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(exchangeRate), PageRequest.of(0, 1), 1));

        // Search the exchangeRate
        restExchangeRateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + exchangeRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exchangeRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].businessReportingDay").value(hasItem(DEFAULT_BUSINESS_REPORTING_DAY.toString())))
            .andExpect(jsonPath("$.[*].buyingRate").value(hasItem(DEFAULT_BUYING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellingRate").value(hasItem(DEFAULT_SELLING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].meanRate").value(hasItem(DEFAULT_MEAN_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].closingBidRate").value(hasItem(DEFAULT_CLOSING_BID_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].closingOfferRate").value(hasItem(DEFAULT_CLOSING_OFFER_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].usdCrossRate").value(hasItem(DEFAULT_USD_CROSS_RATE.doubleValue())));
    }
}
