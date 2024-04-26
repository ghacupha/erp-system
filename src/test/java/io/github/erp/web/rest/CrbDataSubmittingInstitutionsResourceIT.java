package io.github.erp.web.rest;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CrbDataSubmittingInstitutions;
import io.github.erp.repository.CrbDataSubmittingInstitutionsRepository;
import io.github.erp.repository.search.CrbDataSubmittingInstitutionsSearchRepository;
import io.github.erp.service.criteria.CrbDataSubmittingInstitutionsCriteria;
import io.github.erp.service.dto.CrbDataSubmittingInstitutionsDTO;
import io.github.erp.service.mapper.CrbDataSubmittingInstitutionsMapper;
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
 * Integration tests for the {@link CrbDataSubmittingInstitutionsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbDataSubmittingInstitutionsResourceIT {

    private static final String DEFAULT_INSTITUTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_CATEGORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-data-submitting-institutions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-data-submitting-institutions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbDataSubmittingInstitutionsRepository crbDataSubmittingInstitutionsRepository;

    @Autowired
    private CrbDataSubmittingInstitutionsMapper crbDataSubmittingInstitutionsMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbDataSubmittingInstitutionsSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbDataSubmittingInstitutionsSearchRepository mockCrbDataSubmittingInstitutionsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbDataSubmittingInstitutionsMockMvc;

    private CrbDataSubmittingInstitutions crbDataSubmittingInstitutions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbDataSubmittingInstitutions createEntity(EntityManager em) {
        CrbDataSubmittingInstitutions crbDataSubmittingInstitutions = new CrbDataSubmittingInstitutions()
            .institutionCode(DEFAULT_INSTITUTION_CODE)
            .institutionName(DEFAULT_INSTITUTION_NAME)
            .institutionCategory(DEFAULT_INSTITUTION_CATEGORY);
        return crbDataSubmittingInstitutions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbDataSubmittingInstitutions createUpdatedEntity(EntityManager em) {
        CrbDataSubmittingInstitutions crbDataSubmittingInstitutions = new CrbDataSubmittingInstitutions()
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionName(UPDATED_INSTITUTION_NAME)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY);
        return crbDataSubmittingInstitutions;
    }

    @BeforeEach
    public void initTest() {
        crbDataSubmittingInstitutions = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbDataSubmittingInstitutions() throws Exception {
        int databaseSizeBeforeCreate = crbDataSubmittingInstitutionsRepository.findAll().size();
        // Create the CrbDataSubmittingInstitutions
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeCreate + 1);
        CrbDataSubmittingInstitutions testCrbDataSubmittingInstitutions = crbDataSubmittingInstitutionsList.get(
            crbDataSubmittingInstitutionsList.size() - 1
        );
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCode()).isEqualTo(DEFAULT_INSTITUTION_CODE);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionName()).isEqualTo(DEFAULT_INSTITUTION_NAME);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCategory()).isEqualTo(DEFAULT_INSTITUTION_CATEGORY);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(1)).save(testCrbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void createCrbDataSubmittingInstitutionsWithExistingId() throws Exception {
        // Create the CrbDataSubmittingInstitutions with an existing ID
        crbDataSubmittingInstitutions.setId(1L);
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        int databaseSizeBeforeCreate = crbDataSubmittingInstitutionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(0)).save(crbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void checkInstitutionCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbDataSubmittingInstitutionsRepository.findAll().size();
        // set the field null
        crbDataSubmittingInstitutions.setInstitutionCode(null);

        // Create the CrbDataSubmittingInstitutions, which fails.
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInstitutionNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbDataSubmittingInstitutionsRepository.findAll().size();
        // set the field null
        crbDataSubmittingInstitutions.setInstitutionName(null);

        // Create the CrbDataSubmittingInstitutions, which fails.
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInstitutionCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbDataSubmittingInstitutionsRepository.findAll().size();
        // set the field null
        crbDataSubmittingInstitutions.setInstitutionCategory(null);

        // Create the CrbDataSubmittingInstitutions, which fails.
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutions() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbDataSubmittingInstitutions.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE)))
            .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)));
    }

    @Test
    @Transactional
    void getCrbDataSubmittingInstitutions() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get the crbDataSubmittingInstitutions
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(get(ENTITY_API_URL_ID, crbDataSubmittingInstitutions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbDataSubmittingInstitutions.getId().intValue()))
            .andExpect(jsonPath("$.institutionCode").value(DEFAULT_INSTITUTION_CODE))
            .andExpect(jsonPath("$.institutionName").value(DEFAULT_INSTITUTION_NAME))
            .andExpect(jsonPath("$.institutionCategory").value(DEFAULT_INSTITUTION_CATEGORY));
    }

    @Test
    @Transactional
    void getCrbDataSubmittingInstitutionsByIdFiltering() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        Long id = crbDataSubmittingInstitutions.getId();

        defaultCrbDataSubmittingInstitutionsShouldBeFound("id.equals=" + id);
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("id.notEquals=" + id);

        defaultCrbDataSubmittingInstitutionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbDataSubmittingInstitutionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode equals to DEFAULT_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCode.equals=" + DEFAULT_INSTITUTION_CODE);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode equals to UPDATED_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCode.equals=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode not equals to DEFAULT_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCode.notEquals=" + DEFAULT_INSTITUTION_CODE);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode not equals to UPDATED_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCode.notEquals=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode in DEFAULT_INSTITUTION_CODE or UPDATED_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldBeFound(
            "institutionCode.in=" + DEFAULT_INSTITUTION_CODE + "," + UPDATED_INSTITUTION_CODE
        );

        // Get all the crbDataSubmittingInstitutionsList where institutionCode equals to UPDATED_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCode.in=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode is not null
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCode.specified=true");

        // Get all the crbDataSubmittingInstitutionsList where institutionCode is null
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCodeContainsSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode contains DEFAULT_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCode.contains=" + DEFAULT_INSTITUTION_CODE);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode contains UPDATED_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCode.contains=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode does not contain DEFAULT_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCode.doesNotContain=" + DEFAULT_INSTITUTION_CODE);

        // Get all the crbDataSubmittingInstitutionsList where institutionCode does not contain UPDATED_INSTITUTION_CODE
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCode.doesNotContain=" + UPDATED_INSTITUTION_CODE);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionNameIsEqualToSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionName equals to DEFAULT_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionName.equals=" + DEFAULT_INSTITUTION_NAME);

        // Get all the crbDataSubmittingInstitutionsList where institutionName equals to UPDATED_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionName.equals=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionName not equals to DEFAULT_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionName.notEquals=" + DEFAULT_INSTITUTION_NAME);

        // Get all the crbDataSubmittingInstitutionsList where institutionName not equals to UPDATED_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionName.notEquals=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionNameIsInShouldWork() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionName in DEFAULT_INSTITUTION_NAME or UPDATED_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldBeFound(
            "institutionName.in=" + DEFAULT_INSTITUTION_NAME + "," + UPDATED_INSTITUTION_NAME
        );

        // Get all the crbDataSubmittingInstitutionsList where institutionName equals to UPDATED_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionName.in=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionName is not null
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionName.specified=true");

        // Get all the crbDataSubmittingInstitutionsList where institutionName is null
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionName.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionNameContainsSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionName contains DEFAULT_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionName.contains=" + DEFAULT_INSTITUTION_NAME);

        // Get all the crbDataSubmittingInstitutionsList where institutionName contains UPDATED_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionName.contains=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionNameNotContainsSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionName does not contain DEFAULT_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionName.doesNotContain=" + DEFAULT_INSTITUTION_NAME);

        // Get all the crbDataSubmittingInstitutionsList where institutionName does not contain UPDATED_INSTITUTION_NAME
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionName.doesNotContain=" + UPDATED_INSTITUTION_NAME);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory equals to DEFAULT_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCategory.equals=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory equals to UPDATED_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCategory.equals=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory not equals to DEFAULT_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCategory.notEquals=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory not equals to UPDATED_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCategory.notEquals=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory in DEFAULT_INSTITUTION_CATEGORY or UPDATED_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldBeFound(
            "institutionCategory.in=" + DEFAULT_INSTITUTION_CATEGORY + "," + UPDATED_INSTITUTION_CATEGORY
        );

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory equals to UPDATED_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCategory.in=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory is not null
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCategory.specified=true");

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory is null
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCategoryContainsSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory contains DEFAULT_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCategory.contains=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory contains UPDATED_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCategory.contains=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbDataSubmittingInstitutionsByInstitutionCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory does not contain DEFAULT_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldNotBeFound("institutionCategory.doesNotContain=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbDataSubmittingInstitutionsList where institutionCategory does not contain UPDATED_INSTITUTION_CATEGORY
        defaultCrbDataSubmittingInstitutionsShouldBeFound("institutionCategory.doesNotContain=" + UPDATED_INSTITUTION_CATEGORY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbDataSubmittingInstitutionsShouldBeFound(String filter) throws Exception {
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbDataSubmittingInstitutions.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE)))
            .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)));

        // Check, that the count call also returns 1
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbDataSubmittingInstitutionsShouldNotBeFound(String filter) throws Exception {
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbDataSubmittingInstitutions() throws Exception {
        // Get the crbDataSubmittingInstitutions
        restCrbDataSubmittingInstitutionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbDataSubmittingInstitutions() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();

        // Update the crbDataSubmittingInstitutions
        CrbDataSubmittingInstitutions updatedCrbDataSubmittingInstitutions = crbDataSubmittingInstitutionsRepository
            .findById(crbDataSubmittingInstitutions.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbDataSubmittingInstitutions are not directly saved in db
        em.detach(updatedCrbDataSubmittingInstitutions);
        updatedCrbDataSubmittingInstitutions
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionName(UPDATED_INSTITUTION_NAME)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY);
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            updatedCrbDataSubmittingInstitutions
        );

        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbDataSubmittingInstitutionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);
        CrbDataSubmittingInstitutions testCrbDataSubmittingInstitutions = crbDataSubmittingInstitutionsList.get(
            crbDataSubmittingInstitutionsList.size() - 1
        );
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCode()).isEqualTo(UPDATED_INSTITUTION_CODE);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionName()).isEqualTo(UPDATED_INSTITUTION_NAME);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository).save(testCrbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void putNonExistingCrbDataSubmittingInstitutions() throws Exception {
        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();
        crbDataSubmittingInstitutions.setId(count.incrementAndGet());

        // Create the CrbDataSubmittingInstitutions
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbDataSubmittingInstitutionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(0)).save(crbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbDataSubmittingInstitutions() throws Exception {
        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();
        crbDataSubmittingInstitutions.setId(count.incrementAndGet());

        // Create the CrbDataSubmittingInstitutions
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(0)).save(crbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbDataSubmittingInstitutions() throws Exception {
        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();
        crbDataSubmittingInstitutions.setId(count.incrementAndGet());

        // Create the CrbDataSubmittingInstitutions
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(0)).save(crbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void partialUpdateCrbDataSubmittingInstitutionsWithPatch() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();

        // Update the crbDataSubmittingInstitutions using partial update
        CrbDataSubmittingInstitutions partialUpdatedCrbDataSubmittingInstitutions = new CrbDataSubmittingInstitutions();
        partialUpdatedCrbDataSubmittingInstitutions.setId(crbDataSubmittingInstitutions.getId());

        partialUpdatedCrbDataSubmittingInstitutions
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY);

        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbDataSubmittingInstitutions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbDataSubmittingInstitutions))
            )
            .andExpect(status().isOk());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);
        CrbDataSubmittingInstitutions testCrbDataSubmittingInstitutions = crbDataSubmittingInstitutionsList.get(
            crbDataSubmittingInstitutionsList.size() - 1
        );
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCode()).isEqualTo(UPDATED_INSTITUTION_CODE);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionName()).isEqualTo(DEFAULT_INSTITUTION_NAME);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void fullUpdateCrbDataSubmittingInstitutionsWithPatch() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();

        // Update the crbDataSubmittingInstitutions using partial update
        CrbDataSubmittingInstitutions partialUpdatedCrbDataSubmittingInstitutions = new CrbDataSubmittingInstitutions();
        partialUpdatedCrbDataSubmittingInstitutions.setId(crbDataSubmittingInstitutions.getId());

        partialUpdatedCrbDataSubmittingInstitutions
            .institutionCode(UPDATED_INSTITUTION_CODE)
            .institutionName(UPDATED_INSTITUTION_NAME)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY);

        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbDataSubmittingInstitutions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbDataSubmittingInstitutions))
            )
            .andExpect(status().isOk());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);
        CrbDataSubmittingInstitutions testCrbDataSubmittingInstitutions = crbDataSubmittingInstitutionsList.get(
            crbDataSubmittingInstitutionsList.size() - 1
        );
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCode()).isEqualTo(UPDATED_INSTITUTION_CODE);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionName()).isEqualTo(UPDATED_INSTITUTION_NAME);
        assertThat(testCrbDataSubmittingInstitutions.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void patchNonExistingCrbDataSubmittingInstitutions() throws Exception {
        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();
        crbDataSubmittingInstitutions.setId(count.incrementAndGet());

        // Create the CrbDataSubmittingInstitutions
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbDataSubmittingInstitutionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(0)).save(crbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbDataSubmittingInstitutions() throws Exception {
        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();
        crbDataSubmittingInstitutions.setId(count.incrementAndGet());

        // Create the CrbDataSubmittingInstitutions
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(0)).save(crbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbDataSubmittingInstitutions() throws Exception {
        int databaseSizeBeforeUpdate = crbDataSubmittingInstitutionsRepository.findAll().size();
        crbDataSubmittingInstitutions.setId(count.incrementAndGet());

        // Create the CrbDataSubmittingInstitutions
        CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsMapper.toDto(
            crbDataSubmittingInstitutions
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbDataSubmittingInstitutionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbDataSubmittingInstitutions in the database
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(0)).save(crbDataSubmittingInstitutions);
    }

    @Test
    @Transactional
    void deleteCrbDataSubmittingInstitutions() throws Exception {
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);

        int databaseSizeBeforeDelete = crbDataSubmittingInstitutionsRepository.findAll().size();

        // Delete the crbDataSubmittingInstitutions
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbDataSubmittingInstitutions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbDataSubmittingInstitutions> crbDataSubmittingInstitutionsList = crbDataSubmittingInstitutionsRepository.findAll();
        assertThat(crbDataSubmittingInstitutionsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbDataSubmittingInstitutions in Elasticsearch
        verify(mockCrbDataSubmittingInstitutionsSearchRepository, times(1)).deleteById(crbDataSubmittingInstitutions.getId());
    }

    @Test
    @Transactional
    void searchCrbDataSubmittingInstitutions() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbDataSubmittingInstitutionsRepository.saveAndFlush(crbDataSubmittingInstitutions);
        when(mockCrbDataSubmittingInstitutionsSearchRepository.search("id:" + crbDataSubmittingInstitutions.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbDataSubmittingInstitutions), PageRequest.of(0, 1), 1));

        // Search the crbDataSubmittingInstitutions
        restCrbDataSubmittingInstitutionsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbDataSubmittingInstitutions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbDataSubmittingInstitutions.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE)))
            .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)));
    }
}
