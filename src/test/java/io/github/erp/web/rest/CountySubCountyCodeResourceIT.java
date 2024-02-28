package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CountySubCountyCode;
import io.github.erp.repository.CountySubCountyCodeRepository;
import io.github.erp.repository.search.CountySubCountyCodeSearchRepository;
import io.github.erp.service.criteria.CountySubCountyCodeCriteria;
import io.github.erp.service.dto.CountySubCountyCodeDTO;
import io.github.erp.service.mapper.CountySubCountyCodeMapper;
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
 * Integration tests for the {@link CountySubCountyCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CountySubCountyCodeResourceIT {

    private static final String DEFAULT_SUB_COUNTY_CODE = "0140";
    private static final String UPDATED_SUB_COUNTY_CODE = "4487";

    private static final String DEFAULT_SUB_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_COUNTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY_CODE = "31";
    private static final String UPDATED_COUNTY_CODE = "67";

    private static final String DEFAULT_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/county-sub-county-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/county-sub-county-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountySubCountyCodeRepository countySubCountyCodeRepository;

    @Autowired
    private CountySubCountyCodeMapper countySubCountyCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CountySubCountyCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CountySubCountyCodeSearchRepository mockCountySubCountyCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountySubCountyCodeMockMvc;

    private CountySubCountyCode countySubCountyCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountySubCountyCode createEntity(EntityManager em) {
        CountySubCountyCode countySubCountyCode = new CountySubCountyCode()
            .subCountyCode(DEFAULT_SUB_COUNTY_CODE)
            .subCountyName(DEFAULT_SUB_COUNTY_NAME)
            .countyCode(DEFAULT_COUNTY_CODE)
            .countyName(DEFAULT_COUNTY_NAME);
        return countySubCountyCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountySubCountyCode createUpdatedEntity(EntityManager em) {
        CountySubCountyCode countySubCountyCode = new CountySubCountyCode()
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME)
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME);
        return countySubCountyCode;
    }

    @BeforeEach
    public void initTest() {
        countySubCountyCode = createEntity(em);
    }

    @Test
    @Transactional
    void createCountySubCountyCode() throws Exception {
        int databaseSizeBeforeCreate = countySubCountyCodeRepository.findAll().size();
        // Create the CountySubCountyCode
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);
        restCountySubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeCreate + 1);
        CountySubCountyCode testCountySubCountyCode = countySubCountyCodeList.get(countySubCountyCodeList.size() - 1);
        assertThat(testCountySubCountyCode.getSubCountyCode()).isEqualTo(DEFAULT_SUB_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getSubCountyName()).isEqualTo(DEFAULT_SUB_COUNTY_NAME);
        assertThat(testCountySubCountyCode.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(1)).save(testCountySubCountyCode);
    }

    @Test
    @Transactional
    void createCountySubCountyCodeWithExistingId() throws Exception {
        // Create the CountySubCountyCode with an existing ID
        countySubCountyCode.setId(1L);
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        int databaseSizeBeforeCreate = countySubCountyCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountySubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(0)).save(countySubCountyCode);
    }

    @Test
    @Transactional
    void checkSubCountyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countySubCountyCodeRepository.findAll().size();
        // set the field null
        countySubCountyCode.setSubCountyCode(null);

        // Create the CountySubCountyCode, which fails.
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        restCountySubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubCountyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countySubCountyCodeRepository.findAll().size();
        // set the field null
        countySubCountyCode.setSubCountyName(null);

        // Create the CountySubCountyCode, which fails.
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        restCountySubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countySubCountyCodeRepository.findAll().size();
        // set the field null
        countySubCountyCode.setCountyCode(null);

        // Create the CountySubCountyCode, which fails.
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        restCountySubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countySubCountyCodeRepository.findAll().size();
        // set the field null
        countySubCountyCode.setCountyName(null);

        // Create the CountySubCountyCode, which fails.
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        restCountySubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodes() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList
        restCountySubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countySubCountyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)));
    }

    @Test
    @Transactional
    void getCountySubCountyCode() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get the countySubCountyCode
        restCountySubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, countySubCountyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countySubCountyCode.getId().intValue()))
            .andExpect(jsonPath("$.subCountyCode").value(DEFAULT_SUB_COUNTY_CODE))
            .andExpect(jsonPath("$.subCountyName").value(DEFAULT_SUB_COUNTY_NAME))
            .andExpect(jsonPath("$.countyCode").value(DEFAULT_COUNTY_CODE))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME));
    }

    @Test
    @Transactional
    void getCountySubCountyCodesByIdFiltering() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        Long id = countySubCountyCode.getId();

        defaultCountySubCountyCodeShouldBeFound("id.equals=" + id);
        defaultCountySubCountyCodeShouldNotBeFound("id.notEquals=" + id);

        defaultCountySubCountyCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountySubCountyCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultCountySubCountyCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountySubCountyCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyCode equals to DEFAULT_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("subCountyCode.equals=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countySubCountyCodeList where subCountyCode equals to UPDATED_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("subCountyCode.equals=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyCode not equals to DEFAULT_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("subCountyCode.notEquals=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countySubCountyCodeList where subCountyCode not equals to UPDATED_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("subCountyCode.notEquals=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyCode in DEFAULT_SUB_COUNTY_CODE or UPDATED_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("subCountyCode.in=" + DEFAULT_SUB_COUNTY_CODE + "," + UPDATED_SUB_COUNTY_CODE);

        // Get all the countySubCountyCodeList where subCountyCode equals to UPDATED_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("subCountyCode.in=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyCode is not null
        defaultCountySubCountyCodeShouldBeFound("subCountyCode.specified=true");

        // Get all the countySubCountyCodeList where subCountyCode is null
        defaultCountySubCountyCodeShouldNotBeFound("subCountyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyCodeContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyCode contains DEFAULT_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("subCountyCode.contains=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countySubCountyCodeList where subCountyCode contains UPDATED_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("subCountyCode.contains=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyCode does not contain DEFAULT_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("subCountyCode.doesNotContain=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countySubCountyCodeList where subCountyCode does not contain UPDATED_SUB_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("subCountyCode.doesNotContain=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyName equals to DEFAULT_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("subCountyName.equals=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countySubCountyCodeList where subCountyName equals to UPDATED_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("subCountyName.equals=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyName not equals to DEFAULT_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("subCountyName.notEquals=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countySubCountyCodeList where subCountyName not equals to UPDATED_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("subCountyName.notEquals=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyName in DEFAULT_SUB_COUNTY_NAME or UPDATED_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("subCountyName.in=" + DEFAULT_SUB_COUNTY_NAME + "," + UPDATED_SUB_COUNTY_NAME);

        // Get all the countySubCountyCodeList where subCountyName equals to UPDATED_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("subCountyName.in=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyName is not null
        defaultCountySubCountyCodeShouldBeFound("subCountyName.specified=true");

        // Get all the countySubCountyCodeList where subCountyName is null
        defaultCountySubCountyCodeShouldNotBeFound("subCountyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyNameContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyName contains DEFAULT_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("subCountyName.contains=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countySubCountyCodeList where subCountyName contains UPDATED_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("subCountyName.contains=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesBySubCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where subCountyName does not contain DEFAULT_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("subCountyName.doesNotContain=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countySubCountyCodeList where subCountyName does not contain UPDATED_SUB_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("subCountyName.doesNotContain=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyCode equals to DEFAULT_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("countyCode.equals=" + DEFAULT_COUNTY_CODE);

        // Get all the countySubCountyCodeList where countyCode equals to UPDATED_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("countyCode.equals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyCode not equals to DEFAULT_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("countyCode.notEquals=" + DEFAULT_COUNTY_CODE);

        // Get all the countySubCountyCodeList where countyCode not equals to UPDATED_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("countyCode.notEquals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyCode in DEFAULT_COUNTY_CODE or UPDATED_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("countyCode.in=" + DEFAULT_COUNTY_CODE + "," + UPDATED_COUNTY_CODE);

        // Get all the countySubCountyCodeList where countyCode equals to UPDATED_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("countyCode.in=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyCode is not null
        defaultCountySubCountyCodeShouldBeFound("countyCode.specified=true");

        // Get all the countySubCountyCodeList where countyCode is null
        defaultCountySubCountyCodeShouldNotBeFound("countyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyCodeContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyCode contains DEFAULT_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("countyCode.contains=" + DEFAULT_COUNTY_CODE);

        // Get all the countySubCountyCodeList where countyCode contains UPDATED_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("countyCode.contains=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyCode does not contain DEFAULT_COUNTY_CODE
        defaultCountySubCountyCodeShouldNotBeFound("countyCode.doesNotContain=" + DEFAULT_COUNTY_CODE);

        // Get all the countySubCountyCodeList where countyCode does not contain UPDATED_COUNTY_CODE
        defaultCountySubCountyCodeShouldBeFound("countyCode.doesNotContain=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyName equals to DEFAULT_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("countyName.equals=" + DEFAULT_COUNTY_NAME);

        // Get all the countySubCountyCodeList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("countyName.equals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyName not equals to DEFAULT_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("countyName.notEquals=" + DEFAULT_COUNTY_NAME);

        // Get all the countySubCountyCodeList where countyName not equals to UPDATED_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("countyName.notEquals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyName in DEFAULT_COUNTY_NAME or UPDATED_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("countyName.in=" + DEFAULT_COUNTY_NAME + "," + UPDATED_COUNTY_NAME);

        // Get all the countySubCountyCodeList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("countyName.in=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyName is not null
        defaultCountySubCountyCodeShouldBeFound("countyName.specified=true");

        // Get all the countySubCountyCodeList where countyName is null
        defaultCountySubCountyCodeShouldNotBeFound("countyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyNameContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyName contains DEFAULT_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("countyName.contains=" + DEFAULT_COUNTY_NAME);

        // Get all the countySubCountyCodeList where countyName contains UPDATED_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("countyName.contains=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountySubCountyCodesByCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        // Get all the countySubCountyCodeList where countyName does not contain DEFAULT_COUNTY_NAME
        defaultCountySubCountyCodeShouldNotBeFound("countyName.doesNotContain=" + DEFAULT_COUNTY_NAME);

        // Get all the countySubCountyCodeList where countyName does not contain UPDATED_COUNTY_NAME
        defaultCountySubCountyCodeShouldBeFound("countyName.doesNotContain=" + UPDATED_COUNTY_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountySubCountyCodeShouldBeFound(String filter) throws Exception {
        restCountySubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countySubCountyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)));

        // Check, that the count call also returns 1
        restCountySubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountySubCountyCodeShouldNotBeFound(String filter) throws Exception {
        restCountySubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountySubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountySubCountyCode() throws Exception {
        // Get the countySubCountyCode
        restCountySubCountyCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountySubCountyCode() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();

        // Update the countySubCountyCode
        CountySubCountyCode updatedCountySubCountyCode = countySubCountyCodeRepository.findById(countySubCountyCode.getId()).get();
        // Disconnect from session so that the updates on updatedCountySubCountyCode are not directly saved in db
        em.detach(updatedCountySubCountyCode);
        updatedCountySubCountyCode
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME)
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME);
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(updatedCountySubCountyCode);

        restCountySubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countySubCountyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);
        CountySubCountyCode testCountySubCountyCode = countySubCountyCodeList.get(countySubCountyCodeList.size() - 1);
        assertThat(testCountySubCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getSubCountyName()).isEqualTo(UPDATED_SUB_COUNTY_NAME);
        assertThat(testCountySubCountyCode.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository).save(testCountySubCountyCode);
    }

    @Test
    @Transactional
    void putNonExistingCountySubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();
        countySubCountyCode.setId(count.incrementAndGet());

        // Create the CountySubCountyCode
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountySubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countySubCountyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(0)).save(countySubCountyCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountySubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();
        countySubCountyCode.setId(count.incrementAndGet());

        // Create the CountySubCountyCode
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountySubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(0)).save(countySubCountyCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountySubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();
        countySubCountyCode.setId(count.incrementAndGet());

        // Create the CountySubCountyCode
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountySubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(0)).save(countySubCountyCode);
    }

    @Test
    @Transactional
    void partialUpdateCountySubCountyCodeWithPatch() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();

        // Update the countySubCountyCode using partial update
        CountySubCountyCode partialUpdatedCountySubCountyCode = new CountySubCountyCode();
        partialUpdatedCountySubCountyCode.setId(countySubCountyCode.getId());

        partialUpdatedCountySubCountyCode
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME)
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME);

        restCountySubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountySubCountyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountySubCountyCode))
            )
            .andExpect(status().isOk());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);
        CountySubCountyCode testCountySubCountyCode = countySubCountyCodeList.get(countySubCountyCodeList.size() - 1);
        assertThat(testCountySubCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getSubCountyName()).isEqualTo(UPDATED_SUB_COUNTY_NAME);
        assertThat(testCountySubCountyCode.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCountySubCountyCodeWithPatch() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();

        // Update the countySubCountyCode using partial update
        CountySubCountyCode partialUpdatedCountySubCountyCode = new CountySubCountyCode();
        partialUpdatedCountySubCountyCode.setId(countySubCountyCode.getId());

        partialUpdatedCountySubCountyCode
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME)
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME);

        restCountySubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountySubCountyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountySubCountyCode))
            )
            .andExpect(status().isOk());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);
        CountySubCountyCode testCountySubCountyCode = countySubCountyCodeList.get(countySubCountyCodeList.size() - 1);
        assertThat(testCountySubCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getSubCountyName()).isEqualTo(UPDATED_SUB_COUNTY_NAME);
        assertThat(testCountySubCountyCode.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCountySubCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCountySubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();
        countySubCountyCode.setId(count.incrementAndGet());

        // Create the CountySubCountyCode
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountySubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countySubCountyCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(0)).save(countySubCountyCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountySubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();
        countySubCountyCode.setId(count.incrementAndGet());

        // Create the CountySubCountyCode
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountySubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(0)).save(countySubCountyCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountySubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countySubCountyCodeRepository.findAll().size();
        countySubCountyCode.setId(count.incrementAndGet());

        // Create the CountySubCountyCode
        CountySubCountyCodeDTO countySubCountyCodeDTO = countySubCountyCodeMapper.toDto(countySubCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountySubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countySubCountyCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountySubCountyCode in the database
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(0)).save(countySubCountyCode);
    }

    @Test
    @Transactional
    void deleteCountySubCountyCode() throws Exception {
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);

        int databaseSizeBeforeDelete = countySubCountyCodeRepository.findAll().size();

        // Delete the countySubCountyCode
        restCountySubCountyCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, countySubCountyCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountySubCountyCode> countySubCountyCodeList = countySubCountyCodeRepository.findAll();
        assertThat(countySubCountyCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CountySubCountyCode in Elasticsearch
        verify(mockCountySubCountyCodeSearchRepository, times(1)).deleteById(countySubCountyCode.getId());
    }

    @Test
    @Transactional
    void searchCountySubCountyCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        countySubCountyCodeRepository.saveAndFlush(countySubCountyCode);
        when(mockCountySubCountyCodeSearchRepository.search("id:" + countySubCountyCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(countySubCountyCode), PageRequest.of(0, 1), 1));

        // Search the countySubCountyCode
        restCountySubCountyCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + countySubCountyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countySubCountyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)));
    }
}
