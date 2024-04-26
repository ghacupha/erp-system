package io.github.erp.erp.resources.gdi;

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
import io.github.erp.domain.CrbAgingBands;
import io.github.erp.repository.CrbAgingBandsRepository;
import io.github.erp.repository.search.CrbAgingBandsSearchRepository;
import io.github.erp.service.dto.CrbAgingBandsDTO;
import io.github.erp.service.mapper.CrbAgingBandsMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CrbAgingBandsResource;
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

/**
 * Integration tests for the {@link CrbAgingBandsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CrbAgingBandsResourceIT {

    private static final String DEFAULT_AGING_BAND_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AGING_BAND_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AGING_BAND_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_AGING_BAND_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_AGING_BAND_CATEGORY_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_AGING_BAND_CATEGORY_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/crb-aging-bands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/crb-aging-bands";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbAgingBandsRepository crbAgingBandsRepository;

    @Autowired
    private CrbAgingBandsMapper crbAgingBandsMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbAgingBandsSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbAgingBandsSearchRepository mockCrbAgingBandsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbAgingBandsMockMvc;

    private CrbAgingBands crbAgingBands;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAgingBands createEntity(EntityManager em) {
        CrbAgingBands crbAgingBands = new CrbAgingBands()
            .agingBandCategoryCode(DEFAULT_AGING_BAND_CATEGORY_CODE)
            .agingBandCategory(DEFAULT_AGING_BAND_CATEGORY)
            .agingBandCategoryDetails(DEFAULT_AGING_BAND_CATEGORY_DETAILS);
        return crbAgingBands;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAgingBands createUpdatedEntity(EntityManager em) {
        CrbAgingBands crbAgingBands = new CrbAgingBands()
            .agingBandCategoryCode(UPDATED_AGING_BAND_CATEGORY_CODE)
            .agingBandCategory(UPDATED_AGING_BAND_CATEGORY)
            .agingBandCategoryDetails(UPDATED_AGING_BAND_CATEGORY_DETAILS);
        return crbAgingBands;
    }

    @BeforeEach
    public void initTest() {
        crbAgingBands = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbAgingBands() throws Exception {
        int databaseSizeBeforeCreate = crbAgingBandsRepository.findAll().size();
        // Create the CrbAgingBands
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);
        restCrbAgingBandsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeCreate + 1);
        CrbAgingBands testCrbAgingBands = crbAgingBandsList.get(crbAgingBandsList.size() - 1);
        assertThat(testCrbAgingBands.getAgingBandCategoryCode()).isEqualTo(DEFAULT_AGING_BAND_CATEGORY_CODE);
        assertThat(testCrbAgingBands.getAgingBandCategory()).isEqualTo(DEFAULT_AGING_BAND_CATEGORY);
        assertThat(testCrbAgingBands.getAgingBandCategoryDetails()).isEqualTo(DEFAULT_AGING_BAND_CATEGORY_DETAILS);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(1)).save(testCrbAgingBands);
    }

    @Test
    @Transactional
    void createCrbAgingBandsWithExistingId() throws Exception {
        // Create the CrbAgingBands with an existing ID
        crbAgingBands.setId(1L);
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        int databaseSizeBeforeCreate = crbAgingBandsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbAgingBandsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(0)).save(crbAgingBands);
    }

    @Test
    @Transactional
    void checkAgingBandCategoryCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAgingBandsRepository.findAll().size();
        // set the field null
        crbAgingBands.setAgingBandCategoryCode(null);

        // Create the CrbAgingBands, which fails.
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        restCrbAgingBandsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgingBandCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAgingBandsRepository.findAll().size();
        // set the field null
        crbAgingBands.setAgingBandCategory(null);

        // Create the CrbAgingBands, which fails.
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        restCrbAgingBandsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgingBandCategoryDetailsIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAgingBandsRepository.findAll().size();
        // set the field null
        crbAgingBands.setAgingBandCategoryDetails(null);

        // Create the CrbAgingBands, which fails.
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        restCrbAgingBandsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbAgingBands() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList
        restCrbAgingBandsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAgingBands.getId().intValue())))
            .andExpect(jsonPath("$.[*].agingBandCategoryCode").value(hasItem(DEFAULT_AGING_BAND_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].agingBandCategory").value(hasItem(DEFAULT_AGING_BAND_CATEGORY)))
            .andExpect(jsonPath("$.[*].agingBandCategoryDetails").value(hasItem(DEFAULT_AGING_BAND_CATEGORY_DETAILS)));
    }

    @Test
    @Transactional
    void getCrbAgingBands() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get the crbAgingBands
        restCrbAgingBandsMockMvc
            .perform(get(ENTITY_API_URL_ID, crbAgingBands.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbAgingBands.getId().intValue()))
            .andExpect(jsonPath("$.agingBandCategoryCode").value(DEFAULT_AGING_BAND_CATEGORY_CODE))
            .andExpect(jsonPath("$.agingBandCategory").value(DEFAULT_AGING_BAND_CATEGORY))
            .andExpect(jsonPath("$.agingBandCategoryDetails").value(DEFAULT_AGING_BAND_CATEGORY_DETAILS));
    }

    @Test
    @Transactional
    void getCrbAgingBandsByIdFiltering() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        Long id = crbAgingBands.getId();

        defaultCrbAgingBandsShouldBeFound("id.equals=" + id);
        defaultCrbAgingBandsShouldNotBeFound("id.notEquals=" + id);

        defaultCrbAgingBandsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbAgingBandsShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbAgingBandsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbAgingBandsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryCode equals to DEFAULT_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryCode.equals=" + DEFAULT_AGING_BAND_CATEGORY_CODE);

        // Get all the crbAgingBandsList where agingBandCategoryCode equals to UPDATED_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryCode.equals=" + UPDATED_AGING_BAND_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryCode not equals to DEFAULT_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryCode.notEquals=" + DEFAULT_AGING_BAND_CATEGORY_CODE);

        // Get all the crbAgingBandsList where agingBandCategoryCode not equals to UPDATED_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryCode.notEquals=" + UPDATED_AGING_BAND_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryCode in DEFAULT_AGING_BAND_CATEGORY_CODE or UPDATED_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldBeFound(
            "agingBandCategoryCode.in=" + DEFAULT_AGING_BAND_CATEGORY_CODE + "," + UPDATED_AGING_BAND_CATEGORY_CODE
        );

        // Get all the crbAgingBandsList where agingBandCategoryCode equals to UPDATED_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryCode.in=" + UPDATED_AGING_BAND_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryCode is not null
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryCode.specified=true");

        // Get all the crbAgingBandsList where agingBandCategoryCode is null
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryCode contains DEFAULT_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryCode.contains=" + DEFAULT_AGING_BAND_CATEGORY_CODE);

        // Get all the crbAgingBandsList where agingBandCategoryCode contains UPDATED_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryCode.contains=" + UPDATED_AGING_BAND_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryCode does not contain DEFAULT_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryCode.doesNotContain=" + DEFAULT_AGING_BAND_CATEGORY_CODE);

        // Get all the crbAgingBandsList where agingBandCategoryCode does not contain UPDATED_AGING_BAND_CATEGORY_CODE
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryCode.doesNotContain=" + UPDATED_AGING_BAND_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategory equals to DEFAULT_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldBeFound("agingBandCategory.equals=" + DEFAULT_AGING_BAND_CATEGORY);

        // Get all the crbAgingBandsList where agingBandCategory equals to UPDATED_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategory.equals=" + UPDATED_AGING_BAND_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategory not equals to DEFAULT_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategory.notEquals=" + DEFAULT_AGING_BAND_CATEGORY);

        // Get all the crbAgingBandsList where agingBandCategory not equals to UPDATED_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldBeFound("agingBandCategory.notEquals=" + UPDATED_AGING_BAND_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategory in DEFAULT_AGING_BAND_CATEGORY or UPDATED_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldBeFound("agingBandCategory.in=" + DEFAULT_AGING_BAND_CATEGORY + "," + UPDATED_AGING_BAND_CATEGORY);

        // Get all the crbAgingBandsList where agingBandCategory equals to UPDATED_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategory.in=" + UPDATED_AGING_BAND_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategory is not null
        defaultCrbAgingBandsShouldBeFound("agingBandCategory.specified=true");

        // Get all the crbAgingBandsList where agingBandCategory is null
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryContainsSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategory contains DEFAULT_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldBeFound("agingBandCategory.contains=" + DEFAULT_AGING_BAND_CATEGORY);

        // Get all the crbAgingBandsList where agingBandCategory contains UPDATED_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategory.contains=" + UPDATED_AGING_BAND_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategory does not contain DEFAULT_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategory.doesNotContain=" + DEFAULT_AGING_BAND_CATEGORY);

        // Get all the crbAgingBandsList where agingBandCategory does not contain UPDATED_AGING_BAND_CATEGORY
        defaultCrbAgingBandsShouldBeFound("agingBandCategory.doesNotContain=" + UPDATED_AGING_BAND_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryDetails equals to DEFAULT_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryDetails.equals=" + DEFAULT_AGING_BAND_CATEGORY_DETAILS);

        // Get all the crbAgingBandsList where agingBandCategoryDetails equals to UPDATED_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryDetails.equals=" + UPDATED_AGING_BAND_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryDetails not equals to DEFAULT_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryDetails.notEquals=" + DEFAULT_AGING_BAND_CATEGORY_DETAILS);

        // Get all the crbAgingBandsList where agingBandCategoryDetails not equals to UPDATED_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryDetails.notEquals=" + UPDATED_AGING_BAND_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryDetails in DEFAULT_AGING_BAND_CATEGORY_DETAILS or UPDATED_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldBeFound(
            "agingBandCategoryDetails.in=" + DEFAULT_AGING_BAND_CATEGORY_DETAILS + "," + UPDATED_AGING_BAND_CATEGORY_DETAILS
        );

        // Get all the crbAgingBandsList where agingBandCategoryDetails equals to UPDATED_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryDetails.in=" + UPDATED_AGING_BAND_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryDetails is not null
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryDetails.specified=true");

        // Get all the crbAgingBandsList where agingBandCategoryDetails is null
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryDetailsContainsSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryDetails contains DEFAULT_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryDetails.contains=" + DEFAULT_AGING_BAND_CATEGORY_DETAILS);

        // Get all the crbAgingBandsList where agingBandCategoryDetails contains UPDATED_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryDetails.contains=" + UPDATED_AGING_BAND_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void getAllCrbAgingBandsByAgingBandCategoryDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        // Get all the crbAgingBandsList where agingBandCategoryDetails does not contain DEFAULT_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldNotBeFound("agingBandCategoryDetails.doesNotContain=" + DEFAULT_AGING_BAND_CATEGORY_DETAILS);

        // Get all the crbAgingBandsList where agingBandCategoryDetails does not contain UPDATED_AGING_BAND_CATEGORY_DETAILS
        defaultCrbAgingBandsShouldBeFound("agingBandCategoryDetails.doesNotContain=" + UPDATED_AGING_BAND_CATEGORY_DETAILS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbAgingBandsShouldBeFound(String filter) throws Exception {
        restCrbAgingBandsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAgingBands.getId().intValue())))
            .andExpect(jsonPath("$.[*].agingBandCategoryCode").value(hasItem(DEFAULT_AGING_BAND_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].agingBandCategory").value(hasItem(DEFAULT_AGING_BAND_CATEGORY)))
            .andExpect(jsonPath("$.[*].agingBandCategoryDetails").value(hasItem(DEFAULT_AGING_BAND_CATEGORY_DETAILS)));

        // Check, that the count call also returns 1
        restCrbAgingBandsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbAgingBandsShouldNotBeFound(String filter) throws Exception {
        restCrbAgingBandsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbAgingBandsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbAgingBands() throws Exception {
        // Get the crbAgingBands
        restCrbAgingBandsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbAgingBands() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();

        // Update the crbAgingBands
        CrbAgingBands updatedCrbAgingBands = crbAgingBandsRepository.findById(crbAgingBands.getId()).get();
        // Disconnect from session so that the updates on updatedCrbAgingBands are not directly saved in db
        em.detach(updatedCrbAgingBands);
        updatedCrbAgingBands
            .agingBandCategoryCode(UPDATED_AGING_BAND_CATEGORY_CODE)
            .agingBandCategory(UPDATED_AGING_BAND_CATEGORY)
            .agingBandCategoryDetails(UPDATED_AGING_BAND_CATEGORY_DETAILS);
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(updatedCrbAgingBands);

        restCrbAgingBandsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAgingBandsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);
        CrbAgingBands testCrbAgingBands = crbAgingBandsList.get(crbAgingBandsList.size() - 1);
        assertThat(testCrbAgingBands.getAgingBandCategoryCode()).isEqualTo(UPDATED_AGING_BAND_CATEGORY_CODE);
        assertThat(testCrbAgingBands.getAgingBandCategory()).isEqualTo(UPDATED_AGING_BAND_CATEGORY);
        assertThat(testCrbAgingBands.getAgingBandCategoryDetails()).isEqualTo(UPDATED_AGING_BAND_CATEGORY_DETAILS);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository).save(testCrbAgingBands);
    }

    @Test
    @Transactional
    void putNonExistingCrbAgingBands() throws Exception {
        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();
        crbAgingBands.setId(count.incrementAndGet());

        // Create the CrbAgingBands
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAgingBandsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAgingBandsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(0)).save(crbAgingBands);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbAgingBands() throws Exception {
        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();
        crbAgingBands.setId(count.incrementAndGet());

        // Create the CrbAgingBands
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgingBandsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(0)).save(crbAgingBands);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbAgingBands() throws Exception {
        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();
        crbAgingBands.setId(count.incrementAndGet());

        // Create the CrbAgingBands
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgingBandsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(0)).save(crbAgingBands);
    }

    @Test
    @Transactional
    void partialUpdateCrbAgingBandsWithPatch() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();

        // Update the crbAgingBands using partial update
        CrbAgingBands partialUpdatedCrbAgingBands = new CrbAgingBands();
        partialUpdatedCrbAgingBands.setId(crbAgingBands.getId());

        partialUpdatedCrbAgingBands.agingBandCategoryCode(UPDATED_AGING_BAND_CATEGORY_CODE).agingBandCategory(UPDATED_AGING_BAND_CATEGORY);

        restCrbAgingBandsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAgingBands.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAgingBands))
            )
            .andExpect(status().isOk());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);
        CrbAgingBands testCrbAgingBands = crbAgingBandsList.get(crbAgingBandsList.size() - 1);
        assertThat(testCrbAgingBands.getAgingBandCategoryCode()).isEqualTo(UPDATED_AGING_BAND_CATEGORY_CODE);
        assertThat(testCrbAgingBands.getAgingBandCategory()).isEqualTo(UPDATED_AGING_BAND_CATEGORY);
        assertThat(testCrbAgingBands.getAgingBandCategoryDetails()).isEqualTo(DEFAULT_AGING_BAND_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbAgingBandsWithPatch() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();

        // Update the crbAgingBands using partial update
        CrbAgingBands partialUpdatedCrbAgingBands = new CrbAgingBands();
        partialUpdatedCrbAgingBands.setId(crbAgingBands.getId());

        partialUpdatedCrbAgingBands
            .agingBandCategoryCode(UPDATED_AGING_BAND_CATEGORY_CODE)
            .agingBandCategory(UPDATED_AGING_BAND_CATEGORY)
            .agingBandCategoryDetails(UPDATED_AGING_BAND_CATEGORY_DETAILS);

        restCrbAgingBandsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAgingBands.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAgingBands))
            )
            .andExpect(status().isOk());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);
        CrbAgingBands testCrbAgingBands = crbAgingBandsList.get(crbAgingBandsList.size() - 1);
        assertThat(testCrbAgingBands.getAgingBandCategoryCode()).isEqualTo(UPDATED_AGING_BAND_CATEGORY_CODE);
        assertThat(testCrbAgingBands.getAgingBandCategory()).isEqualTo(UPDATED_AGING_BAND_CATEGORY);
        assertThat(testCrbAgingBands.getAgingBandCategoryDetails()).isEqualTo(UPDATED_AGING_BAND_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbAgingBands() throws Exception {
        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();
        crbAgingBands.setId(count.incrementAndGet());

        // Create the CrbAgingBands
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAgingBandsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbAgingBandsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(0)).save(crbAgingBands);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbAgingBands() throws Exception {
        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();
        crbAgingBands.setId(count.incrementAndGet());

        // Create the CrbAgingBands
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgingBandsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(0)).save(crbAgingBands);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbAgingBands() throws Exception {
        int databaseSizeBeforeUpdate = crbAgingBandsRepository.findAll().size();
        crbAgingBands.setId(count.incrementAndGet());

        // Create the CrbAgingBands
        CrbAgingBandsDTO crbAgingBandsDTO = crbAgingBandsMapper.toDto(crbAgingBands);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAgingBandsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAgingBandsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAgingBands in the database
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(0)).save(crbAgingBands);
    }

    @Test
    @Transactional
    void deleteCrbAgingBands() throws Exception {
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);

        int databaseSizeBeforeDelete = crbAgingBandsRepository.findAll().size();

        // Delete the crbAgingBands
        restCrbAgingBandsMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbAgingBands.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbAgingBands> crbAgingBandsList = crbAgingBandsRepository.findAll();
        assertThat(crbAgingBandsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbAgingBands in Elasticsearch
        verify(mockCrbAgingBandsSearchRepository, times(1)).deleteById(crbAgingBands.getId());
    }

    @Test
    @Transactional
    void searchCrbAgingBands() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbAgingBandsRepository.saveAndFlush(crbAgingBands);
        when(mockCrbAgingBandsSearchRepository.search("id:" + crbAgingBands.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbAgingBands), PageRequest.of(0, 1), 1));

        // Search the crbAgingBands
        restCrbAgingBandsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbAgingBands.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAgingBands.getId().intValue())))
            .andExpect(jsonPath("$.[*].agingBandCategoryCode").value(hasItem(DEFAULT_AGING_BAND_CATEGORY_CODE)))
            .andExpect(jsonPath("$.[*].agingBandCategory").value(hasItem(DEFAULT_AGING_BAND_CATEGORY)))
            .andExpect(jsonPath("$.[*].agingBandCategoryDetails").value(hasItem(DEFAULT_AGING_BAND_CATEGORY_DETAILS)));
    }
}
