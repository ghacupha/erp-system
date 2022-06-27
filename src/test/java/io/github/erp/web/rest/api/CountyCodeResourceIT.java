package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 11 (Artaxerxes Series)
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.CountyCode;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.CountyCodeRepository;
import io.github.erp.repository.search.CountyCodeSearchRepository;
import io.github.erp.service.CountyCodeService;
import io.github.erp.service.dto.CountyCodeDTO;
import io.github.erp.service.mapper.CountyCodeMapper;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
 * Integration tests for the {@link CountyCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class CountyCodeResourceIT {

    private static final Integer DEFAULT_COUNTY_CODE = 1;
    private static final Integer UPDATED_COUNTY_CODE = 2;
    private static final Integer SMALLER_COUNTY_CODE = 1 - 1;

    private static final String DEFAULT_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SUB_COUNTY_CODE = 1;
    private static final Integer UPDATED_SUB_COUNTY_CODE = 2;
    private static final Integer SMALLER_SUB_COUNTY_CODE = 1 - 1;

    private static final String DEFAULT_SUB_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_COUNTY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/county-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/county-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountyCodeRepository countyCodeRepository;

    @Mock
    private CountyCodeRepository countyCodeRepositoryMock;

    @Autowired
    private CountyCodeMapper countyCodeMapper;

    @Mock
    private CountyCodeService countyCodeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CountyCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CountyCodeSearchRepository mockCountyCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountyCodeMockMvc;

    private CountyCode countyCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyCode createEntity(EntityManager em) {
        CountyCode countyCode = new CountyCode()
            .countyCode(DEFAULT_COUNTY_CODE)
            .countyName(DEFAULT_COUNTY_NAME)
            .subCountyCode(DEFAULT_SUB_COUNTY_CODE)
            .subCountyName(DEFAULT_SUB_COUNTY_NAME);
        return countyCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountyCode createUpdatedEntity(EntityManager em) {
        CountyCode countyCode = new CountyCode()
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME);
        return countyCode;
    }

    @BeforeEach
    public void initTest() {
        countyCode = createEntity(em);
    }

    @Test
    @Transactional
    void createCountyCode() throws Exception {
        int databaseSizeBeforeCreate = countyCodeRepository.findAll().size();
        // Create the CountyCode
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);
        restCountyCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeCreate + 1);
        CountyCode testCountyCode = countyCodeList.get(countyCodeList.size() - 1);
        assertThat(testCountyCode.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
        assertThat(testCountyCode.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testCountyCode.getSubCountyCode()).isEqualTo(DEFAULT_SUB_COUNTY_CODE);
        assertThat(testCountyCode.getSubCountyName()).isEqualTo(DEFAULT_SUB_COUNTY_NAME);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(1)).save(testCountyCode);
    }

    @Test
    @Transactional
    void createCountyCodeWithExistingId() throws Exception {
        // Create the CountyCode with an existing ID
        countyCode.setId(1L);
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        int databaseSizeBeforeCreate = countyCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(0)).save(countyCode);
    }

    @Test
    @Transactional
    void checkCountyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyCodeRepository.findAll().size();
        // set the field null
        countyCode.setCountyCode(null);

        // Create the CountyCode, which fails.
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        restCountyCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyCodeRepository.findAll().size();
        // set the field null
        countyCode.setCountyName(null);

        // Create the CountyCode, which fails.
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        restCountyCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubCountyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyCodeRepository.findAll().size();
        // set the field null
        countyCode.setSubCountyCode(null);

        // Create the CountyCode, which fails.
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        restCountyCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubCountyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countyCodeRepository.findAll().size();
        // set the field null
        countyCode.setSubCountyName(null);

        // Create the CountyCode, which fails.
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        restCountyCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountyCodes() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList
        restCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCountyCodesWithEagerRelationshipsIsEnabled() throws Exception {
        when(countyCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCountyCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(countyCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCountyCodesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(countyCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCountyCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(countyCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCountyCode() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get the countyCode
        restCountyCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, countyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countyCode.getId().intValue()))
            .andExpect(jsonPath("$.countyCode").value(DEFAULT_COUNTY_CODE))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME))
            .andExpect(jsonPath("$.subCountyCode").value(DEFAULT_SUB_COUNTY_CODE))
            .andExpect(jsonPath("$.subCountyName").value(DEFAULT_SUB_COUNTY_NAME));
    }

    @Test
    @Transactional
    void getCountyCodesByIdFiltering() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        Long id = countyCode.getId();

        defaultCountyCodeShouldBeFound("id.equals=" + id);
        defaultCountyCodeShouldNotBeFound("id.notEquals=" + id);

        defaultCountyCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountyCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultCountyCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountyCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode equals to DEFAULT_COUNTY_CODE
        defaultCountyCodeShouldBeFound("countyCode.equals=" + DEFAULT_COUNTY_CODE);

        // Get all the countyCodeList where countyCode equals to UPDATED_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("countyCode.equals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode not equals to DEFAULT_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("countyCode.notEquals=" + DEFAULT_COUNTY_CODE);

        // Get all the countyCodeList where countyCode not equals to UPDATED_COUNTY_CODE
        defaultCountyCodeShouldBeFound("countyCode.notEquals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode in DEFAULT_COUNTY_CODE or UPDATED_COUNTY_CODE
        defaultCountyCodeShouldBeFound("countyCode.in=" + DEFAULT_COUNTY_CODE + "," + UPDATED_COUNTY_CODE);

        // Get all the countyCodeList where countyCode equals to UPDATED_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("countyCode.in=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode is not null
        defaultCountyCodeShouldBeFound("countyCode.specified=true");

        // Get all the countyCodeList where countyCode is null
        defaultCountyCodeShouldNotBeFound("countyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode is greater than or equal to DEFAULT_COUNTY_CODE
        defaultCountyCodeShouldBeFound("countyCode.greaterThanOrEqual=" + DEFAULT_COUNTY_CODE);

        // Get all the countyCodeList where countyCode is greater than or equal to UPDATED_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("countyCode.greaterThanOrEqual=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode is less than or equal to DEFAULT_COUNTY_CODE
        defaultCountyCodeShouldBeFound("countyCode.lessThanOrEqual=" + DEFAULT_COUNTY_CODE);

        // Get all the countyCodeList where countyCode is less than or equal to SMALLER_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("countyCode.lessThanOrEqual=" + SMALLER_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode is less than DEFAULT_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("countyCode.lessThan=" + DEFAULT_COUNTY_CODE);

        // Get all the countyCodeList where countyCode is less than UPDATED_COUNTY_CODE
        defaultCountyCodeShouldBeFound("countyCode.lessThan=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyCode is greater than DEFAULT_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("countyCode.greaterThan=" + DEFAULT_COUNTY_CODE);

        // Get all the countyCodeList where countyCode is greater than SMALLER_COUNTY_CODE
        defaultCountyCodeShouldBeFound("countyCode.greaterThan=" + SMALLER_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyName equals to DEFAULT_COUNTY_NAME
        defaultCountyCodeShouldBeFound("countyName.equals=" + DEFAULT_COUNTY_NAME);

        // Get all the countyCodeList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("countyName.equals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyName not equals to DEFAULT_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("countyName.notEquals=" + DEFAULT_COUNTY_NAME);

        // Get all the countyCodeList where countyName not equals to UPDATED_COUNTY_NAME
        defaultCountyCodeShouldBeFound("countyName.notEquals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyName in DEFAULT_COUNTY_NAME or UPDATED_COUNTY_NAME
        defaultCountyCodeShouldBeFound("countyName.in=" + DEFAULT_COUNTY_NAME + "," + UPDATED_COUNTY_NAME);

        // Get all the countyCodeList where countyName equals to UPDATED_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("countyName.in=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyName is not null
        defaultCountyCodeShouldBeFound("countyName.specified=true");

        // Get all the countyCodeList where countyName is null
        defaultCountyCodeShouldNotBeFound("countyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyNameContainsSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyName contains DEFAULT_COUNTY_NAME
        defaultCountyCodeShouldBeFound("countyName.contains=" + DEFAULT_COUNTY_NAME);

        // Get all the countyCodeList where countyName contains UPDATED_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("countyName.contains=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesByCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where countyName does not contain DEFAULT_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("countyName.doesNotContain=" + DEFAULT_COUNTY_NAME);

        // Get all the countyCodeList where countyName does not contain UPDATED_COUNTY_NAME
        defaultCountyCodeShouldBeFound("countyName.doesNotContain=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode equals to DEFAULT_SUB_COUNTY_CODE
        defaultCountyCodeShouldBeFound("subCountyCode.equals=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countyCodeList where subCountyCode equals to UPDATED_SUB_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("subCountyCode.equals=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode not equals to DEFAULT_SUB_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("subCountyCode.notEquals=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countyCodeList where subCountyCode not equals to UPDATED_SUB_COUNTY_CODE
        defaultCountyCodeShouldBeFound("subCountyCode.notEquals=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode in DEFAULT_SUB_COUNTY_CODE or UPDATED_SUB_COUNTY_CODE
        defaultCountyCodeShouldBeFound("subCountyCode.in=" + DEFAULT_SUB_COUNTY_CODE + "," + UPDATED_SUB_COUNTY_CODE);

        // Get all the countyCodeList where subCountyCode equals to UPDATED_SUB_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("subCountyCode.in=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode is not null
        defaultCountyCodeShouldBeFound("subCountyCode.specified=true");

        // Get all the countyCodeList where subCountyCode is null
        defaultCountyCodeShouldNotBeFound("subCountyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode is greater than or equal to DEFAULT_SUB_COUNTY_CODE
        defaultCountyCodeShouldBeFound("subCountyCode.greaterThanOrEqual=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countyCodeList where subCountyCode is greater than or equal to UPDATED_SUB_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("subCountyCode.greaterThanOrEqual=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode is less than or equal to DEFAULT_SUB_COUNTY_CODE
        defaultCountyCodeShouldBeFound("subCountyCode.lessThanOrEqual=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countyCodeList where subCountyCode is less than or equal to SMALLER_SUB_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("subCountyCode.lessThanOrEqual=" + SMALLER_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode is less than DEFAULT_SUB_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("subCountyCode.lessThan=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countyCodeList where subCountyCode is less than UPDATED_SUB_COUNTY_CODE
        defaultCountyCodeShouldBeFound("subCountyCode.lessThan=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyCode is greater than DEFAULT_SUB_COUNTY_CODE
        defaultCountyCodeShouldNotBeFound("subCountyCode.greaterThan=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the countyCodeList where subCountyCode is greater than SMALLER_SUB_COUNTY_CODE
        defaultCountyCodeShouldBeFound("subCountyCode.greaterThan=" + SMALLER_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyName equals to DEFAULT_SUB_COUNTY_NAME
        defaultCountyCodeShouldBeFound("subCountyName.equals=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countyCodeList where subCountyName equals to UPDATED_SUB_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("subCountyName.equals=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyName not equals to DEFAULT_SUB_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("subCountyName.notEquals=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countyCodeList where subCountyName not equals to UPDATED_SUB_COUNTY_NAME
        defaultCountyCodeShouldBeFound("subCountyName.notEquals=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyName in DEFAULT_SUB_COUNTY_NAME or UPDATED_SUB_COUNTY_NAME
        defaultCountyCodeShouldBeFound("subCountyName.in=" + DEFAULT_SUB_COUNTY_NAME + "," + UPDATED_SUB_COUNTY_NAME);

        // Get all the countyCodeList where subCountyName equals to UPDATED_SUB_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("subCountyName.in=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyName is not null
        defaultCountyCodeShouldBeFound("subCountyName.specified=true");

        // Get all the countyCodeList where subCountyName is null
        defaultCountyCodeShouldNotBeFound("subCountyName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyNameContainsSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyName contains DEFAULT_SUB_COUNTY_NAME
        defaultCountyCodeShouldBeFound("subCountyName.contains=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countyCodeList where subCountyName contains UPDATED_SUB_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("subCountyName.contains=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesBySubCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        // Get all the countyCodeList where subCountyName does not contain DEFAULT_SUB_COUNTY_NAME
        defaultCountyCodeShouldNotBeFound("subCountyName.doesNotContain=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the countyCodeList where subCountyName does not contain UPDATED_SUB_COUNTY_NAME
        defaultCountyCodeShouldBeFound("subCountyName.doesNotContain=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllCountyCodesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);
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
        countyCode.addPlaceholder(placeholder);
        countyCodeRepository.saveAndFlush(countyCode);
        Long placeholderId = placeholder.getId();

        // Get all the countyCodeList where placeholder equals to placeholderId
        defaultCountyCodeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the countyCodeList where placeholder equals to (placeholderId + 1)
        defaultCountyCodeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountyCodeShouldBeFound(String filter) throws Exception {
        restCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)));

        // Check, that the count call also returns 1
        restCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountyCodeShouldNotBeFound(String filter) throws Exception {
        restCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountyCode() throws Exception {
        // Get the countyCode
        restCountyCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountyCode() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();

        // Update the countyCode
        CountyCode updatedCountyCode = countyCodeRepository.findById(countyCode.getId()).get();
        // Disconnect from session so that the updates on updatedCountyCode are not directly saved in db
        em.detach(updatedCountyCode);
        updatedCountyCode
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME);
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(updatedCountyCode);

        restCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);
        CountyCode testCountyCode = countyCodeList.get(countyCodeList.size() - 1);
        assertThat(testCountyCode.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testCountyCode.getSubCountyName()).isEqualTo(UPDATED_SUB_COUNTY_NAME);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository).save(testCountyCode);
    }

    @Test
    @Transactional
    void putNonExistingCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();
        countyCode.setId(count.incrementAndGet());

        // Create the CountyCode
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(0)).save(countyCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();
        countyCode.setId(count.incrementAndGet());

        // Create the CountyCode
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(0)).save(countyCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();
        countyCode.setId(count.incrementAndGet());

        // Create the CountyCode
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countyCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(0)).save(countyCode);
    }

    @Test
    @Transactional
    void partialUpdateCountyCodeWithPatch() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();

        // Update the countyCode using partial update
        CountyCode partialUpdatedCountyCode = new CountyCode();
        partialUpdatedCountyCode.setId(countyCode.getId());

        partialUpdatedCountyCode.subCountyCode(UPDATED_SUB_COUNTY_CODE);

        restCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyCode))
            )
            .andExpect(status().isOk());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);
        CountyCode testCountyCode = countyCodeList.get(countyCodeList.size() - 1);
        assertThat(testCountyCode.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
        assertThat(testCountyCode.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testCountyCode.getSubCountyName()).isEqualTo(DEFAULT_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCountyCodeWithPatch() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();

        // Update the countyCode using partial update
        CountyCode partialUpdatedCountyCode = new CountyCode();
        partialUpdatedCountyCode.setId(countyCode.getId());

        partialUpdatedCountyCode
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME);

        restCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountyCode))
            )
            .andExpect(status().isOk());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);
        CountyCode testCountyCode = countyCodeList.get(countyCodeList.size() - 1);
        assertThat(testCountyCode.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testCountyCode.getSubCountyName()).isEqualTo(UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();
        countyCode.setId(count.incrementAndGet());

        // Create the CountyCode
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countyCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(0)).save(countyCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();
        countyCode.setId(count.incrementAndGet());

        // Create the CountyCode
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(0)).save(countyCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = countyCodeRepository.findAll().size();
        countyCode.setId(count.incrementAndGet());

        // Create the CountyCode
        CountyCodeDTO countyCodeDTO = countyCodeMapper.toDto(countyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countyCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountyCode in the database
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(0)).save(countyCode);
    }

    @Test
    @Transactional
    void deleteCountyCode() throws Exception {
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);

        int databaseSizeBeforeDelete = countyCodeRepository.findAll().size();

        // Delete the countyCode
        restCountyCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, countyCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CountyCode> countyCodeList = countyCodeRepository.findAll();
        assertThat(countyCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CountyCode in Elasticsearch
        verify(mockCountyCodeSearchRepository, times(1)).deleteById(countyCode.getId());
    }

    @Test
    @Transactional
    void searchCountyCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        countyCodeRepository.saveAndFlush(countyCode);
        when(mockCountyCodeSearchRepository.search("id:" + countyCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(countyCode), PageRequest.of(0, 1), 1));

        // Search the countyCode
        restCountyCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + countyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)));
    }
}
