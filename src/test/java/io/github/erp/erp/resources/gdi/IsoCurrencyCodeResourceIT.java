package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.IsoCurrencyCode;
import io.github.erp.repository.IsoCurrencyCodeRepository;
import io.github.erp.repository.search.IsoCurrencyCodeSearchRepository;
import io.github.erp.service.dto.IsoCurrencyCodeDTO;
import io.github.erp.service.mapper.IsoCurrencyCodeMapper;
import io.github.erp.web.rest.IsoCurrencyCodeResource;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IsoCurrencyCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class IsoCurrencyCodeResourceIT {

    private static final String DEFAULT_ALPHABETIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ALPHABETIC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERIC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MINOR_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_MINOR_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/iso-currency-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/iso-currency-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IsoCurrencyCodeRepository isoCurrencyCodeRepository;

    @Autowired
    private IsoCurrencyCodeMapper isoCurrencyCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.IsoCurrencyCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private IsoCurrencyCodeSearchRepository mockIsoCurrencyCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIsoCurrencyCodeMockMvc;

    private IsoCurrencyCode isoCurrencyCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsoCurrencyCode createEntity(EntityManager em) {
        IsoCurrencyCode isoCurrencyCode = new IsoCurrencyCode()
            .alphabeticCode(DEFAULT_ALPHABETIC_CODE)
            .numericCode(DEFAULT_NUMERIC_CODE)
            .minorUnit(DEFAULT_MINOR_UNIT)
            .currency(DEFAULT_CURRENCY)
            .country(DEFAULT_COUNTRY);
        return isoCurrencyCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IsoCurrencyCode createUpdatedEntity(EntityManager em) {
        IsoCurrencyCode isoCurrencyCode = new IsoCurrencyCode()
            .alphabeticCode(UPDATED_ALPHABETIC_CODE)
            .numericCode(UPDATED_NUMERIC_CODE)
            .minorUnit(UPDATED_MINOR_UNIT)
            .currency(UPDATED_CURRENCY)
            .country(UPDATED_COUNTRY);
        return isoCurrencyCode;
    }

    @BeforeEach
    public void initTest() {
        isoCurrencyCode = createEntity(em);
    }

    @Test
    @Transactional
    void createIsoCurrencyCode() throws Exception {
        int databaseSizeBeforeCreate = isoCurrencyCodeRepository.findAll().size();
        // Create the IsoCurrencyCode
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);
        restIsoCurrencyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeCreate + 1);
        IsoCurrencyCode testIsoCurrencyCode = isoCurrencyCodeList.get(isoCurrencyCodeList.size() - 1);
        assertThat(testIsoCurrencyCode.getAlphabeticCode()).isEqualTo(DEFAULT_ALPHABETIC_CODE);
        assertThat(testIsoCurrencyCode.getNumericCode()).isEqualTo(DEFAULT_NUMERIC_CODE);
        assertThat(testIsoCurrencyCode.getMinorUnit()).isEqualTo(DEFAULT_MINOR_UNIT);
        assertThat(testIsoCurrencyCode.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testIsoCurrencyCode.getCountry()).isEqualTo(DEFAULT_COUNTRY);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(1)).save(testIsoCurrencyCode);
    }

    @Test
    @Transactional
    void createIsoCurrencyCodeWithExistingId() throws Exception {
        // Create the IsoCurrencyCode with an existing ID
        isoCurrencyCode.setId(1L);
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        int databaseSizeBeforeCreate = isoCurrencyCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIsoCurrencyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(0)).save(isoCurrencyCode);
    }

    @Test
    @Transactional
    void checkAlphabeticCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = isoCurrencyCodeRepository.findAll().size();
        // set the field null
        isoCurrencyCode.setAlphabeticCode(null);

        // Create the IsoCurrencyCode, which fails.
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        restIsoCurrencyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumericCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = isoCurrencyCodeRepository.findAll().size();
        // set the field null
        isoCurrencyCode.setNumericCode(null);

        // Create the IsoCurrencyCode, which fails.
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        restIsoCurrencyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinorUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = isoCurrencyCodeRepository.findAll().size();
        // set the field null
        isoCurrencyCode.setMinorUnit(null);

        // Create the IsoCurrencyCode, which fails.
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        restIsoCurrencyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = isoCurrencyCodeRepository.findAll().size();
        // set the field null
        isoCurrencyCode.setCurrency(null);

        // Create the IsoCurrencyCode, which fails.
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        restIsoCurrencyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodes() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList
        restIsoCurrencyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isoCurrencyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].alphabeticCode").value(hasItem(DEFAULT_ALPHABETIC_CODE)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getIsoCurrencyCode() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get the isoCurrencyCode
        restIsoCurrencyCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, isoCurrencyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(isoCurrencyCode.getId().intValue()))
            .andExpect(jsonPath("$.alphabeticCode").value(DEFAULT_ALPHABETIC_CODE))
            .andExpect(jsonPath("$.numericCode").value(DEFAULT_NUMERIC_CODE))
            .andExpect(jsonPath("$.minorUnit").value(DEFAULT_MINOR_UNIT))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getIsoCurrencyCodesByIdFiltering() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        Long id = isoCurrencyCode.getId();

        defaultIsoCurrencyCodeShouldBeFound("id.equals=" + id);
        defaultIsoCurrencyCodeShouldNotBeFound("id.notEquals=" + id);

        defaultIsoCurrencyCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIsoCurrencyCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultIsoCurrencyCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIsoCurrencyCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByAlphabeticCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where alphabeticCode equals to DEFAULT_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("alphabeticCode.equals=" + DEFAULT_ALPHABETIC_CODE);

        // Get all the isoCurrencyCodeList where alphabeticCode equals to UPDATED_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("alphabeticCode.equals=" + UPDATED_ALPHABETIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByAlphabeticCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where alphabeticCode not equals to DEFAULT_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("alphabeticCode.notEquals=" + DEFAULT_ALPHABETIC_CODE);

        // Get all the isoCurrencyCodeList where alphabeticCode not equals to UPDATED_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("alphabeticCode.notEquals=" + UPDATED_ALPHABETIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByAlphabeticCodeIsInShouldWork() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where alphabeticCode in DEFAULT_ALPHABETIC_CODE or UPDATED_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("alphabeticCode.in=" + DEFAULT_ALPHABETIC_CODE + "," + UPDATED_ALPHABETIC_CODE);

        // Get all the isoCurrencyCodeList where alphabeticCode equals to UPDATED_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("alphabeticCode.in=" + UPDATED_ALPHABETIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByAlphabeticCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where alphabeticCode is not null
        defaultIsoCurrencyCodeShouldBeFound("alphabeticCode.specified=true");

        // Get all the isoCurrencyCodeList where alphabeticCode is null
        defaultIsoCurrencyCodeShouldNotBeFound("alphabeticCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByAlphabeticCodeContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where alphabeticCode contains DEFAULT_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("alphabeticCode.contains=" + DEFAULT_ALPHABETIC_CODE);

        // Get all the isoCurrencyCodeList where alphabeticCode contains UPDATED_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("alphabeticCode.contains=" + UPDATED_ALPHABETIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByAlphabeticCodeNotContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where alphabeticCode does not contain DEFAULT_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("alphabeticCode.doesNotContain=" + DEFAULT_ALPHABETIC_CODE);

        // Get all the isoCurrencyCodeList where alphabeticCode does not contain UPDATED_ALPHABETIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("alphabeticCode.doesNotContain=" + UPDATED_ALPHABETIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByNumericCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where numericCode equals to DEFAULT_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("numericCode.equals=" + DEFAULT_NUMERIC_CODE);

        // Get all the isoCurrencyCodeList where numericCode equals to UPDATED_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("numericCode.equals=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByNumericCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where numericCode not equals to DEFAULT_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("numericCode.notEquals=" + DEFAULT_NUMERIC_CODE);

        // Get all the isoCurrencyCodeList where numericCode not equals to UPDATED_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("numericCode.notEquals=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByNumericCodeIsInShouldWork() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where numericCode in DEFAULT_NUMERIC_CODE or UPDATED_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("numericCode.in=" + DEFAULT_NUMERIC_CODE + "," + UPDATED_NUMERIC_CODE);

        // Get all the isoCurrencyCodeList where numericCode equals to UPDATED_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("numericCode.in=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByNumericCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where numericCode is not null
        defaultIsoCurrencyCodeShouldBeFound("numericCode.specified=true");

        // Get all the isoCurrencyCodeList where numericCode is null
        defaultIsoCurrencyCodeShouldNotBeFound("numericCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByNumericCodeContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where numericCode contains DEFAULT_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("numericCode.contains=" + DEFAULT_NUMERIC_CODE);

        // Get all the isoCurrencyCodeList where numericCode contains UPDATED_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("numericCode.contains=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByNumericCodeNotContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where numericCode does not contain DEFAULT_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldNotBeFound("numericCode.doesNotContain=" + DEFAULT_NUMERIC_CODE);

        // Get all the isoCurrencyCodeList where numericCode does not contain UPDATED_NUMERIC_CODE
        defaultIsoCurrencyCodeShouldBeFound("numericCode.doesNotContain=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByMinorUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where minorUnit equals to DEFAULT_MINOR_UNIT
        defaultIsoCurrencyCodeShouldBeFound("minorUnit.equals=" + DEFAULT_MINOR_UNIT);

        // Get all the isoCurrencyCodeList where minorUnit equals to UPDATED_MINOR_UNIT
        defaultIsoCurrencyCodeShouldNotBeFound("minorUnit.equals=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByMinorUnitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where minorUnit not equals to DEFAULT_MINOR_UNIT
        defaultIsoCurrencyCodeShouldNotBeFound("minorUnit.notEquals=" + DEFAULT_MINOR_UNIT);

        // Get all the isoCurrencyCodeList where minorUnit not equals to UPDATED_MINOR_UNIT
        defaultIsoCurrencyCodeShouldBeFound("minorUnit.notEquals=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByMinorUnitIsInShouldWork() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where minorUnit in DEFAULT_MINOR_UNIT or UPDATED_MINOR_UNIT
        defaultIsoCurrencyCodeShouldBeFound("minorUnit.in=" + DEFAULT_MINOR_UNIT + "," + UPDATED_MINOR_UNIT);

        // Get all the isoCurrencyCodeList where minorUnit equals to UPDATED_MINOR_UNIT
        defaultIsoCurrencyCodeShouldNotBeFound("minorUnit.in=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByMinorUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where minorUnit is not null
        defaultIsoCurrencyCodeShouldBeFound("minorUnit.specified=true");

        // Get all the isoCurrencyCodeList where minorUnit is null
        defaultIsoCurrencyCodeShouldNotBeFound("minorUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByMinorUnitContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where minorUnit contains DEFAULT_MINOR_UNIT
        defaultIsoCurrencyCodeShouldBeFound("minorUnit.contains=" + DEFAULT_MINOR_UNIT);

        // Get all the isoCurrencyCodeList where minorUnit contains UPDATED_MINOR_UNIT
        defaultIsoCurrencyCodeShouldNotBeFound("minorUnit.contains=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByMinorUnitNotContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where minorUnit does not contain DEFAULT_MINOR_UNIT
        defaultIsoCurrencyCodeShouldNotBeFound("minorUnit.doesNotContain=" + DEFAULT_MINOR_UNIT);

        // Get all the isoCurrencyCodeList where minorUnit does not contain UPDATED_MINOR_UNIT
        defaultIsoCurrencyCodeShouldBeFound("minorUnit.doesNotContain=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where currency equals to DEFAULT_CURRENCY
        defaultIsoCurrencyCodeShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the isoCurrencyCodeList where currency equals to UPDATED_CURRENCY
        defaultIsoCurrencyCodeShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where currency not equals to DEFAULT_CURRENCY
        defaultIsoCurrencyCodeShouldNotBeFound("currency.notEquals=" + DEFAULT_CURRENCY);

        // Get all the isoCurrencyCodeList where currency not equals to UPDATED_CURRENCY
        defaultIsoCurrencyCodeShouldBeFound("currency.notEquals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultIsoCurrencyCodeShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the isoCurrencyCodeList where currency equals to UPDATED_CURRENCY
        defaultIsoCurrencyCodeShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where currency is not null
        defaultIsoCurrencyCodeShouldBeFound("currency.specified=true");

        // Get all the isoCurrencyCodeList where currency is null
        defaultIsoCurrencyCodeShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where currency contains DEFAULT_CURRENCY
        defaultIsoCurrencyCodeShouldBeFound("currency.contains=" + DEFAULT_CURRENCY);

        // Get all the isoCurrencyCodeList where currency contains UPDATED_CURRENCY
        defaultIsoCurrencyCodeShouldNotBeFound("currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where currency does not contain DEFAULT_CURRENCY
        defaultIsoCurrencyCodeShouldNotBeFound("currency.doesNotContain=" + DEFAULT_CURRENCY);

        // Get all the isoCurrencyCodeList where currency does not contain UPDATED_CURRENCY
        defaultIsoCurrencyCodeShouldBeFound("currency.doesNotContain=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where country equals to DEFAULT_COUNTRY
        defaultIsoCurrencyCodeShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the isoCurrencyCodeList where country equals to UPDATED_COUNTRY
        defaultIsoCurrencyCodeShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where country not equals to DEFAULT_COUNTRY
        defaultIsoCurrencyCodeShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the isoCurrencyCodeList where country not equals to UPDATED_COUNTRY
        defaultIsoCurrencyCodeShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultIsoCurrencyCodeShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the isoCurrencyCodeList where country equals to UPDATED_COUNTRY
        defaultIsoCurrencyCodeShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where country is not null
        defaultIsoCurrencyCodeShouldBeFound("country.specified=true");

        // Get all the isoCurrencyCodeList where country is null
        defaultIsoCurrencyCodeShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCountryContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where country contains DEFAULT_COUNTRY
        defaultIsoCurrencyCodeShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the isoCurrencyCodeList where country contains UPDATED_COUNTRY
        defaultIsoCurrencyCodeShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllIsoCurrencyCodesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        // Get all the isoCurrencyCodeList where country does not contain DEFAULT_COUNTRY
        defaultIsoCurrencyCodeShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the isoCurrencyCodeList where country does not contain UPDATED_COUNTRY
        defaultIsoCurrencyCodeShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIsoCurrencyCodeShouldBeFound(String filter) throws Exception {
        restIsoCurrencyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isoCurrencyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].alphabeticCode").value(hasItem(DEFAULT_ALPHABETIC_CODE)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));

        // Check, that the count call also returns 1
        restIsoCurrencyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIsoCurrencyCodeShouldNotBeFound(String filter) throws Exception {
        restIsoCurrencyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIsoCurrencyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIsoCurrencyCode() throws Exception {
        // Get the isoCurrencyCode
        restIsoCurrencyCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIsoCurrencyCode() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();

        // Update the isoCurrencyCode
        IsoCurrencyCode updatedIsoCurrencyCode = isoCurrencyCodeRepository.findById(isoCurrencyCode.getId()).get();
        // Disconnect from session so that the updates on updatedIsoCurrencyCode are not directly saved in db
        em.detach(updatedIsoCurrencyCode);
        updatedIsoCurrencyCode
            .alphabeticCode(UPDATED_ALPHABETIC_CODE)
            .numericCode(UPDATED_NUMERIC_CODE)
            .minorUnit(UPDATED_MINOR_UNIT)
            .currency(UPDATED_CURRENCY)
            .country(UPDATED_COUNTRY);
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(updatedIsoCurrencyCode);

        restIsoCurrencyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isoCurrencyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);
        IsoCurrencyCode testIsoCurrencyCode = isoCurrencyCodeList.get(isoCurrencyCodeList.size() - 1);
        assertThat(testIsoCurrencyCode.getAlphabeticCode()).isEqualTo(UPDATED_ALPHABETIC_CODE);
        assertThat(testIsoCurrencyCode.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);
        assertThat(testIsoCurrencyCode.getMinorUnit()).isEqualTo(UPDATED_MINOR_UNIT);
        assertThat(testIsoCurrencyCode.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testIsoCurrencyCode.getCountry()).isEqualTo(UPDATED_COUNTRY);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository).save(testIsoCurrencyCode);
    }

    @Test
    @Transactional
    void putNonExistingIsoCurrencyCode() throws Exception {
        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();
        isoCurrencyCode.setId(count.incrementAndGet());

        // Create the IsoCurrencyCode
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsoCurrencyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, isoCurrencyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(0)).save(isoCurrencyCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchIsoCurrencyCode() throws Exception {
        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();
        isoCurrencyCode.setId(count.incrementAndGet());

        // Create the IsoCurrencyCode
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsoCurrencyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(0)).save(isoCurrencyCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIsoCurrencyCode() throws Exception {
        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();
        isoCurrencyCode.setId(count.incrementAndGet());

        // Create the IsoCurrencyCode
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsoCurrencyCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(0)).save(isoCurrencyCode);
    }

    @Test
    @Transactional
    void partialUpdateIsoCurrencyCodeWithPatch() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();

        // Update the isoCurrencyCode using partial update
        IsoCurrencyCode partialUpdatedIsoCurrencyCode = new IsoCurrencyCode();
        partialUpdatedIsoCurrencyCode.setId(isoCurrencyCode.getId());

        partialUpdatedIsoCurrencyCode.minorUnit(UPDATED_MINOR_UNIT);

        restIsoCurrencyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsoCurrencyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsoCurrencyCode))
            )
            .andExpect(status().isOk());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);
        IsoCurrencyCode testIsoCurrencyCode = isoCurrencyCodeList.get(isoCurrencyCodeList.size() - 1);
        assertThat(testIsoCurrencyCode.getAlphabeticCode()).isEqualTo(DEFAULT_ALPHABETIC_CODE);
        assertThat(testIsoCurrencyCode.getNumericCode()).isEqualTo(DEFAULT_NUMERIC_CODE);
        assertThat(testIsoCurrencyCode.getMinorUnit()).isEqualTo(UPDATED_MINOR_UNIT);
        assertThat(testIsoCurrencyCode.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testIsoCurrencyCode.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateIsoCurrencyCodeWithPatch() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();

        // Update the isoCurrencyCode using partial update
        IsoCurrencyCode partialUpdatedIsoCurrencyCode = new IsoCurrencyCode();
        partialUpdatedIsoCurrencyCode.setId(isoCurrencyCode.getId());

        partialUpdatedIsoCurrencyCode
            .alphabeticCode(UPDATED_ALPHABETIC_CODE)
            .numericCode(UPDATED_NUMERIC_CODE)
            .minorUnit(UPDATED_MINOR_UNIT)
            .currency(UPDATED_CURRENCY)
            .country(UPDATED_COUNTRY);

        restIsoCurrencyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIsoCurrencyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIsoCurrencyCode))
            )
            .andExpect(status().isOk());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);
        IsoCurrencyCode testIsoCurrencyCode = isoCurrencyCodeList.get(isoCurrencyCodeList.size() - 1);
        assertThat(testIsoCurrencyCode.getAlphabeticCode()).isEqualTo(UPDATED_ALPHABETIC_CODE);
        assertThat(testIsoCurrencyCode.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);
        assertThat(testIsoCurrencyCode.getMinorUnit()).isEqualTo(UPDATED_MINOR_UNIT);
        assertThat(testIsoCurrencyCode.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testIsoCurrencyCode.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingIsoCurrencyCode() throws Exception {
        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();
        isoCurrencyCode.setId(count.incrementAndGet());

        // Create the IsoCurrencyCode
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIsoCurrencyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, isoCurrencyCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(0)).save(isoCurrencyCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIsoCurrencyCode() throws Exception {
        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();
        isoCurrencyCode.setId(count.incrementAndGet());

        // Create the IsoCurrencyCode
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsoCurrencyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(0)).save(isoCurrencyCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIsoCurrencyCode() throws Exception {
        int databaseSizeBeforeUpdate = isoCurrencyCodeRepository.findAll().size();
        isoCurrencyCode.setId(count.incrementAndGet());

        // Create the IsoCurrencyCode
        IsoCurrencyCodeDTO isoCurrencyCodeDTO = isoCurrencyCodeMapper.toDto(isoCurrencyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIsoCurrencyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(isoCurrencyCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IsoCurrencyCode in the database
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(0)).save(isoCurrencyCode);
    }

    @Test
    @Transactional
    void deleteIsoCurrencyCode() throws Exception {
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);

        int databaseSizeBeforeDelete = isoCurrencyCodeRepository.findAll().size();

        // Delete the isoCurrencyCode
        restIsoCurrencyCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, isoCurrencyCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IsoCurrencyCode> isoCurrencyCodeList = isoCurrencyCodeRepository.findAll();
        assertThat(isoCurrencyCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IsoCurrencyCode in Elasticsearch
        verify(mockIsoCurrencyCodeSearchRepository, times(1)).deleteById(isoCurrencyCode.getId());
    }

    @Test
    @Transactional
    void searchIsoCurrencyCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        isoCurrencyCodeRepository.saveAndFlush(isoCurrencyCode);
        when(mockIsoCurrencyCodeSearchRepository.search("id:" + isoCurrencyCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(isoCurrencyCode), PageRequest.of(0, 1), 1));

        // Search the isoCurrencyCode
        restIsoCurrencyCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + isoCurrencyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isoCurrencyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].alphabeticCode").value(hasItem(DEFAULT_ALPHABETIC_CODE)))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }
}
