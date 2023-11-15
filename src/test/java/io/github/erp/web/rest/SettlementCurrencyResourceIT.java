package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.repository.SettlementCurrencyRepository;
import io.github.erp.repository.search.SettlementCurrencySearchRepository;
import io.github.erp.service.SettlementCurrencyService;
import io.github.erp.service.criteria.SettlementCurrencyCriteria;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import io.github.erp.service.mapper.SettlementCurrencyMapper;
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

/**
 * Integration tests for the {@link SettlementCurrencyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SettlementCurrencyResourceIT {

    private static final String DEFAULT_ISO_4217_CURRENCY_CODE = "AAA";
    private static final String UPDATED_ISO_4217_CURRENCY_CODE = "BBB";

    private static final String DEFAULT_CURRENCY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERIC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MINOR_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_MINOR_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/settlement-currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/settlement-currencies";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SettlementCurrencyRepository settlementCurrencyRepository;

    @Mock
    private SettlementCurrencyRepository settlementCurrencyRepositoryMock;

    @Autowired
    private SettlementCurrencyMapper settlementCurrencyMapper;

    @Mock
    private SettlementCurrencyService settlementCurrencyServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SettlementCurrencySearchRepositoryMockConfiguration
     */
    @Autowired
    private SettlementCurrencySearchRepository mockSettlementCurrencySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettlementCurrencyMockMvc;

    private SettlementCurrency settlementCurrency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettlementCurrency createEntity(EntityManager em) {
        SettlementCurrency settlementCurrency = new SettlementCurrency()
            .iso4217CurrencyCode(DEFAULT_ISO_4217_CURRENCY_CODE)
            .currencyName(DEFAULT_CURRENCY_NAME)
            .country(DEFAULT_COUNTRY)
            .numericCode(DEFAULT_NUMERIC_CODE)
            .minorUnit(DEFAULT_MINOR_UNIT)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        return settlementCurrency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettlementCurrency createUpdatedEntity(EntityManager em) {
        SettlementCurrency settlementCurrency = new SettlementCurrency()
            .iso4217CurrencyCode(UPDATED_ISO_4217_CURRENCY_CODE)
            .currencyName(UPDATED_CURRENCY_NAME)
            .country(UPDATED_COUNTRY)
            .numericCode(UPDATED_NUMERIC_CODE)
            .minorUnit(UPDATED_MINOR_UNIT)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        return settlementCurrency;
    }

    @BeforeEach
    public void initTest() {
        settlementCurrency = createEntity(em);
    }

    @Test
    @Transactional
    void createSettlementCurrency() throws Exception {
        int databaseSizeBeforeCreate = settlementCurrencyRepository.findAll().size();
        // Create the SettlementCurrency
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);
        restSettlementCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeCreate + 1);
        SettlementCurrency testSettlementCurrency = settlementCurrencyList.get(settlementCurrencyList.size() - 1);
        assertThat(testSettlementCurrency.getIso4217CurrencyCode()).isEqualTo(DEFAULT_ISO_4217_CURRENCY_CODE);
        assertThat(testSettlementCurrency.getCurrencyName()).isEqualTo(DEFAULT_CURRENCY_NAME);
        assertThat(testSettlementCurrency.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSettlementCurrency.getNumericCode()).isEqualTo(DEFAULT_NUMERIC_CODE);
        assertThat(testSettlementCurrency.getMinorUnit()).isEqualTo(DEFAULT_MINOR_UNIT);
        assertThat(testSettlementCurrency.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testSettlementCurrency.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(1)).save(testSettlementCurrency);
    }

    @Test
    @Transactional
    void createSettlementCurrencyWithExistingId() throws Exception {
        // Create the SettlementCurrency with an existing ID
        settlementCurrency.setId(1L);
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        int databaseSizeBeforeCreate = settlementCurrencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettlementCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeCreate);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(0)).save(settlementCurrency);
    }

    @Test
    @Transactional
    void checkIso4217CurrencyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementCurrencyRepository.findAll().size();
        // set the field null
        settlementCurrency.setIso4217CurrencyCode(null);

        // Create the SettlementCurrency, which fails.
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        restSettlementCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementCurrencyRepository.findAll().size();
        // set the field null
        settlementCurrency.setCurrencyName(null);

        // Create the SettlementCurrency, which fails.
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        restSettlementCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementCurrencyRepository.findAll().size();
        // set the field null
        settlementCurrency.setCountry(null);

        // Create the SettlementCurrency, which fails.
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        restSettlementCurrencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSettlementCurrencies() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList
        restSettlementCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementCurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].iso4217CurrencyCode").value(hasItem(DEFAULT_ISO_4217_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettlementCurrenciesWithEagerRelationshipsIsEnabled() throws Exception {
        when(settlementCurrencyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettlementCurrencyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(settlementCurrencyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettlementCurrenciesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(settlementCurrencyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettlementCurrencyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(settlementCurrencyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSettlementCurrency() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get the settlementCurrency
        restSettlementCurrencyMockMvc
            .perform(get(ENTITY_API_URL_ID, settlementCurrency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(settlementCurrency.getId().intValue()))
            .andExpect(jsonPath("$.iso4217CurrencyCode").value(DEFAULT_ISO_4217_CURRENCY_CODE))
            .andExpect(jsonPath("$.currencyName").value(DEFAULT_CURRENCY_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.numericCode").value(DEFAULT_NUMERIC_CODE))
            .andExpect(jsonPath("$.minorUnit").value(DEFAULT_MINOR_UNIT))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }

    @Test
    @Transactional
    void getSettlementCurrenciesByIdFiltering() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        Long id = settlementCurrency.getId();

        defaultSettlementCurrencyShouldBeFound("id.equals=" + id);
        defaultSettlementCurrencyShouldNotBeFound("id.notEquals=" + id);

        defaultSettlementCurrencyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSettlementCurrencyShouldNotBeFound("id.greaterThan=" + id);

        defaultSettlementCurrencyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSettlementCurrencyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByIso4217CurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where iso4217CurrencyCode equals to DEFAULT_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldBeFound("iso4217CurrencyCode.equals=" + DEFAULT_ISO_4217_CURRENCY_CODE);

        // Get all the settlementCurrencyList where iso4217CurrencyCode equals to UPDATED_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldNotBeFound("iso4217CurrencyCode.equals=" + UPDATED_ISO_4217_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByIso4217CurrencyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where iso4217CurrencyCode not equals to DEFAULT_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldNotBeFound("iso4217CurrencyCode.notEquals=" + DEFAULT_ISO_4217_CURRENCY_CODE);

        // Get all the settlementCurrencyList where iso4217CurrencyCode not equals to UPDATED_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldBeFound("iso4217CurrencyCode.notEquals=" + UPDATED_ISO_4217_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByIso4217CurrencyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where iso4217CurrencyCode in DEFAULT_ISO_4217_CURRENCY_CODE or UPDATED_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldBeFound(
            "iso4217CurrencyCode.in=" + DEFAULT_ISO_4217_CURRENCY_CODE + "," + UPDATED_ISO_4217_CURRENCY_CODE
        );

        // Get all the settlementCurrencyList where iso4217CurrencyCode equals to UPDATED_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldNotBeFound("iso4217CurrencyCode.in=" + UPDATED_ISO_4217_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByIso4217CurrencyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where iso4217CurrencyCode is not null
        defaultSettlementCurrencyShouldBeFound("iso4217CurrencyCode.specified=true");

        // Get all the settlementCurrencyList where iso4217CurrencyCode is null
        defaultSettlementCurrencyShouldNotBeFound("iso4217CurrencyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByIso4217CurrencyCodeContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where iso4217CurrencyCode contains DEFAULT_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldBeFound("iso4217CurrencyCode.contains=" + DEFAULT_ISO_4217_CURRENCY_CODE);

        // Get all the settlementCurrencyList where iso4217CurrencyCode contains UPDATED_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldNotBeFound("iso4217CurrencyCode.contains=" + UPDATED_ISO_4217_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByIso4217CurrencyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where iso4217CurrencyCode does not contain DEFAULT_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldNotBeFound("iso4217CurrencyCode.doesNotContain=" + DEFAULT_ISO_4217_CURRENCY_CODE);

        // Get all the settlementCurrencyList where iso4217CurrencyCode does not contain UPDATED_ISO_4217_CURRENCY_CODE
        defaultSettlementCurrencyShouldBeFound("iso4217CurrencyCode.doesNotContain=" + UPDATED_ISO_4217_CURRENCY_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCurrencyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where currencyName equals to DEFAULT_CURRENCY_NAME
        defaultSettlementCurrencyShouldBeFound("currencyName.equals=" + DEFAULT_CURRENCY_NAME);

        // Get all the settlementCurrencyList where currencyName equals to UPDATED_CURRENCY_NAME
        defaultSettlementCurrencyShouldNotBeFound("currencyName.equals=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCurrencyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where currencyName not equals to DEFAULT_CURRENCY_NAME
        defaultSettlementCurrencyShouldNotBeFound("currencyName.notEquals=" + DEFAULT_CURRENCY_NAME);

        // Get all the settlementCurrencyList where currencyName not equals to UPDATED_CURRENCY_NAME
        defaultSettlementCurrencyShouldBeFound("currencyName.notEquals=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCurrencyNameIsInShouldWork() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where currencyName in DEFAULT_CURRENCY_NAME or UPDATED_CURRENCY_NAME
        defaultSettlementCurrencyShouldBeFound("currencyName.in=" + DEFAULT_CURRENCY_NAME + "," + UPDATED_CURRENCY_NAME);

        // Get all the settlementCurrencyList where currencyName equals to UPDATED_CURRENCY_NAME
        defaultSettlementCurrencyShouldNotBeFound("currencyName.in=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCurrencyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where currencyName is not null
        defaultSettlementCurrencyShouldBeFound("currencyName.specified=true");

        // Get all the settlementCurrencyList where currencyName is null
        defaultSettlementCurrencyShouldNotBeFound("currencyName.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCurrencyNameContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where currencyName contains DEFAULT_CURRENCY_NAME
        defaultSettlementCurrencyShouldBeFound("currencyName.contains=" + DEFAULT_CURRENCY_NAME);

        // Get all the settlementCurrencyList where currencyName contains UPDATED_CURRENCY_NAME
        defaultSettlementCurrencyShouldNotBeFound("currencyName.contains=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCurrencyNameNotContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where currencyName does not contain DEFAULT_CURRENCY_NAME
        defaultSettlementCurrencyShouldNotBeFound("currencyName.doesNotContain=" + DEFAULT_CURRENCY_NAME);

        // Get all the settlementCurrencyList where currencyName does not contain UPDATED_CURRENCY_NAME
        defaultSettlementCurrencyShouldBeFound("currencyName.doesNotContain=" + UPDATED_CURRENCY_NAME);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where country equals to DEFAULT_COUNTRY
        defaultSettlementCurrencyShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the settlementCurrencyList where country equals to UPDATED_COUNTRY
        defaultSettlementCurrencyShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where country not equals to DEFAULT_COUNTRY
        defaultSettlementCurrencyShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the settlementCurrencyList where country not equals to UPDATED_COUNTRY
        defaultSettlementCurrencyShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultSettlementCurrencyShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the settlementCurrencyList where country equals to UPDATED_COUNTRY
        defaultSettlementCurrencyShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where country is not null
        defaultSettlementCurrencyShouldBeFound("country.specified=true");

        // Get all the settlementCurrencyList where country is null
        defaultSettlementCurrencyShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCountryContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where country contains DEFAULT_COUNTRY
        defaultSettlementCurrencyShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the settlementCurrencyList where country contains UPDATED_COUNTRY
        defaultSettlementCurrencyShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where country does not contain DEFAULT_COUNTRY
        defaultSettlementCurrencyShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the settlementCurrencyList where country does not contain UPDATED_COUNTRY
        defaultSettlementCurrencyShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByNumericCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where numericCode equals to DEFAULT_NUMERIC_CODE
        defaultSettlementCurrencyShouldBeFound("numericCode.equals=" + DEFAULT_NUMERIC_CODE);

        // Get all the settlementCurrencyList where numericCode equals to UPDATED_NUMERIC_CODE
        defaultSettlementCurrencyShouldNotBeFound("numericCode.equals=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByNumericCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where numericCode not equals to DEFAULT_NUMERIC_CODE
        defaultSettlementCurrencyShouldNotBeFound("numericCode.notEquals=" + DEFAULT_NUMERIC_CODE);

        // Get all the settlementCurrencyList where numericCode not equals to UPDATED_NUMERIC_CODE
        defaultSettlementCurrencyShouldBeFound("numericCode.notEquals=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByNumericCodeIsInShouldWork() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where numericCode in DEFAULT_NUMERIC_CODE or UPDATED_NUMERIC_CODE
        defaultSettlementCurrencyShouldBeFound("numericCode.in=" + DEFAULT_NUMERIC_CODE + "," + UPDATED_NUMERIC_CODE);

        // Get all the settlementCurrencyList where numericCode equals to UPDATED_NUMERIC_CODE
        defaultSettlementCurrencyShouldNotBeFound("numericCode.in=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByNumericCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where numericCode is not null
        defaultSettlementCurrencyShouldBeFound("numericCode.specified=true");

        // Get all the settlementCurrencyList where numericCode is null
        defaultSettlementCurrencyShouldNotBeFound("numericCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByNumericCodeContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where numericCode contains DEFAULT_NUMERIC_CODE
        defaultSettlementCurrencyShouldBeFound("numericCode.contains=" + DEFAULT_NUMERIC_CODE);

        // Get all the settlementCurrencyList where numericCode contains UPDATED_NUMERIC_CODE
        defaultSettlementCurrencyShouldNotBeFound("numericCode.contains=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByNumericCodeNotContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where numericCode does not contain DEFAULT_NUMERIC_CODE
        defaultSettlementCurrencyShouldNotBeFound("numericCode.doesNotContain=" + DEFAULT_NUMERIC_CODE);

        // Get all the settlementCurrencyList where numericCode does not contain UPDATED_NUMERIC_CODE
        defaultSettlementCurrencyShouldBeFound("numericCode.doesNotContain=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByMinorUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where minorUnit equals to DEFAULT_MINOR_UNIT
        defaultSettlementCurrencyShouldBeFound("minorUnit.equals=" + DEFAULT_MINOR_UNIT);

        // Get all the settlementCurrencyList where minorUnit equals to UPDATED_MINOR_UNIT
        defaultSettlementCurrencyShouldNotBeFound("minorUnit.equals=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByMinorUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where minorUnit not equals to DEFAULT_MINOR_UNIT
        defaultSettlementCurrencyShouldNotBeFound("minorUnit.notEquals=" + DEFAULT_MINOR_UNIT);

        // Get all the settlementCurrencyList where minorUnit not equals to UPDATED_MINOR_UNIT
        defaultSettlementCurrencyShouldBeFound("minorUnit.notEquals=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByMinorUnitIsInShouldWork() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where minorUnit in DEFAULT_MINOR_UNIT or UPDATED_MINOR_UNIT
        defaultSettlementCurrencyShouldBeFound("minorUnit.in=" + DEFAULT_MINOR_UNIT + "," + UPDATED_MINOR_UNIT);

        // Get all the settlementCurrencyList where minorUnit equals to UPDATED_MINOR_UNIT
        defaultSettlementCurrencyShouldNotBeFound("minorUnit.in=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByMinorUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where minorUnit is not null
        defaultSettlementCurrencyShouldBeFound("minorUnit.specified=true");

        // Get all the settlementCurrencyList where minorUnit is null
        defaultSettlementCurrencyShouldNotBeFound("minorUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByMinorUnitContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where minorUnit contains DEFAULT_MINOR_UNIT
        defaultSettlementCurrencyShouldBeFound("minorUnit.contains=" + DEFAULT_MINOR_UNIT);

        // Get all the settlementCurrencyList where minorUnit contains UPDATED_MINOR_UNIT
        defaultSettlementCurrencyShouldNotBeFound("minorUnit.contains=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByMinorUnitNotContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where minorUnit does not contain DEFAULT_MINOR_UNIT
        defaultSettlementCurrencyShouldNotBeFound("minorUnit.doesNotContain=" + DEFAULT_MINOR_UNIT);

        // Get all the settlementCurrencyList where minorUnit does not contain UPDATED_MINOR_UNIT
        defaultSettlementCurrencyShouldBeFound("minorUnit.doesNotContain=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementCurrencyList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementCurrencyList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the settlementCurrencyList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where fileUploadToken is not null
        defaultSettlementCurrencyShouldBeFound("fileUploadToken.specified=true");

        // Get all the settlementCurrencyList where fileUploadToken is null
        defaultSettlementCurrencyShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementCurrencyList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the settlementCurrencyList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultSettlementCurrencyShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementCurrencyList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementCurrencyList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the settlementCurrencyList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where compilationToken is not null
        defaultSettlementCurrencyShouldBeFound("compilationToken.specified=true");

        // Get all the settlementCurrencyList where compilationToken is null
        defaultSettlementCurrencyShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementCurrencyList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        // Get all the settlementCurrencyList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the settlementCurrencyList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultSettlementCurrencyShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllSettlementCurrenciesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);
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
        settlementCurrency.addPlaceholder(placeholder);
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);
        Long placeholderId = placeholder.getId();

        // Get all the settlementCurrencyList where placeholder equals to placeholderId
        defaultSettlementCurrencyShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the settlementCurrencyList where placeholder equals to (placeholderId + 1)
        defaultSettlementCurrencyShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSettlementCurrencyShouldBeFound(String filter) throws Exception {
        restSettlementCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementCurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].iso4217CurrencyCode").value(hasItem(DEFAULT_ISO_4217_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restSettlementCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSettlementCurrencyShouldNotBeFound(String filter) throws Exception {
        restSettlementCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSettlementCurrencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSettlementCurrency() throws Exception {
        // Get the settlementCurrency
        restSettlementCurrencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSettlementCurrency() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();

        // Update the settlementCurrency
        SettlementCurrency updatedSettlementCurrency = settlementCurrencyRepository.findById(settlementCurrency.getId()).get();
        // Disconnect from session so that the updates on updatedSettlementCurrency are not directly saved in db
        em.detach(updatedSettlementCurrency);
        updatedSettlementCurrency
            .iso4217CurrencyCode(UPDATED_ISO_4217_CURRENCY_CODE)
            .currencyName(UPDATED_CURRENCY_NAME)
            .country(UPDATED_COUNTRY)
            .numericCode(UPDATED_NUMERIC_CODE)
            .minorUnit(UPDATED_MINOR_UNIT)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(updatedSettlementCurrency);

        restSettlementCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settlementCurrencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);
        SettlementCurrency testSettlementCurrency = settlementCurrencyList.get(settlementCurrencyList.size() - 1);
        assertThat(testSettlementCurrency.getIso4217CurrencyCode()).isEqualTo(UPDATED_ISO_4217_CURRENCY_CODE);
        assertThat(testSettlementCurrency.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testSettlementCurrency.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSettlementCurrency.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);
        assertThat(testSettlementCurrency.getMinorUnit()).isEqualTo(UPDATED_MINOR_UNIT);
        assertThat(testSettlementCurrency.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testSettlementCurrency.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository).save(testSettlementCurrency);
    }

    @Test
    @Transactional
    void putNonExistingSettlementCurrency() throws Exception {
        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();
        settlementCurrency.setId(count.incrementAndGet());

        // Create the SettlementCurrency
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settlementCurrencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(0)).save(settlementCurrency);
    }

    @Test
    @Transactional
    void putWithIdMismatchSettlementCurrency() throws Exception {
        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();
        settlementCurrency.setId(count.incrementAndGet());

        // Create the SettlementCurrency
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(0)).save(settlementCurrency);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSettlementCurrency() throws Exception {
        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();
        settlementCurrency.setId(count.incrementAndGet());

        // Create the SettlementCurrency
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementCurrencyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(0)).save(settlementCurrency);
    }

    @Test
    @Transactional
    void partialUpdateSettlementCurrencyWithPatch() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();

        // Update the settlementCurrency using partial update
        SettlementCurrency partialUpdatedSettlementCurrency = new SettlementCurrency();
        partialUpdatedSettlementCurrency.setId(settlementCurrency.getId());

        partialUpdatedSettlementCurrency
            .iso4217CurrencyCode(UPDATED_ISO_4217_CURRENCY_CODE)
            .currencyName(UPDATED_CURRENCY_NAME)
            .numericCode(UPDATED_NUMERIC_CODE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restSettlementCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlementCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlementCurrency))
            )
            .andExpect(status().isOk());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);
        SettlementCurrency testSettlementCurrency = settlementCurrencyList.get(settlementCurrencyList.size() - 1);
        assertThat(testSettlementCurrency.getIso4217CurrencyCode()).isEqualTo(UPDATED_ISO_4217_CURRENCY_CODE);
        assertThat(testSettlementCurrency.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testSettlementCurrency.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSettlementCurrency.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);
        assertThat(testSettlementCurrency.getMinorUnit()).isEqualTo(DEFAULT_MINOR_UNIT);
        assertThat(testSettlementCurrency.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testSettlementCurrency.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateSettlementCurrencyWithPatch() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();

        // Update the settlementCurrency using partial update
        SettlementCurrency partialUpdatedSettlementCurrency = new SettlementCurrency();
        partialUpdatedSettlementCurrency.setId(settlementCurrency.getId());

        partialUpdatedSettlementCurrency
            .iso4217CurrencyCode(UPDATED_ISO_4217_CURRENCY_CODE)
            .currencyName(UPDATED_CURRENCY_NAME)
            .country(UPDATED_COUNTRY)
            .numericCode(UPDATED_NUMERIC_CODE)
            .minorUnit(UPDATED_MINOR_UNIT)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restSettlementCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlementCurrency.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlementCurrency))
            )
            .andExpect(status().isOk());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);
        SettlementCurrency testSettlementCurrency = settlementCurrencyList.get(settlementCurrencyList.size() - 1);
        assertThat(testSettlementCurrency.getIso4217CurrencyCode()).isEqualTo(UPDATED_ISO_4217_CURRENCY_CODE);
        assertThat(testSettlementCurrency.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testSettlementCurrency.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSettlementCurrency.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);
        assertThat(testSettlementCurrency.getMinorUnit()).isEqualTo(UPDATED_MINOR_UNIT);
        assertThat(testSettlementCurrency.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testSettlementCurrency.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingSettlementCurrency() throws Exception {
        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();
        settlementCurrency.setId(count.incrementAndGet());

        // Create the SettlementCurrency
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, settlementCurrencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(0)).save(settlementCurrency);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSettlementCurrency() throws Exception {
        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();
        settlementCurrency.setId(count.incrementAndGet());

        // Create the SettlementCurrency
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(0)).save(settlementCurrency);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSettlementCurrency() throws Exception {
        int databaseSizeBeforeUpdate = settlementCurrencyRepository.findAll().size();
        settlementCurrency.setId(count.incrementAndGet());

        // Create the SettlementCurrency
        SettlementCurrencyDTO settlementCurrencyDTO = settlementCurrencyMapper.toDto(settlementCurrency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementCurrencyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementCurrencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettlementCurrency in the database
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(0)).save(settlementCurrency);
    }

    @Test
    @Transactional
    void deleteSettlementCurrency() throws Exception {
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);

        int databaseSizeBeforeDelete = settlementCurrencyRepository.findAll().size();

        // Delete the settlementCurrency
        restSettlementCurrencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, settlementCurrency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SettlementCurrency> settlementCurrencyList = settlementCurrencyRepository.findAll();
        assertThat(settlementCurrencyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SettlementCurrency in Elasticsearch
        verify(mockSettlementCurrencySearchRepository, times(1)).deleteById(settlementCurrency.getId());
    }

    @Test
    @Transactional
    void searchSettlementCurrency() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        settlementCurrencyRepository.saveAndFlush(settlementCurrency);
        when(mockSettlementCurrencySearchRepository.search("id:" + settlementCurrency.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(settlementCurrency), PageRequest.of(0, 1), 1));

        // Search the settlementCurrency
        restSettlementCurrencyMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + settlementCurrency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementCurrency.getId().intValue())))
            .andExpect(jsonPath("$.[*].iso4217CurrencyCode").value(hasItem(DEFAULT_ISO_4217_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
