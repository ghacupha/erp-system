package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.UltimateBeneficiaryCategory;
import io.github.erp.repository.UltimateBeneficiaryCategoryRepository;
import io.github.erp.repository.search.UltimateBeneficiaryCategorySearchRepository;
import io.github.erp.service.criteria.UltimateBeneficiaryCategoryCriteria;
import io.github.erp.service.dto.UltimateBeneficiaryCategoryDTO;
import io.github.erp.service.mapper.UltimateBeneficiaryCategoryMapper;
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
 * Integration tests for the {@link UltimateBeneficiaryCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UltimateBeneficiaryCategoryResourceIT {

    private static final String DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ULTIMATE_BENEFICIARY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMATE_BENEFICIARY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ultimate-beneficiary-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/ultimate-beneficiary-categories";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository;

    @Autowired
    private UltimateBeneficiaryCategoryMapper ultimateBeneficiaryCategoryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.UltimateBeneficiaryCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private UltimateBeneficiaryCategorySearchRepository mockUltimateBeneficiaryCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUltimateBeneficiaryCategoryMockMvc;

    private UltimateBeneficiaryCategory ultimateBeneficiaryCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UltimateBeneficiaryCategory createEntity(EntityManager em) {
        UltimateBeneficiaryCategory ultimateBeneficiaryCategory = new UltimateBeneficiaryCategory()
            .ultimateBeneficiaryCategoryTypeCode(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE)
            .ultimateBeneficiaryType(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryCategoryTypeDetails(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);
        return ultimateBeneficiaryCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UltimateBeneficiaryCategory createUpdatedEntity(EntityManager em) {
        UltimateBeneficiaryCategory ultimateBeneficiaryCategory = new UltimateBeneficiaryCategory()
            .ultimateBeneficiaryCategoryTypeCode(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE)
            .ultimateBeneficiaryType(UPDATED_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryCategoryTypeDetails(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);
        return ultimateBeneficiaryCategory;
    }

    @BeforeEach
    public void initTest() {
        ultimateBeneficiaryCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createUltimateBeneficiaryCategory() throws Exception {
        int databaseSizeBeforeCreate = ultimateBeneficiaryCategoryRepository.findAll().size();
        // Create the UltimateBeneficiaryCategory
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        UltimateBeneficiaryCategory testUltimateBeneficiaryCategory = ultimateBeneficiaryCategoryList.get(
            ultimateBeneficiaryCategoryList.size() - 1
        );
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeCode())
            .isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryType()).isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeDetails())
            .isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(1)).save(testUltimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void createUltimateBeneficiaryCategoryWithExistingId() throws Exception {
        // Create the UltimateBeneficiaryCategory with an existing ID
        ultimateBeneficiaryCategory.setId(1L);
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        int databaseSizeBeforeCreate = ultimateBeneficiaryCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(0)).save(ultimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void checkUltimateBeneficiaryCategoryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ultimateBeneficiaryCategoryRepository.findAll().size();
        // set the field null
        ultimateBeneficiaryCategory.setUltimateBeneficiaryCategoryTypeCode(null);

        // Create the UltimateBeneficiaryCategory, which fails.
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUltimateBeneficiaryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ultimateBeneficiaryCategoryRepository.findAll().size();
        // set the field null
        ultimateBeneficiaryCategory.setUltimateBeneficiaryType(null);

        // Create the UltimateBeneficiaryCategory, which fails.
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategories() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList
        restUltimateBeneficiaryCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ultimateBeneficiaryCategory.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryCategoryTypeCode").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryType").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryCategoryTypeDetails")
                    .value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS))
            );
    }

    @Test
    @Transactional
    void getUltimateBeneficiaryCategory() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get the ultimateBeneficiaryCategory
        restUltimateBeneficiaryCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, ultimateBeneficiaryCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ultimateBeneficiaryCategory.getId().intValue()))
            .andExpect(jsonPath("$.ultimateBeneficiaryCategoryTypeCode").value(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE))
            .andExpect(jsonPath("$.ultimateBeneficiaryType").value(DEFAULT_ULTIMATE_BENEFICIARY_TYPE))
            .andExpect(
                jsonPath("$.ultimateBeneficiaryCategoryTypeDetails").value(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS)
            );
    }

    @Test
    @Transactional
    void getUltimateBeneficiaryCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        Long id = ultimateBeneficiaryCategory.getId();

        defaultUltimateBeneficiaryCategoryShouldBeFound("id.equals=" + id);
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultUltimateBeneficiaryCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultUltimateBeneficiaryCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryCategoryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode equals to DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldBeFound(
            "ultimateBeneficiaryCategoryTypeCode.equals=" + DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode equals to UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound(
            "ultimateBeneficiaryCategoryTypeCode.equals=" + UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryCategoryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode not equals to DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound(
            "ultimateBeneficiaryCategoryTypeCode.notEquals=" + DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode not equals to UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldBeFound(
            "ultimateBeneficiaryCategoryTypeCode.notEquals=" + UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryCategoryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode in DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE or UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldBeFound(
            "ultimateBeneficiaryCategoryTypeCode.in=" +
            DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE +
            "," +
            UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode equals to UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound(
            "ultimateBeneficiaryCategoryTypeCode.in=" + UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryCategoryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode is not null
        defaultUltimateBeneficiaryCategoryShouldBeFound("ultimateBeneficiaryCategoryTypeCode.specified=true");

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode is null
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("ultimateBeneficiaryCategoryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryCategoryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode contains DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldBeFound(
            "ultimateBeneficiaryCategoryTypeCode.contains=" + DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode contains UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound(
            "ultimateBeneficiaryCategoryTypeCode.contains=" + UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryCategoryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode does not contain DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound(
            "ultimateBeneficiaryCategoryTypeCode.doesNotContain=" + DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryCategoryTypeCode does not contain UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        defaultUltimateBeneficiaryCategoryShouldBeFound(
            "ultimateBeneficiaryCategoryTypeCode.doesNotContain=" + UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType equals to DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldBeFound("ultimateBeneficiaryType.equals=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("ultimateBeneficiaryType.equals=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType not equals to DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("ultimateBeneficiaryType.notEquals=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType not equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldBeFound("ultimateBeneficiaryType.notEquals=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType in DEFAULT_ULTIMATE_BENEFICIARY_TYPE or UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldBeFound(
            "ultimateBeneficiaryType.in=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE + "," + UPDATED_ULTIMATE_BENEFICIARY_TYPE
        );

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType equals to UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("ultimateBeneficiaryType.in=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType is not null
        defaultUltimateBeneficiaryCategoryShouldBeFound("ultimateBeneficiaryType.specified=true");

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType is null
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("ultimateBeneficiaryType.specified=false");
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryTypeContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType contains DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldBeFound("ultimateBeneficiaryType.contains=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType contains UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("ultimateBeneficiaryType.contains=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    @Test
    @Transactional
    void getAllUltimateBeneficiaryCategoriesByUltimateBeneficiaryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType does not contain DEFAULT_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldNotBeFound("ultimateBeneficiaryType.doesNotContain=" + DEFAULT_ULTIMATE_BENEFICIARY_TYPE);

        // Get all the ultimateBeneficiaryCategoryList where ultimateBeneficiaryType does not contain UPDATED_ULTIMATE_BENEFICIARY_TYPE
        defaultUltimateBeneficiaryCategoryShouldBeFound("ultimateBeneficiaryType.doesNotContain=" + UPDATED_ULTIMATE_BENEFICIARY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUltimateBeneficiaryCategoryShouldBeFound(String filter) throws Exception {
        restUltimateBeneficiaryCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ultimateBeneficiaryCategory.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryCategoryTypeCode").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryType").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryCategoryTypeDetails")
                    .value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS))
            );

        // Check, that the count call also returns 1
        restUltimateBeneficiaryCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUltimateBeneficiaryCategoryShouldNotBeFound(String filter) throws Exception {
        restUltimateBeneficiaryCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUltimateBeneficiaryCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUltimateBeneficiaryCategory() throws Exception {
        // Get the ultimateBeneficiaryCategory
        restUltimateBeneficiaryCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUltimateBeneficiaryCategory() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();

        // Update the ultimateBeneficiaryCategory
        UltimateBeneficiaryCategory updatedUltimateBeneficiaryCategory = ultimateBeneficiaryCategoryRepository
            .findById(ultimateBeneficiaryCategory.getId())
            .get();
        // Disconnect from session so that the updates on updatedUltimateBeneficiaryCategory are not directly saved in db
        em.detach(updatedUltimateBeneficiaryCategory);
        updatedUltimateBeneficiaryCategory
            .ultimateBeneficiaryCategoryTypeCode(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE)
            .ultimateBeneficiaryType(UPDATED_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryCategoryTypeDetails(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            updatedUltimateBeneficiaryCategory
        );

        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ultimateBeneficiaryCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);
        UltimateBeneficiaryCategory testUltimateBeneficiaryCategory = ultimateBeneficiaryCategoryList.get(
            ultimateBeneficiaryCategoryList.size() - 1
        );
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeCode())
            .isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryType()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeDetails())
            .isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository).save(testUltimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void putNonExistingUltimateBeneficiaryCategory() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();
        ultimateBeneficiaryCategory.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryCategory
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ultimateBeneficiaryCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(0)).save(ultimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchUltimateBeneficiaryCategory() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();
        ultimateBeneficiaryCategory.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryCategory
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(0)).save(ultimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUltimateBeneficiaryCategory() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();
        ultimateBeneficiaryCategory.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryCategory
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(0)).save(ultimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void partialUpdateUltimateBeneficiaryCategoryWithPatch() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();

        // Update the ultimateBeneficiaryCategory using partial update
        UltimateBeneficiaryCategory partialUpdatedUltimateBeneficiaryCategory = new UltimateBeneficiaryCategory();
        partialUpdatedUltimateBeneficiaryCategory.setId(ultimateBeneficiaryCategory.getId());

        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUltimateBeneficiaryCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUltimateBeneficiaryCategory))
            )
            .andExpect(status().isOk());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);
        UltimateBeneficiaryCategory testUltimateBeneficiaryCategory = ultimateBeneficiaryCategoryList.get(
            ultimateBeneficiaryCategoryList.size() - 1
        );
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeCode())
            .isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryType()).isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeDetails())
            .isEqualTo(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateUltimateBeneficiaryCategoryWithPatch() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();

        // Update the ultimateBeneficiaryCategory using partial update
        UltimateBeneficiaryCategory partialUpdatedUltimateBeneficiaryCategory = new UltimateBeneficiaryCategory();
        partialUpdatedUltimateBeneficiaryCategory.setId(ultimateBeneficiaryCategory.getId());

        partialUpdatedUltimateBeneficiaryCategory
            .ultimateBeneficiaryCategoryTypeCode(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE)
            .ultimateBeneficiaryType(UPDATED_ULTIMATE_BENEFICIARY_TYPE)
            .ultimateBeneficiaryCategoryTypeDetails(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);

        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUltimateBeneficiaryCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUltimateBeneficiaryCategory))
            )
            .andExpect(status().isOk());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);
        UltimateBeneficiaryCategory testUltimateBeneficiaryCategory = ultimateBeneficiaryCategoryList.get(
            ultimateBeneficiaryCategoryList.size() - 1
        );
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeCode())
            .isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryType()).isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_TYPE);
        assertThat(testUltimateBeneficiaryCategory.getUltimateBeneficiaryCategoryTypeDetails())
            .isEqualTo(UPDATED_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingUltimateBeneficiaryCategory() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();
        ultimateBeneficiaryCategory.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryCategory
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ultimateBeneficiaryCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(0)).save(ultimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUltimateBeneficiaryCategory() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();
        ultimateBeneficiaryCategory.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryCategory
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(0)).save(ultimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUltimateBeneficiaryCategory() throws Exception {
        int databaseSizeBeforeUpdate = ultimateBeneficiaryCategoryRepository.findAll().size();
        ultimateBeneficiaryCategory.setId(count.incrementAndGet());

        // Create the UltimateBeneficiaryCategory
        UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryMapper.toDto(
            ultimateBeneficiaryCategory
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUltimateBeneficiaryCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ultimateBeneficiaryCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UltimateBeneficiaryCategory in the database
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(0)).save(ultimateBeneficiaryCategory);
    }

    @Test
    @Transactional
    void deleteUltimateBeneficiaryCategory() throws Exception {
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);

        int databaseSizeBeforeDelete = ultimateBeneficiaryCategoryRepository.findAll().size();

        // Delete the ultimateBeneficiaryCategory
        restUltimateBeneficiaryCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, ultimateBeneficiaryCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UltimateBeneficiaryCategory> ultimateBeneficiaryCategoryList = ultimateBeneficiaryCategoryRepository.findAll();
        assertThat(ultimateBeneficiaryCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UltimateBeneficiaryCategory in Elasticsearch
        verify(mockUltimateBeneficiaryCategorySearchRepository, times(1)).deleteById(ultimateBeneficiaryCategory.getId());
    }

    @Test
    @Transactional
    void searchUltimateBeneficiaryCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        ultimateBeneficiaryCategoryRepository.saveAndFlush(ultimateBeneficiaryCategory);
        when(mockUltimateBeneficiaryCategorySearchRepository.search("id:" + ultimateBeneficiaryCategory.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ultimateBeneficiaryCategory), PageRequest.of(0, 1), 1));

        // Search the ultimateBeneficiaryCategory
        restUltimateBeneficiaryCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ultimateBeneficiaryCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ultimateBeneficiaryCategory.getId().intValue())))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryCategoryTypeCode").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_CODE))
            )
            .andExpect(jsonPath("$.[*].ultimateBeneficiaryType").value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_TYPE)))
            .andExpect(
                jsonPath("$.[*].ultimateBeneficiaryCategoryTypeDetails")
                    .value(hasItem(DEFAULT_ULTIMATE_BENEFICIARY_CATEGORY_TYPE_DETAILS))
            );
    }
}
