package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.InstitutionCodeRepository;
import io.github.erp.repository.search.InstitutionCodeSearchRepository;
import io.github.erp.service.InstitutionCodeService;
import io.github.erp.service.criteria.InstitutionCodeCriteria;
import io.github.erp.service.dto.InstitutionCodeDTO;
import io.github.erp.service.mapper.InstitutionCodeMapper;
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

/**
 * Integration tests for the {@link InstitutionCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InstitutionCodeResourceIT {

    private static final String DEFAULT_INSTITUTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_OWNERSHIP = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_OWNERSHIP = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_LICENSED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LICENSED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_LICENSED = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_INSTITUTION_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/institution-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/institution-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstitutionCodeRepository institutionCodeRepository;

    @Mock
    private InstitutionCodeRepository institutionCodeRepositoryMock;

    @Autowired
    private InstitutionCodeMapper institutionCodeMapper;

    @Mock
    private InstitutionCodeService institutionCodeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InstitutionCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private InstitutionCodeSearchRepository mockInstitutionCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstitutionCodeMockMvc;

    private InstitutionCode institutionCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionCode createEntity(EntityManager em) {
        InstitutionCode institutionCode = new InstitutionCode()
            .institutionCode(DEFAULT_INSTITUTION_CODE)
            .institutionName(DEFAULT_INSTITUTION_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .category(DEFAULT_CATEGORY)
            .institutionCategory(DEFAULT_INSTITUTION_CATEGORY)
            .institutionOwnership(DEFAULT_INSTITUTION_OWNERSHIP)
            .dateLicensed(DEFAULT_DATE_LICENSED)
            .institutionStatus(DEFAULT_INSTITUTION_STATUS);
        return institutionCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionCode createUpdatedEntity(EntityManager em) {
        InstitutionCode institutionCode = new InstitutionCode()
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionName(UPDATED_INSTITUTION_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .category(UPDATED_CATEGORY)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY)
            .institutionOwnership(UPDATED_INSTITUTION_OWNERSHIP)
            .dateLicensed(UPDATED_DATE_LICENSED)
            .institutionStatus(UPDATED_INSTITUTION_STATUS);
        return institutionCode;
    }

    @BeforeEach
    public void initTest() {
        institutionCode = createEntity(em);
    }

    @Test
    @Transactional
    void createInstitutionCode() throws Exception {
        int databaseSizeBeforeCreate = institutionCodeRepository.findAll().size();
        // Create the InstitutionCode
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);
        restInstitutionCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeCreate + 1);
        InstitutionCode testInstitutionCode = institutionCodeList.get(institutionCodeList.size() - 1);
        assertThat(testInstitutionCode.getInstitutionCode()).isEqualTo(DEFAULT_INSTITUTION_CODE);
        assertThat(testInstitutionCode.getInstitutionName()).isEqualTo(DEFAULT_INSTITUTION_NAME);
        assertThat(testInstitutionCode.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testInstitutionCode.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionCategory()).isEqualTo(DEFAULT_INSTITUTION_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionOwnership()).isEqualTo(DEFAULT_INSTITUTION_OWNERSHIP);
        assertThat(testInstitutionCode.getDateLicensed()).isEqualTo(DEFAULT_DATE_LICENSED);
        assertThat(testInstitutionCode.getInstitutionStatus()).isEqualTo(DEFAULT_INSTITUTION_STATUS);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(1)).save(testInstitutionCode);
    }

    @Test
    @Transactional
    void createInstitutionCodeWithExistingId() throws Exception {
        // Create the InstitutionCode with an existing ID
        institutionCode.setId(1L);
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        int databaseSizeBeforeCreate = institutionCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstitutionCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(0)).save(institutionCode);
    }

    @Test
    @Transactional
    void checkInstitutionCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionCodeRepository.findAll().size();
        // set the field null
        institutionCode.setInstitutionCode(null);

        // Create the InstitutionCode, which fails.
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        restInstitutionCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInstitutionNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionCodeRepository.findAll().size();
        // set the field null
        institutionCode.setInstitutionName(null);

        // Create the InstitutionCode, which fails.
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        restInstitutionCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInstitutionCodes() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList
        restInstitutionCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE)))
            .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)))
            .andExpect(jsonPath("$.[*].institutionOwnership").value(hasItem(DEFAULT_INSTITUTION_OWNERSHIP)))
            .andExpect(jsonPath("$.[*].dateLicensed").value(hasItem(DEFAULT_DATE_LICENSED.toString())))
            .andExpect(jsonPath("$.[*].institutionStatus").value(hasItem(DEFAULT_INSTITUTION_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInstitutionCodesWithEagerRelationshipsIsEnabled() throws Exception {
        when(institutionCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInstitutionCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(institutionCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInstitutionCodesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(institutionCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInstitutionCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(institutionCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInstitutionCode() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get the institutionCode
        restInstitutionCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, institutionCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(institutionCode.getId().intValue()))
            .andExpect(jsonPath("$.institutionCode").value(DEFAULT_INSTITUTION_CODE))
            .andExpect(jsonPath("$.institutionName").value(DEFAULT_INSTITUTION_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.institutionCategory").value(DEFAULT_INSTITUTION_CATEGORY))
            .andExpect(jsonPath("$.institutionOwnership").value(DEFAULT_INSTITUTION_OWNERSHIP))
            .andExpect(jsonPath("$.dateLicensed").value(DEFAULT_DATE_LICENSED.toString()))
            .andExpect(jsonPath("$.institutionStatus").value(DEFAULT_INSTITUTION_STATUS));
    }

    @Test
    @Transactional
    void getInstitutionCodesByIdFiltering() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        Long id = institutionCode.getId();

        defaultInstitutionCodeShouldBeFound("id.equals=" + id);
        defaultInstitutionCodeShouldNotBeFound("id.notEquals=" + id);

        defaultInstitutionCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInstitutionCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultInstitutionCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInstitutionCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCode equals to DEFAULT_INSTITUTION_CODE
        defaultInstitutionCodeShouldBeFound("institutionCode.equals=" + DEFAULT_INSTITUTION_CODE);

        // Get all the institutionCodeList where institutionCode equals to UPDATED_INSTITUTION_CODE
        defaultInstitutionCodeShouldNotBeFound("institutionCode.equals=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCode not equals to DEFAULT_INSTITUTION_CODE
        defaultInstitutionCodeShouldNotBeFound("institutionCode.notEquals=" + DEFAULT_INSTITUTION_CODE);

        // Get all the institutionCodeList where institutionCode not equals to UPDATED_INSTITUTION_CODE
        defaultInstitutionCodeShouldBeFound("institutionCode.notEquals=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCode in DEFAULT_INSTITUTION_CODE or UPDATED_INSTITUTION_CODE
        defaultInstitutionCodeShouldBeFound("institutionCode.in=" + DEFAULT_INSTITUTION_CODE + "," + UPDATED_INSTITUTION_CODE);

        // Get all the institutionCodeList where institutionCode equals to UPDATED_INSTITUTION_CODE
        defaultInstitutionCodeShouldNotBeFound("institutionCode.in=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCode is not null
        defaultInstitutionCodeShouldBeFound("institutionCode.specified=true");

        // Get all the institutionCodeList where institutionCode is null
        defaultInstitutionCodeShouldNotBeFound("institutionCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCodeContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCode contains DEFAULT_INSTITUTION_CODE
        defaultInstitutionCodeShouldBeFound("institutionCode.contains=" + DEFAULT_INSTITUTION_CODE);

        // Get all the institutionCodeList where institutionCode contains UPDATED_INSTITUTION_CODE
        defaultInstitutionCodeShouldNotBeFound("institutionCode.contains=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCodeNotContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCode does not contain DEFAULT_INSTITUTION_CODE
        defaultInstitutionCodeShouldNotBeFound("institutionCode.doesNotContain=" + DEFAULT_INSTITUTION_CODE);

        // Get all the institutionCodeList where institutionCode does not contain UPDATED_INSTITUTION_CODE
        defaultInstitutionCodeShouldBeFound("institutionCode.doesNotContain=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionName equals to DEFAULT_INSTITUTION_NAME
        defaultInstitutionCodeShouldBeFound("institutionName.equals=" + DEFAULT_INSTITUTION_NAME);

        // Get all the institutionCodeList where institutionName equals to UPDATED_INSTITUTION_NAME
        defaultInstitutionCodeShouldNotBeFound("institutionName.equals=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionName not equals to DEFAULT_INSTITUTION_NAME
        defaultInstitutionCodeShouldNotBeFound("institutionName.notEquals=" + DEFAULT_INSTITUTION_NAME);

        // Get all the institutionCodeList where institutionName not equals to UPDATED_INSTITUTION_NAME
        defaultInstitutionCodeShouldBeFound("institutionName.notEquals=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionNameIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionName in DEFAULT_INSTITUTION_NAME or UPDATED_INSTITUTION_NAME
        defaultInstitutionCodeShouldBeFound("institutionName.in=" + DEFAULT_INSTITUTION_NAME + "," + UPDATED_INSTITUTION_NAME);

        // Get all the institutionCodeList where institutionName equals to UPDATED_INSTITUTION_NAME
        defaultInstitutionCodeShouldNotBeFound("institutionName.in=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionName is not null
        defaultInstitutionCodeShouldBeFound("institutionName.specified=true");

        // Get all the institutionCodeList where institutionName is null
        defaultInstitutionCodeShouldNotBeFound("institutionName.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionNameContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionName contains DEFAULT_INSTITUTION_NAME
        defaultInstitutionCodeShouldBeFound("institutionName.contains=" + DEFAULT_INSTITUTION_NAME);

        // Get all the institutionCodeList where institutionName contains UPDATED_INSTITUTION_NAME
        defaultInstitutionCodeShouldNotBeFound("institutionName.contains=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionNameNotContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionName does not contain DEFAULT_INSTITUTION_NAME
        defaultInstitutionCodeShouldNotBeFound("institutionName.doesNotContain=" + DEFAULT_INSTITUTION_NAME);

        // Get all the institutionCodeList where institutionName does not contain UPDATED_INSTITUTION_NAME
        defaultInstitutionCodeShouldBeFound("institutionName.doesNotContain=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where shortName equals to DEFAULT_SHORT_NAME
        defaultInstitutionCodeShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the institutionCodeList where shortName equals to UPDATED_SHORT_NAME
        defaultInstitutionCodeShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByShortNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where shortName not equals to DEFAULT_SHORT_NAME
        defaultInstitutionCodeShouldNotBeFound("shortName.notEquals=" + DEFAULT_SHORT_NAME);

        // Get all the institutionCodeList where shortName not equals to UPDATED_SHORT_NAME
        defaultInstitutionCodeShouldBeFound("shortName.notEquals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultInstitutionCodeShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the institutionCodeList where shortName equals to UPDATED_SHORT_NAME
        defaultInstitutionCodeShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where shortName is not null
        defaultInstitutionCodeShouldBeFound("shortName.specified=true");

        // Get all the institutionCodeList where shortName is null
        defaultInstitutionCodeShouldNotBeFound("shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByShortNameContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where shortName contains DEFAULT_SHORT_NAME
        defaultInstitutionCodeShouldBeFound("shortName.contains=" + DEFAULT_SHORT_NAME);

        // Get all the institutionCodeList where shortName contains UPDATED_SHORT_NAME
        defaultInstitutionCodeShouldNotBeFound("shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where shortName does not contain DEFAULT_SHORT_NAME
        defaultInstitutionCodeShouldNotBeFound("shortName.doesNotContain=" + DEFAULT_SHORT_NAME);

        // Get all the institutionCodeList where shortName does not contain UPDATED_SHORT_NAME
        defaultInstitutionCodeShouldBeFound("shortName.doesNotContain=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where category equals to DEFAULT_CATEGORY
        defaultInstitutionCodeShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the institutionCodeList where category equals to UPDATED_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where category not equals to DEFAULT_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("category.notEquals=" + DEFAULT_CATEGORY);

        // Get all the institutionCodeList where category not equals to UPDATED_CATEGORY
        defaultInstitutionCodeShouldBeFound("category.notEquals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultInstitutionCodeShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the institutionCodeList where category equals to UPDATED_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where category is not null
        defaultInstitutionCodeShouldBeFound("category.specified=true");

        // Get all the institutionCodeList where category is null
        defaultInstitutionCodeShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByCategoryContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where category contains DEFAULT_CATEGORY
        defaultInstitutionCodeShouldBeFound("category.contains=" + DEFAULT_CATEGORY);

        // Get all the institutionCodeList where category contains UPDATED_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where category does not contain DEFAULT_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("category.doesNotContain=" + DEFAULT_CATEGORY);

        // Get all the institutionCodeList where category does not contain UPDATED_CATEGORY
        defaultInstitutionCodeShouldBeFound("category.doesNotContain=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCategory equals to DEFAULT_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldBeFound("institutionCategory.equals=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the institutionCodeList where institutionCategory equals to UPDATED_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("institutionCategory.equals=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCategory not equals to DEFAULT_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("institutionCategory.notEquals=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the institutionCodeList where institutionCategory not equals to UPDATED_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldBeFound("institutionCategory.notEquals=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCategory in DEFAULT_INSTITUTION_CATEGORY or UPDATED_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldBeFound("institutionCategory.in=" + DEFAULT_INSTITUTION_CATEGORY + "," + UPDATED_INSTITUTION_CATEGORY);

        // Get all the institutionCodeList where institutionCategory equals to UPDATED_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("institutionCategory.in=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCategory is not null
        defaultInstitutionCodeShouldBeFound("institutionCategory.specified=true");

        // Get all the institutionCodeList where institutionCategory is null
        defaultInstitutionCodeShouldNotBeFound("institutionCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCategoryContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCategory contains DEFAULT_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldBeFound("institutionCategory.contains=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the institutionCodeList where institutionCategory contains UPDATED_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("institutionCategory.contains=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionCategory does not contain DEFAULT_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldNotBeFound("institutionCategory.doesNotContain=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the institutionCodeList where institutionCategory does not contain UPDATED_INSTITUTION_CATEGORY
        defaultInstitutionCodeShouldBeFound("institutionCategory.doesNotContain=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionOwnershipIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionOwnership equals to DEFAULT_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldBeFound("institutionOwnership.equals=" + DEFAULT_INSTITUTION_OWNERSHIP);

        // Get all the institutionCodeList where institutionOwnership equals to UPDATED_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldNotBeFound("institutionOwnership.equals=" + UPDATED_INSTITUTION_OWNERSHIP);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionOwnershipIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionOwnership not equals to DEFAULT_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldNotBeFound("institutionOwnership.notEquals=" + DEFAULT_INSTITUTION_OWNERSHIP);

        // Get all the institutionCodeList where institutionOwnership not equals to UPDATED_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldBeFound("institutionOwnership.notEquals=" + UPDATED_INSTITUTION_OWNERSHIP);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionOwnershipIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionOwnership in DEFAULT_INSTITUTION_OWNERSHIP or UPDATED_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldBeFound(
            "institutionOwnership.in=" + DEFAULT_INSTITUTION_OWNERSHIP + "," + UPDATED_INSTITUTION_OWNERSHIP
        );

        // Get all the institutionCodeList where institutionOwnership equals to UPDATED_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldNotBeFound("institutionOwnership.in=" + UPDATED_INSTITUTION_OWNERSHIP);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionOwnershipIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionOwnership is not null
        defaultInstitutionCodeShouldBeFound("institutionOwnership.specified=true");

        // Get all the institutionCodeList where institutionOwnership is null
        defaultInstitutionCodeShouldNotBeFound("institutionOwnership.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionOwnershipContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionOwnership contains DEFAULT_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldBeFound("institutionOwnership.contains=" + DEFAULT_INSTITUTION_OWNERSHIP);

        // Get all the institutionCodeList where institutionOwnership contains UPDATED_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldNotBeFound("institutionOwnership.contains=" + UPDATED_INSTITUTION_OWNERSHIP);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionOwnershipNotContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionOwnership does not contain DEFAULT_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldNotBeFound("institutionOwnership.doesNotContain=" + DEFAULT_INSTITUTION_OWNERSHIP);

        // Get all the institutionCodeList where institutionOwnership does not contain UPDATED_INSTITUTION_OWNERSHIP
        defaultInstitutionCodeShouldBeFound("institutionOwnership.doesNotContain=" + UPDATED_INSTITUTION_OWNERSHIP);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed equals to DEFAULT_DATE_LICENSED
        defaultInstitutionCodeShouldBeFound("dateLicensed.equals=" + DEFAULT_DATE_LICENSED);

        // Get all the institutionCodeList where dateLicensed equals to UPDATED_DATE_LICENSED
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.equals=" + UPDATED_DATE_LICENSED);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed not equals to DEFAULT_DATE_LICENSED
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.notEquals=" + DEFAULT_DATE_LICENSED);

        // Get all the institutionCodeList where dateLicensed not equals to UPDATED_DATE_LICENSED
        defaultInstitutionCodeShouldBeFound("dateLicensed.notEquals=" + UPDATED_DATE_LICENSED);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed in DEFAULT_DATE_LICENSED or UPDATED_DATE_LICENSED
        defaultInstitutionCodeShouldBeFound("dateLicensed.in=" + DEFAULT_DATE_LICENSED + "," + UPDATED_DATE_LICENSED);

        // Get all the institutionCodeList where dateLicensed equals to UPDATED_DATE_LICENSED
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.in=" + UPDATED_DATE_LICENSED);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed is not null
        defaultInstitutionCodeShouldBeFound("dateLicensed.specified=true");

        // Get all the institutionCodeList where dateLicensed is null
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed is greater than or equal to DEFAULT_DATE_LICENSED
        defaultInstitutionCodeShouldBeFound("dateLicensed.greaterThanOrEqual=" + DEFAULT_DATE_LICENSED);

        // Get all the institutionCodeList where dateLicensed is greater than or equal to UPDATED_DATE_LICENSED
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.greaterThanOrEqual=" + UPDATED_DATE_LICENSED);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed is less than or equal to DEFAULT_DATE_LICENSED
        defaultInstitutionCodeShouldBeFound("dateLicensed.lessThanOrEqual=" + DEFAULT_DATE_LICENSED);

        // Get all the institutionCodeList where dateLicensed is less than or equal to SMALLER_DATE_LICENSED
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.lessThanOrEqual=" + SMALLER_DATE_LICENSED);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsLessThanSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed is less than DEFAULT_DATE_LICENSED
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.lessThan=" + DEFAULT_DATE_LICENSED);

        // Get all the institutionCodeList where dateLicensed is less than UPDATED_DATE_LICENSED
        defaultInstitutionCodeShouldBeFound("dateLicensed.lessThan=" + UPDATED_DATE_LICENSED);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByDateLicensedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where dateLicensed is greater than DEFAULT_DATE_LICENSED
        defaultInstitutionCodeShouldNotBeFound("dateLicensed.greaterThan=" + DEFAULT_DATE_LICENSED);

        // Get all the institutionCodeList where dateLicensed is greater than SMALLER_DATE_LICENSED
        defaultInstitutionCodeShouldBeFound("dateLicensed.greaterThan=" + SMALLER_DATE_LICENSED);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionStatus equals to DEFAULT_INSTITUTION_STATUS
        defaultInstitutionCodeShouldBeFound("institutionStatus.equals=" + DEFAULT_INSTITUTION_STATUS);

        // Get all the institutionCodeList where institutionStatus equals to UPDATED_INSTITUTION_STATUS
        defaultInstitutionCodeShouldNotBeFound("institutionStatus.equals=" + UPDATED_INSTITUTION_STATUS);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionStatus not equals to DEFAULT_INSTITUTION_STATUS
        defaultInstitutionCodeShouldNotBeFound("institutionStatus.notEquals=" + DEFAULT_INSTITUTION_STATUS);

        // Get all the institutionCodeList where institutionStatus not equals to UPDATED_INSTITUTION_STATUS
        defaultInstitutionCodeShouldBeFound("institutionStatus.notEquals=" + UPDATED_INSTITUTION_STATUS);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionStatus in DEFAULT_INSTITUTION_STATUS or UPDATED_INSTITUTION_STATUS
        defaultInstitutionCodeShouldBeFound("institutionStatus.in=" + DEFAULT_INSTITUTION_STATUS + "," + UPDATED_INSTITUTION_STATUS);

        // Get all the institutionCodeList where institutionStatus equals to UPDATED_INSTITUTION_STATUS
        defaultInstitutionCodeShouldNotBeFound("institutionStatus.in=" + UPDATED_INSTITUTION_STATUS);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionStatus is not null
        defaultInstitutionCodeShouldBeFound("institutionStatus.specified=true");

        // Get all the institutionCodeList where institutionStatus is null
        defaultInstitutionCodeShouldNotBeFound("institutionStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionStatusContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionStatus contains DEFAULT_INSTITUTION_STATUS
        defaultInstitutionCodeShouldBeFound("institutionStatus.contains=" + DEFAULT_INSTITUTION_STATUS);

        // Get all the institutionCodeList where institutionStatus contains UPDATED_INSTITUTION_STATUS
        defaultInstitutionCodeShouldNotBeFound("institutionStatus.contains=" + UPDATED_INSTITUTION_STATUS);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByInstitutionStatusNotContainsSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        // Get all the institutionCodeList where institutionStatus does not contain DEFAULT_INSTITUTION_STATUS
        defaultInstitutionCodeShouldNotBeFound("institutionStatus.doesNotContain=" + DEFAULT_INSTITUTION_STATUS);

        // Get all the institutionCodeList where institutionStatus does not contain UPDATED_INSTITUTION_STATUS
        defaultInstitutionCodeShouldBeFound("institutionStatus.doesNotContain=" + UPDATED_INSTITUTION_STATUS);
    }

    @Test
    @Transactional
    void getAllInstitutionCodesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);
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
        institutionCode.addPlaceholder(placeholder);
        institutionCodeRepository.saveAndFlush(institutionCode);
        Long placeholderId = placeholder.getId();

        // Get all the institutionCodeList where placeholder equals to placeholderId
        defaultInstitutionCodeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the institutionCodeList where placeholder equals to (placeholderId + 1)
        defaultInstitutionCodeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInstitutionCodeShouldBeFound(String filter) throws Exception {
        restInstitutionCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE)))
            .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)))
            .andExpect(jsonPath("$.[*].institutionOwnership").value(hasItem(DEFAULT_INSTITUTION_OWNERSHIP)))
            .andExpect(jsonPath("$.[*].dateLicensed").value(hasItem(DEFAULT_DATE_LICENSED.toString())))
            .andExpect(jsonPath("$.[*].institutionStatus").value(hasItem(DEFAULT_INSTITUTION_STATUS)));

        // Check, that the count call also returns 1
        restInstitutionCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInstitutionCodeShouldNotBeFound(String filter) throws Exception {
        restInstitutionCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInstitutionCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInstitutionCode() throws Exception {
        // Get the institutionCode
        restInstitutionCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstitutionCode() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();

        // Update the institutionCode
        InstitutionCode updatedInstitutionCode = institutionCodeRepository.findById(institutionCode.getId()).get();
        // Disconnect from session so that the updates on updatedInstitutionCode are not directly saved in db
        em.detach(updatedInstitutionCode);
        updatedInstitutionCode
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionName(UPDATED_INSTITUTION_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .category(UPDATED_CATEGORY)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY)
            .institutionOwnership(UPDATED_INSTITUTION_OWNERSHIP)
            .dateLicensed(UPDATED_DATE_LICENSED)
            .institutionStatus(UPDATED_INSTITUTION_STATUS);
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(updatedInstitutionCode);

        restInstitutionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionCode testInstitutionCode = institutionCodeList.get(institutionCodeList.size() - 1);
        assertThat(testInstitutionCode.getInstitutionCode()).isEqualTo(UPDATED_INSTITUTION_CODE);
        assertThat(testInstitutionCode.getInstitutionName()).isEqualTo(UPDATED_INSTITUTION_NAME);
        assertThat(testInstitutionCode.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testInstitutionCode.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionOwnership()).isEqualTo(UPDATED_INSTITUTION_OWNERSHIP);
        assertThat(testInstitutionCode.getDateLicensed()).isEqualTo(UPDATED_DATE_LICENSED);
        assertThat(testInstitutionCode.getInstitutionStatus()).isEqualTo(UPDATED_INSTITUTION_STATUS);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository).save(testInstitutionCode);
    }

    @Test
    @Transactional
    void putNonExistingInstitutionCode() throws Exception {
        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();
        institutionCode.setId(count.incrementAndGet());

        // Create the InstitutionCode
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, institutionCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(0)).save(institutionCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstitutionCode() throws Exception {
        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();
        institutionCode.setId(count.incrementAndGet());

        // Create the InstitutionCode
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(0)).save(institutionCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstitutionCode() throws Exception {
        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();
        institutionCode.setId(count.incrementAndGet());

        // Create the InstitutionCode
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(0)).save(institutionCode);
    }

    @Test
    @Transactional
    void partialUpdateInstitutionCodeWithPatch() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();

        // Update the institutionCode using partial update
        InstitutionCode partialUpdatedInstitutionCode = new InstitutionCode();
        partialUpdatedInstitutionCode.setId(institutionCode.getId());

        partialUpdatedInstitutionCode
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionName(UPDATED_INSTITUTION_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .institutionOwnership(UPDATED_INSTITUTION_OWNERSHIP);

        restInstitutionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitutionCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionCode))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionCode testInstitutionCode = institutionCodeList.get(institutionCodeList.size() - 1);
        assertThat(testInstitutionCode.getInstitutionCode()).isEqualTo(UPDATED_INSTITUTION_CODE);
        assertThat(testInstitutionCode.getInstitutionName()).isEqualTo(UPDATED_INSTITUTION_NAME);
        assertThat(testInstitutionCode.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testInstitutionCode.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionCategory()).isEqualTo(DEFAULT_INSTITUTION_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionOwnership()).isEqualTo(UPDATED_INSTITUTION_OWNERSHIP);
        assertThat(testInstitutionCode.getDateLicensed()).isEqualTo(DEFAULT_DATE_LICENSED);
        assertThat(testInstitutionCode.getInstitutionStatus()).isEqualTo(DEFAULT_INSTITUTION_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateInstitutionCodeWithPatch() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();

        // Update the institutionCode using partial update
        InstitutionCode partialUpdatedInstitutionCode = new InstitutionCode();
        partialUpdatedInstitutionCode.setId(institutionCode.getId());

        partialUpdatedInstitutionCode
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionName(UPDATED_INSTITUTION_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .category(UPDATED_CATEGORY)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY)
            .institutionOwnership(UPDATED_INSTITUTION_OWNERSHIP)
            .dateLicensed(UPDATED_DATE_LICENSED)
            .institutionStatus(UPDATED_INSTITUTION_STATUS);

        restInstitutionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstitutionCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstitutionCode))
            )
            .andExpect(status().isOk());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);
        InstitutionCode testInstitutionCode = institutionCodeList.get(institutionCodeList.size() - 1);
        assertThat(testInstitutionCode.getInstitutionCode()).isEqualTo(UPDATED_INSTITUTION_CODE);
        assertThat(testInstitutionCode.getInstitutionName()).isEqualTo(UPDATED_INSTITUTION_NAME);
        assertThat(testInstitutionCode.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testInstitutionCode.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);
        assertThat(testInstitutionCode.getInstitutionOwnership()).isEqualTo(UPDATED_INSTITUTION_OWNERSHIP);
        assertThat(testInstitutionCode.getDateLicensed()).isEqualTo(UPDATED_DATE_LICENSED);
        assertThat(testInstitutionCode.getInstitutionStatus()).isEqualTo(UPDATED_INSTITUTION_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingInstitutionCode() throws Exception {
        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();
        institutionCode.setId(count.incrementAndGet());

        // Create the InstitutionCode
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstitutionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, institutionCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(0)).save(institutionCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstitutionCode() throws Exception {
        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();
        institutionCode.setId(count.incrementAndGet());

        // Create the InstitutionCode
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(0)).save(institutionCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstitutionCode() throws Exception {
        int databaseSizeBeforeUpdate = institutionCodeRepository.findAll().size();
        institutionCode.setId(count.incrementAndGet());

        // Create the InstitutionCode
        InstitutionCodeDTO institutionCodeDTO = institutionCodeMapper.toDto(institutionCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstitutionCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(institutionCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InstitutionCode in the database
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(0)).save(institutionCode);
    }

    @Test
    @Transactional
    void deleteInstitutionCode() throws Exception {
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);

        int databaseSizeBeforeDelete = institutionCodeRepository.findAll().size();

        // Delete the institutionCode
        restInstitutionCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, institutionCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InstitutionCode> institutionCodeList = institutionCodeRepository.findAll();
        assertThat(institutionCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InstitutionCode in Elasticsearch
        verify(mockInstitutionCodeSearchRepository, times(1)).deleteById(institutionCode.getId());
    }

    @Test
    @Transactional
    void searchInstitutionCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        institutionCodeRepository.saveAndFlush(institutionCode);
        when(mockInstitutionCodeSearchRepository.search("id:" + institutionCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(institutionCode), PageRequest.of(0, 1), 1));

        // Search the institutionCode
        restInstitutionCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + institutionCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE)))
            .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)))
            .andExpect(jsonPath("$.[*].institutionOwnership").value(hasItem(DEFAULT_INSTITUTION_OWNERSHIP)))
            .andExpect(jsonPath("$.[*].dateLicensed").value(hasItem(DEFAULT_DATE_LICENSED.toString())))
            .andExpect(jsonPath("$.[*].institutionStatus").value(hasItem(DEFAULT_INSTITUTION_STATUS)));
    }
}
