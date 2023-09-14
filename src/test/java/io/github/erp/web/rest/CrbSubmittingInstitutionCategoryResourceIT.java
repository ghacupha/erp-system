package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.CrbSubmittingInstitutionCategory;
import io.github.erp.repository.CrbSubmittingInstitutionCategoryRepository;
import io.github.erp.repository.search.CrbSubmittingInstitutionCategorySearchRepository;
import io.github.erp.service.criteria.CrbSubmittingInstitutionCategoryCriteria;
import io.github.erp.service.dto.CrbSubmittingInstitutionCategoryDTO;
import io.github.erp.service.mapper.CrbSubmittingInstitutionCategoryMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CrbSubmittingInstitutionCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbSubmittingInstitutionCategoryResourceIT {

    private static final String DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_SUBMITTING_INSTITUTION_CATEGORY_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-submitting-institution-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-submitting-institution-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbSubmittingInstitutionCategoryRepository crbSubmittingInstitutionCategoryRepository;

    @Autowired
    private CrbSubmittingInstitutionCategoryMapper crbSubmittingInstitutionCategoryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbSubmittingInstitutionCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbSubmittingInstitutionCategorySearchRepository mockCrbSubmittingInstitutionCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbSubmittingInstitutionCategoryMockMvc;

    private CrbSubmittingInstitutionCategory crbSubmittingInstitutionCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbSubmittingInstitutionCategory createEntity(EntityManager em) {
        CrbSubmittingInstitutionCategory crbSubmittingInstitutionCategory = new CrbSubmittingInstitutionCategory()
            .submittingInstitutionCategoryTypeCode(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE)
            .submittingInstitutionCategoryType(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE)
            .submittingInstitutionCategoryDetails(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);
        return crbSubmittingInstitutionCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbSubmittingInstitutionCategory createUpdatedEntity(EntityManager em) {
        CrbSubmittingInstitutionCategory crbSubmittingInstitutionCategory = new CrbSubmittingInstitutionCategory()
            .submittingInstitutionCategoryTypeCode(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE)
            .submittingInstitutionCategoryType(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE)
            .submittingInstitutionCategoryDetails(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);
        return crbSubmittingInstitutionCategory;
    }

    @BeforeEach
    public void initTest() {
        crbSubmittingInstitutionCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbSubmittingInstitutionCategory() throws Exception {
        int databaseSizeBeforeCreate = crbSubmittingInstitutionCategoryRepository.findAll().size();
        // Create the CrbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        CrbSubmittingInstitutionCategory testCrbSubmittingInstitutionCategory = crbSubmittingInstitutionCategoryList.get(
            crbSubmittingInstitutionCategoryList.size() - 1
        );
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryTypeCode())
            .isEqualTo(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryType())
            .isEqualTo(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryDetails())
            .isEqualTo(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(1)).save(testCrbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void createCrbSubmittingInstitutionCategoryWithExistingId() throws Exception {
        // Create the CrbSubmittingInstitutionCategory with an existing ID
        crbSubmittingInstitutionCategory.setId(1L);
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        int databaseSizeBeforeCreate = crbSubmittingInstitutionCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(0)).save(crbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void checkSubmittingInstitutionCategoryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbSubmittingInstitutionCategoryRepository.findAll().size();
        // set the field null
        crbSubmittingInstitutionCategory.setSubmittingInstitutionCategoryTypeCode(null);

        // Create the CrbSubmittingInstitutionCategory, which fails.
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmittingInstitutionCategoryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbSubmittingInstitutionCategoryRepository.findAll().size();
        // set the field null
        crbSubmittingInstitutionCategory.setSubmittingInstitutionCategoryType(null);

        // Create the CrbSubmittingInstitutionCategory, which fails.
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategories() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSubmittingInstitutionCategory.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].submittingInstitutionCategoryTypeCode").value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].submittingInstitutionCategoryType").value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE)))
            .andExpect(
                jsonPath("$.[*].submittingInstitutionCategoryDetails")
                    .value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS.toString()))
            );
    }

    @Test
    @Transactional
    void getCrbSubmittingInstitutionCategory() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get the crbSubmittingInstitutionCategory
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, crbSubmittingInstitutionCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbSubmittingInstitutionCategory.getId().intValue()))
            .andExpect(jsonPath("$.submittingInstitutionCategoryTypeCode").value(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE))
            .andExpect(jsonPath("$.submittingInstitutionCategoryType").value(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE))
            .andExpect(
                jsonPath("$.submittingInstitutionCategoryDetails").value(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS.toString())
            );
    }

    @Test
    @Transactional
    void getCrbSubmittingInstitutionCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        Long id = crbSubmittingInstitutionCategory.getId();

        defaultCrbSubmittingInstitutionCategoryShouldBeFound("id.equals=" + id);
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCrbSubmittingInstitutionCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbSubmittingInstitutionCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode equals to DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryTypeCode.equals=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode equals to UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryTypeCode.equals=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode not equals to DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryTypeCode.notEquals=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode not equals to UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryTypeCode.notEquals=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode in DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE or UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryTypeCode.in=" +
            DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE +
            "," +
            UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode equals to UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryTypeCode.in=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode is not null
        defaultCrbSubmittingInstitutionCategoryShouldBeFound("submittingInstitutionCategoryTypeCode.specified=true");

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode is null
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound("submittingInstitutionCategoryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode contains DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryTypeCode.contains=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode contains UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryTypeCode.contains=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode does not contain DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryTypeCode.doesNotContain=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryTypeCode does not contain UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryTypeCode.doesNotContain=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType equals to DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryType.equals=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType equals to UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryType.equals=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType not equals to DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryType.notEquals=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType not equals to UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryType.notEquals=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType in DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE or UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryType.in=" +
            DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE +
            "," +
            UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType equals to UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryType.in=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType is not null
        defaultCrbSubmittingInstitutionCategoryShouldBeFound("submittingInstitutionCategoryType.specified=true");

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType is null
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound("submittingInstitutionCategoryType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeContainsSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType contains DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryType.contains=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType contains UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryType.contains=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbSubmittingInstitutionCategoriesBySubmittingInstitutionCategoryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType does not contain DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(
            "submittingInstitutionCategoryType.doesNotContain=" + DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );

        // Get all the crbSubmittingInstitutionCategoryList where submittingInstitutionCategoryType does not contain UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        defaultCrbSubmittingInstitutionCategoryShouldBeFound(
            "submittingInstitutionCategoryType.doesNotContain=" + UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbSubmittingInstitutionCategoryShouldBeFound(String filter) throws Exception {
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSubmittingInstitutionCategory.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].submittingInstitutionCategoryTypeCode").value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].submittingInstitutionCategoryType").value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE)))
            .andExpect(
                jsonPath("$.[*].submittingInstitutionCategoryDetails")
                    .value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS.toString()))
            );

        // Check, that the count call also returns 1
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbSubmittingInstitutionCategoryShouldNotBeFound(String filter) throws Exception {
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbSubmittingInstitutionCategory() throws Exception {
        // Get the crbSubmittingInstitutionCategory
        restCrbSubmittingInstitutionCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbSubmittingInstitutionCategory() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();

        // Update the crbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategory updatedCrbSubmittingInstitutionCategory = crbSubmittingInstitutionCategoryRepository
            .findById(crbSubmittingInstitutionCategory.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbSubmittingInstitutionCategory are not directly saved in db
        em.detach(updatedCrbSubmittingInstitutionCategory);
        updatedCrbSubmittingInstitutionCategory
            .submittingInstitutionCategoryTypeCode(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE)
            .submittingInstitutionCategoryType(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE)
            .submittingInstitutionCategoryDetails(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            updatedCrbSubmittingInstitutionCategory
        );

        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbSubmittingInstitutionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);
        CrbSubmittingInstitutionCategory testCrbSubmittingInstitutionCategory = crbSubmittingInstitutionCategoryList.get(
            crbSubmittingInstitutionCategoryList.size() - 1
        );
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryTypeCode())
            .isEqualTo(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryType())
            .isEqualTo(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryDetails())
            .isEqualTo(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository).save(testCrbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void putNonExistingCrbSubmittingInstitutionCategory() throws Exception {
        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();
        crbSubmittingInstitutionCategory.setId(count.incrementAndGet());

        // Create the CrbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbSubmittingInstitutionCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(0)).save(crbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbSubmittingInstitutionCategory() throws Exception {
        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();
        crbSubmittingInstitutionCategory.setId(count.incrementAndGet());

        // Create the CrbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(0)).save(crbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbSubmittingInstitutionCategory() throws Exception {
        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();
        crbSubmittingInstitutionCategory.setId(count.incrementAndGet());

        // Create the CrbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(0)).save(crbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void partialUpdateCrbSubmittingInstitutionCategoryWithPatch() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();

        // Update the crbSubmittingInstitutionCategory using partial update
        CrbSubmittingInstitutionCategory partialUpdatedCrbSubmittingInstitutionCategory = new CrbSubmittingInstitutionCategory();
        partialUpdatedCrbSubmittingInstitutionCategory.setId(crbSubmittingInstitutionCategory.getId());

        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbSubmittingInstitutionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbSubmittingInstitutionCategory))
            )
            .andExpect(status().isOk());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);
        CrbSubmittingInstitutionCategory testCrbSubmittingInstitutionCategory = crbSubmittingInstitutionCategoryList.get(
            crbSubmittingInstitutionCategoryList.size() - 1
        );
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryTypeCode())
            .isEqualTo(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryType())
            .isEqualTo(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryDetails())
            .isEqualTo(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbSubmittingInstitutionCategoryWithPatch() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();

        // Update the crbSubmittingInstitutionCategory using partial update
        CrbSubmittingInstitutionCategory partialUpdatedCrbSubmittingInstitutionCategory = new CrbSubmittingInstitutionCategory();
        partialUpdatedCrbSubmittingInstitutionCategory.setId(crbSubmittingInstitutionCategory.getId());

        partialUpdatedCrbSubmittingInstitutionCategory
            .submittingInstitutionCategoryTypeCode(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE)
            .submittingInstitutionCategoryType(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE)
            .submittingInstitutionCategoryDetails(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);

        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbSubmittingInstitutionCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbSubmittingInstitutionCategory))
            )
            .andExpect(status().isOk());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);
        CrbSubmittingInstitutionCategory testCrbSubmittingInstitutionCategory = crbSubmittingInstitutionCategoryList.get(
            crbSubmittingInstitutionCategoryList.size() - 1
        );
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryTypeCode())
            .isEqualTo(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryType())
            .isEqualTo(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_TYPE);
        assertThat(testCrbSubmittingInstitutionCategory.getSubmittingInstitutionCategoryDetails())
            .isEqualTo(UPDATED_SUBMITTING_INSTITUTION_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbSubmittingInstitutionCategory() throws Exception {
        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();
        crbSubmittingInstitutionCategory.setId(count.incrementAndGet());

        // Create the CrbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbSubmittingInstitutionCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(0)).save(crbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbSubmittingInstitutionCategory() throws Exception {
        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();
        crbSubmittingInstitutionCategory.setId(count.incrementAndGet());

        // Create the CrbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(0)).save(crbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbSubmittingInstitutionCategory() throws Exception {
        int databaseSizeBeforeUpdate = crbSubmittingInstitutionCategoryRepository.findAll().size();
        crbSubmittingInstitutionCategory.setId(count.incrementAndGet());

        // Create the CrbSubmittingInstitutionCategory
        CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryMapper.toDto(
            crbSubmittingInstitutionCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbSubmittingInstitutionCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbSubmittingInstitutionCategory in the database
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(0)).save(crbSubmittingInstitutionCategory);
    }

    @Test
    @Transactional
    void deleteCrbSubmittingInstitutionCategory() throws Exception {
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);

        int databaseSizeBeforeDelete = crbSubmittingInstitutionCategoryRepository.findAll().size();

        // Delete the crbSubmittingInstitutionCategory
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbSubmittingInstitutionCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbSubmittingInstitutionCategory> crbSubmittingInstitutionCategoryList = crbSubmittingInstitutionCategoryRepository.findAll();
        assertThat(crbSubmittingInstitutionCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbSubmittingInstitutionCategory in Elasticsearch
        verify(mockCrbSubmittingInstitutionCategorySearchRepository, times(1)).deleteById(crbSubmittingInstitutionCategory.getId());
    }

    @Test
    @Transactional
    void searchCrbSubmittingInstitutionCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbSubmittingInstitutionCategoryRepository.saveAndFlush(crbSubmittingInstitutionCategory);
        when(
            mockCrbSubmittingInstitutionCategorySearchRepository.search(
                "id:" + crbSubmittingInstitutionCategory.getId(),
                PageRequest.of(0, 20)
            )
        )
            .thenReturn(new PageImpl<>(Collections.singletonList(crbSubmittingInstitutionCategory), PageRequest.of(0, 1), 1));

        // Search the crbSubmittingInstitutionCategory
        restCrbSubmittingInstitutionCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbSubmittingInstitutionCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbSubmittingInstitutionCategory.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].submittingInstitutionCategoryTypeCode").value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].submittingInstitutionCategoryType").value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_TYPE)))
            .andExpect(
                jsonPath("$.[*].submittingInstitutionCategoryDetails")
                    .value(hasItem(DEFAULT_SUBMITTING_INSTITUTION_CATEGORY_DETAILS.toString()))
            );
    }
}
